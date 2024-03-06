package rs.raf.demo.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rs.raf.demo.entities.Permission;
import rs.raf.demo.entities.User;
import rs.raf.demo.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(User user){
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public User findById(Long id){
        return this.userRepository.findById(id);
    }

    public User findByUsername(String username){
        return this.userRepository.findByUsername(username);
    }

    @Transactional
    public void deleteUserById(Long userId) {
        userRepository.deleteUserById(userId);
    }

    public void save(User user){
        this.userRepository.save(user);
    }

    public Set<Permission> getUserPermissions(Long userId) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findById(userId));
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getPermissions();
        }
        return Collections.emptySet();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User with the username: " + username + " not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}
