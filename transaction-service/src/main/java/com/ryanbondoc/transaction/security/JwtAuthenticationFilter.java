package com.ryanbondoc.transaction.security;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.publicKey}")
    private String publicKeyPem;

    private PublicKey publicKey;
    private volatile boolean publicKeyInitialized = false;

    private PublicKey loadPublicKeyFromEnv(String publicKeyPem) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Remove PEM headers/footers and newlines
        String keyContent = publicKeyPem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", ""); // remove any whitespace, newlines, tabs

        byte[] decoded = Base64.getDecoder().decode(keyContent);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Lazy load public key
        if (!publicKeyInitialized) {
            synchronized (this) {
                if (!publicKeyInitialized) {
                    try {
                        this.publicKey = loadPublicKeyFromEnv(publicKeyPem);
                        publicKeyInitialized = true;

                        System.out.println("Public Key (Object): " + publicKey);
                        String base64PublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
                        System.out.println("Public Key (Base64): " + base64PublicKey);
                    } catch (Exception e) {
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        response.getWriter().write("Error loading public key.");
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(publicKey)
                        .parseClaimsJws(jwt)
                        .getBody();

                request.setAttribute("userId", claims.getSubject());
            } catch (JwtException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired token.");
                return;
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing Authorization header.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-resources")
                || path.startsWith("/webjars")
                || path.equals("/");
    }
}
