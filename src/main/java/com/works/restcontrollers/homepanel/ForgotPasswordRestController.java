package com.works.restcontrollers.homepanel;

import com.works.business._restcontrollers.homepanel.ForgotPasswordRestControllerBusiness;
import com.works.utils.REnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/rest/forgotpassword")
@Api(value = "Forgot Password Rest Controller")
public class ForgotPasswordRestController {

    final ForgotPasswordRestControllerBusiness business;

    public ForgotPasswordRestController(ForgotPasswordRestControllerBusiness business) {
        this.business = business;
    }

    @ApiOperation(value = "Şifre Değiştirme -> Token Gönderme")
    @GetMapping("/sendtoken")
    public Map<Object, Object> forgotpassword(@RequestParam("us_mail") String us_mail) {
        return business.forgotpassword(us_mail);
    }

    @ApiOperation(value = "Şifre Değiştirme -> Şifre Güncelleme")
    @PostMapping("/change")
    public Map<REnum, Object> change(@RequestParam("mail") String mail, @RequestParam("ref") String ref, @RequestParam("newpass") String newpass, @RequestParam("newpassagain") String newpassagain) {
        return business.change(mail, ref, newpass, newpassagain);
    }
}
