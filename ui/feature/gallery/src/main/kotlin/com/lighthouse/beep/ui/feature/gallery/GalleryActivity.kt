package com.lighthouse.beep.ui.feature.gallery

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewPropertyAnimator
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.core.ui.animation.SimpleAnimatorListener
import com.lighthouse.beep.core.ui.decoration.GridItemDecoration
import com.lighthouse.beep.core.ui.exts.repeatOnStarted
import com.lighthouse.beep.ui.feature.gallery.adapter.gallery.GalleryAllAdapter
import com.lighthouse.beep.ui.feature.gallery.adapter.gallery.GalleryRecommendAdapter
import com.lighthouse.beep.ui.feature.gallery.databinding.ActivityGalleryBinding
import com.lighthouse.beep.ui.feature.gallery.model.BucketType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class GalleryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGalleryBinding

    private val viewModel by viewModels<GalleryViewModel>()

    private val galleryRecommendAdapter = GalleryRecommendAdapter()

    private val galleryAllAdapter = GalleryAllAdapter()

    private val recommendScrollListener = object: RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val manager = recyclerView.layoutManager as? GridLayoutManager ?: return
            val lastVisible = manager.findLastVisibleItemPosition()
            viewModel.requestNext(lastVisible)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setUpBucketTypeTab()
        setUpGalleryList()
        setUpCollectState()
    }

    private fun setUpBucketTypeTab() {
        binding.tabBucketType.addOnTabSelectedListener(object: OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab ?: return
                viewModel.setBucketType(BucketType.entries[tab.position])
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
            override fun onTabReselected(tab: TabLayout.Tab?) = Unit
        })

        BucketType.entries.forEach {
            val tab = binding.tabBucketType.newTab().apply {
                setText(it.titleRes)
            }
            binding.tabBucketType.addTab(tab)
        }
    }

    private fun setUpGalleryList() {
        binding.listGallery.layoutManager = GridLayoutManager(this, GalleryViewModel.spanCount)
        binding.listGallery.addItemDecoration(GridItemDecoration(4f.dp))
    }

    private fun setUpCollectState() {
        repeatOnStarted {
            viewModel.bucketType.collect { type ->
                binding.listGallery.stopScroll()
                binding.listGallery.clearOnScrollListeners()
                when(type) {
                    BucketType.RECOMMEND -> {
                        binding.listGallery.adapter = galleryRecommendAdapter
                        binding.listGallery.addOnScrollListener(recommendScrollListener)
                        viewModel.requestNext()
                    }
                    BucketType.ALL -> {
                        binding.listGallery.adapter = galleryAllAdapter
                    }
                }
            }
        }

        repeatOnStarted {
            viewModel.recommendList.collect {
                galleryRecommendAdapter.submitList(it)
            }
        }

        repeatOnStarted {
            viewModel.allList.collect {
                galleryAllAdapter.submitData(it)
            }
        }

        repeatOnStarted {
            var animator: ViewPropertyAnimator? = null
            viewModel.showRecognizeProgress.collect { isShow ->
                val translationY = if (isShow) {
                    - binding.progressRecognize.height - 150f.dp
                } else {
                    0f.dp
                }
                animator?.cancel()
                animator = binding.progressRecognize.animate()
                    .translationY(translationY)
                    .setDuration(300)
                    .setListener(object: SimpleAnimatorListener(){
                        override fun onAnimationStart(animator: Animator) {
                            binding.progressRecognize.isVisible = true
                        }

                        override fun onAnimationEnd(animator: Animator) {
                            binding.progressRecognize.isVisible = isShow
                        }

                        override fun onAnimationCancel(animator: Animator) {
                            binding.progressRecognize.isVisible = isShow
                        }
                    })
                animator?.start()
            }
        }
    }
}