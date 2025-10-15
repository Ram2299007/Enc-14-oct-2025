package com.Appzia.enclosure.models;

import android.webkit.JavascriptInterface;

import com.Appzia.enclosure.activities.CallVoiceActivity;


public class InterfaceVoiceJava {

    CallVoiceActivity callActivity;

    public InterfaceVoiceJava(CallVoiceActivity callActivity) {
        this.callActivity = callActivity;
    }

    @JavascriptInterface
    public void onPeerConnected(){
        callActivity.onPeerConnected();
    }


//    @JavascriptInterface
//    public void onCallConnected(){
//        // Notify the activity that the call is connected
//        callActivity.onCallConnected();
//    }
}
