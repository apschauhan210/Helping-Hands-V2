package com.hh.helping_hands_as.controllers;

import com.hh.helping_hands_as.dtos.UserDto;
import com.hh.helping_hands_as.entities.Authority;
import com.hh.helping_hands_as.entities.User;
import com.hh.helping_hands_as.security.SecurityUser;
import com.hh.helping_hands_as.services.JpaUserDetailsManager;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;

@Controller
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final JpaUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public ModelAndView registerUserForm(
                                            @RequestParam String role,
                                            @RequestParam(required = false) Boolean error,
                                            @RequestParam(required = false) String err_code,
                                            @RequestParam(required = false) String redirect_to
                                        ) throws UnsupportedEncodingException {
        ModelAndView mav = new ModelAndView("registration-form");
        UserDto userDto = new UserDto();
        userDto.setRole(role);
        mav.addObject("user", userDto);
        String defaultRedirectUrl = "/login";
        mav.addObject("redirect_to", redirect_to != null ? URLEncoder.encode(redirect_to, "UTF-8") : defaultRedirectUrl);
        return mav;
    }

    @PostMapping("/register-action")
    public ModelAndView registerUser(@ModelAttribute UserDto user, @RequestParam String redirect_to) {
        User newUser = new User(user.getUsername(), passwordEncoder.encode(user.getPassword()), user.getFirstName(), user.getLastName());
        newUser.setAuthorities(new HashSet<>());
        newUser.getAuthorities().add(new Authority("ROLE_" + user.getRole()));
        SecurityUser userDetails = new SecurityUser(newUser);
        ModelAndView mav = new ModelAndView();
        try {
            userDetailsManager.createUser(userDetails);
            mav.setViewName("redirect:" + redirect_to);
            return mav;
        } catch (IllegalStateException e) {
            logger.warn(e.getMessage());
            mav.setViewName("redirect:/user/register");
            mav.addObject("role", user.getRole());
            mav.addObject("error", true);
            mav.addObject("err_code", "duplicate");
            if(redirect_to != null) {
                mav.addObject("redirect_to", redirect_to);
            }
            return mav;
        } catch (Exception e) {
            e.printStackTrace();
            mav.setViewName("err.json");
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return mav;
        }
    }

    @PutMapping("/change_password")
    public ResponseEntity changePassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        userDetailsManager.changePassword(oldPassword, newPassword);
        return new ResponseEntity("Password updated successfully!", HttpStatus.OK);
    }
}
