package com.lighthouse.beep.model.deviceconfig

import android.net.Uri
import com.lighthouse.beep.model.serializer.UriSerializer
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeviceConfig(
    val authInfo: AuthInfo = AuthInfo(
        userUid = "",
        provider = AuthProvider.NONE,
        displayName = "",
        email = "",
        photoUrl = Uri.EMPTY,
    ),
    val hash: RecentHash = RecentHash(
        backup = "",
    ),
    val shownGuidePage: ShownGuidePage = ShownGuidePage(
        permission = false,
    ),
    val subscription: Subscription = Subscription(
        paidDateCount = 0,
        freeDateCount = 0,
        subscriptionDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    ),
    val security: Security = Security(
        option = SecurityOption.None,
        subOption = SecurityOption.None,
    ),
    val themeOption: ThemeOption = ThemeOption.SYSTEM,
)

@Serializable
data class AuthInfo(
    val userUid: String = "",
    val provider: AuthProvider = AuthProvider.NONE,
    val displayName: String = "",
    val email: String = "",
    @Serializable(with = UriSerializer::class)
    val photoUrl: Uri? = Uri.EMPTY,
)

enum class AuthProvider(
    private val firebaseName: String,
) {
    @SerialName("none")
    NONE(firebaseName = "none"),

    @SerialName("naver")
    NAVER(firebaseName = "naver"),

    @SerialName("kakao")
    KAKAO(firebaseName = "kakao"),

    @SerialName("google")
    GOOGLE(firebaseName = "google"),

    @SerialName("guest")
    GUEST(firebaseName = "anonymous"),
    ;

    companion object {
        fun of(name: String): AuthProvider {
            return values().find {
                it.firebaseName == name
            } ?: NONE
        }
    }
}

@Serializable
data class RecentHash(
    val backup: String,
)

@Serializable
data class ShownGuidePage(
    val permission: Boolean,
)

@Serializable
data class Subscription(
    val paidDateCount: Int,
    val freeDateCount: Int,
    val subscriptionDate: LocalDateTime,
)

@Serializable
data class Security(
    val option: SecurityOption,
    val subOption: SecurityOption,
)

sealed interface SecurityOption {

    @Serializable
    object None : SecurityOption

    @Serializable
    data class Pin(val password: String) : SecurityOption

    @Serializable
    data class Pattern(val password: String) : SecurityOption

    @Serializable
    object FingerPrint : SecurityOption
}

enum class ThemeOption {
    @SerialName("system")
    SYSTEM,

    @SerialName("light")
    LIGHT,

    @SerialName("dark")
    DARK,
}
