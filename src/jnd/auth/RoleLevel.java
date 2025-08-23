package jnd.auth;

public class RoleLevel {
    public static final int GUEST = 0;
    public static final int USER = 1;
    public static final int MODERATOR = 2;
    public static final int ADMIN = 3;
    public static final int SUPER_ADMIN = 4;

    private final String role;
    private final int level;

    public RoleLevel(String role, int level) {
        this.role = role;
        this.level = level;
    }

    public String getRole() {
        return role;
    }

    public int getLevel() {
        return level;
    }

    public boolean hasRequiredLevel(int requiredLevel) {
        return this.level >= requiredLevel;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RoleLevel roleLevel = (RoleLevel) obj;
        return level == roleLevel.level && role.equals(roleLevel.role);
    }

    @Override
    public int hashCode() {
        return 31 * role.hashCode() + level;
    }
} 