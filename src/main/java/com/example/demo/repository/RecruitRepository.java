package com.example.demo.repository;

import com.example.demo.domain.Recruit;
import com.example.demo.domain.Sheet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by 程江涛 on 2018/3/13 0013
 */
public interface RecruitRepository extends JpaRepository<Recruit, Integer>{

    //查询所有数据
    List<Recruit> findAll();
}
