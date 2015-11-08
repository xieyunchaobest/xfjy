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
@Table(name = "T_USER_ADDRESS")
public class UserAddress {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "OPEN_ID")
	private String openId="";
	
	@Column(name = "USER_NAME")
	private String userName="";
	
	@Column(name = "MOBILE_NO")
	private String mobileNo="";
	
	@Column(name = "AREA_ID")
	private String areaId;
	
	@Column(name = "COMMUNITY_ID")
	private String communityId;
	 
	@Column(name = "DETAIL_ADDRESS")
	private String detailAddress;

	@Column(name = "IS_DEFAULT")
	private String isDefault;
	
	public UserAddress() {
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


	public String getMobileNo() {
		return mobileNo;
	}


	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}


	public String getAreaId() {
		return areaId;
	}


	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}


	public String getCommunityId() {
		return communityId;
	}


	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}


	public String getDetailAddress() {
		return detailAddress;
	}


	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getIsDefault() {
		return isDefault;
	}


	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

 
	 
	 
}
