package com.lighthouse.auth.google.local

import androidx.compose.runtime.compositionLocalOf
import com.lighthouse.auth.google.GoogleClient

val LocalGoogleClient = compositionLocalOf<GoogleClient> {
    error("no provide google client")
}
