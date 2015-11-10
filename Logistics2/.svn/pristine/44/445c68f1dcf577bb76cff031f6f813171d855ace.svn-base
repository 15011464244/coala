package com.ems.express.fragment.message;

import java.util.List;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.adapter.message.MailMeassageAdapter;
import com.ems.express.adapter.message.SendNoticeBean;

public class MessageFragment extends Fragment{

	private View mview;
	private ListView listmessage;
	private MailMeassageAdapter adapter;
	private Dialog dialog;
	private List<SendNoticeBean> messageListData;
	private ImageView imgview;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mview=inflater.inflate(R.layout.message_track_fragment, null);
		  
		  messageListData = App.dbHelper.querySendNotice(App.db);
		  
		  listmessage =(ListView)mview.findViewById(R.id.list_message);
		  imgview  =(ImageView)mview.findViewById(R.id.img_view2);
		  
			adapter = new MailMeassageAdapter(getActivity(), messageListData);
			listmessage.setAdapter(adapter);
			
			int count = listmessage.getCount();
			if(count==0){
				imgview.setVisibility(View.VISIBLE);
			}else{
				imgview.setVisibility(View.GONE);
			}
			
			listmessage.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View view, final int position, long id) {
					
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
							int sendId = messageListData.get(position).getSendId();
							App.dbHelper.deleteSendNotice(App.db, ""+sendId);
							messageListData.remove(position);
							adapter.notifyDataSetChanged();
//							listmessage.setAdapter(adapter);
							dialog.dismiss();
						}
					});
					
					buttonCancel.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
					dialog.show();
					return false;
				}
			});
		return mview;
	}
	
}
