package com.lighthouse.beep.data.local.utils

/**
 * WiFi QR 코드 생성 및 파싱을 위한 유틸리티 클래스
 * 
 * WiFi QR 코드 형식: WIFI:T:WPA;S:NetworkName;P:Password;H:false;;
 */
object WifiQrUtils {

    /**
     * WiFi QR 코드를 생성합니다.
     * 
     * @param ssid 네트워크 이름
     * @param password 비밀번호
     * @param securityType 보안 타입 (WPA, WPA2, WEP, nopass)
     * @param isHidden 숨김 네트워크 여부
     * @return WiFi QR 코드 문자열
     */
    fun generateWifiQr(
        ssid: String,
        password: String,
        securityType: String = "WPA",
        isHidden: Boolean = false
    ): String {
        val escapedSsid = escapeSpecialChars(ssid)
        val escapedPassword = escapeSpecialChars(password)
        val hiddenFlag = if (isHidden) "true" else "false"
        
        return "WIFI:T:$securityType;S:$escapedSsid;P:$escapedPassword;H:$hiddenFlag;;"
    }

    /**
     * WiFi QR 코드를 파싱합니다.
     * 
     * @param qrCode WiFi QR 코드 문자열
     * @return 파싱된 WiFi 정보, 실패 시 null
     */
    fun parseWifiQr(qrCode: String): WifiInfo? {
        if (!qrCode.startsWith("WIFI:")) {
            return null
        }

        try {
            val components = mutableMapOf<String, String>()
            
            // WIFI: 제거하고 각 컴포넌트 파싱
            val content = qrCode.removePrefix("WIFI:")
            val parts = content.split(";")
            
            for (part in parts) {
                if (part.contains(":")) {
                    val (key, value) = part.split(":", limit = 2)
                    components[key] = unescapeSpecialChars(value)
                }
            }

            return WifiInfo(
                ssid = components["S"] ?: "",
                password = components["P"] ?: "",
                securityType = components["T"] ?: "WPA",
                isHidden = components["H"] == "true"
            )
        } catch (e: Exception) {
            return null
        }
    }

    /**
     * WiFi QR 코드인지 확인합니다.
     */
    fun isWifiQr(qrCode: String): Boolean {
        return qrCode.startsWith("WIFI:")
    }

    /**
     * 특수 문자를 이스케이프 처리합니다.
     */
    private fun escapeSpecialChars(input: String): String {
        return input.replace("\\", "\\\\")
            .replace(",", "\\,")
            .replace(";", "\\;")
            .replace(":", "\\:")
            .replace("\"", "\\\"")
    }

    /**
     * 이스케이프된 특수 문자를 복원합니다.
     */
    private fun unescapeSpecialChars(input: String): String {
        return input.replace("\\\\", "\\")
            .replace("\\,", ",")
            .replace("\\;", ";")
            .replace("\\:", ":")
            .replace("\\\"", "\"")
    }

    /**
     * WiFi 정보를 담는 데이터 클래스
     */
    data class WifiInfo(
        val ssid: String,
        val password: String,
        val securityType: String,
        val isHidden: Boolean
    ) {
        fun isValid(): Boolean {
            return ssid.isNotEmpty() && 
                   securityType.isNotEmpty() &&
                   (securityType == "nopass" || password.isNotEmpty())
        }
    }

    /**
     * 지원하는 보안 타입들
     */
    object SecurityType {
        const val WPA = "WPA"
        const val WPA2 = "WPA2" 
        const val WEP = "WEP"
        const val NONE = "nopass"
    }
}