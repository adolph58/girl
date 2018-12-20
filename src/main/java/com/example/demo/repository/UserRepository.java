package com.example.demo.repository;

import com.example.demo.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends Repository<User, Long> {

    /**
     * 自动解析方法名查询
     */
    List<User> findByNameAndAddress(String name, String address);

    /**
     * HQL语句查询
     */
    @Query(value = "from User u where u.name=:name")
    List<User> findByName1(@Param("name") String name);

    /**
     * sql语句查询
     */
    @Query(value = "select * from user u where u.name=?1", nativeQuery = true)
    List<User> findByName2(String name);

    /**
     * User 实体类注解查询
     */
    List<User> findByName(String name);

    /**
     * 自动解析方法名查询
     */
    List<User> findByAddress(String address);

}
