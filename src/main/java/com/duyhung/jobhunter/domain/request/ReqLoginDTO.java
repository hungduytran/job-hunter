package com.duyhung.jobhunter.domain.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReqLoginDTO {

    @NotBlank(message = "username khong duoc de trong")
    private String username;

    @NotBlank(message = "password khong duoc de trong")
    private String password;

}
