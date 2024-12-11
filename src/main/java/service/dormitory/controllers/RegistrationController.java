package service.dormitory.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import service.dormitory.models.Usr;
import service.dormitory.models.UsrRepository;

@RestController
public class RegistrationController {

    private final UsrRepository usrRepository;

    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UsrRepository usrRepository, PasswordEncoder passwordEncoder) {
        this.usrRepository = usrRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value = "/registration", consumes = "application/json")
    public Usr createUser(@RequestBody Usr user){
        System.out.println(user);
        user.setRole("user");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return usrRepository.save(user);
    }

}