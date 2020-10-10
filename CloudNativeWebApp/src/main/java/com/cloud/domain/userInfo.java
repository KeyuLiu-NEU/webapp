package com.cloud.domain;
import lombok.Data;
@Data
public class userInfo {
    private String first_name;
    private String last_name;
    private String password;
    private String email_address;

    public userInfo(String first_name, String last_name, String password, String email_address) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.password = password;
        this.email_address = email_address;
    }
}
