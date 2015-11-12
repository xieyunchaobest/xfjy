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
@Table(name = "T_SCHEDULE")
public class Schedule {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "ORDER_ID")
	private Long orderId;
	
	@Column(name = "BUSI_DATE")
	private String busiDate="";
	
	@Column(name = "START_TIME")
	private Integer startTime;
	
	@Column(name = "END_TIME")
	private Integer endTime;
	
	@Column(name = "AYI_ID")
	private Long ayiId;
	
	@Column(name = "state")
	private String  state="P";
	
	
	
	@Column(name = "REMARK")
	private String remark;
	 
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME") 
	private Date createdTime=new Date();

	public Schedule() {
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public Long getOrderId() {
		return orderId;
	}


	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}


	public String getBusiDate() {
		return busiDate;
	}


	public void setBusiDate(String busiDate) {
		this.busiDate = busiDate;
	}


	public Long getAyiId() {
		return ayiId;
	}


	public void setAyiId(Long ayiId) {
		this.ayiId = ayiId;
	}


	public Date getCreatedTime() {
		return createdTime;
	}


	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public Integer getStartTime() {
		return startTime;
	}


	public void setStartTime(Integer startTime) {
		this.startTime = startTime;
	}


	public Integer getEndTime() {
		return endTime;
	}


	public void setEndTime(Integer endTime) {
		this.endTime = endTime;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}

 
 
 
	 
}
