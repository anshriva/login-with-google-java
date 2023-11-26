package com.anubhav.controllers;

import com.anubhav.models.CredVerificationReq;
import com.anubhav.models.SessionData;
import com.anubhav.services.SessionService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/js")
public class JsController {

    @Autowired
    SessionService sessionService;

    @PostMapping("/js-callback")
    public SessionData jsCallBack(
            @RequestBody CredVerificationReq credential,
            HttpServletResponse httpServletResponse
            )
            throws GeneralSecurityException, IOException {
        SessionData sessionData = this.sessionService.verifyIdTokenAndGetSessionData(credential.getCredential());
        this.sessionService.addSessionToCookieAndCache(httpServletResponse, sessionData);
        return sessionData;
    }
}