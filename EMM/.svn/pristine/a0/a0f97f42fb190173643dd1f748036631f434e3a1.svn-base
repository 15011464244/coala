package com.koala.emm.util;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.koala.emm.R;

public class DialogUtils{
//	private static Dialog dialogfuj;
//	private static Dialog dialog ;
	
	/**
	 * 确定、取消dialog
	 * @param context
	 * @return
	 */
	public static Dialog showTwoButton2(final Context context){
		final Dialog dialog = new Dialog(context,R.style.DialogStyle4);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
		TextView content = (TextView) view.findViewById(R.id.dialog_content);
		content.setText("清除的数据不能恢复！\n您确定要清除缓存吗？");
		Button buttonCancel = (Button) view.findViewById(R.id.dialog_cancel);
		buttonCancel.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		Button buttonDetermine = (Button) view.findViewById(R.id.dialog_determine);
		buttonDetermine.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		return null;
	}
	
	

/**
 * 通知提示框
 * @param context
 * @param Content
 * @param btnStr
 * @return
 */
	public static Dialog noticeDialog(final Context context,final String Content,String btnStr){
		final Dialog dialog = new Dialog(context,R.style.DialogStyle4);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_notice, null);
		TextView content = (TextView) view.findViewById(R.id.dialog_content);
		content.setText(Content);
		
		Button buttonDetermine = (Button) view.findViewById(R.id.dialog_determine);
		buttonDetermine.setText(btnStr);
		buttonDetermine.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.setContentView(view);
		dialog.show();
		return null;
	}
	
	/**
	 * 带标题通知提示框
	 * @param context
	 * @param Content
	 * @param btnStr
	 * @param title
	 * @return
	 */
		public static Dialog noticeDialog(final Context context,final String Content,String btnStr,String title){
			final Dialog dialog = new Dialog(context,R.style.DialogStyle4);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			
			View view = LayoutInflater.from(context).inflate(R.layout.dialog_notice2, null);
			TextView titleView = (TextView) view.findViewById(R.id.dialog_title2);
			titleView.setVisibility(View.VISIBLE);
			titleView.setText(title);
			View line = (View)view.findViewById(R.id.line_1);
			line.setVisibility(view.VISIBLE);
					
			TextView content = (TextView) view.findViewById(R.id.dialog_content);
			content.setText(Content);
			
			Button buttonDetermine = (Button) view.findViewById(R.id.dialog_determine);
			buttonDetermine.setText(btnStr);
			buttonDetermine.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			dialog.setContentView(view);
			dialog.show();
			return null;
		}



}
