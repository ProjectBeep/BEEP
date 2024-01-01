package com.lighthouse.beep.ui.feature.archive

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.core.ui.exts.repeatOnStarted
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.core.ui.exts.show
import com.lighthouse.beep.core.ui.exts.viewHeight
import com.lighthouse.beep.core.ui.recyclerview.GridCalculator
import com.lighthouse.beep.ui.designsystem.snackbar.BeepSnackBar
import com.lighthouse.beep.ui.dialog.gifticondetail.GifticonDetailDialog
import com.lighthouse.beep.ui.dialog.gifticondetail.GifticonDetailListener
import com.lighthouse.beep.ui.dialog.gifticondetail.GifticonDetailParam
import com.lighthouse.beep.ui.feature.archive.databinding.ActivityArchiveBinding
import com.lighthouse.beep.ui.feature.archive.list.UsedGifticonAdapter
import com.lighthouse.beep.ui.feature.archive.list.UsedGifticonListener
import com.lighthouse.beep.ui.feature.archive.model.GifticonViewMode
import com.lighthouse.beep.ui.feature.archive.model.UsedGifticonItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
internal class ArchiveActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArchiveBinding

    private val viewModel by viewModels<ArchiveViewModel>()

    private val requestManager by lazy {
        Glide.with(this)
    }

    private val beepSnackBar by lazy {
        BeepSnackBar.Builder(this)
            .setLifecycleOwner(this)
            .setRootView(binding.root)
    }

    private val usedGifticonAdapter by lazy {
        UsedGifticonAdapter(
            requestManager = requestManager,
            usedGifticonListener = usedGifticonListener,
        )
    }

    private val usedGifticonListener = object : UsedGifticonListener {
        override fun isSelectedFlow(item: UsedGifticonItem): Flow<Boolean> {
            return viewModel.selectedGifticonListFlow
                .map { list -> list.find { it.id == item.id } != null }
                .distinctUntilChanged()
        }

        override fun getViewModeFlow(): Flow<GifticonViewMode> {
            return viewModel.gifticonViewMode
        }

        override fun onClick(item: UsedGifticonItem) {
            when (viewModel.gifticonViewMode.value) {
                GifticonViewMode.VIEW -> showGifticonDetail(item)
                GifticonViewMode.EDIT -> viewModel.selectGifticon(item)
            }
        }
    }

    private fun showGifticonDetail(item: UsedGifticonItem) {
        show(GifticonDetailDialog.TAG) {
            val param = GifticonDetailParam(item.id)
            GifticonDetailDialog.newInstance(param).apply {
                setGifticonDetailListener(object : GifticonDetailListener {
                    override fun onDeleteGifticon() {
                        beepSnackBar.info()
                            .setTextResId(R.string.archive_delete_gifticon_message)
                            .show()
                    }

                    override fun onUseGifticon() = Unit

                    override fun onUseCash() = Unit

                    override fun onRevertGifticon() {
                        beepSnackBar.info()
                            .setTextResId(R.string.archive_revert_gifticon_message)
                            .show()
                    }
                })
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArchiveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpGridUsedGifticon()
        setUpCollectState()
        setUpOnClickEvent()
    }

    private fun setUpGridUsedGifticon() {
        binding.gridUsedGifticon.adapter = usedGifticonAdapter
        val spanCount = GridCalculator.getSpanCount(172.dp, 17.dp, 48.dp, 2)
        binding.gridUsedGifticon.layoutManager =
            GridLayoutManager(this, spanCount)
    }

    private fun setUpCollectState() {
        repeatOnStarted {
            viewModel.usedGifticonList.collect {
                if (it.isEmpty()) {
                    finish()
                } else {
                    binding.textUsedGifticonCount.text = it.size.toString()
                    usedGifticonAdapter.submitList(it)
                }
            }
        }

        repeatOnStarted {
            var init = true
            viewModel.gifticonViewMode.collect { mode ->
                when (mode) {
                    GifticonViewMode.VIEW -> {
                        binding.textEdit.setText(R.string.archive_edit_gifticon)
                        binding.iconEdit.setImageResource(com.lighthouse.beep.theme.R.drawable.icon_edit)
                    }

                    GifticonViewMode.EDIT -> {
                        binding.textEdit.setText(R.string.archive_edit_cancel)
                        binding.iconEdit.setImageResource(com.lighthouse.beep.theme.R.drawable.icon_edit_cancel)
                    }
                }

                val editParams = binding.containerEdit.layoutParams as? ViewGroup.MarginLayoutParams
                val bottomMarginEdit = editParams?.bottomMargin ?: 0
                val startEdit = binding.containerEdit.translationY
                val endEdit = when (mode) {
                    GifticonViewMode.VIEW -> 0
                    GifticonViewMode.EDIT -> -binding.containerEdit.viewHeight - bottomMarginEdit
                }

                if (!init) {
                    ValueAnimator.ofFloat(0f, 1f).apply {
                        duration = 300L
                        interpolator = DecelerateInterpolator()
                        addUpdateListener {
                            binding.containerEdit.translationY =
                                startEdit - (startEdit - endEdit) * it.animatedFraction
                        }
                    }.start()
                } else {
                    binding.containerEdit.translationY = endEdit.toFloat()
                }
                init = false
            }
        }
    }

    private fun setUpOnClickEvent() {
        binding.btnBack.setOnThrottleClickListener {
            finish()
        }

        binding.btnEdit.setOnThrottleClickListener {
            viewModel.toggleGifticonViewModel()
        }

        binding.btnDelete.setOnThrottleClickListener {
            val selectedCount = viewModel.selectedCount
            viewModel.deleteSelectedGifticon()
            beepSnackBar.info()
                .setText(getString(R.string.archive_delete_multi_gifticon_message, selectedCount))
                .show()
        }

        binding.btnUse.setOnThrottleClickListener {
            val selectedCount = viewModel.selectedCount
            viewModel.revertSelectedGifticon()
            beepSnackBar.info()
                .setText(getString(R.string.archive_revert_multi_gifticon_message, selectedCount))
                .show()
        }
    }
}