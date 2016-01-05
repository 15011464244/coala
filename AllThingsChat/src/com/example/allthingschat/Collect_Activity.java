package com.example.allthingschat;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Collect_Activity extends Fragment{
	View v;
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	v = inflater.inflate(R.layout.collect_listview_items, container,false);
	
	

	return v;
}
}
