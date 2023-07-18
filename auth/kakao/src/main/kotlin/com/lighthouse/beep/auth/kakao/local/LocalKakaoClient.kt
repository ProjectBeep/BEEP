package com.lighthouse.beep.auth.kakao.local

import androidx.compose.runtime.compositionLocalOf
import com.lighthouse.beep.auth.kakao.KakaoClient

val LocalKakaoClient = compositionLocalOf<KakaoClient> {
    error("no provide kakao client")
}
