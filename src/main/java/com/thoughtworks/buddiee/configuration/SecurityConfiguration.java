package com.thoughtworks.buddiee.configuration;

import com.thoughtworks.buddiee.filter.JWTAuthenticationFilter;
import com.thoughtworks.buddiee.filter.JWTAuthorizationFilter;
import com.thoughtworks.buddiee.service.RedisService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    public static final String API_PRODUCTS = "/api/products";

    private final RedisService redisService;

    public final AuthenticationEntryPoint authenticationEntryPoint;

    private final UserDetailsService userDetailsService;

    public SecurityConfiguration(RedisService redisService,
                                 @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService,
                                 AuthenticationEntryPoint authenticationEntryPoint) {
        this.redisService = redisService;
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, API_PRODUCTS + "/**").authenticated()
                .antMatchers(HttpMethod.POST, API_PRODUCTS, "/api/auth/logout").authenticated()
                .antMatchers(HttpMethod.PUT, API_PRODUCTS + "/**").authenticated()
                .antMatchers(HttpMethod.DELETE, API_PRODUCTS + "/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), redisService))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), redisService))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
    }
}
