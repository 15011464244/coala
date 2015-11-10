package com.koala.emm.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.commons.io.FileUtils;

import com.arvin.koalapush.util.ToastUtil;
import com.koala.emm.R;
import com.koala.emm.app.EmmApplication;
import com.koala.emm.basic.BaseActivity;
import com.koala.emm.basicdata.BasicDataService;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateUtil {

	public final static int UPDATA_CLIENT = 1;
	public final static int GET_UNDATAINFO_ERROR = 2;
	public final static int DOWN_ERROR = 3;

	public final static String APP_UPDATE_FORCED = "1";
	public final static String APP_UPDATE_NOT_FORCED = "2";
	public final static String DATA_UPDATE_FORCED = "3";
	public final static String DATA_UPDATE_NOT_FORCED = "4";

	public Context mContext;

	public UpdateUtil(Context mContext) {
		this.mContext = mContext;
	}

	// 获取本地的版本信息
	// public static PackageInfo getLocalInfo() {
	// PackageManager manager;
	// PackageInfo info = null;
	// manager = BaseActivity.currentContext.getPackageManager();
	// try {
	// info =
	// manager.getPackageInfo(BaseActivity.currentContext.getPackageName(), 0);
	// } catch (NameNotFoundException e) {
	// e.printStackTrace();
	// }
	// return info;
	// }

	// 从服务器下载apk:
	public File getFileFromServer(String path, ProgressDialog pd)
			throws Exception {
		/*
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		pd.setMax(conn.getContentLength());
		String fileName = path.substring(path.lastIndexOf("/")+1);
		InputStream is = conn.getInputStream();
		FileOutputStream outStream = mContext.openFileOutput(fileName,Context.MODE_WORLD_READABLE);
		byte buffer[] = new byte[1024];
		int len = -1;
		int total = 0;
		while ((len=is.read(buffer))!=-1) {
			outStream.write(buffer);
			total += len;
			// 获取当前下载量
			pd.setProgress(total);
		}
		File file = new File(mContext.getFilesDir(), fileName);
		Log.i("tgxx", mContext.getFilesDir().toString());
		is.close();
		outStream.close();
		return file;
		*/
		// 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
		 
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			// 获取到文件的大小
			pd.setMax(conn.getContentLength());
			// 获取文件名称
			String fileName = path.substring(path.lastIndexOf("/")+1);
			InputStream is = conn.getInputStream();
			File dir = new File(Environment.getExternalStorageDirectory(),"/mdm/file");
//			File dir = new File(Environment.getExternalStorageDirectory(),fileName);
//			deleteFile(dir);
			
			if (!dir .exists())   
			{    
			    dir.mkdirs(); 
			}
			Log.i("tgxx", dir.exists()+"");
			Log.i("tgxx",dir.toString() );
			System.out.println(dir);
			System.out.println(fileName);
			
			File file = new File(dir, fileName);
			if (file.exists()){
//				ToastUtil.show(mContext, "您已经下载了该文件！");
				if(!( file.length() < conn.getContentLength())){
					return file;
				}
				
			}
			
			//File file = new File(dir,fileName);
//			FileUtils.copyURLToFile(url, file);
			FileOutputStream fos = new FileOutputStream(new File(dir.toString()+"/"+fileName),false);
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] buffer = new byte[1024];
			int len;
			int total = 0;
			while ((len = bis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
				total += len;
				// 获取当前下载量
				pd.setProgress(total);
			}
			fos.close();
			bis.close();
			is.close();
			Log.i("tgxx", "文件路径"+file.getAbsolutePath());
			Log.i("tgxx", "文件名："+file.getName());
			return file;
		} else {
			return null;
		}
	} 
	public void deleteFile(File file) {
			    if (file.exists()) { // 判断文件是否存在
			        if (file.isFile()) { // 判断是否是文件
			            file.delete(); // delete()方法 你应该知道 是删除的意思;
			        } else if (file.isDirectory()) { // 否则如果它是一个目录
			            File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
			            for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
			                this.deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
			            }
			        }
			        file.delete();
			    } else {
			    }
			}
	/*
	 * 从服务器中下载APK
	 */
	public void downLoadApk(final String path, final String updateType) {
		final ProgressDialog pd; // 进度条对话框
		pd = new ProgressDialog(mContext);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在下载更新");
		if (APP_UPDATE_FORCED.equals(updateType)) {
			// 点击返回键不关闭
			pd.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode,
						KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_BACK
							&& event.getRepeatCount() == 0) {
						return true;
					} else {
						return false;
					}
				}

			});
			// 点击dialog外不关闭
			pd.setCanceledOnTouchOutside(false);
		}

		// 全局对话框
		pd.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

		pd.show();
		new Thread() {
			@Override
			public void run() {
				try {
					File file = getFileFromServer(path, pd);
//					sleep(3000);
					pd.dismiss(); // 结束掉进度条对话框
					installApk(file);
//					while (file.exists()) {
//						file.delete();
//						Log.i("tgxx", file.delete()+"");
//					}
					if (APP_UPDATE_FORCED.equals(updateType)) {
						System.exit(0);
						 mContext.stopService(new Intent(mContext,BasicDataService.class));
					}
				} catch (Exception e) {
					Log.i("tgxx", "error");
					e.printStackTrace();
					pd.dismiss();
					Message msg = new Message();
					msg.what = DOWN_ERROR;
					handler.sendMessage(msg);
					if (APP_UPDATE_FORCED.equals(updateType)) {
						 System.exit(0);
						 mContext.stopService(new Intent(mContext,BasicDataService.class));
					}
				}
			}
		}.start();
	}

	/*
	 * 从服务器中下载文件
	 */
	public void downLoadFile(final String path, final String updateType) {
		final ProgressDialog pd; // 进度条对话框
		pd = new ProgressDialog(mContext);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在下载更新");

		// 全局对话框
		pd.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

		pd.show();
		new Thread() {
			@Override
			public void run() {
				try {
					File file = getFileFromServer(path, pd);
//					sleep(3000);
					ToastUtil.show(mContext, "下载完成");
//					openFile(mCo\text, file.getPath());
					// installApk(file);
					pd.dismiss(); // 结束掉进度条对话框
					// if(APP_UPDATE_FORCED.equals(updateType)){
					// System.exit(0);
					// }
				} catch (Exception e) {
					pd.dismiss();
					Message msg = new Message();
//					msg.what = DOWN_ERROR;
//					handler.sendMessage(msg);
					e.printStackTrace();
					// if(APP_UPDATE_FORCED.equals(updateType)){
					// System.exit(0);
					// }
				}
			}
		}.start();
	}

	/**
	 * 安装apk
	 * @param file
	 */
	protected void installApk(File file) {
		Intent intent = new Intent();
		// 执行动作
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		// 执行的数据类型
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");// 编者按：此处Android应为android，否则造成安装不了
		
//		intent.setDataAndType(Uri.fromFile(file),
//				"application/vnd.android.package-archive");// 编者按：此处Android应为android，否则造成安装不了
		Log.i("tgxx",file.toString()+"---------" );
		mContext.startActivity(intent);
	}

	/**
	 * 打开文件
	 * 
	 * @param fileName
	 * @return
	 */
	public void openFile(Context mContext,String fileName) {
		Intent intent = IntentUtil.getFileIntent(fileName);
		mContext.startActivity(intent);
	}


	/*
	 * 进入程序的主界面
	 */
	private void LoginMain() {
		// Intent intent = new
		// Intent(BaseActivity.currentContext,MainActivity.class);
		// startActivity(intent);
		// 结束掉当前的activity
		// this.finish();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case UPDATA_CLIENT:
				// 对话框通知用户升级程序
				// showUpdataDialog();
				break;
			case GET_UNDATAINFO_ERROR:
				// 服务器超时
				Toast.makeText(mContext, "获取服务器更新信息失败", 1).show();
				return;
			case DOWN_ERROR:
				// 下载apk失败
				Toast.makeText(mContext, "下载新版本失败", 1).show();
				return;
			}
		}
	};

	// 更新确认
	public Dialog updateConfirm(final String path, final String updateType) {
		final Dialog dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.dialog_update_confirm, null);

		if (APP_UPDATE_FORCED.equals(updateType)) {
			((TextView) view.findViewById(R.id.custom_dia_centercall))
					.setText("有新版本应用，若不更新，应用不可用\n您确定要下载更新？");
		} else if (APP_UPDATE_NOT_FORCED.equals(updateType)) {
			((TextView) view.findViewById(R.id.custom_dia_centercall))
					.setText("发现新版本\n您是否要下载更新？");
			// }else if(DATA_UPDATE_FORCED.equals(updateType)){
			// ((TextView)view.findViewById(R.id.custom_dia_centercall)).setText("您有新的文件需要下载？");
		} else if (DATA_UPDATE_NOT_FORCED.equals(updateType)) {
			((TextView) view.findViewById(R.id.custom_dia_centercall))
					.setText("有文件需要更新\n您是否下载更新？");
		}

		Button buttonCancel = (Button) view.findViewById(R.id.info_cancel);
		buttonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (APP_UPDATE_FORCED.equals(updateType)) {
					System.exit(0);
				}
			}
		});
		Button buttonDetermine = (Button) view.findViewById(R.id.info_sure);
		buttonDetermine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (DATA_UPDATE_FORCED.equals(updateType)
						|| DATA_UPDATE_NOT_FORCED.equals(updateType)) {
					downLoadFile(path, updateType);
				} else {
					downLoadApk(path, updateType);
				}
			}
		});
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);
		//全局对话框
		dialog.getWindow()
				.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

		dialog.show();
		return null;
	}
}
