package com.tencent.midas.oversea.recharge.firebase

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tencent.midas.oversea.recharge.comm.MLog
import com.tencent.midas.oversea.recharge.data.DataHandler

class MFirebaseMessagingService : FirebaseMessagingService() {

    /**
     * There are two types of messages data messages and notification messages.
     * Data messages are handled ere in onMessageReceived whether the app is in the foreground or background.
     * Data messages are the type traditionally used with GCM.
     *
     * Notification messages are only received here in onMessageReceived when the app is in the foreground.
     * When the app is in the background an automatically generated notification is displayed.
     * When the user taps on the notification they are returned to the app.
     *
     * Messages containing both notification and data payloads are treated as notification messages.
     * The Firebase console always sends notification messages.
     * For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
     * @param remoteMessage
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        MLog.d(TAG, "From: " + remoteMessage!!.from!!)

        //check if message contains a data payload
        if (remoteMessage.data.size > 0) {
            DataHandler.handleData(remoteMessage.data)
        }

        //check if message contains a notification payload
        if (remoteMessage.notification != null) {
            MLog.d(TAG, "Messaging notification body: " + remoteMessage.notification!!.body!!)
        }
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String?) {
        MLog.d(TAG, "Refreshed token: " + token!!)
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

    companion object {
        val TAG = "MFirebaseMessagingService"
    }
}
