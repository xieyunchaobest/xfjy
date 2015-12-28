package com.xyc.proj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyc.proj.entity.Order;
import com.xyc.proj.global.Constants;
import com.xyc.proj.repository.OrderRepository;

@Service
public class HouseKeepingServiceImpl implements HouseKeepingService {
	@Autowired
	OrderRepository orderRepository;
	
	public void dispatchOrder(Order o) {
		Order order=orderRepository.findOne(o.getId());
		order.setServiceDate(o.getServiceDate());
		order.setWorkerId(o.getWorkerId());
		order.setState(Constants.ORDER_STATE_CONFIRMED);
		orderRepository.save(order);
	}

	public void createOrder(Order o) {
		o.setServiceType(Constants.CYCLE_TYPE_SY);
		o.setState(Constants.ORDER_STATE_UNPAY);
		o.setTotalFee(0d);
		orderRepository.save(o);
		//发送客服消息给老师
		
		//发送短信给老师
		
	}
}
