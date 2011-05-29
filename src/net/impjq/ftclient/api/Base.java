package net.impjq.ftclient.api;

public abstract class Base implements Runnable {
	protected String mServerURL="http://216.24.194.197:8000/HelloServlet";
	
	
	protected String mCommand;
	
	/**
	 * Service type,twitter of facebook
	 */
	protected String mServiceType;

	protected String mUserName;
	protected String mPassword;
	protected String mMessage;
	
	public void setUserName(String userName){
		mUserName=userName;
	}
	
	public void setPassword(String password){
		mPassword=password;
	}
	
	public void setMessage(String message){
		mMessage=message;
	}
	
	
	private String createUrl(){
		String url="";
		
		url=mServerURL+"/"+mServiceType+"/"+mCommand;
		
		
		return url;
	}
	
	protected void execute(){
		
		
	}
	
	

}
