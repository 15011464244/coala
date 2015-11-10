package com.ems.express.fragment.message;

import java.util.ArrayList;

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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.ems.express.R;
import com.ems.express.adapter.message.MailNewsAdapter;

public class NewsFragment extends Fragment{

	
	private View mview;
	private ListView listmessage;
	private ArrayList<String> messageListData;
	private ArrayList<String> myList;
	private MailNewsAdapter adapter;
	private Dialog dialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			mview=inflater.inflate(R.layout.message_nows_fragment, null);
		
			listmessage =(ListView)mview.findViewById(R.id.list_news);
			
			
				
				 messageListData = new ArrayList<String>();
					for (int i = 0; i < 10; i++) {
						String item;
							item = String.valueOf(i)+"00000000000";
							messageListData.add(item);			
						}
					
			
					myList = new ArrayList<String>();
					for (int i = 0; i < 10; i++) {
					
					myList.add("从上海出来到达深圳揽收部");			
				}
			
		adapter =new MailNewsAdapter(getActivity(),messageListData,myList);
			
				listmessage.setAdapter(adapter);
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
								messageListData.remove(position);
								myList.remove(position);
								adapter.notifyDataSetChanged();
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
