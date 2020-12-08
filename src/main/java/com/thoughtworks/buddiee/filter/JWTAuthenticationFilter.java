package com.thoughtworks.buddiee.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.buddiee.dto.JwtUser;
import com.thoughtworks.buddiee.dto.LoginUserDTO;
import com.thoughtworks.buddiee.service.RedisService;
import com.thoughtworks.buddiee.utils.JwtTokenUtils;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final int EXPIRATION_WEEK_TIME = 10080;
    public static final int EXPIRATION_HOUR_TIME = 60;
    private final ThreadLocal<Boolean> rememberMe = new ThreadLocal<>();
    private final AuthenticationManager authenticationManager;

    private final RedisService redisService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, RedisService redisService) {
        this.authenticationManager = authenticationManager;
        this.redisService = redisService;
        super.setFilterProcessesUrl("/api/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) {
        try {
            LoginUserDTO loginUserDTO = new ObjectMapper().readValue(request.getInputStream(), LoginUserDTO.class);
            rememberMe.set(loginUserDTO.getRememberMe());
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUserDTO.getUsername(), loginUserDTO.getPassword())
            );
        } catch (IOException e) {
            e.printStackTrace();
            rememberMe.remove();
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {
        JwtUser jwtUser = (JwtUser) authResult.getPrincipal();
        boolean isRememberMe = rememberMe.get() != null && rememberMe.get();
        String role = "";
        for (GrantedAuthority authority : jwtUser.getAuthorities()){
            role = authority.getAuthority();
        }

        String token = JwtTokenUtils.createToken(jwtUser.getUsername(), role, isRememberMe);

        redisService.setTimeout(
                "Authentication_" + jwtUser.getUsername(),
                token,
                isRememberMe ? EXPIRATION_WEEK_TIME : EXPIRATION_HOUR_TIME);

        response.setHeader("token", JwtTokenUtils.TOKEN_PREFIX + token);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.OK.value());
        PrintWriter writer = response.getWriter();
        JSONObject successResponse = new JSONObject();
        successResponse.put("username",jwtUser.getUsername());
        successResponse.put("token",JwtTokenUtils.TOKEN_PREFIX + token);
        writer.write(successResponse.toJSONString());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        JSONObject errorResponse = new JSONObject();
        errorResponse.put("error", "authentication failed, reason: "+failed.getMessage());
        writer.write(errorResponse.toJSONString());
    }
}
