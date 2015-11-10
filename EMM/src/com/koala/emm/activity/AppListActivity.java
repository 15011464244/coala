
package com.koala.emm.activity;

import java.util.ArrayList;
import java.util.List;

import com.koala.emm.R;
import com.koala.emm.basic.BaseActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AppListActivity extends BaseActivity {
	private List<ApplicationInfo> mAppList;
	private AppAdapter mAdapter;
	private ListView mListView;
	public PackageManager pManager;
	
	private AppBroadcastReceiver mAppBroadcastReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_list);
		
		pManager = getPackageManager();
		
		setHeadTitle("应用管理");
		
		List<ApplicationInfo> mAllAppList = pManager.getInstalledApplications(0);
		mAppList = new ArrayList<ApplicationInfo>();
		//过滤掉系统应用
		for (ApplicationInfo applicationInfo : mAllAppList) {
			if((applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0){
				mAppList.add(applicationInfo);
			}
		}
		
		
		mListView = (ListView) findViewById(R.id.lv_app_list);
		mAdapter = new AppAdapter();
		mListView.setAdapter(mAdapter);
		
		 mAppBroadcastReceiver=new AppBroadcastReceiver(); 
	     IntentFilter intentFilter=new IntentFilter(); 
	     intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
	     intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
	     this.registerReceiver(mAppBroadcastReceiver,intentFilter);
		
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.unregisterReceiver(mAppBroadcastReceiver);
	}
	
	class AppAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if(null != mAppList){
				return mAppList.size();
			}
			return 0;
			
		}

		@Override
		public ApplicationInfo getItem(int position) {
			return mAppList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(),
						R.layout.app_list_item, null);
				new ViewHolder(convertView);
			}
			ViewHolder holder = (ViewHolder) convertView.getTag();
			ApplicationInfo item = getItem(position);
			holder.appIcon.setImageDrawable(item.loadIcon(getPackageManager()));
			holder.appName.setText(item.loadLabel(getPackageManager()));
			holder.setOnclickEvent(item.packageName);
			return convertView;
		}

		class ViewHolder {
			ImageView appIcon;
			TextView appName,appOpen,appDelete;

			public ViewHolder(View view) {
				appIcon = (ImageView) view.findViewById(R.id.iv_app_icon);
				appName = (TextView) view.findViewById(R.id.tv_app_name);
				appOpen = (TextView) view.findViewById(R.id.tv_app_open);
				appDelete = (TextView) view.findViewById(R.id.tv_app_delete);
				view.setTag(this);
			}
			
			public void setOnclickEvent(final String packageName){
				appOpen.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(); 
					  	intent = pManager.getLaunchIntentForPackage(packageName); 
					  	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP) ; 
					  	currentContext.startActivity(intent);
					}
				});
				
				appDelete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
					    intent.setAction(Intent.ACTION_DELETE);
					    intent.setData(Uri.parse("package:"+packageName));
						startActivity(intent);
					}
				});
			}
		}
	}

	
	class AppBroadcastReceiver extends BroadcastReceiver { 
	    private final String ADD_APP ="android.intent.action.PACKAGE_ADDED"; 
	    private final String REMOVE_APP ="android.intent.action.PACKAGE_REMOVED"; 
	    @Override 
	    public void onReceive(Context context, Intent intent) { 
	    	List<ApplicationInfo> mAllAppList = pManager.getInstalledApplications(0);
			mAppList = new ArrayList<ApplicationInfo>();
			//过滤掉系统应用
			for (ApplicationInfo applicationInfo : mAllAppList) {
				if((applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0){
					mAppList.add(applicationInfo);
				}
			}
	    	
	    	mAdapter.notifyDataSetChanged();
	    	
	    	
	    } 
	   
	}
	
	
}
