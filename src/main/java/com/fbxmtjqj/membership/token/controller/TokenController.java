package com.fbxmtjqj.membership.token.controller;

import com.fbxmtjqj.membership.common.aop.TokenAnnotation;
import com.fbxmtjqj.membership.token.model.dto.TokenResponse;
import com.fbxmtjqj.membership.token.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @TokenAnnotation
    @PostMapping(value = "/token")
    public ResponseEntity<TokenResponse> getToken(
            @RequestBody final String apiKey) {
        return ResponseEntity.ok(tokenService.createToken(apiKey));
    }
}
