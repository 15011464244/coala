package com.koala.emm.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.arvin.koalapush.util.ToastUtil;
import com.koala.emm.R;
import com.koala.emm.R.drawable;
import com.koala.emm.R.id;
import com.koala.emm.R.layout;
import com.koala.emm.basic.BaseActivity;
import com.koala.emm.util.IntentUtil;
import com.lidroid.xutils.util.LogUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class FileManagerActivity extends BaseActivity {

	private ListView mListView;
	
	String path = null;
	File[] files = null;
	FileAdapter adapter ;
	
	@Override 
    protected void onCreate(Bundle savedInstanceState) {   
        super.onCreate(savedInstanceState);   
        setContentView(R.layout.activity_file_manager);   
        setHeadTitle("文件管理");
        mListView = (ListView) findViewById(R.id.lv_file_list);
        path = Environment.getExternalStorageDirectory().toString()+"/mdm/file";
        File root = new File(path);
        files = root.listFiles();
        adapter = new FileAdapter(files);
        mListView.setAdapter(adapter);
        
    }  
	
	class FileAdapter extends BaseAdapter {
		private File[] fileArray;
		
		public FileAdapter(File[] fileArray) {
			this.fileArray = fileArray;
		}

		@Override
		public int getCount() {
			if(null != fileArray){
				return fileArray.length;
			}
			return 0;
			
		}

		@Override
		public File getItem(int position) {
			return fileArray[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(),
						R.layout.file_list_item, null);
				new ViewHolder(convertView);
			}
			ViewHolder holder = (ViewHolder) convertView.getTag();
			File item = getItem(position);
//			holder.fileIcon.setImageDrawable();
			holder.fileName.setText(item.getName());
			holder.fileIcon.setImageDrawable(getFileIcon(item));
			holder.setOnclickEvent(item);
			return convertView;
		}

		class ViewHolder {
			ImageView fileIcon;
			TextView fileName,fileOpen,fileDelete;

			public ViewHolder(View view) {
				fileIcon = (ImageView) view.findViewById(R.id.iv_file_icon);
				fileName = (TextView) view.findViewById(R.id.tv_file_name);
				fileOpen = (TextView) view.findViewById(R.id.tv_file_open);
				fileDelete = (TextView) view.findViewById(R.id.tv_file_delete);
				view.setTag(this);
			}
			
			public void setOnclickEvent(final File file){
				fileOpen.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = IntentUtil.getFileIntent(file);
					  	currentContext.startActivity(intent);
					}
				});
				
				fileDelete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(file.delete()){
							ToastUtil.show(currentContext, "删除成功");
						}else{
							ToastUtil.show(currentContext, "删除失败");
						}
						files = new File(path).listFiles();
						adapter.notifyList(files);
					}
				});
			}
		}
		
		public void notifyList(File[] files){
			this.fileArray = files;
			adapter.notifyDataSetChanged();
		}
	}
	/**
	 * 获取文件的图标
	 */
	public Drawable getFileIcon(File file){
		 /* 取得扩展名 */
        String end=file.getName().substring(file.getName().lastIndexOf(".") + 1,file.getName().length()).toLowerCase(); 
        /* 依扩展名的类型决定MimeType */
        /*if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||
                end.equals("xmf")||end.equals("ogg")||end.equals("wav")){
            return getAudioFileIntent(filePath);
        }else if(end.equals("3gp")||end.equals("mp4")){
            return getAudioFileIntent(filePath);
        }else */if(end.equals("jpg")||end.equals("gif")||end.equals("png")||
                end.equals("jpeg")||end.equals("bmp")){
            return getResources().getDrawable(R.drawable.img_image_icon);
        }else if(end.equals("apk")){
        	 return getResources().getDrawable(R.drawable.img_apk_icon);
        }else if(end.equals("xls")||end.equals("xlsx")){
        	 return getResources().getDrawable(R.drawable.img_excel_icon);
        }else if(end.equals("doc") || end.equals("docx")){
        	 return getResources().getDrawable(R.drawable.img_word_icon);
        }else if(end.equals("pdf")){
        	return getResources().getDrawable(R.drawable.img_pdf_icon);
        }else if(end.equals("txt")){
        	return getResources().getDrawable(R.drawable.img_text_icon);
        }else{
        	return getResources().getDrawable(R.drawable.img_unknow_icon);
        }
	}
	 
	         

}
