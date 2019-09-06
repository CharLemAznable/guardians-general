package com.github.charlemaznable.guardians.general.requestFields;

import com.github.charlemaznable.guardians.PostGuardian;
import com.github.charlemaznable.guardians.PreGuardian;
import com.github.charlemaznable.guardians.general.RequestField;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractor.RequestBodyFormat.Json;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.Body;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.Cookie;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.Header;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.Path;
import static com.github.charlemaznable.core.net.Http.fetchParameterMap;
import static com.github.charlemaznable.core.net.Http.responseJson;

@Controller
@RequestMapping("/requestFields")
@PreGuardian(RequestFieldsGuardian.class)
@PostGuardian(RequestFieldsGuardian.class)
public class RequestFieldsController {

    @RequestMapping("/error")
    public void error(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @RequestField(keyName = "appId", postProcessors = RequestFieldsParameterPostProcessor.class)
    @RequestField(keyName = "accessId", extractorType = Path, postProcessors = RequestFieldsPathPostProcessor.class)
    @RequestField(keyName = "accessId", extractorType = Header, postProcessors = RequestFieldsHeaderPostProcessor.class)
    @RequestField(keyName = "accessId", extractorType = Cookie, postProcessors = RequestFieldsCookiePostProcessor.class)
    @RequestField(keyName = "accessId", extractorType = Body, bodyFormat = Json, postProcessors = RequestFieldsBodyPostProcessor.class)
    @RequestMapping("/composite/{accessId}")
    public void param(HttpServletRequest request,
                      @PathVariable String accessId,
                      @RequestBody SimpleBody requestBody,
                      HttpServletResponse response) {
        responseJson(response, "{}");
    }

    @Data
    public static class SimpleBody {

        private String accessId;
    }
}
