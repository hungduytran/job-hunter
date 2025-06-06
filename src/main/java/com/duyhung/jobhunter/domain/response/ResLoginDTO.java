package com.duyhung.jobhunter.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResLoginDTO {

    @JsonProperty("access_token")
    private String accesstoken;
    private UserLogin user;

    public static class UserLogin {
        private long id;
        private String name;
        private String email;

        public UserLogin() {
        }

        public UserLogin(long id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
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
    }

    public static class UserGetAccount {
        private UserLogin user;

        public UserGetAccount() {
        }

        public UserGetAccount(UserLogin user) {
            this.user = user;
        }

        public UserLogin getUser() {
            return user;
        }

        public void setUser(UserLogin user) {
            this.user = user;
        }
    }

    public UserLogin getUser() {
        return user;
    }

    public void setUser(UserLogin user) {
        this.user = user;
    }

    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }
}
