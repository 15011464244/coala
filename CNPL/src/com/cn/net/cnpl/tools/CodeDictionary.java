package com.cn.net.cnpl.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class CodeDictionary {
	
//	�ʼ���	mailNo	1
//	�Ƿ�����	isMangle	2
//	���η�	responsible	3
//	�쳣ʱ��	abnormity_time	4
//	��������	type	5
//	��������	code	6
//		header	7
//	��¼��	loginName	8
//	��������	connectType	9
//	���ӿ�ʼʱ��	connectStartTime	A
//	���ӽ���ʱ��	connectEndTime	B
//		body	C
//	�ʼ�ɨ�贴��ʱ��	create_time	D
//	�ʼ��ϴ�ʱ��	upload_time	E


	private static Map<String, String>  map= new HashMap<String, String>(){{
		put("1", "mailNo");
		put("2", "isMangle");
		put("3", "responsible");
		put("4", "abnormity_time");
		put("5", "type");
		put("6", "code");
		put("7", "header");
		put("8", "loginName");
		put("9", "connectType");
		put("A", "connectStartTime");
		put("B", "connectEndTime");
		put("C", "body");
		put("D", "create_time");
		put("E", "upload_time");
	}
	};
	
	private static List<String> keys= new ArrayList<String>(){{
		add("1");
		add("2");
		add("3");
		add("4");
		add("5");
		add("6");
		add("7");
		add("8");
		add("9");
		add("A");
		add("B");
		add("C");
		add("D");
		add("E");
	}
	};
	
	
	
	private static Map<String, String>  map2= new HashMap<String, String>(){{
		put( "mailNo","1");
		put( "isMangle","2");
		put( "responsible","3");
		put("abnormity_time","4");
		put("type","5");
		put("code","6");
		put("header","7");
		put("loginName","8");
		put("connectType","9");
		put("connectStartTime","A");
		put( "connectEndTime","B");
		put( "body","C");
		put("create_time","D");
		put( "upload_time","E");
	}
	};
	
	private static List<String> key2s= new ArrayList<String>(){{
		add( "mailNo");
		add( "isMangle");
		add( "responsible");
		add( "abnormity_time");
		add( "type");
		add( "code");
		add( "header");
		add( "loginName");
		add( "connectType");
		add( "connectStartTime");
		add( "connectEndTime");
		add( "body");
		add("create_time");
		add( "upload_time");
	}
	};
	
	/**
	 * �õ�����ȫ��
	 * @param key
	 * @return
	 */
	public static String getCode(String key){
		return map.get(key);
	}
	/**
	 * �滻json�еĴ���Ϊȫд
	 * @param jsonObject
	 * @return
	 */
	public static JSONObject replaceNumKey(JSONObject jsonObject){
		
		for(int i=0;i<keys.size();i++){
			try {
				if(jsonObject.get(keys.get(i)) != null ){  //???KEY??? ?�I
					jsonObject.put(map.get(keys.get(i)), jsonObject.get(keys.get(i)));
					jsonObject.remove(keys.get(i));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return jsonObject;
	}
	
	/**
	 * �õ������д
	 * @param key
	 * @return
	 */
	public static String getNum(String key){
		return map2.get(key);
	}
	
	/**
	 * �滻json�еĴ���Ϊ��д
	 * @param jsonObject
	 * @return
	 */
	public static JSONObject replaceCodeKey(JSONObject jsonObject){
		
		for(int i=0;i<key2s.size();i++){
			try {
				if(jsonObject.get(key2s.get(i)) != null ){  //???KEY??? ?�I
					jsonObject.put(map2.get(key2s.get(i)), jsonObject.get(key2s.get(i)));
					jsonObject.remove(key2s.get(i));
				}
			} catch (Exception e) {
			}
		}
		return jsonObject;
	}
	
	
	
	/**
	 * ���ɶ�ά����ַ����� ��header��body
	 * @param jsonObject
	 * @param flag
	 * @return
	 */
	public static List<String>  createCode2Str(JSONObject jsonObject){
		int num=1;
		 List<String>  strList=new ArrayList<String>();
			try {
				//header
				jsonObject = CodeDictionary.replaceCodeKey(jsonObject);
				JSONObject	headJson=	jsonObject.getJSONObject(CodeDictionary.getNum("header"));
				headJson=CodeDictionary.replaceCodeKey(headJson);
				jsonObject.put(CodeDictionary.getNum("header"), headJson);
				//body
				JSONArray temparray = new JSONArray();
				JSONArray bodyarray= jsonObject.getJSONArray(CodeDictionary.getNum("body"));
					for(int i=0;i<bodyarray.length();i++){
						temparray.put( CodeDictionary.replaceCodeKey(bodyarray.getJSONObject(i)) );
					}
					jsonObject.put(CodeDictionary.getNum("body"),temparray);
					
					//�ʼ����� ÿ��2��
					while(temparray.length()>num){
						List<JSONArray>  arraysss= takeApart(temparray,num);
						jsonObject.put(CodeDictionary.getNum("body"),arraysss.get(0));
						strList.add(BaseCommand.encodeStr(jsonObject.toString()));
						temparray=arraysss.get(1);
					}
					List<JSONArray>  arraysss= takeApart(temparray,num);
					jsonObject.put(CodeDictionary.getNum("body"),arraysss.get(0));
					String endcStr=BaseCommand.encodeStr(jsonObject.toString());
					strList.add(endcStr);
					
			} catch (Exception e) {
				return null;
			}
		return strList;
	}
	
	
	/**
	 * ���ɶ�ά����ַ���������ת����
	 * @param jsonObject
	 * @param flag
	 * @return
	 */
	public static List<String>  createCode2Str(JSONArray jsonArray){
		int num=1;
		 List<String>  strList=new ArrayList<String>();
			try {
				JSONArray temparray = new JSONArray();
					for(int i=0;i<jsonArray.length();i++){
						temparray.put(CodeDictionary.replaceCodeKey(jsonArray.getJSONObject(i)) );
					}
					//�ʼ����� ÿ��5��
					while(temparray.length()>num){
						List<JSONArray>  arraysss= takeApart(temparray,num);
						strList.add(BaseCommand.encodeStr(arraysss.get(0).toString()));
						temparray=arraysss.get(1);
					}
					
					String endcStr=BaseCommand.encodeStr(temparray.toString());
					strList.add(endcStr);
					
			} catch (Exception e) {
				return null;
			}
		return strList;
	}
	
	
	/**
	 * �������
	 * @param jsonArray
	 * @return
	 */
	public static List<JSONArray>  takeApart(JSONArray jsonArray,int num){
		
		 List<JSONArray>  arrayList=new ArrayList<JSONArray>();
			try {
				JSONArray temparray1 = new JSONArray();
				JSONArray temparray2 = new JSONArray();
					for(int i=0;i<jsonArray.length();i++){
							if(i<num){
								temparray1.put(jsonArray.getJSONObject(i));
							}else{
								temparray2.put(jsonArray.getJSONObject(i));
							}
						
					}
					arrayList.add(temparray1);
					arrayList.add(temparray2);
					
			} catch (Exception e) {
				return null;
			}
		return arrayList;
	}
	
	/**
	 * ������ά��ɨ������� 
	 * @param endcStr
	 * @return
	 */
	public static JSONArray decodeCode2JsonArray(String endcStr){
		JSONArray jsonArray = new JSONArray();
		try {
			JSONTokener jsonParser = new JSONTokener(BaseCommand.decodeStr(endcStr));
			JSONArray temparray = (JSONArray) jsonParser.nextValue();
			for(int i=0;i<temparray.length();i++){
					jsonArray.put(CodeDictionary.replaceNumKey( temparray.getJSONObject(i)) );
				}
		} catch (Exception e) {
			return null;
		}
		return jsonArray;
		
	}
	
	/**
	 * ������ά��ɨ������� �����
	 * @param endcStr
	 * @return
	 */
	public static JSONObject decodeCode2jsonObject(String endcStr){
		JSONObject jsonObject = new JSONObject();
		try {
			JSONTokener jsonParser = new JSONTokener(BaseCommand.decodeStr(endcStr));
			JSONObject tempjsonObject = (JSONObject) jsonParser.nextValue();
			JSONObject headJson=	tempjsonObject.getJSONObject(CodeDictionary.getNum("header"));
			jsonObject.put("header", CodeDictionary.replaceNumKey(headJson));
			JSONArray temparray = new JSONArray();
			JSONArray bodyarray= tempjsonObject.getJSONArray(CodeDictionary.getNum("body"));
				for(int i=0;i<bodyarray.length();i++){
					temparray.put( CodeDictionary.replaceNumKey(bodyarray.getJSONObject(i)) );
				}
				jsonObject.put("body",temparray);
				
			} catch (Exception e) {
		}
		return jsonObject;
		
	}
	
	/**
	 * ɨ�����
	 * @param sanStr
	 * @return
	 */
	public static Map<String, String>  decodeOrgCode(String sanStr){
		 Map<String, String> mapresult= new HashMap<String, String>();
		try {
//			{"type":"1","code":"35000100"} 
			JSONTokener jsonParser = new JSONTokener(BaseCommand.decodeStr(sanStr));
			JSONObject tempjsonObject = (JSONObject) jsonParser.nextValue();
			mapresult.put("type", tempjsonObject.getString("type"));
			mapresult.put("code", tempjsonObject.getString("code"));
			} catch (Exception e) {
				mapresult.put("code", sanStr);
		}
		return mapresult;
	}
	
}
