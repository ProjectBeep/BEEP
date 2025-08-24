package com.lighthouse.beep.ui.feature.editor.page.edit

import android.app.Activity
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.lighthouse.beep.core.common.exts.EMPTY_DATE
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.core.ui.exts.preventTouchPropagation
import com.lighthouse.beep.core.ui.exts.repeatOnStarted
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.core.ui.exts.setUpSystemInsetsPadding
import com.lighthouse.beep.core.ui.exts.setVisible
import com.lighthouse.beep.core.ui.exts.show
import com.lighthouse.beep.core.ui.exts.viewWidth
import com.lighthouse.beep.core.ui.recyclerview.scroller.CenterScrollLayoutManager
import com.lighthouse.beep.permission.BeepPermission
import com.lighthouse.beep.ui.designsystem.snackbar.BeepSnackBar
import com.lighthouse.beep.ui.dialog.confirmation.ConfirmationDialog
import com.lighthouse.beep.ui.dialog.confirmation.ConfirmationParam
import com.lighthouse.beep.ui.dialog.datepicker.DatePickerDialog
import com.lighthouse.beep.ui.dialog.datepicker.DatePickerParam
import com.lighthouse.beep.ui.dialog.datepicker.DatePickerResult
import com.lighthouse.beep.ui.dialog.textinput.TextInputDialog
import com.lighthouse.beep.ui.dialog.textinput.TextInputResult
import com.lighthouse.beep.ui.feature.editor.EditorInfoProvider
import com.lighthouse.beep.ui.feature.editor.R
import com.lighthouse.beep.ui.feature.editor.databinding.ActivityEditBinding
import com.lighthouse.beep.ui.feature.editor.dialog.BuiltInThumbnailDialog
import com.lighthouse.beep.ui.feature.editor.dialog.BuiltInThumbnailListener
import com.lighthouse.beep.ui.feature.editor.feature.crop.EditorCropFragment
import com.lighthouse.beep.ui.feature.editor.feature.crop.EditorCropInfoListener
import com.lighthouse.beep.ui.feature.editor.list.chip.EditorPropertyChipAdapter
import com.lighthouse.beep.ui.feature.editor.list.chip.OnEditorPropertyChipListener
import com.lighthouse.beep.ui.feature.editor.list.editor.EditorAdapter
import com.lighthouse.beep.ui.feature.editor.list.editor.OnEditorExpiredListener
import com.lighthouse.beep.ui.feature.editor.list.editor.OnEditorMemoListener
import com.lighthouse.beep.ui.feature.editor.list.editor.OnEditorTextListener
import com.lighthouse.beep.ui.feature.editor.list.editor.OnEditorThumbnailListener
import com.lighthouse.beep.ui.feature.editor.list.preview.EditorPreviewViewHolder
import com.lighthouse.beep.ui.feature.editor.list.preview.OnEditorPreviewListener
import com.lighthouse.beep.ui.feature.editor.model.EditData
import com.lighthouse.beep.ui.feature.editor.model.EditGifticonThumbnail
import com.lighthouse.beep.ui.feature.editor.model.EditType
import com.lighthouse.beep.ui.feature.editor.model.EditorChip
import com.lighthouse.beep.ui.feature.editor.model.EditorGifticonInfo
import com.lighthouse.beep.ui.feature.editor.model.EditorPage
import com.lighthouse.beep.ui.feature.editor.model.GifticonData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.math.max

@AndroidEntryPoint
internal class EditActivity : AppCompatActivity(), EditorInfoProvider {

    companion object {
        private const val TAG_EXIT_EDITOR = "Tag.ExitEditor"
    }

    private lateinit var binding: ActivityEditBinding

    private val viewModel by viewModels<EditViewModel>()

    private val requestManager: RequestManager by lazy {
        Glide.with(this)
    }

    private val beepSnackBar by lazy {
        BeepSnackBar.Builder(this)
            .setLifecycleOwner(this)
            .setRootView(binding.root)
    }

    private val onEditorPreviewListener = object : OnEditorPreviewListener {
        override fun getGifticonDataFlow(id: Long): Flow<GifticonData> {
            return viewModel.gifticonData
                .filterNotNull()
                .distinctUntilChanged()
        }

        override fun onCashChange(id: Long, isCash: Boolean) {
            viewModel.updateGifticonData(EditData.Cash(isCash))
        }

        override fun onEditClick(editType: EditType) {
            selectEditorChip(EditorChip.Property(editType))
        }
    }

