package com.itrip.controller;

import com.itrip.common.DtoUtil;
import com.itrip.dao.itripAreaDic.ItripAreaDicMapper;
import com.itrip.dao.itripLabelDic.ItripLabelDicMapper;
import com.itrip.pojo.ItripAreaDic;
import com.itrip.pojo.ItripAreaDicVO;
import com.itrip.pojo.ItripLabelDic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class HotelController {

    @Resource
    private ItripAreaDicMapper areaDicMapper;

    @Resource
    private ItripLabelDicMapper labelDicMapper;

    @RequestMapping(value = "/api/hotel/querytradearea/{cityId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object querytradearea(@PathVariable("cityId") Long cityId) throws Exception {

        try {
            List<ItripAreaDicVO> itripAreaDic = areaDicMapper.getItripAreaDicById(cityId);

            return DtoUtil.returnDataSuccess(itripAreaDic);
        } catch (Exception e) {
            return DtoUtil.returnFail("系统异常,获取失败", "10202");
        }
    }

    @RequestMapping(value = "/api/hotel/queryhotcity/{type}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object queryhotcity(@PathVariable("type") int type) throws Exception {

        try {
            List<ItripAreaDic> list = areaDicMapper.getItripAreaDicListByType(type);

            return DtoUtil.returnDataSuccess(list);
        } catch (Exception e) {
            return DtoUtil.returnFail("系统异常,获取失败", "10202");
        }
    }


    @RequestMapping(value = "/api/hotel/queryhotelfeature", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object queryhotelfeature() throws Exception {

        try {
            List<ItripLabelDic> list = labelDicMapper.getItripLabelDicList();

            return DtoUtil.returnDataSuccess(list);
        } catch (Exception e) {

            return DtoUtil.returnFail("系统异常,获取失败", "10202");
        }
    }

    @RequestMapping(value = "/api/hotellist/searchItripHotelListByHotCity", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object hotellist(int cityId, int count) throws Exception {

        System.out.println(cityId);

        System.out.println(count);

        return DtoUtil.returnDataSuccess("");

    }

}
