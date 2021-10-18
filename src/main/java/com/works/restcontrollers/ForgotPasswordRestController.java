package com.works.restcontrollers;
import com.works.repositories._jpa.ForgotPasswordUserRepository;
import com.works.repositories._jpa.UserRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restcont/forgotpassword")
public class ForgotPasswordRestController {

    final UserRepository userRepository;
    final ForgotPasswordUserRepository forgotPasswordUserRepository;

    public ForgotPasswordRestController(UserRepository userRepository, ForgotPasswordUserRepository forgotPasswordUserRepository) {
        this.userRepository = userRepository;
        this.forgotPasswordUserRepository = forgotPasswordUserRepository;
    }


}
