package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.exception.PrivilegeGuardianException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;

import java.util.List;

import static com.github.charlemaznable.core.lang.Condition.checkNotNull;
import static com.github.charlemaznable.core.lang.Listt.newArrayList;
import static com.github.charlemaznable.core.spring.SpringContext.getBeanOrCreate;
import static java.util.Objects.isNull;

public interface PrivilegeAbstractGuardian {

    @Guard(true)
    default boolean preGuard(Privilege privilegeAnnotation) {
        checkNotNull(privilegeAnnotation, new PrivilegeGuardianException(
                "Missing Annotation: " + Privilege.class.getName()));

        val privileges = newArrayList(privilegeAnnotation.allow());

        List<String> accessPrivileges = newArrayList();
        val privilegesSuppliers = privilegeAnnotation.privilegesSuppliers();
        for (val privilegesSupplier : privilegesSuppliers) {
            val supplier = getBeanOrCreate(privilegesSupplier);
            accessPrivileges.addAll(newArrayList(supplier.supplyAccessPrivileges()));
        }
        return checkPrivilege(privileges, accessPrivileges);
    }

    boolean checkPrivilege(List<String> privileges,
                           List<String> accessPrivileges);

    @Guard(true)
    default void postGuard(HttpServletRequest request,
                           HttpServletResponse response,
                           PrivilegeGuardianException exception) {
        if (isNull(exception)) return;
        handleGuardianException(request, response, exception);
    }

    void handleGuardianException(HttpServletRequest request,
                                 HttpServletResponse response,
                                 PrivilegeGuardianException exception);
}
