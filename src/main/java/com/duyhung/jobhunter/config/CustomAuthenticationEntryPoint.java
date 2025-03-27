package com.duyhung.jobhunter.config;

import com.duyhung.jobhunter.domain.RestResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final BearerTokenAuthenticationEntryPoint delegate = new BearerTokenAuthenticationEntryPoint();
    private final ObjectMapper mapper;

    public CustomAuthenticationEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // Dùng default xử lý của Spring Security
        this.delegate.commence(request, response, authException);

        // Cập nhật response với JSON lỗi
        response.setContentType("application/json;charset=utf-8");

        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        String errorMessage = Optional.ofNullable(authException.getCause())
                        .map(Throwable::getMessage)
                .orElse(authException.getMessage());
        res .setError(errorMessage);
        res.setMessage("Token không hợp lệ hoặc đã hết hạn");
//        res.setError(authException.getMessage());

        // Ghi dữ liệu lỗi ra response
        mapper.writeValue(response.getWriter(), res);
    }
}
