package com.github.charlemaznable.guardians.general;

import jakarta.servlet.http.HttpServletRequest;

public interface Counter {

    void count(HttpServletRequest request);
}
