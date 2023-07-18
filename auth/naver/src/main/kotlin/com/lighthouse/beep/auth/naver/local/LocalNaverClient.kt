package com.lighthouse.beep.auth.naver.local

import androidx.compose.runtime.compositionLocalOf
import com.lighthouse.beep.auth.naver.NaverClient

val LocalNaverClient = compositionLocalOf<NaverClient> {
    error("no provide naver client")
}
