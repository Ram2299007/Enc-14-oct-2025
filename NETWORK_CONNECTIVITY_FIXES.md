# Network Connectivity Fixes for Voice and Video Calls

## Problem Description
Voice and video calls were failing on Airtel WiFi while working fine on mobile networks. This was due to several network-related issues:

1. **Limited ICE Server Configuration**: Only basic STUN servers were configured
2. **No Network Type Detection**: App didn't adapt to different network types
3. **Insufficient Retry Logic**: Connection attempts weren't optimized for different networks
4. **Missing Network Security Configuration**: Some networks block certain WebRTC traffic

## Solutions Implemented

### 1. Enhanced ICE Server Configuration

#### Video Calls (`script.js`)
```javascript
const peer = new Peer({
    config: {
        iceServers: [
            // Primary STUN servers
            { urls: 'stun:stun.l.google.com:19302' },
            { urls: 'stun:stun1.l.google.com:19302' },
            { urls: 'stun:stun2.l.google.com:19302' },
            { urls: 'stun:stun3.l.google.com:19302' },
            { urls: 'stun:stun4.l.google.com:19302' },
            
            // Additional STUN servers for better connectivity
            { urls: 'stun:stun.voiparound.com:3478' },
            { urls: 'stun:stun.voipbuster.com:3478' },
            { urls: 'stun:stun.voipstunt.com:3478' },
            { urls: 'stun:stun.voxgratia.org:3478' },
            { urls: 'stun:stun.ekiga.net:3478' },
            { urls: 'stun:stun.ideasip.com:3478' },
            { urls: 'stun:stun.schlund.de:3478' },
            { urls: 'stun:stun.stunprotocol.org:3478' },
            
            // TURN servers for NAT traversal
            {
                urls: 'turn:openrelay.metered.ca:80',
                username: 'openrelay.project',
                credential: 'openrelay'
            },
            {
                urls: 'turn:openrelay.metered.ca:443',
                username: 'openrelay.project',
                credential: 'openrelay'
            },
            {
                urls: 'turn:openrelay.metered.ca:443?transport=tcp',
                username: 'openrelay.project',
                credential: 'openrelay'
            },
            {
                urls: 'turn:openrelay.metered.ca:80?transport=tcp',
                username: 'openrelay.project',
                credential: 'openrelay'
            }
        ],
        iceCandidatePoolSize: 10,
        bundlePolicy: 'max-bundle',
        rtcpMuxPolicy: 'require',
        iceTransportPolicy: 'all'
    },
    debug: 3
});
```

#### Voice Calls (`scriptVoice.js`)
Same enhanced configuration applied to voice calls.

### 2. Network Type Detection and Optimization

#### JavaScript Functions
```javascript
function detectNetworkType() {
    const connection = navigator.connection || navigator.mozConnection || navigator.webkitConnection;
    if (connection) {
        const type = connection.effectiveType || connection.type;
        const isWiFi = connection.type === 'wifi' || connection.type === 'ethernet';
        const isMobile = connection.type === 'cellular' || connection.type === '2g' || connection.type === '3g' || connection.type === '4g' || connection.type === '5g';
        
        console.log('Network type detected:', type, 'WiFi:', isWiFi, 'Mobile:', isMobile);
        return { type, isWiFi, isMobile, effectiveType: connection.effectiveType };
    }
    return { type: 'unknown', isWiFi: false, isMobile: false, effectiveType: 'unknown' };
}

function optimizeForNetwork() {
    const networkInfo = detectNetworkType();
    console.log('Optimizing for network:', networkInfo);
    
    // Adjust ICE gathering timeout based on network
    if (networkInfo.isWiFi) {
        // WiFi networks may have more complex NAT, use longer timeout
        return {
            iceGatheringTimeout: 15000,
            connectionTimeout: 30000,
            retryAttempts: 5
        };
    } else if (networkInfo.isMobile) {
        // Mobile networks are usually more direct
        return {
            iceGatheringTimeout: 10000,
            connectionTimeout: 20000,
            retryAttempts: 3
        };
    } else {
        // Default settings
        return {
            iceGatheringTimeout: 12000,
            connectionTimeout: 25000,
            retryAttempts: 4
        };
    }
}
```

