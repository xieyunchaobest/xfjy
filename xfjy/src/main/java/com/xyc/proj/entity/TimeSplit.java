/**
 * All rights, including trade secret rights, reserved.
 */
package com.xyc.proj.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_TIME_SPLIT")
public class TimeSplit {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "DURATION_TYPE")
	private String durationType;
	
	@Column(name = "START_TIME")
	private Integer startTime;
	
	@Column(name = "END_TIME")
	private Integer endTime;
	
	@Column(name = "CODE")
	private String code; //防止数据删除造成的影响，code为时间段的标识
	
	
	 
	public TimeSplit() {
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getDurationType() {
		return durationType;
	}


	public void setDurationType(String durationType) {
		this.durationType = durationType;
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


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}
	 
	
	
}
