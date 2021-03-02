package com.itrip.controller;

import com.itrip.common.Dto;
import com.itrip.common.DtoUtil;
import com.itrip.dao.BaseDao;
import com.itrip.pojo.ItripHotelVO;
import com.itrip.pojo.Page;
import com.itrip.pojo.SearchHotCityVO;
import com.itrip.pojo.SearchHotelVO;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrResponse;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Controller
public class HotelListController {

    BaseDao dao = new BaseDao();

    @RequestMapping(value = "/api/hotellist/searchItripHotelListByHotCity", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object getList(@RequestBody SearchHotCityVO vo) throws IOException, SolrServerException {
        dao.getConnection();
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("*:*");
        solrQuery.addFilterQuery("cityId:"+vo.getCityId());
        solrQuery.setStart(0);
        solrQuery.setRows(vo.getCount());

        QueryResponse queryResponse = dao.httpSolrClient.query(solrQuery);
        List<ItripHotelVO> list = queryResponse.getBeans(ItripHotelVO.class);

        return DtoUtil.returnDataSuccess(list);
    }

    @RequestMapping(value = "/api/hotellist/searchItripHotelPage", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Dto<Page> getPage(@RequestBody SearchHotelVO vo) throws IOException, SolrServerException {

        dao.getConnection();

        SolrQuery solrquery = new SolrQuery("*:*");

        if (!vo.getKeywords().equals("")&&vo.getKeywords()!=null){ solrquery.addFilterQuery("keyword:"+vo.getKeywords());}
        if (vo.getDestination()!=null&&!vo.getDestination().equals("")){ solrquery.addFilterQuery("destination:"+vo.getDestination());}
        if (vo.getMinPrice()!=null&&!vo.getMinPrice().equals("")){ solrquery.addFilterQuery("minPrice:["+vo.getMinPrice()+" TO *]");}
        if (vo.getMaxPrice()!=null&&!vo.getMaxPrice().equals("")){ solrquery.addFilterQuery("maxPrice:[* TO "+vo.getMaxPrice()+"]");}
        if (vo.getHotelLevel()!=null&&!vo.getHotelLevel().equals("")){ solrquery.addFilterQuery("hotelLevel:"+vo.getHotelLevel());}
        if (vo.getTradeAreaIds()!=null&&!vo.getTradeAreaIds().equals("")){
            String[] str = vo.getTradeAreaIds().split(",");
            StringBuffer stringBuffer = new StringBuffer();
            for (int i=0;i<str.length;i++){
                if(i==0){
                    stringBuffer.append("tradingAreaIds:*,"+str[i]+",*");
                } else {
                    stringBuffer.append(" or tradingAreaIds:*," + str[i] + ",*");
                }
            }
            solrquery.addFilterQuery(stringBuffer.toString());
        }
        if (vo.getFeatureIds()!=null&&!vo.getFeatureIds().equals("")){
            String[] str = vo.getFeatureIds().split(",");
            StringBuffer stringBuffer = new StringBuffer();
            for (int i=0;i<str.length;i++){
                if(i==0){
                    stringBuffer.append("featureIds:*,"+str[i]+",*");
                } else{
                    stringBuffer.append(" or featureIds:*,"+str[i]+",*");
                }
            }
            solrquery.addFilterQuery(stringBuffer.toString());
        }
        if (vo.getAscSort()!=null&&!vo.getAscSort().equals("")){ solrquery.addSort(vo.getAscSort(),SolrQuery.ORDER.asc);}
        if (vo.getPageNo()==null){ vo.setPageNo(1);}
        if (vo.getPageSize()==null){ vo.setPageSize(6);}

        Page<ItripHotelVO> page = dao.page(solrquery, vo.getPageNo(), vo.getPageSize());

        return DtoUtil.returnDataSuccess(page);
    }


}