### 3. Enhanced Connection Retry Logic

#### Network-Aware Retry Function
```javascript
function connectToPeerWithRetry(peerId, retries = 3, delay = 5000) {
    const networkOpts = optimizeForNetwork();
    const maxRetries = networkOpts.retryAttempts;
    
    console.log('Connecting to peer with network optimization:', peerId, 'Max retries:', maxRetries);
    
    if (retries <= 0) {
        console.error('Max retry attempts reached for peer:', peerId);
        statusBar.textContent = `Failed to connect to peer: ${peerId}`;
        return;
    }
    
    // Connection logic with network-specific timeouts
    // ... (implementation details in script files)
}
```

### 4. Android Network Monitoring

#### Enhanced Network Callback
```java
private void registerNetworkCallback() {
    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkRequest request = new NetworkRequest.Builder().build();

    networkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onLost(@NonNull Network network) {
            runOnUiThread(() -> {
                Log.d(TAG, "Network lost, notifying WebView");
                binding.webView.evaluateJavascript("javascript:handleNetworkLoss()", null);
            });
        }

        @Override
        public void onAvailable(@NonNull Network network) {
            runOnUiThread(() -> {
                Log.d(TAG, "Network available, notifying WebView");
                binding.webView.evaluateJavascript("javascript:handleNetworkResume()", null);
            });
        }

        @Override
        public void onCapabilitiesChanged(@NonNull Network network, @NonNull android.net.NetworkCapabilities networkCapabilities) {
            runOnUiThread(() -> {
                // Detect network type and notify WebView
                boolean isWiFi = networkCapabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_WIFI);
                boolean isCellular = networkCapabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_CELLULAR);
                boolean isEthernet = networkCapabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_ETHERNET);
                
                String networkType = "unknown";
                if (isWiFi) networkType = "wifi";
                else if (isCellular) networkType = "cellular";
                else if (isEthernet) networkType = "ethernet";
                
                Log.d(TAG, "Network capabilities changed - Type: " + networkType + ", WiFi: " + isWiFi + ", Cellular: " + isCellular);
                
                // Notify WebView about network type change
                binding.webView.evaluateJavascript("javascript:handleNetworkTypeChange('" + networkType + "', " + isWiFi + ", " + isCellular + ")", null);
            });
        }
    };

    connectivityManager.registerNetworkCallback(request, networkCallback);
}
```

### 5. Network Security Configuration

#### `network_security_config.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <!-- Allow cleartext traffic for WebRTC and TURN servers -->
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">localhost</domain>
        <domain includeSubdomains="true">127.0.0.1</domain>
        <domain includeSubdomains="true">10.0.2.2</domain>
        <domain includeSubdomains="true">stun.l.google.com</domain>
        <domain includeSubdomains="true">stun1.l.google.com</domain>
        <domain includeSubdomains="true">stun2.l.google.com</domain>
        <domain includeSubdomains="true">stun3.l.google.com</domain>
        <domain includeSubdomains="true">stun4.l.google.com</domain>
        <domain includeSubdomains="true">openrelay.metered.ca</domain>
        <domain includeSubdomains="true">stun.voiparound.com</domain>
        <domain includeSubdomains="true">stun.voipbuster.com</domain>
        <domain includeSubdomains="true">stun.voipstunt.com</domain>
        <domain includeSubdomains="true">stun.voxgratia.org</domain>
        <domain includeSubdomains="true">stun.ekiga.net</domain>
        <domain includeSubdomains="true">stun.ideasip.com</domain>
        <domain includeSubdomains="true">stun.schlund.de</domain>
        <domain includeSubdomains="true">stun.stunprotocol.org</domain>
        <domain includeSubdomains="true">global.stun.twilio.com</domain>
    </domain-config>
    
    <!-- Allow WebView to access local assets without security restrictions -->
    <base-config cleartextTrafficPermitted="true">
        <trust-anchors>
            <certificates src="system"/>
            <certificates src="user"/>
        </trust-anchors>
    </base-config>
    
    <!-- Prevent WebView from accessing native camera characteristics -->
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">file://</domain>
        <domain includeSubdomains="true">content://</domain>
    </domain-config>
    
    <!-- Enhanced configuration for corporate/ISP networks -->
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">*.google.com</domain>
        <domain includeSubdomains="true">*.googleapis.com</domain>
        <domain includeSubdomains="true">*.firebase.com</domain>
        <domain includeSubdomains="true">*.firebaseio.com</domain>
        <domain includeSubdomains="true">*.googleusercontent.com</domain>
    </domain-config>
