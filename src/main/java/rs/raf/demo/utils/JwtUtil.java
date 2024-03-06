package rs.raf.demo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import rs.raf.demo.entities.Permission;
import rs.raf.demo.entities.User;
import rs.raf.demo.services.UserService;

import java.util.*;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "my_jwt_secret_key";
    private final UserService userService;

    @Autowired
    public JwtUtil(UserService userService) {
        this.userService = userService;
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public User extractId(String token) {
        return userService.findByUsername(extractUsername(token));
    }

    public Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        Set<Permission> permissionSet = userService.getUserPermissions(userService.findByUsername(username).getId());
        for (Permission permission : permissionSet){
            claims.put(permission.getName(), permission.getId());
            System.out.println(permission.getName() + " id: " + permission.getId());
        }

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setId(userService.findByUsername(username).getId().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 10000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
    }

    public boolean validateToken(String token, UserDetails user) {
        return (user.getUsername().equals(extractUsername(token)) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}
