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
@Table(name = "T_CLIENT_USER")
public class ClientUser {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "OPEN_ID")
	private String openId="";
	 
	
	@Column(name = "MOBILE_NO")
	private String mobileNo;
	 
	public ClientUser() {
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


	public String getOpenId() {
		return openId;
	}


	public void setOpenId(String openId) {
		this.openId = openId;
	}

 
 
	 
}
