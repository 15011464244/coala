package com.koala.emm.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Message;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.koala.emm.app.EmmApplication;
import com.koala.emm.basicdata.BasicDataService;
import com.koala.emm.constant.Constant;
import com.koala.emm.model.PolicyUpdateModel;
import com.koala.emm.tools.NetHelper;
import com.koala.emm.tools.PhoneMessageUtils;
import com.koala.emm.util.DeviceUtil;
import com.koala.emm.util.SpfsUtil;
import com.lidroid.xutils.util.LogUtils;

/**
 * @author wanjun  2015年6月5日,下午2:29:10
 * 策略更新处理类
 */
public class PolicyUpdate {
	
	private static JSONObject obj;
	private static String result;
	/**
	 * 电量
	 */
	public static String ELECTRICITY = "1";
	/**
	 * 流量
	 */
	public static String FLOW="2";
	/**
	 * 内存
	 */
	public static String MEMORY="3";
	
	/**
	 * 策略存储
	 * @return
	 */
	public static void strategyStorage(final PhoneMessageUtils pmu,final String policyType){
		if(DeviceUtil.isNetwork()){
			RequestQueue queue = EmmApplication.VolleyQueue();
			StringRequest stringRequest = new StringRequest(Method.POST,Constant.strategyStorage,  
	                new Response.Listener<String>() {  
	                    @Override  
	                    public void onResponse(String result) { 
	                    	synchronized(""){
	        					LogUtils.e("策略更新下行参数："+result);
	        					String returnResult="";
	        					if(null!=result&&!"".equals(result)&&!"请求服务器失败".equals(result)){
	        						JSONObject resultObj = JSONObject.parseObject(result);
	        						String resultList = resultObj.getString("result");
	        						List<PolicyUpdateModel> list =JSONObject.parseArray(resultList, PolicyUpdateModel.class);
	        						buildStrategyStorage(list,policyType);
	        						returnResult = "策略存储成功";
	        					}else{
	        						returnResult = "查询结果为空";
	        					}
	        					LogUtils.e(returnResult);
	        				}
	                    }  
	                }, new Response.ErrorListener() {  
	                    @Override  
	                    public void onErrorResponse(VolleyError error) {  
	                        LogUtils.e("网络连接超时");
	                    }  
	                }){
						@Override
					    protected Map<String, String> getParams() {
					        //在这里设置需要post的参数
							JSONObject obj = new JSONObject();
							obj.put("vendor", pmu.getmEquipmentmanufacturers());//厂商
							obj.put("device_model", pmu.getmAnlagentyp());//设备型号
							obj.put("system_model", pmu.getmSystemVersion());//系统型号
							obj.put("app_edition", pmu.getmAppVersion());//应用版本号
							obj.put("strategyUpdateType", policyType);//策略类型
							LogUtils.e("上行数据："+obj.toString());
							Map<String, String> params = new HashMap<String, String>();
							params.put("json", obj.toString());
					        return params;
					    }
			};
			queue.add(stringRequest);
		}else{
			LogUtils.e("查询策略信息：未连接网络");
		}
		
	}
	
	
	public synchronized static void buildStrategyStorage(List<PolicyUpdateModel> list,String policyType){
		JSONArray ja = null;
		if(list.size()>0){
			ja = new JSONArray();
			for(PolicyUpdateModel var : list){
				JSONObject obj = new JSONObject();
				boolean isNum = var.getWarning_max().matches("[0-9]+");
				if(!isNum){
					return;
				}
				obj.put("warning_type", var.getWarning_type());
				obj.put("warning_max", var.getWarning_max());
				obj.put("warning_mark", var.getWarning_mark());
				ja.add(obj);
			}
		}
		
		if(ja != null && ja.size() > 0){
			String type = ((JSONObject)ja.get(0)).getString("warning_type");
			if(ELECTRICITY.equals(type)){
				SpfsUtil.setElectricity(ja.toString());
				LogUtils.e(SpfsUtil.getElectricity());
			}else if(FLOW.equals(type)){
				SpfsUtil.setFlow(ja.toString());
				LogUtils.e(SpfsUtil.getFlow());
			}else if(MEMORY.equals(type)){
 				SpfsUtil.setMemory(ja.toString());
				LogUtils.e(SpfsUtil.getMemory());
			}
			//初始化数据
			BasicDataService.initData(type);
		}
	}
		
	
}
