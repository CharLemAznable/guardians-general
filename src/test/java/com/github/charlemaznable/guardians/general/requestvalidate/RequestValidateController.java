package com.github.charlemaznable.guardians.general.requestvalidate;

import com.github.charlemaznable.guardians.PostGuardian;
import com.github.charlemaznable.guardians.PreGuardian;
import com.github.charlemaznable.guardians.general.utils.RequestBodyFormat;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.net.Http.dealRequestBodyStream;
import static com.github.charlemaznable.core.net.Http.fetchParameterMap;
import static com.github.charlemaznable.core.net.Http.responseJson;
import static com.github.charlemaznable.guardians.general.utils.RequestBodyFormat.JSON;
import static com.github.charlemaznable.guardians.general.utils.RequestBodyFormat.XML;
import static com.github.charlemaznable.guardians.general.utils.RequestValueExtractor.BODY;
import static com.github.charlemaznable.guardians.general.utils.RequestValueExtractor.COOKIE;
import static com.github.charlemaznable.guardians.general.utils.RequestValueExtractor.HEADER;
import static com.github.charlemaznable.guardians.general.utils.RequestValueExtractor.PATH;

@Controller
@RequestMapping("/requestValidate")
@PreGuardian(RequestValidateSampleGuardian.class)
@PostGuardian(RequestValidateSampleGuardian.class)
public class RequestValidateController {

    @RequestMapping("/error")
    public void error(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @RequestValidateSample
    @RequestMapping("/param")
    public void param(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @RequestValidateSample(keyNames = "accessId", extractorType = PATH)
    @RequestMapping("/path/{accessId}")
    public void path(@PathVariable String accessId, HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @RequestValidateSample(keyNames = {"accessId", "userId"}, extractorType = HEADER)
    @RequestMapping("/header")
    public void header(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @RequestValidateSample(validateType = SampleBody.class, extractorType = COOKIE)
    @RequestMapping("/cookie")
    public void cookie(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @RequestValidateSample(keyNames = "accessId", validateType = SampleBody.class, extractorType = BODY)
    @RequestMapping("/body")
    public void body(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(RequestBodyFormat.FORM.parse(dealRequestBodyStream(request, "UTF-8"), "UTF-8")));
    }

    @RequestValidateSample(validateType = SampleBody.class, extractorType = BODY, bodyFormat = JSON)
    @RequestMapping("/bodyJson")
    public void bodyJson(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(RequestBodyFormat.JSON.parse(dealRequestBodyStream(request, "UTF-8"), "UTF-8")));
    }

    @RequestValidateSample(validateType = SampleBody.class, extractorType = BODY, bodyFormat = XML)
    @RequestMapping("/bodyXml")
    public void bodyXml(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(RequestBodyFormat.XML.parse(dealRequestBodyStream(request, "UTF-8"), "UTF-8")));
    }

    @XmlRootElement
    @Getter
    @Setter
    public static class SampleBody {

        private String accessId;
        private String userId;
    }
}
