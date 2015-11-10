package com.koala.emm.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.media.MediaCodec.BufferInfo;
import android.text.format.DateFormat;

public class LogSave {
	public static StringBuffer sb = new StringBuffer();
	public final static SimpleDateFormat dateTimeFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public final static SimpleDateFormat dateTimeFormat2 = new SimpleDateFormat(
			"yyyy-MM-dd HH_mm_ss");
	
	public static void save(String device){
		FileOutputStream fos = null;
		InputStream is = null;
		try {
			
			File file = new File(android.os.Environment.getExternalStorageDirectory()+"/EMMlogs/"+device+"_"+dateTimeFormat2.format(new Date())+".txt");
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			if(!file.exists()){
				 file.createNewFile();
			 }
			 fos= new FileOutputStream(file);
			 is = new StringBufferInputStream(sb.toString());
			
			 sb.delete(0, sb.length());
			 
			byte[] buf = new byte[1024];
			int len = 0;  
            while ((len = is.read(buf)) > 0) {  
                fos.write(buf, 0, len);  
            }
			if(!file.exists()){
				file.createNewFile();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(fos != null){
					fos.close();
				}
				if(is != null){
					is.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
			
		}
	}
	public static void append(String log){
		sb.append(dateTimeFormat.format(new Date())+"::");
		try {
			sb.append(new String(log.getBytes("UTF-8"),"ISO8859-1"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sb.append("\r\n");
	}

}