    private val onEditorPropertyChipListener = object : OnEditorPropertyChipListener {
        override fun isSelectedFlow(item: EditorChip.Property): Flow<Boolean> {
            return viewModel.selectedEditorChip
                .map { it is EditorChip.Property && it.type == item.type }
                .distinctUntilChanged()
        }

        override fun isInvalidFlow(type: EditType): Flow<Boolean> {
            return viewModel.gifticonData
                .filterNotNull()
                .map { type.isInvalid(it) }
                .distinctUntilChanged()
        }

        override fun onClick(item: EditorChip.Property, position: Int) {
            binding.listEditorChip.smoothScrollToPosition(position)
            viewModel.selectEditorChip(item)
        }
    }

    private val editorPropertyChipAdapter = EditorPropertyChipAdapter(
        onEditorPropertyChipListener = onEditorPropertyChipListener
    )

    private val onEditorMemoListener = object : OnEditorMemoListener {
        override fun getMemoFlow(): Flow<String> {
            return viewModel.gifticonData
                .filterNotNull()
                .map { it.memo }
                .distinctUntilChanged()
        }

        override fun onMemoClick() {
            showTextInputDialog(EditType.MEMO)
        }
    }

    private val onEditorThumbnailListener = object : OnEditorThumbnailListener {
        override fun getThumbnailFlow(): Flow<EditGifticonThumbnail> {
            return viewModel.gifticonData
                .filterNotNull()
                .map { it.thumbnail }
                .distinctUntilChanged()
        }

        override fun showBuiltInThumbnail() {
            showBuiltInThumbnailDialog()
        }

        override fun clearThumbnail() {
            viewModel.updateGifticonData(editData = EditData.ClearThumbnail)
        }
    }

    private fun showBuiltInThumbnailDialog() {
        show(BuiltInThumbnailDialog.TAG) {
            BuiltInThumbnailDialog()
        }
    }

    private val onEditorTextListener = object : OnEditorTextListener {
        override fun getTextFlow(type: EditType): Flow<String> {
            return viewModel.gifticonData
                .filterNotNull()
                .map { type.getText(it) }
                .distinctUntilChanged()
        }

        override fun onEditClick(type: EditType) {
            showTextInputDialog(type)
        }

        override fun onClearClick(type: EditType) {
            viewModel.updateGifticonData(editData = type.createEditDataWithText(""))
        }
    }

    private val onEditorExpiredListener = object : OnEditorExpiredListener {
        override fun getGifticonDataFlow(): Flow<GifticonData> {
            return viewModel.gifticonData
                .filterNotNull()
                .distinctUntilChanged()
        }

        override fun showExpired() {
            showExpiredDialog()
        }
    }

    private fun showExpiredDialog() {
        show(DatePickerDialog.TAG) {
            supportFragmentManager.setFragmentResultListener(
                DatePickerResult.KEY,
                this,
            ) { requestKey, data ->
                val result = DatePickerResult(data)
                val editData = EditData.Expired(result.date)
                viewModel.updateGifticonData(editData = editData)
                supportFragmentManager.clearFragmentResultListener(requestKey)
            }

            val expired = viewModel.gifticonData.value?.expireAt ?: EMPTY_DATE
            val param = DatePickerParam(expired)
            DatePickerDialog.newInstance(param)
        }
    }

    private val editorAdapter by lazy {
        EditorAdapter(
            requestManager = requestManager,
            onEditorMemoListener = onEditorMemoListener,
            onEditorThumbnailListener = onEditorThumbnailListener,
            onEditorTextListener = onEditorTextListener,
            onEditorExpiredListener = onEditorExpiredListener,
        )
    }

    override val cropInfoListener: EditorCropInfoListener = object : EditorCropInfoListener {
        override val selectedEditType: EditType?
            get() {
                val chip = viewModel.selectedEditorChip.value as? EditorChip.Property
                return chip?.type
            }

        override val selectedGifticonData: GifticonData?
            get() = viewModel.gifticonData.value

        override val selectedGifticonFlow: Flow<EditorGifticonInfo>
            get() = viewModel.editorGifticonInfo.filterNotNull()

        override val selectedGifticonDataFlow: Flow<GifticonData>
            get() = viewModel.gifticonData.filterNotNull()

        override val selectedPropertyChipFlow: Flow<EditorChip.Property>
            get() = viewModel.selectedEditorChip.filterIsInstance<EditorChip.Property>()

        override fun getGifticonData(id: Long): GifticonData? {
            return viewModel.gifticonData.value
        }

        override fun updateGifticonData(editData: EditData) {
            viewModel.updateGifticonData(editData = editData)
        }
    }

    override val builtInThumbnailListener: BuiltInThumbnailListener =
        object : BuiltInThumbnailListener {
            override val selectedGifticonDataFlow: Flow<GifticonData>
                get() = viewModel.gifticonData.filterNotNull()

            override fun updateGifticonData(editData: EditData) {
                viewModel.updateGifticonData(editData = editData)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpSystemInsetsPadding(binding.root)
        setUpBackPress()
        setUpPreview()
        setUpPropertyChipList()
        setUpRecycleEditor()
        setUpCollectState()
        setUpOnClickEvent()
    }

