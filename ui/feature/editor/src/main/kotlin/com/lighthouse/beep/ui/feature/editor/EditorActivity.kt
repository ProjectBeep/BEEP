package com.lighthouse.beep.ui.feature.editor

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.core.ui.decoration.LinearItemDecoration
import com.lighthouse.beep.core.ui.exts.createThrottleClickListener
import com.lighthouse.beep.core.ui.exts.hide
import com.lighthouse.beep.core.ui.exts.repeatOnStarted
import com.lighthouse.beep.core.ui.exts.show
import com.lighthouse.beep.core.ui.scroller.CenterScrollLayoutManager
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.navs.result.EditorResult
import com.lighthouse.beep.ui.dialog.confirmation.ConfirmationDialog
import com.lighthouse.beep.ui.dialog.confirmation.ConfirmationParam
import com.lighthouse.beep.ui.dialog.progress.ProgressDialog
import com.lighthouse.beep.ui.dialog.progress.ProgressParam
import com.lighthouse.beep.ui.dialog.textinput.TextInputDialog
import com.lighthouse.beep.ui.dialog.textinput.TextInputResult
import com.lighthouse.beep.ui.feature.editor.adapter.chip.EditorPropertyChipAdapter
import com.lighthouse.beep.ui.feature.editor.adapter.chip.OnEditorPropertyChipListener
import com.lighthouse.beep.ui.feature.editor.adapter.editor.EditorAdapter
import com.lighthouse.beep.ui.feature.editor.adapter.editor.OnEditorPreviewListener
import com.lighthouse.beep.ui.feature.editor.adapter.editor.OnEditorTextListener
import com.lighthouse.beep.ui.feature.editor.adapter.editor.OnEditorThumbnailListener
import com.lighthouse.beep.ui.feature.editor.adapter.gifticon.OnEditorGifticonListener
import com.lighthouse.beep.ui.feature.editor.adapter.gifticon.EditorGifticonAdapter
import com.lighthouse.beep.ui.feature.editor.databinding.ActivityEditorBinding
import com.lighthouse.beep.ui.feature.editor.model.CropData
import com.lighthouse.beep.ui.feature.editor.model.EditData
import com.lighthouse.beep.ui.feature.editor.model.EditorChip
import com.lighthouse.beep.ui.feature.editor.model.EditType
import com.lighthouse.beep.ui.feature.editor.model.EditorPage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlin.math.max
import com.lighthouse.beep.theme.R as ThemeR

@AndroidEntryPoint
internal class EditorActivity : AppCompatActivity(), OnDialogProvider, OnEditorChipListener {

    companion object {
        private const val TAG_SELECTED_GIFTICON_DELETE = "Tag.SelectedGifticonDelete"
    }

    private lateinit var binding: ActivityEditorBinding

    private val viewModel by viewModels<EditorViewModel>()

    private val onEditorGifticonListener = object : OnEditorGifticonListener {
        override fun isSelectedFlow(item: GalleryImage): Flow<Boolean> {
            return viewModel.selectedGifticon
                .map { it?.id == item.id }
                .distinctUntilChanged()
        }

        override fun isInvalidFlow(item: GalleryImage): Flow<Boolean> {
            return viewModel.gifticonDataMapFlow
                .map { map -> map[item.id]?.isInvalid }
                .filterNotNull()
                .distinctUntilChanged()
        }

        override fun getCropDataFlow(item: GalleryImage): Flow<CropData> {
            return viewModel.gifticonDataMapFlow
                .map { map -> map[item.id]?.cropData }
                .filterNotNull()
                .distinctUntilChanged()
        }

        override fun onClick(item: GalleryImage) {
            viewModel.selectGifticon(item)
            selectEditorChip(EditorChip.Preview)
        }

        override fun onDeleteClick(item: GalleryImage) {
            showSelectedGifticonDeleteDialog(item)
        }
    }

    private fun showSelectedGifticonDeleteDialog(item: GalleryImage) {
        show(TAG_SELECTED_GIFTICON_DELETE) {
            val param = ConfirmationParam(
                message = getString(R.string.editor_gifticon_delete_message),
                okText = getString(R.string.editor_gifticon_delete_ok),
                cancelText = getString(R.string.editor_gifticon_delete_cancel)
            )
            ConfirmationDialog.newInstance(param).apply {
                setOnOkClickListener {
                    viewModel.deleteItem(item)
                }
            }
        }
    }

