/**
 * Copyright (c) 2014 Sony Mobile Communications Inc.
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
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.xyc.proj.utility.CustomDateSerializer;

@Entity
@Table(name = "t_coupon")
public class Coupon {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "PROMOTION_ID")
	private Long promotionId;
	
	@Column(name = "CODE")
	private String code;
	
	@Column(name = "OPEN_ID")
	private String openId;
 
	
	@Column(name = "BATCH_CODE")
	private String batchCode;
	
	@Column(name = "BATCH_REMARK")
	private String batchRemark;
	
	@Column(name = "TYPE")
	private String type;//折扣券，代金券？
 
	@Column(name = "SERVICE_TYPE")
	private String serviceType;//适用于什么业务类型
	
	@Column(name = "DISCOUNT")
	private Double discount;
	
	@Column(name = "CASH")
	private Double cash;
	
	@Column(name = "EXPIRE_DATE")
	private String expireDate;
	
	@Column(name = "STATE")
	private String state; //删除，使用，未用
	
	@Column(name = "CREATE_TIME")
	@JsonSerialize(using = CustomDateSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime=new Date();
	
	@Column(name = "UPDATE_TIME")
	@JsonSerialize(using = CustomDateSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime=new Date();
	
	@Column(name = "TITLE")
	private String title;
	
	@Column(name = "DESCRIPTION")
	private String description;

	public Coupon() {
	}



	public long getId() {
		return id;
	}


 

	public String getOpenId() {
		return openId;
	}



	public void setOpenId(String openId) {
		this.openId = openId;
	}



	public String getServiceType() {
		return serviceType;
	}



	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}



	public void setId(long id) {
		this.id = id;
	}



	public Long getPromotionId() {
		return promotionId;
	}



	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}



	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}



	public String getBatchCode() {
		return batchCode;
	}



	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}



	public String getBatchRemark() {
		return batchRemark;
	}



	public void setBatchRemark(String batchRemark) {
		this.batchRemark = batchRemark;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}


 


	public Double getDiscount() {
		return discount;
	}



	public void setDiscount(Double discount) {
		this.discount = discount;
	}



	public Double getCash() {
		return cash;
	}



	public void setCash(Double cash) {
		this.cash = cash;
	}



	public String getExpireDate() {
		return expireDate;
	}



	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}



	public String getState() {
		return state;
	}



	public void setState(String state) {
		this.state = state;
	}



	public Date getCreateTime() {
		return createTime;
	}



	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}



	public Date getUpdateTime() {
		return updateTime;
	}



	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

 
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

 
}
