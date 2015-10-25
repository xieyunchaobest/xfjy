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
@Table(name = "CLEAN_TOOLS")
public class CleanTools {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "USER_ID")
	private Long userId;
	
	@Column(name = "AREA_ID")
	private Long areaId;
	
	
	@Column(name = "COMMUNITY_ID")
	private Long communityId;
	
	@Column(name = "DETAIL_ADDRESS")
	private String detailAddress; 
	 

	public CleanTools() {
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}

	 
	 
}
