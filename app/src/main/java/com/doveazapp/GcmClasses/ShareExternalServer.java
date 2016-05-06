package com.doveazapp.GcmClasses;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.doveazapp.Constants;
import com.doveazapp.Utils.Config;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ShareExternalServer extends AppCompatActivity {

    /*public String shareRegIdWithAppServer(final String regId,
                                          final String userName) {
        String result = "";
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("action", "shareRegId");
        paramsMap.put("regId", regId);
        paramsMap.put(Config.REGISTER_NAME, userName);
        result = request(paramsMap);
        if ("success".equalsIgnoreCase(result)) {
            result = "RegId shared with GCM application server successfully. Regid: "
                    + regId + ". Username: " + userName;
        }
        Log.d("ShareExternalServer", "Result: " + result);
        return result;
    }*/

    public String sendMessage(final String toUserId, final String messageToSend, final String api_token) {

        String result = "";
        Map<String, String> paramsMap = new HashMap<String, String>();
        //paramsMap.put("action", "sendMessage");
        paramsMap.put(Constants.USER_IDS, toUserId);
        paramsMap.put(Constants.MESSAGE, messageToSend);
        result = request(paramsMap, api_token);
        if ("success".equalsIgnoreCase(result)) {
            result = "Message " + messageToSend + " sent to user "
                    + " successfully.";
        }
        Log.d("ShareExternalServer", "Result: " + result);
        return result;
    }

    public String request(Map<String, String> paramsMap, final String api_token) {
        String result = "";
        URL serverUrl = null;
        OutputStream out = null;
        HttpURLConnection httpCon = null;
        try {
            serverUrl = new URL(Config.APP_SERVER_URL);
            StringBuilder postBody = new StringBuilder();
            Iterator<Entry<String, String>> iterator = paramsMap.entrySet()
                    .iterator();
            while (iterator.hasNext()) {
                Entry<String, String> param = iterator.next();
                postBody.append(param.getKey()).append('=')
                        .append(param.getValue());
                if (iterator.hasNext()) {
                    postBody.append('&');
                }
            }
            String body = postBody.toString();
            byte[] bytes = body.getBytes();
            httpCon = (HttpURLConnection) serverUrl.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setUseCaches(false);
            httpCon.setFixedLengthStreamingMode(bytes.length);
            httpCon.setRequestMethod("POST");
            httpCon.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            httpCon.setRequestProperty(Constants.KEY_API_TOKEN, api_token);
            Log.d("ShareExternalServer", "Just before getting output stream.");
            out = httpCon.getOutputStream();
            out.write(bytes);
            int status = httpCon.getResponseCode();
            Log.d("ShareExternalServer", "HTTP Connection Status: " + status);
            if (status == 200) {
                result = "success";
            } else {
                result = "Post Failure." + " Status: " + status;
            }

        } catch (MalformedURLException e) {
            Log.e("ShareExternalServer", "Unable to Connect. Invalid URL: "
                    + Config.APP_SERVER_URL, e);
            result = "Invalid URL: " + Config.APP_SERVER_URL;
        } catch (IOException e) {
            Log.e("ShareExternalServer",
                    "Unable to Connect. Communication Error: " + e);
            result = "Unable to Connect GCM App Server.";
        } finally {
            if (httpCon != null) {
                httpCon.disconnect();
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
        }
        return result;
    }

}