package com.koala.emm.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.mapapi.map.Text;
import com.koala.emm.R;
import com.koala.emm.R.id;
import com.koala.emm.R.layout;
import com.koala.emm.app.EmmApplication;
import com.koala.emm.basic.BaseActivity;
import com.koala.emm.model.WarnPushMessage;
import com.koala.emm.model.WarnType;
import com.koala.emm.util.DialogUtils;

import android.R.bool;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MessageListActivity extends BaseActivity implements OnClickListener{
	private ListView listView;
	private List<WarnPushMessage> messageList;
	public Map<String, String> typeSelect;
	private MessageAdapter adapter;
	String[] types ;
	
	private TextView delete;
	private boolean llDeleteShow = false;
	
	private LinearLayout llDelete;
	private CheckBox cbSelectAll;
	private TextView tvDeleteBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_list);
		
		initView();
		
		Intent intent = getIntent();
		types = intent.getStringArrayExtra("messageTypes");
		messageList = EmmApplication.dbHelper.queryMessage(EmmApplication.db,
				types);
		
		adapter = new MessageAdapter(this,messageList);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterview, View view,
					int i, long l) {
				Intent detail =  new Intent(currentContext, MessageDetailActivity.class);
				detail.putExtra("title", messageList.get(i).getTitle());
				detail.putExtra("content", messageList.get(i).getText());
				detail.putExtra("receiveTime", messageList.get(i).getReceiveTime().replace(" ", "\n"));
				currentContext.startActivity(detail);
				
			}
		});

	}
	
	public void initView(){
		listView = (ListView) this.findViewById(R.id.lv_message_list);
		
		llDelete = (LinearLayout) findViewById(R.id.ll_delete);
		cbSelectAll = (CheckBox) findViewById(R.id.cb_select_all);
		tvDeleteBtn = (TextView) findViewById(R.id.tv_delete);
		
		llDelete.setOnClickListener(this);
		cbSelectAll.setOnClickListener(this);
		tvDeleteBtn.setOnClickListener(this);
		
		//初始化选择器
		typeSelect = new HashMap<String,String>();
		typeSelect.put(WarnType.APPLICATION_DELETE, "卸载软件");
		typeSelect.put(WarnType.APPLICATION_NOT_UPDATE_FORCED, "应用非强制更新");
		typeSelect.put(WarnType.APPLICATION_UPDATE_FORCED, "应用强制更新");
		typeSelect.put(WarnType.DATA_DECRYPTION, "数据解密");
		typeSelect.put(WarnType.DATA_ENCRYPTION, "数据加密");
		typeSelect.put(WarnType.DATA_FORCED_UPDATE, "数据强制更新");
		typeSelect.put(WarnType.NOT_DATA_FORCED_UPDATE, "数据非强制更新");
		typeSelect.put(WarnType.MEMORY_CLEANUP, "清理内存");
		typeSelect.put(WarnType.POLICY_UPDATE, "策略更新");
		typeSelect.put(WarnType.RECEIVE_MESSAGE, "消息通讯");
		
		setHeadTitle("消息");
		
		delete = (TextView) findViewById(R.id.tv_other_function);
		delete.setVisibility(View.VISIBLE);
		delete.setText("清除");
		delete.setOnClickListener(this);
		
		
	}

	public class MessageAdapter extends BaseAdapter{
		private boolean showDelete = false;
		private Context mContext;
		private List<WarnPushMessage> mList;
		private LayoutInflater mInflater;
		
		private boolean selectAll = false;
		
		private Map<Integer,Integer> checked;

		public MessageAdapter(Context mContext,
				List<WarnPushMessage> messageList) {
			this.mContext = mContext;
			this.mList = messageList;
			this.mInflater = LayoutInflater.from(mContext);
			checked = new HashMap<>();
		}

		@Override
		public int getCount() {
			if (mList != null && mList.size() > 0) {
				return mList.size();
			}else{
				return 0;
			}
		}

		@Override
		public Object getItem(int i) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int i) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewgroup) {
			final int  index = i;
			ViewHolder holder = null;
			if (view == null) {
				holder = new ViewHolder();
				view = mInflater.inflate(R.layout.message_list_item, null);
				holder.cbSelect = (CheckBox) view.findViewById(R.id.cb_select);
				holder.messageTitle = (TextView) view.findViewById(R.id.tv_message_title);
				holder.messageContent = (TextView) view.findViewById(R.id.tv_message_text);
				holder.receiveTime = (TextView) view.findViewById(R.id.tv_receive_time);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			if (messageList != null) {
				holder.messageTitle.setText(mList.get(index).getTitle());
				holder.messageContent.setText(mList.get(index).getText());
				holder.receiveTime.setText(mList.get(index).getReceiveTime().replace(" ", "\n"));
				if(showDelete){
					holder.cbSelect.setVisibility(View.VISIBLE);
				}else{
					holder.cbSelect.setVisibility(View.GONE);
				}
				System.out.println(index +"     " +checked.get(index));
				
				if(selectAll){
					checked.put(index, mList.get(index).getId());
					holder.cbSelect.setChecked(true);
				}else{
					if(checked.get(index) != null){
						holder.cbSelect.setChecked(true);
					}else{
						holder.cbSelect.setChecked(false);
					}
				}
				holder.cbSelect.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View view) {
						if(((CheckBox)view).isChecked() == true){
							checked.put(index, mList.get(index).getId());
						}else{
							checked.remove(index);
							selectAll = false;
						}
					}
				});
			}
			return view;

		}
		
		public boolean isShowDelete(){
			return showDelete;
		}
		public void setShowDelete(boolean isShow){
			showDelete = isShow;
		}
		public void setSelectAll(boolean selectAll){
			this.selectAll = selectAll;
		}
		
		public void notifyList(List<WarnPushMessage> list){
			this.mList = list;
			notifyDataSetChanged();
		}

	}

	class ViewHolder {
		CheckBox cbSelect;
		TextView messageTitle;
		TextView messageContent;
		TextView receiveTime;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_other_function:
			adapter.checked.clear();
			cbSelectAll.setChecked(false);
			adapter.setSelectAll(false);
			
			llDelete.setVisibility(llDeleteShow ? View.GONE:View.VISIBLE);
			adapter.setShowDelete(!llDeleteShow);
			adapter.notifyList(messageList);
			llDeleteShow = !llDeleteShow;
			break;
			
		case R.id.tv_delete:
			if(adapter.checked == null || adapter.checked.size() < 1){
				DialogUtils.noticeDialog(this, "至少选择一项\n删除信息", "知道了");
				return;
			}
			List<Integer> ids = new ArrayList<Integer>();
			if(cbSelectAll.isChecked()){
				for (WarnPushMessage wpm : messageList) {
					ids.add(wpm.getId());
				}
			}else{
				//获取选中的keys
				for (Integer key : adapter.checked.keySet()) {
					ids.add(adapter.checked.get(key));
				}
			}
			
			//删除
			EmmApplication.dbHelper.removeMessageByIds(EmmApplication.db, ids);
			adapter.checked.clear();
			adapter.setSelectAll(false);
			//刷新
			messageList = EmmApplication.dbHelper.queryMessage(EmmApplication.db,types);
			llDelete.setVisibility(llDeleteShow ? View.GONE:View.VISIBLE);
			adapter.setShowDelete(!llDeleteShow);
			llDeleteShow = !llDeleteShow;
			adapter.notifyList(messageList);
			break;
		case R.id.cb_select_all:
			if(((CheckBox)v).isChecked()){
				adapter.checked.clear();
				adapter.setSelectAll(true);
				adapter.notifyList(messageList);
			}else{
				adapter.checked.clear();
				adapter.setSelectAll(false);
				adapter.notifyList(messageList);
			}

		default:
			break;
		}
		
	}

}
