package com.example.demo.repository;

import com.example.demo.domain.User;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by 程江涛 on 2018/11/15.
 * implements UserRepository
 */

public class UserRepositoryImpl {

    public List<User> findUserByName(String condition) {
        StringBuilder jpql = new StringBuilder(" select u from user u where u.tel = '18012345678'  " );
        if (StringUtils.hasText(condition)) {
            jpql.append(" and u.truename = :condition " );
        }
        return null;
    }
}
