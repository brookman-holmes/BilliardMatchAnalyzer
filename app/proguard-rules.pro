# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class id to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# ButterKnife 7

-keepattributes Signature

-keep class butterknife.** { *; }
-keep class **$$ViewBinder { *; }
-keep class .R
-keep class com.github.mikephil.charting.** { *; }
-keep class **.R$* {
    <fields>;
}

-keepclasseswithmembernames class * { @butterknife.* <fields>; }
-keepclasseswithmembernames class * { @butterknife.* <methods>; }

-keepclassmembers class com.brookmanholmes.bma.data.TurnModel {
    *;
}
-keepclassmembers class com.brookmanholmes.bma.data.UserModel {
    *;
}
-keepclassmembers class com.brookmanholmes.bma.data.MatchModel {
     *;
}
-keepclassmembers class com.brookmanholmes.bma.data.AdvStatsModel {
     *;
}
 -keepclassmembers class com.brookmanholmes.billiards.match.Match {
    *;
}

-dontwarn butterknife.internal.**
-dontwarn java.awt.**
-dontwarn okio.**
-dontwarn retrofit2.Platform$Java8

# for the about libaries to include the libraries it needs
