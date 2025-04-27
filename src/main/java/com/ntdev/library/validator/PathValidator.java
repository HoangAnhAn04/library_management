package com.ntdev.library.validator;

import java.util.Set;

public class PathValidator {

    private static final Set<String> EXCLUDED_PATHS = Set.of(
            "/api/v1/auth/login",
            "/api/v1/auth/register");

    private static final Set<String> ADMIN_ONLY_PATHS = Set.of(
            "/api/v1/books/create",
            "/api/v1/books/update",
            "/api/v1/books/delete");

    private static final Set<String> OWNER_ONLY_PATHS = Set.of(
            "/api/v1/users/update",
            "/api/v1/users/delete");

    public static boolean isExcluded(String path) {
        return EXCLUDED_PATHS.contains(path);
    }

    public static boolean isAdminOnly(String path) {
        return ADMIN_ONLY_PATHS.contains(path);
    }

    public static boolean isOwner(String universityId, String resourceId) {
        return universityId.equals(resourceId);
    }

    public static boolean isAdminAndOwnerOnly(String path) {
        return OWNER_ONLY_PATHS.stream().anyMatch(path::startsWith);
    }
}
