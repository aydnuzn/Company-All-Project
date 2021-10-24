package com.works.business._restcontrollers.homepanel;

import com.works.entities.ForgotPasswordUser;
import com.works.entities.security.User;
import com.works.repositories._jpa.ForgotPasswordUserRepository;
import com.works.repositories._jpa.UserRepository;
import com.works.services.MailService;
import com.works.services.UserService;
import com.works.utils.REnum;
import com.works.utils.Util;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class ForgotPasswordRestControllerBusiness {

    final UserRepository userRepository;
    final ForgotPasswordUserRepository forgotPasswordUserRepository;
    final MailService mailService;
    final UserService userService;

    public ForgotPasswordRestControllerBusiness(UserRepository userRepository, ForgotPasswordUserRepository forgotPasswordUserRepository, MailService mailService, UserService userService) {
        this.userRepository = userRepository;
        this.forgotPasswordUserRepository = forgotPasswordUserRepository;
        this.mailService = mailService;
        this.userService = userService;
    }

    public Map<Object, Object> forgotpassword(String us_mail) {
        Map<Object, Object> hm = new LinkedHashMap<>();
        Optional<User> optUser = userRepository.findByEmailEquals(us_mail);
        if (optUser.get().getRoles().get(0).getRo_id() != 3) {
            //Müşterilerin her zaman tek rolü var ve o rolId = 3
            hm.put(REnum.STATUS, false);
            hm.put(REnum.MESSAGE, "İşlem Başarısız. Bu servis sadece müşterilere yöneliktir.");
            return hm;
        }
        if (optUser.isPresent()) {
            Optional<ForgotPasswordUser> optForgotPasswordUser = forgotPasswordUserRepository.findByForgotMail(optUser.get().getEmail());
            ForgotPasswordUser forgotPasswordUser = null;
            if(!optForgotPasswordUser.isPresent()){
                forgotPasswordUser = new ForgotPasswordUser();
            }else{
                forgotPasswordUser = optForgotPasswordUser.get();
                forgotPasswordUserRepository.deleteById(forgotPasswordUser.getForgot_id());
            }

            String uuid = UUID.randomUUID().toString();
            forgotPasswordUser.setStatus(true);
            forgotPasswordUser.setUs_mail(us_mail);
            forgotPasswordUser.setForgot_id(uuid);

            hm.put(REnum.STATUS, true);
            hm.put(REnum.MESSAGE, "İşlem başarılı.");
            hm.put("Durum", "Aktif");
            hm.put(REnum.REF, uuid);
            hm.put("Email", us_mail);
            forgotPasswordUserRepository.save(forgotPasswordUser);
        } else {
            hm.put(REnum.STATUS, false);
            hm.put(REnum.MESSAGE, "Mail bulunamadı.");
            hm.put(REnum.REF, null);
        }
        return hm;
    }

    public Map<REnum, Object> change(String mail, String ref, String newpass, String newpassagain) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        if (newpass.equals(newpassagain)) {
            if (newpass.length() >= 6) {
                Optional<ForgotPasswordUser> optForgot = forgotPasswordUserRepository.findById(ref);
                Optional<User> optUser = userRepository.findByEmailEquals(mail);
                if (optForgot.isPresent() && optForgot.get().getUs_mail().equals(mail) && optForgot.get().getStatus().equals(true) && optUser.isPresent()) {
                    optUser.get().setPassword(userService.encoder().encode(newpass));
                    userRepository.saveAndFlush(optUser.get());
                    optForgot.get().setStatus(false);
                    forgotPasswordUserRepository.saveAndFlush(optForgot.get());
                    hm.put(REnum.STATUS, true);
                    hm.put(REnum.MESSAGE, "İşlem Başarılı. Şifre değiştirildi.");
                } else {
                    hm.put(REnum.STATUS, false);
                    hm.put(REnum.MESSAGE, "İşlem Başarısız.");
                }
            }else {
                hm.put(REnum.STATUS, false);
                hm.put(REnum.MESSAGE,"Şifre 6 haneden küçük olamaz");
            }
        }else {
            hm.put(REnum.STATUS,false);
            hm.put(REnum.MESSAGE, "Şifreler birbirinden farklı");
        }
        return hm;
    }


}
