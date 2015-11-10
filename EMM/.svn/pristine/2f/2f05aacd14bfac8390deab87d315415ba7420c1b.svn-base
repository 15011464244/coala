package com.koala.emm.supervision;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.koala.emm.R;
import com.koala.emm.basic.BaseActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class TrafficActivity extends BaseActivity {
	@ViewInject(R.id.tv_traffic)
	TextView tv_traffic;
	private long total;
	private String traffic;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_traffic);
		ViewUtils.inject(this); 
		total=TrafficMonitoring.traffic_Monitoring();
		traffic=TrafficMonitoring.convertTraffic(total);
		tv_traffic.setText(traffic);
		Intent intent = new Intent(TrafficActivity.this,
				TrafficService.class);
		startService(intent);
	}
}
