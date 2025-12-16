package com.example.demo.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;


public class TransferDTO {
	private Long fromAccNo;
	private Long toAccNo;
	private BigDecimal amount;
	public Long getFromAccNo() {
		return fromAccNo;
	}
	public void setFromAccNo(Long fromAccNo) {
		this.fromAccNo = fromAccNo;
	}
	public Long getToAccNo() {
		return toAccNo;
	}
	public void setToAccNo(Long toAccNo) {
		this.toAccNo = toAccNo;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	


    // getters and setters
}

