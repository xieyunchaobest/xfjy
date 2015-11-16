/**
 * All rights, including trade secret rights, reserved.
 */
package com.xyc.proj.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "T_DEPOSIT_LOG")
public class DepositLog {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "OPEN_ID")
	private String openId;
	
	@Column(name = "DEPOSIT_AMOUNT")
	private Double depositAmount;
	
	@Column(name = "BALANCE")
	private Double balance;
	
	@Column(name = "ORDER_ID")
	private String orderId;//消费时记录哪个订单
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME") 
	private Date createdTime=new Date();
	 
	
	public DepositLog() {
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


	public Double getDepositAmount() {
		return depositAmount;
	}


	public void setDepositAmount(Double depositAmount) {
		this.depositAmount = depositAmount;
	}


	public Date getCreatedTime() {
		return createdTime;
	}


	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}


	public Double getBalance() {
		return balance;
	}


	public void setBalance(Double balance) {
		this.balance = balance;
	}


	public String getOrderId() {
		return orderId;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
 
 
	 
}
