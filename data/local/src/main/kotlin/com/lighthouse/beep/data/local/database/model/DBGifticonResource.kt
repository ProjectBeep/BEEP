package com.lighthouse.beep.data.local.database.model

import android.net.Uri
import androidx.room.ColumnInfo

internal data class DBGifticonResource(
    @ColumnInfo(name = "gifticon_uri") val gifticonUri: Uri,
    @ColumnInfo(name = "thumbnail_uri") val thumbnailUri: Uri?,
)