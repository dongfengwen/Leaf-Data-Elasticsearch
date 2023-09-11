package com.mig.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mig.config.EsConfiguration;
import com.mig.data.entity.esIndex.QuestionAnswer;
import com.mig.repository.EsOptlogRepository;
import com.mig.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 实现类固定继承ServiceImpl类，目的是直接注入Dao层以及操作的实例类，并实现自定义的service接口
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private RestHighLevelClient client;

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private EsOptlogRepository esOptlogRepository;

    public List<QuestionAnswer> findAnswerByTitle(String content) {

        List<QuestionAnswer> esProductTOS = new ArrayList<>();
        //1.构建检索条件
        SearchRequest searchRequest = new SearchRequest();
        //2.指定要检索的索引库（与实体类定义的索引库同个或是不指定）
        searchRequest.indices("universa_question");
        //3.指定检索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        sourceBuilder.query(QueryBuilders.multiMatchQuery(content, "content"));

        //4.结果高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.requireFieldMatch(true); //如果该属性中有多个关键字 则都高亮
        highlightBuilder.field("content");//这里配置的是“content”字段，如有多个，自行添加
        highlightBuilder.preTags("<span style='color:red'>");//高亮模式
        highlightBuilder.postTags("</span>");

        sourceBuilder.highlighter(highlightBuilder);
        searchRequest.source(sourceBuilder);
        SearchResponse response = null;
        try {
            response = client.search(searchRequest, EsConfiguration.COMMON_OPTIONS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        org.elasticsearch.search.SearchHit[] hits = response.getHits().getHits();
        for (org.elasticsearch.search.SearchHit hit : hits) {
            //如果不做高亮，则可以直接转为json，然后转为对象
//            String value = hit.getSourceAsString();
//            ESProductTO esProductTO = JSON.parseObject(value, ESProductTO.class);
            //解析高亮字段
            //获取当前命中的对象的高亮的字段
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            log.info("json-hit: " + JSONObject.toJSONString(hit));

            //查询精确后的匹配分数值（可以用入参来进行判断做筛选过滤条件）
            float score = hit.getScore();
            log.info("hit score : " + score);

            HighlightField productName = highlightFields.get("content");
            String newName = "";
            if (productName != null) {
                //获取该高亮字段的高亮信息
                Text[] fragments = productName.getFragments();
                //将前缀、关键词、后缀进行拼接
                for (Text fragment : fragments) {
                    newName += fragment;
                }
            }
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            //将高亮后的值替换掉旧值（这里就是高亮显示的部分）
            sourceAsMap.put("content", newName);
            String json = JSONObject.toJSONString(sourceAsMap);
            QuestionAnswer esProductTO = JSONObject.parseObject(json, QuestionAnswer.class);
            esProductTOS.add(esProductTO);
        }
        return esProductTOS;
    }

    @Override
    public void searchWCSave(QuestionAnswer questionAnswer) {
        boolean exists = elasticsearchRestTemplate.indexOps(QuestionAnswer.class).exists();
        System.out.println(exists);
        if (exists) elasticsearchRestTemplate.indexOps(QuestionAnswer.class).delete();
        elasticsearchRestTemplate.indexOps(QuestionAnswer.class).create();
        elasticsearchRestTemplate.indexOps(QuestionAnswer.class).putMapping();
        //保存到es
        esOptlogRepository.save(questionAnswer);
    }

    static String FILE_PATH = "com.mig.data.entity.esIndex.";

    @Override
    public void indexSave(String filePath, String fileName) {
        //根据索引名称 获取自定义类
        if (StringUtils.isBlank(filePath)) {
            filePath = FILE_PATH + fileName;
        }
        try {
            Class<?> aClass = Class.forName(filePath);
            if (elasticsearchRestTemplate.indexOps(aClass).exists()) {
                elasticsearchRestTemplate.indexOps(aClass).delete();
            }
            elasticsearchRestTemplate.indexOps(aClass).create();
            elasticsearchRestTemplate.indexOps(aClass).putMapping();
        } catch (ClassNotFoundException e) {
            log.error("路径异常：{},e:{}", filePath, e.getException());
            e.printStackTrace();
        }
    }

    @Override
    public void contentSave(String indexName, String contentJson) {


    }
}

