package com.example.demo;

import com.example.demo.domain.Girl;
import com.example.demo.domain.Recruit;
import com.example.demo.domain.Sheet;
import com.example.demo.service.GirlService;
import com.example.demo.service.SheetService;
import com.example.demo.util.SmsSendUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by 程江涛 on 2018/3/13 0013
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GirlServiceTest {

    @Autowired
    private GirlService girlService;

    @Autowired
    private SheetService sheetService;

    String message = ":";

    @Test
    public void findOneTest() {
        Girl girl = girlService.findOne(10);
        Assert.assertEquals(new Integer(12), girl.getAge());
    }

    @Test
    public void findAll() {
        List<Recruit> recruitList = sheetService.getAllCustom();
        System.out.println("--------------------------");
        System.out.println(recruitList.toString());
        for(Recruit recruit: recruitList) {
            //SmsSendUtil.sendSmsMessage(recruit.getTel(), recruit.getName() + message);
        }
    }

    @Test
    public void findById() {
        List<Sheet> list = sheetService.findById(100);
        System.out.println("--------------------------");
        for(Sheet sheet : list) {
            System.out.println(sheet.toString());
            //SmsSendUtil.sendSmsMessage(sheet.getTel(), sheet.getName() + message);
        }
    }
}