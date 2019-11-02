# glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-dontnote retrofit2.Platform
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions
-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**
-dontwarn sun.misc.Unsafe
-dontwarn javax.annotation.**
-ignorewarnings
-keep class * {
    public private *;
}
-keepnames class * { @butterknife.Bind *;}
-dontwarn com.google.android.gms.**

-keep class com.firebase.** { *; }
-dontwarn com.google.firebase.messaging.RemoteMessage
-keep class com.google.firebase.messaging.RemoteMessage

-keep public class com.google.firebase.analytics.FirebaseAnalytics {
    public *;
}
-keep public class com.google.android.gms.measurement.AppMeasurement {
    public *;
}
-keepclasseswithmembernames class * {
    native <methods>;
}
-keep class android.support.v8.renderscript.** { *; }

# facebook
-keep class com.facebook.FacebookSdk {
   boolean isInitialized();
}
-keep class com.facebook.appevents.AppEventsLogger {
   com.facebook.appevents.AppEventsLogger newLogger(android.content.Context);
   void logSdkEvent(java.lang.String, java.lang.Double, android.os.Bundle);
}