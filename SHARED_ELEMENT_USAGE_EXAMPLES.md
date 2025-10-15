# 🎨 Shared Element Transition Usage Examples

## 📱 **Layout XML Examples**

### **1. Basic Shared Element Setup**
```xml
<!-- In your layout XML, add transition names to views -->
<ImageView
    android:id="@+id/profile_image"
    android:layout_width="100dp"
    android:layout_height="100dp"
    android:transitionName="profile_image_transition"
    android:src="@drawable/profile_placeholder" />

<TextView
    android:id="@+id/profile_name"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:transitionName="profile_name_transition"
    android:text="John Doe" />

<TextView
    android:id="@+id/profile_status"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:transitionName="profile_status_transition"
    android:text="Online" />
```

### **2. Chat Message Shared Elements**
```xml
<!-- Chat message item with shared elements -->
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="horizontal"
    android:padding="8dp">
    
    <ImageView
        android:id="@+id/message_image"
        android:layout_width="60dp"
        android:layout_height="60dp"
        android:transitionName="message_image_transition"
        android:src="@drawable/message_placeholder" />
    
    <LinearLayout
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:orientation="vertical"
        android:layout_marginStart="8dp">
        
        <TextView
            android:id="@+id/sender_name"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:transitionName="sender_name_transition"
            android:text="Sender Name"
            android:textStyle="bold" />
        
        <TextView
            android:id="@+id/message_text"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:transitionName="message_text_transition"
            android:text="Message content here" />
    </LinearLayout>
</LinearLayout>
```

### **3. Media Viewer Shared Elements**
```xml
<!-- Media viewer with shared elements -->
<FrameLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    
    <ImageView
        android:id="@+id/media_image"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:transitionName="media_image_transition"
        android:scaleType="centerCrop"
        android:src="@drawable/media_placeholder" />
    
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom"
        android:background="@drawable/gradient_overlay"
        android:orientation="vertical"
        android:padding="16dp">
        
        <TextView
            android:id="@+id/media_title"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:transitionName="media_title_transition"
            android:text="Media Title"
            android:textColor="@android:color/white"
            android:textSize="18sp"
            android:textStyle="bold" />
        
        <TextView
            android:id="@+id/media_description"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:transitionName="media_description_transition"
            android:text="Media description"
            android:textColor="@android:color/white"
            android:textSize="14sp" />
    </LinearLayout>
</FrameLayout>
```

---

## 💻 **Java/Kotlin Usage Examples**

### **1. Basic Shared Element Transition**
```java
// Start activity with single shared element
private void openProfileActivity() {
    Intent intent = new Intent(this, ProfileActivity.class);
    ImageView profileImage = findViewById(R.id.profile_image);
    
    // Using SharedElementBaseActivity
    startActivityWithSharedElement(intent, profileImage, "profile_image_transition");
    
    // OR using SmoothNavigationHelper
    SmoothNavigationHelper.startActivityWithSharedElement(this, intent, profileImage, "profile_image_transition");
}
```

### **2. Multiple Shared Elements**
```java
// Start activity with multiple shared elements
private void openChatActivity() {
    Intent intent = new Intent(this, ChatActivity.class);
    
    // Create shared element pairs
    List<Pair<View, String>> sharedElements = new ArrayList<>();
    sharedElements.add(SmoothNavigationHelper.createSharedElementPair(
        findViewById(R.id.profile_image), "profile_image_transition"));
    sharedElements.add(SmoothNavigationHelper.createSharedElementPair(
        findViewById(R.id.profile_name), "profile_name_transition"));
    sharedElements.add(SmoothNavigationHelper.createSharedElementPair(
        findViewById(R.id.profile_status), "profile_status_transition"));
    
    // Start activity with shared elements
    startActivityWithSharedElements(intent, sharedElements);
}
```

### **3. Smart Transition (Automatic)**
```java
// Use smart transition - automatically chooses best transition
private void openNextActivity() {
    Intent intent = new Intent(this, NextActivity.class);
    
    // Automatically uses shared element transitions on Android 5.0+
    // Falls back to smooth animations on older versions
    SmoothNavigationHelper.startActivityWithSmartTransition(this, intent);
}
```

