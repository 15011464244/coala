package com.cn.net.cnpl.exception;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import com.cn.net.cnpl.Global;
import com.cn.net.cnpl.tools.NetHelper;

/**
 * 
 * 
 * UncaughtExceptionHandler���߳�δ�����쳣����������������δ�����쳣�ġ� 
 *                           ������������δ�����쳣Ĭ�������������ǿ�йرնԻ���
 *                           ʵ�ָýӿڲ�ע��Ϊ�����е�Ĭ��δ�����쳣���� 
 *                           ������δ�����쳣����ʱ���Ϳ�����Щ�쳣�������
 *                           ���磺�ռ��쳣��Ϣ�����ʹ��󱨸� �ȡ�
 * 
 * UncaughtException������,��������Uncaught�쳣��ʱ��,�ɸ������ӹܳ���,����¼���ʹ��󱨸�.
 */
public class CrashHandler implements UncaughtExceptionHandler {
	/** Debug Log Tag */
	public static final String TAG = "CrashHandler";
	/** �Ƿ�����־���, ��Debug״̬�¿���, ��Release״̬�¹ر��������������� */
	public static final boolean DEBUG = true;
	/** CrashHandlerʵ�� */
	private static CrashHandler INSTANCE;
	/** �����Context���� */
	private Context mContext;
	/** ϵͳĬ�ϵ�UncaughtException������ */
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	
	/** ʹ��Properties�������豸����Ϣ�ʹ����ջ��Ϣ */
	private Properties mDeviceCrashInfo = new Properties();
	private static final String VERSION_NAME = "versionName";
	private static final String VERSION_CODE = "versionCode";
	private static final String STACK_TRACE = "STACK_TRACE";
	/** ���󱨸��ļ�����չ�� */
	private static final String CRASH_REPORTER_EXTENSION = ".cr";
	
	/** ��ֻ֤��һ��CrashHandlerʵ�� */
	private CrashHandler() {
	}

