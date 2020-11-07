package org.johnpc.jwt.util;

import org.johnpc.jwt.model.User;
import org.johnpc.jwt.repo.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DbInit implements CommandLineRunner {

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    public DbInit(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }
    @Override
    public void run(String... args) throws Exception {
        this.userRepository.deleteAll();
        User wajid = new User("wajid", passwordEncoder.encode("wajid123"), "USER", "");
        User admin = new User("admin", passwordEncoder.encode("admin123"), "ADMIN", "ACCESS_TEST1, ACCESS_TEST2");
        User manager = new User("manager", passwordEncoder.encode("manager123"), "MANAGER", "ACCESS_TEST1");

        List<User> users = Arrays.asList(wajid,admin,manager);
        this.userRepository.saveAll(users);
    }
}
