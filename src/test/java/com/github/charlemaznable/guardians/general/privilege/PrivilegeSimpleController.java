package com.github.charlemaznable.guardians.general.privilege;

import com.github.charlemaznable.guardians.PostGuardian;
import com.github.charlemaznable.guardians.PreGuardian;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.codec.Json.json;
import static com.github.charlemaznable.net.Http.fetchParameterMap;
import static com.github.charlemaznable.net.Http.responseJson;

@Controller
@RequestMapping("/privilege")
@PreGuardian(PrivilegeSimpleGuardian.class)
@PostGuardian(PrivilegeSimpleGuardian.class)
public class PrivilegeSimpleController {

    @RequestMapping("/error")
    public void error(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @PrivilegeSimple(allow = "管理员")
    @RequestMapping("/admin")
    public void admin(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @PrivilegeSimple(allow = "员工")
    @RequestMapping("/user")
    public void user(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @PrivilegeSimple(allow = "游客")
    @RequestMapping("/guest")
    public void guest(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }
}
