package com.lighthouse.beep.model.gifticon

enum class GifticonBarcodeType {
    QR_CODE,
    CODE_128;

    companion object {
        fun of(name: String): GifticonBarcodeType {
            return runCatching {
                entries.find { it.name == name } ?: CODE_128
            }.getOrDefault(CODE_128)
        }
    }
}