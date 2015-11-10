package com.ems.express.fragment.message;

import java.util.List;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.adapter.message.MailDeliverAdapter;
import com.ems.express.bean.DeliveryMessageBean;
import com.ems.express.ui.message.MessageActivity;
import com.ems.express.util.DialogUtils;

public class DeliverFragment extends Fragment{

	private View mview;
	private ListView deliverlist;
	private MailDeliverAdapter adapter;
	private Dialog dialog;
	private List<DeliveryMessageBean> dmList;
	private ImageView imgview;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mview=inflater.inflate(R.layout.message_deliver_fragment,null);
		
		deliverlist =(ListView)mview.findViewById(R.id.list_deliver);
		imgview	 =(ImageView)mview.findViewById(R.id.img_view1);
		dmList = App.dbHelper.queryAllDeliveryMessage(App.db);
		adapter  =new MailDeliverAdapter(getActivity(), dmList);
		deliverlist.setAdapter(adapter);
		int count = deliverlist.getCount();
		if(count==0){
			imgview.setVisibility(View.VISIBLE);
		}else{
			imgview.setVisibility(View.GONE);
		}
		deliverlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.setReddot(position);
				adapter.notifyDataSetChanged();
				MessageActivity.setPromptMessage();
				if(dmList.get(position).getMobile()==null||"".equals(dmList.get(position).getMobile())){
					return;
				}else {
					DialogUtils.getMessageDialog(getActivity(),dmList ,position);
				}
			}
		});
		
		deliverlist.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				dialog = new Dialog(getActivity(),R.style.DialogStyle2);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				View myview = LayoutInflater.from(getActivity()).inflate(
						R.layout.dialog_message_itme, null);
				dialog.setContentView(myview);
				
				Button buttonRemove = (Button) myview.findViewById(R.id.message_dialog__fragment1);
				Button buttonCancel = (Button) myview.findViewById(R.id.message_dialog_fragment2);
				
				buttonRemove.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						int delId = dmList.get(position).getDelId();
						App.dbHelper.deleteDeliveryMessageIsDelId(App.db,""+delId);
						dmList.remove(position);
						adapter.notifyDataSetChanged();
						MessageActivity.setPromptMessage();
						dialog.dismiss();
					}
				});
				buttonCancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						MessageActivity.setPromptMessage();
						adapter.setReddot(position);
						adapter.notifyDataSetChanged();
						dialog.dismiss();
					}
				});
				dialog.show();
				return true;
			}
		});
		return mview;
	}
}
