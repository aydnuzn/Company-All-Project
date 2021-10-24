package com.works.business._restcontrollers.homepanel;

import com.works.entities.ForgotPasswordUser;
import com.works.entities.security.User;
import com.works.repositories._jpa.ForgotPasswordUserRepository;
import com.works.repositories._jpa.UserRepository;
import com.works.services.MailService;
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

    public ForgotPasswordRestControllerBusiness(UserRepository userRepository, ForgotPasswordUserRepository forgotPasswordUserRepository, MailService mailService) {
        this.userRepository = userRepository;
        this.forgotPasswordUserRepository = forgotPasswordUserRepository;
        this.mailService = mailService;
    }


    public Map<Object, Object> forgotpassword(String us_mail) {
        Map<Object, Object> hm = new LinkedHashMap<>();
        Optional<User> optUser = userRepository.findByEmailEquals(us_mail);
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
