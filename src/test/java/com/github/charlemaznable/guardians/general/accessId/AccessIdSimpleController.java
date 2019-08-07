package com.github.charlemaznable.guardians.general.accessId;

import com.github.charlemaznable.guardians.PostGuardian;
import com.github.charlemaznable.guardians.PreGuardian;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.codec.Json.json;
import static com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractor.RequestBodyFormat.Json;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.Body;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.Cookie;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.Header;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.Path;
import static com.github.charlemaznable.net.Http.fetchParameterMap;
import static com.github.charlemaznable.net.Http.responseJson;

@Controller
@RequestMapping("/accessId")
@PreGuardian(AccessIdSimpleGuardian.class)
@PostGuardian(AccessIdSimpleGuardian.class)
public class AccessIdSimpleController {

    @RequestMapping("/error")
    public void error(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @AccessIdSimple(keyName = "appId")
    @RequestMapping("/param")
    public void param(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @AccessIdSimple(extractorType = Path)
    @RequestMapping("/path/{accessId}")
    public void path(@PathVariable String accessId, HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @AccessIdSimple(extractorType = Header)
    @RequestMapping("/header")
    public void header(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @AccessIdSimple(extractorType = Cookie)
    @RequestMapping("/cookie")
    public void cookie(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @AccessIdSimple(extractorType = Body, bodyParser = Json)
    @RequestMapping("/body")
    public void body(@RequestBody SimpleBody requestBody, HttpServletResponse response) {
        responseJson(response, json(requestBody));
    }

    @Data
    public static class SimpleBody {

        private String accessId;
    }
}
