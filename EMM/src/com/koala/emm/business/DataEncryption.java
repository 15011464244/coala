/**
 * 
 */
package com.koala.emm.business;

import java.io.File;

import javax.crypto.spec.SecretKeySpec;

import com.arvin.koalapush.util.ToastUtil;
import com.koala.emm.basicdata.BasicDataService;
import com.koala.emm.util.AESEncryptUtil;
import com.koala.emm.util.AESKeyModel;
import com.koala.emm.util.FileDES;
import com.koala.emm.util.FileUtil;
import com.koala.emm.util.SpfsUtil;
import com.koala.emm.util.WarningDialogUtil;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

/**
 * @author wanjun 2015年6月5日,下午2:29:58 数据加密处理类
 */
public class DataEncryption {
	private Context mContext;
	
	// 数据是否加密常量
	public static final String IS_ENCRYPTED = "1";
	public static final String IS_NOT_ENCRYPTED = "0";

	public DataEncryption(Context mContext) {
		this.mContext = mContext;
	}

	/**
	 * 文档加密
	 * 
	 * @param encKey
	 */
	public Boolean encryData(String encKey) {
		boolean flag = false;
		String encTag = SpfsUtil.getEncryption();// 加密标识
		String content = "";// 弹窗内容
		if (IS_NOT_ENCRYPTED.equals(encTag)) {
			// 数据库加密
			String filepath = Environment.getExternalStorageDirectory().toString()+"/mdm/file";
			File dir = new File(filepath);
			if (dir != null && dir.exists()
					&& dir.isDirectory()) {
				for (File item : dir.listFiles()) {
					String filename = item.getName();
					// 文件加密
					String last = filename.substring(filename.lastIndexOf("."));
					File encryptFile = AESEncryptUtil.encryptFile(item, last,
							encKey);
					FileUtil.saveFile(item.getPath()+"fuben", encryptFile);
				}
			}
			SpfsUtil.setEncryption(IS_ENCRYPTED);
		}

//		if (IS_NOT_ENCRYPTED.equals(encTag)) {
//			content = "数据加密完成";
////			handler.sendEmptyMessage(HANDLE_SEND_DEVICE_INFO);
//			flag = true;
//		} else {
//			content = "数据已加密";
//		}
//		// 弹窗
//		WarningDialogUtil.createSystemAlertDialog(mContext,
//				"数据安全", content, "确定", new OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						WarningDialogUtil.dismiss();
//					}
//				}, null, null);
		
		return flag;
	}

	/**
	 * 文档解密
	 * 
	 * @param encKey
	 */
	public  boolean decryData(String encKey) {
		boolean flag = false;
		String encTag = SpfsUtil.getEncryption();// 加密标识
		String content = "";// 弹窗内容
		if (IS_ENCRYPTED.equals(encTag)) {
			// 数据库加密
			String filepath = Environment.getExternalStorageDirectory().toString()+"/mdm/file";
			File dir = new File(filepath);
			if (dir != null && dir.exists()
					&& dir.isDirectory()) {
				for (File item : dir.listFiles()) {
					String filename = item.getName();
					// 文件加密
					String last = filename.substring(filename.lastIndexOf("."));
					File decryptFile = AESEncryptUtil.decryptFile(item, last,
							encKey);
					FileUtil.saveFile(item.getPath()+"fuben", decryptFile);
				}
			}
			SpfsUtil.setEncryption(IS_NOT_ENCRYPTED);
		}

//		if (IS_ENCRYPTED.equals(encTag)) {
//			content = "数据解密完成";
////			handler.sendEmptyMessage(HANDLE_SEND_DEVICE_INFO);
//			flag = true;
//		} else {
//			content = "数据未加密！";
//		}
//		// 弹窗
//		WarningDialogUtil.createSystemAlertDialog(mContext,
//				"数据安全", content, "确定", new OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						WarningDialogUtil.dismiss();
//					}
//				}, null, null);
		return flag;
	}
	
