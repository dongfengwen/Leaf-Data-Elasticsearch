package com.mig.controller;

import com.beust.jcommander.internal.Lists;
import com.common.util.JsonData;
import com.mig.data.entity.BasePageForm;
import com.mig.data.entity.P;
import com.mig.data.utils.EsUtil;
import com.mig.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * @description: OrderController
 * @author: fengwen.dong@going-link.com
 * @createDate: 2023-09-10 19:51
 */

/**
 * es官方文档：https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.10/java-rest-high-supported-apis.html
 *
 * @author Gordon
 * @date 2022/7/15
 **/
@Api(tags = "es基本用法测试")
@RequestMapping("/ceshi")
@RestController
@Slf4j
public class OrderController {

    private static String indexName = "user_index";

    @Resource
    private EsUtil esUtil;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "创建索引")
    @GetMapping("/createIndex")
    public Boolean create(@RequestParam("indexName") String indexName) {
        return esUtil.createIndex(indexName);
    }

    @ApiOperation(value = "查询索引 * 查询全部")
    @GetMapping("searchIndex")
    public List<String> searchIndex(@RequestParam("indexName") String indexName) {
        return esUtil.searchIndex(indexName);
    }

    @ApiOperation(value = "删除索引")
    @GetMapping("deleteIndex")

    public Boolean deleteIndex(@RequestParam("indexName") String indexName) {
        return esUtil.deleteIndex(indexName);
    }


    @ApiOperation(value = "文档id 删除文档")
    @GetMapping("delete")
    public String delete(@RequestParam("id") String id) {
        return esUtil.delete(indexName, id);
    }

    @ApiOperation(value = "批量删除文档")
    @GetMapping("deleteBatch")
    public List<String> deleteBatch(@RequestParam("ids") List<String> ids) {
        return esUtil.deleteBatch(indexName, ids);
    }

    @ApiOperation(value = "文档id 查询文档")
    @GetMapping("searchById")
    public String searchById(@RequestParam("id") String id) {
        return esUtil.searchById(indexName, id);
    }

    @ApiOperation(value = "查询全部的条数")
    @GetMapping("findTotal")
    public Long findTotal() {
        TermQueryBuilder queryBuilder = QueryBuilders.termQuery("userName.keyword", "王五");
        return esUtil.findTotal(indexName, queryBuilder);
    }

    @ApiOperation(value = "组合QueryBuilder 进行查询")
    @GetMapping("findAll")
    public List<Map<String, Object>> findAll() {
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.fuzzyQuery("userName.keyword", "李四"));
        return esUtil.findAll(indexName, queryBuilder);
    }

    @ApiOperation(value = "分页查询文档")
    @PostMapping("fromSizePage")
    public P<Map<String, Object>> fromSizePage(@RequestBody BasePageForm basePageForm) {
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("userName.keyword", "王五");
        return esUtil.fromSizePage(indexName, termQueryBuilder, basePageForm);
    }

    @ApiOperation(value = "分页查询文档")
    @PostMapping("searchAfterPage")
    public P<Map<String, Object>> searchAfterPage(@RequestBody BasePageForm basePageForm) {
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("createTime")
                .from("1660634673318").to("1660634713318");
        return esUtil.searchAfterPage(indexName, rangeQueryBuilder, basePageForm);
    }

    @ApiOperation(value = "单字段聚合查询")
    @GetMapping("sinFieldsAggregateQuery")
    public List<Object> sinFieldsAggregateQuery() {
        // 需要分组的字段，可以随意指定
        List<String> fieldList = Lists.newArrayList("age");
        TermsAggregationBuilder termsAge = AggregationBuilders.terms(fieldList.get(0)).field(fieldList.get(0))
                .subAggregation(AggregationBuilders.avg("avg").field(fieldList.get(0)))
                .subAggregation(AggregationBuilders.sum("sum").field(fieldList.get(0)))
                .subAggregation(AggregationBuilders.min("min").field(fieldList.get(0)))
                .subAggregation(AggregationBuilders.count("count").field(fieldList.get(0)))
                .subAggregation(AggregationBuilders.cardinality("cardinality").field(fieldList.get(0)));
        return esUtil.aggregateQuery(indexName, fieldList, termsAge);
    }

    @ApiOperation(value = "多字段聚合查询")
    @GetMapping("multipleFieldsAggregateQuery")
    public List<Object> multipleFieldsAggregateQuery() {
        // 需要分组的字段，可以随意指定
        List<String> fieldList = Lists.newArrayList("age", "createTime");
        TermsAggregationBuilder termsAge = AggregationBuilders.terms(fieldList.get(0)).field(fieldList.get(0));
        TermsAggregationBuilder termsCreateTime = AggregationBuilders.terms(fieldList.get(1)).field(fieldList.get(1))
                .subAggregation(AggregationBuilders.avg("avg").field(fieldList.get(0)))
                .subAggregation(AggregationBuilders.sum("sum").field(fieldList.get(0)))
                .subAggregation(AggregationBuilders.min("min").field(fieldList.get(0)))
                .subAggregation(AggregationBuilders.count("count").field(fieldList.get(0)))
                .subAggregation(AggregationBuilders.cardinality("cardinality").field(fieldList.get(0)));
        return esUtil.aggregateQuery(indexName, fieldList, termsAge.subAggregation(termsCreateTime));
    }


    //根据关键字搜索 参数：page当前页、limit每页多少数据、kw高亮值,produces可以没有它的目的只是设置返回值的类型和编码集
    @ResponseBody
    @GetMapping(value = "/searchWC", produces = "application/json; charset=utf-8")
    public List<?> searchWC(String content) {
        return userService.findAnswerByTitle(content);
    }

    @ApiOperation(value = "创建索引")
    @PostMapping(value = "/save-index", produces = "application/json; charset=utf-8")
    public JsonData indexSave(@RequestParam(required = false, name = "filePath") String filePath,
                              @RequestParam("indexName") String indexName) {
        userService.indexSave(filePath, indexName);
        return JsonData.buildSuccess();
    }

    @ApiOperation(value = "新增内容")
    @PostMapping(value = "/save-content", produces = "application/json; charset=utf-8")
    public JsonData contentSave(@RequestParam("indexName") String indexName,
                                @RequestBody String contentJson) {
        userService.contentSave(indexName, contentJson);
        return JsonData.buildSuccess();
    }


}
