package com.lighthouse.beep.ui.feature.home.page.empty

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lighthouse.beep.core.common.exts.cast
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.ui.feature.home.databinding.FragmentEmptyGifticonBinding
import com.lighthouse.beep.ui.feature.home.provider.HomeNavigation

class HomeEmptyFragment : Fragment() {

    companion object {
        const val TAG = "HomeEmpty"
    }

    private var _binding: FragmentEmptyGifticonBinding? = null
    private val binding: FragmentEmptyGifticonBinding
        get() = requireNotNull(_binding)

    private lateinit var navigationProvider: HomeNavigation

    override fun onAttach(context: Context) {
        super.onAttach(context)

        navigationProvider = requireActivity().cast()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmptyGifticonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpClickEvent()
    }

    private fun setUpClickEvent() {
        binding.btnGotoRegister.setOnThrottleClickListener {
            navigationProvider.gotoGallery()
        }
    }
}