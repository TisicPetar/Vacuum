package rs.raf.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.entities.ErrorHistory;
import rs.raf.demo.services.ErrorHistoryService;
import rs.raf.demo.utils.JwtUtil;

import java.util.List;

@Controller
@CrossOrigin("*")
//@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequestMapping(value = "/errorHistory", produces = { MediaType.APPLICATION_JSON_VALUE })
public class ErrorHistoryController {
    private final ErrorHistoryService errorHistoryService;
    private final JwtUtil jwtUtil;

    public ErrorHistoryController(ErrorHistoryService errorHistoryService, JwtUtil jwtUtil) {
        this.errorHistoryService = errorHistoryService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ResponseEntity<List<ErrorHistory>> getAllErrors(@RequestHeader(name = "Authorization") String authorization,
                                                           @RequestParam(defaultValue = "0") Integer page,
                                                           @RequestParam(defaultValue = "3") Integer size) {
        if (authorization.startsWith("Bearer ")) {
            return new ResponseEntity<>(errorHistoryService.paginate(page,size,jwtUtil.extractId(authorization.substring(7))),HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}
