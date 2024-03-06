package rs.raf.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.entities.Status;
import rs.raf.demo.entities.User;
import rs.raf.demo.entities.Vacuum;
import rs.raf.demo.services.StatusService;
import rs.raf.demo.services.VacuumService;
import rs.raf.demo.utils.JwtUtil;

import java.util.*;

@Controller
@CrossOrigin("*")
//@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequestMapping(value = "/vacuums", produces = { MediaType.APPLICATION_JSON_VALUE })
public class VacuumController {
    private final VacuumService vacuumService;
    private final StatusService statusService;
    private final JwtUtil jwtUtil;

    public VacuumController(VacuumService vacuumService, StatusService statusService,JwtUtil jwtUtil) {
        this.vacuumService = vacuumService;
        this.statusService = statusService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ResponseEntity<List<Vacuum>> getAllVacuums(@RequestHeader(name = "Authorization") String authorization) {
        if (authorization.startsWith("Bearer ")) {
            User byUser = jwtUtil.extractId(authorization.substring(7));
            return new ResponseEntity<>(vacuumService.getAllVacuums(byUser), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping
    public ResponseEntity<List<Vacuum>> addVacuum(@RequestHeader(name = "Authorization") String authorization, @RequestBody Vacuum vacuumToAdd){
        Map<String, Object> permisionMap;
        if (authorization.startsWith("Bearer ")) {
            String jwt = authorization.substring(7);
            permisionMap = jwtUtil.extractClaims(jwt);
            if (permisionMap.containsKey("can_add_vacuum")){
                vacuumToAdd.setAddedByUser(jwtUtil.extractId(jwt));
                vacuumToAdd.setStatus(statusService.getStatusByName("STOPPED"));
                return new ResponseEntity(vacuumService.addVacuum(vacuumToAdd), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Vacuum> removeVacuum(@RequestHeader(name = "Authorization") String authorization, @PathVariable Long id){
        Map<String, Object> permisionMap;
        if (authorization.startsWith("Bearer ")) {
            String jwt = authorization.substring(7);
            permisionMap = jwtUtil.extractClaims(jwt);
            User user = jwtUtil.extractId(jwt);
            if (permisionMap.containsKey("can_remove_vacuum")){
                Vacuum vacuumToRemove = vacuumService.getVacuumById(id);
                if (vacuumToRemove.getAddedByUser().equals(user) && vacuumToRemove.isActive() && vacuumToRemove.getStatus().getStatus().equals("STOPPED")){
                    vacuumToRemove.setActive(false);
                    vacuumService.addVacuum(vacuumToRemove);
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/start/{id}")
    public ResponseEntity<Vacuum> startVacuum(@RequestHeader(name = "Authorization") String authorization, @PathVariable Long id){
        Map<String, Object> permisionMap;
        if (authorization.startsWith("Bearer ")) {
            permisionMap = jwtUtil.extractClaims(authorization.substring(7));
            if (permisionMap.containsKey("can_start_vacuum")){
                Vacuum vacuumToStart = vacuumService.getVacuumById(id);
                if(vacuumToStart.isActive() && vacuumToStart.getStatus().getStatus().equals("STOPPED")){
                    vacuumService.vacuumScheduler(10000L,vacuumToStart,statusService.getStatusByName("RUNNING"));
                    vacuumToStart.setStatus(statusService.getStatusByName("PROCESSING"));
                    vacuumToStart.setCount(vacuumToStart.getCount() + 1);
                    vacuumService.addVacuum(vacuumToStart);
                    return new ResponseEntity<>(vacuumToStart, HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/schedule/start/{id}")
    public ResponseEntity<Vacuum> scheduleStartVacuum(@RequestHeader(name = "Authorization") String authorization, @RequestParam Long time, @PathVariable Long id){
        Map<String, Object> permisionMap;
        if (authorization.startsWith("Bearer ")) {
            permisionMap = jwtUtil.extractClaims(authorization.substring(7));
            if (permisionMap.containsKey("can_start_vacuum")){
                Vacuum vacuumToStart = vacuumService.getVacuumById(id);
                if(vacuumToStart.isActive()){
                    vacuumService.vacuumSchedulerAdvanced(time, id, statusService.getStatusByName("RUNNING"));
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/stop/{id}")
    public ResponseEntity<Vacuum> stopVacuum(@RequestHeader(name = "Authorization") String authorization, @PathVariable Long id){
        Map<String, Object> permisionMap;
        if (authorization.startsWith("Bearer ")) {
            permisionMap = jwtUtil.extractClaims(authorization.substring(7));
            if (permisionMap.containsKey("can_stop_vacuum")){
                Vacuum vacuumToStop = vacuumService.getVacuumById(id);
                if(vacuumToStop.isActive() && vacuumToStop.getStatus().getStatus().equals("RUNNING")){
                    if(vacuumToStop.getCount() >= 3){
                        vacuumToStop.setCount(0);
                        vacuumService.vacuumScheduler(10000L,vacuumToStop,statusService.getStatusByName("DISCHARGING"));
                        vacuumService.vacuumScheduler(25000L,vacuumToStop,statusService.getStatusByName("STOPPED"));
                        vacuumToStop.setStatus(statusService.getStatusByName("PROCESSING"));
                        vacuumService.addVacuum(vacuumToStop);
                        return new ResponseEntity<>(vacuumToStop, HttpStatus.OK);
                    }else {
                        vacuumService.vacuumScheduler(10000L,vacuumToStop,statusService.getStatusByName("STOPPED"));
                        vacuumToStop.setStatus(statusService.getStatusByName("PROCESSING"));
                        vacuumService.addVacuum(vacuumToStop);
                        return new ResponseEntity<>(vacuumToStop, HttpStatus.OK);
                    }
                }
            }
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/schedule/stop/{id}")
    public ResponseEntity<Vacuum> scheduleStopVacuum(@RequestHeader(name = "Authorization") String authorization,@RequestParam Long time, @PathVariable Long id){
        Map<String, Object> permisionMap;
        if (authorization.startsWith("Bearer ")) {
            permisionMap = jwtUtil.extractClaims(authorization.substring(7));
            if (permisionMap.containsKey("can_stop_vacuum")){
                Vacuum vacuumToStop = vacuumService.getVacuumById(id);
                if(vacuumToStop.isActive()){
                    vacuumService.vacuumSchedulerAdvanced(time, id, statusService.getStatusByName("STOPPED"));
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/discharge/{id}")
    public ResponseEntity<Vacuum> dischargeVacuum(@RequestHeader(name = "Authorization") String authorization, @PathVariable Long id){
        Map<String, Object> permisionMap;
        if (authorization.startsWith("Bearer ")) {
            permisionMap = jwtUtil.extractClaims(authorization.substring(7));
            if (permisionMap.containsKey("can_discharge_vacuum")){
                Vacuum vacuumToDischarge = vacuumService.getVacuumById(id);
                if(vacuumToDischarge.isActive() && vacuumToDischarge.getStatus().getStatus().equals("STOPPED")){
                    vacuumService.vacuumScheduler(10000L,vacuumToDischarge,statusService.getStatusByName("DISCHARGING"));
                    vacuumService.vacuumScheduler(25000L,vacuumToDischarge,statusService.getStatusByName("STOPPED"));
                    vacuumToDischarge.setStatus(statusService.getStatusByName("PROCESSING"));
                    vacuumService.addVacuum(vacuumToDischarge);
                    return new ResponseEntity<>(vacuumToDischarge, HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/schedule/discharge/{id}")
    public ResponseEntity<Vacuum> scheduleDischargeVacuum(@RequestHeader(name = "Authorization") String authorization, @RequestParam Long time, @PathVariable Long id){
        Map<String, Object> permisionMap;
        if (authorization.startsWith("Bearer ")) {
            permisionMap = jwtUtil.extractClaims(authorization.substring(7));
            if (permisionMap.containsKey("can_discharge_vacuum")){
                Vacuum vacuumToDischarge = vacuumService.getVacuumById(id);
                if(vacuumToDischarge.isActive()){
                    vacuumService.vacuumSchedulerAdvanced(time, id, statusService.getStatusByName("DISCHARGING"));
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Vacuum>> searchVacuum(@RequestHeader(name = "Authorization") String authorization,
                                                    @RequestParam(required = false) String name,
                                                    @RequestParam(required = false) String stat,
                                                    @RequestParam(required = false) Long dateFrom,
                                                    @RequestParam(required = false) Long dateTo){
        Map<String, Object> permisionMap;
        if (authorization.startsWith("Bearer ")) {
            String jwt = authorization.substring(7);
            permisionMap = jwtUtil.extractClaims(jwt);
            if (permisionMap.containsKey("can_search_vacuum")){
                List<Vacuum> vacuums;
                if (stat != null){
                    Status status = statusService.getStatusByName(stat);
                    vacuums = vacuumService.findVacuums(name, status, dateFrom, dateTo, jwtUtil.extractId(jwt));
                }else {
                    vacuums = vacuumService.findVacuums(name, null, dateFrom, dateTo, jwtUtil.extractId(jwt));
                }
                return new ResponseEntity<>(vacuums, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}
