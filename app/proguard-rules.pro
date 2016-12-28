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
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# ButterKnife 7

-keep class butterknife.** { *; }
-keep class **$$ViewBinder { *; }
-keep class .R
-keep class com.github.mikephil.charting.** { *; }
-keep class **.R$* {
    <fields>;
}

-keepclasseswithmembernames class * { @butterknife.* <fields>; }
-keepclasseswithmembernames class * { @butterknife.* <methods>; }

-dontwarn butterknife.internal.**
-dontwarn com.github.mikephil.charting.data.realm.**
-dontwarn java.awt.**

# for the about libaries to include the libraries it needs
