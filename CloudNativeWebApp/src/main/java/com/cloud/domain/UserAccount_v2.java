package com.cloud.domain;

import lombok.Data;



@Data
public class UserAccount_v2 {
    private String id;
    private String first_name;
    private String last_name;
    private String password;
    private String email_address;
    private String account_created;
    private String account_updated;
    public UserAccount_v2(){}
}
