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
@Table(name = "T_STORE")
public class Store {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "ADDRESS")
	private String address;
	
	@Column(name = "STORE_PHONE")
	private String storePhone;
	
	@Column(name = "LEADER_NAME")
	private String leaderName;
	
	@Column(name = "LEADER_PHONE")
	private String leaderPhone;
	 
	public Store() {
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getLeaderName() {
		return leaderName;
	}


	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}


	public String getLeaderPhone() {
		return leaderPhone;
	}


	public void setLeaderPhone(String leaderPhone) {
		this.leaderPhone = leaderPhone;
	}


	public String getStorePhone() {
		return storePhone;
	}


	public void setStorePhone(String storePhone) {
		this.storePhone = storePhone;
	}

 
	 
}
