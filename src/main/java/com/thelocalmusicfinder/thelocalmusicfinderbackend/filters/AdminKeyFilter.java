package com.thelocalmusicfinder.thelocalmusicfinderbackend.filters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(1)
public class AdminKeyFilter extends OncePerRequestFilter {

  @Value("${ADMIN_KEY}")
  private String adminKey;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String path = request.getRequestURI();

    if (path.startsWith("/api/admin") && !request.getMethod().equals("OPTIONS")) {
      String headerKey = request.getHeader("Admin-Key");

      if (headerKey == null || !headerKey.equals(adminKey)) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return;
      }
    }

    filterChain.doFilter(request, response);
  }
}