package com.example.demo.repository;

import com.example.demo.domain.Sheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by 程江涛 on 2018/3/13 0013
 */
public interface SheetRepository extends JpaRepository<Sheet, Integer>{

    //查询所有数据
    List<Sheet> findAll();

    /**
     * sql语句查询
     */
    @Query(value = "select * from sheet where id<?1", nativeQuery = true)
    List<Sheet> findById(Integer id);
}
