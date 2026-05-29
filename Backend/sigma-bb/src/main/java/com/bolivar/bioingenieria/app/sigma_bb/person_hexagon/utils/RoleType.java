package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {
    ENGINEER("ENGINEER"),
    ADMIN("ADMIN"),
    CEO_CLIENT("CEO_CLIENT");

    private final String name;
}
