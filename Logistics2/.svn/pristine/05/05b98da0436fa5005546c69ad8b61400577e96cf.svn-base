package com.ems.express.ui;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.ems.express.App;
import com.ems.express.PriceActivity;
import com.ems.express.R;
import com.ems.express.constant.Constant;
import com.ems.express.core.msg.IObserverBase;
import com.ems.express.core.msg.MessageManager;
import com.ems.express.net.MyRequest;
import com.ems.express.scan.activity.CaptureActivity;
import com.ems.express.ui.chat.ChatListActivity;
import com.ems.express.ui.check.ResultActivity;
import com.ems.express.ui.mail.MailOrderListActivity;
import com.ems.express.ui.message.MessageActivity;
import com.ems.express.ui.send.SendActivity;
import com.ems.express.ui.view.CircleImageView;
import com.ems.express.ui.view.SlideMenu;
import com.ems.express.util.DialogUtils;
import com.ems.express.util.NotificationUtil;
import com.ems.express.util.ParamsUtil;
import com.ems.express.util.SpfsUtil;
import com.ems.express.util.ToastUtil;
import com.google.zxing.common.StringUtils;

/**
 * 首页
 */
public class HomeActivity extends Activity implements OnClickListener {
	private View newMessage = null;
	private EditText expressId = null;
	private Context mContext;
	public static SlideMenu slideMenu;
	public static CircleImageView mIconView;
	public static TextView mUserNameView;
	private TextView mStatusView,mMessageCount,mDeliveryMessage,mPromptMessage;
	private LinearLayout mMessageView;
	private RelativeLayout mDeliveryMessageView;
	private RelativeLayout mImgPromptMessage;
	
	
	IObserverBase ib = new IObserverBase() {
		
		@Override
		public void sendMessage(String message) {
//			ToastUtil.show(HomeActivity.this, "HomeActivity接收被观察者发送的消息");
			if("0".equals(message)){
				setMessageView();
			}else if("2".equals(message)){
//				setEliveryMessageView();
				setPromptMessage();
			}
		}

	};
	private LinearLayout layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		MessageManager.getInstance().attach(ib);
		ButterKnife.inject(this);
		init();
		initSlideMenuOfChildView();
		HashMap<String, Object> json = new HashMap<String, Object>();
		json.put("queryFlag", "2");
		json.put("sendCity", "长春");
		json.put("recvCity", "北京");
		json.put("sendDate", "2015-02-06 12:03:11");
		String params = ParamsUtil.getUrlParamsByMap(json);
		System.out.println("json-pre:" + params);
		MyRequest<Object> req = new MyRequest<Object>(
				Method.POST,
				null,
				Constant.TimeQuery,
				new Listener<Object>() {

					@Override
					public void onResponse(Object arg0) {
						System.out.println("obj:" + arg0);

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						arg0.printStackTrace();
					}
				}, params);

