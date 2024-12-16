package com.modsen.ridesservice.configuration.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class KeycloakJwtTokenConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String AZP_CLAIM = "azp";
    private static final String AZP_CLAIM_VALUE = "admin-cli";
    private static final String REALM_ACCESS_CLAIM =  "realm_access";
    private static final String REALM_ACCESS_CLAIM_VALUE = "roles";
    private static final String ROLE_PREFIX = "ROLE_";
    private static final String ROLE_ADMIN_VALUE = "ROLE_ADMIN";

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        if(jwt.getClaim(AZP_CLAIM).equals(AZP_CLAIM_VALUE)) {
            return Collections.singletonList(new SimpleGrantedAuthority(ROLE_ADMIN_VALUE));
        }
        var realmAccessMap = jwt.getClaimAsMap(REALM_ACCESS_CLAIM);
        Object rolesObject = realmAccessMap.get(REALM_ACCESS_CLAIM_VALUE);

        if (rolesObject instanceof List<?>) {
            List<String> realmAccess = ((List<?>) rolesObject).stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .toList();

            return realmAccess.stream()
                    .map(role -> ROLE_PREFIX + role)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

}