	public static void main(String[] args) {
		String s = "C:/Usersmingsheng/Desktop/啦啦啦啦啦.txt";
		
		File f= new File(s);
		
		File decryptFile = AESEncryptUtil.encryptFile(f, "txt",
				"11");
//		File f2 = AESEncryptUtil.decryptFile(decryptFile, "txt",
//				"11");
	}
	/**
	 * 文件加密2
	 * @param srcFile
	 */
	void  testAESJiaMi(File srcFile){
		String srcjiamiFile;
			if(!srcFile.exists()||srcFile.isDirectory())
				Toast.makeText(mContext.getApplicationContext(), "该文件不合法!", Toast.LENGTH_SHORT).show();
			else{
				srcjiamiFile=Environment.getExternalStorageDirectory().toString()+"/mdm/file2";
				
				File dir = new File(srcjiamiFile);
				if  (!dir .exists())      
				{       
				    dir.mkdir(); 
				}
				
				String destPath = dir+"/"+srcFile.getName();
				
				AESKeyModel model_aes=new AESKeyModel();
				model_aes.setSrcFile(srcFile.getPath());
				model_aes.setDestionFile(destPath);
				
				try {
					SecretKeySpec key_AES = new SecretKeySpec(model_aes.initSecretKey(),AESKeyModel.KEY_ALGORITHM);
					model_aes.encryptionFile(key_AES);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	}
	
	/**
	 * 文件解密2
	 * @param srcFile
	 */
	void testAESJieMi(File srcFile){
		String srcjiamiFile;
			if(!srcFile.exists()||srcFile.isDirectory())
				Toast.makeText(mContext.getApplicationContext(), "该文件不合法!", Toast.LENGTH_SHORT).show();
			else{
				srcjiamiFile=Environment.getExternalStorageDirectory().toString()+"/mdm/file";
				
				File dir = new File(srcjiamiFile);
				if  (!dir .exists())      
				{       
				    dir.mkdir(); 
				}
				String destPath = dir+"/"+srcFile.getName();
				AESKeyModel model_aes=new AESKeyModel();
				model_aes.setSrcFile(srcFile.getPath());
				model_aes.setDestionFile(destPath);
				
				try {
					SecretKeySpec key_AES = new SecretKeySpec(model_aes.initSecretKey(),AESKeyModel.KEY_ALGORITHM);
					model_aes.descryptionFile(key_AES);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	}
	
	public void testAESJiaMi(String dirPath){
		File dir = new File(dirPath);
		if(dir.exists()){
			File[] files = dir.listFiles();
			for (File file : files) {
				testAESJiaMi(file);
			}
		}else{
			Toast.makeText(mContext.getApplicationContext(), "指定的目录不存在!", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void testAESJieMi(String dirPath){
		File dir = new File(dirPath);
		if(dir.exists()){
			File[] files = dir.listFiles();
			for (File file : files) {
				testAESJieMi(file);
			}
		}else{
			Toast.makeText(mContext.getApplicationContext(), "指定的目录不存在!", Toast.LENGTH_SHORT).show();
		}
	}
	/**
	 * 数据加密3
	 * @param srcPath
	 * @param savePath
	 */
	public void FileEncry(String srcPath,String savePath){
		try {
			FileDES fileDes = new FileDES("00000000");
			fileDes.doEncryptFile(srcPath, savePath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 数据解密3
	 * @param srcPath
	 * @param savePath
	 */
	public void FileDecry(String srcPath,String savePath){
		try {
			FileDES fileDes = new FileDES("00000000");
			fileDes.doDecryptFile(srcPath, savePath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 加密入口
	 */
	public void startEncry(String srcDir,String destDir){
		File srcD = new File(srcDir);
		if(!srcD.exists()){
			Toast.makeText(mContext, "指定的目录不存在!", Toast.LENGTH_SHORT).show();
		}else{
			File[] files = srcD.listFiles();
			for (File file : files) {
				FileEncry(file.getAbsolutePath(), destDir+"/"+file.getName());
				file.delete();
			}
		}
	}
	/**
	 * 解密入口
	 */
	public void startDecry(String srcDir,String destDir){
		File srcD = new File(srcDir);
		if(!srcD.exists()){
//			ToastUtil.show(mContext, "指定目录不存在");
		}else{
			File[] files = srcD.listFiles();
			for (File file : files) {
				FileDecry(file.getPath(), destDir+"/"+file.getName());
				file.delete();
			}
		}
	}
	
	
	
}
