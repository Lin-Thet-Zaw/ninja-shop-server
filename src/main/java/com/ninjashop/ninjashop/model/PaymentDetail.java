package com.ninjashop.ninjashop.model;

public class PaymentDetail {
    public PaymentDetail() {
    }

    public PaymentDetail(String paymentMethod, String paymentId, String status, String razorypayPaymentLinkId, String razorypayPaymentLinkReferenceId, String razorypayPaymentLinkStatus, String razorypayPaymentId) {
        this.paymentMethod = paymentMethod;
        this.paymentId = paymentId;
        this.status = status;
        this.razorypayPaymentLinkId = razorypayPaymentLinkId;
        this.razorypayPaymentLinkReferenceId = razorypayPaymentLinkReferenceId;
        this.razorypayPaymentLinkStatus = razorypayPaymentLinkStatus;
        this.razorypayPaymentId = razorypayPaymentId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRazorypayPaymentLinkId() {
        return razorypayPaymentLinkId;
    }

    public void setRazorypayPaymentLinkId(String razorypayPaymentLinkId) {
        this.razorypayPaymentLinkId = razorypayPaymentLinkId;
    }

    public String getRazorypayPaymentLinkReferenceId() {
        return razorypayPaymentLinkReferenceId;
    }

    public void setRazorypayPaymentLinkReferenceId(String razorypayPaymentLinkReferenceId) {
        this.razorypayPaymentLinkReferenceId = razorypayPaymentLinkReferenceId;
    }

    public String getRazorypayPaymentId() {
        return razorypayPaymentId;
    }

    public void setRazorypayPaymentId(String razorypayPaymentId) {
        this.razorypayPaymentId = razorypayPaymentId;
    }

    public String getRazorypayPaymentLinkStatus() {
        return razorypayPaymentLinkStatus;
    }

    public void setRazorypayPaymentLinkStatus(String razorypayPaymentLinkStatus) {
        this.razorypayPaymentLinkStatus = razorypayPaymentLinkStatus;
    }

    private String paymentMethod;
    private String paymentId;
    private String status;
    private String razorypayPaymentLinkId;
    private String razorypayPaymentLinkReferenceId;
    private String razorypayPaymentLinkStatus;
    private String razorypayPaymentId;
}
