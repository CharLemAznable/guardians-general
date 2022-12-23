package com.github.charlemaznable.guardians.general.riven;

import com.github.charlemaznable.guardians.general.Decryption.DecryptionKeySupplier;
import com.github.charlemaznable.guardians.general.RequestValidate;
import com.github.charlemaznable.guardians.general.RequestValidateAbstractGuardian;
import com.github.charlemaznable.guardians.general.Signature.SignatureKeySupplier;
import com.github.charlemaznable.guardians.general.exception.RequestValidateGuardianException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.github.charlemaznable.core.lang.Condition.checkNotBlank;
import static com.github.charlemaznable.core.lang.Condition.checkNotNull;
import static com.github.charlemaznable.core.lang.Mapp.getStr;
import static com.github.charlemaznable.core.lang.Str.toStr;
import static com.github.charlemaznable.core.net.Http.errorText;
import static com.github.charlemaznable.guardians.spring.GuardianContext.get;
import static com.github.charlemaznable.guardians.spring.GuardianContext.set;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
@Component
public class RivenAppIdGuardian implements RequestValidateAbstractGuardian {

    private static final String RIVEN_APP_ID_KEY = "RivenAppId";
    private static final String RIVEN_APP_PRV_KEY = "RivenAppPrvKey";
    private static final String RIVEN_APP_SIGN_KEY = "RivenAppSignKey";

    @Autowired
    private AppDao appDao;

    @SuppressWarnings("unchecked")
    @Override
    public boolean validateRequest(RequestValidate requestValidate,
                                   Object requestValidateObject) {
        Map<String, Object> valueMap = (Map<String, Object>) requestValidateObject;
        val appId = checkNotBlank(getStr(valueMap, "App-Id"),
                new RequestValidateGuardianException("Missing AppId"));
        val app = checkNotNull(appDao.queryApp(appId),
                new RequestValidateGuardianException("Illegal App"));

        set(RIVEN_APP_ID_KEY, appId);
        set(RIVEN_APP_PRV_KEY, app.getPrvKey());
        set(RIVEN_APP_SIGN_KEY, app.getSignKey());
        return true;
    }

    @Override
    public void handleGuardianException(HttpServletRequest request, HttpServletResponse response,
                                        RequestValidateGuardianException exception) {
        log.error("RivenRequestFieldsGuardian handled exception: ", exception);
        errorText(response, FORBIDDEN.value(), FORBIDDEN.getReasonPhrase());
    }

    public static class RivenDecryptKeySupplier implements DecryptionKeySupplier {

        @Override
        public String supplyDecryptionKey() {
            return toStr(get(RIVEN_APP_PRV_KEY));
        }
    }

    public static class RivenSignKeySupplier implements SignatureKeySupplier {

        @Override
        public String supplySignatureKey() {
            return toStr(get(RIVEN_APP_SIGN_KEY));
        }
    }
}
