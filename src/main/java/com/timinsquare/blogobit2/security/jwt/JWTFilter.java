package com.timinsquare.blogobit2.security.jwt;

import com.timinsquare.blogobit2.services.BUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    private final BUserDetailsService userDetailsService;
    private final JWTUtil jwtUtil;
    private final HandlerExceptionResolver exceptionResolver;

    public static final String HEADER_AUTH = "Authorization";

    public static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    public JWTFilter(BUserDetailsService userDetailsService,
                     JWTUtil jwtUtil,
                     @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.exceptionResolver = exceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String autHeader = request.getHeader(HEADER_AUTH);

        try {
            if (!StringUtils.hasText(autHeader) ||
                    !StringUtils.startsWithIgnoreCase(autHeader, BEARER_PREFIX)) {
                filterChain.doFilter(request, response);
                return;
            }

            String jwt = autHeader.substring(BEARER_PREFIX.length());
            String username = jwtUtil.extractUsername(jwt);
            if (StringUtils.hasText(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtUtil.isTokenValid(jwt, userDetails)) {
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, userDetails.getPassword(), userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    context.setAuthentication(authToken);
                    SecurityContextHolder.setContext(context);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            exceptionResolver.resolveException(request, response, null, ex);
        }
    }
}