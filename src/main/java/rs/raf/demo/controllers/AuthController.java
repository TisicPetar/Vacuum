package rs.raf.demo.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import rs.raf.demo.login.LoginResponse;
import rs.raf.demo.login.LoginRequest;
import rs.raf.demo.services.UserService;
import rs.raf.demo.utils.JwtUtil;

@Controller
@CrossOrigin("*")
@RequestMapping(value = "/auth", produces = { MediaType.APPLICATION_JSON_VALUE })
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword())
            );
        } catch(Exception e) {
            System.out.println("Greska pri logovanju!");
            e.printStackTrace();
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(new LoginResponse(jwtUtil.generateToken(loginRequest.getUsername()), this.userService.findByUsername(loginRequest.getUsername()).getPermissions()));
    }
}
