package com.atguigu.lease.web.app.service.impl;

import com.aliyun.dysmsapi20170525.Client;
import com.atguigu.lease.common.sms.AliyunSmsConfiguration;
import com.atguigu.lease.web.app.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    private AliyunSmsConfiguration aliyunSmsConfiguration;

    @Override
    public void sendCode(String phone, String code) throws Exception {

        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                .setSignName("阿里云短信测试")
                .setTemplateCode("SMS_154950909")
                .setTemplateParam("{\"code\":\""+ code +"\"}");
        Client client = aliyunSmsConfiguration.smsClient();
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        client.sendSmsWithOptions(sendSmsRequest,runtime);
    }
}
