package com.github.charlemaznable.guardians.spring;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.AccessId;
import com.github.charlemaznable.guardians.general.exception.AccessIdGuardianException;
import com.github.charlemaznable.guardians.general.utils.SpringUtils;
import lombok.val;
import lombok.var;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.lang.Condition.blankThen;
import static com.github.charlemaznable.lang.Condition.checkNotBlank;
import static com.github.charlemaznable.lang.Condition.checkNotNull;
import static com.google.common.base.Charsets.UTF_8;

public abstract class AccessIdAbstractGuardian {

    @Guard(true)
    public boolean preGuard(AccessId accessIdAnnotation) {
        checkNotNull(accessIdAnnotation, new AccessIdGuardianException(
                "Missing Annotation: " + AccessId.class.getName()));

        val keyName = blankThen(accessIdAnnotation.keyName(), () -> "accessId");
        val extractorType = accessIdAnnotation.extractorType();
        val bodyFormat = accessIdAnnotation.bodyFormat();
        val charsetName = blankThen(accessIdAnnotation.charsetName(), UTF_8::name);
        val extractor = extractorType.extractor(keyName, bodyFormat, charsetName);
        var accessId = checkNotBlank(extractor.extract(GuardianContext.request()),
                new AccessIdGuardianException("Missing Access Identification: " + keyName));

        val postProcessors = accessIdAnnotation.postProcessors();
        for (val postProcessor : postProcessors) {
            val processor = SpringUtils.getOrCreateBean(postProcessor);
            accessId = processor.processAccessId(accessId);
        }

        return checkAccessId(accessId);
    }

    @Guard(true)
    public void postGuard(HttpServletRequest request,
                          HttpServletResponse response,
                          AccessIdGuardianException exception) {
        if (null == exception) return;
        handleAccessIdException(request, response, exception);
    }

    public abstract boolean checkAccessId(String accessId);

    public abstract void handleAccessIdException(HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 AccessIdGuardianException exception);
}
