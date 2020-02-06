package com.hyx.house.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class Errorhandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(Errorhandler.class);

    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public String error500(HttpServletRequest request, Exception e){
        LOGGER.error(e.getMessage(), e);
        LOGGER.error(request.getRequestURI() + "\tencounter 500");
        return "error/500";
    }
}
