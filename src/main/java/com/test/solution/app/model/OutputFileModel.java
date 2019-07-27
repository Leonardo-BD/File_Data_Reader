package com.test.solution.app.model;

public class OutputFileModel {

    private int customerCount;
    private int sellerCount;
    private String higherValueSaleId;
    private String worstSeller;

    public OutputFileModel(int customerCount, int sellerCount, String higherValueSaleId, String worstSeller) {
        this.customerCount = customerCount;
        this.sellerCount = sellerCount;
        this.higherValueSaleId = higherValueSaleId;
        this.worstSeller = worstSeller;
    }

    public int getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(int customerCount) {
        this.customerCount = customerCount;
    }

    public int getSellerCount() {
        return sellerCount;
    }

    public void setSellerCount(int sellerCount) {
        this.sellerCount = sellerCount;
    }

    public String getHigherValueSaleId() {
        return higherValueSaleId;
    }

    public void setHigherValueSaleId(String higherValueSaleId) {
        this.higherValueSaleId = higherValueSaleId;
    }

    public String getWorstSeller() {
        return worstSeller;
    }

    public void setWorstSeller(String worstSeller) {
        this.worstSeller = worstSeller;
    }
}
