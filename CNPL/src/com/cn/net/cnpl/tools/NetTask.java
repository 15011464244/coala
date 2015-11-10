package com.cn.net.cnpl.tools;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.cn.net.cnpl.Global;
import com.cn.net.cnpl.model.Head;

public class NetTask extends AsyncTask<Object, integer, Head> {

	private Context mContext = null;
	private boolean mClose =true;

	public ProgressDialog myDialog = null;

	public NetTask(Context context,boolean bClose) {
		mContext = context;
		mClose =bClose;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Head doInBackground(Object... params) {

		List<JSONObject> rList = null;
		Head head = null;
		try {
			NetHelper client = new NetHelper();
			client.Create(params[0].toString());
			head = new Head();
			rList = client.execute((List<NameValuePair>) params[1], head);
			if (head != null) {
				head.setrList(rList);
			}

		} catch (Exception ex) {
			head.setRet("1");
			head.setErrorMsg(ex.getMessage());
		}

		return head;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(Head head) {
		myDialog.dismiss();
			try{
				Toast.makeText(mContext, head.getErrorMsg(), Toast.LENGTH_LONG).show();
				if (mClose && "0".equals(head.getRet())) {
					((Activity) mContext).finish();
				}
			}catch (Exception e) {
//				Toast.makeText(mContext, "连接服务器失败", Toast.LENGTH_LONG).show();
		 	}
		super.onPostExecute(head);
	}

	@Override
	protected void onPreExecute() {
		myDialog = new ProgressDialog(mContext, 0);
		myDialog = ProgressDialog.show(mContext,  Global.DIALOG_NAME, "正在处理中...", true,
				true);
		super.onPreExecute();
	}
}
