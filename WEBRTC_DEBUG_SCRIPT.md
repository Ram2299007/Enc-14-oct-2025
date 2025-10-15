# 🔧 WebRTC Debug Script for Airtel WiFi

## Quick Test Script

Add this to your browser console to test the WebRTC configuration:

```javascript
// Test STUN server accessibility
async function testStunServers() {
    const servers = [
        'stun:stun.l.google.com:19302',
        'stun:stun1.l.google.com:19302',
        'stun:stun2.l.google.com:19302'
    ];
    
    console.log('Testing STUN servers...');
    for (const server of servers) {
        try {
            const pc = new RTCPeerConnection({ iceServers: [{ urls: server }] });
            await pc.createOffer();
            pc.close();
            console.log(`✅ ${server} - WORKING`);
        } catch (error) {
            console.log(`❌ ${server} - FAILED: ${error.message}`);
        }
    }
}

// Test TURN server accessibility
async function testTurnServers() {
    const servers = [
        {
            urls: 'turn:openrelay.metered.ca:80',
            username: 'openrelayproject',
            credential: 'openrelayproject'
        }
    ];
    
    console.log('Testing TURN servers...');
    for (const server of servers) {
        try {
            const pc = new RTCPeerConnection({ iceServers: [server] });
            await pc.createOffer();
            pc.close();
            console.log(`✅ TURN server - WORKING`);
        } catch (error) {
            console.log(`❌ TURN server - FAILED: ${error.message}`);
        }
    }
}

// Run tests
testStunServers().then(() => testTurnServers());
```

## Expected Results

### On Airtel WiFi:
- ❌ STUN servers should fail
- ✅ TURN servers should work
- App should automatically switch to TURN-only mode

### On Normal WiFi:
- ✅ STUN servers should work
- ✅ TURN servers should work
- App should use balanced configuration

## Debug Console Commands

```javascript
// Check network detection
console.log('Network info:', await networkDetector.detectNetwork());

// Check ICE servers
console.log('ICE servers:', networkDetector.getOptimalIceServers());

// Check connection quality
console.log('Connection qualities:', networkMonitor.getAllConnectionQualities());

// Force TURN-only mode
networkMonitor.switchToTurnOnlyMode();
```

## What the Logs Mean

### Error Code -105
- **Meaning**: "Name or service not known" (DNS resolution failed)
- **Cause**: Airtel is blocking DNS resolution for STUN servers
- **Solution**: Our app automatically switches to TURN-only mode

### Expected Console Output
```
Network type detected: 4g
Region detected: IN
Network restrictive test: 0/2 STUN servers accessible, restrictive: true
Using TURN-only configuration for restrictive network
Peer initialized with 4 ICE servers
```

## Troubleshooting Steps

1. **Check if TURN-only mode is active**:
   ```javascript
   console.log('ICE transport policy:', peer._config.iceTransportPolicy);
   ```

2. **Monitor connection attempts**:
   ```javascript
   peer.on('icecandidate', (candidate) => {
       console.log('ICE candidate:', candidate);
   });
   ```

3. **Check connection state**:
   ```javascript
   peer.on('connection', (conn) => {
       console.log('Connection state:', conn.connectionState);
   });
   ```

## Success Indicators

✅ **Working correctly when you see**:
- "Using TURN-only configuration for restrictive network"
- "Peer initialized with X ICE servers" (where X > 0)
- No more DNS resolution errors for TURN servers
- Video calls connecting successfully

❌ **Still having issues when you see**:
- "Using balanced configuration for normal network" (on Airtel)
- "Peer initialized with 0 ICE servers"
- Continued DNS resolution errors
- Video calls still failing to connect
