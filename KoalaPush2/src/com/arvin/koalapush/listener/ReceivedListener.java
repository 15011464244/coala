package com.arvin.koalapush.listener;

public abstract interface ReceivedListener {
	public abstract String onReceived(Object paramObject);

	public abstract void onError(Object paramObject);
}
