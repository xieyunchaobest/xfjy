package com.xyc.proj.service;

import java.text.DecimalFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyc.proj.entity.ClientUser;
import com.xyc.proj.entity.Order;
import com.xyc.proj.entity.OrderWorker;
import com.xyc.proj.entity.Worker;
import com.xyc.proj.global.Constants;
import com.xyc.proj.pay.Configure;
import com.xyc.proj.repository.ClientUserRepository;
import com.xyc.proj.repository.OrderRepository;
import com.xyc.proj.repository.OrderWorkerRepository;
import com.xyc.proj.repository.WorkerRepository;

@Service
public class HouseKeepingServiceImpl implements HouseKeepingService {
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	ServerService serverService;
	@Autowired
	ClientUserRepository clientUserRepository;
	@Autowired
	WorkerRepository workerRepository;
	@Autowired
	ClientService clientService;
	@Autowired
	OrderWorkerRepository orderWorkerRepository;
	
	public void dispatchOrder(Order o) {
		Order order=orderRepository.findOne(o.getId());
		order.setServiceDate(o.getServiceDate());
		order.setWorkerId(o.getWorkerId());
		order.setState(Constants.ORDER_STATE_CONFIRMED);
		orderRepository.save(order);
		//插入订单、员工关系表，先删除，后插入
		List<OrderWorker> orderWorkerList=orderWorkerRepository.findByOrderId(o.getId());
		orderWorkerRepository.delete(orderWorkerList);
		OrderWorker ow=new OrderWorker();
		ow.setOrderId(o.getId());
		ow.setWorkerId(o.getWorkerId());
		orderWorkerRepository.save(ow);
		
		//发送短信给客户
//		String content="您的订单已确认,请尽快进入微平台在我的订单中完成支付。";
//		clientService.sendShortMsg(order.getMobileNo(), content);
		
	}

	public void createOrder(Order o) {
		o.setServiceType(Constants.SERVICE_TYPE_JZ);
		o.setState(Constants.ORDER_STATE_UNPAY);
		Worker worker=serverService.findWorker(o.getWorkerId());
		double salary=worker.getSalary();
		double totalFee=Double.parseDouble( new DecimalFormat("#.00").format(salary/22.0d*3d*1.2d)) ;
		o.setTotalFee(totalFee);
		o.setFirstPayAmount(totalFee);
		orderRepository.save(o);
		//发送客服消息给老师
		String loginurl = "http://weixin.tjxfjz.com/xfjy/client/orderDetail4jz.html";
		String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + Configure.appID
				+ "&redirect_uri=" + loginurl
				+ "&response_type=code&scope=snsapi_base&state=1#wechat_redirect";
		Worker teacher=workerRepository.findOne(o.getTeacherId());
		
		//ClientUser cu=clientUserRepository.findByMobileNo(teacher.getPhone());
//		MsgUtil.sendTemplateMsg(Constants.MSG_KF_TEMPLATE_ID, cu.getOpenId(), url, "您有新的任务",
//				o.getFullAddress(), "订单确认", "点击查看详情");
//		Worker ayi=workerRepository.findOne(o.getWorkerId());
//		//发送短信给老师
//		//客户{xxxxxxxxxxxxxxxx}电话{xxxxxxxxxxxxxxxx}预订阿姨{xxxxxxxxxxxxxxxx}日期{xxxxxxxxxxxxxxxx}的家政试用订单已支付完成{x}请尽快安排阿姨上门服务{x}
//		String content="您收到客户"+o.getName()+",电话"+o.getMobileNo()+",预订阿姨"+ayi.getName()+",日期"+o.getServiceDate()+",的家政试用订单,请尽快进入待办中查看";
//		clientService.sendShortMsg(teacher.getPhone(), content);
	}
	
	public Order getOrder(Long oid) {
		Order o=orderRepository.findOne(oid);
		Worker worker=serverService.findWorker(o.getWorkerId());
		double salary=worker.getSalary();
		double totalFee=Double.parseDouble( new DecimalFormat("#.00").format(salary/22.0d*3d*1.2d)) ;
		o.setTotalFee(totalFee);
		return o;
	}
	
	public static void main(String args[]) {
		Order o=new Order();
		o.setName("王思思");
		o.setMobileNo("18766787656");
		o.setServiceDate("2014-12-23");
		Worker ayi=new Worker();
		ayi.setName("史蒂夫");
		Worker teacher=new Worker();
		teacher.setPhone("15110093297");
		//String content="客户"+o.getName()+",电话"+o.getMobileNo()+",预订阿姨"+ayi.getName()+",日期"+o.getServiceDate()+"的家政试用订单已支付完成,请尽快安排阿姨上门服务.";
		//String content="您收到客户"+o.getName()+",电话"+o.getMobileNo()+",预订阿姨"+ayi.getName()+",日期"+o.getServiceDate()+",的家政试用订单,请尽快进入待办中查看";
		String content="您的订单已确认,请尽快进入微平台在我的订单中完成支付。";
		new ClientServiceImpl().sendShortMsg(teacher.getPhone(), content);
	}
}
