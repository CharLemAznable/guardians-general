package com.github.charlemaznable.guardians.spring;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.AccessId;
import com.github.charlemaznable.guardians.general.AccessId.AccessIdPostFunction;
import com.github.charlemaznable.guardians.general.exception.AccessIdGuardianException;
import lombok.val;
import lombok.var;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractor.RequestBodyParser.Form;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractType.Parameter;
import static com.github.charlemaznable.lang.Condition.blankThen;
import static com.github.charlemaznable.lang.Condition.checkNotBlank;
import static com.github.charlemaznable.lang.Condition.checkNotNull;
import static com.github.charlemaznable.lang.Condition.nullThen;
import static com.google.common.base.Charsets.UTF_8;
import static org.joor.Reflect.onClass;

public abstract class AccessIdAbstractGuardian {

    @Guard(true)
    public boolean preGuard(AccessId accessIdAnnotation) {
        checkNotNull(accessIdAnnotation, new AccessIdGuardianException(
                "Missing Annotation: " + AccessId.class.getName()));

        val keyName = blankThen(accessIdAnnotation.keyName(), () -> "accessId");
        val extractorType = nullThen(accessIdAnnotation.extractorType(), () -> Parameter);
        val bodyParser = nullThen(accessIdAnnotation.bodyParser(), () -> Form);
        val charsetName = blankThen(accessIdAnnotation.charsetName(), UTF_8::name);
        val extractor = extractorType.extractor(keyName, bodyParser, charsetName);

        var accessId = checkNotBlank(extractor.apply(GuardianContext.request()),
                new AccessIdGuardianException("Missing Access Identification: " + keyName));
        val postFunctions = nullThen(accessIdAnnotation.postFunctions(), () -> new Class[0]);
        for (val postFunction : postFunctions) {
            AccessIdPostFunction function = onClass(postFunction).create().get();
            accessId = function.apply(accessId);
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
