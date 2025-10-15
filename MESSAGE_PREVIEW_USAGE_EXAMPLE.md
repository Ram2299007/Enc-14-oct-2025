# Message Preview Usage Example

## Overview
I've implemented a new message preview functionality that shows a preview of the actual message data in the same design as the RecyclerView items when you long-click on a message.

## Files Created/Modified

### 1. New Layout File
- `app/src/main/res/layout/message_preview_dialog.xml` - The preview dialog layout

### 2. New Helper Class
- `app/src/main/java/com/Appzia/enclosure/Utils/MessagePreviewHelper.java` - Handles the preview functionality

### 3. Modified Chat Adapter
- Added import for `MessagePreviewHelper`
- Added static method `showMessagePreview()` to the chat adapter

## How to Use

### Option 1: Use the Static Method (Recommended)
```java
// In your long click listener
((senderViewHolder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
    @Override
    public boolean onLongClick(View v) {
        // Get touch location for positioning
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        float touchX = location[0];
        float touchY = location[1];

        // Show message preview with exact same design as RecyclerView
        chatAdapter.showMessagePreview(mContext, model, SENDER_VIEW_TYPE, touchX, touchY);
        
        return true;
    }
});
```

### Option 2: Use the Helper Directly
```java
// In your long click listener
((receiverViewHolder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
    @Override
    public boolean onLongClick(View v) {
        // Get touch location for positioning
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        float touchX = location[0];
        float touchY = location[1];

        // Show message preview with exact same design as RecyclerView
        MessagePreviewHelper.showMessagePreview(mContext, model, RECEIVER_VIEW_TYPE, touchX, touchY);
        
        return true;
    }
});
```

## Features

1. **Exact Same Design**: The preview shows the message in the same visual style as the actual RecyclerView items
2. **Smart Positioning**: The dialog is positioned near the touch point but stays within screen bounds
3. **Blur Background**: Uses the existing BlurHelper for a professional look
4. **Action Buttons**: Includes Copy, Reply, and Delete buttons
5. **Message Type Support**: Handles both sender and receiver message types
6. **Responsive Layout**: Adapts to different message content lengths

## Message Types Supported

- Text messages
- Images (with preview)
- Videos (with thumbnail)
- Documents
- Links (with preview)
- Contacts
- Voice messages

## Customization

You can customize the preview by modifying:
- `message_preview_dialog.xml` - Layout and styling
- `MessagePreviewHelper.java` - Preview logic and action handlers
- Colors and fonts in the layout file

## Integration

To integrate this into your existing long click implementations:

1. Find your existing long click listener in the chat adapter
2. Replace the `BlurHelper.showDialogWithBlurBackground()` call with the new preview method
3. Remove the complex dialog setup code
4. The preview will automatically handle all message types and positioning

## Example Integration

Instead of this complex code:
```java
BlurHelper.showDialogWithBlurBackground(mContext, R.layout.sender_long_press_dialogue);
BlurHelper.dialogLayoutColor.show();
// ... 100+ lines of dialog setup code
```

Use this simple code:
```java
MessagePreviewHelper.showMessagePreview(mContext, model, SENDER_VIEW_TYPE, touchX, touchY);
```

The preview will automatically show the message content in the same design as your RecyclerView items!
