package p466.volunteer.web;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import p466.volunteer.User;
import p466.volunteer.UserRole;
import p466.volunteer.data.UserRepository;
import p466.volunteer.data.UserRolesRepository;
import p466.volunteer.security.RegistrationForm;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private UserRepository userRepo;
    private UserRolesRepository userRolesRepo;
    private PasswordEncoder passwordEncoder;

    public RegistrationController(UserRepository userRepo, UserRolesRepository userRolesRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.userRolesRepo = userRolesRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String registerForm() {
        return "registration";
    }

    @PostMapping
    public String processRegistration(RegistrationForm form) {
        User user = userRepo.save(form.toUser(passwordEncoder));
        UserRole role = new UserRole();
        role.setUserId(user.getId());
        role.setRole("ROLE_USER");
        userRolesRepo.save(role);
        return "redirect:/login";
    }
}
