package com.example.demo4.SecurityApp.controllers;

import com.example.demo4.SecurityApp.dto.LogOutDTO;

import com.example.demo4.SecurityApp.services.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logOut")
@RequiredArgsConstructor
public class LogOut {
    private final SessionService sessionService;

    @PostMapping
    public String logOut(@RequestBody LogOutDTO logOutRequest) {
        try {
            // Call service to log out from a specific session
            sessionService.logOut(logOutRequest.getUserId(), logOutRequest.getRefreshToken());
            return "You have been logged out from the specified session.";
        } catch (Exception e) {
            return "An error occurred during logout.";
        }
    }

}
