package com.github.charlemaznable.guardians.general.privilege;

import com.github.charlemaznable.guardians.general.Privilege.AccessPrivilegeSupplier;
import com.github.charlemaznable.guardians.spring.GuardianContext;
import com.github.charlemaznable.lang.Mapp;
import lombok.val;

import java.util.List;
import java.util.Map;

import static com.github.charlemaznable.lang.Listt.newArrayList;

public class PrivilegeSimpleSupplier implements AccessPrivilegeSupplier {

    private static final Map<String, List<String>> mockPrivilegesMap = Mapp.of(
            "admin", newArrayList("管理员", "员工", "游客"),
            "user", newArrayList("员工", "游客"),
            "guest", newArrayList("游客")
    );

    @Override
    public String[] get() {
        val accessId = GuardianContext.request().getParameter("accessId");
        val accessPrivileges = newArrayList(mockPrivilegesMap.get(accessId));
        return accessPrivileges.toArray(new String[0]);
    }
}
