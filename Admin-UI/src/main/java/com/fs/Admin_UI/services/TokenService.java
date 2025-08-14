package com.fs.Admin_UI.services;

import com.vaadin.flow.server.VaadinSession;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Objects;


public class TokenService {
    private static final String KEY = "jwt";

    public static void save(String token) {
        VaadinSession.getCurrent().setAttribute(KEY, token);
    }
    public static String get() {
        var s = VaadinSession.getCurrent();
        return s != null ? (String) s.getAttribute(KEY) : null;
    }
    public static void clear() {
        VaadinSession.getCurrent().setAttribute(KEY, null);
    }
    public static boolean isPresent() {
        return get() != null && !Objects.requireNonNull(get()).isBlank();
    }
    public static boolean isExpired() {
        try {
            String[] parts = Objects.requireNonNull(get()).split("\\.");
            if (parts.length < 2) return true;
            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            // very tiny JSON parsing without libs:
            // look for "exp": <number>
            int idx = payloadJson.indexOf("\"exp\"");
            if (idx < 0) return false; // no exp => treat as non-expiring (or return true to force re-login)
            String sub = payloadJson.substring(idx).replaceAll("[^0-9]", " ").trim().split("\\s+")[0];
            long expEpoch = Long.parseLong(sub);
            return Instant.now().getEpochSecond() >= expEpoch;
        } catch (Exception e) {
            return true; // invalid token → treat as expired
        }
    }
    public static boolean isValid() {
        return isPresent() && !isExpired();
    }
}
