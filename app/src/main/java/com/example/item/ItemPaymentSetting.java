package com.example.item;

public class ItemPaymentSetting {

    private String currencyCode;
    private String stripePublisherKey;
    private String razorPayKey;
    private String payStackPublicKey;
    private boolean isPayPal = false;
    private boolean isPayPalSandbox = false;
    private boolean isStripe = false;
    private boolean isRazorPay = false;
    private boolean isPayStack = false;
    private boolean isPayUMoney = false;
    private boolean isPayUMoneySandbox = false;
    private String payUMoneyMerchantId;
    private String payUMoneyMerchantKey;
    private boolean isPayTM = false;
    private boolean isPayTMSandbox = false;
    private String payTMMid;


    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public boolean isPayPal() {
        return isPayPal;
    }

    public void setPayPal(boolean payPal) {
        isPayPal = payPal;
    }

    public boolean isStripe() {
        return isStripe;
    }

    public void setStripe(boolean stripe) {
        isStripe = stripe;
    }

    public boolean isRazorPay() {
        return isRazorPay;
    }

    public void setRazorPay(boolean razorPay) {
        isRazorPay = razorPay;
    }


    public boolean isPayPalSandbox() {
        return isPayPalSandbox;
    }

    public void setPayPalSandbox(boolean payPalSandbox) {
        isPayPalSandbox = payPalSandbox;
    }

    public String getStripePublisherKey() {
        return stripePublisherKey;
    }

    public void setStripePublisherKey(String stripePublisherKey) {
        this.stripePublisherKey = stripePublisherKey;
    }

    public String getRazorPayKey() {
        return razorPayKey;
    }

    public void setRazorPayKey(String razorPayKey) {
        this.razorPayKey = razorPayKey;
    }

    public String getPayStackPublicKey() {
        return payStackPublicKey;
    }

    public void setPayStackPublicKey(String payStackPublicKey) {
        this.payStackPublicKey = payStackPublicKey;
    }

    public boolean isPayStack() {
        return isPayStack;
    }

    public void setPayStack(boolean payStack) {
        isPayStack = payStack;
    }

    public boolean isPayUMoney() {
        return isPayUMoney;
    }

    public void setPayUMoney(boolean payUMoney) {
        isPayUMoney = payUMoney;
    }

    public boolean isPayUMoneySandbox() {
        return isPayUMoneySandbox;
    }

    public void setPayUMoneySandbox(boolean payUMoneySandbox) {
        isPayUMoneySandbox = payUMoneySandbox;
    }

    public String getPayUMoneyMerchantId() {
        return payUMoneyMerchantId;
    }

    public void setPayUMoneyMerchantId(String payUMoneyMerchantId) {
        this.payUMoneyMerchantId = payUMoneyMerchantId;
    }

    public String getPayUMoneyMerchantKey() {
        return payUMoneyMerchantKey;
    }

    public void setPayUMoneyMerchantKey(String payUMoneyMerchantKey) {
        this.payUMoneyMerchantKey = payUMoneyMerchantKey;
    }

    public boolean isPayTM() {
        return isPayTM;
    }

    public void setPayTM(boolean payTM) {
        isPayTM = payTM;
    }

    public boolean isPayTMSandbox() {
        return isPayTMSandbox;
    }

    public void setPayTMSandbox(boolean payTMSandbox) {
        isPayTMSandbox = payTMSandbox;
    }

    public String getPayTMMid() {
        return payTMMid;
    }

    public void setPayTMMid(String payTMMid) {
        this.payTMMid = payTMMid;
    }

}
