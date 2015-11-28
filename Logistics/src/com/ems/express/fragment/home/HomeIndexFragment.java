package com.ems.express.fragment.home;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient.ConnectCallback;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.model.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.constant.Constant;
import com.ems.express.net.MyRequest;
import com.ems.express.ui.BaiduMapActivity;
import com.ems.express.ui.Home2Activity;
import com.ems.express.ui.LoginActivity;
import com.ems.express.ui.ToolsActivity;
import com.ems.express.ui.chat.ChatListActivity;
import com.ems.express.ui.message.send.SendMessageListActivity;
import com.ems.express.ui.send.SendActivity;
import com.ems.express.util.AnimationUtil;
import com.ems.express.util.DialogUtils;
import com.ems.express.util.ParamsUtil;
import com.ems.express.util.SpfsUtil;
import com.ems.express.util.ToastUtil;

public class HomeIndexFragment extends Fragment implements OnClickListener {
	private int width;
	private int height;
	private RelativeLayout viewPageVeiw;
	private LinearLayout ll_one;
	private LinearLayout ll_two;
	
	private View view;

	private LinearLayout send, receive, talk, message, near, tools;
	private Context mContext;

	// 来消息的时候判断是否被隐藏，改变红圈
	public static Fragment indexFragment;

	private TextView recNoread;
	private TextView sendNoread;

	private ViewPager mViewPager;
	private int imageIds[];
	private ArrayList<ImageView> images;
	private ViewPagerAdapter adapter;
	private ArrayList<View> dots;
	private boolean isFirstIn = true;

	private int oldPosition = 0;// 记录上一次点的位置
	private int currentItem ; // 当前页面
	
	
	private AnimationUtil animationUtil;
	private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
	
	private ViewPagerTask viewPagerTask = new ViewPagerTask();

	public BroadcastReceiver newMsgBReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			showMessageTag();
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.home_index_fragment, null);
		mContext = this.getActivity();
		animationUtil = new AnimationUtil(mContext, R.style.dialog_animation);
		indexFragment = this;

		// 注册一个自定义的广播接收器
		IntentFilter filter = new IntentFilter();
		filter.addAction("NewMsgReceiver_Action");
		mContext.registerReceiver(newMsgBReceiver, filter);

		initView();
		
		if(isFirstIn){
			// 每隔10秒钟切换一张图片
			
			scheduledExecutorService.scheduleWithFixedDelay(viewPagerTask,
					10, 3, TimeUnit.SECONDS);
			isFirstIn = false;
		}
		Log.e("gongjie", "fragment oncreat");
		return view;
	}

	// 切换图片
	private class ViewPagerTask implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
