/**
 * All rights, including trade secret rights, reserved.
 */
package com.xyc.proj.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.xyc.proj.entity.Area;
import com.xyc.proj.entity.Community;
import com.xyc.proj.entity.Config;
import com.xyc.proj.entity.Store;
import com.xyc.proj.entity.Worker;
import com.xyc.proj.global.Constants;
import com.xyc.proj.pay.RandomStringGenerator;
import com.xyc.proj.service.ClientService;
import com.xyc.proj.service.ServerService;
import com.xyc.proj.utility.DateUtil;
import com.xyc.proj.utility.PageView;
import com.xyc.proj.utility.Properties;
import com.xyc.proj.utility.StringUtil;

@Controller
@SessionAttributes("user")
public class ServerController {

	@Autowired
	ServerService serverService;
	@Autowired
	ClientService clientService;
	@Autowired
	Properties properties;

	@RequestMapping(value = "/server/login.html", method = { RequestMethod.POST, RequestMethod.GET })
	public String loginInit() {
		return "server/login";
	}

	@RequestMapping(value = "/server/doLogin", method = { RequestMethod.POST, RequestMethod.GET })
	public String doLogin(@RequestParam(value = "code", required = true) String code, HttpSession session,
			@RequestParam(value = "password", required = true) String password, Model model) {
		List workList = serverService.findByCodeAndPassword(code, password);
		String res = "server/index";
		if (workList == null || workList.size() == 0) {
			model.addAttribute("error", "用户名或密码错误！");
			return "server/login";
		} else {
			Worker w = (Worker) workList.get(0);
			session.setAttribute("user", w);
			model.addAttribute("user", w);
		}

		return res;
	}

