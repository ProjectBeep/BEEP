package com.lighthouse.beep.ui.feature.home.page.expired

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lighthouse.beep.core.ui.binding.viewBindings
import com.lighthouse.beep.core.ui.decoration.LinearItemDecoration
import com.lighthouse.beep.core.ui.exts.dp
import com.lighthouse.beep.core.ui.exts.repeatOnStarted
import com.lighthouse.beep.ui.feature.home.R
import com.lighthouse.beep.ui.feature.home.adapter.expired.ExpiredBrandChipAdapter
import com.lighthouse.beep.ui.feature.home.adapter.expired.OnExpiredBrandListener
import com.lighthouse.beep.ui.feature.home.databinding.FragmentExpiredGifticonBinding
import com.lighthouse.beep.ui.feature.home.model.ExpiredBrandItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow

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

    }

    private fun setUpCollectState() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.brandList.collect {
                brandAdapter.submitList(it)
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.gifticonList.collect {

            }
        }
    }
}