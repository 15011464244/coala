package com.alipay.sdk.pay.demo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

public class PayDemoActivity extends FragmentActivity {

	//商户PID
//	public static final String PARTNER = "2088021282301748";
//	public static final String PARTNER = "2088021605626202";
	public static final String PARTNER = "2088121200952305";
	//商户收款账号
//	public static final String SELLER = "pingandaijia126@126.com";
//	public static final String SELLER = "jxshjggj2@126.com";
	public static final String SELLER = "jxyzxbj@163.com";
	//商户私钥，pkcs8格式
	public static final String RSA_PRIVA？TE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJn2KKkgxGDWq757jyHtJYiPQYtki8S1HAKJdXhkMerPFqSB4i5F0KiKH5EDSVGgBRw7oSiiPtnD8AEPNQMwDzyMs1oFmx/AeYKQScalMilUPQdOC7OMprM8zorfhpOVQ7RkYIVvuQD2MmCXpIf1PAqe/LGe6W3MS7qk0PRy4SHPAgMBAAECgYAjS3smyow6ZvwYPtshO+xO0giEnBgukBZLvpdfchi/a5oVPHFNilO7T27NH6O/Qp/pSQI4/njKE1EB7SqKAIp9S/2D3g8S+lE3xAkSyl3wdqEf33vKlzaJgcnL9KeniiLNFBU+KSX7SqqNH1Xhs4ib6/J9adJNB4so8VeGyFhwAQJBAMcZuTlsKZlRiJkz9jaScJ3Gp7Kbkd3AAQ+ayuSLXhXB/nKGIdWC5kmK0Hy9jDI6hv4jphz4AYOusG8RHqdoA08CQQDF9g1hQx5XnOY/C5xNE/NAHRzNZzxZjHFr2dIAGYkqRRBXO2hDt4crmcx16N/LAldicUkvV0UhNxFboxIMX1mBAkB/izQD3A1eAUQvWIEufmsUN5FwMoaj9n73fyLge4M/DvIwbUq5W0yo6fsbHdX0y1d08GNWhW167OprjB0GAvSzAkAWAUzpc+GKkalSdsLwGniett29w20E80SkXXkng68ooLa5S6RCasM+yIDe1n0R/vehvMAK4COSFqH6Ur0t3OeBAkA+F+JWtAN0LcND8qguPtgHOXQHjNWO4Y+MjCdWF+YHfyRo/Dw6FkTrSF90qiX1CI2tVPkjTx05vlX5nW6D/Qa4";
//	public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAM/UHFG6RWgwHHGbWCpVFUjCfA9g40iXn16s3tAuLdraW1uL9qj/KpNzZPSiKq7Jh2UOo/xed9/NZ15jJTbLnB7RR0Jcs6sQbmPI0gaQeNgaOEoY3kGyzJZM8V7vY+5Ft1HseTvT5C7r3o6GQCtWkjSd3pcsXUI1TD/vgQtM0qtJAgMBAAECgYEAg9DV0WRip010jP3pcpgactaf1yUtoxpAA82DWOkhPmEmdq8UWGXXPESBcMP2bHv3+znI+sV0JKfj+zLJJLMJpkGdi6cX9SAgoFpQRbE8ciSlc1SEd0JzP+i7Zot52Ft+JwoRzQI9ntb/55m6AZbbzQYKLYFVsJdg/z4KhxYR5oECQQDoS4Ba198j3vAgLD5JLuAtsiUmWK/5vL7uzf9mYYP4WY6tkt3vvK3dlKZxUfFWVAqwTvcwMS0jYJfG2mOeEdT5AkEA5QlyceBBEAevjYVLhJYAdyv/xgBk6ZiAVrvE8IEoJJ3whfj9g1Km1ff6u5BKqUEmyCbTGW/yIY9iB3HMzDEs0QJAYW1hPTizJxlW/zdvPESD08aW3DlwPT9TmVce2nLl5eCx/czOdsFc7sVs9bRXk0yWWxITjMvjUaYuokFwbJiwCQJAUZ7TPQe4x/ilNB7Lcb1u/wkcBji/vYtOORqDLV4pqxrpyySXW4RJ1jigDtV19ek04+EKG6eMNy98tMB2RlJD8QJAIQku1lS741XBRRhlfQNguTJm3/d4exVkhK7Dn0vsLeERVrF3mWx0n6AWWkmQMBH8aswIh6gAWESqpcX4A7fzbg==";
	public static final String RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALiJHYLArvwB2nSzo9e0QSJUV1xn4h9BCRdYg+blSOt2JfNcbP4r+pznNSPyRuxGGh/ThDiUH7P1vbdYbhx2b29CJW3MCrDE2+ShsT54R04/TW8Ov1TsZNhdibrxbdlD1ESsejlxrAJyAtpJhc5pAm8y75vEtL+X8P03bKmaW8ulAgMBAAECgYBoTniz054VLCcDGBdgca7vbKUEwmN6LxSocYv9E1UoYtwItxGUid9amQXcj7GvE+DoZHK0WGWjHyBhZka3J+rMDqLY9aMX/VicC2m1OKDuVovgMLzuh0W03OOCmSDQGM8CxcnJWppFU7LnHU3DnIe4+zGaR7s+32wsKUKABtsvAQJBAOQpsumEzIwaRiM/NHe6UwgmKPOMjXDfXweIjCgWcGhh/H7y8+XUPVz4hPDyEf7j/OzGEc4iQx2oPlBnUgp8MHUCQQDPDMnWFVitg/A7yVu5zENIqeUTZTRr37oOthSv8RhZpFiQg29sI59H5sr+CnScPWxr2WRqPy4z1EoaB/WPE8hxAkBhZm+rAnbzhC/W0h+K0O6rNucaeTDUM3b011TFtDaOSO4qH3sA7Oa5LppJ5XzLK/aH+VIWhkfSfEUFu+Wo9jUVAkAZvEsyMwEMwEOSCts94ufSFlwE5yV26x4jSfqUS/Fs4wKnI88rr9nShBtjLEWGThQ165WRpzzE8HQtfCPHnvvhAkBefmGNrQ+r2JEUxAaKsKPTH2Td/yYUGcxnG1o3cii+hBWDKyF9n+TjVfY/hYgoQWK4YOfTWr7AAyzd7iex7NWY";
	//支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";


	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				
				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();
				
				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(PayDemoActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(PayDemoActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(PayDemoActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(PayDemoActivity.this, "检查结果为：" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_main);
	}

	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void pay(View v) {
		// 订单
		String orderInfo = getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01");

		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(PayDemoActivity.this);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	public void check(View v) {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask payTask = new PayTask(PayDemoActivity.this);
				// 调用查询接口，获取查询结果
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
				+ "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";
		Log.e("gongjie", orderInfo);
		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

}
