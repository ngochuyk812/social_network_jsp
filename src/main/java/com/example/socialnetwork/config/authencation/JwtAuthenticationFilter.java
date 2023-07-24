package com.example.socialnetwork.config.authencation;

import com.example.socialnetwork.config.ExeptionHandler.ExceptionError;
import com.example.socialnetwork.service.imp.UserServiceImp;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private ExceptionError exceptionError;
    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException {
        String jwt = getJwtFromRequest(request);
        boolean checkToken = false;

            if (jwt != null ) {
                try {
                    checkToken = tokenProvider.validateToken(jwt);
                } catch (IllegalArgumentException e) {
                    exceptionError.errorNoToken(response, "Unable to get JWT Token");
                    return;
                } catch (ExpiredJwtException e) {
                    exceptionError.errorNoToken(response, "JWT Token has expired");
                    return;

                }
                if(checkToken){
                    String username = tokenProvider.getUsernameFromJWT(jwt);
                    System.out.println(username);

                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                    if (userDetails != null) {
                        UsernamePasswordAuthenticationToken
                                authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContext context = SecurityContextHolder.createEmptyContext();
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        request.setAttribute("username", username);
                    }else{
                        exceptionError.errorNoToken(response, "Unknown User");
                        return;
                    }
                }
            }
            filterChain.doFilter(request, response);

    }
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
