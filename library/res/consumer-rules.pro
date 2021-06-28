##---------------Begin: proguard configuration for Glide ----------
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
<init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
**[] $VALUES;
public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
*** rewind();
}

# Uncomment for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule


##---------------Begin: proguard configuration for RxJava ----------
# Uncomment if you use RxJava
#-dontwarn java.util.concurrent.Flow*

# APP

-keep class id.apwdevs.app.res.util.PageType
-keep class id.apwdevs.app.res.fragment.FragmentWithState

-keepattributes InnerClasses
-keep class id.apwdevs.app.res.R
-keep class id.apwdevs.app.res.R$* {
    <fields>;
}
