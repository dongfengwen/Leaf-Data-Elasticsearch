package com.mig.repository;

/**
 * @description: UserRepository
 * @author: fengwen.dong@going-link.com
 * @createDate: 2023-09-11 14:19
 */

import com.mig.data.entity.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * ElasticsearchRepository<数据Bean,一般都是String>
 * 继承后即可拥有基本的操作方法
 */
@Repository
public interface UserRepository extends ElasticsearchRepository<User, String> {

}
