package com.xyc.proj.pay;

/**
 * SDK总入口
 */
public class WXPay {
 

    /**
     * 请求支付服务
     * @param scanPayReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的数据
     * @throws Exception
     */
    public static String requestScanPayService(ScanPayReqData scanPayReqData) throws Exception{
        return new ScanPayService().request(scanPayReqData);
    }
 

}
