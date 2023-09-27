package com.lighthouse.beep.ui.feature.gallery

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewPropertyAnimator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.core.ui.animation.SimpleAnimatorListener
import com.lighthouse.beep.core.ui.decoration.GridItemDecoration
import com.lighthouse.beep.core.ui.decoration.LinearItemDecoration
import com.lighthouse.beep.core.ui.exts.repeatOnStarted
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.ui.feature.gallery.adapter.gallery.GalleryAllAdapter
import com.lighthouse.beep.ui.feature.gallery.adapter.gallery.GalleryRecommendAdapter
import com.lighthouse.beep.ui.feature.gallery.adapter.gallery.OnGalleryListener
import com.lighthouse.beep.ui.feature.gallery.adapter.selected.OnSelectedGalleryListener
import com.lighthouse.beep.ui.feature.gallery.adapter.selected.SelectedGalleryAdapter
import com.lighthouse.beep.ui.feature.gallery.databinding.ActivityGalleryBinding
import com.lighthouse.beep.ui.feature.gallery.model.BucketType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
internal class GalleryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGalleryBinding

    private val viewModel by viewModels<GalleryViewModel>()

    private val onGalleryListener = object: OnGalleryListener {
        override fun getSelectedIndexFlow(item: GalleryImage): Flow<Int> {
            return viewModel.selectedListFlow.map { list ->
                list.indexOfFirst { it.id == item.id }
            }
        }

        override fun onClick(item: GalleryImage) {
            viewModel.selectItem(item)
        }
    }

    private val galleryRecommendAdapter = GalleryRecommendAdapter(onGalleryListener)

    private val galleryAllAdapter = GalleryAllAdapter(onGalleryListener)

    private val onSelectedGalleryListener = object: OnSelectedGalleryListener {
        override fun onClick(item: GalleryImage) {
            viewModel.deleteItem(item)
        }
    }

    private val selectedGalleryAdapter = SelectedGalleryAdapter(
        onSelectedGalleryListener = onSelectedGalleryListener
    )

    private val recommendScrollListener = object: RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val manager = recyclerView.layoutManager as? GridLayoutManager ?: return
            val lastVisible = manager.findLastVisibleItemPosition()
            viewModel.requestRecommendNext(lastVisible)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setUpBucketTypeTab()
        setUpSelectedGalleryList()
        setUpGalleryList()
        setUpCollectState()
    }

    override fun onStop() {
        viewModel.cancelRecommendNext()

        super.onStop()
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

    private fun setUpSelectedGalleryList() {
        binding.listSelected.adapter = selectedGalleryAdapter
        binding.listSelected.setHasFixedSize(true)
        binding.listSelected.addItemDecoration(LinearItemDecoration(1.5f.dp))
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
                        viewModel.requestRecommendNext()
                    }
                    BucketType.ALL -> {
                        binding.listGallery.adapter = galleryAllAdapter
                        viewModel.cancelRecommendNext()
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
                    (-150f).dp
                } else {
                    binding.progressRecognize.height.toFloat()
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
                    })
                animator?.start()
            }
        }

        repeatOnStarted {
            viewModel.selectedListFlow.collect { list ->
                selectedGalleryAdapter.submitList(list.toList())

                binding.textSelectedItemCount.text = if (list.isEmpty()) {
                    getString(R.string.selected_item_empty)
                } else {
                    getString(R.string.selected_item_count, list.size)
                }

                val isListOpen = list.isNotEmpty()
                val start = binding.listSelected.height
                val end = if (isListOpen) resources.getDimension(R.dimen.selected_gallery_list_height).toInt() else 0.dp
                if (start != end) {
                    binding.listSelected.animate()
                        .setDuration(300L)
                        .setUpdateListener {
                            binding.listSelected.updateLayoutParams {
                                height = (start - (start - end) * it.animatedFraction).toInt()
                            }
                        }.setListener(object: SimpleAnimatorListener() {
                            override fun onAnimationStart(animator: Animator) {
                                binding.listSelected.adapter = null
                            }

                            override fun onAnimationEnd(animator: Animator) {
                                binding.listSelected.adapter = selectedGalleryAdapter
                            }
                        }).start()
                }
            }
        }
    }
}