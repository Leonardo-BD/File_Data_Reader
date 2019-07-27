package com.test.solution.app.model;

import java.math.BigDecimal;
import java.util.List;

public class Sale {

    private String saleId;
    private List<SaleItem> saleItemList;
    private String salesmanName;
    private BigDecimal totalValue;

    public Sale(String saleId, List<SaleItem> saleItemList, String salesmanName, BigDecimal totalValue) {
        this.saleId = saleId;
        this.saleItemList = saleItemList;
        this.salesmanName = salesmanName;
        this.totalValue = totalValue;
    }

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public List<SaleItem> getSaleItemList() {
        return saleItemList;
    }

    public void setSaleItemList(List<SaleItem> saleItemList) {
        this.saleItemList = saleItemList;
    }

    public String getSalesmanName() {
        return salesmanName;
    }

    public void setSalesmanName(String salesmanName) {
        this.salesmanName = salesmanName;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }
}