	/** ��ȡCrashHandlerʵ�� ,����ģʽ */
	public static CrashHandler getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CrashHandler();
		return INSTANCE;
	}
	
	/**
	 * ��ʼ��,ע��Context����, ��ȡϵͳĬ�ϵ�UncaughtException������, ���ø�CrashHandlerΪ�����Ĭ�ϴ�����
	 * 
	 * @param ctx
	 */
	public void init(Context ctx) {
		mContext = ctx;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	
	/**
	 * ��UncaughtException����ʱ��ת��ú���������
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// ����û�û�д�������ϵͳĬ�ϵ��쳣������������
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			// Sleepһ����������
			// �����߳�ֹͣһ����Ϊ����ʾToast��Ϣ���û���Ȼ��Kill����
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Log.e(TAG, "Error : ", e);
			}
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(10);
		}
	}

	/**
	 * �Զ��������,�ռ�������Ϣ ���ʹ��󱨸�Ȳ������ڴ����. �����߿��Ը����Լ���������Զ����쳣�����߼�
	 * 
	 * @param ex
	 * @return true:��������˸��쳣��Ϣ;���򷵻�false
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return true;
		}
		final String msg = ex.getLocalizedMessage();
		// ʹ��Toast����ʾ�쳣��Ϣ
		new Thread() {
			@Override
			public void run() {
				// Toast ��ʾ��Ҫ������һ���̵߳���Ϣ������
				Looper.prepare();
				Toast.makeText(mContext, "����������������½���" , Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();
		// �ռ��豸��Ϣ
		// ������󱨸��ļ�
		 saveCrashInfoToFile(mContext,ex);
		// ���ʹ��󱨸浽������
		sendCrashReportsToServer(mContext);
		return true;
	}

	/**
	 * ���������Ϣ���ļ���
	 * 
	 * @param ex
	 * @return
	 */
	private String saveCrashInfoToFile(Context context, Throwable ex) {
		String crashReport=getCrashReport(context, ex);
		File crashFile =	save2File( context,crashReport);
		if(crashFile != null)
		return crashFile.getName();
		
		return null;
	}
	
	/**
	 * ��ȡAPP�����쳣����
	 * 
	 * @param ex
	 * @return
	 */
	private String getCrashReport(Context context, Throwable ex) {
		PackageInfo pinfo = getPackageInfo(context);
		StringBuffer exceptionStr = new StringBuffer();
		exceptionStr.append("Version: " + pinfo.versionName + "("
				+ pinfo.versionCode + ")\n");
		exceptionStr.append("Android: " + android.os.Build.VERSION.RELEASE
				+ "(" + android.os.Build.MODEL + ")\n");
		exceptionStr.append("Exception: " + ex.getMessage() + "\n");
		StackTraceElement[] elements = ex.getStackTrace();
		for (int i = 0; i < elements.length; i++) {
			exceptionStr.append(elements[i].toString() + "\n");
		}
		return exceptionStr.toString();
	}
	
	/**
	 * ��ȡApp��װ����Ϣ
	 * 
	 * @return
	 */

	private PackageInfo getPackageInfo(Context context) {
		PackageInfo info = null;
		try {
			info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// e.printStackTrace(System.err);
			// L.i("getPackageInfo err = " + e.getMessage());
		}
		if (info == null)
			info = new PackageInfo();
		return info;
	}

	
	
	private File save2File(Context ctx,String crashReport) {

	String fileName = "crash-" + System.currentTimeMillis() + CRASH_REPORTER_EXTENSION;
	if (Environment.getExternalStorageState().equals(
			Environment.MEDIA_MOUNTED)) {
		try {
			File dir = ctx.getFilesDir();
//			File dir = new File(Environment.getExternalStorageDirectory()
//					.getAbsolutePath() + File.separator + "crash");
			if (!dir.exists())
				dir.mkdir();
			File file = new File(dir, fileName);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(crashReport.toString().getBytes());
			fos.close();
			return file;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	return null;
}
	
	/**
	 * �Ѵ��󱨸淢�͸�������,�����²����ĺ���ǰû���͵�.
	 * 
	 * @param ctx
	 */
	private void sendCrashReportsToServer(Context ctx) {
		String[] crFiles = getCrashReportFiles(ctx);
		if (crFiles != null && crFiles.length > 0) {
			TreeSet<String> sortedFiles = new TreeSet<String>();
			sortedFiles.addAll(Arrays.asList(crFiles));

			for (String fileName : sortedFiles) {
				File cr = new File(ctx.getFilesDir(), fileName);
				postReport(cr);
				cr.delete();// ɾ���ѷ��͵ı���
			}
		}
	}

	/**
	 * ��ȡ���󱨸��ļ���
	 * 
	 * @param ctx
	 * @return
	 */
	private String[] getCrashReportFiles(Context ctx) {
		File filesDir = ctx.getFilesDir();
		// ʵ��FilenameFilter�ӿڵ���ʵ�������ڹ������ļ���
		FilenameFilter filter = new FilenameFilter() {
			// accept(File dir, String name)
			// ����ָ���ļ��Ƿ�Ӧ�ð�����ĳһ�ļ��б��С�
			public boolean accept(File dir, String name) {
				return name.endsWith(CRASH_REPORTER_EXTENSION);
			}
		};
		// list(FilenameFilter filter)
		// ����һ���ַ������飬��Щ�ַ���ָ���˳���·������ʾ��Ŀ¼������ָ�����������ļ���Ŀ¼
		return filesDir.list(filter);
	}

	private void postReport(File file) {
		TelephonyManager telephonemanage = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
		NetHelper helper=new NetHelper();
		NetHelper client = new NetHelper();
//		String url = Global.BASE_URL
//				+ Global.EXCEPTIONACTION;
		String url = "";
		BufferedReader reader=null;
		StringBuffer fileContent=new StringBuffer();
		try {
			JSONObject jsonObject=new JSONObject();
			 reader = new BufferedReader(new FileReader(file)); 
			String tempString = null; 
			//һ�ζ���һ�У�ֱ������nullΪ�ļ����� 
			while ((tempString = reader.readLine()) != null){ 
				fileContent.append("\n").append(tempString);
			} 
			reader.close(); 
			jsonObject.put("id", "13");
			jsonObject.put("deviceNo", telephonemanage.getDeviceId());
			jsonObject.put("role", "8");
			jsonObject.put("client_name", Global.DIALOG_NAME);
			jsonObject.put("exception_content",fileContent.toString());
			jsonObject.put("file_name", file.getName());
			jsonObject.put("create_time",  DateFormat.format("yyyyMMddkkmmss", new Date()));
			JSONObject resultJsonObject = client.exeRequestGzip(jsonObject.toString(),url);
		} catch (Exception e) {
//			e.printStackTrace();
		} finally { 
			if (reader != null){ 
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
				} 
				}
			} 
		
//		ʹ��HTTP Post ���ʹ��󱨸浽������
	}

	/**
	 * �ڳ�������ʱ��, ���Ե��øú�����������ǰû�з��͵ı���
	 */
	public void sendPreviousReportsToServer() {
		sendCrashReportsToServer(mContext);
	}
}