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
@Table(name = "t_order_worker")
public class OrderWorker {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "ORDER_ID")
	private Long orderId;
	
	@Column(name = "WORKER_ID")
	private Long workerId;
	 
	
	
	 
	public OrderWorker() {
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


	public Long getWorkerId() {
		return workerId;
	}


	public void setWorkerId(Long workerId) {
		this.workerId = workerId;
	}
 

 
 
	 
}
