package com.example.allthingschat.util;

import java.util.List;

import com.example.allthingschat.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class DialogUtils {
	public static Dialog add_friend(final Context context){//添加好友dialog
		final Dialog dialog = new Dialog(context,R.style.DialogStyle4);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
		TextView content = (TextView) view.findViewById(R.id.dialog_content);
		content.setText("邀请已经发送，很快你们将成为好友，多加好友，朋友更多哦");
		Button buttonCancel = (Button) view.findViewById(R.id.dialog_determine);
		buttonCancel.setOnClickListener(new OnClickListener() {
			
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
	public static Dialog sharefriend(final Context context){//分享dialog
		final Dialog dialog = new Dialog(context, R.style.DialogStyle4);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_bottom, null);
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		
		
		
		
		return null;
		
	}
}