    private val editorGifticonAdapter = EditorGifticonAdapter(
        onSelectedGalleryListener = onEditorGifticonListener
    )

    private val onEditorPropertyChipListener = object : OnEditorPropertyChipListener {
        override fun isSelectedFlow(item: EditorChip.Property): Flow<Boolean> {
            return viewModel.selectedEditorChip
                .map { it is EditorChip.Property && it.type == item.type }
                .distinctUntilChanged()
        }

        override fun isInvalidFlow(type: EditType): Flow<Boolean> {
            return viewModel.selectedGifticonDataFlow
                .map { type.isInvalid(it) }
                .distinctUntilChanged()
        }

        override fun onClick(item: EditorChip.Property, position: Int) {
            binding.listEditorChip.smoothScrollToPosition(position)
            viewModel.selectEditorChip(item)
        }
    }

    override fun selectEditorChip(type: EditType) {
        selectEditorChip(EditorChip.Property(type))
    }

    override fun selectEditorChip(item: EditorChip) {
        val position = when(item) {
            is EditorChip.Preview -> 0
            is EditorChip.Property -> editorPropertyChipAdapter.getPosition(item)
        }
        if (position >= 0) {
            binding.listEditorChip.smoothScrollToPosition(position)
            viewModel.selectEditorChip(item)
        }
    }

    private val editorPropertyChipAdapter = EditorPropertyChipAdapter(
        onEditorPropertyChipListener = onEditorPropertyChipListener
    )

    private val onEditorPreviewListener = object : OnEditorPreviewListener {
        override fun getInvalidPropertyFlow(): Flow<List<EditType>> {
            return viewModel.selectedGifticonDataFlow
                .map { data -> EditType.entries.filter { it.isInvalid(data) } }
                .distinctUntilChanged()
        }
    }

    private val onEditorThumbnailListener = object : OnEditorThumbnailListener {
        override fun getThumbnailFlow(): Flow<Uri> {
            return viewModel.selectedGifticonDataFlow
                .map { it.originUri }
                .distinctUntilChanged()
        }

        override fun getCropDataFlow(): Flow<CropData> {
            return viewModel.selectedGifticonDataFlow
                .map { it.cropData }
                .distinctUntilChanged()
        }

        override fun isThumbnailEditedFlow(): Flow<Boolean> {
            return viewModel.selectedGifticonDataFlow
                .map { it.cropData.isCropped }
                .distinctUntilChanged()
        }
    }

    override fun showTextInputDialog(type: EditType) {
        val data = viewModel.selectedGifticonData.value?: return
        show(type.name) {
            val param = type.createTextInputParam(data)
            supportFragmentManager.setFragmentResultListener(type.name, this) { requestKey, data ->
                val result = TextInputResult(data)
                val editData = type.createEditDataWithText(result.value)
                if (editData !is EditData.None) {
                    viewModel.updateGifticonData(editData = editData)
                }
                supportFragmentManager.clearFragmentResultListener(requestKey)
            }
            TextInputDialog.newInstance(param)
        }
    }

    private val onEditorTextListener = object : OnEditorTextListener {
        override fun getTextFlow(type: EditType): Flow<String> {
            return viewModel.selectedGifticonDataFlow
                .map { type.getText(it) }
                .distinctUntilChanged()
        }

        override fun onEditClick(type: EditType) {
            showTextInputDialog(type)
        }
    }

    private val editorAdapter = EditorAdapter(
        onEditorPreviewListener = onEditorPreviewListener,
        onEditorThumbnailListener = onEditorThumbnailListener,
        onEditorTextListener = onEditorTextListener,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setUpBackPress()
        setUpGifticonList()
        setUpPropertyChipList()
        setUpRecycleEditor()
        setUpCollectState()
        setUpOnClickEvent()
    }

    private fun setUpBackPress() {
        onBackPressedDispatcher.addCallback(this) {
            cancelEditor()
        }
    }

