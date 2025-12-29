package com.thelocalmusicfinder.thelocalmusicfinderbackend.context;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import lombok.Data;

@Component
@RequestScope
@Data
public class IpContext {
  private boolean isAdminIp;
}
