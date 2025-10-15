# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# -----------------------
# Debugging / Crash reports
# -----------------------
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# -----------------------
# Firebase + Play Services
# -----------------------
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**

# Firebase Init Provider (auto-init)
-keep class com.google.firebase.provider.FirebaseInitProvider { *; }

# Firebase Messaging (getToken)
-keep class com.google.firebase.messaging.FirebaseMessaging { *; }
-keepclassmembers class com.google.firebase.messaging.FirebaseMessaging {
    public com.google.android.gms.tasks.Task getToken();
}

# Google Play Services Tasks API
-keep class com.google.android.gms.tasks.** { *; }
-keepclassmembers class com.google.android.gms.tasks.** { *; }
-keep interface com.google.android.gms.tasks.** { *; }

# Ensure OnCompleteListener isn’t stripped
-keepclassmembers class * implements com.google.android.gms.tasks.OnCompleteListener {
    public void onComplete(com.google.android.gms.tasks.Task);
}

# Firebase Storage
-keep class com.google.firebase.storage.** { *; }
-keepclassmembers class com.google.firebase.storage.** { *; }

# -----------------------
# Jetpack DataStore + protobuf-lite
# -----------------------
-keep class androidx.datastore.** { *; }
-dontwarn androidx.datastore.**

-keep class androidx.datastore.preferences.protobuf.** { *; }
-dontwarn androidx.datastore.preferences.protobuf.**

-keep class com.google.protobuf.** { *; }
-dontwarn com.google.protobuf.**

# -----------------------
# Fresco / Facebook ImagePipeline
# -----------------------
-keep class com.facebook.imagepipeline.** { *; }
-keep class com.facebook.fresco.** { *; }
-dontwarn com.facebook.imagepipeline.**
-dontwarn com.facebook.fresco.**
-dontwarn com.facebook.imagepipeline.nativecode.WebpTranscoder
-dontwarn com.facebook.imagepipeline.nativecode.WebpTranscoderImpl

# -----------------------
# Jitsi Meet SDK
# -----------------------
-keep class org.jitsi.meet.** { *; }
-keep class org.webrtc.** { *; }
-dontwarn org.jitsi.meet.**
-dontwarn org.webrtc.**

# -----------------------
# Retrofit
# -----------------------
-keepattributes Signature, InnerClasses, EnclosingMethod, RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# -----------------------
# Gson
# -----------------------
-keepattributes Signature, *Annotation*, EnclosingMethod, InnerClasses
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# -----------------------
# Guava TypeToken
# -----------------------
-keep class com.google.common.reflect.TypeToken { *; }
-keep class * extends com.google.common.reflect.TypeToken
-keepclassmembers class * extends com.google.common.reflect.TypeToken {
    *;
}

# -----------------------
# AsyncHttp
# -----------------------
-keep class com.loopj.android.http.** { *; }
-dontwarn com.loopj.android.http.**

# -----------------------
# AndroidNetworking
# -----------------------
-keep class com.androidnetworking.** { *; }
-dontwarn com.androidnetworking.**

# -----------------------
# Glide
# -----------------------
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule { <init>(...); }
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}

# -----------------------
# Picasso
# -----------------------
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.picasso.** { *; }

# -----------------------
# JSON
# -----------------------
-keep class org.json.** { *; }
-keepclassmembers class org.json.** { *; }
-dontwarn org.json.**

# -----------------------
# General / Reflection helpers
# -----------------------
-keepclassmembers class * { @android.webkit.JavascriptInterface <methods>; }
-keepclasseswithmembernames class * { native <methods>; }

# Enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Retrofit interfaces
-keep interface * { @retrofit2.http.* <methods>; }

# -----------------------
# ExoPlayer
# -----------------------
-keep class com.google.android.exoplayer2.** { *; }
-dontwarn com.google.android.exoplayer2.**

# -----------------------
# AndroidX Media / Support libs
# -----------------------
-keep class androidx.media.** { *; }
-keep class android.support.v4.media.** { *; }
-keep class androidx.media3.** { *; }
-dontwarn androidx.media.**
-dontwarn android.support.v4.media.**
-dontwarn androidx.media3.**

# -----------------------
# Synthetic / Lambdas
# -----------------------
-keepclassmembers class * { synthetic <methods>; }
-keep class **$$ExternalSynthetic* { *; }

# -----------------------
# React Native
# -----------------------
-keep class com.facebook.react.** { *; }
-keep class com.facebook.hermes.** { *; }
-keep class com.facebook.jni.** { *; }
-dontwarn com.facebook.react.**
-dontwarn com.facebook.hermes.**
-dontwarn com.facebook.jni.**

# -----------------------
# RecyclerView and Adapter preservation
# -----------------------
-keep class androidx.recyclerview.** { *; }
-keep class androidx.recyclerview.widget.** { *; }
-keepclassmembers class androidx.recyclerview.widget.** { *; }

