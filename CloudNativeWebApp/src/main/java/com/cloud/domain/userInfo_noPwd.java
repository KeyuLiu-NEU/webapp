package com.cloud.domain;
import lombok.Data;
@Data

public class userInfo_noPwd {
    private String id;
    private String first_name;
    private String last_name;
    private String email_address;
    private String account_created;
    private String account_updated;

    public userInfo_noPwd(String id, String first_name, String last_name, String email_address, String account_created, String account_updated) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email_address = email_address;
        this.account_created = account_created;
        this.account_updated = account_updated;
    }


}
