package com.modsen.registrationservice.service.impl;

import com.modsen.registrationservice.client.driver.DriverFeignClient;
import com.modsen.registrationservice.client.passenger.PassengerFeignClient;
import com.modsen.registrationservice.configuration.KeycloakProperties;
import com.modsen.registrationservice.dto.SignInAdminDto;
import com.modsen.registrationservice.dto.SignInDto;
import com.modsen.registrationservice.dto.SignUpDto;
import com.modsen.registrationservice.exception.ApiExceptionDto;
import com.modsen.registrationservice.exception.keycloak.KeycloakCreateUserException;
import com.modsen.registrationservice.service.UserManagementService;
import com.modsen.registrationservice.service.ServiceConstants;
import jakarta.ws.rs.ServiceUnavailableException;
import jakarta.ws.rs.core.Response;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.modsen.registrationservice.constants.AppConstants.SERVICE_UNAVAILABLE_MESSAGE_KEY;
import static com.modsen.registrationservice.service.ServiceConstants.CLIENT_ID_FIELD;
import static com.modsen.registrationservice.service.ServiceConstants.CLIENT_SECRET_FIELD;
import static com.modsen.registrationservice.service.ServiceConstants.GRANT_TYPE_CLIENT_CREDENTIALS_FIELD;
import static com.modsen.registrationservice.service.ServiceConstants.GRANT_TYPE_FIELD;
import static com.modsen.registrationservice.service.ServiceConstants.GRANT_TYPE_PASSWORD_FIELD;
import static com.modsen.registrationservice.service.ServiceConstants.PASSENGER_ROLE;
import static com.modsen.registrationservice.service.ServiceConstants.PASSWORD_FIELD;
import static com.modsen.registrationservice.service.ServiceConstants.USERNAME_FIELD;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserManagementServiceImpl implements UserManagementService {

    private final Keycloak keycloak;
    private final RestTemplate restTemplate;
    private final KeycloakProperties keycloakProperties;
    private final DriverFeignClient driverFeignClient;
    private final PassengerFeignClient passengerFeignClient;
    private final MessageSource messageSource;

    @Override
    public void signUp(SignUpDto signUpDto) {
        UserRepresentation keycloakUser = getUserRepresentation(signUpDto);
        RealmResource realmResource = keycloak.realm(keycloakProperties.getRealm());
        UsersResource usersResource = realmResource.users();
        String adminClientAccessToken = keycloak.tokenManager().getAccessTokenString();
        Response response;
        try {
            response = Optional.ofNullable(usersResource.create(keycloakUser))
                    .orElseThrow();
        } catch (Exception e) {
            throw new ServiceUnavailableException(messageSource.getMessage(
                    SERVICE_UNAVAILABLE_MESSAGE_KEY,
                    new Object[]{},
                    LocaleContextHolder.getLocale()));
        }
        if (response.getStatus() == HttpStatus.CREATED.value()) {
            try {
                if (Objects.equals(signUpDto.role(), PASSENGER_ROLE)) {
                    passengerFeignClient.createPassenger(signUpDto, LocaleContextHolder.getLocale().toLanguageTag(),
                            "Bearer " + adminClientAccessToken);
                } else {
                    driverFeignClient.createDriver(signUpDto, LocaleContextHolder.getLocale().toLanguageTag(),
                            "Bearer " + adminClientAccessToken);
                }
            } catch (Exception exception) {
                usersResource.delete(CreatedResponseUtil.getCreatedId(response));
                throw exception;
            }
        } else {
            throw new KeycloakCreateUserException(
                    new ApiExceptionDto(HttpStatus.valueOf(response.getStatus()),
                            readResponseBody(response),
                            LocalDateTime.now()));
        }
        RolesResource rolesResource = realmResource.roles();
        RoleRepresentation role = rolesResource.get(signUpDto.role()).toRepresentation();
        UserResource userById = usersResource.get(CreatedResponseUtil.getCreatedId(response));
        userById.roles()
                .realmLevel()
                .add(List.of(role));
    }

    @Override
    public ResponseEntity<String> signIn(SignInDto signInDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(GRANT_TYPE_FIELD, GRANT_TYPE_PASSWORD_FIELD);
        body.add(USERNAME_FIELD, signInDto.email());
        body.add(PASSWORD_FIELD, signInDto.password());
        body.add(CLIENT_ID_FIELD, keycloakProperties.getAuth().getClientId());

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        return getResponseWithToken(requestEntity);
    }

    @Override
    public ResponseEntity<String> signInAsAdmin(SignInAdminDto signInDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(GRANT_TYPE_FIELD, GRANT_TYPE_CLIENT_CREDENTIALS_FIELD);
        body.add(CLIENT_ID_FIELD, keycloakProperties.getUserManagement().getClientId());
        body.add(CLIENT_SECRET_FIELD, keycloakProperties.getUserManagement().getClientSecret());

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        return getResponseWithToken(requestEntity);
    }

    private String getAuthUrl() {
        return keycloakProperties.getUserManagement().getServerUrl() +
                "/realms/" + keycloakProperties.getRealm() +
                "/protocol/openid-connect/token";
    }

    private ResponseEntity<String> getResponseWithToken(HttpEntity<MultiValueMap<String, String>> requestEntity) {
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(getAuthUrl(), HttpMethod.POST, requestEntity, String.class);
        } catch (HttpClientErrorException exception) {
            throw new KeycloakCreateUserException(
                    new ApiExceptionDto(HttpStatus.valueOf(exception.getStatusCode().value()),
                            exception.getMessage(),
                            LocalDateTime.now()));
        }
        return response;
    }

    private UserRepresentation getUserRepresentation(SignUpDto signUpDto) {
        UserRepresentation keycloakUser = new UserRepresentation();

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(signUpDto.password());

        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put(ServiceConstants.GENDER_ATTRIBUTE_FIELD, List.of(signUpDto.gender()));

        keycloakUser.setFirstName(signUpDto.firstName());
        keycloakUser.setLastName(signUpDto.lastName());
        keycloakUser.setEmail(signUpDto.email());
        keycloakUser.setCredentials(List.of(credential));
        keycloakUser.setEnabled(true);
        keycloakUser.setAttributes(attributes);
        return keycloakUser;
    }

    @SneakyThrows
    private String readResponseBody(Response response) {
        if (Objects.nonNull(response)) {
            @Cleanup InputStreamReader inputStreamReader = new InputStreamReader(response.readEntity(InputStream.class),
                    StandardCharsets.UTF_8);
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString()
                    .replace("\"errorMessage\":", "")
                    .replace("\"", "");
        }
        return "";
    }

}
