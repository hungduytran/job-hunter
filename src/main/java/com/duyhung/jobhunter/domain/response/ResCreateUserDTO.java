package com.duyhung.jobhunter.domain.response;

import com.duyhung.jobhunter.util.constant.GenderEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class ResCreateUserDTO {
    private long id;
    private String name;
    private String email;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant createdAt;

    private CompanyUser companyUser;

    @Setter
    @Getter
    public static class CompanyUser {
        private long id;
        private String name;

    }

}
