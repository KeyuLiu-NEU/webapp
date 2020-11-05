package com.cloud.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class AnswerInfo {
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(32)")
    @Id
    private String id;
    private String question_id;
    private String created_timestamp;
    private String updated_timestamp;
    private String userid;
    private String answer_text;
    @OneToMany(cascade = {CascadeType.ALL})
    private List<FileInfo> attachment;
}
