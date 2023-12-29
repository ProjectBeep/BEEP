package com.lighthouse.beep.ui.dialog.gifticondetail

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.lighthouse.beep.core.ui.exts.repeatOnStarted
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.navs.AppNavigator
import com.lighthouse.beep.theme.R
import com.lighthouse.beep.ui.dialog.gifticondetail.databinding.DialogGifticonDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GifticonDetailDialog : DialogFragment() {

    companion object {
        const val TAG = "GifticonDetail"
    }

    private var _binding: DialogGifticonDetailBinding? = null
    private val binding: DialogGifticonDetailBinding
        get() = requireNotNull(_binding)

    private val viewModel by viewModels<GifticonDetailViewModel>()

    @Inject
    lateinit var navigator: AppNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.Theme_Dialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.apply {
                setCancelable(false)
                setCanceledOnTouchOutside(false)
            }
            setOnKeyListener { _, keyCode, _ ->
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    viewModel.saveGifticon()
                    true
                } else {
                    false
                }
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
            viewModel.gifticonDetail.collect {

            }
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.isUsed.collect {

            }
        }
    }

    private fun setUpOnClickEvent() {
        binding.btnEdit.setOnThrottleClickListener {
            // navigator 로 Editor 페이지로 이동하기
        }

        binding.btnClose.setOnThrottleClickListener {
            viewModel.saveGifticon()
        }

        binding.btnDelete.setOnThrottleClickListener {
            viewModel.deleteGifticon()
        }

        binding.btnUseAndRevert.setOnThrottleClickListener {
            if (viewModel.isUsed.value == true) {
                viewModel.revertUseGifticon()
            } else {
                viewModel.useGifticon()
            }
        }
    }
}