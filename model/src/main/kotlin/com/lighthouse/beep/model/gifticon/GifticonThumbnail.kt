package com.lighthouse.beep.model.gifticon

import android.graphics.Rect
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.DiffUtil
import com.lighthouse.beep.model.R

sealed interface GifticonThumbnail {

    companion object {
        const val TYPE_BUILT_IN = "BuiltIn"
        const val TYPE_IMAGE = "Image"
    }

    val type: String

    data class BuildIn(val icon: GifticonBuiltInThumbnail): GifticonThumbnail {

        constructor(code: String): this(GifticonBuiltInThumbnail.of(code))

        override val type: String = TYPE_BUILT_IN
    }

    data class Image(val uri: Uri, val rect: Rect): GifticonThumbnail {

        override val type: String = TYPE_IMAGE
    }
}

@Suppress("unused")
enum class GifticonBuiltInThumbnail(
    val code: String,
    @DrawableRes val iconRes: Int,
    @DrawableRes val smallIconRes: Int,
    @DrawableRes val largeIconRes: Int,
    @StringRes val titleRes: Int,
) {
    COFFEE(
        code = "coffee",
        iconRes = R.drawable.icon_thumbnail_coffee,
        smallIconRes = R.drawable.icon_small_thumbnail_coffee,
        largeIconRes = R.drawable.icon_large_thumbnail_coffee,
        titleRes = R.string.title_thumbnail_coffee,
    ),
    CHICKEN(
        code = "chicken",
        iconRes = R.drawable.icon_thumbnail_chicken,
        smallIconRes = R.drawable.icon_small_thumbnail_chicken,
        largeIconRes = R.drawable.icon_large_thumbnail_chicken,
        titleRes = R.string.title_thumbnail_chicken,
    ),
    FAST_FOOD(
        code = "fast_food",
        iconRes = R.drawable.icon_thumbnail_fast_food,
        smallIconRes = R.drawable.icon_small_thumbnail_fast_food,
        largeIconRes = R.drawable.icon_large_thumbnail_fast_food,
        titleRes = R.string.title_thumbnail_fast_food,
    ),
    SHOP(
        code = "shop",
        iconRes = R.drawable.icon_thumbnail_shop,
        smallIconRes = R.drawable.icon_small_thumbnail_shop,
        largeIconRes = R.drawable.icon_large_thumbnail_shop,
        titleRes = R.string.title_thumbnail_shop,
    ),
    DIGITAL(
        code = "digital",
        iconRes = R.drawable.icon_thumbnail_digital,
        smallIconRes = R.drawable.icon_small_thumbnail_digital,
        largeIconRes = R.drawable.icon_large_thumbnail_digital,
        titleRes = R.string.title_thumbnail_digital,
    ),
    ENTERTAINMENT(
        code = "entertainment",
        iconRes = R.drawable.icon_thumbnail_entertainment,
        smallIconRes = R.drawable.icon_small_thumbnail_entertainment,
        largeIconRes = R.drawable.icon_large_thumbnail_entertainment,
        titleRes = R.string.title_thumbnail_entertainment,
    ),
    FOOD(
        code = "food",
        iconRes = R.drawable.icon_thumbnail_food,
        smallIconRes = R.drawable.icon_small_thumbnail_food,
        largeIconRes = R.drawable.icon_large_thumbnail_food,
        titleRes = R.string.title_thumbnail_food,
    ),
    SPORTS(
        code = "sports",
        iconRes = R.drawable.icon_thumbnail_sports,
        smallIconRes = R.drawable.icon_small_thumbnail_sports,
        largeIconRes = R.drawable.icon_large_thumbnail_sports,
        titleRes = R.string.title_thumbnail_sports,
    ),
    FASHION(
        code = "fashion",
        iconRes = R.drawable.icon_thumbnail_fashion,
        smallIconRes = R.drawable.icon_small_thumbnail_fashion,
        largeIconRes = R.drawable.icon_large_thumbnail_fashion,
        titleRes = R.string.title_thumbnail_fashion,
    ),
    STATIONERY(
        code = "stationery",
        iconRes = R.drawable.icon_thumbnail_stationery,
        smallIconRes = R.drawable.icon_small_thumbnail_stationery,
        largeIconRes = R.drawable.icon_large_thumbnail_stationery,
        titleRes = R.string.title_thumbnail_stationery,
    ),
    LIVING(
        code = "living",
        iconRes = R.drawable.icon_thumbnail_living,
        smallIconRes = R.drawable.icon_small_thumbnail_living,
        largeIconRes = R.drawable.icon_large_thumbnail_living,
        titleRes = R.string.title_thumbnail_living,
    ),
    ETC(
        code = "etc",
        iconRes = R.drawable.icon_thumbnail_etc,
        smallIconRes = R.drawable.icon_small_thumbnail_etc,
        largeIconRes = R.drawable.icon_large_thumbnail_etc,
        titleRes = R.string.title_thumbnail_etc,
    );

    companion object {
        fun of(code: String): GifticonBuiltInThumbnail {
            return runCatching {
                entries.find { it.code == code } ?: ETC
            }.getOrDefault(ETC)
        }
    }
}

class GifticonBuiltInThumbnailDiff : DiffUtil.ItemCallback<GifticonBuiltInThumbnail>() {
    override fun areItemsTheSame(
        oldItem: GifticonBuiltInThumbnail,
        newItem: GifticonBuiltInThumbnail
    ): Boolean {
        return oldItem.code == newItem.code
    }

    override fun areContentsTheSame(
        oldItem: GifticonBuiltInThumbnail,
        newItem: GifticonBuiltInThumbnail
    ): Boolean {
        return oldItem.code == newItem.code
    }
}