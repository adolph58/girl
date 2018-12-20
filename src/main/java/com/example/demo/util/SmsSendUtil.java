package com.example.demo.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adolph on 2017/12/6.
 */
public class SmsSendUtil {

    //private static Logger logger = Logger.getLogger(SmsSendUtil.class);

    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    static final String accessKeyId = "U9CEEAsL7Hyqpimw";
    static final String accessKeySecret = "9Ol27QEg8q9mhaSAHzPCYxtZhJYM6x";

    private static SendSmsRequest request ;
    private static IAcsClient acsClient ;


    static {
        try{
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");
            //初始化acsClient,暂不支持region化
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            acsClient = new DefaultAcsClient(profile);
            //组装请求对象-具体描述见控制台-文档部分内容
            request = new SendSmsRequest();
            //必填:短信签名-可在短信控制台中找到
            request.setSignName("外贸圈");
        }catch (Exception e) {
            System.out.println("smsUtil工具初始化失败");
            //logger.error("smsUtil工具初始化失败");
        }
    }

    public static boolean send(String phone, String code){
        try{
            System.out.println("\">>>>>>>>>短信验证码>>>>>>>>>>phone:\" + phone + \">>>>>>>>>>>>code:\" + code ");
            //logger.info(">>>>>>>>>短信验证码>>>>>>>>>>phone:" + phone + ">>>>>>>>>>>>code:" + code );
            //必填:待发送手机号
            request.setPhoneNumbers(phone);
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode("SMS_115765459");
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            request.setTemplateParam("{\"name\":\"Tom\", \"code\":\"" + code + "\"}");

            //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
            //request.setSmsUpExtendCode("90997");

            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            request.setOutId("yourOutId");

            //hint 此处可能会抛出异常，注意catch
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            if("OK".equals(sendSmsResponse.getMessage()) && "OK".equals(sendSmsResponse.getCode())) {
                return true;
            }else {
                throw new RuntimeException("Code=" + sendSmsResponse.getCode()
                        +"Message=" + sendSmsResponse.getMessage()
                        +"RequestId=" + sendSmsResponse.getRequestId()
                        +"BizId=" + sendSmsResponse.getBizId());
            }
        }catch (Exception e) {
            //logger.error("短信验证码异常"+e);
            System.out.println("短信验证码异常"+e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 通用信息发送
     * @param message 发送的内容
     * @return
     */
    public static boolean sendSmsMessage(String phone, String message){
        try{
            //logger.info(">>>>>>>>预约通知>>>>>>>>>>phone:" + phone + ">>>>>>>>>>>>code:");
            //必填:待发送手机号
            request.setPhoneNumbers(phone);
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode("SMS_117465077");
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            request.setTemplateParam("{\"message\":\"" + message + "\"}");

            //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
            //request.setSmsUpExtendCode("90997");

            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            request.setOutId("yourOutId");

            //hint 此处可能会抛出异常，注意catch
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            if("OK".equals(sendSmsResponse.getMessage()) && "OK".equals(sendSmsResponse.getCode())) {
                System.out.println("发送成功！");
                return true;
            }else {
                System.out.println("发送失败！");
                //return false;
                throw new RuntimeException("Code=" + sendSmsResponse.getCode()
                        +"Message=" + sendSmsResponse.getMessage()
                        +"RequestId=" + sendSmsResponse.getRequestId()
                        +"BizId=" + sendSmsResponse.getBizId());
            }
        }catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return false;
            //logger.error(e);
            //throw new RuntimeException(e);
        }
    }

    /**
     * 发信息
     * @param phone
     * @return
     */
    public static boolean massSend(String phone){
       return true;
    }


    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("18617377358");
        list.add("18665609299");  //王志远
        list.add("18806569483");  //王志远
        list.add("13828404885");  //外贸圈小秘书
        list.add("18320800878");  //何辉
        for(int i = 0; i < list.size(); i++) {
            String mobile = list.get(i);
            sendSmsMessage(mobile, ",五月和六月的绩效怎么还没算出来？");
        }
        //sendSmsMessage("18617377358","");
    }


}
