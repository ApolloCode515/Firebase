package com.spark.swarajyabiz.Extra;

import com.google.gson.annotations.SerializedName;

public class PayoutRequest {
    @SerializedName("wallet_id")
    private String walletId;

    @SerializedName("beneficiary_id")
    private String beneficiaryId;

    @SerializedName("amount")
    private int amount;

    @SerializedName("currency")
    private String currency;

    @SerializedName("mode")
    private String mode;

    @SerializedName("purpose")
    private String purpose;

    @SerializedName("narration")
    private String narration;

    @SerializedName("reference_id")
    private String referenceId;

    public PayoutRequest(String walletId, String beneficiaryId, int amount, String currency, String mode, String purpose, String narration, String referenceId) {
        this.walletId = walletId;
        this.beneficiaryId = beneficiaryId;
        this.amount = amount;
        this.currency = currency;
        this.mode = mode;
        this.purpose = purpose;
        this.narration = narration;
        this.referenceId = referenceId;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(String beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }
}
