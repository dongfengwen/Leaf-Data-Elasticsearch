package com.mig.repository;

import com.mig.data.entity.esIndex.QuestionAnswer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @description:
 * @author: fengwen.dong@going-link.com
 * @createDate: 2023-09-11 15:17
 */
public interface EsOptlogRepository extends ElasticsearchRepository<QuestionAnswer, String> {
}