		App.getQueue().add(req);
		setMessageView();
		setEliveryMessageView();
	}

	private void init() {
		mContext = this;
		newMessage = findViewById(R.id.img_new_message);
		expressId = (EditText) findViewById(R.id.edt_express_id);
		slideMenu = (SlideMenu) findViewById(R.id.slidemenu);
		findViewById(R.id.mine).setOnClickListener(this);// 我的
		findViewById(R.id.btn_search).setOnClickListener(this);// 查询按钮
		findViewById(R.id.imgb_scan).setOnClickListener(this);// 扫码
		findViewById(R.id.item_communication).setOnClickListener(this);// 实时通讯
		findViewById(R.id.item_service_range).setOnClickListener(this);// 收送范围
		findViewById(R.id.item_time_query).setOnClickListener(this);// 时效查询
		findViewById(R.id.item_carrier_location).setOnClickListener(this);// 快递员位置
		findViewById(R.id.item_query_expense).setOnClickListener(this);// 运费查询
		findViewById(R.id.item_send).setOnClickListener(this);// 寄件
		findViewById(R.id.item_servic_point).setOnClickListener(this);// 服务网点
		findViewById(R.id.adv).setOnClickListener(this);// 服务网点
		mMessageCount = (TextView) findViewById(R.id.message_count);
		mMessageView = (LinearLayout) findViewById(R.id.message_view);
	}

	private void initSlideMenuOfChildView() {
		mIconView = (CircleImageView) slideMenu.findViewById(R.id.iv_icon);
		mUserNameView = (TextView) slideMenu.findViewById(R.id.tv_name);
		mStatusView = (TextView) slideMenu.findViewById(R.id.tv_status);
		String headImageUrl = SpfsUtil.loadHeadImageUrl();
		System.out.println("headImageUrl-----"+headImageUrl);
		if(headImageUrl!=""){
			Bitmap photo = BitmapFactory.decodeFile(headImageUrl);
			mIconView.setImageBitmap(photo);
		}else{
			Bitmap aa = BitmapFactory.decodeResource(getResources(), R.drawable.default_head);
			mIconView.setImageBitmap(aa);
		}
		if(!TextUtils.isEmpty(SpfsUtil.loadName()) && !"null".equals(SpfsUtil.loadName())){
			mUserNameView.setText(SpfsUtil.loadName());
		}else{
			mUserNameView.setText("");
		}

		// slideMenu.findViewById(R.id.btn_post_notice).setOnClickListener(this);//寄件提醒
		slideMenu.findViewById(R.id.btn_bind_card).setOnClickListener(this);// 绑定银行卡
		slideMenu.findViewById(R.id.btn_order_list).setOnClickListener(this);// 订单列表
		slideMenu.findViewById(R.id.btn_personal_center).setOnClickListener(
				this);// 个人中心
		slideMenu.findViewById(R.id.btn_settings).setOnClickListener(this);// 设置
		slideMenu.findViewById(R.id.btn_regist).setOnClickListener(this);// 注册
		slideMenu.findViewById(R.id.btn_sender).setOnClickListener(this);//寄件记录
		slideMenu.findViewById(R.id.btn_message_centre).setOnClickListener(this);//通知中心
		slideMenu.findViewById(R.id.btn_message).setOnClickListener(this);// 消息中心
		
		mDeliveryMessage = (TextView) slideMenu.findViewById(R.id.delivery_message);
		mDeliveryMessageView = (RelativeLayout) slideMenu.findViewById(R.id.delivery_message_view);
		layout = (LinearLayout) slideMenu.findViewById(R.id.btn_message_rl);
		layout.setOnClickListener(this);
		mPromptMessage = (TextView) slideMenu.findViewById(R.id.tv_message_prompt);
		mImgPromptMessage = (RelativeLayout) slideMenu.findViewById(R.id.img_message_prompt);
		
		if(SpfsUtil.loadPhone().equals("")){
			((TextView)slideMenu.findViewById(R.id.btn_regist)).setText("登录");
		}else{
			((TextView)slideMenu.findViewById(R.id.btn_regist)).setText("登出");
		}

	}

	private void showNewMessage(boolean hasNew) {
		if (hasNew) {
			newMessage.setVisibility(View.VISIBLE);
		} else {
			newMessage.setVisibility(View.GONE);
		}
	}

	private void search() {
		String id = expressId.getEditableText().toString();
		if (TextUtils.isEmpty(id)) {
			ToastUtil.show(this, "运单号不能为空");
		} else {
			// TODO 跳转到运单查询界面
			ResultActivity.actionStart(mContext, id);
		}
	}

	Intent intent = null;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.adv:// 评价
//			ToastUtil.show(this, "评价");
//			CommentActivity.startAction(this);
			break;
		case R.id.item_communication:// 实时通讯
//			ToastUtil.show(this, "实时通讯");
			ChatListActivity.startAction(this);
			break;
		case R.id.item_service_range:// 收送范围
