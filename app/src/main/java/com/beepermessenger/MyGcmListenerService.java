package com.beepermessenger;

/**
 * Class : 
 * Task : This class 
 * Author: playstore.apps.android@gmail.com
 */

import android.content.Intent;
import android.os.Bundle;

import com.beepermessenger.CommonFiles.AppNotification;
import com.beepermessenger.CommonFiles.CommonUtilities;
import com.google.android.gms.gcm.GcmListenerService;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        String title = data.getString("from_user_name");
        String type = data.getString("type");
        String fromImage = data.getString("from_user_image");
        String time = data.getString("time");
        System.out.println("================" + type);
        long fromId;
        long messageId;
        try {
            fromId = Long.parseLong(data.getString("from_user_id"));
        }catch (Exception e)
        {
            fromId = 0;
        }
        long toId;
        try {
            toId = Long.parseLong(data.getString("to_user_id"));
        }catch (Exception e)
        {
            toId = 0;
        }
        try {
            messageId = Long.parseLong(data.getString("message_id"));
        }catch (Exception e)
        {
            messageId = 0;
        }

        /*
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);
        Log.d(TAG, "GCM from_user_id: " + data.getString("from_user_id"));
        Log.d(TAG, "Message: to_user_id" + data.getString("to_user_id"));
        Log.d(TAG, "Message: type" + data.getString("type"));
        */
        if (type.equalsIgnoreCase("chat")) {
            if (BaseActivity.activity == null) {
                AppNotification.genrateNotification(MyGcmListenerService.this, new Intent(this, BaseActivity.class).putExtra("type","chat").putExtra("id",fromId), title, message, toId);
            } else {
                BaseActivity.activity.gotMessage(messageId,fromId, toId,title,message,fromImage,time);
            }
        } else {
            AppNotification.genrateNotification(MyGcmListenerService.this, new Intent(this, BaseActivity.class), title, message,messageId);
        }
        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        // [END_EXCLUDE]
    }
    // [END receive_message]


}
