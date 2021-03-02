package com.itrip.controller;


import com.alibaba.fastjson.JSONArray;
import com.itrip.common.*;
import com.itrip.dao.itripUser.ItripUserMapper;
import com.itrip.pojo.ItripUser;
import com.itrip.pojo.ItripUserVO;

import cz.mallat.uasparser.UserAgentInfo;
import org.apache.http.cookie.CookieIdentityComparator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Controller
public class UserController {

    @Resource
    private ItripUserMapper dao;

    @Resource
    private SMS_Util sms_util;

    @Resource
    private JredisApi jredisApi;

    @RequestMapping(value = "/api/registerbyphone", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object registerByPhone(@RequestBody ItripUserVO vo) throws Exception {
        //存入数据库
        ItripUser user = new ItripUser();

        user.setUserName(vo.getUserName());
        user.setUserCode(vo.getUserCode());
        user.setUserPassword(MD5.getMd5(vo.getUserPassword(), 32));
        user.setCreationDate(new Date());
        user.setActivated(0);
        user.setUserType(0);

        String to = vo.getUserCode();
        int code = MD5.getRandomCode();
        String[] datas = new String[]{String.valueOf(code)};
        String subAppend = "1234";
        String reqId = String.valueOf(to+new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()));

        boolean result = false;

        if (null != dao.isExisted(user.getUserCode())) {

            result = true;
        } else {
            if (dao.insertItripUser(user) > 0) result = true;
        }

        if (result) {

            jredisApi.SetRedis("validate:" + to, String.valueOf(code), 2 * 60);

            //sms_util.sendMessage(to, datas, subAppend,MD5.getMd5(reqId, 32));

            return DtoUtil.returnSuccess();
        }

        return DtoUtil.returnFail("注册失败", "10000");
    }

    @RequestMapping(value = "/api/validatephone", method = RequestMethod.PUT, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object validatePhone(String user, String code) throws Exception {

        String valCode = jredisApi.getRedis("validate:"+user);

        if (null != valCode){
           if (valCode.equals(code)) {
               dao.updateActived(1,user);

               return DtoUtil.returnSuccess();
           } else {
               return DtoUtil.returnFail("验证码有误", "10002");
           }
        } else {
            return DtoUtil.returnFail("重新注册", "10001");
        }
    }

    @RequestMapping(value = "/api/doregister", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object doregister(@RequestBody ItripUserVO vo) throws Exception {
        //存入数据库
        ItripUser user = new ItripUser();

        user.setUserName(vo.getUserName());
        user.setUserCode(vo.getUserCode());
        user.setUserPassword(MD5.getMd5(vo.getUserPassword(), 32));
        user.setCreationDate(new Date());
        user.setActivated(0);
        user.setUserType(0);

        String to = vo.getUserCode();
        int code = MD5.getRandomCode();

        boolean result = false;

        if (null != dao.isExisted(user.getUserCode())) {

            result = true;
        } else {
            if (dao.insertItripUser(user) > 0) result = true;
        }

        if (result) {

            jredisApi.SetRedis("validate:" + to, String.valueOf(code), 2 * 60);

            EmailUtil.sendHtmlMail(new String[]{to}, String.valueOf(code));

            return DtoUtil.returnSuccess();
        }

        return DtoUtil.returnFail("注册失败", "10000");
    }

    @RequestMapping(value = "/api/activate", method = RequestMethod.PUT, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object activate(String user, String code) throws Exception {

        String valCode = jredisApi.getRedis("validate:"+user);

        if (null != valCode){
            if (valCode.equals(code)) {
                dao.updateActived(1,user);

                return DtoUtil.returnSuccess();
            } else {
                return DtoUtil.returnFail("验证码有误", "10002");
            }
        } else {
            return DtoUtil.returnFail("重新注册", "10001");
        }
    }

    @RequestMapping(value = "/api/ckusr", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object ckusr(String name) throws Exception {

        String emailAddr = name;

        if (null != dao.isExisted(emailAddr)){

            return DtoUtil.returnFail("邮箱已被注册", "251");
        }

        return DtoUtil.returnSuccess("邮箱可用");
    }

    @RequestMapping(value = "/api/dologin", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object login(String name, String password, HttpServletRequest request) throws Exception {

        ItripUser user =  dao.getItripUser(name, MD5.getMd5(password, 32));

        if (null != user){

            if (1 == user.getActivated()) {

                String token = generateToken(request.getHeader("User-Agent"), user);

                jredisApi.SetRedis(token, JSONArray.toJSONString(user), 7200);

                ItripTokenVO itripTokenVO = new ItripTokenVO();

                itripTokenVO.setToken(token);
                itripTokenVO.setGenTime(new Date().getTime());
                itripTokenVO.setExpTime(new Date().getTime()+7200);

                return DtoUtil.returnDataSuccess(itripTokenVO);
            } else {

                return DtoUtil.returnFail("账号未激活", "5000");
            }
        }
        return DtoUtil.returnFail("登陆失败", "250");
    }

    @RequestMapping(value = "/api/retoken", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object retoken(HttpServletRequest request) {

        String token = request.getHeader("token");

        String oldToken = jredisApi.getRedis(token);

        ItripTokenVO itripTokenVO = new ItripTokenVO();

        try {
            jredisApi.delRedis(token);

            jredisApi.SetRedis(token, oldToken, 7200);

            itripTokenVO.setToken(token);
            itripTokenVO.setGenTime(new Date().getTime());
            itripTokenVO.setExpTime(new Date().getTime()+7200);
        } catch (Exception e) {
            return DtoUtil.returnFail("失败", "250");
        }

        return DtoUtil.returnSuccess();
    }

    @RequestMapping(value = "/api/logout", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object logout(HttpServletRequest request) {

        String token = request.getHeader("token");

        System.out.println(token);

        try {
            jredisApi.delRedis(token);
        } catch (Exception e) {
            return DtoUtil.returnFail("退出失败", "250");
        }

        return DtoUtil.returnSuccess();
    }

    public String generateToken(String agent, ItripUser user) {
        // TODO Auto-generated method stub
        try {
            UserAgentInfo userAgentInfo = UserAgentUtil.getUasParser().parse(
                    agent);
            StringBuilder sb = new StringBuilder();
            sb.append("token:");//统一前缀
            if (userAgentInfo.getDeviceType().equals(UserAgentInfo.UNKNOWN)) {
                if (UserAgentUtil.CheckAgent(agent)) {
                    sb.append("MOBILE-");
                } else {
                    sb.append("PC-");
                }
            } else if (userAgentInfo.getDeviceType()
                    .equals("Personal computer")) {
                sb.append("PC-");
            } else
                sb.append("MOBILE-");
//			sb.append(user.getUserCode() + "-");
            sb.append(MD5.getMd5(user.getUserCode(),32) + "-");//加密用户名称
            sb.append(user.getId() + "-");
            sb.append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
                    + "-");
            sb.append(MD5.getMd5(agent, 6));// 识别客户端的简化实现——6位MD5码

            return sb.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}

