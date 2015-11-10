package com.arvin.koalapush.adapter;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.filterchain.IoFilter.NextFilter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.arvin.koalapush.util.LogUtil;

public class ReConnAdapter extends IoFilterAdapter{
	//mina服务端口
	private IoSession session;
	// 创建连接客户端
	protected NioSocketConnector connector;
	public ReConnAdapter(IoSession session,NioSocketConnector connector ){
		this.session = session;
		this.connector = connector;
	}
	
	@Override
	public void sessionClosed(NextFilter nextFilter,
			IoSession ioSession) throws Exception {
		for (;;) {
			if (session != null && session.isConnected()) {
				break;
			}
			try {
				Thread.sleep(3000);
				ConnectFuture future = connector.connect();
				future.awaitUninterruptibly();// 等待连接创建成功

				session = future.getSession();// 获取会话
				if (session.isConnected()) {
					LogUtil.w("断线重连["
							+ connector
									.getDefaultRemoteAddress()
									.getHostName()
							+ ":"
							+ connector
									.getDefaultRemoteAddress()
									.getPort() + "]成功");
					break;
				}
			} catch (Exception ex) {
				LogUtil.w("重连服务器登录失败,3秒再连接一次:"
						+ ex.getMessage());
			}
		}
	}

}
