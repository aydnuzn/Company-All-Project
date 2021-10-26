package com.works.restcontrollers.adminpanel;

import com.works.business._restcontrollers.adminpanel.AddressRestControllerBusiness;
import com.works.properties.AddressInterlayer;
import com.works.utils.REnum;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/rest/admin/address")
public class AddressRestController {

    final AddressRestControllerBusiness business;

    public AddressRestController(AddressRestControllerBusiness business) {
        this.business = business;
    }

    @GetMapping("/{stIndex}")
    public Map<REnum, Object> addressList(@PathVariable String stIndex) {
        return business.addressList(stIndex);
    }

    @DeleteMapping("/delete/{stIndex}")
    public Map<REnum, Object> addressDelete(@PathVariable String stIndex) {
        return business.addressDelete(stIndex);
    }

    @PostMapping("/add/{stIndex}")
    public Map<REnum, Object> add(@Valid @RequestBody AddressInterlayer addressInterlayer, BindingResult bindingResult, @PathVariable String stIndex) {
        return business.add(addressInterlayer, bindingResult, stIndex);
    }
}
