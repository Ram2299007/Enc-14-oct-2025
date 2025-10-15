# Voice Call Photo Display and Audio Output Fixes

## Issues Identified and Fixed

### 1. **Receiver Photo Not Showing for Sender**
- **Problem**: When the sender initiates a call, the receiver's photo was not displaying properly
- **Root Cause**: The photo information wasn't being properly propagated to the UI when set via `setCallerInfo` or `setRemoteCallerInfo`
- **Fix**: Enhanced photo handling and UI updates

### 2. **Default Audio Output to Loudspeaker Instead of Earpiece**
- **Problem**: Voice calls were defaulting to loudspeaker instead of earpiece
- **Root Cause**: Audio output wasn't being initialized to earpiece by default
- **Fix**: Set default audio output to earpiece on page load and peer connection

## Implemented Solutions

### 1. **Enhanced Photo Display Logic**

#### Fixed `setCallerInfo` Function
```javascript
function setCallerInfo(name, photo, uid) {
    if (!uid || uid === "self") return; // skip invalid keys

    console.log(`[setCallerInfo] Setting info for UID: ${uid}, Name: ${name}, Photo: ${photo}`);

    if (!participantData[uid] || participantData[uid].name !== name || participantData[uid].photo !== photo) {
        participantData[uid] = {
            name: name || 'Unknown',
            photo: photo || 'file:///android_asset/user.png'
        };
        console.log(`[setCallerInfo] Updated participant data for ${uid}:`, participantData[uid]);
    }

    // If this is a remote participant (not self), also set as remote caller info
    if (uid !== myUid) {
        remoteCallerPhoto = photo;
        remoteCallerName = name;
        console.log(`[setCallerInfo] Set as remote caller: ${remoteCallerName}, ${remoteCallerPhoto}`);
    }

    if (uid === myUid) {
        const callerName = document.getElementById('callerName');
        if (callerName) callerName.textContent = name || 'Name';

        const callerImage = document.getElementById('callerImage');
        if (callerImage) callerImage.src = participantData[uid].photo;
    }

    updateParticipantsUI();
}
```

#### Enhanced `setRemoteCallerInfo` Function
```javascript
function setRemoteCallerInfo(photo, name) {
    remoteCallerPhoto = photo;
    remoteCallerName = name;
    console.log("[setRemoteCallerInfo] Remote caller info set:", remoteCallerPhoto, remoteCallerName);

    // Force update the UI immediately
    updateParticipantsUI();
    
    // Also initialize caller info for backward compatibility
    initializeCallerInfo();
    
    console.log("[setRemoteCallerInfo] UI updated with remote caller info");
}
```

#### Improved UI Update Logic
```javascript
// Handle 2 participants (you + one)
else if (participantCount === 2) {
    const remoteUid = uidList.find(uid => uid !== myUid);
    const remotePhoto = remoteCallerPhoto || participantData[remoteUid]?.photo || 'file:///android_asset/user.png';
    const remoteName = remoteCallerName || participantData[remoteUid]?.name || 'Name';

    console.log(`[updateParticipantsUI] 2 participants - Remote UID: ${remoteUid}, Photo: ${remotePhoto}, Name: ${remoteName}`);
    console.log(`[updateParticipantsUI] remoteCallerPhoto: ${remoteCallerPhoto}, remoteCallerName: ${remoteCallerName}`);

    singleCallerInfo.innerHTML = `
        <img id="callerImage" src="${remotePhoto}" alt="${remoteName}" style="border-radius: 50%; width: 100px; height: 100px;">
        <div id="callerName" class="caller-name">${remoteName}</div>
    `;
    singleCallerInfo.appendChild(callTimer);
    singleCallerInfo.appendChild(callStatus);
    
    // Force image load and handle errors
    const callerImage = document.getElementById('callerImage');
    if (callerImage) {
        callerImage.onload = () => {
            console.log(`[updateParticipantsUI] Image loaded successfully: ${remotePhoto}`);
        };
        callerImage.onerror = () => {
            console.error(`[updateParticipantsUI] Failed to load image: ${remotePhoto}, using default`);
            callerImage.src = 'file:///android_asset/user.png';
        };
    }
}
```

### 2. **Default Audio Output to Earpiece**

#### Set Default Audio on Peer Connection
```javascript
peer.on('open', id => {
    myUid = id;
    callStatus.textContent = 'Connecting';
    console.log('Peer connection opened with ID:', id);
    if (typeof Android !== 'undefined') {
        try {
            Android.sendPeerId(id);
            Android.checkBluetoothAvailability();
            console.log('Sent peer ID and checked Bluetooth availability');
        } catch (err) {
            console.error('Error with Android interface:', err);
            callStatus.textContent = 'Connecting';
        }
    }
    initializeLocalStream()
        .then(stream => {
            localStream = stream;
            updateParticipantsUI();
            
            // Set default audio output to earpiece for voice calls
            setTimeout(() => {
                console.log('Setting default audio output to earpiece...');
                setAudioOutput('earpiece');
            }, 1000); // Delay to ensure Android is ready

        })
        .catch(err => {
            console.error('Failed to get local audio stream:', err);
            callStatus.textContent = 'Failed to access microphone';
        });
});
```

