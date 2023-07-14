package com.hh.helping_hands_as.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
public class AuthController {

    @GetMapping("/login")
    public ModelAndView login(
                HttpServletRequest request,
                HttpServletResponse response,
                @RequestParam(required = false) boolean fromAS  // include it if the request is sent from the AS itself(clicking on Login on Home page of AS) and not from the client
            ) {

        ModelAndView mav = new ModelAndView("login");

        if(fromAS) {  // if the request from the AS itself then don't get the cached requests
            mav.addObject("redirect_to", URLEncoder.encode("/login", StandardCharsets.UTF_8));
        } else {
            DefaultSavedRequest savedRequest = (DefaultSavedRequest) new HttpSessionRequestCache().getRequest(request, response);
            if(savedRequest != null) {
                String redirect_to = savedRequest.getServletPath() + "?" + savedRequest.getQueryString();
                mav.addObject("redirect_to", URLEncoder.encode(redirect_to, StandardCharsets.UTF_8));
            } else {
                mav.addObject("redirect_to", URLEncoder.encode("/login", StandardCharsets.UTF_8));
            }
        }
        return mav;
    }
}