    private fun setUpGifticonList() {
        binding.listSelected.adapter = editorGifticonAdapter
        binding.listSelected.setHasFixedSize(true)
        binding.listSelected.addItemDecoration(LinearItemDecoration(1.5f.dp))
    }

    private fun setUpPropertyChipList() {
        binding.listEditorChip.itemAnimator = null
        binding.listEditorChip.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val preview = binding.btnPreview
                val offset = binding.listEditorChip.computeHorizontalScrollOffset()
                val current = max(preview.maxWidth - offset, preview.minWidth)
                if (current != preview.width) {
                    val progress =
                        (current - preview.minWidth).toFloat() / (preview.maxWidth - preview.minWidth)
                    binding.textPreview.alpha = progress
                    preview.updateLayoutParams { width = current }
                }
            }
        })

        binding.listEditorChip.adapter = editorPropertyChipAdapter
        binding.listEditorChip.layoutManager = CenterScrollLayoutManager(
            context = this,
            orientation = RecyclerView.HORIZONTAL,
            offset = (-12).dp
        )
        binding.btnPreview.post {
            binding.btnPreview.maxWidth = binding.btnPreview.measuredWidth
            binding.listEditorChip.addItemDecoration(
                LinearItemDecoration(
                    8.dp,
                    binding.btnPreview.maxWidth + 12.dp
                )
            )
        }
    }

    private fun setUpRecycleEditor() {
        binding.recyclerEditor.adapter = editorAdapter
        binding.recyclerEditor.itemAnimator = null
        binding.recyclerEditor.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    }

    private fun showProgress(isLoading: Boolean) {
        if (isLoading) {
            show(ProgressDialog.TAG) {
                val param = ProgressParam(getColor(ThemeR.color.black_60))
                ProgressDialog.newInstance(param).apply {
                    setOnCancelListener {
                        cancelEditor()
                    }
                }
            }
        } else {
            hide(ProgressDialog.TAG)
        }
    }

    private fun setUpCollectState() {
        repeatOnStarted {
            viewModel.editorPropertyChipList.collect {
                editorPropertyChipAdapter.submitList(it)
            }
        }

        repeatOnStarted {
            viewModel.galleryImage.collect {
                if (it.isEmpty()) {
                    cancelEditor()
                } else {
                    editorGifticonAdapter.submitList(it)
                }
            }
        }

        repeatOnStarted {
            viewModel.isSelectPreview.collect { isSelected ->
                binding.btnPreview.isSelected = isSelected
                binding.textPreview.isSelected = isSelected
                binding.iconPreview.isSelected = isSelected
            }
        }

        repeatOnStarted {
            viewModel.selectedEditorChip.collect {
                editorAdapter.submitList(listOf(it))
            }
        }

        repeatOnStarted {
            viewModel.selectedEditorPage.collect {
                changeEditorPage(it)
            }
        }

        repeatOnStarted {
            viewModel.recognizeLoading.collect { isLoading ->
                showProgress(isLoading)
            }
        }

        repeatOnStarted {
            viewModel.isRegisterActivated.collect { isActivated ->
                binding.btnRegister.isActivated = isActivated
            }
        }
    }

    private fun setUpOnClickEvent() {
        binding.btnBack.setOnClickListener(createThrottleClickListener {
            cancelEditor()
        })

        binding.btnPreview.setOnClickListener(createThrottleClickListener {
            binding.listEditorChip.smoothScrollToPosition(0)
            viewModel.selectEditorChip(EditorChip.Preview)
        })

        binding.btnRegister.setOnClickListener(createThrottleClickListener {
            if (viewModel.isRegisterActivated.value) {

            } else {

            }
        })
    }

    private fun changeEditorPage(page: EditorPage) {
        var fragment = supportFragmentManager.findFragmentByTag(page.name)
        if (fragment == null) {
            fragment = page.createFragment()
        }
        supportFragmentManager.commit {
            replace(binding.containerEditor.id, fragment, page.name)
        }
    }

    private fun cancelEditor() {
        val result = EditorResult(viewModel.galleryImage.value)
        setResult(Activity.RESULT_CANCELED, result.createIntent())
        finish()
    }
}