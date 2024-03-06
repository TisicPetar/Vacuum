package rs.raf.demo.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import rs.raf.demo.entities.*;
import rs.raf.demo.repositories.*;

import java.util.HashSet;
import java.util.Set;

@Component
public class BootstrapData implements CommandLineRunner {
    private final PermissionRepository permissionRepository;
    private final StatusRepository statusRepository;
    private final UserRepository userRepository;
    private final VacuumRepository vacuumRepository;
    private final ErrorHistoryRepository errorHistoryRepository;

    public BootstrapData(PermissionRepository permissionRepository, StatusRepository statusRepository, UserRepository userRepository, VacuumRepository vacuumRepository, ErrorHistoryRepository errorHistoryRepository) {
        this.permissionRepository = permissionRepository;
        this.statusRepository = statusRepository;
        this.userRepository = userRepository;
        this.vacuumRepository = vacuumRepository;
        this.errorHistoryRepository = errorHistoryRepository;
    }

    @Override
    public void run(String... args){
        //DODAVANJE PERMISIJA ZA KORISNIKE
        Permission permission1 = new Permission("can_read_users");
        Permission permission2 = new Permission("can_create_users");
        Permission permission3 = new Permission("can_update_users");
        Permission permission4 = new Permission("can_delete_users");
        permissionRepository.save(permission1);permissionRepository.save(permission2);
        permissionRepository.save(permission3);permissionRepository.save(permission4);

        //DODAVANJE PERMISIJA ZA USISIVACE
        Permission permission11 = new Permission("can_add_vacuum");
        Permission permission12 = new Permission("can_remove_vacuum");
        Permission permission13 = new Permission("can_start_vacuum");
        Permission permission14 = new Permission("can_stop_vacuum");
        Permission permission15 = new Permission("can_discharge_vacuum");
        Permission permission16 = new Permission("can_search_vacuum");
        permissionRepository.save(permission11);permissionRepository.save(permission12);
        permissionRepository.save(permission13);permissionRepository.save(permission14);
        permissionRepository.save(permission15);permissionRepository.save(permission16);

        //DODAVANJE STATUSA
        Status status1 = new Status("STOPPED");
        Status status2 = new Status("RUNNING");
        Status status3 = new Status("DISCHARGING");
        Status status4 = new Status("PROCESSING");
        statusRepository.save(status1);statusRepository.save(status2);
        statusRepository.save(status3);statusRepository.save(status4);

        //DODAVANJE USERA
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //user1
        User user1 = new User("petar",passwordEncoder.encode("petar"));
        Set<Permission> permissionSet1 = new HashSet<>();
        permissionSet1.add(permission1);permissionSet1.add(permission2);
        permissionSet1.add(permission3);permissionSet1.add(permission4);
        permissionSet1.add(permission11);permissionSet1.add(permission12);
        permissionSet1.add(permission13);permissionSet1.add(permission14);
        permissionSet1.add(permission15);permissionSet1.add(permission16);
        user1.setPermissions(permissionSet1);
        //user2
        User user2 = new User("probni",passwordEncoder.encode("probni"));
        Set<Permission> permissionSet2 = new HashSet<>();
        permissionSet2.add(permission1);permissionSet2.add(permission3);
        permissionSet2.add(permission11);permissionSet2.add(permission13);
        permissionSet2.add(permission15);
        user2.setPermissions(permissionSet2);
        //user3
        User user3 = new User("peca",passwordEncoder.encode("peca"));
        Set<Permission> permissionSet3 = new HashSet<>();
        permissionSet3.add(permission1);permissionSet3.add(permission4);
        permissionSet3.add(permission13);permissionSet3.add(permission14);
        permissionSet3.add(permission16);
        user3.setPermissions(permissionSet3);
        //user4
        User user4 = new User("stojke",passwordEncoder.encode("stojke"));
        Set<Permission> permissionSet4 = new HashSet<>();
        permissionSet4.add(permission11);permissionSet4.add(permission12);
        permissionSet4.add(permission16);permissionSet4.add(permission1);
        user4.setPermissions(permissionSet4);

        userRepository.save(user1);userRepository.save(user2);
        userRepository.save(user3);userRepository.save(user4);

        //DODAVANJE USISIVACA
        Vacuum vacuum1 = new Vacuum("usisivac1petar",status1,user1);
        Vacuum vacuum2 = new Vacuum("usisivac2petar",status1,user1);
        Vacuum vacuum3 = new Vacuum("zujalica", status1, user3);
        Vacuum vacuum4 = new Vacuum("sunjalica", status1, user3);
        Vacuum vacuum5 = new Vacuum("gorenjeE273", status1, user4);
        Vacuum vacuum6 = new Vacuum("gorenje", status1, user1);

        vacuumRepository.save(vacuum1);vacuumRepository.save(vacuum2);
        vacuumRepository.save(vacuum3);vacuumRepository.save(vacuum4);
        vacuumRepository.save(vacuum5);vacuumRepository.save(vacuum6);

        //DODAVANJE ERROR_HISTORYA
        ErrorHistory eh1 = new ErrorHistory("automatski generisano", user1, vacuum1, status1);
        ErrorHistory eh2 = new ErrorHistory("automatski generisano", user1, vacuum1, status1);
        ErrorHistory eh3 = new ErrorHistory("automatski generisano", user1, vacuum1, status1);
        ErrorHistory eh4 = new ErrorHistory("automatski generisano", user1, vacuum2, status1);
        errorHistoryRepository.save(eh1);errorHistoryRepository.save(eh2);
        errorHistoryRepository.save(eh3);errorHistoryRepository.save(eh4);
    }
}
