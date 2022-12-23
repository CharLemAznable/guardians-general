package com.github.charlemaznable.guardians.general.requestvalidates;

import com.github.charlemaznable.guardians.PostGuardian;
import com.github.charlemaznable.guardians.PreGuardian;
import com.github.charlemaznable.guardians.general.RequestValidate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.net.Http.fetchParameterMap;
import static com.github.charlemaznable.core.net.Http.responseJson;
import static com.github.charlemaznable.guardians.general.utils.RequestBodyFormat.JSON;
import static com.github.charlemaznable.guardians.general.utils.RequestValueExtractor.BODY;
import static com.github.charlemaznable.guardians.general.utils.RequestValueExtractor.COOKIE;
import static com.github.charlemaznable.guardians.general.utils.RequestValueExtractor.HEADER;
import static com.github.charlemaznable.guardians.general.utils.RequestValueExtractor.PATH;

@Controller
@RequestMapping("/requestValidates")
@PreGuardian(RequestValidatesGuadian.class)
@PostGuardian(RequestValidatesGuadian.class)
public class RequestValidatesController {

    @RequestMapping("/error")
    public void error(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @RequestValidate
    @RequestValidate(keyNames = "accessId", extractorType = PATH)
    @RequestValidate(keyNames = {"accessId", "userId"}, extractorType = HEADER)
    @RequestValidate(validateType = SampleBody.class, extractorType = COOKIE)
    @RequestValidate(keyNames = "accessId", validateType = SampleBody.class, extractorType = BODY, bodyFormat = JSON)
    @RequestMapping("/index/{accessId}")
    public void index(@PathVariable String accessId, HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @Getter
    @Setter
    public static class SampleBody {

        private String accessId;
        private String userId;
    }
}
