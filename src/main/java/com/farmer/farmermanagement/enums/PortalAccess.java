package com.farmer.farmermanagement.enums;

public enum PortalAccess {
    ACTIVE("Active"),     // Fully active user with access to all functionalities
    INACTIVE("Inactive"), // Temporarily deactivated user
    SUSPENDED("Suspended"), // Suspended due to policy violations or non-compliance
    PENDING("Pending");   // Awaiting admin approval for activation

    private final String displayName;

    PortalAccess(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static PortalAccess fromString(String text) {
        for (PortalAccess access : PortalAccess.values()) {
            if (access.name().equalsIgnoreCase(text) || access.displayName.equalsIgnoreCase(text)) {
                return access;
            }
        }
        throw new IllegalArgumentException("Invalid portal access status: " + text);
    }
}
