package org.johnpc.jwt.jwtconfig;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.johnpc.jwt.UserPrinciple;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    //Get triggered when we issue post request to /login
    //@SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // grab credentials and map them to loginViewModel
        LoginViewModel credentials = null;
        try {
            credentials = new ObjectMapper().readValue(request.getInputStream(), LoginViewModel.class);
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        //create login token
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                credentials.getUsername(),
                credentials.getPassword(),
                new ArrayList<>());
        //Authenticated user
        Authentication auth = authenticationManager.authenticate(authenticationToken);
        return auth;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //Grab principle
        UserPrinciple principle = (UserPrinciple) authResult.getPrincipal();
        //Create Token
        String token = JWT.create()
                .withSubject(principle.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()));
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);
    }
}
