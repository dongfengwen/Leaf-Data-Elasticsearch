package com.mig.data.entity.esIndex;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * @description: QuestionAnswer
 * @author: fengwen.dong@going-link.com
 * @createDate: 2023-09-11 15:04
 */

@Document(indexName = "universa_question")
@Data
public class QuestionAnswer implements Serializable {

    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String title;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String content;

}
