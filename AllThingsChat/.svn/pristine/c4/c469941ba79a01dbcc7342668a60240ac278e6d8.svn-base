package com.example.allthingschat;

import com.example.allthingschat.util.DialogUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Pensonl_activity extends Activity implements OnClickListener{
	Button add_friend;
	Context context;
@Override

protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.personol_activity);
	add_friend = (Button) this.findViewById(R.id.add_firend);
	add_friend.setOnClickListener(this);
}
@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	switch (v.getId()) {
	case R.id.add_firend:
		DialogUtils.add_friend(context);
		break;

	default:
		break;
	}
}
}
