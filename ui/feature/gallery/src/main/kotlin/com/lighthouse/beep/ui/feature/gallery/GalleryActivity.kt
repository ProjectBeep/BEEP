package com.lighthouse.beep.ui.feature.gallery

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.core.ui.decoration.GridItemDecoration
import com.lighthouse.beep.core.ui.exts.repeatOnStarted
import com.lighthouse.beep.ui.feature.gallery.adapter.gallery.GalleryAdapter
import com.lighthouse.beep.ui.feature.gallery.databinding.ActivityGalleryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class GalleryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGalleryBinding

    private val viewModel by viewModels<GalleryViewModel>()

    private val galleryAdapter = GalleryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setUpGalleryList()
        setUpCollectState()
    }

    private fun setUpGalleryList() {
        val gridLayoutManager = GridLayoutManager(this, viewModel.spanCount)
        binding.listGallery.adapter = galleryAdapter
        binding.listGallery.layoutManager = gridLayoutManager
        binding.listGallery.addItemDecoration(GridItemDecoration(4f.dp))
        binding.listGallery.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = gridLayoutManager.itemCount
                val lastVisible = gridLayoutManager.findLastVisibleItemPosition()
                if (lastVisible >= totalItemCount - viewModel.pageFetchCount) {
                    viewModel.requestNext(totalItemCount + viewModel.pageSize)
                }
            }
        })
        viewModel.requestNext(viewModel.pageSize)
    }

    private fun setUpCollectState() {
        repeatOnStarted {
            viewModel.appendGalleryImages.collect {
                galleryAdapter.appendList(it)
            }
        }
    }
}