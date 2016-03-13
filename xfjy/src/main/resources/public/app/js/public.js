/*<![CDATA[*/
var baseUrl=window.location.href;
var basePath='';
if(baseUrl.indexOf("\/xfjy\/") > 0){
	basePath='/xfjy';
}else if(baseUrl.indexOf("\/xfjytest\/") > 0){
	basePath='/xfjytest';
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
	var hour= myDate.getHours();       //获取当前小时数(0-23)
	return hour;
}

//和当前时间相差的小时数
function getHourDiff(d2,h2){
	var today=new  Date();
	
	var y=parseInt(d2.substring(0,4));
	var m=parseInt(d2.substring(5,7))-1;
	var d=parseInt(d2.substring(8,10));
	var ah=parseInt(h2);
	
	var dt=new Date(y,m,d,ah,0,0);
	 
	
	var diffmills=dt.getTime()-today.getTime()  //时间差的毫秒数
	console.info('diffmillsdiffmills='+diffmills);
	var hours=Math.floor(diffmills/(3600*1000))
	console.info('hourshours='+hours);
	return hours;
}
/*]]>*/