package com.ems.express.ui.chat;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.bean.ChatListItemBean;
import com.ems.express.core.msg.IObserverBase;
import com.ems.express.core.msg.MessageManager;
import com.ems.express.net.Carrier;
import com.ems.express.ui.BaiduMapActivity;
import com.ems.express.ui.HomeActivity;
import com.ems.express.util.ToastUtil;

public class ChatListActivity extends Activity implements OnClickListener {
	private Context mContext = ChatListActivity.this;
	private ListView mLvFriend;
	private List<ChatListItemBean> mData = new ArrayList<ChatListItemBean>();
	private ChatListAdapter mAdapter;
	private ImageView mBtnBack;
	private TextView mBtnNext;
	private PopupWindow popWin = null;//
	private View popView = null;//
	private RelativeLayout mLoutTitle;
	private Boolean mFlag = false;
	private TextView mNotCourier;

	public static void startAction(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, ChatListActivity.class);
		context.startActivity(intent);
	}

	IObserverBase ib = new IObserverBase() {

		@Override
		public void sendMessage(String message) {
//			ToastUtil.show(ChatListActivity.this, "ChatListActivity接收被观察者发送的消息");
			setMessageView();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_list);
		MessageManager.getInstance().attach(ib);
		mBtnBack = (ImageView) findViewById(R.id.back_button);
		mBtnNext = (TextView) findViewById(R.id.next_button);
		mLoutTitle = (RelativeLayout) findViewById(R.id.title);
		mLvFriend = (ListView) findViewById(R.id.friend_list);
		mNotCourier = (TextView) findViewById(R.id.not_courier);
		mAdapter = new ChatListAdapter(this, mData);
		mLvFriend.setAdapter(mAdapter);
		mBtnBack.setOnClickListener(this);
		mBtnNext.setOnClickListener(this);
//		Carrier point = new Carrier();
//		point.setLongitude(123.11);
//		point.setLatitude(123.123);
//		point.setPeople("张三");
//		point.setMobile("15027004360");
//		//607d653ab5a33669b3e8838b3cb76bf2
//		point.setClientId("2a1c7e52894b333c82833398375b9235");
//		point.setEmployeeNo("123");
//		point.setSID("null");
//		App.dbHelper.insertChatList(App.db, point);
//
//		loadChatListData();// 测试数据

		mLvFriend.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MainActivity.startAction(ChatListActivity.this,
						mData.get(position));
//				String employeeNo = App.dbHelper.queryEmployeeNoIsClientId(App.db,mData.get(position).getClientId());
//				App.dbHelper.updateUnReadMessage(App.db, employeeNo);
			}
		});
	}

	public void loadChatListData() {
		mData.clear();
		List<Carrier> list = App.dbHelper.queryAllChatList(App.db);
		if (!(list.size() > 0)) {
			mNotCourier.setVisibility(View.VISIBLE);
			mLvFriend.setVisibility(View.GONE);
		} else {
			mNotCourier.setVisibility(View.GONE);
			mLvFriend.setVisibility(View.VISIBLE);
			for (int i = 0; i < list.size(); i++) {
				ChatListItemBean bean = new ChatListItemBean();
				bean.setImage(list.get(i).getSID());
				bean.setMobile(list.get(i).getMobile());
				bean.setName(list.get(i).getPeople());
				bean.setClientId(list.get(i).getClientId());
				bean.setUnRedCount(list.get(i).getUnRedCount());
				mData.add(bean);
			}
			mAdapter.notifyDataSetChanged();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button:
			finish();
			break;
		case R.id.next_button:
			 Intent intent = new Intent();
			 intent.putExtra(BaiduMapActivity.KEY_TYPE,
			 BaiduMapActivity.TYPE_CARRIER);
			 intent.setClass(this, BaiduMapActivity.class);
			 startActivity(intent);
			break;

		default:
			break;
		}
	}
	
	public void setMessageView(){
		mData.clear();
		List<Carrier> list = App.dbHelper.queryAllChatList(App.db);
		for (int i = 0; i < list.size(); i++) {
			ChatListItemBean bean = new ChatListItemBean();
			bean.setImage(list.get(i).getSID());
			bean.setMobile(list.get(i).getMobile());
			bean.setName(list.get(i).getPeople());
			bean.setClientId(list.get(i).getClientId());
			bean.setUnRedCount(list.get(i).getUnRedCount());
			mData.add(bean);
		}
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onResume() {
		super.onResume();
		loadChatListData();
		setMessageView();
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

}
