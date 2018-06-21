package com.bright.apollo.service.AliRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.iot.model.v20170420.CreateProductRequest;
import com.aliyuncs.iot.model.v20170420.CreateProductResponse;
import com.aliyuncs.iot.model.v20170420.QueryDeviceByNameRequest;
import com.aliyuncs.iot.model.v20170420.QueryDeviceByNameResponse;
import com.aliyuncs.iot.model.v20170420.RegistDeviceRequest;
import com.aliyuncs.iot.model.v20170420.RegistDeviceResponse;
import com.aliyuncs.iot.model.v20170420.UpdateProductRequest;
import com.aliyuncs.iot.model.v20170420.UpdateProductResponse;
import com.bright.apollo.common.entity.TAliDevice;
import com.bright.apollo.common.entity.TAliDeviceUS;
import com.bright.apollo.enums.AliRegionEnum;
import com.bright.apollo.service.AliDeviceService;
 

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年6月20日  
 *@Version:1.1.0  
 */
@Component
public class AliService extends BaseRequest{
	@Autowired
	private AliDeviceService aliDeviceService;
	
    /**
     * 创建产品
     *
     * @param productName 产品名称
     * @param productDesc 产品描述 可空
     * @return 产品的PK
     */
    public String createProduct(String productName, String productDesc,AliRegionEnum region) {
        CreateProductRequest request = new CreateProductRequest();
        request.setName(productName);
        request.setDesc(productDesc);
        CreateProductResponse response = (CreateProductResponse)executeCmd(request,region);
        if (response != null && response.getSuccess() != false) {
        	System.out.println("创建产品成功！productKey:" + response.getProductInfo().getProductKey());
            return response.getProductInfo().getProductKey();
        } else {
        	System.out.println("创建产品失败！requestId:" + response.getRequestId() + "原因:" + response.getErrorMessage());
        }
        return null;
    }
    
    /**
     * 注册设备
     *
     * @param productKey 产品pk
     * @param deviceName 设备名称 非必须
     * @return 设备名称
     */
    public  TAliDevice registDevice(String productKey, String deviceName,AliRegionEnum region) {
        RegistDeviceRequest request = new RegistDeviceRequest();
        request.setProductKey(productKey);
        request.setDeviceName(deviceName);
        
        RegistDeviceResponse response = (RegistDeviceResponse)executeCmd(request,region);
        if (response != null && response.getSuccess() != false) {
        	TAliDevice tAliDevice = new TAliDevice();
        	tAliDevice.setDeviceName(response.getDeviceName());
        	tAliDevice.setDeviceSecret(response.getDeviceSecret());
        	tAliDevice.setProductKey(productKey);
        	if(region.equals(AliRegionEnum.AMERICA)){
            	TAliDeviceUS tAliDeviceUS = new TAliDeviceUS();
            	tAliDeviceUS.setDeviceName(response.getDeviceName());
            	tAliDeviceUS.setDeviceSecret(response.getDeviceSecret());
            	tAliDeviceUS.setProductKey(productKey);
            	try {
            		aliDeviceService.addAliDevUS(tAliDeviceUS);
    			} catch (Exception e) {
    				// TODO: handle exception
    				e.printStackTrace();
    			}
        	}else{
            	try {
            		aliDeviceService.addAliDev(tAliDevice);
    			} catch (Exception e) {
    				// TODO: handle exception
    				e.printStackTrace();
    			}
        	}

        	
            return tAliDevice;
        } else {
        	System.out.println("创建设备失败！requestId:" + response.getRequestId() + "原因：" + response.getErrorMessage());
        }
        return null;
    }
    
    /**
     * 查询单个设备信息
     *
     * @param productKey 产品PK 必需
     * @param productName 产品名称 必需
     */
    public String queryDeviceByName(String productKey, String deviceName,AliRegionEnum region) {
        QueryDeviceByNameRequest request = new QueryDeviceByNameRequest();
        request.setProductKey(productKey);
        request.setDeviceName(deviceName);
        QueryDeviceByNameResponse response = (QueryDeviceByNameResponse)executeCmd(request,region);
        if (response != null && response.getSuccess() != false) {
        	System.out.println("查询设备成功！ " + JSONObject.toJSONString(response));
        	return response.getDeviceInfo().getDeviceSecret();
        } else {
        	System.out.println("查询设备失败！ " + JSONObject.toJSONString(response));
//        	System.out.println("查询设备失败！requestId:" + response.getRequestId() + "原因：" + response.getErrorMessage());
        }
        return null;
    }
    
    /**
     * 修改产品
     *
     * @param productKey 产品PK 必需
     * @param productName 产品名称 非必需
     * @param productDesc 产品描述 非必需
     */
    public  void updateProduct(String productKey, String productName, String productDesc,AliRegionEnum region) {
        UpdateProductRequest request = new UpdateProductRequest();
        request.setProductKey(productKey);
        request.setProductName(productName);
        request.setProductDesc(productDesc);
        UpdateProductResponse response = (UpdateProductResponse)executeCmd(request,region);
        if (response != null && response.getSuccess() != false) {
        	System.out.println("修改产品成功！");
        } else {
        	System.out.println("修改产品失败！requestId:" + response.getRequestId() + "原因:" + response.getErrorMessage());
        }

    }


}
