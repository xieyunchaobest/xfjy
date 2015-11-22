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
import javax.persistence.Transient;

import com.xyc.proj.utility.DateUtil;


@Entity
@Table(name = "T_ORDER")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "ORDER_ID")
	private String orderId="";
	
	@Column(name = "TRADE_NO")
	private String tradeNo="";
	
	@Column(name = "OUT_TRADE_NO")
	private String outTradeNo="";
	
	@Column(name = "OPEN_ID")
	private String openId;
	
	@Column(name = "MOBILE_NO")
	private String mobileNo;
	
	@Column(name = "PAY_MODE")
	private String payMode="W";
	
	@Transient
	private String payModeText="";
	
	
	@Column(name = "USER_ADDRESS_ID")
	private Long userAddressId;
	
	@Column(name = "SERVICE_DATE")
	private String serviceDate;
	
	@Column(name = "WINDOW_COUNT")
	private String windowCount;
	
	@Column(name = "BALCONY_COUNT")
	private String balconyCount;
	
	@Column(name = "LAST_DAY")
	private String lastDay;
	
	
	@Column(name = "DURATION")
	private String duration;//持续时间
	
	@Column(name = "AREA")
	private String area;//面积
	
	@Column(name = "START_TIME")
	private String startTime;
	
	@Column(name = "END_TIME")
	private String endTime;
	
	
	
	@Column(name = "UNIT_PRICE")
	private String unitPrice;
	
	@Column(name = "UNIT_PRICE_WINDOW")
	private String unitPriceWindow;
	
	@Column(name = "UNIT_PRICE_BALCONY")
	private String unitPriceBalcony;
	
	
	@Column(name = "TOTAL_FEE")
	private Double totalFee;
	
	@Column(name = "TOOL_FEE_CLEAN")
	private Double toolFeeClean;//工具费用
	
	@Column(name = "SERVICE_TYPE")
	private String serviceType; //家政，宝洁
	
	@Transient
	private String serviceTypeText; //家政，宝洁
	
	@Column(name = "CYCLE_TYPE")
	private String cycleType;//零工，包月
	
	@Transient
	private String cycleTypeText;//零工，包月
	 
	@Column(name = "DRATION_MONTH")
	private String durationMonth;
	
	@Column(name = "REPEAT_IN_WEEK")
	private String repeatInWeek;
	
	@Transient
	private String repeatInWeekText;
	
	@Transient
	private String durationMonthText;
	
	@Column(name = "DURATION_TEXT")
	private String durationText="";
	 
	@Column(name = "STATE")
	private String state;
	 
	@Transient
	private String stateText;
	
	
	@Column(name = "AYI_ID")
	private Long ayiId; 
	  
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME") 
	private Date createdTime=new Date();
	 
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PAY_TIME") 
	private Date payTime;
	
	@Column(name = "full_address") 
	private String fullAddress="";
	
	@Transient
	private String isProviceCleanTools="";
	
	

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


	public Long getUserAddressId() {
		return userAddressId;
	}


	public void setUserAddressId(Long userAddressId) {
		this.userAddressId = userAddressId;
	}


	public String getServiceDate() {
		return serviceDate;
	}


	public void setServiceDate(String serviceDate) {
		this.serviceDate = serviceDate;
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


 

	public Double getToolFeeClean() {
		return toolFeeClean;
	}


	public void setToolFeeClean(Double toolFeeClean) {
		this.toolFeeClean = toolFeeClean;
	}


 

	public String getServiceType() {
		return serviceType;
	}


	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
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


	public String getStartTime() {
		return startTime;
	}


	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}


	public String getEndTime() {
		return endTime;
	}


	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


	public String getUnitPrice() {
		return unitPrice;
	}


	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}


	public String getFullAddress() {
		return fullAddress;
	}


	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}


	public String getDurationMonth() {
		return durationMonth;
	}


	public void setDurationMonth(String durationMonth) {
		this.durationMonth = durationMonth;
	}


	public String getRepeatInWeek() {
		return repeatInWeek;
	}


	public void setRepeatInWeek(String repeatInWeek) {
		this.repeatInWeek = repeatInWeek;
	}


	public String getRepeatInWeekText() {
		return repeatInWeekText;
	}


	public void setRepeatInWeekText(String repeatInWeekText) {
		this.repeatInWeekText = repeatInWeekText;
	}


	public String getDurationMonthText() {
		return durationMonthText;
	}


	public void setDurationMonthText(String durationMonthText) {
		this.durationMonthText = durationMonthText;
	}


	public String getDurationText() {
		return durationText;
	}


	public void setDurationText(String durationText) {
		this.durationText = durationText;
	}


	public String getIsProviceCleanTools() {
		return isProviceCleanTools;
	}


	public void setIsProviceCleanTools(String isProviceCleanTools) {
		this.isProviceCleanTools = isProviceCleanTools;
	}


	public String getArea() {
		return area;
	}


	public void setArea(String area) {
		this.area = area;
	}


	public String getWindowCount() {
		return windowCount;
	}


	public void setWindowCount(String windowCount) {
		this.windowCount = windowCount;
	}


	public String getBalconyCount() {
		return balconyCount;
	}


	public void setBalconyCount(String balconyCount) {
		this.balconyCount = balconyCount;
	}


	public String getUnitPriceWindow() {
		return unitPriceWindow;
	}


	public void setUnitPriceWindow(String unitPriceWindow) {
		this.unitPriceWindow = unitPriceWindow;
	}


	public String getUnitPriceBalcony() {
		return unitPriceBalcony;
	}


	public void setUnitPriceBalcony(String unitPriceBalcony) {
		this.unitPriceBalcony = unitPriceBalcony;
	}


	public String getOrderId() {
		return orderId;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public String getPayMode() {
		return payMode;
	}


	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}


	public String getLastDay() {
		return lastDay;
	}


	public void setLastDay(String lastDay) {
		this.lastDay = lastDay;
	}


	public String getServiceTypeText() {
		return serviceTypeText;
	}


	public void setServiceTypeText(String serviceTypeText) {
		this.serviceTypeText = serviceTypeText;
	}


	public String getCycleTypeText() {
		return cycleTypeText;
	}


	public void setCycleTypeText(String cycleTypeText) {
		this.cycleTypeText = cycleTypeText;
	}


	public String getStateText() {
		return stateText;
	}


	public void setStateText(String stateText) {
		this.stateText = stateText;
	}


	public String getPayModeText() {
		return payModeText;
	}


	public void setPayModeText(String payModeText) {
		this.payModeText = payModeText;
	}


 
	 
}
