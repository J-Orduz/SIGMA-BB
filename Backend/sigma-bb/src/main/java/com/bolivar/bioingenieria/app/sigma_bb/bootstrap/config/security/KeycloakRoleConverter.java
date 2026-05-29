package com.bolivar.bioingenieria.app.sigma_bb.bootstrap.config.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String CLIENT_ID = "sigma-api";

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {

        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");

        if (resourceAccess == null || !resourceAccess.containsKey(CLIENT_ID)) {
            return Collections.emptyList();
        }

        Map<String, Object> client = (Map<String, Object>) resourceAccess.get(CLIENT_ID);

        List<String> roles = (List<String>) client.get("roles");

        if (roles == null) {
            return Collections.emptyList();
        }

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .map(GrantedAuthority.class::cast)
                .toList();
    }
}
