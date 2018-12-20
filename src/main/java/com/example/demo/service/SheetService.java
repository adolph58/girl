package com.example.demo.service;

import com.example.demo.domain.Recruit;
import com.example.demo.domain.Sheet;
import com.example.demo.repository.RecruitRepository;
import com.example.demo.repository.SheetRepository;
import com.example.demo.util.SmsSendUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 程江涛 on 2018/3/13 0013
 */
@Service
public class SheetService {

    @Autowired
    private SheetRepository sheetRepository;

    @Autowired
    private RecruitRepository recruitRepository;

    public List<Sheet> getAll() {
        return sheetRepository.findAll();
    }

    public List<Recruit> getAllCustom() {
        return recruitRepository.findAll();
    }

    public void sendSmsForCompany() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgCompany = "：新外贸人才节招聘会于3月25日上午9点在广州白云5号停机坪风车广场举行，请报名的企业客户准时参加。温馨提示：请携带易拉宝，创意招聘海报，公司产品宣传资料，人才登记表等在签到处报到，感谢支持。";
                List<Sheet> list = sheetRepository.findAll();
                for(Sheet sheet: list) {
                    SmsSendUtil.sendSmsMessage(sheet.getTel(), sheet.getName() + msgCompany);
                }
            }
        }).start();

    }

    public void sendSmsForCustom() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgCustom = "：还有一天开始！钱多 事少 离家近的贸招聘会，5号停机坪风车广场，等你！详情下载外贸圈app，咨询:18344304696。";
                List<Recruit> recruitList = recruitRepository.findAll();
                for(Recruit recruit: recruitList) {
                    SmsSendUtil.sendSmsMessage(recruit.getTel(), recruit.getName() + msgCustom);
                }
            }
        }).start();

    }

    public List<Sheet> findById(Integer id) {
        return sheetRepository.findById(id);
    }
}