//			currentItem = (currentItem + 1) % imageIds.length;
			synchronized (mViewPager) {
                currentItem = (currentItem+1)%images.size();
                handler.obtainMessage().sendToTarget();
            }
		}

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			// 设置当前页面
			mViewPager.setCurrentItem(currentItem);
		}

	};

	private void initView() {
		
		// 由于在xml里不能进行weight适配，所以在代码里进行了适配
		DisplayMetrics metric = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
		width = metric.widthPixels;
//		Log.e("gongjie", width+"");
		height = metric.heightPixels;
		Log.e("gongjie", height+"");
		viewPageVeiw = (RelativeLayout) view.findViewById(R.id.rl_viewpager);
		ll_one = (LinearLayout) view.findViewById(R.id.ll_one);
		ll_two = (LinearLayout) view.findViewById(R.id.ll_two);
		LinearLayout.LayoutParams linearParams  = (android.widget.LinearLayout.LayoutParams) viewPageVeiw.getLayoutParams();
		LinearLayout.LayoutParams linearParams1 = (android.widget.LinearLayout.LayoutParams) ll_one.getLayoutParams();
		LinearLayout.LayoutParams linearParams2 = (android.widget.LinearLayout.LayoutParams) ll_two.getLayoutParams();
		// 获取手机型号：
		Log.e("gongjie", android.os.Build.MODEL+"");
		//获取手机厂商：
		Log.e("gongjie", android.os.Build.MANUFACTURER+"");
		if (height == 1803) {
			if (android.os.Build.MANUFACTURER.equals("OPPO")) {
				Log.e("gongjie", "进入OPPO");
				linearParams.height = height*10/40;
				linearParams.width = width;
				viewPageVeiw.setLayoutParams(linearParams);
				linearParams1.height = height*14/80;
				linearParams1.width = width; 
				ll_one.setLayoutParams(linearParams1);
				linearParams2.height = height*14/80;
				linearParams2.width = width;
				ll_two.setLayoutParams(linearParams2);
			}
		}else if (height == 1920) {
			if (android.os.Build.MANUFACTURER.equals("Mizu")) {
				Log.e("gongjie", "进入Meizu");
				linearParams.height = height*10/40;
				linearParams.width = width;
				viewPageVeiw.setLayoutParams(linearParams);
				linearParams1.height = height*11/80;
				linearParams1.width = width; 
				ll_one.setLayoutParams(linearParams1);
				linearParams2.height = height*6/40;
				linearParams2.width = width;
				ll_two.setLayoutParams(linearParams2);
			}
		}
		send = (LinearLayout) view.findViewById(R.id.ll_send);
		receive = (LinearLayout) view.findViewById(R.id.ll_receive);
		talk = (LinearLayout) view.findViewById(R.id.ll_talk);
		message = (LinearLayout) view.findViewById(R.id.ll_message);
		near = (LinearLayout) view.findViewById(R.id.ll_near);
		tools = (LinearLayout) view.findViewById(R.id.ll_tools);
		recNoread = (TextView) view.findViewById(R.id.rec_noread_count);
		sendNoread = (TextView) view.findViewById(R.id.send_noread_count);

		
		send.setOnClickListener(this);
		receive.setOnClickListener(this);
		talk.setOnClickListener(this);
		message.setOnClickListener(this);
		near.setOnClickListener(this);
		tools.setOnClickListener(this);

//		signCheck(SpfsUtil.loadPhone());
		// 隐藏扫件
		Home2Activity parent = (Home2Activity) getActivity();
		parent.showScan(false);
		parent.showSign(true);
		

		mViewPager = (ViewPager) view.findViewById(R.id.vp_image_change);
		mViewPager.setFocusable(true);
		// 图片ID
		imageIds = new int[] { R.drawable.img_index, R.drawable.img_index3, R.drawable.img_index4};
		// 显示的图片
		images = new ArrayList<ImageView>();
		for (int i = 0; i < imageIds.length; i++) {
			ImageView imageView = new ImageView(mContext);
			DisplayMetrics dm = new DisplayMetrics();
			// 为了让轮播图正常全显示出来
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
			int tempWidth = BitmapFactory.decodeResource(getResources(), imageIds[i]).getWidth();
			int tempHeight = BitmapFactory.decodeResource(getResources(), imageIds[i]).getHeight();
			LinearLayout.LayoutParams   params =  (android.widget.LinearLayout.LayoutParams) viewPageVeiw.getLayoutParams();
			params.height = (int) (tempHeight * ((double) dm.widthPixels / (double) tempWidth));
			viewPageVeiw.setLayoutParams(params);
			imageView.setImageResource(imageIds[i]);
			images.add(imageView);
		}
		
		//显示的点
        dots = new ArrayList<View>();
        dots.add(view.findViewById(R.id.dot_0));
        dots.add(view.findViewById(R.id.dot_1));
        dots.add(view.findViewById(R.id.dot_2));

		adapter = new ViewPagerAdapter();
		mViewPager.setAdapter(adapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			boolean isAutoPlay = false; 
			@Override
			public void onPageSelected(int position) {
				/*// TODO Auto-generated method stub
				// title.setText(titles[position]);	 
				Log.e("gongjie", oldPosition+"--" +position);
				 dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
				 dots.get(position).setBackgroundResource(R.drawable.dot_focused);

				oldPosition = position;
				currentItem = position;*/
				currentItem = position;
				 for(int i=0;i < dots.size();i++){
		                if(i == position){
		                    ((View)dots.get(position)).setBackgroundResource(R.drawable.dot_focused);
		                }else {
		                    ((View)dots.get(i)).setBackgroundResource(R.drawable.dot_normal);
		                }
		            }
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
//				
//				  switch (arg0) {
//		            case 1:// 手势滑动，空闲中
//		                isAutoPlay = false;
//		                break;
//		            case 2:// 界面切换中
//		                isAutoPlay = true;
//		                break;
//		            case 0:// 滑动结束，即切换完毕或者加载完毕
//		                // 当前为最后一张，此时从右向左滑，则切换到第一张
//		                if (mViewPager.getCurrentItem() == mViewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
//		                	mViewPager.setCurrentItem(0);
//		                }
//		                // 当前为第一张，此时从左向右滑，则切换到最后一张
//		                else if (mViewPager.getCurrentItem() == 0 && !isAutoPlay) {
//		                	mViewPager.setCurrentItem(mViewPager.getAdapter().getCount() - 1);
//		                }
//		                break;
//		        }

			}
		});

	}
	private void startPlay(){
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(viewPagerTask,
				10, 10, TimeUnit.SECONDS);
        
//        scheduledExecutorService.scheduleAtFixedRate(viewPagerTask, 1, 4, TimeUnit.SECONDS);
    }
    /**
     * 停止轮播图切换
     */
    private void stopPlay(){
    	if (scheduledExecutorService != null) {
    		scheduledExecutorService.shutdown();
		}
    }
	
	/**
	 * 签到check
	 * @param phone
	 * @return
	 */
    
	public void signCheck(String phone){
		animationUtil.show();
		HashMap<String, Object> json = new HashMap<String, Object>();
		json.put("mobile", phone);
		String params = ParamsUtil.getUrlParamsByMap(json);
		MyRequest<Object> req = new MyRequest<Object>(Request.Method.POST, null, Constant.checkSign,
				new Response.Listener<Object>() {

					@Override
					public void onResponse(Object arg0) {
							Log.e("msg", arg0.toString());
							
							animationUtil.dismiss();
							try {
								String result = (String) arg0;
								JSONObject object = new JSONObject(result.toString());
								if (object.getInt("result") == 1) {
									//已签到
									SpfsUtil.setIsSign(true);
								} else {
									//未签到
									SpfsUtil.setIsSign(false);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
				
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						Toast.makeText(getActivity(), "查询是否签到失败!", Toast.LENGTH_LONG).show();
						arg0.printStackTrace();
					}
				}, params);
		App.getQueue().add(req);
		
	}


	@Override
	public void onResume() {
		super.onResume();
		Log.e("gongjie", "fragment onresume");
		
		showMessageTag();
		mViewPager.setCurrentItem(0);
//			startPlay();
	}

	public void showMessageTag() {
		// 是否有未读收件消息
		int recMsgCount = App.dbHelper.queryUnRedDeliveryMessage(App.db);
		if (recMsgCount == 0) {
			recNoread.setVisibility(View.GONE);
		} else {
			recNoread.setVisibility(View.VISIBLE);
			recNoread.setText(recMsgCount + "");
		}
		// 是否有未读寄件消息
		int receiveOtherCount = App.dbHelper.querySendNoticeMessage(App.db);
		int sendMsgCount = App.dbHelper.queryUnReadMessage(App.db);
		int total = receiveOtherCount + sendMsgCount;
		if (total == 0) {
			sendNoread.setVisibility(View.GONE);
		} else {
			sendNoread.setVisibility(View.VISIBLE);
			sendNoread.setText(total + "");
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.ll_send:
			if (SpfsUtil.loadPhone().equals("")) {
				Intent intent1 = new Intent(mContext, LoginActivity.class);
				startActivity(intent1);
				ToastUtil.show(mContext, "请先登录");
			} else {
				SendActivity.actionStart(mContext);
			}
			if (SpfsUtil.getIsLoadingCity() == true) {
				DialogUtils.successMessage(mContext, "初始化数据加载中\n请20秒后重新再试！");
				return;
			}

			break;
		case R.id.ll_receive:
			if (SpfsUtil.loadPhone().equals("")) {
				Intent intent1 = new Intent(mContext, LoginActivity.class);
				startActivity(intent1);
				ToastUtil.show(mContext, "请先登录");
			} else {
				DialogUtils.noticeDialog(mContext, "功能敬请期待", "知道了");
//				ReceiveMailActivity.actionStart(mContext);
			}
			break;
		case R.id.ll_talk:
			ToastUtil.show(mContext, "实时通讯");
			//融云的token
			String token = "h+sOjOvSP5wO9eUdbiF8hKKZgOBwfU4srIHJPxpl05ywbdNxTSrcwGohfdTC3K3l1F8zAXjhjOs=";
			RongIM.connect(token, new ConnectCallback() {

				@Override
				public void onError(ErrorCode arg0) {
					Toast.makeText(mContext, "connect onError", Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onSuccess(String arg0) {
					Toast.makeText(mContext, "connect onSuccess", Toast.LENGTH_SHORT).show();
					/**
					 * 设置用户信息的提供者，供 RongIM 调用获取用户名称和头像信息。
					 *
					 * @param userInfoProvider 用户信息提供者。
					 * @param isCacheUserInfo  设置是否由 IMKit 来缓存用户信息。<br>
					 *                         如果 App 提供的 UserInfoProvider。
					 *                         每次都需要通过网络请求用户数据，而不是将用户数据缓存到本地内存，会影响用户信息的加载速度；<br>
					 *                         此时最好将本参数设置为 true，由 IMKit 将用户信息缓存到本地内存中。
					 * @see UserInfoProvider
					 */
					RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

					    @Override
					    public UserInfo getUserInfo(String userId) {
					    	Log.e("gongjie", "userId"+userId);
					    	String dlvorgcode = "33004607";
					    	String username = "9401";
					    	String url = "http://111.75.223.93:9008/post-carrier-service/PhoneAction/findEmployeeImage?"+"dlvorgcode="+dlvorgcode+"&username="+username;
					    	UserInfo info = new UserInfo(userId, "gongjie", Uri.parse(url));
					        return info;//根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
					    }

					}, true);
					RongIM.getInstance().startConversationList(mContext);
				}

				@Override
				public void onTokenIncorrect() {
					// TODO Auto-generated method stub
					
				}
			});
			ChatListActivity.startAction(mContext);
			break;
		case R.id.ll_message:
			if (SpfsUtil.loadPhone().equals("")) {
				Intent intent1 = new Intent(mContext, LoginActivity.class);
				startActivity(intent1);
				ToastUtil.show(mContext, "请先登录");
			} else {
//				intent = new Intent(mContext, SendMessageActivity.class);
				intent = new Intent(mContext, SendMessageListActivity.class);
				intent.putExtra(BaiduMapActivity.KEY_TYPE,
						BaiduMapActivity.TYPE_CARRIER);
				startActivity(intent);
			}

			break;
		case R.id.ll_near:
			if (SpfsUtil.getIsLoadingCity() == true) {
				DialogUtils.successMessage(mContext, "初始化数据加载中\n请20秒后重新再试！");
				return;
			}
			// ToastUtil.show(this, "快递员位置");
			intent = new Intent(mContext, BaiduMapActivity.class);
			intent.putExtra(BaiduMapActivity.KEY_TYPE,
					BaiduMapActivity.TYPE_CARRIER);
			startActivity(intent);
			break;
		case R.id.ll_tools:
			ToolsActivity.actionStart(mContext);
			break;

		default:
			break;
		}
	}

	@Override
	public void onDestroy() {
		mContext.unregisterReceiver(newMsgBReceiver);
		super.onDestroy();
		Log.e("gongjie", "fragment ondestory");
//		stopPlay();
	}

	private class ViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return images.size();
		}

		// 是否是同一张图片
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup view, int position, Object object) {
			// TODO Auto-generated method stub
			// super.destroyItem(container, position, object);
			// view.removeViewAt(position);
			view.removeView(images.get(position));

		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			// TODO Auto-generated method stub
			view.addView(images.get(position));
			switch (position) {
			case 2:
				images.get(position).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						DialogUtils.getQrcodeDialog(getActivity()).show();
					}
				});
				break;
			}
			
			return images.get(position);
		}
	}
@Override
public void onStart() {
	// TODO Auto-generated method stub
	Log.e("gongjie", "fragment onstart");
	super.onStart();
}
@Override
public void onStop() {
	// TODO Auto-generated method stub
	super.onStop();
	Log.e("gongjie", "fragment onStop");
}

}
