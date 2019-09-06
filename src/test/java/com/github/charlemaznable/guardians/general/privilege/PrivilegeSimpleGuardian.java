package com.github.charlemaznable.guardians.general.privilege;

import com.github.charlemaznable.guardians.general.exception.PrivilegeGuardianException;
import com.github.charlemaznable.guardians.spring.PrivilegeAbstractGuardian;
import com.github.charlemaznable.core.spring.MutableHttpServletUtils;
import lombok.val;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.codec.Json.unJson;
import static com.github.charlemaznable.core.lang.Mapp.newHashMap;

@Component
public class PrivilegeSimpleGuardian extends PrivilegeAbstractGuardian {

    @Override
    public boolean checkPrivilege(List<String> privileges,
                                  List<String> accessPrivileges) {
        accessPrivileges.retainAll(privileges);
        if (!accessPrivileges.isEmpty()) return true;
        throw new PrivilegeGuardianException("Access has been Denied");
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void handleGuardianException(HttpServletRequest request,
                                        HttpServletResponse response,
                                        PrivilegeGuardianException exception) {
        MutableHttpServletUtils.mutateResponse(response, mutableResponse -> {
            val contentAsString = mutableResponse.getContentAsString();
            val contentMap = newHashMap(unJson(contentAsString));
            contentMap.put("error", exception.getMessage());
            mutableResponse.setContentByString(json(contentMap));
        });
    }
}
