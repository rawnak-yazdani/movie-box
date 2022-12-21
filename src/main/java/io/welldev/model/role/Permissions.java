package io.welldev.model.role;

public enum Permissions {
    USER_READ("User :: Read"),

    USER_WRITE("User :: Write"),

    ADMIN_READ("Admin :: Read"),

    ADMIN_WRITE("Admin :: Write");

    private final String permission;

    Permissions(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
