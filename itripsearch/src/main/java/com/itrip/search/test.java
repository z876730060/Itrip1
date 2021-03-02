package com.itrip.search;

import com.itrip.pojo.ItripHotelVO;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.io.IOException;
import java.util.List;

public class test {
    public static void main(String[] args) throws IOException, SolrServerException {
        String url = "http://127.0.0.1:8080/solr-4.9.1/hotel";
        HttpSolrClient httpSolrClient = new HttpSolrClient(url);
        httpSolrClient.setParser(new XMLResponseParser());
        httpSolrClient.setConnectionTimeout(500);
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("*:*");
        solrQuery.setStart(0);
        solrQuery.setRows(40);
        QueryResponse queryResponse = httpSolrClient.query(solrQuery);
        List<ItripHotelVO> list = queryResponse.getBeans(ItripHotelVO.class);
        for (ItripHotelVO vo : list){
            System.out.println(vo.getId());
        }
    }
}
