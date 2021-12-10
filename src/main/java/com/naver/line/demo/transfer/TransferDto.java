package com.naver.line.demo.transfer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import static com.naver.line.demo.common.Constants.ACCOUNT_NUMBER_PATTERN;

@Data
public class TransferDto {
    @JsonProperty("sender_account_number")
    private String senderAccountNumber;
    @JsonProperty("reeiver_account_number")
    private String receiverAccountNumber;
    private Long amount;
    @JsonProperty("sender_note")
    private String senderNote;
    @JsonProperty("receiver_note")
    private String receiverNote;

    public void validate() {
        ACCOUNT_NUMBER_PATTERN.matcher(senderAccountNumber);
        ACCOUNT_NUMBER_PATTERN.matcher(receiverAccountNumber);
        if(amount < 10) throw new IllegalArgumentException("amount 는 최소 10 이상이어야 합니다.");
    }
}
