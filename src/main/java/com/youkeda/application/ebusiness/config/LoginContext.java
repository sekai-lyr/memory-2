package com.youkeda.application.ebusiness.config;

import com.youkeda.application.ebusiness.dataobject.UserDO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class LoginContext {

    public static final String SESSION_USER = "sekai_user";

    /**
     * 获取当前登录用户，未登录返回 null
     */
    public UserDO currentUser() {
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return null;
        }
        HttpSession session = attrs.getRequest().getSession(false);
        if (session == null) {
            return null;
        }
        return (UserDO) session.getAttribute(SESSION_USER);
    }

    /**
     * 获取当前登录用户 id，未登录返回 null
     */
    public Long currentUserId() {
        UserDO user = currentUser();
        return user == null ? null : user.getId();
    }

    public boolean isLogin() {
        return currentUserId() != null;
    }
}