    override fun onResume() {
        super.onResume()

        if (!BeepPermission.checkStoragePermission(this)) {
            finish()
        }
    }

    private fun setUpBackPress() {
        onBackPressedDispatcher.addCallback(this) {
            if (viewModel.selectedEditorChip.value !is EditorChip.Preview) {
                binding.listEditorChip.smoothScrollToPosition(0)
                viewModel.selectEditorChip(EditorChip.Preview)
                return@addCallback
            }
            showExitEditorDialog()
        }
    }

    private fun setUpPreview() {
        val preview = EditorPreviewViewHolder(
            parent = binding.containerPreview,
            requestManager = requestManager,
            listener = onEditorPreviewListener
        )
        preview.bind(viewModel.gifticonId)
        binding.containerPreview.addView(preview.itemView)
        binding.containerPreview.preventTouchPropagation()
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
        binding.btnPreview.doOnPreDraw {
            binding.btnPreview.maxWidth = binding.btnPreview.viewWidth
            binding.listEditorChip.updatePadding(
                left = binding.btnPreview.maxWidth + 4.dp
            )
        }
    }

    private fun setUpRecycleEditor() {
        binding.recyclerEditor.adapter = editorAdapter
        binding.recyclerEditor.itemAnimator = null
        binding.recyclerEditor.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    }

    private fun setUpCollectState() {
        repeatOnStarted {
            viewModel.editorPropertyChipList.collect {
                editorPropertyChipAdapter.submitList(it)
            }
        }

        repeatOnStarted {
            viewModel.selectedEditorChip.collect {
                editorAdapter.submitList(listOf(it))
            }
        }

        repeatOnStarted {
            viewModel.selectedEditorPage.collect {
                binding.btnPreview.isSelected = it == EditorPage.PREVIEW
                binding.textPreview.isSelected = it == EditorPage.PREVIEW
                binding.iconPreview.isSelected = it == EditorPage.PREVIEW
                binding.containerPreview.isVisible = it == EditorPage.PREVIEW

                setVisibleCrop(it == EditorPage.CROP)
            }
        }

        repeatOnStarted {
            viewModel.validGifticon.collect { valid ->
                binding.btnEdit.isActivated = valid
            }
        }
    }

    private fun setVisibleCrop(visible: Boolean) {
        setVisible(binding.containerCrop.id, EditorPage.CROP.name, visible) {
            EditorCropFragment()
        }
    }

    private fun setUpOnClickEvent() {
        binding.btnBack.setOnThrottleClickListener {
            showExitEditorDialog()
        }

        binding.btnPreview.setOnThrottleClickListener {
            binding.listEditorChip.smoothScrollToPosition(0)
            viewModel.selectEditorChip(EditorChip.Preview)
        }

        binding.btnEdit.setOnThrottleClickListener {
            val valid = viewModel.validGifticon.value
            if (valid) {
                lifecycleScope.launch {
                    viewModel.editGifticon()
                    completeEditor()
                }
            } else {
                viewModel.selectInvalidEditType()

                val selectedEditChip = viewModel.selectedEditorChip.value as? EditorChip.Property
                if (selectedEditChip != null) {
                    val position = editorPropertyChipAdapter.getPosition(selectedEditChip)
                    binding.listEditorChip.smoothScrollToPosition(position)
                }

                beepSnackBar.error()
                    .setTextResId(R.string.edit_gifticon_failed)
                    .show()
            }
        }
    }

    private fun showExitEditorDialog() {
        show(TAG_EXIT_EDITOR) {
            val param = ConfirmationParam(
                message = getString(R.string.edit_gifticon_exit_message),
                okText = getString(R.string.edit_gifticon_exit_ok),
                cancelText = getString(R.string.edit_gifticon_exit_cancel)
            )
            ConfirmationDialog.newInstance(param).apply {
                setOnOkClickListener {
                    cancelEditor()
                }
            }
        }
    }

    private fun selectEditorChip(item: EditorChip) {
        val position = when (item) {
            is EditorChip.Preview -> 0
            is EditorChip.Property -> editorPropertyChipAdapter.getPosition(item)
        }
        if (position >= 0) {
            binding.listEditorChip.smoothScrollToPosition(position)
            viewModel.selectEditorChip(item)
        }
    }

    private fun showTextInputDialog(type: EditType) {
        val data = viewModel.gifticonData.value ?: return
        show(type.name) {
            val param = type.createTextInputParam(data)
            supportFragmentManager.setFragmentResultListener(
                TextInputResult.KEY,
                this
            ) { requestKey, data ->
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

    private fun completeEditor() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun cancelEditor() {
        finish()
    }
}