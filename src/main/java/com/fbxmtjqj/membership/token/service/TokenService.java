package com.fbxmtjqj.membership.token.service;

import com.fbxmtjqj.membership.common.exception.ErrorCode;
import com.fbxmtjqj.membership.common.exception.ServerException;
import com.fbxmtjqj.membership.token.model.dto.TokenResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {

    private final String API_KEY = "lf2McyT3V5gDu2pNNm4VxmX3C2mezX3s";
    private final String SECRET_KEY = "GYghbwpVZ4tZtbHu4Bdh8EBhAQj8EKax";

    public TokenResponse createToken(String apiKey){
        if(StringUtils.isBlank(apiKey)) {
            throw new ServerException(ErrorCode.NOT_FOUND_TOKEN);
        } else if(!API_KEY.equals(apiKey)) {
            throw new ServerException(ErrorCode.NOT_EQUAL_TOKEN);
        }

        final Instant now = Instant.now();
        final Instant expired = now.plusSeconds(1800);

        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        Map<String, Object> header = new HashMap<>();
        header.put("type", "jwt");
        header.put("alg", "HS256");

        String jwt = Jwts.builder()
                .setHeader(header)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expired))
                .claim("apiKey", API_KEY)
                .signWith(key)
                .compact();

        return TokenResponse.builder()
                .token(jwt)
                .build();
    }
}
