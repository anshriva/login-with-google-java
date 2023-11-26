package com.anubhav.controllers;

import com.anubhav.models.SessionData;
import com.anubhav.services.SessionService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.security.GeneralSecurityException;

@Controller
@RequestMapping("/html")
public class HtmlController {
    @Autowired
    SessionService sessionService;

    @GetMapping("/home")
    public String getHomePage(
            @CookieValue(value = "sessionId") String sessionId,
            Model model){
        SessionData sessionData = this.sessionService.getSessionDataBySessionId(sessionId);
        model.addAttribute("userId", sessionData.getUserId());
        model.addAttribute("email", sessionData.getEmail());
        model.addAttribute("isEmailVerified", sessionData.getIsEmailVerified());
        model.addAttribute("name", sessionData.getName());
        model.addAttribute("pictureUrl", sessionData.getPictureUrl());
        model.addAttribute("locale", sessionData.getLocale());
        model.addAttribute("familyName", sessionData.getFamilyName());
        model.addAttribute("giveName", sessionData.getGiveName());
        return  "home";
    }

    @PostMapping("/html-callback")
    public String htmlCallback(
            @RequestParam String credential,
            @RequestParam String g_csrf_token,
            @CookieValue(value = "g_csrf_token", defaultValue = "default") String csrfToken,
            HttpServletResponse httpServletResponse)
            throws IOException, GeneralSecurityException {

        this.validateCookie(g_csrf_token, csrfToken);
        SessionData sessionData =  this.sessionService.verifyIdTokenAndGetSessionData(credential);
        this.sessionService.addSessionToCookieAndCache(httpServletResponse, sessionData);
        return "redirect:/html/home";
    }

    private void validateCookie(String csrfTokenFromPayload, String csrfTokenFromCookie) throws AccessDeniedException {
        if(csrfTokenFromCookie == null || csrfTokenFromCookie.trim().isEmpty()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "CSRF token in cookie is empty");
        }

        if(csrfTokenFromPayload  == null || csrfTokenFromPayload.trim().isEmpty()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "CSRF token in param is empty");
        }

        if(!csrfTokenFromPayload.equalsIgnoreCase(csrfTokenFromCookie)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "CSRF token in body is not equal to CSRF token in payload");
        }
    }
}
