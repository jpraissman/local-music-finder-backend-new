package com.thelocalmusicfinder.thelocalmusicfinderbackend.filters;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.context.IpContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@Order(2)
@RequiredArgsConstructor
public class IpAddressFilter extends OncePerRequestFilter {

  @Value("${admin.ip}")
  private String adminIp;

  private final IpContext ipContext;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String ipAddress = request.getRemoteAddr();
    ipContext.setAdminIp(ipAddress.equals(adminIp));

    filterChain.doFilter(request, response);
  }
}
