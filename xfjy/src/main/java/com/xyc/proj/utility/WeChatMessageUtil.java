package com.xyc.proj.utility;

import java.io.StringReader;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.xyc.proj.entity.Msg;

public class WeChatMessageUtil {
	public final static String text_type="text";
	
	public static String getResponeTxt(Msg msg){
		
		long currentTime=new Date().getTime();
		String fromUser="xingfujiayuanyanglao";
		String xml="<xml>"+
				   "<ToUserName><![CDATA["+msg.getToUserName()+"]]></ToUserName>"+
				   "<FromUserName><![CDATA["+fromUser+"]]></FromUserName>"+
				   "<CreateTime>"+currentTime+"</CreateTime>"+
				   "<MsgType><![CDATA["+text_type+"]]></MsgType>"+
				   "<Content><![CDATA["+msg.getContent()+"]]></Content>"+
				   "</xml>";
		return xml;
	}
	
	
	/**
	 * 接收消息
	 * @param xml
	 * @return
	 */
	public static  Msg recevieMsg(String xml) {
//		 xml="<xml>"+
//				   "<ToUserName><![CDATA[toUser]]></ToUserName>"+
//					" <FromUserName><![CDATA[fromUser]]></FromUserName>"+ 
//					 "<CreateTime>1348831860</CreateTime>"+
//					 "<MsgType><![CDATA[text]]></MsgType>"+
//					 "<Content><![CDATA[this is a test]]></Content>"+
//					 "<MsgId>1234567890123456</MsgId>"+
//					 "</xml>";
		System.out.println("xmlllllll="+xml);
		 Msg msg=new Msg();
		try { 
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			StringReader sr = new StringReader(xml);
			InputSource is = new InputSource(sr);
			Document document = db.parse(is);

			Element root = document.getDocumentElement();
			NodeList ToUserNameNl = root.getElementsByTagName("ToUserName");
			NodeList FromUserNameNl = root.getElementsByTagName("FromUserName");
			NodeList CreateTimeNl = root.getElementsByTagName("CreateTime");
			NodeList MsgTypeNl = root.getElementsByTagName("MsgType");
			NodeList ContentNl = root.getElementsByTagName("Content");
			NodeList MsgIdNl = root.getElementsByTagName("MsgId");
			NodeList EventNl = root.getElementsByTagName("Event");
			NodeList EventKeyNl = root.getElementsByTagName("EventKey");
			
			
			String toUserName = ToUserNameNl.item(0).getTextContent();
			String fromUserName=  FromUserNameNl.item(0).getTextContent();
			String createTime=  CreateTimeNl.item(0).getTextContent();
			String msgType=  MsgTypeNl.item(0).getTextContent();
			String content="";
			if(ContentNl!=null) {
				if(ContentNl.getLength()>0) {
					content=  ContentNl.item(0).getTextContent();
				}
			}
			if(EventNl!=null && EventKeyNl!=null) {
				if(EventKeyNl.getLength()>=0) {
					if(EventKeyNl.item(0).getTextContent().equals("servicePhone")) {
						content="022-60956627";
					}
				}
				
			}
			
			String msgId="";
			if(MsgIdNl!=null) {
				if(MsgIdNl.getLength()!=0) {
					msgId=MsgIdNl.item(0).getTextContent();
				}
			}
			
			msg.setToUserName(toUserName);
			msg.setFromUserName(fromUserName);
			msg.setCreateTime(createTime);
			msg.setMsgType(msgType);
			msg.setContent(content);
			msg.setMsgId(msgId);
			
			
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println("we chat parse xml error");
		}
		return msg;
	}
	
	public static void main(String args[]) {
//		String xml=WeChatMessageUtil.getResponeTxt("234234", text_type, "你好，此为测试消息！");
//		System.out.println("xml======="+xml);
//		Msg msg=WeChatMessageUtil.getMsg("");
	}
}
