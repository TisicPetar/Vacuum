package rs.raf.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.entities.User;
import rs.raf.demo.services.UserService;
import rs.raf.demo.utils.JwtUtil;

import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin("*")
//@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequestMapping(value = "/users", produces = { MediaType.APPLICATION_JSON_VALUE })
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers(@RequestHeader(name = "Authorization") String authorization) {
        Map <String, Object> permisionMap;
        if (authorization.startsWith("Bearer ")) {
            permisionMap = jwtUtil.extractClaims(authorization.substring(7));
            if (permisionMap.containsKey("can_read_users")){
                return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping()
    public ResponseEntity<User> addUser(@RequestHeader(name = "Authorization") String authorization, @RequestBody User userToBeAdded) {
        Map <String, Object> permisionMap;
        if (authorization.startsWith("Bearer ")) {
            permisionMap = jwtUtil.extractClaims(authorization.substring(7));
            if (permisionMap.containsKey("can_create_users")){
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                userToBeAdded.setPassword(passwordEncoder.encode(userToBeAdded.getPassword()));
                return new ResponseEntity<>(this.userService.addUser(userToBeAdded),HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PutMapping()
    public ResponseEntity<User> updateUser(@RequestHeader(name = "Authorization") String authorization, @RequestBody User userToUpdate){
        Map <String, Object> permisionMap;
        if (authorization.startsWith("Bearer ")) {
            permisionMap = jwtUtil.extractClaims(authorization.substring(7));
            if (permisionMap.containsKey("can_update_users")){
                User existedUser = this.userService.findById(userToUpdate.getId());
                if(!userToUpdate.getUsername().equals("")){
                    existedUser.setUsername(userToUpdate.getUsername());
                }
                if(userToUpdate.getPermissions() != null && userToUpdate.getPermissions().size() > 0){
                    existedUser.setPermissions(userToUpdate.getPermissions());
                }
                this.userService.save(existedUser);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping()
    public ResponseEntity<List<User>> deleteUse(@RequestHeader(name = "Authorization") String authorization, @RequestBody User userToBeDeleted) {
        Map <String, Object> permisionMap;
        if (authorization.startsWith("Bearer ")) {
            permisionMap = jwtUtil.extractClaims(authorization.substring(7));
            if (permisionMap.containsKey("can_delete_users")){
                this.userService.deleteUserById(userToBeDeleted.getId());
                return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}
