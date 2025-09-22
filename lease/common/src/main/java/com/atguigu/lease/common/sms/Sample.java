// This file is auto-generated, don't edit it. Thanks.
package com.atguigu.lease.common.sms;

// This file is auto-generated, don't edit it. Thanks.


// public class Sample {
//
//     /**
//      * <b>description</b> :
//      * <p>使用凭据初始化账号Client</p>
//      * @return Client
//      *
//      * @throws Exception
//      */
//     public static com.aliyun.dysmsapi20170525.Client createClient() throws Exception {
//         // 工程代码建议使用更安全的无AK方式，凭据配置方式请参见：https://help.aliyun.com/document_detail/378657.html。
//         Config config1 = new Config();
//         config1.setType("access_key");
//         config1.setAccessKeyId(System.getenv("OSS_ACCESS_KEY_ID"));
//         config1.setAccessKeySecret(System.getenv("OSS_ACCESS_KEY_SECRET"));
//         com.aliyun.credentials.Client credential = new com.aliyun.credentials.Client(config1);
//
//
//         com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
//                 .setCredential(credential);
//         // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
//         config.endpoint = "dysmsapi.aliyuncs.com";
//         return new com.aliyun.dysmsapi20170525.Client(config);
//     }
//
//     public static void main(String[] args_) throws Exception {
//
//         com.aliyun.dysmsapi20170525.Client client = Sample.createClient();
//         com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
//                 .setPhoneNumbers("13299061596")
//                 .setSignName("阿里云短信测试")
//                 .setTemplateCode("SMS_154950909")
//                 .setTemplateParam("{\"code\":\"1234\"}");
//         com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
//         try {
//             // 复制代码运行请自行打印 API 的返回值
//             System.out.println(client.sendSmsWithOptions(sendSmsRequest, runtime));
//         } catch (TeaException error) {
//             // 此处仅做打印展示，请谨慎对待异常处理，在工程项目中切勿直接忽略异常。
//             // 错误 message
//             System.out.println(error.getMessage());
//             // 诊断地址
//             System.out.println(error.getData().get("Recommend"));
//             com.aliyun.teautil.Common.assertAsString(error.message);
//         } catch (Exception _error) {
//             TeaException error = new TeaException(_error.getMessage(), _error);
//             // 此处仅做打印展示，请谨慎对待异常处理，在工程项目中切勿直接忽略异常。
//             // 错误 message
//             System.out.println(error.getMessage());
//             // 诊断地址
//             System.out.println(error.getData().get("Recommend"));
//             com.aliyun.teautil.Common.assertAsString(error.message);
//         }
//     }
// }