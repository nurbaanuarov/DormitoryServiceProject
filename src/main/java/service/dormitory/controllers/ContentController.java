package service.dormitory.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import service.dormitory.models.*;

import java.util.Optional;

@Controller
public class ContentController {
    private final UsrRepository usrRepository;

    public ContentController(UsrRepository usrRepository) {
        this.usrRepository = usrRepository;
    }


    @GetMapping("/login")
    public String login() {
        return "auth/login-page";
    }

    @GetMapping("/set-email")
    public String setEmail(HttpServletResponse response) {
        String email;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        email = authentication.getName();
        Cookie emailCookie = new Cookie("email", email);
        emailCookie.setPath("/");
        emailCookie.setMaxAge(60 * 60);
        response.addCookie(emailCookie);
        return "redirect:/home";
    }

    @GetMapping("/registration")
    public String signup(){
        return "auth/signup-page";
    }

    @GetMapping("/home")
    public String home(@CookieValue(value = "email") String email, Model model){
        Optional<Usr> usr = usrRepository.findByEmail(email).stream().findAny();
        if (usr.isPresent()){
            model.addAttribute("name", usr.get().getName());
            model.addAttribute("email", usr.get().getEmail());
        }
        return "2Home_page";
    }

}