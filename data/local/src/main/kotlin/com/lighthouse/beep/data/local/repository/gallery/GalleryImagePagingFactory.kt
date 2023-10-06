package com.lighthouse.beep.data.local.repository.gallery

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lighthouse.beep.model.gallery.GalleryImage

internal class GalleryImagePagingFactory(
    private val dataSource: GalleryDataSource,
    private val page: Int,
    private val limit: Int,
) : PagingSource<Int, GalleryImage>() {

    override fun getRefreshKey(state: PagingState<Int, GalleryImage>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GalleryImage> {
        val current = params.key ?: page
        val results = dataSource.getImages(current, params.loadSize, 0)
        return try {
            LoadResult.Page(
                data = results,
                prevKey = null,
                nextKey = if (results.size < params.loadSize) null else current + (params.loadSize / limit),
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
