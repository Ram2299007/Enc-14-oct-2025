package com.Appzia.enclosure.models;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.Appzia.enclosure.activities.CallActivity;


public class InterfaceJava {

    CallActivity callActivity;

    public InterfaceJava(CallActivity callActivity) {
        this.callActivity = callActivity;
    }

    @JavascriptInterface
    public void onPeerConnected(){
        callActivity.onPeerConnected();
    }



}