//			ToastUtil.show(this, "收送范围");
			intent = new Intent(this, ServiceRangeActivity.class);
			startActivity(intent);
			// TODO
			break;
		case R.id.item_time_query:// 时效查询
//			ToastUtil.show(this, "时效查询");
			// TODO
			intent = new Intent(this, TimeQueryActivity.class);
			startActivity(intent);
			break;
		case R.id.item_carrier_location:// 快递员位置
//			ToastUtil.show(this, "快递员位置");
			intent = new Intent(this, BaiduMapActivity.class);
			intent.putExtra(BaiduMapActivity.KEY_TYPE,
					BaiduMapActivity.TYPE_CARRIER);
			startActivity(intent);
			// TODO
			break;
		case R.id.item_query_expense:// 运费查询
			PriceActivity.actionStart(mContext);
			break;
		case R.id.item_send:// 寄件
			if(SpfsUtil.loadPhone().equals("")){
				Intent intent1 = new Intent(this,LoginActivity.class);
				startActivity(intent1);
				ToastUtil.show(this, "请先登录");
			}else{
				SendActivity.actionStart(mContext);
			}
			break;
		case R.id.item_servic_point:// 服务网点
//			ToastUtil.show(this, "服务网点");
			intent = new Intent(this, BaiduMapActivity.class);
			intent.putExtra(BaiduMapActivity.KEY_TYPE,
					BaiduMapActivity.TYPE_SERVICE_POINT);
			startActivity(intent);
			// TODO
			break;
		case R.id.btn_search:
			search();
			break;
		case R.id.mine:
			if (slideMenu.isMainScreenShowing()) {
				slideMenu.openMenu();
			} else {
				slideMenu.closeMenu();
			}
