package com.works.business._controllers.homepanel;

import com.works.entities.ForgotPasswordUser;
import com.works.entities.security.User;
import com.works.repositories._jpa.ForgotPasswordUserRepository;
import com.works.repositories._jpa.UserRepository;
import com.works.services.MailService;
import com.works.services.UserService;
import com.works.utils.REnum;
import com.works.utils.Util;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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

    public Map<Object, Object> forgotpasswordEmailSend(String us_mail) {
        Map<Object, Object> hm = new LinkedHashMap<>();
        Optional<User> optUser = userRepository.findByEmailEquals(us_mail);
        if(optUser.isPresent()){
            if (optUser.get().getRoles().get(0).getRo_id() == 3) {
                //Müşterilerin her zaman tek rolü var ve o rolId = 3
                hm.put(REnum.STATUS, false);
                hm.put(REnum.MESSAGE, "İşlem Başarısız. Bu servisi müşteriler kullanamaz.");
                return hm;
            }
        }else{
            hm.put(REnum.STATUS, false);
            hm.put(REnum.MESSAGE, "Mail sistemde kayıtlı değil");
            return hm;
        }
        if (optUser.isPresent()) {
            Optional<ForgotPasswordUser> optForgotPasswordUser = forgotPasswordUserRepository.findByForgotMail(optUser.get().getEmail());
            ForgotPasswordUser forgotPasswordUser = null;
            if (!optForgotPasswordUser.isPresent()) {
                forgotPasswordUser = new ForgotPasswordUser();
            } else {
                forgotPasswordUser = optForgotPasswordUser.get();
                forgotPasswordUserRepository.deleteById(forgotPasswordUser.getForgot_id());
            }
            String uuid = UUID.randomUUID().toString();
            forgotPasswordUser.setStatus(true);
            forgotPasswordUser.setUs_mail(us_mail);
            forgotPasswordUser.setForgot_id(uuid);

            String path = Util.BASE_URL + "forgotpassword/change/" + forgotPasswordUser.getForgot_id();
            if (mailService.sendMail(forgotPasswordUser.getUs_mail(), "Password Change", path)) {
                // Mail başarıyla iletildi
                hm.put(REnum.STATUS, true);
                hm.put(REnum.MESSAGE, "İşlem başarılı.");
                hm.put("Durum", "Aktif");
                hm.put(REnum.REF, uuid);
                hm.put("Email", us_mail);
                forgotPasswordUserRepository.save(forgotPasswordUser);
            } else {
                // Mail sistemde var ama mesaj yollanmadı.
                hm.put(REnum.STATUS, false);
                hm.put(REnum.MESSAGE, "Sistemsel Hata! Mail gönderilemedi.");
                hm.put(REnum.REF, null);
                hm.put(REnum.MAILSEND, false);
            }
        } else {
            hm.put(REnum.STATUS, false);
            hm.put(REnum.MESSAGE, "Mail bulunamadı.");
            hm.put(REnum.REF, null);
            hm.put(REnum.MAILSEND, null);
        }
        return hm;
    }

}
