# -----------------------------------------------------------------------
# Artificialss Showcase — ProGuard / R8 rules
# -----------------------------------------------------------------------

# --- Kotlin ---
-keepattributes *Annotation*
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-dontnote kotlin.**

# --- Kotlinx Serialization ---
-keepattributes InnerClasses
-keep,includedescriptorclasses class com.artificialss.showcase.**$$serializer { *; }
-keepclassmembers class com.artificialss.showcase.** {
    *** Companion;
}
-keepclasseswithmembers class com.artificialss.showcase.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-dontwarn kotlinx.serialization.**

# --- Koin ---
-keep class org.koin.** { *; }
-keep class com.artificialss.showcase.di.** { *; }
-dontwarn org.koin.**

# --- Room ---
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao interface *
-keepclassmembers @androidx.room.Entity class * { *; }
-dontwarn androidx.room.**

# --- Apollo ---
-keep class com.artificialss.showcase.graphql.** { *; }
-keep class com.apollographql.** { *; }
-keepclassmembers class com.apollographql.** { *; }
-dontwarn com.apollographql.**

# --- Ktor ---
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**

# --- OkHttp (Ktor Android engine) ---
-dontwarn okhttp3.**
-dontwarn okio.**

# --- Coil ---
-dontwarn coil.**

# --- App domain models (serialized / reflected by Room & Apollo) ---
-keep class com.artificialss.showcase.domain.model.** { *; }
-keep class com.artificialss.showcase.data.local.entity.** { *; }

# --- ViewModels (referenced by Koin via concrete class names) ---
-keep class com.artificialss.showcase.ui.feature.**.** extends androidx.lifecycle.ViewModel { *; }

# --- Compose (R8 handles most internally; keep animation metadata) ---
-dontwarn androidx.compose.**
