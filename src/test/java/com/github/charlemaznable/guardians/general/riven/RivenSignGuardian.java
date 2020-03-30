package com.github.charlemaznable.guardians.general.riven;

import com.github.charlemaznable.guardians.general.Signature.PlainTextBuilder;
import com.github.charlemaznable.guardians.general.SignatureAbstractGuardian;
import com.github.charlemaznable.guardians.general.exception.SignatureGuardianException;
import com.github.charlemaznable.guardians.utils.RequestBodyFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.core.lang.Condition.checkNotNull;
import static com.github.charlemaznable.core.net.Http.errorText;
import static com.github.charlemaznable.core.spring.MutableHttpServletUtils.mutableRequest;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
@Component
public class RivenSignGuardian implements SignatureAbstractGuardian {

    @Override
    public void handleGuardianException(HttpServletRequest request, HttpServletResponse response,
                                        SignatureGuardianException exception) {
        log.error("RivenSignGuardian handled exception: ", exception);
        errorText(response, FORBIDDEN.value(), FORBIDDEN.getReasonPhrase());
    }

    public static class RivenSignPlainTextBuilder implements PlainTextBuilder {

        @Override
        public String buildPlainText(HttpServletRequest request, RequestBodyFormat bodyFormat, String charsetName) {
            return checkNotNull(mutableRequest(request)).getRequestBody();
        }
    }
}
