package io.welldev.model.role;

public enum Permissions {
    READ("Read"),
    WRITE("Write");

    private final String permission;


    Permissions(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
