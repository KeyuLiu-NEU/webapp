package com.cloud.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Question {
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(32)")
    @Id

    private String id;
    private String created_timestamp;
    private String updated_timestamp;
    private String userId;
    private String question_text;
    @OneToMany
    private List<CategoryInfo> categories;
    @OneToMany
    private List<AnswerInfo> answers;

//    private void addCategories(String string){
//        this.getCategories().add(string);
//    }
//
//    private void addAnswerInfo(AnswerInfo answerInfo){
//        this.getAnswers().add(answerInfo);
//    }
//
//    private void removeAnswerInfo(String answer_id){
//        for (AnswerInfo info : this.getAnswers()){
//            if(info.getId() == answer_id){
//                this.getAnswers().remove(info);
//            }
//        }
//    }
}
