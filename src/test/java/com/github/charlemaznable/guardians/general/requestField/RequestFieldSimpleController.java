package com.github.charlemaznable.guardians.general.requestField;

import com.github.charlemaznable.guardians.PostGuardian;
import com.github.charlemaznable.guardians.PreGuardian;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.codec.Json.jsonOf;
import static com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractor.RequestBodyFormat.Json;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.Body;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.BodyRaw;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.Cookie;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.Header;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.Path;
import static com.github.charlemaznable.core.net.Http.dealRequestBodyStream;
import static com.github.charlemaznable.core.net.Http.fetchParameterMap;
import static com.github.charlemaznable.core.net.Http.responseJson;

@Controller
@RequestMapping("/requestField")
@PreGuardian(RequestFieldSimpleGuardian.class)
@PostGuardian(RequestFieldSimpleGuardian.class)
public class RequestFieldSimpleController {

    @RequestMapping("/error")
    public void error(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @RequestFieldSimple(keyName = "appId")
    @RequestMapping("/param")
    public void param(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @RequestFieldSimple(keyName = "accessId", extractorType = Path)
    @RequestMapping("/path/{accessId}")
    public void path(@PathVariable String accessId, HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @RequestFieldSimple(keyName = "accessId", extractorType = Header)
    @RequestMapping("/header")
    public void header(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @RequestFieldSimple(keyName = "accessId", extractorType = Cookie)
    @RequestMapping("/cookie")
    public void cookie(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @RequestFieldSimple(keyName = "accessId", extractorType = Body, bodyFormat = Json)
    @RequestMapping("/body")
    public void body(@RequestBody SimpleBody requestBody, HttpServletResponse response) {
        responseJson(response, json(requestBody));
    }

    @RequestFieldSimple(extractorType = BodyRaw)
    @RequestMapping("/bodyRaw")
    public void bodyRaw(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, jsonOf("accessId", dealRequestBodyStream(request, "UTF-8")));
    }

    @Data
    public static class SimpleBody {

        private String accessId;
    }
}
