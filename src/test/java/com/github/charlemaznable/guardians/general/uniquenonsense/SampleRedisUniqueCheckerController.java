package com.github.charlemaznable.guardians.general.uniquenonsense;

import com.github.charlemaznable.guardians.PostGuardian;
import com.github.charlemaznable.guardians.PreGuardian;
import com.github.charlemaznable.guardians.general.UniqueNonsense;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.core.codec.Json.jsonOf;
import static com.github.charlemaznable.core.net.Http.responseJson;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.BODY_RAW;

@Controller
@RequestMapping("/unique-nonsense")
@PreGuardian(SampleRedisUniqueCheckerGuardian.class)
@PostGuardian(SampleRedisUniqueCheckerGuardian.class)
public class SampleRedisUniqueCheckerController {

    @UniqueNonsense
    @RequestMapping("/unlimit")
    public void unlimit(HttpServletResponse response) {
        responseJson(response, jsonOf("result", "SUCCESS"));
    }

    @UniqueNonsense(checker = SampleRedisUniqueChecker.class)
    @RequestMapping("/index")
    public void index(HttpServletResponse response) {
        responseJson(response, jsonOf("result", "SUCCESS"));
    }

    @UniqueNonsense(
            extractorType = BODY_RAW,
            checker = SampleRedisUniqueChecker.class)
    @RequestMapping(value = "/raw", method = RequestMethod.POST)
    public void raw(HttpServletResponse response) {
        responseJson(response, jsonOf("result", "SUCCESS"));
    }
}
