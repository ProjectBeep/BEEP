package com.lighthouse.beep.ui.feature.home.page.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.lighthouse.beep.core.common.exts.cast
import com.lighthouse.beep.core.ui.exts.createThrottleClickListener
import com.lighthouse.beep.core.ui.exts.preventTouchPropagation
import com.lighthouse.beep.core.ui.exts.repeatOnStarted
import com.lighthouse.beep.core.ui.exts.viewWidth
import com.lighthouse.beep.core.ui.model.ScrollInfo
import com.lighthouse.beep.model.location.DmsPos
import com.lighthouse.beep.ui.feature.home.databinding.FragmentMainHomeBinding
import com.lighthouse.beep.ui.feature.home.decorator.HomeItemDecoration
import com.lighthouse.beep.ui.feature.home.decorator.HomeItemDecorationCallback
import com.lighthouse.beep.ui.feature.home.model.ExpiredBrandItem
import com.lighthouse.beep.ui.feature.home.model.ExpiredOrder
import com.lighthouse.beep.ui.feature.home.model.HomeItem
import com.lighthouse.beep.ui.feature.home.model.MapGifticonItem
import com.lighthouse.beep.ui.feature.home.page.home.section.HomeAdapter
import com.lighthouse.beep.ui.feature.home.page.home.section.expired.ExpiredHeaderViewHolder
import com.lighthouse.beep.ui.feature.home.page.home.section.expired.OnExpiredBrandListener
import com.lighthouse.beep.ui.feature.home.page.home.section.expired.OnExpiredGifticonListener
import com.lighthouse.beep.ui.feature.home.page.home.section.expired.OnExpiredHeaderListener
import com.lighthouse.beep.ui.feature.home.page.home.section.map.OnMapGifticonListener
import com.lighthouse.beep.ui.feature.home.page.home.section.map.OnMapGifticonSectionListener
import com.lighthouse.beep.ui.feature.home.provider.HomeNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.math.max

@AndroidEntryPoint
class HomeMainFragment : Fragment() {

    companion object {
        const val TAG = "HomeMain"
    }

    private var _binding: FragmentMainHomeBinding? = null
    private val binding: FragmentMainHomeBinding
        get() = requireNotNull(_binding)

    private val viewModel by viewModels<HomeMainViewModel>()

    private lateinit var navigationProvider: HomeNavigation

    private val homeAdapter by lazy {
        HomeAdapter(
            onMapGifticonSectionListener = onMapGifticonSectionListener,
            onMapGifticonListener = onMapGifticonListener,
            onExpiredHeaderListener = onExpiredHeaderListener,
            onExpiredBrandListener = onExpiredBrandListener,
            onExpiredGifticonListener = onExpiredGifticonListener,
        )
    }

    private val onMapGifticonSectionListener = object : OnMapGifticonSectionListener {
        override fun getMapGifticonListFlow(): Flow<List<MapGifticonItem>> {
            return viewModel.mapGifticonList
        }

        override fun onGotoMapClick() {
        }
    }

    private val onMapGifticonListener = object : OnMapGifticonListener {
        override fun getCurrentDmsPosFlow(): Flow<DmsPos> {
            return flow {
                emit(DmsPos(0.0, 0.0))
            }
        }

        override fun onClick(item: MapGifticonItem) {
        }
    }

    private val onExpiredHeaderListener = object : OnExpiredHeaderListener {
        override fun getBrandListFlow(): Flow<List<ExpiredBrandItem>> {
            return viewModel.brandList
        }

        override fun getSelectedOrder(): Flow<ExpiredOrder> {
            return viewModel.selectedExpiredOrder
        }

        override fun getBrandScrollInfo(): Flow<ScrollInfo> {
            return viewModel.brandScrollInfo
        }

        override fun onOrderClick(order: ExpiredOrder) {
            viewModel.setSelectExpiredOrder(order)
        }

        override fun onBrandClick(item: ExpiredBrandItem) {
            viewModel.setSelectBrand(item)
        }

        override fun onGotoEditClick() {
        }

        override fun onBrandScroll(scrollInfo: ScrollInfo) {
            viewModel.setBrandScrollInfo(scrollInfo)
        }
    }

    private val onExpiredBrandListener = object : OnExpiredBrandListener {
        override fun getSelectedFlow(): Flow<ExpiredBrandItem> {
            return viewModel.selectedBrand
        }
    }

    private val onExpiredGifticonListener = object : OnExpiredGifticonListener {
        override fun getNextDayEventFlow(): Flow<Unit> {
            return viewModel.nextDayRemainingTimeFlow
        }

        override fun onClick(item: HomeItem.ExpiredGifticonItem) {

        }
    }

    private val homeItemDecorationCallback = object : HomeItemDecorationCallback {
        override fun onTopItemPosition(position: Int) {
            val isShow = viewModel.expiredHeaderIndex <= position
            binding.containerStickyHeader.isVisible = isShow
        }

        override fun getExpiredGifticonFirstIndex(): Int {
            return viewModel.expiredGifticonFirstIndex
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        navigationProvider = requireActivity().cast()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setUpExpiredStickyHeader()
        setUpHomeList()
        setUpCollectState()
        setUpClickEvent()
    }

    private fun setUpExpiredStickyHeader() {
        val header =
            ExpiredHeaderViewHolder(binding.list, onExpiredHeaderListener, onExpiredBrandListener)
        header.bind(HomeItem.ExpiredHeader)
        binding.containerStickyHeader.addView(header.itemView)
        binding.containerStickyHeader.preventTouchPropagation()
    }

    private fun setUpHomeList() {
        binding.list.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val button = binding.btnGotoRegister
                val offset = binding.list.computeVerticalScrollOffset()
                val current = max(button.maxWidth - offset, button.minWidth)
                if (current != button.width) {
                    val progress = (current - button.minWidth).toFloat() / (button.maxWidth - button.minWidth)
                    binding.textGotoRegister.alpha = progress
                    button.updateLayoutParams { width = current }
                }
            }
        })

        binding.list.adapter = homeAdapter
        binding.list.addItemDecoration(HomeItemDecoration(homeItemDecorationCallback))
        binding.btnGotoRegister.doOnPreDraw {
            binding.btnGotoRegister.maxWidth = it.viewWidth
        }
    }

    private fun setUpCollectState() {
        repeatOnStarted {
            viewModel.homeList.collect {
                homeAdapter.submitList(it)
            }
        }
    }

    private fun setUpClickEvent() {
        binding.btnGotoRegister.setOnClickListener(createThrottleClickListener {
            navigationProvider.gotoGallery()
        })
    }
}