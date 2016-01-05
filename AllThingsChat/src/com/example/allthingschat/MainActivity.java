package com.example.allthingschat;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;


public class MainActivity extends Activity implements OnClickListener{
	LinearLayout ll_index,ll_release,ll_collect,ll_my;
	FragmentManager fragmentManager;
	Context context;
	Intent intent;
	Index_Activity index_fragment;
	My_Activity my_fragment;
	Collect_Activity collect_fragment;
	FragmentTransaction fragmentTransaction;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initview();
        
        
    }
       public void initview(){
    	   ll_index=(LinearLayout) this.findViewById(R.id.index);
    	   ll_release=(LinearLayout) this.findViewById(R.id.release);
    	   ll_collect=(LinearLayout) this.findViewById(R.id.collect);
    	   ll_my=(LinearLayout) this.findViewById(R.id.my);
    	   ll_index.setOnClickListener(this);
    	   ll_release.setOnClickListener(this);
    	   ll_collect.setOnClickListener(this);
    	   ll_my.setOnClickListener(this);
    	   ll_index.setSelected(true);
    	   
    	   fragmentManager = getFragmentManager();
    	   index_fragment = new Index_Activity();
    	   my_fragment = new My_Activity();
    	   collect_fragment = new Collect_Activity();
    	   fragmentTransaction = fragmentManager.beginTransaction();
    	   fragmentTransaction.replace(R.id.ll_content,index_fragment);
    	   fragmentTransaction.commit();
    	  
    	  
    	
       }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 fragmentTransaction = fragmentManager.beginTransaction();
		switch (v.getId()) {
		case R.id.index:
			ll_index.setSelected(true);
			ll_release.setSelected(false);
			ll_collect.setSelected(false);
			ll_my.setSelected(false);
			fragmentTransaction.replace(R.id.ll_content,index_fragment);
			break;
		case R.id.release:
			ll_index.setSelected(false);
			ll_release.setSelected(true);
			ll_collect.setSelected(false);
			ll_my.setSelected(false);
			break;
		case R.id.collect:
			ll_index.setSelected(false);
			ll_release.setSelected(false);
			ll_collect.setSelected(true);
			ll_my.setSelected(false);
			fragmentTransaction.replace(R.id.ll_content,collect_fragment);
			break;
		case R.id.my:
			ll_index.setSelected(false);
			ll_release.setSelected(false);
			ll_collect.setSelected(false);
			ll_my.setSelected(true);
	    	fragmentTransaction.replace(R.id.ll_content,my_fragment);
			Log.e("log", "到这了 ");
			break;

		default:
			break;
			
		}
		fragmentTransaction.commit();
	}
    
}