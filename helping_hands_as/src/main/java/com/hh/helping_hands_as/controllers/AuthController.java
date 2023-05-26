package com.hh.helping_hands_as.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

@Controller
public class AuthController {

    @GetMapping("/login")
    public ModelAndView login(
                HttpServletRequest request,
                HttpServletResponse response
            ) throws UnsupportedEncodingException {

        ModelAndView mav = new ModelAndView("login");

        DefaultSavedRequest savedRequest = (DefaultSavedRequest) new HttpSessionRequestCache().getRequest(request, response);
        if(savedRequest != null) {
            String redirect_to = savedRequest.getServletPath() + "?" + savedRequest.getQueryString();
            mav.addObject("redirect_to", URLEncoder.encode(redirect_to, "UTF-8"));
        } else {
            mav.addObject("redirect_to", null);
        }
        return mav;
    }
}
