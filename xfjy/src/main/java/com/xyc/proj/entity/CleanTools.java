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
@Table(name = "t_clean_tools")
public class CleanTools {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "TOOL_NAME")
	private String toolName;
	
	@Column(name = "AMOUNT")
	private String amount;
	
	
	@Column(name = "UNIT")
	private String unit;
	
	@Column(name = "SPECIFICATION")
	private String specification; 
	 
	@Column(name = "SERVICE_TYPE")
	private String servieType; 

	public CleanTools() {
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getToolName() {
		return toolName;
	}


	public void setToolName(String toolName) {
		this.toolName = toolName;
	}


	public String getAmount() {
		return amount;
	}


	public void setAmount(String amount) {
		this.amount = amount;
	}


	public String getUnit() {
		return unit;
	}


	public void setUnit(String unit) {
		this.unit = unit;
	}


	public String getSpecification() {
		return specification;
	}


	public void setSpecification(String specification) {
		this.specification = specification;
	}


	public String getServieType() {
		return servieType;
	}


	public void setServieType(String servieType) {
		this.servieType = servieType;
	}

	 
	 
}
