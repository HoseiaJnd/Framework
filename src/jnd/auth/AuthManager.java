package jnd.auth;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class AuthManager {
    private static final String SESSION_USER_KEY = "auth_user";
    private static final String SESSION_ROLES_KEY = "auth_roles";
    
    private static final Map<String, RoleLevel> defaultRoles = new HashMap<>();
    
    static {
        defaultRoles.put("GUEST", new RoleLevel("GUEST", RoleLevel.GUEST));
        defaultRoles.put("USER", new RoleLevel("USER", RoleLevel.USER));
        defaultRoles.put("MODERATOR", new RoleLevel("MODERATOR", RoleLevel.MODERATOR));
        defaultRoles.put("ADMIN", new RoleLevel("ADMIN", RoleLevel.ADMIN));
        defaultRoles.put("SUPER_ADMIN", new RoleLevel("SUPER_ADMIN", RoleLevel.SUPER_ADMIN));
    }

    public static void login(HttpSession session, String username, Set<RoleLevel> roles) {
        session.setAttribute(SESSION_USER_KEY, username);
        session.setAttribute(SESSION_ROLES_KEY, roles);
    }

    public static void logout(HttpSession session) {
        session.removeAttribute(SESSION_USER_KEY);
        session.removeAttribute(SESSION_ROLES_KEY);
    }

    public static boolean isAuthenticated(HttpSession session) {
        return session.getAttribute(SESSION_USER_KEY) != null;
    }

    public static String getCurrentUser(HttpSession session) {
        return (String) session.getAttribute(SESSION_USER_KEY);
    }

    @SuppressWarnings("unchecked")
    public static Set<RoleLevel> getCurrentUserRoles(HttpSession session) {
        Set<RoleLevel> roles = (Set<RoleLevel>) session.getAttribute(SESSION_ROLES_KEY);
        return roles != null ? roles : new HashSet<>();
    }

    public static boolean hasRole(HttpSession session, String role) {
        Set<RoleLevel> userRoles = getCurrentUserRoles(session);
        RoleLevel requiredRole = defaultRoles.get(role.toUpperCase());
        
        if (requiredRole == null) {
            return false;
        }

        for (RoleLevel userRole : userRoles) {
            if (userRole.getRole().equals(role) || userRole.hasRequiredLevel(requiredRole.getLevel())) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasAnyRole(HttpSession session, String[] roles) {
        for (String role : roles) {
            if (hasRole(session, role)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasRequiredLevel(HttpSession session, int requiredLevel) {
        Set<RoleLevel> userRoles = getCurrentUserRoles(session);
        for (RoleLevel userRole : userRoles) {
            if (userRole.hasRequiredLevel(requiredLevel)) {
                return true;
            }
        }
        return false;
    }

    public static void addRole(HttpSession session, RoleLevel role) {
        Set<RoleLevel> roles = getCurrentUserRoles(session);
        roles.add(role);
        session.setAttribute(SESSION_ROLES_KEY, roles);
    }

    public static void removeRole(HttpSession session, String role) {
        Set<RoleLevel> roles = getCurrentUserRoles(session);
        roles.removeIf(r -> r.getRole().equals(role));
        session.setAttribute(SESSION_ROLES_KEY, roles);
    }
} 