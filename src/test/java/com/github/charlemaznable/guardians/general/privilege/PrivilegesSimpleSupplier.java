package com.github.charlemaznable.guardians.general.privilege;

import com.github.charlemaznable.core.lang.Mapp;
import com.github.charlemaznable.guardians.general.Privilege.AccessPrivilegesSupplier;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.github.charlemaznable.core.lang.Listt.newArrayList;
import static com.github.charlemaznable.guardians.spring.GuardianContext.request;

@Component
public class PrivilegesSimpleSupplier implements AccessPrivilegesSupplier {

    private static final Map<String, List<String>> mockPrivilegesMap = Mapp.of(
            "admin", newArrayList("管理员", "员工", "游客"),
            "user", newArrayList("员工", "游客"),
            "guest", newArrayList("游客")
    );

    @Override
    public String[] supplyAccessPrivileges() {
        val accessId = request().getParameter("accessId");
        val accessPrivileges = newArrayList(mockPrivilegesMap.get(accessId));
        return accessPrivileges.toArray(new String[0]);
    }
}
