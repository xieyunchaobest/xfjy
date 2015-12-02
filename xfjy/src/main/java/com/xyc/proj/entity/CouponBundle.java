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
@Table(name = "PN_Couponbundle")
public class CouponBundle {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "COUPON_ID")
	private Long couponId;
	
	@Column(name = "U_ID")
	private Long uid;
	
	@Column(name = "TRACK_ID")
	private Long trackId;
	
	@Column(name = "ORDER_ID")
	private String orderId;
	
	@Column(name = "ALBUM_ID")
	private Long albumId;
	 
	@Column(name = "CREATE_TIME")
	@JsonSerialize(using = CustomDateSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	
	@Column(name = "UPDATE_TIME")
	@JsonSerialize(using = CustomDateSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;
	
	@Column(name = "IS_DELETE")
	private boolean isDelete=false;
	

	public CouponBundle() {
	}


	public Long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public Long getCouponId() {
		return couponId;
	}


	public void setCouponId(long couponId) {
		this.couponId = couponId;
	}


	public Long getUid() {
		return uid;
	}


	public void setUid(Long uid) {
		this.uid = uid;
	}


	public Long getTrackId() {
		return trackId;
	}


	public void setTrackId(Long trackId) {
		this.trackId = trackId;
	}


	public String getOrderId() {
		return orderId;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public Long getAlbumId() {
		return albumId;
	}


	public void setAlbumId(Long albumId) {
		this.albumId = albumId;
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


	public boolean isDelete() {
		return isDelete;
	}


	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	 
}
