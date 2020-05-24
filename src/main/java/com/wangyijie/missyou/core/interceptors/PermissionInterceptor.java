package com.wangyijie.missyou.core.interceptors;

import com.auth0.jwt.interfaces.Claim;
import com.wangyijie.missyou.exception.http.ForbiddenException;
import com.wangyijie.missyou.exception.http.UnAuthenticatedException;
import com.wangyijie.missyou.util.JwtToken;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class PermissionInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取到请求token
        // 2. 验证token
        // 3. scope
        // 4. 读取API @ScopeLevel level值
        // 5. 比较scope和level的大小
        ScopeLevel scopeLevel = getScopeLevel(handler);
        if (scopeLevel == null) { // 公开接口
            return true;
        }
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.isEmpty(bearerToken)) {
            throw new UnAuthenticatedException(10004);
        }
        if (!bearerToken.startsWith("Bearer")) {
            throw new UnAuthenticatedException(10004);
        }
        String[] tokens = bearerToken.split(" ");
        if (tokens.length != 2) {
            throw new UnAuthenticatedException(10004);
        }
        String token = tokens[1];
        Map<String, Claim> claimMap = JwtToken.getClaims(token);
        if (claimMap == null) {
            throw new UnAuthenticatedException(10004);
        }
        boolean valid = hasPermission(scopeLevel, claimMap);

        return valid;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    private ScopeLevel getScopeLevel(Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            ScopeLevel scopeLevel = handlerMethod.getMethod().getAnnotation(ScopeLevel.class);
            return scopeLevel;
        }
        return null;
    }

    private boolean hasPermission(ScopeLevel scopeLevel, Map<String, Claim> map) {
        Integer level = scopeLevel.value();
        Integer scope = map.get("scope").asInt();
        if (level > scope) {
            throw new ForbiddenException(10005);
        }
        return true;
    }
}
