package com.example.demo.repository;

import com.example.demo.domain.Girl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by 程江涛 on 2018/3/13 0013
 */
public interface GirlRepository extends JpaRepository<Girl, Integer>{

    //通过年龄来查询
    public List<Girl> findByAge(Integer age);
}
