package com.duyhung.jobhunter.domain.response;

import com.duyhung.jobhunter.util.constant.GenderEnum;

import java.time.Instant;


public class ResUserDTO {
    private long id;
    private String name;
    private String email;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant updatedAt;
    private Instant createdAt;

    private CompanyUser companyUser;

    public static class CompanyUser {
        private Long id;
        private String name;

        public CompanyUser() {
        }

        public CompanyUser(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


    public ResUserDTO() {
    }

    public ResUserDTO(long id, String name, String email, GenderEnum gender, String address, int age, Instant updatedAt, Instant createdAt, CompanyUser companyUser) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.address = address;
        this.age = age;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.companyUser = companyUser;
    }

    public CompanyUser getCompanyUser() {
        return companyUser;
    }

    public void setCompanyUser(CompanyUser companyUser) {
        this.companyUser = companyUser;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
