package com.ems.express.ui;


import com.ems.express.R;
import com.ems.express.util.PreUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Welcome extends Activity {


	boolean isFirstIn;;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		isFirstIn = PreUtils.loadBoolean("first", true);
		if (isFirstIn) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					goGuide();
				}
			}, 2000);
		} else {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					goHome();
				}
			}, 2000);
		}
	}

	private void goHome() {
		Intent intent = new Intent(Welcome.this, HomeActivity.class);
		Welcome.this.startActivity(intent);
		Welcome.this.finish();
	}

	private void goGuide() {
		Intent intent = new Intent(Welcome.this, GuideViewPager.class);
		Welcome.this.startActivity(intent);
		Welcome.this.finish();
		PreUtils.saveBoolean("first", false);
	}

    @Override  
    public void onBackPressed() {  
        Intent intent= new Intent(Intent.ACTION_MAIN);  
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        intent.addCategory(Intent.CATEGORY_HOME);  
        startActivity(intent);  
    }  
	
}
