package com.lighthouse.beep.ui.feature.editor.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.lighthouse.beep.model.gifticon.GifticonBuiltInThumbnail
import com.lighthouse.beep.ui.dialog.bottomsheet.BeepBottomSheetDialog
import com.lighthouse.beep.ui.feature.editor.EditorViewModel
import com.lighthouse.beep.ui.feature.editor.R
import com.lighthouse.beep.ui.feature.editor.databinding.DialogBuiltInThumbnailBinding
import com.lighthouse.beep.ui.feature.editor.dialog.adapter.BuiltInThumbnailAdapter
import com.lighthouse.beep.ui.feature.editor.dialog.adapter.OnBuiltInThumbnailListener
import com.lighthouse.beep.ui.feature.editor.model.EditData
import com.lighthouse.beep.ui.feature.editor.model.GifticonThumbnail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class BuiltInThumbnailDialog : BeepBottomSheetDialog() {

    companion object {
        const val TAG = "BuiltInThumbnail"
    }

    private val editorViewModel by activityViewModels<EditorViewModel>()

    private var _binding: DialogBuiltInThumbnailBinding? = null
    private val binding
        get() = requireNotNull(_binding)

    private val builtInThumbnailAdapter = BuiltInThumbnailAdapter(
        listener = object : OnBuiltInThumbnailListener {
            override fun isSelectedFlow(item: GifticonBuiltInThumbnail): Flow<Boolean> {
                return editorViewModel.selectedGifticonDataFlow.map {
                    val builtIn = it.thumbnail as? GifticonThumbnail.BuiltIn
                    builtIn?.builtIn == item
                }.distinctUntilChanged()
            }

            override fun onClick(item: GifticonBuiltInThumbnail) {
                val editData = EditData.BuiltInThumbnail(item)
                editorViewModel.updateGifticonData(editData = editData)
                hideDialog()
            }

            override fun getBackgroundRes(position: Int): Int {
                val manager =
                    binding.gridBuiltInThumbnail.layoutManager as? GridLayoutManager ?: return R.drawable.bg_built_in_thumbnail
                val spanCount = manager.spanCount
                val itemCount = binding.gridBuiltInThumbnail.adapter?.itemCount ?: return R.drawable.bg_built_in_thumbnail
                val spanIndex = manager.spanSizeLookup.getSpanIndex(position, spanCount)
                val groupIndex = manager.spanSizeLookup.getSpanGroupIndex(position, spanCount)
                val lastGroupIndex = manager.spanSizeLookup.getSpanGroupIndex(itemCount - 1, spanCount)

                return when {
                    groupIndex == 0 && spanIndex == 0 -> R.drawable.bg_built_in_thumbnail_lt
                    groupIndex == 0 && spanIndex == spanCount - 1 -> R.drawable.bg_built_in_thumbnail_rt
                    groupIndex == lastGroupIndex && spanIndex == 0 -> R.drawable.bg_built_in_thumbnail_lb
                    groupIndex == lastGroupIndex && spanIndex == spanCount - 1 -> R.drawable.bg_built_in_thumbnail_rb
                    else -> R.drawable.bg_built_in_thumbnail
                }
            }
        }
    )

    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogBuiltInThumbnailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpBuiltInList()
        attachHandle(binding.viewHandle)
    }

    private fun setUpBuiltInList() {
        builtInThumbnailAdapter.submitList(GifticonBuiltInThumbnail.entries)
        binding.container.clipChildren = false
        binding.gridBuiltInThumbnail.clipChildren = false
        binding.gridBuiltInThumbnail.adapter = builtInThumbnailAdapter
    }
}