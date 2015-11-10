package com.ems.express.ui.message;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.adapter.message.MailDeliverAdapter;
import com.ems.express.adapter.message.SendNoticeBean;
import com.ems.express.constant.Constant;
import com.ems.express.fragment.message.DeliverFragment;
import com.ems.express.fragment.message.MessageFragment;
import com.ems.express.net.MyRequest;
//import com.ems.express.fragment.message.NewsFragment;
import com.ems.express.ui.HomeActivity;
import com.ems.express.util.AnimationUtil;
import com.ems.express.util.DialogUtils;
import com.ems.express.util.ParamsUtil;
import com.ems.express.util.SpfsUtil;
import com.ems.express.util.ToastUtil;

public class MessageActivity extends Activity implements OnClickListener{
	
	private TextView tv;
	
	private FragmentManager mFragmentManager;
	private FragmentTransaction mFragmentTransaction;

	private DeliverFragment mDeliverFragment;
	private MessageFragment mMessageFragment;
	//private NewsFragment mNewsFragment;

	private TextView deliverradiobutton;

	//private TextView mailradiobutton;

	private TextView messageradiobutton;
	
	private Context mContext;

	private static ImageView mImgPromptMessage,mImgPrompt;
	
	private AnimationUtil animationUtil;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		mContext = this;
		animationUtil = new AnimationUtil(mContext, R.style.dialog_animation);
		init();
//		//造假数据
//		App.dbHelper.insertDeliveryMessage(App.db, 
//				12.3, 32.1, "18510811449", 
//				"12312", "123123", "1",
//				"1231", "123", "1231", "1",
//				"2015-03-31 14:05", "1", "CNPLZ0149634400", "123123",
//				"123", "1");
//		App.dbHelper.insertDeliveryMessage(App.db, 
//				12.3, 32.1, "", 
//				"12312", "123123", "2",
//				"1231", "123", "1231", "1",
//				"2015-03-31 14:05", "1", "CNPLZ014963JJ", "123123",
//				"123", "1");
//		
//		App.dbHelper.insertSendNotice(App.db,
//				"2015-03-31 14:06", "1",12.3, 
//				32.1, "15711265573", "1", 
//				"1", "1", "1", "1", 
//				"1","3","1111111111111111", "1", "11");
//		App.dbHelper.insertSendNotice2(App.db,
//				"2015-03-31 14:06", "1", 
//				"8", "222222222222222");
		setPromptMessage();
		setPrompt();
	}
	

	private void init() {
		tv=(TextView)findViewById(R.id.tv_title);
		tv.setText("通知中心");
		tv.setTextSize(20);
		
		deliverradiobutton  =(TextView)findViewById(R.id.message_deliver);
		//mailradiobutton  =(TextView)findViewById(R.id.message_mail);
		messageradiobutton  =(TextView)findViewById(R.id.message_message);
		
		mImgPromptMessage =(ImageView)findViewById(R.id.message_cirle);
		
		mImgPrompt =(ImageView)findViewById(R.id.fragment_circle);
		
		deliverradiobutton.setOnClickListener(this);
		//mailradiobutton.setOnClickListener(this);
		messageradiobutton.setOnClickListener(this);
		
		deliverradiobutton.setSelected(true);
		
		mFragmentManager = getFragmentManager();
		mFragmentTransaction = mFragmentManager.beginTransaction();
		
		mDeliverFragment = new DeliverFragment();
		mMessageFragment = new MessageFragment();
		//mNewsFragment = new NewsFragment();
		
		mFragmentTransaction.add(R.id.fragment_message,mDeliverFragment);
		mFragmentTransaction.commit();
		
	}
	
	//返回
		public void back(View v) {
			finish();
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.message_deliver:
				setPromptMessage();
				setPrompt();
				mFragmentTransaction = mFragmentManager.beginTransaction();
				deliverradiobutton.setSelected(true);
				messageradiobutton.setSelected(false);
				//mailradiobutton.setSelected(false);
				mFragmentTransaction.replace(R.id.fragment_message, mDeliverFragment);
				mFragmentTransaction.commit();
				break;
			/*case R.id.message_mail:
				mFragmentTransaction = mFragmentManager.beginTransaction();
				mailradiobutton.setSelected(true);
				deliverradiobutton.setSelected(false);
				messageradiobutton.setSelected(false);
				mFragmentTransaction.replace(R.id.fragment_message, mNewsFragment);
				mFragmentTransaction.commit();
				break;*/
			case R.id.message_message:
				setPrompt();
				setPromptMessage();
				mImgPrompt.setVisibility(View.GONE);
				mFragmentTransaction = mFragmentManager.beginTransaction();
				messageradiobutton.setSelected(true);
				deliverradiobutton.setSelected(false);
				//mailradiobutton.setSelected(false);
				mFragmentTransaction.replace(R.id.fragment_message, mMessageFragment);
				mFragmentTransaction.commit();
				break;
			}
		}
		
		public static void setPromptMessage() {
			int eliveryCount = App.dbHelper.queryUnRedDeliveryMessage(App.db);
			if(eliveryCount>0){
				mImgPromptMessage.setVisibility(View.VISIBLE);
			}else{
				mImgPromptMessage.setVisibility(View.GONE);
			}
		}
		
		public static void setPrompt() {
			int eliveryCount = App.dbHelper.querySendNoticeMessage(App.db);
			List<SendNoticeBean> messageListData = App.dbHelper.querySendNotice(App.db);
			int sendId;
			for(int i = 0 ; i<messageListData.size();i++){
			sendId = messageListData.get(i).getSendId();
			App.dbHelper.updateSendNotice(App.db, ""+sendId);
			}
			if(eliveryCount>0){
				mImgPrompt.setVisibility(View.VISIBLE);
			}else{
				mImgPrompt.setVisibility(View.GONE);
			}
		}
		
		
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			switch (requestCode) {
			case 999:
				System.out.println("扫扫待处理");
				if(data!=null){
					String mailNum = data.getStringExtra("mailNum");
					String resultStr = data.getExtras().getString("txtResult");
					if(resultStr.isEmpty()){
						DialogUtils.successMessage(this, "扫件失败");
					}else if(!resultStr.equals(mailNum)){
						DialogUtils.successMessage(this, "邮件不匹配");
					}else{
					//签收
					MailDeliverAdapter.receiptHandle(data.getIntExtra("index", 0));
					MessageActivity.setPromptMessage();
					}
				}
				break;

			default:
				break;
			}
		}
}
