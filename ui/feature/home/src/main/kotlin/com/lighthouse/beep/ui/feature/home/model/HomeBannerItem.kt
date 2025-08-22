package com.lighthouse.beep.ui.feature.home.model

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager

internal sealed interface HomeBannerItem {

    data class BuiltIn(
        @DrawableRes val resId: Int,
    ) : HomeBannerItem {

        override fun load(requestManager: RequestManager): RequestBuilder<Drawable> {
            return requestManager.load(resId)
        }
    }

    data class Server(
        val imageUrl: String,
    ) : HomeBannerItem {

        override fun load(requestManager: RequestManager): RequestBuilder<Drawable> {
            return requestManager.load(imageUrl)
        }
    }

    fun load(
        requestManager: RequestManager,
    ): RequestBuilder<Drawable>
}

internal fun RequestManager.loadThumbnail(banner: HomeBannerItem): RequestBuilder<Drawable> {
    return banner.load(this)
}

internal class HomeBannerDiff : DiffUtil.ItemCallback<HomeBannerItem>() {
    override fun areItemsTheSame(oldItem: HomeBannerItem, newItem: HomeBannerItem): Boolean {
        return when {
            oldItem === newItem -> true
            oldItem is HomeBannerItem.BuiltIn && newItem is HomeBannerItem.BuiltIn -> oldItem.resId == newItem.resId
            oldItem is HomeBannerItem.Server && newItem is HomeBannerItem.Server -> oldItem.imageUrl == newItem.imageUrl
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: HomeBannerItem, newItem: HomeBannerItem): Boolean {
        return when {
            oldItem is HomeBannerItem.BuiltIn && newItem is HomeBannerItem.BuiltIn -> oldItem.resId == newItem.resId
            oldItem is HomeBannerItem.Server && newItem is HomeBannerItem.Server -> oldItem.imageUrl == newItem.imageUrl
            else -> false
        }
    }
}