package com.github.ezsecure.http.cors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by youssefguenoun on 28/06/2017.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsResponseFilter implements Filter {

    private @Value("${http.cors.allow.credentials:true}") boolean allowCredentials;
    private @Value("${http.cors.allow.headers:Origin, Content-Type, Accept, Authorization, Access-Control-Request-Headers, Access-Control-Request-Method}") String allowHeaders;
    private @Value("${http.cors.allow.methods:GET, POST, DELETE, OPTIONS, HEAD, PATCH}") String allowMethods;
    private @Value("${http.cors.allow.origin:*}") String allowOrigin;
    private @Value("${http.cors.max.age:1209600}") String maxAge;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        response.setHeader("Access-Control-Allow-Credentials", Boolean.toString(allowCredentials));
        response.setHeader("Access-Control-Allow-Headers", allowHeaders);
        response.setHeader("Access-Control-Allow-Methods", allowMethods);
        response.setHeader("Access-Control-Allow-Origin", allowOrigin);
        response.setHeader("Access-Control-Max-Age", maxAge);

        if(allowCredentials){
            String origin = request.getHeader(HttpHeaders.ORIGIN);
            response.setHeader("Access-Control-Allow-Origin", origin);
        }

        if(HttpMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod())){
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
