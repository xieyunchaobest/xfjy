/**
 * All rights, including trade secret rights, reserved.
 */
package com.xyc.proj.entity;

import java.text.DecimalFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_DEPOSIT_SUMMARY")
public class DepositSummary {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "OPEN_ID")
	private String openId;
	
	@Column(name = "FEE")
	private Double fee;
	 
	
	public DepositSummary() {
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getOpenId() {
		return openId;
	}


	public void setOpenId(String openId) {
		this.openId = openId;
	}


	public Double getFee() {
		return Double.parseDouble( new DecimalFormat("#.00").format(fee));
	}


	public void setFee(Double fee) {
		this.fee = fee;
	}
 
 
	 
}
