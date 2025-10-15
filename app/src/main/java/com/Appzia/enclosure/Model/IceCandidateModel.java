package com.Appzia.enclosure.Model;

import org.webrtc.IceCandidate;

public class IceCandidateModel {
    public String sdp;
    public String sdpMid;
    public int sdpMLineIndex;

    public IceCandidateModel() {
        // Required for Firebase
    }

    public IceCandidateModel(IceCandidate candidate) {
        this.sdp = candidate.sdp;
        this.sdpMid = candidate.sdpMid;
        this.sdpMLineIndex = candidate.sdpMLineIndex;
    }

    public IceCandidate toIceCandidate() {
        return new IceCandidate(sdpMid, sdpMLineIndex, sdp);
    }
}
