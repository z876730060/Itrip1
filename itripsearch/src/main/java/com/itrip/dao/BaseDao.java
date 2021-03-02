package com.itrip.dao;

import com.itrip.pojo.ItripHotelVO;
import com.itrip.pojo.Page;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrResponse;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.io.IOException;
import java.util.List;

public class BaseDao {
    String url = "http://127.0.0.1:8080/solr-4.9.1/hotel";

    public HttpSolrClient httpSolrClient;

    public void getConnection(){
        httpSolrClient = new HttpSolrClient(url);
        httpSolrClient.setParser(new XMLResponseParser());
        httpSolrClient.setConnectionTimeout(500);
    }

    public Page<ItripHotelVO> page(SolrQuery solrQuery, int index, int pagesize) throws IOException, SolrServerException {


        solrQuery.setStart((index-1)*pagesize);
        solrQuery.setRows(pagesize);
        SolrResponse response = httpSolrClient.query(solrQuery);

        List<ItripHotelVO> list = ((QueryResponse)response).getBeans(ItripHotelVO.class);

        SolrDocumentList d = ((QueryResponse)response).getResults();

        Page page = new Page(index, pagesize, new Long(d.getNumFound()).intValue());

        page.setRows(list);
        return page;
    }
}