#### Set Default Audio on Page Load
```javascript
document.addEventListener('DOMContentLoaded', () => {
    // Start audio health monitoring
    monitorAudioHealth();
    
    // Initialize existing functionality
    restoreMuteState();
    const voiceContainer = document.querySelector('.voice-container');
    const controlsContainer = document.querySelector('.controls-container');
    const topBar = document.querySelector('.top-bar');
    callTimer.style.display = 'block';
    callTimer.style.marginTop = '10px';
    callTimer.style.fontSize = '14px';
    callTimer.style.fontWeight = '500';
    callTimer.style.color = '@color/white';

    voiceContainer.addEventListener('click', (event) => {
        if (event.target.closest('.control-btn') || event.target.closest('.top-btn') || event.target.closest('.audio-option')) return;
        controlsContainer.classList.toggle('hidden');
        topBar.classList.toggle('hidden');
        audioOutputMenu.classList.remove('show');
    });

    if (typeof Android !== 'undefined') {
        try {
            Android.onPageReady();
            console.log('Called Android.onPageReady');
            
            // Ensure audio output is set to earpiece on page load
            setTimeout(() => {
                console.log('Setting initial audio output to earpiece on page load...');
                setAudioOutput('earpiece');
            }, 500);
            
        } catch (err) {
            console.error('Error calling Android.onPageReady:', err);
        }
    }
});
```

## How the Fixes Work

### 1. **Photo Display Fix**
- **Enhanced Logging**: Added detailed logging to track photo setting and UI updates
- **Immediate UI Update**: `setRemoteCallerInfo` now forces immediate UI updates
- **Fallback Handling**: Added error handling for image loading with fallback to default image
- **Dual Photo Sources**: Photos can now come from both `setCallerInfo` and `setRemoteCallerInfo`

### 2. **Audio Output Fix**
- **Multiple Initialization Points**: Audio output is set to earpiece at multiple points:
  - Page load (500ms delay)
  - Peer connection open (1000ms delay)
- **Android Integration**: Properly calls `Android.setAudioOutput('earpiece')`
- **Default State**: `currentAudioOutput` variable is initialized to 'earpiece'

## Testing the Fixes

### 1. **Photo Display Test**
- **As Sender**: Initiate a call and verify receiver's photo appears
- **As Receiver**: Receive a call and verify sender's photo appears
- **Check Console**: Look for photo loading success/error messages

### 2. **Audio Output Test**
- **Default State**: Verify audio defaults to earpiece on call start
- **Manual Switch**: Test switching between earpiece, speaker, and Bluetooth
- **Android Integration**: Check that Android audio routing works correctly

### 3. **Console Logs to Monitor**
```
[setCallerInfo] Setting info for UID: [uid], Name: [name], Photo: [photo]
[setCallerInfo] Set as remote caller: [name], [photo]
[setRemoteCallerInfo] Remote caller info set: [photo], [name]
[setRemoteCallerInfo] UI updated with remote caller info
[updateParticipantsUI] 2 participants - Remote UID: [uid], Photo: [photo], Name: [name]
[updateParticipantsUI] Image loaded successfully: [photo]
Setting default audio output to earpiece...
```

## Expected Results

### ✅ **Photo Display**
- Receiver's photo should now display when sender initiates call
- Photos should load properly with fallback to default image
- UI should update immediately when photo information is set

### ✅ **Audio Output**
- Voice calls should default to earpiece instead of loudspeaker
- Audio routing should work properly on Android
- Manual audio switching should work correctly

## Troubleshooting

### If Photos Still Don't Show
1. Check console logs for photo loading errors
2. Verify `setCallerInfo` or `setRemoteCallerInfo` is being called
3. Check network connectivity for remote images
4. Verify image URLs are valid

### If Audio Still Defaults to Loudspeaker
1. Check console logs for audio output setting
2. Verify Android interface is available
3. Check microphone permissions
4. Verify audio constraints are supported

## Android Integration

The fixes ensure proper integration with Android's audio system:
- Uses `AudioManager.MODE_IN_COMMUNICATION` for voice calls
- Calls `Android.setAudioOutput('earpiece')` to set default routing
- Handles audio focus and routing properly
- Supports earpiece, speaker, and Bluetooth switching

These fixes should resolve both the photo display issue and ensure voice calls default to earpiece for better user experience.
