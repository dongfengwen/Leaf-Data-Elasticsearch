package com.mig.service;

import com.mig.data.entity.esIndex.QuestionAnswer;

import java.util.List;

/**
 * @description: UserService
 * @author: fengwen.dong@going-link.com
 * @createDate: 2023-09-11 14:20
 */
public interface UserService {
    List<QuestionAnswer> findAnswerByTitle(String content);


    void searchWCSave(QuestionAnswer questionAnswer);

    void indexSave(String filePath, String fileName);

    void contentSave(String indexName, String contentJson);

}
