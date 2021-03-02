package com.itrip.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * 前端输入-查询酒店搜索条件VO
 * Created by XX on 17-5-10.
 */

public class SearchHotelVO implements Serializable {

    private String destination;

    private Integer hotelLevel;

    private String keywords;

    private String tradeAreaIds;

    private Double minPrice;

    private Double maxPrice;

    private String featureIds;

    private String ascSort;

    private String  descSort;

    private Date checkInDate;

    private Date checkOutDate;

    private Integer pageSize;

    private Integer pageNo;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public String getAscSort() {
        return ascSort;
    }

    public void setAscSort(String ascSort) {
        this.ascSort = ascSort;
    }

    public String getDescSort() {
        return descSort;
    }

    public void setDescSort(String descSort) {
        this.descSort = descSort;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getHotelLevel() {
        return hotelLevel;
    }

    public void setHotelLevel(Integer hotelLevel) {
        this.hotelLevel = hotelLevel;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getTradeAreaIds() {
        return tradeAreaIds;
    }

    public void setTradeAreaIds(String tradeAreaIds) {
        this.tradeAreaIds = tradeAreaIds;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getFeatureIds() {
        return featureIds;
    }

    public void setFeatureIds(String featureIds) {
        this.featureIds = featureIds;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

}