### **4. Different Transition Types**
```java
// Explode transition
private void openWithExplode() {
    Intent intent = new Intent(this, NextActivity.class);
    SmoothNavigationHelper.startActivityWithExplode(this, intent);
}

// Fade transition
private void openWithFade() {
    Intent intent = new Intent(this, NextActivity.class);
    SmoothNavigationHelper.startActivityWithFadeTransition(this, intent);
}

// Slide transition
private void openWithSlide() {
    Intent intent = new Intent(this, NextActivity.class);
    SmoothNavigationHelper.startActivityWithSlideTransition(this, intent, Gravity.END);
}
```

### **5. Activity Setup for Shared Elements**
```java
public class YourActivity extends SharedElementBaseActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your);
        
        // Shared element transitions are automatically enabled!
        // No additional setup needed
        
        setupViews();
    }
    
    private void setupViews() {
        // Your view setup code here
        ImageView profileImage = findViewById(R.id.profile_image);
        profileImage.setOnClickListener(v -> openProfileActivity());
    }
    
    private void openProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        ImageView profileImage = findViewById(R.id.profile_image);
        
        // Use the built-in method from SharedElementBaseActivity
        startActivityWithSharedElement(intent, profileImage, "profile_image_transition");
    }
}
```

### **6. Custom Transition Duration**
```java
public class YourActivity extends SharedElementBaseActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your);
        
        // Setup custom transition duration (500ms)
        setupCustomSharedElementTransition(500);
        
        setupViews();
    }
}
```

### **7. Conditional Shared Element Transitions**
```java
private void openActivityWithConditionalTransition() {
    Intent intent = new Intent(this, NextActivity.class);
    
    if (SmoothNavigationHelper.isSharedElementTransitionsEnabled()) {
        // Use shared element transition
        ImageView sharedView = findViewById(R.id.shared_element);
        SmoothNavigationHelper.startActivityWithSharedElement(this, intent, sharedView, "transition_name");
    } else {
        // Fallback to smooth animation
        SmoothNavigationHelper.startActivityWithSlideFromRight(this, intent);
    }
}
```

---

## 🎯 **Best Practices**

### **1. Transition Names**
- Use descriptive, unique transition names
- Follow naming convention: `element_type_transition`
- Examples: `profile_image_transition`, `message_text_transition`, `media_title_transition`

### **2. View Selection**
- Choose views that are visually prominent
- Ensure views exist in both source and destination activities
- Use views that make sense to transition (images, text, buttons)

### **3. Performance**
- Keep transition duration reasonable (200-500ms)
- Avoid too many shared elements (max 3-4)
- Test on different device performance levels

### **4. Fallback Handling**
- Always provide fallback for older Android versions
- Use `SmoothNavigationHelper.isSharedElementTransitionsEnabled()` to check
- Test on Android 4.4 and below

### **5. Layout Considerations**
- Ensure shared elements have consistent sizing
- Use `android:scaleType` appropriately for images
- Consider different screen sizes and orientations

---

## 🚀 **Quick Migration Checklist**

### **For Existing Activities:**
- [ ] Change class to extend `SharedElementBaseActivity`
- [ ] Update theme in `AndroidManifest.xml`
- [ ] Add `android:transitionName` to views in XML
- [ ] Replace navigation calls with shared element methods
- [ ] Test transitions on different Android versions

### **For New Activities:**
- [ ] Extend `SharedElementBaseActivity` from the start
- [ ] Use appropriate theme in manifest
- [ ] Plan shared elements in layout design
- [ ] Use smart transition methods for navigation

---

## 🎨 **Theme Selection Guide**

| Screen Type | Recommended Theme | Use Case |
|-------------|------------------|----------|
| **Chat Screens** | `Theme.Enclosure.SharedElement.Chat` | Group chat, individual chat |
| **Media Viewers** | `Theme.Enclosure.SharedElement.Media` | Image viewer, video player, document viewer |
| **Profile Screens** | `Theme.Enclosure.SharedElement.Profile` | User profile, edit profile |
| **Settings Screens** | `Theme.Enclosure.SharedElement.Settings` | Settings, preferences, account |
| **General Screens** | `Theme.Enclosure.SharedElement` | Default for most screens |
| **Fast Transitions** | `Theme.Enclosure.SharedElement.Fast` | Quick navigation, lists |
| **Slow Transitions** | `Theme.Enclosure.SharedElement.Slow` | Dramatic effects, important screens |

Your app now has a complete shared element transition system! 🎉