//			ToastUtil.show(this, "我的");
			break;
		// case R.id.btn_post_notice:
		// ToastUtil.show(this, "寄件提醒");
		// break;
		case R.id.btn_bind_card:
			ToastUtil.show(this, "绑定银行卡");
			break;
		case R.id.btn_order_list:
			ToastUtil.show(this, "订单列表");
			break;
		case R.id.btn_personal_center:
			if(SpfsUtil.loadPhone().equals("")){
				Intent intent1 = new Intent(this,LoginActivity.class);
				startActivity(intent1);
				ToastUtil.show(this, "请先登录");
			}else{
				toPersonalCenter();	
			}
			break;
		case R.id.btn_settings:
			SettingActivity.actionStart(mContext);
			break;
		case R.id.btn_message_rl:
			Intent intent3 = new Intent(this, MessageActivity.class);
			startActivity(intent3);
			break;
		case R.id.btn_message:
			MessageCenterActivity.actionStart(mContext);
			break;
		case R.id.btn_regist:
			if(SpfsUtil.loadPhone().equals("")){
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
			}else{
				SpfsUtil.saveAddress("");
				SpfsUtil.saveHeadImageUrl("");
				SpfsUtil.saveName("");
				SpfsUtil.saveId("");
				SpfsUtil.savePhone("");
				SpfsUtil.saveTelephone("");
				((TextView)slideMenu.findViewById(R.id.btn_regist)).setText("登录");
				Bitmap aa = BitmapFactory.decodeResource(getResources(), R.drawable.default_head);
				mIconView.setImageBitmap(aa);
				mUserNameView.setText("");
			}
			break;
		case R.id.imgb_scan:
			Intent intent1 = new Intent(this, CaptureActivity.class);
			startActivityForResult(intent1, 999);
			break;
			
		case R.id.btn_sender:
			if(SpfsUtil.loadPhone().equals("")){
				Intent noLogin = new Intent(this, LoginActivity.class);
				startActivity(noLogin);
				ToastUtil.show(this, "请先登录");
			}else{
				Intent mailIntent = new Intent(this, MailOrderListActivity.class);
				startActivity(mailIntent);
			}
			break;
			
		case R.id.btn_message_centre:
			Intent intent4 = new Intent(this, MessageActivity.class);
			startActivity(intent4);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 999:
			System.out.println("扫扫待处理");
			if(data!=null){
				Bundle bundle = data.getExtras();
				String resultStr = bundle.getString("txtResult");
				expressId.setText(resultStr);
				search();
			}
			break;

		default:
			break;
		}
	}

	private void toPersonalCenter() {
		Intent intent = new Intent(this, PersonalCenterActivity.class);
		startActivity(intent);
	}

	/**
	 * 我的
	 * */
	@OnClick(R.id.mine)
	void toOpenAndCloseSlideMenu() {
		if (slideMenu.isMainScreenShowing()) {
			slideMenu.openMenu();
		} else {
			slideMenu.closeMenu();
		}
//		ToastUtil.show(this, "我的");
	}

	public void setEliveryMessageView(){
		int eliveryCount = App.dbHelper.queryUnRedDeliveryMessage(App.db);
		if(eliveryCount>0){
			mDeliveryMessageView.setVisibility(View.VISIBLE);
			mDeliveryMessage.setText(eliveryCount+"");
			newMessage.setVisibility(View.VISIBLE);
		}else{
			newMessage.setVisibility(View.GONE);
			mDeliveryMessageView.setVisibility(View.GONE);
		}
	}
	
	private void setPromptMessage() {
		int eliveryCount = App.dbHelper.queryUnRedDeliveryMessage(App.db);
		int querySendNotice = App.dbHelper.querySendNoticeMessage(App.db);
		if(eliveryCount>0||querySendNotice>0){
			mImgPromptMessage.setVisibility(View.VISIBLE);
			mPromptMessage.setText(querySendNotice+eliveryCount+"");
			newMessage.setVisibility(View.VISIBLE);
		}else{
			newMessage.setVisibility(View.GONE);
			mImgPromptMessage.setVisibility(View.GONE);
		}
		
	}
	public void setMessageView(){
		int count = App.dbHelper.queryAllUnReadMessage(App.db);
		if(count>0){
			mMessageView.setVisibility(View.VISIBLE);
			mMessageCount.setText(count+"");
			//setNotification();
		}else{
			mMessageView.setVisibility(View.GONE);
			mMessageCount.setText("");
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		String headImageUrl = SpfsUtil.loadHeadImageUrl();
		if(headImageUrl!=""){
			Bitmap photo = BitmapFactory.decodeFile(headImageUrl);
			mIconView.setImageBitmap(photo);
		}else{
			Bitmap aa = BitmapFactory.decodeResource(getResources(), R.drawable.default_head);
			mIconView.setImageBitmap(aa);
		}
		if(!TextUtils.isEmpty(SpfsUtil.loadName()) && !"null".equals(SpfsUtil.loadName())){
			mUserNameView.setText(SpfsUtil.loadName());
		}else{
			mUserNameView.setText("");
		}
		if(SpfsUtil.loadPhone().equals("")){
			((TextView)slideMenu.findViewById(R.id.btn_regist)).setText("登录");
		}else{
			((TextView)slideMenu.findViewById(R.id.btn_regist)).setText("登出");
		}
		setMessageView();
		setEliveryMessageView();
		setPromptMessage();
	}

	@Override
	protected void onStart() {
		super.onStart();
		MessageManager.getInstance().attach(ib);
	}
	@Override
	protected void onStop() {
		super.onStop();
//		MessageManager.getInstance().detach(ib);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
//		MessageManager.getInstance().detach(ib);
	}
	
	@Override  
    public void onBackPressed() {  
        Intent intent= new Intent(Intent.ACTION_MAIN);  
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        intent.addCategory(Intent.CATEGORY_HOME);  
        startActivity(intent); 
    } 
}
