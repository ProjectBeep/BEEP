package com.lighthouse.beep.model.gifticon

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.DiffUtil
import com.lighthouse.beep.model.R

@Suppress("unused")
enum class GifticonBuiltInThumbnail(
    @DrawableRes val iconRes: Int,
    @StringRes val titleRes: Int,
) {
    COFFEE(
        iconRes = R.drawable.icon_thumbnail_coffee,
        titleRes = R.string.title_thumbnail_coffee,
    ),
    CHICKEN(
        iconRes = R.drawable.icon_thumbnail_chicken,
        titleRes = R.string.title_thumbnail_chicken,
    ),
    FAST_FOOD(
        iconRes = R.drawable.icon_thumbnail_fast_food,
        titleRes = R.string.title_thumbnail_fast_food,
    ),
    SHOP(
        iconRes = R.drawable.icon_thumbnail_shop,
        titleRes = R.string.title_thumbnail_shop,
    ),
    DIGITAL(
        iconRes = R.drawable.icon_thumbnail_digital,
        titleRes = R.string.title_thumbnail_digital,
    ),
    ENTERTAINMENT(
        iconRes = R.drawable.icon_thumbnail_entertainment,
        titleRes = R.string.title_thumbnail_entertainment,
    ),
    FOOD(
        iconRes = R.drawable.icon_thumbnail_food,
        titleRes = R.string.title_thumbnail_food,
    ),
    SPORTS(
        iconRes = R.drawable.icon_thumbnail_sports,
        titleRes = R.string.title_thumbnail_sports,
    ),
    FASHION(
        iconRes = R.drawable.icon_thumbnail_fashion,
        titleRes = R.string.title_thumbnail_fashion,
    ),
    STATIONERY(
        iconRes = R.drawable.icon_thumbnail_stationery,
        titleRes = R.string.title_thumbnail_stationery,
    ),
    LIVING(
        iconRes = R.drawable.icon_thumbnail_living,
        titleRes = R.string.title_thumbnail_living,
    ),
    ETC(
        iconRes = R.drawable.icon_thumbnail_etc,
        titleRes = R.string.title_thumbnail_etc,
    );
}

class GifticonBuiltInThumbnailDiff : DiffUtil.ItemCallback<GifticonBuiltInThumbnail>() {
    override fun areItemsTheSame(
        oldItem: GifticonBuiltInThumbnail,
        newItem: GifticonBuiltInThumbnail
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: GifticonBuiltInThumbnail,
        newItem: GifticonBuiltInThumbnail
    ): Boolean {
        return oldItem == newItem
    }
}