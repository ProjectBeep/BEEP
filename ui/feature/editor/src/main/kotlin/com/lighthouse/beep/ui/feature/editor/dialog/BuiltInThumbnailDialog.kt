package com.lighthouse.beep.ui.feature.editor.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.lighthouse.beep.model.gifticon.GifticonBuiltInThumbnail
import com.lighthouse.beep.ui.dialog.bottomsheet.BeepBottomSheetDialog
import com.lighthouse.beep.ui.feature.editor.EditorViewModel
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
        binding.gridBuiltInThumbnail.clipToOutline = true
        binding.gridBuiltInThumbnail.adapter = builtInThumbnailAdapter
    }
}