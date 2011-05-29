package net.impjq.ftclient.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import net.impjq.httpclient.HttpClientHelper;

public abstract class BaseTask implements Runnable {
	protected String mServerURL = "http://216.24.194.197:8090/HelloServlet";

	// Twitter
	public static final int TWITTER_API_UPDATE_MESSAGE = 10;
	public static final int TWITTER_API_GET_TIMELINE = TWITTER_API_UPDATE_MESSAGE + 1;

	// Facebook
	public static final int FACEBOOK_API_UPDATE_MESSAGE = TWITTER_API_GET_TIMELINE + 10;
	public static final int FACEBOOK_API_GET_TIMELINE = FACEBOOK_API_UPDATE_MESSAGE + 1;

	protected String mCommand;

	protected int mCommandId;
	
	protected String mResponse;

	/**
	 * Service type,twitter of facebook
	 */
	protected String mServiceType;

	protected String mUserName;
	protected String mPassword;
	protected String mMessage;

	public int getCommandId() {
		return mCommandId;
	}

	public void setUserName(String userName) {
		mUserName = userName;
	}

	protected String getUserName() {
		return mUserName;
	}

	public void setPassword(String password) {
		mPassword = password;
	}

	protected String getPassword() {
		return mPassword;
	}

	public void setMessage(String message) {
		mMessage = message;
	}

	protected String getMessage() {
		return mMessage;
	}
	
	public String getResponse(){
		return mResponse;
	}

	private String createUrl() {
		String url = "";

		url = mServerURL + "/" + mServiceType + "/" + mCommand;

		return url;
	}

	/**
	 * Create the HttpClient,it support https and http.
	 * 
	 * @return
	 */
	private HttpClient createHttpClient() {
		return HttpClientHelper.getInstance().createNewDefaultHttpClient();
	}

	protected void execute() {

	}

	protected UrlEncodedFormEntity createUrlEncodedFormEntity(
			HashMap<String, String> hashMap) {
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		Iterator<Entry<String, String>> iterator = hashMap.entrySet()
				.iterator();

		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			String key = entry.getKey();
			String value = entry.getValue();
			formparams.add(new BasicNameValuePair(key, value));
		}

		UrlEncodedFormEntity urlEncodedFormEntity = null;
		try {
			urlEncodedFormEntity = new UrlEncodedFormEntity(formparams,
					HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return urlEncodedFormEntity;
	}

	/**
	 * Create the HttpClient.
	 * 
	 * @return
	 */
	// protected HttpClient createHttpClient() {
	// HttpClientHelper helper = HttpClientHelper.getInstance();
	// HttpClient httpClient = helper.createNewDefaultHttpClient();
	// return httpClient;
	// }

	/**
	 * Create the HttpPost
	 * 
	 * @return
	 */
	protected HttpPost createHttpPost() {

		HttpPost httpPost = null;

		httpPost = new HttpPost(createUrl());
		httpPost.addHeader("Accept", "text/plain");
		httpPost.addHeader("Accept-Charset", "UTF-8,*;q=0.5");

		return httpPost;
	}

	/**
	 * Execute the HttpPost.
	 * 
	 * @param httpPost
	 * @return InputStream
	 */
	protected InputStream executeHttpPost(HttpPost httpPost) {
		InputStream inputStream = null;
		try {
			HttpResponse response;
			response = createHttpClient().execute(httpPost);
			System.out.println(response.getStatusLine());
			inputStream = response.getEntity().getContent();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return inputStream;
	}

	/**
	 * Execute the request just according to the HashMap parameters. Some
	 * request doesn't need to setEntity,such as {@link SignOutTask},
	 * {@link KeepAliveTask},so you may can't use it,so pass false to it.
	 * 
	 * @param hashMap
	 * @param enableHttpEntity
	 *            if need the HttpEntity,set to true.
	 * @return InputStream
	 */
	protected InputStream executeRequest(HashMap<String, String> hashMap,
			boolean enableHttpEntity) {
		HttpPost httpPost = createHttpPost();

		HttpEntity httpEntity = createUrlEncodedFormEntity(hashMap);
		httpPost.setEntity(httpEntity);

		return executeHttpPost(httpPost);
	}

}