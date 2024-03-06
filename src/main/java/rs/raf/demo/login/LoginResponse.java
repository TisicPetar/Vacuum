package rs.raf.demo.login;

import lombok.Getter;
import lombok.Setter;
import rs.raf.demo.entities.Permission;

import java.util.Set;

@Getter
@Setter
public class LoginResponse {
    private String jwt;
    private Set<Permission> permissions;

    public LoginResponse(String jwt, Set<Permission> permissions) {
        this.jwt = jwt;
        this.permissions = permissions;
    }
}