# Preserve RecyclerView.Adapter and related classes
-keep class * extends androidx.recyclerview.widget.RecyclerView$Adapter { *; }
-keepclassmembers class * extends androidx.recyclerview.widget.RecyclerView$Adapter {
    public void onBindViewHolder(...);
    public androidx.recyclerview.widget.RecyclerView$ViewHolder onCreateViewHolder(...);
    public int getItemCount();
    public int getItemViewType(int);
    public long getItemId(int);
    public void notifyItemInserted(int);
    public void notifyItemChanged(int);
    public void notifyDataSetChanged();
    public void enableStableIds();
    public void setHasStableIds(boolean);
}

# Preserve specific methods that might be called via reflection or optimization
-keepclassmembers class * {
    public long getItemId(int);
    public int getItemViewType(int);
    public int getItemCount();
    public void onBindViewHolder(androidx.recyclerview.widget.RecyclerView$ViewHolder, int);
    public androidx.recyclerview.widget.RecyclerView$ViewHolder onCreateViewHolder(android.view.ViewGroup, int);
}

# Preserve ViewHolder classes
-keep class * extends androidx.recyclerview.widget.RecyclerView$ViewHolder { *; }
-keepclassmembers class * extends androidx.recyclerview.widget.RecyclerView$ViewHolder {
    public <init>(android.view.View);
}

# Preserve LinearLayoutManager and other layout managers
-keep class androidx.recyclerview.widget.LinearLayoutManager { *; }
-keep class androidx.recyclerview.widget.GridLayoutManager { *; }
-keep class androidx.recyclerview.widget.StaggeredGridLayoutManager { *; }

# -----------------------
# App models / utils
# -----------------------
-keep class com.Appzia.enclosure.Model.** { *; }
-keep class com.Appzia.enclosure.models.** { *; }
-keep class com.Appzia.enclosure.Utils.Constant { *; }
-keepclassmembers class com.Appzia.enclosure.Utils.Constant { public static void loadImageIntoView*(...); }
-keep class com.Appzia.enclosure.SubScreens.ChattingRoomUtils { *; }
-keep class com.Appzia.enclosure.Screens.whatsTheCode { *; }
-keepclassmembers class com.Appzia.enclosure.Screens.whatsTheCode {
    public static java.lang.String PhoneNumberWithoutCountryCode(java.lang.String);
    public static java.lang.String globalNumber;
    public void getContactList(...);
}

# -----------------------
# OkHttp
# -----------------------
-keep class okhttp3.** { *; }
-keep class okio.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

# -----------------------
# Parcelable
# -----------------------
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# -----------------------
# Apache Commons Logging
# -----------------------
-keep class org.apache.commons.logging.** { *; }
-dontwarn org.apache.commons.logging.**

# -----------------------
# Constructors for reflection
# -----------------------
-keepclassmembers class * { public <init>(); }

# -----------------------
# Google API Client
# -----------------------
-keep class com.google.api.client.** { *; }
-keep class com.google.api.client.json.GenericJson { *; }
-keep class com.google.api.client.json.JsonParser { *; }
-keep class com.google.api.client.util.** { *; }
-keepclassmembers class com.google.api.client.json.GenericJson { public <init>(); }
-keepclassmembers class * extends com.google.api.client.json.GenericJson { public <init>(); }
-dontwarn com.google.api.client.**

# -----------------------
# Google Auth OAuth2
# -----------------------
-keep class com.google.auth.** { *; }
-keep class com.google.auth.oauth2.GoogleCredentials { *; }
-dontwarn com.google.auth.**

# -----------------------
# Jackson
# -----------------------
-dontwarn com.fasterxml.jackson.**
-dontwarn java.beans.ConstructorProperties
-dontwarn java.beans.Transient

# -----------------------
# Apache HTTP Legacy
# -----------------------
-dontwarn org.apache.http.**
-dontwarn android.net.http.AndroidHttpClient
-keep class org.apache.http.** { *; }
-keep class org.apache.http.message.ParserCursor { *; }

# -----------------------
# Foreground services (your app)
# -----------------------
-keep class com.Appzia.enclosure.Utils.CallServiceVideoCall { *; }
-keep class com.Appzia.enclosure.Utils.CallServiceVoiceCall { *; }
-keepclassmembers class com.Appzia.enclosure.Utils.CallServiceVideoCall {
    public static final java.lang.String ACTION_END_CALL;
    public static final java.lang.String ACTION_START_TIMER;
    public static final java.lang.String ACTION_TIMER_UPDATE;
    public static final java.lang.String ACTION_REQUEST_DURATION;
    public int onStartCommand(...);
    public void onDestroy();
    private void stopCall();
    public android.os.IBinder onBind(...);
}
-keepclassmembers class com.Appzia.enclosure.Utils.CallServiceVoiceCall {
    public static final java.lang.String ACTION_END_CALL;
    public static final java.lang.String ACTION_START_TIMER;
    public static final java.lang.String ACTION_TIMER_UPDATE;
    public static final java.lang.String ACTION_REQUEST_DURATION;
    public int onStartCommand(...);
    public void onDestroy();
    private void stopCall();
    public android.os.IBinder onBind(...);
}

