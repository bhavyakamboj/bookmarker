package com.sivalabs.bookmarker.web.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler  {

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException e) {
        log.error(e.getMessage(), e);
        return "403";
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception e) {
        log.error(e.getMessage(), e);
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("message", e.getMessage());
        mav.setViewName("error");

        return mav;
    }
}
