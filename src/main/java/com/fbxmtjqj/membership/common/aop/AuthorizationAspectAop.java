package com.fbxmtjqj.membership.common.aop;

import com.fbxmtjqj.membership.common.exception.ErrorCode;
import com.fbxmtjqj.membership.common.exception.ServerException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.regex.Pattern;

@Log4j2
@Aspect
@Component
public class AuthorizationAspectAop {

    @Value(value = "${jwt.at.secret}")
    private String AT_SECRET;
    @Value(value = "${jwt.secret}")
    private String SECRET_KEY;

    @Pointcut("execution(* com.fbxmtjqj.membership..*Controller.*(..))")
    public void AuthorizationAspectChecker(){ }

    @Pointcut("@annotation(TokenAnnotation)")
    public void TokenChecker(){ }

    @Before("AuthorizationAspectChecker() && !TokenChecker()")
    public void insertAdminLog() throws WeakKeyException {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String authorization = request.getHeader("Authorization");
        if(StringUtils.isBlank(authorization)){
            throw new ServerException(ErrorCode.NOT_FOUND_TOKEN);
        }
        if(Pattern.matches("^Bearer .*", authorization)) {
            authorization = authorization.replaceAll("^Bearer( )*", "");
            Jws<Claims> jwsClaims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(authorization);

            if(jwsClaims.getBody() != null) {
                Claims claims = jwsClaims.getBody();
                if(!claims.containsKey("at_secret") || !AT_SECRET.equals(claims.get("at_secret").toString())
                        || claims.getExpiration() == null) {
                    throw new ServerException(ErrorCode.NOT_EQUAL_TOKEN);
                }
                long exp = claims.getExpiration().getTime();
                if(exp < new Date().getTime()) {
                    throw new ServerException(ErrorCode.INVALID_TOKEN);
                }
            }
        } else {
            throw new ServerException(ErrorCode.UNKNOWN_EXCEPTION);
        }
    }
}
