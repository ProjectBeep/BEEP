package com.lighthouse.beep.ui.feature.home.adapter.expired

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.lighthouse.beep.core.ui.binding.viewBindings
import com.lighthouse.beep.ui.feature.home.R
import com.lighthouse.beep.ui.feature.home.databinding.FragmentExpiredGifticonBinding

internal class ExpiredGifticonFragment : Fragment(R.layout.fragment_expired_gifticon) {

    companion object {
        fun newInstance(param: ExpiredGifticonParam): Fragment {
            return ExpiredGifticonFragment().apply {
                arguments = param.buildBundle()
            }
        }
    }

    private val binding by viewBindings<FragmentExpiredGifticonBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
}