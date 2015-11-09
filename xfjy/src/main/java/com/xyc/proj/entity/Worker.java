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
@Table(name = "T_WORKER")
public class Worker {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "CODE")
	private String code;
	
	@Column(name = "ID_NO")
	private String idNo;
	
	@Column(name = "BIRTHDAY")
	private String birthday;
	
	@Column(name = "NATIVE_PLACE")
	private String nativePlace;
	
	@Column(name = "SERVICE_TYPE")
	private String serviceType; //服务类型，家政/保捷等
	
	@Column(name = "CONSTELLATION")
	private String constellation;//星座
	
	@Column(name = "EDUCATION")
	private String education;
	
	@Column(name = "ADDRESS")
	private String address;
	
	@Column(name = "DESCRIBE")
	private String describe;
	
	@Column(name = "SALARY")
	private Double salary;
	 
	public Worker() {
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getIdNo() {
		return idNo;
	}


	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}


	public String getBirthday() {
		return birthday;
	}


	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}


	public String getNativepPlace() {
		return nativePlace;
	}


	public void setNativepPlace(String nativepPlace) {
		this.nativePlace = nativepPlace;
	}


	public String getConstellation() {
		return constellation;
	}


	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}


	public String getEducation() {
		return education;
	}


	public void setEducation(String education) {
		this.education = education;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getDescribe() {
		return describe;
	}


	public void setDescribe(String describe) {
		this.describe = describe;
	}


	public Double getSalary() {
		return salary;
	}


	public void setSalary(Double salary) {
		this.salary = salary;
	}


	public String getServiceType() {
		return serviceType;
	}


	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	
	 
}
