package com.lighthouse.beep.ui.designsystem.dotindicator

import android.view.MotionEvent
import androidx.core.view.doOnAttach
import androidx.core.view.doOnDetach
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class AutoPagerSnapHelper {

    companion object {
        private const val DEFAULT_PAGING_DELAY = 2000L
        private const val DEFAULT_PAGING_DURATION = 200
    }

    private var smoothScroller: RecyclerView.SmoothScroller? = null

    private var onItemTouchListener: RecyclerView.OnItemTouchListener? = null

    private val pagerSnapHelper = PagerSnapHelper()

    private var autoPagerJob: Job? = null

    var pagingDelay = DEFAULT_PAGING_DELAY
    var pagingDuration = DEFAULT_PAGING_DURATION

    fun attachToRecyclerView(lifecycleOwner: LifecycleOwner, recyclerView: RecyclerView) {
        recyclerView.doOnAttach {
            smoothScroller = object : LinearSmoothScroller(recyclerView.context) {
                override fun calculateTimeForScrolling(dx: Int): Int {
                    return super.calculateTimeForScrolling(dx).coerceAtLeast(pagingDuration)
                }
            }

            autoPagerJob?.cancel()
            autoPagerJob = launchAuthPager(lifecycleOwner, recyclerView)
            pagerSnapHelper.attachToRecyclerView(recyclerView)

            onItemTouchListener = object : RecyclerView.SimpleOnItemTouchListener() {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    when (e.action) {
                        MotionEvent.ACTION_DOWN -> {
                            autoPagerJob?.cancel()
                            autoPagerJob = null
                        }

                        MotionEvent.ACTION_CANCEL,
                        MotionEvent.ACTION_UP -> {
                            autoPagerJob = launchAuthPager(lifecycleOwner, rv)
                        }
                    }

                    return super.onInterceptTouchEvent(rv, e)
                }
            }
            onItemTouchListener?.let { listener ->
                recyclerView.addOnItemTouchListener(listener)
            }
        }

        recyclerView.doOnDetach {
            autoPagerJob?.cancel()
            autoPagerJob = null
            onItemTouchListener?.let { listener ->
                recyclerView.removeOnItemTouchListener(listener)
            }
            onItemTouchListener = null
        }
    }

    private fun launchAuthPager(
        lifecycleOwner: LifecycleOwner,
        recyclerView: RecyclerView,
    ): Job = lifecycleOwner.lifecycleScope.launch {
        while (isActive) {
            delay(pagingDelay)
            val adapter = recyclerView.adapter ?: continue
            val layoutManager =
                recyclerView.layoutManager as? LinearLayoutManager ?: continue
            if (adapter.itemCount == 0) {
                continue
            }
            val position = layoutManager.findFirstVisibleItemPosition()
            smoothScroller?.targetPosition = (position + 1) % adapter.itemCount
            recyclerView.layoutManager?.startSmoothScroll(smoothScroller)
        }
    }
}