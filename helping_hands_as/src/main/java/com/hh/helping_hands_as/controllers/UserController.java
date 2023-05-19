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
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final JpaUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public ModelAndView registerUserForm(@RequestParam String role, @RequestParam(required = false) Boolean error, @RequestParam(required = false) String err_code) {
        ModelAndView mav = new ModelAndView("registration-form");
        UserDto userDto = new UserDto();
        userDto.setRole(role);
        mav.addObject("user", userDto);
        return mav;
    }

    @PostMapping("/register-action")
    public ModelAndView registerUser(@ModelAttribute UserDto user) {
        User newUser = new User(user.getUsername(), passwordEncoder.encode(user.getPassword()), user.getFirstName(), user.getLastName());
        newUser.setAuthorities(new HashSet<>());
        newUser.getAuthorities().add(new Authority("ROLE_" + user.getRole()));
        SecurityUser userDetails = new SecurityUser(newUser);
        ModelAndView mav = new ModelAndView();
        try {
            userDetailsManager.createUser(userDetails);
            mav.setViewName("redirect:/login");
            return mav;
        } catch (IllegalStateException e) {
            logger.warn(e.getMessage());
            mav.setViewName("redirect:/user/register");
            mav.addObject("role", "helper");
            mav.addObject("error", true);
            mav.addObject("err_code", "duplicate");
            return mav;
        } catch (Exception e) {
            e.printStackTrace();
            mav.setViewName("err.json");
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return mav;
        }
    }

    @PutMapping("/change_password")
    public ResponseEntity changePassword(@RequestParam String oldPassword, @RequestParam String newPassword, Authentication authentication) {
        userDetailsManager.changePassword(oldPassword, newPassword);
        return new ResponseEntity("Password updated successfully!", HttpStatus.OK);
    }
}
