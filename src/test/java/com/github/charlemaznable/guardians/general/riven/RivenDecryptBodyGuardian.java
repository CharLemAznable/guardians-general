package com.github.charlemaznable.guardians.general.riven;

import com.github.charlemaznable.guardians.general.Decryption.DecryptedTextPostProcessor;
import com.github.charlemaznable.guardians.general.DecryptionAbstractGuardian;
import com.github.charlemaznable.guardians.general.exception.DecryptionGuardianException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.core.lang.Condition.checkNotNull;
import static com.github.charlemaznable.core.net.Http.errorText;
import static com.github.charlemaznable.core.spring.MutableHttpServletElf.mutableRequest;
import static com.github.charlemaznable.guardians.spring.GuardianContext.request;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
@Component
public class RivenDecryptBodyGuardian implements DecryptionAbstractGuardian {

    @Override
    public void handleGuardianException(HttpServletRequest request, HttpServletResponse response,
                                        DecryptionGuardianException exception) {
        log.error("RivenRequestBodyGuardian handled exception: ", exception);
        errorText(response, FORBIDDEN.value(), FORBIDDEN.getReasonPhrase());
    }

    public static class RivenDecryptedProcessor implements DecryptedTextPostProcessor {

        @Override
        public void processDecryptedText(String decryptedText) {
            checkNotNull(mutableRequest(request())).setRequestBody(decryptedText);
        }
    }
}
