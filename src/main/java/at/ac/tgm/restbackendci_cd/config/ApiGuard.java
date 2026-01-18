package at.ac.tgm.restbackendci_cd.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ApiGuard implements HandlerInterceptor {

    @Value("${app.admin.password}")
    private String adminPassword;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        if (request.getRequestURI().startsWith("/api/auth/login")) {
            return true;
        }

        String authHeader = request.getHeader("X-API-KEY");

        if (authHeader == null || !authHeader.equals(adminPassword)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Zugriff verweigert: Falscher API Key");
            return false;
        }

        return true;
    }
}