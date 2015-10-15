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

import com.xyc.proj.utility.DateUtil;


@Entity
@Table(name = "T_ORDER")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "TRADE_NO")
	private String tradeNo="";
	
	@Column(name = "OUT_TRADE_NO")
	private String outTradeNo="";
	
	@Column(name = "OPEN_ID")
	private String openId;
	
	@Column(name = "MOBILE_NO")
	private String mobileNo;
	
	@Column(name = "ADDRESS")
	private String address;
	
	@Column(name = "TOTAL_FEE")
	private Double totalFee;
	
	@Column(name = "TOOL_FEE_CLEAN")
	private Double toolFeeClean;
	
	@Column(name = "SERVICE_TYPE")
	private String servicetype; //家政，宝洁
	
	@Column(name = "CYCLE_TYPE")
	private String cycleType;//零工，包月
	
	@Column(name = "DURATION")
	private String duration;//持续时间
	
	@Column(name = "ADDRESS_ID")
	private Long addressId;
	 
	
	 
	@Column(name = "STATE")
	private String state;
	 
	
	@Column(name = "AYI_ID")
	private Long ayiId; 
	  
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME") 
	private Date createdTime=new Date();
	 
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PAY_TIME") 
	private Date payTime;
	
	 
	
	 

	public Order() {
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getMobileNo() {
		return mobileNo;
	}


	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}


	public Date getCreatedTime() {
		return createdTime;
	}


	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}


 

	public String getOutTradeNo() {
		return outTradeNo;
	}


	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}


	public String getTradeNo() {
		return tradeNo;
	}


	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

 

	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}

  

	public String getPayTime() {
		return DateUtil.Time2Str(payTime);
	}


	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}


	public Double getTotalFee() {
		return totalFee;
	}


	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}


	public String getOpenId() {
		return openId;
	}


	public void setOpenId(String openId) {
		this.openId = openId;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public Double getToolFeeClean() {
		return toolFeeClean;
	}


	public void setToolFeeClean(Double toolFeeClean) {
		this.toolFeeClean = toolFeeClean;
	}


	public String getServicetype() {
		return servicetype;
	}


	public void setServicetype(String servicetype) {
		this.servicetype = servicetype;
	}


	public String getCycleType() {
		return cycleType;
	}


	public void setCycleType(String cycleType) {
		this.cycleType = cycleType;
	}


	public String getDuration() {
		return duration;
	}


	public void setDuration(String duration) {
		this.duration = duration;
	}
 

	public Long getAyiId() {
		return ayiId;
	}


	public void setAyiId(Long ayiId) {
		this.ayiId = ayiId;
	}


	public Long getAddressId() {
		return addressId;
	}


	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

 
	 
}
