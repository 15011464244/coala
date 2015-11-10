package com.cn.net.cnpl.tools;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class MySimpleAdapter extends SimpleAdapter{

    public MySimpleAdapter(Context context, List<? extends Map<String, ?>> data,
            int resource, String[] from, int[] to) {
        super(context,data,resource,from,to);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	View temp = super.getView(position,convertView,parent);
    	if(position % 2 == 0){
    		temp.setBackgroundColor(Color.parseColor("#FFF0D9"));
    	}else{
    		temp.setBackgroundColor(Color.parseColor("#FFFfff"));
    	}
    	return temp;
    }
}
