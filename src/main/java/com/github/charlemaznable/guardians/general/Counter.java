package com.github.charlemaznable.guardians.general;

import javax.servlet.http.HttpServletRequest;

public interface Counter {

    void count(HttpServletRequest request);
}
