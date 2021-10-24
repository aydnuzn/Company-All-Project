package com.works.controllers.homepanel;

import com.works.business._controllers.homepanel.ForgotPasswordControllerBusiness;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/forgotpassword")
public class ForgotPasswordController {

    final ForgotPasswordControllerBusiness business;

    public ForgotPasswordController(ForgotPasswordControllerBusiness business) {
        this.business = business;
    }

    @GetMapping("")
    public String forgotpassword() {
        return business.forgotpassword();
    }

    @GetMapping("/change/{stIndex}")
    public String forgotPasswordChangeShow(@PathVariable String stIndex, Model model) {
        return business.forgotPasswordChangeShow(stIndex, model);
    }

    @PostMapping("/changepass")
    public String forgotPasswordChanged(@RequestParam(defaultValue = "") String newpass, @RequestParam(defaultValue = "") String newpassagain, Model model) {
        return business.forgotPasswordChanged(newpass, newpassagain, model);
    }

    @GetMapping("/{us_mail}")
    @ResponseBody
    public Map<Object, Object> forgotpasswordEmailSend(@RequestBody @PathVariable String us_mail) {
        return business.forgotpasswordEmailSend(us_mail);
    }


}