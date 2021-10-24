package com.works.restcontrollers;

import com.works.business._restcontrollers.homepanel.ForgotPasswordRestControllerBusiness;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/rest/forgotpassword")
public class ForgotPasswordRestController {

    final ForgotPasswordRestControllerBusiness business;

    public ForgotPasswordRestController(ForgotPasswordRestControllerBusiness business) {
        this.business = business;
    }


    @GetMapping("/{us_mail}")
    @ResponseBody
    public Map<Object, Object> forgotpassword(@RequestBody @PathVariable String us_mail) {
        return business.forgotpassword(us_mail);
    }
}
