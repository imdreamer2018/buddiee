package com.thoughtworks.buddiee.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;

public class JwtTokenUtils {

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private static final String SECRET = "ThoughtWorks";
    private static final String ISS = "yangqian";

    //设置过期时间3600S = 1 H
    private static final long EXPIRATION = 3600L;

    // 选择了记住我之后的过期时间为7天
    private static final long EXPIRATION_REMEMBER = 604800L;

    // 添加角色的key
    private static final String ROLE_CLAIMS = "role";

    private static Logger logger = LoggerFactory.getLogger(JwtTokenUtils.class);

    private JwtTokenUtils() {
    }

    public static String createToken(String username, String role, boolean isRememberMe) {
        long expiration = isRememberMe ? EXPIRATION_REMEMBER : EXPIRATION;
        HashMap<String, Object> roleClaimsMap = new HashMap<>();
        roleClaimsMap.put(ROLE_CLAIMS, role);
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .setClaims(roleClaimsMap)
                .setIssuer(ISS)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .compact();
    }

    //从token中获取邮箱账号
    public static String getUsername(String token) {
        return getTokenBody(token).getSubject();
    }

    //从token中获取role
    public static String getUserRole(String token) {
        return (String) getTokenBody(token).get(ROLE_CLAIMS);
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (MalformedJwtException e) {
            logger.error("JWT strings must contain exactly 2 period characters");
            return true;
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported jwt token strings ");
            return true;
        } catch (ExpiredJwtException e) {
            logger.error("token have been expiration");
            return true;
        } catch (Exception e) {
            logger.error("unknown exception");
            return true;
        }
        return getTokenBody(token).getExpiration().before(new Date());
    }

    public static Claims getTokenBody(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}
