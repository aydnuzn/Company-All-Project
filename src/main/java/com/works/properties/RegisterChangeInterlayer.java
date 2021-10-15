package com.works.properties;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RegisterChangeInterlayer {

    @NotEmpty(message = "not empty")
    String[] change_user_roles;

}