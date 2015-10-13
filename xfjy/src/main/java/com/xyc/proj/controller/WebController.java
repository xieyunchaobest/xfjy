/**
 * All rights, including trade secret rights, reserved.
 */
package com.xyc.proj.controller;

import java.io.File;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.xyc.proj.entity.Config;
import com.xyc.proj.entity.ElectricCar;
import com.xyc.proj.entity.Question;
import com.xyc.proj.entity.Store;
import com.xyc.proj.entity.WebUser;
import com.xyc.proj.global.Constants;
import com.xyc.proj.service.ClientService;
import com.xyc.proj.service.ServerService;
import com.xyc.proj.utility.CharacterEncodingFilter;
import com.xyc.proj.utility.DateUtil;
import com.xyc.proj.utility.PageView;
import com.xyc.proj.utility.Properties;
import com.xyc.proj.utility.Result;
import com.xyc.proj.utility.StringUtil;

@Controller
public class WebController {

}
