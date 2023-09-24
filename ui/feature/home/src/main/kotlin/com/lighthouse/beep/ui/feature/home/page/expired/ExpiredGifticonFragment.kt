package com.lighthouse.beep.ui.feature.home.page.expired

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lighthouse.beep.core.common.exts.calculateNextDayRemainingTime
import com.lighthouse.beep.core.ui.binding.viewBindings
import com.lighthouse.beep.core.ui.decoration.LinearItemDecoration
import com.lighthouse.beep.core.ui.exts.dp
import com.lighthouse.beep.core.ui.exts.repeatOnStarted
import com.lighthouse.beep.ui.feature.home.R
import com.lighthouse.beep.ui.feature.home.adapter.expired.ExpiredBrandChipAdapter
import com.lighthouse.beep.ui.feature.home.adapter.expired.ExpiredGifticonAdapter
import com.lighthouse.beep.ui.feature.home.adapter.expired.OnExpiredBrandListener
import com.lighthouse.beep.ui.feature.home.adapter.expired.OnExpiredGifticonListener
import com.lighthouse.beep.ui.feature.home.databinding.FragmentExpiredGifticonBinding
import com.lighthouse.beep.ui.feature.home.model.ExpiredBrandItem
import com.lighthouse.beep.ui.feature.home.model.ExpiredGifticonItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Date

@AndroidEntryPoint
internal class ExpiredGifticonFragment : Fragment(R.layout.fragment_expired_gifticon) {

    companion object {
        fun newInstance(param: ExpiredGifticonParam): Fragment {
            return ExpiredGifticonFragment().apply {
                arguments = param.buildBundle()
            }
        }
    }

    private val binding by viewBindings<FragmentExpiredGifticonBinding>()

    private val viewModel by viewModels<ExpiredGifticonViewModel>()

    private val brandAdapter = ExpiredBrandChipAdapter(
        onExpiredBrandListener = object: OnExpiredBrandListener {
            override fun getSelectedFlow(): Flow<ExpiredBrandItem> {
                return viewModel.selectedBrand
            }

            override fun onClick(item: ExpiredBrandItem) {
                viewModel.selectBrand(item)
            }
        }
    )

    private val gifticonAdapter = ExpiredGifticonAdapter(
        onExpiredGifticonListener = object: OnExpiredGifticonListener {
            override fun getNextDayEventFlow(): Flow<Unit> {
                return flow {
                    while(true) {
                        delay (Date().calculateNextDayRemainingTime())
                        emit(Unit)
                    }
                }
            }

            override fun onClick(item: ExpiredGifticonItem) {
            }
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpBrandList()
        setUpGifticonList()
        setUpCollectState()
    }

    private fun setUpBrandList() {
        binding.listBrand.adapter = brandAdapter
        binding.listBrand.addItemDecoration(LinearItemDecoration(8.dp))
    }

    private fun setUpGifticonList() {
        binding.listExpiredGifticon.adapter = gifticonAdapter
        binding.listExpiredGifticon.addItemDecoration(LinearItemDecoration(8.dp))
    }

    private fun setUpCollectState() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.brandList.collect {
                brandAdapter.submitList(it)
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.gifticonList.collect {
                gifticonAdapter.submitList(it)
            }
        }
    }
}