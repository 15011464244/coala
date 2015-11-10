package com.cn.net.cnpl.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cn.net.cnpl.Global;
import com.cn.net.cnpl.R;
import com.cn.net.cnpl.WelComeActivity;
import com.cn.net.cnpl.model.Head;

public class UpdateManager {

	private Context mContext;
	private Class<WelComeActivity> targetClass;
	//�ӳ�ʱ��
	private final int SPLASH_DISPLAY_LENGHT = 3000;	
	//��װ���İ汾��Ϣ
	private String apkUrl = "";
	private Integer versionCode=0;
	//�Ի���
	private Dialog noticeDialog;	
	private Dialog downloadDialog;
	//���ذ���װ·�� 
    private static final String savePath = "/mnt/sdcard/updatedemo/";
    private static final String saveFileName = savePath + "EmsClient.apk";
    //��������֪ͨuiˢ�µ�handler��msg����
    private ProgressBar mProgress;
    private static final int OVER = 0;//����
    private static final int DOWN_UPDATE = 1;//��ʼ����    
    private static final int DOWN_OVER = 2;//���ؽ���
    private static final int UPDATE_OVER = 3;//��װ����
    private static final int DOWN_CHECK = 10;//��ʼ�汾���
    
    private int progress;//���ؽ��� 
    private Thread downLoadThread;//�����߳� 
    private boolean interceptFlag = false;//ȡ�����صı��    

	private List<JSONObject> rList = null;
    
    private Handler mHandler = new Handler(){
    	public void handleMessage(Message msg) {
    		switch (msg.what) {
    		case OVER:
				new Handler().postDelayed(new Runnable() {
					public void run() {
						Intent intent = new Intent(mContext,targetClass);
						mContext.startActivity(intent);
						((Activity) mContext).finish();
					}
				}, SPLASH_DISPLAY_LENGHT);
    			break;
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				break;
			case DOWN_OVER:				
				installApk();
				break;
			case UPDATE_OVER:
				if(null==mContext || null==targetClass){
					return;
				}
				System.exit(0);  
				break;
			case DOWN_CHECK:
				try {
					if (rList == null) {
						mHandler.sendEmptyMessage(OVER);
						return;
					}
					apkUrl=rList.get(0).getString("versionUrl");
					versionCode=rList.get(0).getInt("versionCode");
					//��ȡ��ǰ�汾��Ϣ
					PackageManager packageManager = mContext.getPackageManager();
					Integer versionCodeMy=packageManager.getPackageInfo(mContext.getPackageName(), 0).versionCode;
					//�жϵ�ǰ�汾�Ƿ�����
					if(versionCode.equals(versionCodeMy)){
						mHandler.sendEmptyMessage(OVER);
						return;
					}
					
					AlertDialog.Builder builder = new Builder(mContext);
					builder.setTitle("����汾����");
					builder.setMessage("���µİ汾���Ը��£��Ƿ��������ظ��³���");
					builder.setPositiveButton("����", new OnClickListener() {			
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							showDownloadDialog();			
						}
					});
					builder.setNegativeButton("����", new OnClickListener() {			
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();	
							mHandler.sendEmptyMessage(OVER);
						}
					});
					noticeDialog = builder.create();
					noticeDialog.show();
					noticeDialog.setCanceledOnTouchOutside(false);
				} catch (NameNotFoundException e) {
					Log.e( Global.DIALOG_NAME, e.getMessage());
//					Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
				} catch (JSONException e) {
					Log.e( Global.DIALOG_NAME, e.getMessage());
//					Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
				}
				break;
			default:
				break;
			}
    	};
    };
    
	public UpdateManager(Context context,Class<WelComeActivity> targetClass) {
		this.mContext = context;
		this.targetClass=targetClass;
	}
	
	//�ⲿ�ӿ���Activity����
	public void checkUpdateInfo() throws Exception{
		showNoticeDialog();
	}	
	
	private void showNoticeDialog() throws Exception{
		new Thread(){
			@Override
			public void run(){
				try {
					//TODO  
					//��ȡ���°汾����Ϣ
//					NetHelper client = new NetHelper();
//					client.Create(Global.JSON_SERVER_URL_RELEASE + Global.URL_ANDROIDVERSION);
//					Head head = new Head();
//					List<NameValuePair> params = new ArrayList<NameValuePair>();
//					params.add(new BasicNameValuePair("mobile", Global.M_MOBILE));
//					params.add(new BasicNameValuePair("accessId", Global.M_ACCESSID));
//					rList = client.execute(params, head);
				} catch (Exception e) {
					Log.e( Global.DIALOG_NAME, e.getMessage());
//					Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
				}finally{
					mHandler.sendEmptyMessage(DOWN_CHECK);					
				}
			}
		}.start();
	}
	
	private void showDownloadDialog(){
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("����汾����");
		
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.progress, null);
		mProgress = (ProgressBar)v.findViewById(R.id.progress);
		
		builder.setView(v);
		builder.setNegativeButton("ȡ��", new OnClickListener() {	
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});
		downloadDialog = builder.create();
		downloadDialog.show();
		
		downloadApk();
	}
	
	private Runnable mdownApkRunnable = new Runnable() {	
		@Override
		public void run() {
			try {
				URL url = new URL(apkUrl);
			
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();
				
				File file = new File(savePath);
				if(!file.exists()){
					file.mkdir();
				}
				String apkFile = saveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);
				
				int count = 0;
				byte buf[] = new byte[1024];
				
				do{   		   		
		    		int numread = is.read(buf);
		    		count += numread;
		    	    progress =(int)(((float)count / length) * 100);
		    	    //���½���
		    	    mHandler.sendEmptyMessage(DOWN_UPDATE);
		    		if(numread <= 0){	
		    			//�������֪ͨ��װ
		    			mHandler.sendEmptyMessage(DOWN_OVER);
		    			break;
		    		}
		    		fos.write(buf,0,numread);
		    	}while(!interceptFlag);//���ȡ����ֹͣ����.
				
				fos.close();
				is.close();
			}catch(MalformedURLException e) {
				Log.e( Global.DIALOG_NAME, e.getMessage());
				Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
			}catch(IOException e){
				Log.e( Global.DIALOG_NAME, e.getMessage());
				Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
			}catch(Exception e){
				Log.e( Global.DIALOG_NAME, e.getMessage());
				Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
			}finally{
				mHandler.sendEmptyMessage(OVER);
			}
		}
	};
	
	 /**
     * ����apk
     * @param url
     */	
	private void downloadApk(){
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}
	 /**
     * ��װapk
     * @param url
     */
	private void installApk(){
		try {
			File apkfile = new File(saveFileName);
			if (!apkfile.exists()) {
			    return;
			}    
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive"); 
			mContext.startActivity(i);
		} catch (Exception e) {
			Log.e( Global.DIALOG_NAME, e.getMessage());
			Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
		}finally{
			mHandler.sendEmptyMessage(UPDATE_OVER);
		}
	}
}
