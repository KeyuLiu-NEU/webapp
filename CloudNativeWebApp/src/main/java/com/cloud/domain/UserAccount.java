package com.cloud.domain;
import javax.persistence.*;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Data
@Entity
public class UserAccount {
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(32)")
    @Id

    private String id;
    private String first_name;
    private String last_name;
    private String password;
    private String emailAddress;
    private String account_created;
    private String account_updated;

    public UserAccount(){}

    public UserAccount(String first_name, String last_name, String password, String emailAddress) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.password = password;
        this.emailAddress = emailAddress;
    }


}