	@RequestMapping(value = "/server/queryWorker.html", method = { RequestMethod.POST, RequestMethod.GET })
	public String queryWorker(@RequestParam(value = "areaId", required = false) String areaId,
			HttpSession session,
			@RequestParam(value = "storeId", required = false) String storeId,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "serviceTypeTwo", required = false) String serviceTypeTwo,
			@RequestParam(value = "role", required = false) String role,
			@RequestParam(value = "workTime", required = false) String workTime,
			@RequestParam(value = "orderId", required = false) String orderId,
			@RequestParam(value = "teacherId", required = false) String teacherId,
			@RequestParam(value = "currentPageNum", required = false, defaultValue = "1") Integer currentPageNum,
			@RequestParam(value = "orderByStr", required = false, defaultValue = "name,desc") String orderBy,
			Model model) {
		Map<String, Object> parmMap = new HashMap<String, Object>();
		parmMap.put(Constants.CURRENT_PAGENUM, currentPageNum);
		parmMap.put(Constants.ORDERBY, StringUtil.formatSortBy(orderBy));

		if (!StringUtil.isBlank(areaId)) {
			long lAreaId = Long.parseLong(areaId);
			parmMap.put("areaId", lAreaId);
		} else {
			parmMap.put("areaId", "");
		}
		
		if (!StringUtil.isBlank(storeId)) {
			long lAreaId = Long.parseLong(storeId);
			parmMap.put("storeId", lAreaId);
		} else {
			parmMap.put("storeId", "");
		}
		
		Worker w=(Worker)session.getAttribute("user");
		if(w!=null && 	Constants.WORK_ROLE_ROLE_TEACHER.equals(w.getRole())) {
			parmMap.put("teacherId", w.getId());
		}
		//如果是店长，能看自己店的老师和老师属下的员工
		if(w!=null && 	Constants.WORK_ROLE_ROLE_DIANZHANG.equals(w.getRole())) {
			parmMap.put("storeId",w.getStoreId());
		}
//		if (!StringUtil.isBlank(teacherId)) {
//			long lteacherId = Long.parseLong(teacherId);
//			parmMap.put("teacherId", lteacherId);
//		} else {
//			parmMap.put("teacherId", "");
//		}
	 

		parmMap.put("name", name);
		parmMap.put("serviceTypeTwo", serviceTypeTwo);
		parmMap.put("role", role);
		parmMap.put("workTime", workTime);
		parmMap.put("state", "A");
		PageView pageView = serverService.getWorkPage(parmMap);
		model.addAttribute("pageView", pageView);
		model.addAttribute("parms", parmMap);

		List areaList = clientService.findAreaList();
		List storeList = serverService.findStore();
		List teacherList=serverService.findWorkerByStateAndRole("A", "T");
		model.addAttribute("areaList", areaList);
		model.addAttribute("storeList", storeList);
//		model.addAttribute("teacherList", teacherList);
		if (StringUtil.isBlank(orderId)) {
			return "server/queryWorker";
		} else {
			model.addAttribute("orderId", orderId);
			return "server/orderDispatch";
		}

	}

	@RequestMapping(value = "/server/dispatchInit.html", method = { RequestMethod.POST, RequestMethod.GET })
	public String dispatchInit(@RequestParam(value = "areaId", required = false) String areaId,
			@RequestParam(value = "storeId", required = false) String storeId,
			@RequestParam(value = "busiDate", required = true) String busiDate,
			@RequestParam(value = "startTime", required = true) String startTime,
			@RequestParam(value = "druation", required = false) String druation,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "serviceTypeTwo", required = false) String serviceTypeTwo,
			@RequestParam(value = "role", required = false) String role,
			@RequestParam(value = "durationMonth", required = false) String durationMonth,
			@RequestParam(value = "workTime", required = false) String workTime,
			@RequestParam(value = "orderId", required = true) String orderId,
			@RequestParam(value = "currentPageNum", required = false, defaultValue = "1") Integer currentPageNum,
			@RequestParam(value = "orderByStr", required = false, defaultValue = "name,desc") String orderBy,
			HttpSession session,
			Model model) {
		Worker w=(Worker)session.getAttribute("user");
		Map<String, Object> parmMap = new HashMap<String, Object>();
		parmMap.put(Constants.CURRENT_PAGENUM, currentPageNum);
		parmMap.put(Constants.ORDERBY, StringUtil.formatSortBy(orderBy));

		if (!StringUtil.isBlank(areaId)) {
			long lAreaId = Long.parseLong(areaId);
			parmMap.put("areaId", lAreaId);
		} else {
			parmMap.put("areaId", "");
		}
		if (!StringUtil.isBlank(storeId)) {
			long lstoreId = Long.parseLong(storeId);
			parmMap.put("storeId", lstoreId);
		}

		parmMap.put("name", name);
		//parmMap.put("serviceTypeTwo", serviceTypeTwo);
		parmMap.put("role", "A");
		parmMap.put("serviceTypeOne", Constants.WORK_SERVICE_TYPE_CLEAN);
		parmMap.put("workTime", workTime);
		parmMap.put("durationMonth", durationMonth);
		parmMap.put("state", "A");
		parmMap.put("workStateNot", "L");
		if(w!=null && 	Constants.WORK_ROLE_ROLE_TEACHER.equals(w.getRole())) {
			parmMap.put("teacherId", w.getId());
		}
		//如果是店长，能看自己店的老师和老师属下的员工
		if(w!=null && 	Constants.WORK_ROLE_ROLE_DIANZHANG.equals(w.getRole())) {
			parmMap.put("storeId",w.getStoreId());
		}
		
		
		PageView pageView = serverService.getWorkPage(parmMap);
		model.addAttribute("pageView", pageView);
		model.addAttribute("parms", parmMap);
		String workName = serverService.getDispatchedWorkerName(Long.parseLong(orderId));

		List areaList = clientService.findAreaList();
		List storeList = serverService.findStore();
		model.addAttribute("areaList", areaList);
		model.addAttribute("storeList", storeList);
		model.addAttribute("orderId", orderId);
		model.addAttribute("workerName", workName);
		model.addAttribute("busiDate", busiDate);
		model.addAttribute("startTime", startTime);
		model.addAttribute("druation", druation);
		model.addAttribute("durationMonth", durationMonth);
		return "server/orderDispatch";

	}

	@RequestMapping(value = "/server/updateWorkInit.html", method = { RequestMethod.POST, RequestMethod.GET })
	public String updateWorkInit(Model model, @RequestParam(value = "workerId", required = false) Long workerId) {
		Worker woker = serverService.getWorker(workerId);
		model.addAttribute("worker", woker);
		return "server/updateWorkInit";
	}

	@ResponseBody
	@RequestMapping(value = "/server/deleteWorker", method = { RequestMethod.POST, RequestMethod.GET })
	public String deleteWorker(Model model, @RequestParam(value = "workerIds", required = false) String workerIds) {
		String res = "S";
		try {
			serverService.deleteWorker(workerIds);
		} catch (Exception e) {
			res = "F";
		}
		return res;
	}

	@RequestMapping(value = "/server/getWorkerTimeSheet.html", method = { RequestMethod.POST, RequestMethod.GET })
	public String getWorkerTimeSheet(Model model, @RequestParam(value = "workerId", required = true) Long workerId) {
		List timeList = serverService.getWorkerTimeSheet(workerId);
		model.addAttribute("timesheet", timeList);
		return "server/timesheet";
	}

	@RequestMapping(value = "/server/queryOrder.html", method = { RequestMethod.POST, RequestMethod.GET })
	public String queryOrder(Model model,
			@RequestParam(value = "areaId", required = false, defaultValue = "0") Long areaId,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "serviceTypeOne", required = false) String serviceTypeOne,
			@RequestParam(value = "serviceDate", required = false) String serviceDate,
			@RequestParam(value = "serviceType", required = false) String serviceType,
			@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "mobileNo", required = false) String mobileNo,
			@RequestParam(value = "finished", required = false) String finished,
			@RequestParam(value = "currentPageNum", required = false, defaultValue = "1") Integer currentPageNum,
			@RequestParam(value = "orderByStr", required = false, defaultValue = "name,desc") String orderBy,
			HttpSession session) {
		Map<String, Object> parmMap = new HashMap<String, Object>();
		parmMap.put(Constants.CURRENT_PAGENUM, currentPageNum);
		parmMap.put(Constants.ORDERBY, StringUtil.formatSortBy(orderBy));
		parmMap.put("areaId", areaId);
//		if(StringUtil.isBlank(startTime)) {
//			startTime=DateUtil.getYesterday();
//		}
//		if(StringUtil.isBlank(endTime)) {
//			endTime=DateUtil.getToday();
//		}
		parmMap.put("startTime", startTime);
		parmMap.put("endTime", endTime);
		parmMap.put("serviceDate", serviceDate);
		parmMap.put("serviceType", serviceType);
		parmMap.put("state", state);
		parmMap.put("mobileNo", mobileNo);
		parmMap.put("finished", finished);
		parmMap.put("serviceTypeOne", serviceTypeOne);
		Worker w=(Worker)session.getAttribute("user");
//		if(w!=null && (Constants.WORK_SERVICE_TYPE_CLEAN.equals(w.getServiceTypeOne())  ||
//				StringUtil.isBlank(w.getServiceTypeOne())
//				)) {//如果是宝洁，或者非业务人员，则不限制老师
//			
//		}else if(w!=null && Constants.WORK_SERVICE_TYPE_JZ.equals(w.getServiceTypeOne())  ){//如果是家政
//			if( Constants.WORK_ROLE_ROLE_TEACHER.equals(w.getRole())) {//如果是老师
//				parmMap.put("teacherId", w.getId());
//			}else if( Constants.WORK_ROLE_ROLE_DIANZHANG.equals(w.getRole())) {
//				parmMap.put("role", Constants.WORK_ROLE_ROLE_DIANZHANG);
//				parmMap.put("teacherId", w.getId());
//			}
//		}
		  
		if(w!=null && (Constants.WORK_ROLE_ROLE_DIANZHANG.equals(w.getRole()) )){//如果是店长，则指能看到该店的担子
			parmMap.put("role", Constants.WORK_ROLE_ROLE_DIANZHANG);
			parmMap.put("teacherId", w.getId());
		}else if(w!=null && (Constants.WORK_ROLE_ROLE_TEACHER.equals(w.getRole()) )) {//如果是老师
			parmMap.put("role", Constants.WORK_ROLE_ROLE_TEACHER);
			parmMap.put("teacherId", w.getId());
			if(w.getServiceTypeOne().equals(Constants.WORK_SERVICE_TYPE_CLEAN)) {
				parmMap.put("serviceTypeOne", "BJ");
			}else {
				parmMap.put("serviceTypeOne", "JZ");
			}
		}
				
		List areaList = clientService.findAreaList();

		PageView pageView = serverService.getOrderPageView(parmMap);
		model.addAttribute("orderPageViews", pageView);
		model.addAttribute("parms", parmMap);
		model.addAttribute("areaId", areaId);
		model.addAttribute("areaList", areaList);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("serviceDate", serviceDate);
		model.addAttribute("serviceType", serviceType);
		model.addAttribute("state", state);
		model.addAttribute("mobileNo", mobileNo);
		model.addAttribute("finished",finished);
		model.addAttribute("serviceTypeOne", serviceTypeOne);
		return "server/queryOrder";

	}

	@ResponseBody
	@RequestMapping(value = "/server/dispatchOrder.html", method = { RequestMethod.POST, RequestMethod.GET })
	public String dispatch(Model model, HttpServletRequest request,
			@RequestParam(value = "orderId", required = true) Long orderId,
			@RequestParam(value = "busiDate", required = true) String busiDate,
			@RequestParam(value = "startTime", required = true) String startTime) {
		String res = "S";
		try {
			List mList = new ArrayList();
			for (int i = 0; i < 16; i++) {
				String aid = request.getParameter("aid" + i);
				if (!StringUtil.isBlank(aid)) {
					String druation = request.getParameter("druation" + i);
					Map w = new HashMap();
					w.put("aid", aid);
					w.put("druation", druation);
					mList.add(w);
				}
			}
			serverService.dispatchOrder(orderId, mList);
		} catch (Exception e) {
			e.printStackTrace();
			res = "E";
		}
		return res;
	}

	@RequestMapping(value = "/server/queryCommunity.html", method = { RequestMethod.POST, RequestMethod.GET })
	public String queryCommunity(Model model, @RequestParam(value = "areaId", required = false) Long areaId,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "currentPageNum", required = false, defaultValue = "1") Integer currentPageNum) {
		Map<String, Object> parmMap = new HashMap<String, Object>();
		parmMap.put(Constants.CURRENT_PAGENUM, currentPageNum);
		parmMap.put("areaId", areaId);
		parmMap.put("name", name);
		List areaList = clientService.findAreaList();

		PageView pageView = serverService.getCommunityPage(parmMap);
		model.addAttribute("pageView", pageView);
		model.addAttribute("parms", parmMap);
		model.addAttribute("areaList", areaList);
		return "server/queryCommunity";
	}

	@RequestMapping(value = "/server/queryClientUser.html", method = { RequestMethod.POST, RequestMethod.GET })
	public String queryClientUser(Model model, @RequestParam(value = "mobileNo", required = false) String mobileNo,
			@RequestParam(value = "currentPageNum", required = false, defaultValue = "1") Integer currentPageNum) {
		Map<String, Object> parmMap = new HashMap<String, Object>();
		parmMap.put(Constants.CURRENT_PAGENUM, currentPageNum);
		parmMap.put("mobileNo", mobileNo);

		PageView pageView = serverService.getClientUserPage(parmMap);
		model.addAttribute("pageView", pageView);
		model.addAttribute("parms", parmMap);
		return "server/queryClientUser";
	}
	
	@RequestMapping(value = "/server/workerAddInit.html", method = { RequestMethod.POST, RequestMethod.GET })
	public String workerAddInit(Model model, @RequestParam(value = "wid", required = false) String wid,
			HttpSession session
			) {
		Worker w=new Worker();
		if(!StringUtil.isBlank(wid)) {
			w=serverService.findWorker(Long.parseLong(wid));
		}
		List areaList = clientService.findAreaList();
		
		List storeList = serverService.findStore();
		Worker worker=(Worker)session.getAttribute("user");
		if(Constants.WORK_ROLE_ROLE_TEACHER.equals(worker.getRole()) || Constants.WORK_ROLE_ROLE_DIANZHANG.equals(worker.getRole()) ) {//如果当前登陆的是店长或者阿姨，则添加员工的时候门店只能选择自己所在的门店
			storeList=new ArrayList();
			Store s=serverService.getStore(worker.getStoreId());
			storeList.add(s);
		}
		if(Constants.WORK_ROLE_ROLE_TEACHER.equals(worker.getRole()) || Constants.WORK_ROLE_ROLE_DIANZHANG.equals(worker.getRole()) ) {//如果当前登陆的是店长或者阿姨，则添加员工的时候门店只能选择自己所在的区域
			areaList=new ArrayList();
			Area a=serverService.getArea(worker.getAreaId());
			areaList.add(a);
		}
			
			
		
		List teacheList=serverService.findTeachers();
		
		if(Constants.WORK_ROLE_ROLE_TEACHER.equals(worker.getRole())) {// 如果是老师，则添加阿姨的时候老师只能是自己
			teacheList=new ArrayList();
			teacheList.add(worker);
		}else if(Constants.WORK_ROLE_ROLE_DIANZHANG.equals(worker.getRole())) {//店长 
			teacheList=new ArrayList();
			teacheList.add(worker);
			List tList=serverService.findWorkerByTeacherId(worker.getId());
			teacheList.addAll(tList);
		}else if(Constants.WORK_ROLE_ROLE_GUANLIYUAN.equals(worker.getRole())) {//如果是管理员 ，老师是店长和老师的合集
			List dList=serverService.findDianZhang();
			teacheList.addAll(dList);
		}
		model.addAttribute("teacherList", teacheList);
		model.addAttribute("worker", w);
		model.addAttribute("areaList", areaList);
		model.addAttribute("storeList", storeList);
		return "server/workerAdd";
	}
	
	@RequestMapping(value = "/server/saveWorker", method = { RequestMethod.POST, RequestMethod.GET })
	public String saveWorker(Model model, @RequestParam(value = "wid", required = false) String wid,
			@RequestParam(value = "afile", required = false) MultipartFile file,
			@RequestParam(value = "aheadphoto", required = false) MultipartFile headphoto,
			HttpServletRequest request,HttpServletResponse response,
			@ModelAttribute("worker") Worker worker
			) throws IOException {
		System.out.println("msdfasfsfasdfd");
		response.setContentType("text/html; charset=utf-8"); 
		 PrintWriter out = response.getWriter();
		String ares="redirect:/server/queryWorker.html";
		String ures="err";
		String imgpath=properties.getFileuploadpath();
		
		String fileName=String.valueOf(new Date().getTime());
		String originalFilename=file.getOriginalFilename();
		 
		String fileEnd="";
		if(!StringUtil.isBlank(originalFilename)) {
			fileEnd=originalFilename.substring(originalFilename.indexOf("."));
		}
		try {
			 if(worker.getId()>0l) {//更新
				Worker w=serverService.findWorker(worker.getId());
					String imgName=w.getPhoto();
					if(!StringUtil.isBlank(fileEnd)) {//如果重新提交
						if(!StringUtil.isBlank(imgName)) {//如果原来就有图片，只需要复制图片就行
							File targetFile = new File(imgpath, imgName);
							if(file.getSize()>3072000) {
								out.println("<script>alert('保存失败，文件大小超过限制！');</script>");
								return ures;
							}
							file.transferTo(targetFile);
						}else {//如果原来没有图片，复制图片，并插入数据库
							String fillwholename=fileName+fileEnd;
							File targetFile = new File(imgpath, fillwholename);
							if(file.getSize()>3072000) {
								out.println("<script>alert('保存失败，文件大小超过限制！');</script>");
								return ures;
							}
							file.transferTo(targetFile);
							worker.setPhoto(fillwholename);
						}
					}else {
						worker.setPhoto(w.getPhoto());
					}
			 }else {
				 if(!StringUtil.isBlank(originalFilename)) {
					 String fillwholename=fileName+fileEnd;
						File targetFile = new File(imgpath, fillwholename);
						if(file.getSize()>3072000) {
							out.println("<script>alert('保存失败，文件大小超过限制！');</script>");
							return ures;
						}
						file.transferTo(targetFile);
						worker.setPhoto(fillwholename);
				 }
			 }
			 
			 String fileName4Head=RandomStringGenerator.getRandomStringByLength(12);
				String originalFilename4Head=headphoto.getOriginalFilename();
				String fileEnd4Head="";
				if(!StringUtil.isBlank(originalFilename4Head)) {
					fileEnd4Head=originalFilename4Head.substring(originalFilename4Head.indexOf("."));
				}
					 if(worker.getId()>0l) {//更新
						Worker w=serverService.findWorker(worker.getId());
							String imgName=w.getHeadphoto();
							if(!StringUtil.isBlank(fileEnd4Head)) {//如果重新提交
								if(!StringUtil.isBlank(imgName)) {//如果原来就有图片，只需要复制图片就行
									File targetFile = new File(imgpath, imgName);
									if(headphoto.getSize()>3072000) {
										out.println("<script>alert('保存失败，文件大小超过限制！');</script>");
										return ures;
									}
									headphoto.transferTo(targetFile);
								}else {//如果原来没有图片，复制图片，并插入数据库
									String fillwholename=fileName4Head+fileEnd4Head;
									File targetFile = new File(imgpath, fillwholename);
									if(headphoto.getSize()>3072000) {
										out.println("<script>alert('保存失败，文件大小超过限制！');</script>");
										return ures;
									}
									headphoto.transferTo(targetFile);
									worker.setHeadphoto(fillwholename);
								}
							}else {
								worker.setHeadphoto(w.getHeadphoto());
							}
					 }else {
						 if(!StringUtil.isBlank(originalFilename4Head)) {
							 String fillwholename=fileName4Head+fileEnd4Head;
								File targetFile = new File(imgpath, fillwholename);
								if(headphoto.getSize()>3072000) {
									out.println("<script>alert('保存失败，文件大小超过限制！');</script>");
									return ures;
								}
								headphoto.transferTo(targetFile);
								worker.setHeadphoto(fillwholename);
						 }
					 }
			serverService.saveWorker(worker);
		}catch(Exception e) {
			e.printStackTrace();
			ares="err";
		}
//		 
		return ares;
	}
	
	
	@RequestMapping(value = "/server/communityAddInit.html", method = { RequestMethod.POST, RequestMethod.GET })
	public String communityAddInit(Model model, @RequestParam(value = "wid", required = false) String wid
			) {
		Community w=new Community();
		if(!StringUtil.isBlank(wid)) {
			w=serverService.findCommunity(Long.parseLong(wid));
		}
		List areaList = clientService.findAreaList();
		model.addAttribute("areaList", areaList);
		 
		model.addAttribute("commu", w);
		return "server/communityAdd";
	}
	
	
	@RequestMapping(value = "/server/saveCommu", method = { RequestMethod.POST, RequestMethod.GET })
	public String saveCommu(Model model,
			@ModelAttribute("community") Community community
			) {
		String ares="redirect:/server/queryCommunity.html";
		
		try {
			serverService.saveCommunity(community);
		}catch(Exception e) {
			e.printStackTrace();
			ares="err";
		}
		 
		return ares;
	}
	
	@ResponseBody
	@RequestMapping(value = "/server/deleteCommu", method = { RequestMethod.POST, RequestMethod.GET })
	public String deleteCommu(Model model, @RequestParam(value = "CommuIds", required = false) String CommuIds) {
		String res = "S";
		try {
			serverService.deleteCommu(CommuIds);
		} catch (Exception e) {
			res = "F";
		}
		return res;
	}
	
	@RequestMapping(value ="/server/showConfig.html",method = {RequestMethod.GET, RequestMethod.POST})
	public String showConfig( Model model, 
			HttpServletRequest request) {
		Map configMap=serverService.getConfigMap();
		model.addAttribute("configMap", configMap);
		return "server/configsetting";
	}
	
	@RequestMapping(value ="/server/updateConfig",method = {RequestMethod.GET, RequestMethod.POST})
	public String updateConfig( Model model, 
			HttpServletRequest request) {
		String PTBJDJ=request.getParameter("PTBJDJ");
		String DBJDJ=request.getParameter("DBJDJ");
		String CNCDJ=request.getParameter("CNCDJ");
		String CYTDJ=request.getParameter("CYTDJ");
		String KHDJ=request.getParameter("KHDJ");
		String QJGJFY4PTBJ=request.getParameter("QJGJFY4PTBJ");
		String QJGJFY4DBJ=request.getParameter("QJGJFY4DBJ");
		String QJGJFY4CBL=request.getParameter("QJGJFY4CBL");
		String QJGJFY4KH=request.getParameter("QJGJFY4KH");
		
		Config cPTBJDJ=serverService.getConfigByCode("PTBJDJ");
		cPTBJDJ.setConfigValue(PTBJDJ);
		
		Config cDBJDJ=serverService.getConfigByCode("DBJDJ");
		cDBJDJ.setConfigValue(DBJDJ);
		
		Config cCNCDJ=serverService.getConfigByCode("CNCDJ");
		cCNCDJ.setConfigValue(CNCDJ);
		
		Config cCYTDJ=serverService.getConfigByCode("CYTDJ");
		cCYTDJ.setConfigValue(CYTDJ);
		
		Config cKHDJ=serverService.getConfigByCode("KHDJ");
		cKHDJ.setConfigValue(KHDJ);
		
		Config cQJGJFY4PTBJ=serverService.getConfigByCode("QJGJFY4PTBJ");
		cQJGJFY4PTBJ.setConfigValue(QJGJFY4PTBJ);
		
		Config cQJGJFY4DBJ=serverService.getConfigByCode("QJGJFY4DBJ");
		cQJGJFY4DBJ.setConfigValue(QJGJFY4DBJ);
		
		Config cQJGJFY4CBL=serverService.getConfigByCode("QJGJFY4CBL");
		cQJGJFY4CBL.setConfigValue(QJGJFY4CBL);
		
		Config cQJGJFY4KH=serverService.getConfigByCode("QJGJFY4KH");
		cQJGJFY4KH.setConfigValue(QJGJFY4KH);
		
		serverService.saveConfig(cPTBJDJ);
		serverService.saveConfig(cDBJDJ);
		serverService.saveConfig(cCNCDJ);
		serverService.saveConfig(cCYTDJ);
		serverService.saveConfig(cKHDJ);
		serverService.saveConfig(cQJGJFY4PTBJ);
		serverService.saveConfig(cQJGJFY4DBJ);
		serverService.saveConfig(cQJGJFY4CBL);
		serverService.saveConfig(cQJGJFY4KH);
		
		return "redirect:/server/showConfig.html";
	}
	
	
	@RequestMapping(value ="/server/updatePwdInit.html",method = {RequestMethod.GET, RequestMethod.POST})
	public String updatePwd( Model model) {
		return "server/updatePwd";
	}
	
	
	@RequestMapping(value = "/server/workerStateUpdateInit.html", method = { RequestMethod.POST, RequestMethod.GET })
	public String workerStateUpdateInit(@RequestParam(value = "areaId", required = false) String areaId,
			@RequestParam(value = "wid", required = false) Long wid,
			Model model) {
		Worker ww=serverService.findWorker(wid);
		Worker w=serverService.findWorkerDetail(ww);
		Store store=new Store();
		if(w.getStoreId()!=null) {
			store=serverService.getStore(w.getStoreId());
		}
		
		model.addAttribute("worker", w);
		model.addAttribute("store", store);
		return "server/workerStateUpdate";

	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/server/updateWorkerState", method = { RequestMethod.POST, RequestMethod.GET })
	public String updateWorkerState(Model model, HttpServletRequest request,
			@RequestParam(value = "wid", required = true) Long wid,
			@RequestParam(value = "workState", required = true) String workState
			) {
		String res = "S";
		try {
			 Worker w=serverService.findWorker(wid);
			 w.setWorkState(workState);
			 serverService.saveWorker(w);
		} catch (Exception e) {
			e.printStackTrace();
			res = "E";
		}
		return res;
	}
	
	/**
	 * 校验该门店中是否已经存在店长
	 * @param model
	 * @param request
	 * @param wid
	 * @param workState
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/server/validateDzExists", method = { RequestMethod.POST, RequestMethod.GET })
	public boolean validateDzExists(Model model, HttpServletRequest request,
			@RequestParam(value = "storeId", required = true) Long storeId,
			@RequestParam(value = "role", required = true) String role,
			@RequestParam(value = "wid", required = true) String id
			) {
		boolean res = true;
		try {
			 if(!role.equals(Constants.WORK_ROLE_ROLE_DIANZHANG))return true;
			 if(Long.parseLong(id)==0l) {//添加
				 List alist=serverService.findByRoleAndStoreIdAndState(Constants.WORK_ROLE_ROLE_DIANZHANG, storeId, Constants.STATE_A);
				 if(alist==null || alist.size()>0) {
					 return false;
				 }
			 }else {//修改需要除去当前记录
				 List alist=serverService.findByRoleAndStoreIdAndStateAndIdNot(Constants.WORK_ROLE_ROLE_DIANZHANG, storeId,Constants.STATE_A, Long.parseLong(id));
				 if(alist==null || alist.size()>0) {
					 return false;
				 }
			 }
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	
	
	
			
	
	@RequestMapping(value ="/server/updatePwd.html",method = {RequestMethod.GET, RequestMethod.POST})
	public String updatePwd( Model model, 
			HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "originalPwd", required = true) String originalPwd,
			@RequestParam(value = "newPwd", required = true) String newPwd,
			HttpSession session) throws IOException {
		 response.setContentType("text/html; charset=utf-8");  
		 PrintWriter out = response.getWriter();
		Worker w=(Worker)session.getAttribute("user");
		String res=serverService.updatePwd(w, originalPwd, newPwd);
		if(res.equals("F")) {
			out.println("<script>alert('修改失败，请检查输入！');</script>");
		}else {
			out.println("<script>alert('修改成功！');</script>");
		}
		return "server/updatePwd";
	}
	
}
