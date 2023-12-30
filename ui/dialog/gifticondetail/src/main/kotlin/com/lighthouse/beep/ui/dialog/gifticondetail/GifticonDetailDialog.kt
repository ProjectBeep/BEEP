package com.lighthouse.beep.ui.dialog.gifticondetail

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.core.ui.exts.repeatOnStarted
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.core.ui.exts.show
import com.lighthouse.beep.library.barcode.BarcodeGenerator
import com.lighthouse.beep.library.textformat.TextInputFormat
import com.lighthouse.beep.model.gifticon.GifticonThumbnail
import com.lighthouse.beep.navs.AppNavigator
import com.lighthouse.beep.theme.R as ThemeR
import com.lighthouse.beep.ui.dialog.confirmation.ConfirmationDialog
import com.lighthouse.beep.ui.dialog.confirmation.ConfirmationParam
import com.lighthouse.beep.ui.dialog.gifticondetail.databinding.DialogGifticonDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.lighthouse.beep.ui.dialog.gifticondetail.usecash.GifticonUseCashDialog
import com.lighthouse.beep.ui.dialog.gifticondetail.usecash.GifticonUseCashParam
import kotlinx.coroutines.flow.filterNotNull
import java.util.Date

@AndroidEntryPoint
class GifticonDetailDialog : DialogFragment() {

    companion object {
        const val TAG = "GifticonDetail"
    }

    private var _binding: DialogGifticonDetailBinding? = null
    private val binding: DialogGifticonDetailBinding
        get() = requireNotNull(_binding)

    private val viewModel by viewModels<GifticonDetailViewModel>()

    private val balanceFormat = TextInputFormat.BALANCE
    private val barcodeFormat = TextInputFormat.BARCODE

    @Inject
    lateinit var navigator: AppNavigator

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
        setUpCollectState()
        setUpOnClickEvent()
    }

    private fun setUpCollectState() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.requestDismissEvent.collect {
                dismiss()
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.gifticonDetail.filterNotNull().collect {
                when (val thumbnail = it.thumbnail) {
                    is GifticonThumbnail.Image -> {
                        Glide.with(this)
                            .load(thumbnail.uri)
                            .into(binding.imageThumbnail)
                    }

                    is GifticonThumbnail.BuildIn -> {
                        Glide.with(this)
                            .load(thumbnail.icon.largeIconRes)
                            .into(binding.imageThumbnail)
                    }
                }

                binding.containerCheck.isVisible = it.isUsed
                when {
                    it.isUsed -> binding.textCheck.setText(R.string.dialog_gifticon_detail_thumbnail_used)
                    Date() > it.expireAt -> binding.textCheck.setText(R.string.dialog_gifticon_detail_thumbnail_expired)
                    else -> Unit
                }

                binding.textBrand.text = it.displayBrand
                binding.textName.text = it.name

                binding.groupCash.isVisible = it.isCashCard
                binding.textRemainCash.text = balanceFormat.valueToTransformed(it.remainCash.toString())
                binding.textTotalCash.text = balanceFormat.valueToTransformed(it.totalCash.toString())

                binding.textExpire.text = it.formattedExpiredDate
                binding.textMemo.text = it.memo

                val image = BarcodeGenerator.loadBarcode(it.barcode, 300.dp, 80.dp)
                binding.imageBarcode.setImageBitmap(image)
                binding.textBarcode.text = barcodeFormat.valueToTransformed(it.barcode)

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
            // navigator 로 Editor 페이지로 이동하기
            dismiss()
        }

        binding.btnClose.setOnThrottleClickListener {
            dismiss()
        }

        binding.btnDelete.setOnThrottleClickListener {
            showDeleteDialog()
        }

        binding.btnUseAndRevert.setOnThrottleClickListener {
            when {
                viewModel.isUsed -> viewModel.revertUseGifticon()
                viewModel.isCashCard -> showUseCashDialog()
                else -> viewModel.useGifticon()
            }
        }
    }

    private fun fadeInDetail() {
        binding.root.isEnabled = true
        binding.root.animate()
            .setDuration(300)
            .alpha(1f)
            .start()
    }

    private fun fadeOutDetail() {
        binding.root.isEnabled = false
        binding.root.animate()
            .setDuration(300)
            .alpha(0f)
            .start()
    }

    private fun showDeleteDialog() {
        fadeOutDetail()

        show(ConfirmationDialog.TAG) {
            val param = ConfirmationParam(
                messageResId = R.string.dialog_gifticon_detail_delete_message,
                cancelTextResId = R.string.dialog_gifticon_detail_delete_cancel,
                okTextResId = R.string.dialog_gifticon_detail_delete_ok,
            )
            ConfirmationDialog.newInstance(param).apply {
                setOnDismissListener {
                    fadeInDetail()
                }
                setOnOkClickListener {
                    viewModel.deleteGifticon()
                }
            }
        }
    }

    private fun showUseCashDialog() {
        fadeOutDetail()
        show(GifticonUseCashDialog.TAG) {
            val param = GifticonUseCashParam(viewModel.remainCash)
            GifticonUseCashDialog.newInstance(param).apply {
                setOnDismissListener {
                    fadeInDetail()
                }
                setOnUseCashListener {
                    viewModel.useCash(it)
                }
            }
        }
    }
}