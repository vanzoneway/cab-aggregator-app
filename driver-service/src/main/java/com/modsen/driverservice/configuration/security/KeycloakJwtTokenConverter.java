package com.modsen.driverservice.configuration.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class KeycloakJwtTokenConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        if(jwt.getClaim("azp").equals("admin-cli")) {
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        var realmAccessMap = jwt.getClaimAsMap("realm_access");
        Object rolesObject = realmAccessMap.get("roles");

        if (rolesObject instanceof List<?>) {
            List<String> realmAccess = ((List<?>) rolesObject).stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .toList();

            return realmAccess.stream()
                    .map(role -> "ROLE_" + role)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

}
