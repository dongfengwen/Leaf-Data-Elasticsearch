package com.mig.repository;

import com.mig.data.entity.esIndex.Order;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @description: ProductESRepository
 * @author: fengwen.dong@going-link.com
 * @createDate: 2023-09-11 09:14
 */
@Repository
public interface ProductESRepository extends ElasticsearchRepository<Order, Long> {
    // 符合Spring Data规范的高级查询方法
}
