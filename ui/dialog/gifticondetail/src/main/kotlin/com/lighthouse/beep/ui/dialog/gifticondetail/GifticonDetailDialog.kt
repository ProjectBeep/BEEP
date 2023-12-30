package com.lighthouse.beep.ui.dialog.gifticondetail

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.core.ui.exts.repeatOnStarted
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.core.ui.exts.show
import com.lighthouse.beep.library.barcode.BarcodeGenerator
import com.lighthouse.beep.library.textformat.TextInputFormat
import com.lighthouse.beep.model.gifticon.GifticonThumbnail
import com.lighthouse.beep.navs.AppNavigator
import com.lighthouse.beep.ui.designsystem.balloon.BalloonHorizontalDirection
import com.lighthouse.beep.ui.designsystem.balloon.TextBalloonBuilder
import com.lighthouse.beep.theme.R as ThemeR
import com.lighthouse.beep.ui.dialog.confirmation.ConfirmationDialog
import com.lighthouse.beep.ui.dialog.confirmation.ConfirmationParam
import com.lighthouse.beep.ui.dialog.gifticondetail.databinding.DialogGifticonDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.lighthouse.beep.ui.dialog.gifticondetail.usecash.GifticonUseCashDialog
import com.lighthouse.beep.ui.dialog.gifticondetail.usecash.GifticonUseCashParam
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GifticonDetailDialog : DialogFragment() {

    companion object {
        const val TAG = "GifticonDetail"

        fun newInstance(param: GifticonDetailParam): GifticonDetailDialog {
            return GifticonDetailDialog().apply {
                arguments = param.buildBundle()
            }
        }
    }

    private var _binding: DialogGifticonDetailBinding? = null
    private val binding: DialogGifticonDetailBinding
        get() = requireNotNull(_binding)

    private val viewModel by viewModels<GifticonDetailViewModel>()

    private val editBalloon by lazy {
        TextBalloonBuilder(requireContext())
            .setText(R.string.dialog_gifticon_detail_edit_tutorial)
            .setTextAppearanceRes(ThemeR.style.Text_Title7)
            .setTextColorRes(ThemeR.color.white)
            .setLifecycleOwner(viewLifecycleOwner)
            .setPadding(horizontal = 12.dp, vertical = 6.dp)
            .setMargin(right = 46.dp)
            .setRightConstraint(binding.btnClose, BalloonHorizontalDirection.RIGHT)
            .setCornerRadius(16f.dp)
            .setBalloonColorRes(ThemeR.color.beep_pink)
            .setArrowRotation(0)
            .setDismissWhenClickOutside(false)
            .setRootView(binding.root)
            .setOnBalloonClickListener {
                viewModel.setShownGifticonDetailEdit(true)
                gotoEdit()
            }
    }

    private val balanceFormat = TextInputFormat.BALANCE

    @Inject
    lateinit var navigator: AppNavigator

    private var gifticonDetailListener: GifticonDetailListener? = null

    fun setGifticonDetailListener(listener: GifticonDetailListener?) {
        gifticonDetailListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, ThemeR.style.Theme_Dialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.apply {
                setCancelable(false)
                setCanceledOnTouchOutside(false)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogGifticonDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        hideDetail()
        setUpCollectState()
        setUpOnClickEvent()
    }

    private fun setUpCollectState() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.requestDismissEvent.collect {
                dismiss()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.gifticonDetail.filterNotNull().take(1).collect {
                if(!viewModel.isShownGifticonDetailEdit()) {
                    editBalloon.show(binding.textEdit)
                }
                fadeInDetail()
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.gifticonDetail.filterNotNull().collect {
                when (val thumbnail = it.thumbnail) {
                    is GifticonThumbnail.Image -> {
                        Glide.with(this)
                            .load(thumbnail.uri)
                            .transform(CircleCrop())
                            .into(binding.imageThumbnail)
                    }

                    is GifticonThumbnail.BuildIn -> {
                        Glide.with(this)
                            .load(thumbnail.icon.largeIconRes)
                            .transform(CircleCrop())
                            .into(binding.imageThumbnail)
                    }
                }

                binding.containerCheck.isVisible = it.isUsed || it.isExpired
                when {
                    it.isUsed -> binding.textCheck.setText(R.string.dialog_gifticon_detail_thumbnail_used)
                    it.isExpired -> binding.textCheck.setText(R.string.dialog_gifticon_detail_thumbnail_expired)
                    else -> Unit
                }

                binding.textBrand.text = it.displayBrand
                binding.textName.text = it.name

                binding.groupCash.isVisible = it.isCashCard
                val remain = balanceFormat.valueToTransformed(it.remainCash.toString())
                binding.textRemainCash.text =
                    getString(R.string.dialog_gifticon_detail_balance, remain)

                val total = balanceFormat.valueToTransformed(it.totalCash.toString())
                binding.textTotalCash.text =
                    getString(R.string.dialog_gifticon_detail_balance, total)

                binding.textExpire.text = it.formattedExpiredDate
                binding.textMemo.text = it.memo

                val bgColor = ContextCompat.getColor(requireContext(), ThemeR.color.bg)
                val image = BarcodeGenerator.loadBarcode(it.barcode, 300.dp, 80.dp, bgColor)
                binding.imageBarcode.setImageBitmap(image)
                binding.textBarcode.text = it.barcode

                if (it.isUsed) {
                    binding.btnDelete.setText(R.string.dialog_gifticon_detail_delete_used)
                    binding.btnUseAndRevert.setText(R.string.dialog_gifticon_detail_revert_use)
                } else {
                    binding.btnDelete.setText(R.string.dialog_gifticon_detail_delete)
                    binding.btnUseAndRevert.setText(R.string.dialog_gifticon_detail_use)
                }
            }
        }
    }

    private fun setUpOnClickEvent() {
        binding.btnEdit.setOnThrottleClickListener {
            gotoEdit()
        }

        binding.btnClose.setOnThrottleClickListener {
            dismiss()
        }

        binding.btnDelete.setOnThrottleClickListener {
            showDeleteDialog()
        }

        binding.btnUseAndRevert.setOnThrottleClickListener {
            when {
                viewModel.isUsed -> {
                    viewModel.revertUseGifticon()
                    gifticonDetailListener?.onRevertGifticon()
                }
                viewModel.isCashCard -> {
                    showUseCashDialog()
                }
                else -> {
                    viewModel.useGifticon()
                    gifticonDetailListener?.onUseGifticon()
                }
            }
        }
    }

    private fun gotoEdit() {
        // navigator 로 Editor 페이지로 이동하기
        dismiss()
    }

    private fun fadeInDetail() {
        binding.root.isEnabled = true
        binding.root.animate()
            .setDuration(300)
            .setUpdateListener {
                if (isAdded) {
                    binding.btnClose.alpha = it.animatedFraction
                    binding.containerContent.alpha = it.animatedFraction
                }
            }.start()
    }

    private fun hideDetail() {
        binding.root.isEnabled = false
        binding.btnClose.alpha = 0f
        binding.containerContent.alpha = 0f
    }

    private fun showDeleteDialog() {
        hideDetail()

        show(ConfirmationDialog.TAG) {
            val param = ConfirmationParam(
                messageResId = R.string.dialog_gifticon_detail_delete_message,
                cancelTextResId = R.string.dialog_gifticon_detail_delete_cancel,
                okTextResId = R.string.dialog_gifticon_detail_delete_ok,
                windowBackgroundColorResId = 0,
            )
            ConfirmationDialog.newInstance(param).apply {
                setOnDismissListener {
                    fadeInDetail()
                }
                setOnOkClickListener {
                    viewModel.deleteGifticon()
                    gifticonDetailListener?.onDeleteGifticon()
                }
            }
        }
    }

    private fun showUseCashDialog() {
        hideDetail()
        show(GifticonUseCashDialog.TAG) {
            val param = GifticonUseCashParam(viewModel.remainCash)
            GifticonUseCashDialog.newInstance(param).apply {
                setOnDismissListener {
                    fadeInDetail()
                }
                setOnUseCashListener {
                    viewModel.useCash(it)
                    gifticonDetailListener?.onUseCash()
                }
            }
        }
    }
}