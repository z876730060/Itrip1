package com.itrip.controller;

import config.WXPayConfig;
import config.WXPayRequest;
import config.WXPayUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PayController {
    @Resource
    WXPayConfig config;

    @RequestMapping(value = "/api/wxpay/createqccode/{orderNo}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Object pay(@PathVariable String orderNo) throws Exception {
        WXPayRequest request = new WXPayRequest(config);

        Map map = new HashMap();
        map.put("total_fee", config.getMoney());
        map.put("body", config.getTitle());
        map.put("out_trade_no", orderNo);
        //map.put("device_info","60.90.227.236");

        Map result = request.unifiedorder(map);
        if (result.get("return_code").equals("SUCCESS")){
            return result.get("code_url");
        }
        return null;
    }

    @RequestMapping(value = "/Pay2", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public void GetPay_1(HttpServletRequest request) {
        WXPayRequest wxPayRequest = new WXPayRequest(this.config);
        Map<String, String> result = new HashMap<String, String>();
        Map<String, String> params = null;
        try {
            InputStream inputStream;
            StringBuffer sb = new StringBuffer();
            inputStream = request.getInputStream();
            String s;
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while ((s = in.readLine()) != null) {
                sb.append(s);
            }
            in.close();
            inputStream.close();
            params = WXPayUtil.xmlToMap(sb.toString());
            boolean flag = wxPayRequest.isResponseSignatureValid(params);
            if (flag) {
                String returnCode = params.get("return_code");
                if (returnCode.equals("SUCCESS")) {
                    System.out.println("成功接受信息");

                    String transactionId = params.get("transaction_id");
                    String outTradeNo = params.get("out_trade_no");
                    //业务操作
                } else {
                    result.put("return_code", "FAIL");
                    result.put("return_msg", "支付失败");
                }
            } else {
                result.put("return_code", "FAIL");
                result.put("return_msg", "签名失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
