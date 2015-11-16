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
	
	@Column(name = "AREA_ID")
	private Long areaId; 
	
	@Column(name = "SUB_AREA_ID")
	private Long subAreaId; 
	
	@Column(name = "NATIVE_PLACE")
	private String nativePlace;
	
	@Column(name = "EDUCATION")
	private String education;
	
	@Column(name = "PHONE")
	private String phone; 
	
	@Column(name = "SERVICE_TYPE_ONE")
	private String serviceTypeOne; //服务类型，家政
	
	@Column(name = "SERVICE_TYPE_TWO")
	private String serviceTypeTwo; //服务类型，家政
	
	@Column(name = "ROLE")
	private String role; //老师，阿姨
	
	@Column(name = "TEACHER_ID")
	private Long teacherId; 
	
	@Column(name = "WORK_TIME")
	private String workTime; //白班，24H
	
	@Column(name = "STORE_ID")
	private Long storeId; 
	
	@Column(name = "CONSTELLATION")
	private String constellation;//星座
	
	
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


	public String getNativePlace() {
		return nativePlace;
	}


	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}


	public String getServiceTypeOne() {
		return serviceTypeOne;
	}


	public void setServiceTypeOne(String serviceTypeOne) {
		this.serviceTypeOne = serviceTypeOne;
	}


	public String getServiceTypeTwo() {
		return serviceTypeTwo;
	}


	public void setServiceTypeTwo(String serviceTypeTwo) {
		this.serviceTypeTwo = serviceTypeTwo;
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public Long getTeacherId() {
		return teacherId;
	}


	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public Long getStoreId() {
		return storeId;
	}


	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}


	public Long getAreaId() {
		return areaId;
	}


	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}


	public Long getSubAreaId() {
		return subAreaId;
	}


	public void setSubAreaId(Long subAreaId) {
		this.subAreaId = subAreaId;
	}


	 
}
