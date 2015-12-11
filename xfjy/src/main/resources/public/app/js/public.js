/*<![CDATA[*/
var baseUrl=window.location.href;
var basePath='';
if(baseUrl.indexOf("\/xfjy\/") > 0){
	basePath='/xfjy';
}else{
	basePath="";
}

function getToday(){
	var day = new Date();
	var str = "";   
    str= day.getFullYear() ;     
    str=str+'-'+((day.getMonth()+1)>10? (day.getMonth()+1).toString():'0' +(day.getMonth()+1));   
    str=str+"-"+(day.getDate()>9?day.getDate().toString():'0' + (day.getDate()));
    return str;
}

function getCurrentHour(){
	var myDate = new Date();
	var hour= myDate.getHours()+1;;       //获取当前小时数(0-23)
	return hour;
}
/*]]>*/