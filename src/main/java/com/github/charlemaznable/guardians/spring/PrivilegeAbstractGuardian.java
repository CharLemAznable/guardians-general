package com.github.charlemaznable.guardians.spring;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.Privilege;
import com.github.charlemaznable.guardians.general.Privilege.AccessPrivilegeSupplier;
import com.github.charlemaznable.guardians.general.exception.PrivilegeGuardianException;
import lombok.val;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.github.charlemaznable.lang.Condition.checkNotNull;
import static com.github.charlemaznable.lang.Condition.nullThen;
import static com.github.charlemaznable.lang.Listt.newArrayList;
import static org.joor.Reflect.onClass;

public abstract class PrivilegeAbstractGuardian {

    @Guard(true)
    public boolean preGuard(Privilege privilegeAnnotation) {
        checkNotNull(privilegeAnnotation, new PrivilegeGuardianException(
                "Missing Annotation: " + Privilege.class.getName()));

        val privileges = newArrayList(privilegeAnnotation.allow());

        List<String> accessPrivileges = newArrayList();
        val accessSuppliers = nullThen(privilegeAnnotation.accessSuppliers(), () -> new Class[0]);
        for (val accessSupplier : accessSuppliers) {
            AccessPrivilegeSupplier supplier = onClass(accessSupplier).create().get();
            accessPrivileges.addAll(newArrayList(supplier.get()));
        }
        return checkPrivilege(privileges, accessPrivileges);
    }

    @Guard(true)
    public void postGuard(HttpServletRequest request,
                          HttpServletResponse response,
                          PrivilegeGuardianException exception) {
        if (null == exception) return;
        handlePrivilegeException(request, response, exception);
    }

    public abstract boolean checkPrivilege(List<String> privileges,
                                           List<String> accessPrivileges);

    public abstract void handlePrivilegeException(HttpServletRequest request,
                                                  HttpServletResponse response,
                                                  PrivilegeGuardianException exception);
}
