package com.lighthouse.beep.model.gallery.json

import android.net.Uri
import com.lighthouse.beep.model.gallery.GalleryImage
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.Types
import java.util.Date

class GalleryImageJsonAdapter : JsonAdapter<GalleryImage>() {
    @FromJson
    override fun fromJson(reader: JsonReader): GalleryImage? {
        return GalleryImage(
            reader.nextLong(),
            Uri.parse(reader.nextString()),
            reader.nextString(),
            Date(reader.nextLong()),
        )
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: GalleryImage?) {
        writer.value(value?.id)
        writer.value(value?.contentUri?.toString())
        writer.value(value?.imagePath)
        writer.value(value?.dateAdded?.time)
    }
}

internal fun getGalleryImageListAdapter(): JsonAdapter<List<GalleryImage>> {
    val moshi = Moshi.Builder()
        .add(GalleryImageJsonAdapter())
        .build()
    val galleryImages = Types.newParameterizedType(List::class.java, GalleryImage::class.java)
    return moshi.adapter(galleryImages)
}

fun List<GalleryImage>.toJson(): String {
    val jsonAdapter = getGalleryImageListAdapter()
    return jsonAdapter.toJson(this)
}