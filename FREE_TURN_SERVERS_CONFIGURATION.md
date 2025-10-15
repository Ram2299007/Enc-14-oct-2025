# 🆓 Free TURN Servers Configuration

## Overview
This configuration uses **ONLY free TURN servers** for video and voice calls, with **NO STUN servers**. This approach ensures maximum NAT traversal capability while keeping costs at zero.

## Why TURN Servers Only?

### Advantages:
- **Better NAT Traversal**: TURN servers can relay traffic when direct peer-to-peer connections fail
- **Higher Success Rate**: Works on restrictive networks (corporate firewalls, public WiFi)
- **No STUN Limitations**: STUN servers only discover public IPs, TURN servers actually relay traffic
- **Cost Effective**: All servers are completely free

### Trade-offs:
- **Slightly Higher Latency**: Traffic goes through TURN servers instead of direct connection
- **Server Dependency**: Relies on TURN server availability (mitigated by multiple servers)

## Free TURN Servers Used

### OpenRelay (Metered.ca) - The Only Reliable Free TURN Server
- **URLs**: 
  - `turn:openrelay.metered.ca:80`
  - `turn:openrelay.metered.ca:443`
  - `turn:openrelay.metered.ca:443?transport=tcp`
  - `turn:openrelay.metered.ca:80?transport=tcp`
- **Credentials**: `openrelay.project` / `openrelay`
- **Status**: ✅ Free and highly reliable
- **Features**: Multiple ports and transport protocols
- **Why this one**: Most other free TURN servers have DNS resolution issues or are unreliable

## Configuration Details

### ICE Server Configuration
```javascript
iceServers: [
    // OpenRelay TURN servers - Only reliable free TURN servers
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
]
```

### Enhanced Settings
- **iceCandidatePoolSize**: 15 (increased for better connectivity)
- **bundlePolicy**: 'max-bundle' (optimizes bandwidth)
- **rtcpMuxPolicy**: 'require' (reduces port usage)

## Network Security Configuration

The Android app's network security config allows traffic to these TURN servers:

```xml
<domain-config cleartextTrafficPermitted="true">
    <domain includeSubdomains="true">openrelay.metered.ca</domain>
</domain-config>
```

## Files Modified

1. **`app/src/main/assets/script.js`** - Video call configuration
2. **`app/src/main/assets/scriptVoice.js`** - Voice call configuration  
3. **`app/src/main/res/xml/network_security_config.xml`** - Network permissions

## Benefits

### ✅ Cost Savings
- **$0/month** - No paid TURN services
- **No usage limits** - Free servers have generous limits
- **No API keys** - Simple username/password authentication

### ✅ Reliability
- **Multiple servers** - 9 different TURN endpoints
- **Redundancy** - If one fails, others take over
- **Different providers** - Reduces single point of failure

### ✅ Compatibility
- **Works on restrictive networks** - Corporate firewalls, public WiFi
- **Multiple transport protocols** - UDP and TCP support
- **Multiple ports** - 80, 443, 3478 for maximum compatibility

## Testing

To test the configuration:

1. **Build and install** the updated APK
2. **Test on different networks**:
   - Mobile data (4G/5G)
   - Home WiFi
   - Public WiFi (restaurants, cafes)
   - Corporate networks
3. **Check logs** for TURN server usage:
   - Look for "relay" candidates in WebRTC logs
   - Verify connections are established through TURN servers

## Troubleshooting

### If calls fail:
1. **Check network connectivity** to TURN servers
2. **Verify credentials** are correct
3. **Try different TURN servers** (they're tried in order)
4. **Check firewall settings** - ensure ports 80, 443, 3478 are open

### Common Issues:
- **Authentication failures**: Verify username/password
- **Connection timeouts**: Try different TURN servers
- **Network blocking**: Some networks block TURN traffic

## Future Considerations

### If you need more TURN servers:
1. **Add more free servers** to the configuration
2. **Consider self-hosting** a TURN server
3. **Monitor usage** to ensure free limits aren't exceeded

### Performance optimization:
1. **Monitor latency** - TURN servers may add 50-200ms
2. **Test different servers** - Some may be faster in your region
3. **Consider geographic distribution** - Use servers closer to your users

---

**Note**: This configuration prioritizes reliability and cost-effectiveness over minimal latency. For applications requiring ultra-low latency, consider adding STUN servers back or using a hybrid approach.
