package com.github.charlemaznable.guardians.spring;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.Privilege;
import com.github.charlemaznable.guardians.general.exception.PrivilegeGuardianException;
import com.github.charlemaznable.guardians.general.utils.SpringUtils;
import lombok.val;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.github.charlemaznable.lang.Condition.checkNotNull;
import static com.github.charlemaznable.lang.Listt.newArrayList;

public abstract class PrivilegeAbstractGuardian {

    @Guard(true)
    public boolean preGuard(Privilege privilegeAnnotation) {
        checkNotNull(privilegeAnnotation, new PrivilegeGuardianException(
                "Missing Annotation: " + Privilege.class.getName()));

        val privileges = newArrayList(privilegeAnnotation.allow());

        List<String> accessPrivileges = newArrayList();
        val privilegesSuppliers = privilegeAnnotation.privilegesSuppliers();
        for (val privilegesSupplier : privilegesSuppliers) {
            val supplier = SpringUtils.getOrCreateBean(privilegesSupplier);
            accessPrivileges.addAll(newArrayList(supplier.supplyAccessPrivileges()));
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
