package com.wallet.account_ms.config;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.wallet.account_ms.domain.User;
import com.wallet.account_ms.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomBasicAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BASIC = "Basic ";

    @Autowired
    UserRepository userRepository;

    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        if (isPublicRoutes(request)) {
            filterChain.doFilter(request, response);
            return;
        }
                
        if (isBasicAuthentication(request)){
            String[] credentials = decodeBase64(getHeader(request).replace(BASIC, "")).split(":");
            String username = credentials[0];
            String password = credentials[1];

            User user = userRepository.findByUsername(username);

            if (user == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("USER DOES NOT EXISTS");
                return;
            }

            boolean valid = checkPassword(password, user.getPassword());

            if (!valid) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("WRONG PASSWORD");
                return;
            }

            setAuthentication(user);
        } else { 
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("AUTHENTICATION NOT PROVIDED");
        }
    }

    private boolean isPublicRoutes(HttpServletRequest request) {
        
        String uri = request.getRequestURI();
        String method = request.getMethod();

        System.out.println(uri);
       
        if (uri.startsWith("/swagger-ui") || uri.startsWith("/v3/api-docs")) {
            return true;
        }
        
        if(method.equals("POST") && (uri.equals("/login") || uri.equals("/account"))){
            return true;
        }

        return false;
    }

    private void setAuthentication(User user) {
        Authentication authentication = createAuthentication(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Authentication createAuthentication(User user) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        return new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
    }

    private boolean checkPassword(String loginPassword, String registeredPassword) {
        return passwordEncoder().matches(loginPassword, registeredPassword);
    }

    private String decodeBase64(String base64) {
        byte[] decodedBytes = Base64.getDecoder().decode(base64);
        return new String(decodedBytes);
    }


    private boolean isBasicAuthentication(HttpServletRequest request) {
        String header = getHeader(request);
        return header!= null && header.startsWith(BASIC);
    }

    private String getHeader(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION);
    }

}
