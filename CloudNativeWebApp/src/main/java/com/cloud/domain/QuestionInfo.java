package com.cloud.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


import javax.persistence.ElementCollection;
import java.util.List;

@Data
public class QuestionInfo {
    private String question_text;
    @ElementCollection
    private List<CategoryInfo> categories;
}
