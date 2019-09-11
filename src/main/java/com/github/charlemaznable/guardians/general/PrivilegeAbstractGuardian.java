package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.exception.PrivilegeGuardianException;
import lombok.val;

import java.util.List;

import static com.github.charlemaznable.core.lang.Condition.checkNotNull;
import static com.github.charlemaznable.core.lang.Listt.newArrayList;
import static com.github.charlemaznable.guardians.general.utils.SpringUtils.getOrCreateBean;

public abstract class PrivilegeAbstractGuardian implements PostGuardExceptionHandler<PrivilegeGuardianException> {

    @Guard(true)
    public boolean preGuard(Privilege privilegeAnnotation) {
        checkNotNull(privilegeAnnotation, new PrivilegeGuardianException(
                "Missing Annotation: " + Privilege.class.getName()));

        val privileges = newArrayList(privilegeAnnotation.allow());

        List<String> accessPrivileges = newArrayList();
        val privilegesSuppliers = privilegeAnnotation.privilegesSuppliers();
        for (val privilegesSupplier : privilegesSuppliers) {
            val supplier = getOrCreateBean(privilegesSupplier);
            accessPrivileges.addAll(newArrayList(supplier.supplyAccessPrivileges()));
        }
        return checkPrivilege(privileges, accessPrivileges);
    }

    public abstract boolean checkPrivilege(List<String> privileges,
                                           List<String> accessPrivileges);
}