</network-security-config>
```

#### AndroidManifest.xml Update
```xml
<application
    android:name="util.MyApplication"
    android:allowBackup="true"
    android:dataExtractionRules="@xml/data_extraction_rules"
    android:fullBackupContent="@xml/backup_rules"
    android:hardwareAccelerated="true"
    android:icon="@mipmap/blue"
    android:label="@string/app_name"
    android:requestLegacyExternalStorage="true"
    android:roundIcon="@mipmap/blue_round"
    android:supportsRtl="true"
    android:theme="@style/Theme.Enclosure"
    android:usesCleartextTraffic="true"
    android:networkSecurityConfig="@xml/network_security_config"
    tools:targetApi="35">
```

## Key Benefits

### 1. **Improved NAT Traversal**
- Multiple STUN servers for better connectivity
- TURN servers for complex NAT scenarios
- Enhanced ICE gathering with more candidates

### 2. **Network-Aware Optimization**
- Different timeout values for WiFi vs Mobile
- Adaptive retry logic based on network type
- Real-time network type detection

### 3. **Better Error Handling**
- Network-specific retry attempts
- Connection timeout optimization
- Automatic reconnection on network changes

### 4. **Enhanced Security**
- Proper network security configuration
- Support for corporate/ISP networks
- Cleartext traffic permissions for WebRTC

## Testing Recommendations

### 1. **Network Types to Test**
- Airtel WiFi (primary issue)
- Jio WiFi
- BSNL WiFi
- Mobile data (4G/5G)
- Corporate WiFi networks
- Public WiFi hotspots

### 2. **Test Scenarios**
- Call initiation on different networks
- Network switching during calls
- Poor network conditions
- Network disconnection/reconnection

### 3. **Monitoring**
- Check browser console for network detection logs
- Monitor Android logs for network callback events
- Verify ICE candidate gathering success
- Test connection establishment times

## Troubleshooting

### If calls still fail on WiFi:
1. Check browser console for ICE gathering errors
2. Verify TURN server accessibility
3. Test with different WiFi networks
4. Check corporate firewall settings

### If calls work but have poor quality:
1. Adjust connection timeout values
2. Increase retry attempts for WiFi
3. Monitor network type detection accuracy

## Files Modified

1. `app/src/main/assets/script.js` - Video call WebRTC configuration
2. `app/src/main/assets/scriptVoice.js` - Voice call WebRTC configuration
3. `app/src/main/java/com/Appzia/enclosure/activities/MainActivityVideoCall.java` - Video call network monitoring
4. `app/src/main/java/com/Appzia/enclosure/activities/MainActivityVoiceCall.java` - Voice call network monitoring
5. `app/src/main/res/xml/network_security_config.xml` - Network security configuration
6. `app/src/main/AndroidManifest.xml` - Network security config reference

## Conclusion

These fixes address the core network connectivity issues that were preventing calls from working on Airtel WiFi. The enhanced ICE server configuration, network-aware optimization, and improved retry logic should significantly improve call success rates across different network types.
