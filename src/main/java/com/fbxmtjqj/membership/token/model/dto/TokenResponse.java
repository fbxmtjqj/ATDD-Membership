package com.fbxmtjqj.membership.token.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class TokenResponse {

    private final String token;
}
