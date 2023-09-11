package com.mig.data.entity.esIndex;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @description: Order
 * @author: fengwen.dong@going-link.com
 * @createDate: 2023-09-10 19:50
 */
@Data
@Document(indexName = "order", shards = 1, replicas = 1)
@Setting(replicas = 0)
public class Order implements Serializable {

    @Id
    private String id;

    // 订单状态 0未付款 1未发货 2运输中 3待签收 4已签收
    @Field(type = FieldType.Integer, name = "status")
    private Integer status;

    @Field(type = FieldType.Keyword, name = "no")
    private String no;

    @Field(type = FieldType.Date, name = "create_time", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Field(type = FieldType.Double, name = "amount")
    private Double amount;

    @Field(type = FieldType.Keyword, name = "creator")
    private String creator;

    @GeoPointField
    @Field(name = "point")
    private GeoPoint point;

    @Field(type = FieldType.Text, name = "address", analyzer = "ik_max_word")
    private String address;

    @Field(type = FieldType.Nested, name = "creator")
    private List<Product> product;

    @Data
    public class Product implements Serializable {

        @Field(type = FieldType.Long, name = "id")
        private Long id;

        @Field(type = FieldType.Keyword, name = "name")
        private String name;

        @Field(type = FieldType.Double, name = "price")
        private Double price;

        @Field(type = FieldType.Integer, name = "quantity")
        private Double quantity;

    }
}



