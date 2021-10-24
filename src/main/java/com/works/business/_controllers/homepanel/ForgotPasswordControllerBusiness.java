package com.works.business._controllers.homepanel;

import com.works.entities.ForgotPasswordUser;
import com.works.entities.security.User;
import com.works.repositories._jpa.ForgotPasswordUserRepository;
import com.works.repositories._jpa.UserRepository;
import com.works.services.MailService;
import com.works.services.UserService;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Optional;

@Service
public class ForgotPasswordControllerBusiness {

    final String rvalue = "homepanel/forgotpassword/";

    final UserRepository userRepository;
    final ForgotPasswordUserRepository forgotPasswordUserRepository;
    final MailService mailService;
    final UserService userService;

    public ForgotPasswordControllerBusiness(UserRepository userRepository, ForgotPasswordUserRepository forgotPasswordUserRepository, MailService mailService, UserService userService) {
        this.userRepository = userRepository;
        this.forgotPasswordUserRepository = forgotPasswordUserRepository;
        this.mailService = mailService;
        this.userService = userService;
    }

    public String forgotpassword() {
        return rvalue + "forgotpassword";
    }

    String ref = "";
    String mail = "";

    public String forgotPasswordChangeShow(String stIndex, Model model) {
        Optional<ForgotPasswordUser> optForgot = forgotPasswordUserRepository.findById(stIndex);
        if (optForgot.isPresent() && optForgot.get().getStatus() == true) {
            ref = stIndex;
            mail = optForgot.get().getUs_mail();
            model.addAttribute("isError", false);
            model.addAttribute("isError2", false);
            model.addAttribute("isError3", false);
            return rvalue + "forgotpasswordchange";
        }
        return "error/404";
    }

    public String forgotPasswordChanged(String newpass, String newpassagain, Model model) {
        if (newpass.equals(newpassagain)) {
            if (newpass.length() >= 6) {
                Optional<ForgotPasswordUser> optForgot = forgotPasswordUserRepository.findById(ref);
                if (optForgot.isPresent() && optForgot.get().getStatus() == true) {
                    Optional<User> optUser = userRepository.findByEmailEquals(mail);
                    optUser.get().setPassword(userService.encoder().encode(newpass));
                    userRepository.saveAndFlush(optUser.get());

                    optForgot.get().setStatus(false);
                    forgotPasswordUserRepository.saveAndFlush(optForgot.get());
                    System.out.println("Şifre değiştirildi");
                } else {
                    System.err.println("Şifre değiştirilmiş. Tekrardan mail yollayarak işleminize devam edebilirsiniz");
                    model.addAttribute("isError", false);
                    model.addAttribute("isError2", false);
                    model.addAttribute("isError3", true);
                    return rvalue + "forgotpasswordchange";
                }
            } else {
                System.out.println("Şifre 6 haneden küçük olamaz");
                model.addAttribute("isError", true);
                model.addAttribute("isError2", false);
                model.addAttribute("isError3", false);
                return rvalue + "forgotpasswordchange";
            }
        } else {
            System.out.println("Sifreler birbirinden farklı");
            model.addAttribute("isError", false);
            model.addAttribute("isError2", true);
            model.addAttribute("isError3", false);
            return rvalue + "forgotpasswordchange";
        }
        return "redirect:/login";
    }

}
