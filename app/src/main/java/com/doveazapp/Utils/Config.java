package com.doveazapp.Utils;

public interface Config {

	// used to share GCM regId with application server - using php app server
	//static final String APP_SERVER_URL = "http://192.168.1.17/gcm/gcm.php?shareRegId=1";
	static final String APP_SERVER_URL = "http://doveaz.co.in/api/Notification/send";

	// GCM server using java
	// static final String APP_SERVER_URL =
	// "http://192.168.1.17:8080/GCM-App-Server/GCMNotification?shareRegId=1";

	// Google Project Number
	static final String GOOGLE_PROJECT_ID = "938763971544";
	static final String MESSAGE_KEY = "message";
	static final String REGISTER_NAME = "name";
	static final String TO_USER = "user";

}