# Preserve Activities
-keep class com.Appzia.enclosure.activities.MainActivityVideoCall { *; }
-keep class com.Appzia.enclosure.activities.MainActivityVoiceCall { *; }
-keepclassmembers class com.Appzia.enclosure.activities.MainActivityVideoCall {
    public void triggerEndCallButton();
    private void cleanupAndExit();
    protected void onDestroy();
}
-keepclassmembers class com.Appzia.enclosure.activities.MainActivityVoiceCall {
    public void triggerEndCallButton();
    private void cleanupAndExit();
    protected void onDestroy();
}

# -----------------------
# BroadcastReceiver / Services
# -----------------------
-keepclassmembers class * extends android.content.BroadcastReceiver {
    public void onReceive(android.content.Context, android.content.Intent);
}
-keepclassmembers class * extends android.app.Service {
    public void stopSelf();
    public void stopSelf(int);
    public boolean stopSelfResult(int);
    public void stopForeground(boolean);
    public int onStartCommand(android.content.Intent, int, int);
    public void onDestroy();
    public android.os.IBinder onBind(android.content.Intent);
}

# -----------------------
# LocalBroadcastManager
# -----------------------
-keep class androidx.localbroadcastmanager.content.LocalBroadcastManager { *; }
-keepclassmembers class androidx.localbroadcastmanager.content.LocalBroadcastManager {
    public boolean sendBroadcast(android.content.Intent);
    public void registerReceiver(android.content.BroadcastReceiver, android.content.IntentFilter);
    public void unregisterReceiver(android.content.BroadcastReceiver);
    public static androidx.localbroadcastmanager.content.LocalBroadcastManager getInstance(android.content.Context);
}


# Keep Firebase model classes and their members
-keep class com.Appzia.enclosure.Model.messageModel { *; }
-keepclassmembers class com.Appzia.enclosure.Model.messageModel { *; }

-keep class com.Appzia.enclosure.Model.emojiModel { *; }
-keepclassmembers class com.Appzia.enclosure.Model.emojiModel { *; }

# Preserve equals and hashCode methods for DiffUtil
-keepclassmembers class com.Appzia.enclosure.Model.** {
    public boolean equals(java.lang.Object);
    public int hashCode();
    public java.lang.String toString();
}

# Preserve Objects utility class methods
-keep class java.util.Objects { *; }
-keepclassmembers class java.util.Objects { *; }

# Preserve ArrayList and List operations
-keep class java.util.ArrayList { *; }
-keep class java.util.List { *; }
-keepclassmembers class java.util.ArrayList { *; }
-keepclassmembers class java.util.List { *; }

# Preserve common collection methods
-keepclassmembers class * {
    public void add(java.lang.Object);
    public void addAll(java.util.Collection);
    public void clear();
    public java.lang.Object get(int);
    public int size();
    public boolean isEmpty();
}

# Keep chat adapter classes
-keep class com.Appzia.enclosure.Adapter.chatAdapter { *; }
-keepclassmembers class com.Appzia.enclosure.Adapter.chatAdapter { *; }

-keep class com.Appzia.enclosure.Adapter.groupChatAdapter { *; }
-keepclassmembers class com.Appzia.enclosure.Adapter.groupChatAdapter { *; }

# Keep DiffUtil callback classes
-keep class com.Appzia.enclosure.Adapter.chatAdapter$MessageDiffCallback { *; }
-keepclassmembers class com.Appzia.enclosure.Adapter.chatAdapter$MessageDiffCallback { *; }

-keep class com.Appzia.enclosure.Adapter.groupChatAdapter$MessageDiffCallback { *; }
-keepclassmembers class com.Appzia.enclosure.Adapter.groupChatAdapter$MessageDiffCallback { *; }

# Keep ViewHolder classes for chat
-keep class com.Appzia.enclosure.Adapter.viewholders.SenderViewHolder { *; }
-keep class com.Appzia.enclosure.Adapter.viewholders.ReceiverViewHolder { *; }
-keepclassmembers class com.Appzia.enclosure.Adapter.viewholders.SenderViewHolder { *; }
-keepclassmembers class com.Appzia.enclosure.Adapter.viewholders.ReceiverViewHolder { *; }

# If you have other POJOs read/written by Firebase, keep them too
# -keep class com.Appzia.enclosure.Model.** { *; }
# -keepclassmembers class com.Appzia.enclosure.Model.** { *; }

# Keep Constant keys if accessed via reflection
-keep class com.Appzia.enclosure.Utils.Constant { *; }
-keepclassmembers class com.Appzia.enclosure.Utils.Constant { *; }
-keep class **.R$drawable { *; }


# -----------------------
# Temporary: disable obfuscation for debugging
# -----------------------
-dontobfuscate
