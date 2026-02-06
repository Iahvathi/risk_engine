package com.example.engine.Tenant;

import com.example.engine.repository.TenantRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor

@Slf4j

public class TenantFilter extends OncePerRequestFilter {

    private final TenantRepository tenantRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String apiKey = request.getHeader("X-API-KEY");
        log.info("Incoming API KEY: {}", apiKey);

        if (apiKey != null) {
            tenantRepository.findByApiKey(apiKey)
                    .ifPresentOrElse(
                            tenant -> {
                                log.info("Tenant identified: {} (id={})", tenant.getName(), tenant.getId());
                                TenantContext.setTenantId(tenant.getId());
                            },
                            () -> log.warn("No tenant found for apiKey={}", apiKey)
                    );
        } else {
            log.warn("No X-API-KEY header present");
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}