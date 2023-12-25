package com.lighthouse.beep.ui.feature.editor

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.lighthouse.beep.core.common.exts.EMPTY_DATE
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.core.ui.content.OnContentChangeListener
import com.lighthouse.beep.core.ui.content.registerGalleryContentObserver
import com.lighthouse.beep.core.ui.recyclerview.decoration.LinearItemDecoration
import com.lighthouse.beep.core.ui.exts.createThrottleClickListener
import com.lighthouse.beep.core.ui.exts.dismiss
import com.lighthouse.beep.core.ui.exts.repeatOnStarted
import com.lighthouse.beep.core.ui.exts.setVisible
import com.lighthouse.beep.core.ui.exts.show
import com.lighthouse.beep.core.ui.exts.viewWidth
import com.lighthouse.beep.core.ui.recyclerview.scroller.CenterScrollLayoutManager
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.navs.result.EditorResult
import com.lighthouse.beep.permission.BeepPermission
import com.lighthouse.beep.permission.ext.setUpRequirePermission
import com.lighthouse.beep.ui.designsystem.snackbar.BeepSnackBar
import com.lighthouse.beep.ui.designsystem.snackbar.BeepSnackBarAction
import com.lighthouse.beep.ui.dialog.confirmation.ConfirmationDialog
import com.lighthouse.beep.ui.dialog.confirmation.ConfirmationParam
import com.lighthouse.beep.ui.dialog.datepicker.DatePickerDialog
import com.lighthouse.beep.ui.dialog.datepicker.DatePickerParam
import com.lighthouse.beep.ui.dialog.datepicker.DatePickerResult
import com.lighthouse.beep.ui.dialog.progress.ProgressDialog
import com.lighthouse.beep.ui.dialog.progress.ProgressParam
import com.lighthouse.beep.ui.dialog.textinput.TextInputDialog
import com.lighthouse.beep.ui.dialog.textinput.TextInputResult
import com.lighthouse.beep.ui.feature.editor.adapter.chip.EditorPropertyChipAdapter
import com.lighthouse.beep.ui.feature.editor.adapter.chip.OnEditorPropertyChipListener
import com.lighthouse.beep.ui.feature.editor.adapter.editor.EditorAdapter
import com.lighthouse.beep.ui.feature.editor.adapter.editor.OnEditorExpiredListener
import com.lighthouse.beep.ui.feature.editor.adapter.editor.OnEditorMemoListener
import com.lighthouse.beep.ui.feature.editor.adapter.editor.OnEditorTextListener
import com.lighthouse.beep.ui.feature.editor.adapter.editor.OnEditorThumbnailListener
import com.lighthouse.beep.ui.feature.editor.adapter.gifticon.OnEditorGifticonListener
import com.lighthouse.beep.ui.feature.editor.adapter.gifticon.EditorGifticonAdapter
import com.lighthouse.beep.ui.feature.editor.adapter.preview.EditorPreviewAdapter
import com.lighthouse.beep.ui.feature.editor.adapter.preview.OnEditorPreviewListener
import com.lighthouse.beep.ui.feature.editor.databinding.ActivityEditorBinding
import com.lighthouse.beep.ui.feature.editor.dialog.BuiltInThumbnailDialog
import com.lighthouse.beep.ui.feature.editor.model.EditData
import com.lighthouse.beep.ui.feature.editor.model.EditorChip
import com.lighthouse.beep.ui.feature.editor.model.EditType
import com.lighthouse.beep.ui.feature.editor.model.EditorPage
import com.lighthouse.beep.ui.feature.editor.model.GifticonData
import com.lighthouse.beep.ui.feature.editor.model.EditGifticonThumbnail
import com.lighthouse.beep.ui.feature.editor.page.crop.EditorCropFragment
import com.lighthouse.beep.ui.feature.editor.provider.OnEditorProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlin.math.max
import com.lighthouse.beep.theme.R as ThemeR

@AndroidEntryPoint
internal class EditorActivity : AppCompatActivity(), OnEditorProvider {

    companion object {
        private const val TAG_SELECTED_GIFTICON_DELETE = "Tag.SelectedGifticonDelete"
        private const val TAG_EXIT_EDITOR = "Tag.ExitEditor"
    }

    private lateinit var binding: ActivityEditorBinding

    private val viewModel by viewModels<EditorViewModel>()

    override val requestManager: RequestManager by lazy {
        Glide.with(this)
    }

    private val beepSnackBar by lazy {
        BeepSnackBar.Builder(this)
            .setLifecycleOwner(this)
            .setRootView(binding.root)
    }

    private val onPreviewScrollListener = object : RecyclerView.OnScrollListener() {
        private var lastPosition = -1

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                val offset = recyclerView.computeHorizontalScrollOffset()
                val extent = recyclerView.computeHorizontalScrollExtent()
                val position = offset / extent
                if (position != lastPosition) {
                    lastPosition = position
                    binding.listSelected.smoothScrollToPosition(position)
                    viewModel.selectGifticon(position)
                }
            }
        }
    }

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

        override fun getCropDataFlow(item: GalleryImage): Flow<EditGifticonThumbnail> {
            return viewModel.gifticonDataMapFlow
                .map { map -> map[item.id]?.thumbnail }
                .filterNotNull()
                .distinctUntilChanged()
        }

        override fun onClick(item: GalleryImage, position: Int) {
            viewModel.selectGifticon(item)
            binding.listSelected.smoothScrollToPosition(position)
            binding.listPreview.scrollToPosition(position)
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
                    val data = viewModel.getGifticonData(item.id)
                    viewModel.deleteItem(item)

                    if (viewModel.galleryImage.value.isNotEmpty()) {
                        beepSnackBar.info()
                            .setTextResId(R.string.editor_gifticon_delete_success)
                            .setAction(BeepSnackBarAction.Text(
                                textResId = R.string.editor_gifticon_delete_revert,
                                listener = {
                                    viewModel.revertDeleteItem(item, data)
                                }
                            )).show()
                    } else {
                        cancelEditor()
                    }
                }
            }
        }
    }

    private val editorGifticonAdapter by lazy {
        EditorGifticonAdapter(
            requestManager = requestManager,
            onSelectedGalleryListener = onEditorGifticonListener,
        )
    }

    private val onEditorPreviewListener = object : OnEditorPreviewListener {
        override fun getGifticonDataFlow(image: GalleryImage): Flow<GifticonData> {
            return viewModel.gifticonDataMapFlow
                .map { it[image.id] }
                .filterNotNull()
                .distinctUntilChanged()
        }

        override fun onCashChange(item: GalleryImage, isCash: Boolean) {
            viewModel.updateGifticonData(item, EditData.Cash(isCash))
        }

        override fun onEditClick(editType: EditType) {
            selectEditorChip(EditorChip.Property(editType))
        }
    }

    private val editorPreviewAdapter by lazy {
        EditorPreviewAdapter(
            requestManager = requestManager,
            onEditorPreviewListener = onEditorPreviewListener,
        )
    }

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

    private val editorPropertyChipAdapter = EditorPropertyChipAdapter(
        onEditorPropertyChipListener = onEditorPropertyChipListener
    )

    private val onEditorMemoListener = object : OnEditorMemoListener {
        override fun getMemoFlow(): Flow<String> {
            return viewModel.selectedGifticonDataFlow
                .map { it.memo }
                .distinctUntilChanged()
        }

        override fun onMemoClick() {
            showTextInputDialog(EditType.MEMO)
        }
    }

    private fun showBuiltInThumbnailDialog() {
        show(BuiltInThumbnailDialog.TAG) {
            BuiltInThumbnailDialog()
        }
    }

    private val onEditorThumbnailListener = object : OnEditorThumbnailListener {
        override fun getThumbnailFlow(): Flow<EditGifticonThumbnail> {
            return viewModel.selectedGifticonDataFlow
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

    private fun showTextInputDialog(type: EditType) {
        val data = viewModel.selectedGifticonData.value ?: return
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

    private val onEditorTextListener = object : OnEditorTextListener {
        override fun getTextFlow(type: EditType): Flow<String> {
            return viewModel.selectedGifticonDataFlow
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

            val expired = viewModel.selectedGifticonData.value?.expireAt ?: EMPTY_DATE
            val param = DatePickerParam(expired)
            DatePickerDialog.newInstance(param)
        }
    }

    private val onEditorExpiredListener = object : OnEditorExpiredListener {
        override fun getGifticonDataFlow(): Flow<GifticonData> {
            return viewModel.selectedGifticonDataFlow
                .distinctUntilChanged()
        }

        override fun showExpired() {
            showExpiredDialog()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setUpRequirePermission(BeepPermission.storage)
        setUpGalleryContentObserver()
        setUpBackPress()
        setUpGifticonList()
        setUpPreviewList()
        setUpPropertyChipList()
        setUpRecycleEditor()
        setUpCollectState()
        setUpOnClickEvent()
    }

    private fun setUpGalleryContentObserver() {
        registerGalleryContentObserver(object : OnContentChangeListener {
            override fun onInsert(id: Long) = Unit

            override fun onDelete(id: Long) {
                viewModel.deleteGalleryContent(id)
            }
        })
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

    private fun setUpGifticonList() {
        binding.listSelected.adapter = editorGifticonAdapter
        binding.listSelected.setHasFixedSize(true)
        binding.listSelected.addItemDecoration(LinearItemDecoration(14.5f.dp))
        binding.listSelected.layoutManager =
            CenterScrollLayoutManager(this, RecyclerView.HORIZONTAL, false)
    }

    private fun setUpPreviewList() {
        binding.listPreview.adapter = editorPreviewAdapter
        binding.listPreview.setHasFixedSize(true)
        binding.listPreview.addOnScrollListener(onPreviewScrollListener)
        PagerSnapHelper().attachToRecyclerView(binding.listPreview)
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
                val param = ProgressParam(getColor(ThemeR.color.black_30))
                ProgressDialog.newInstance(param).apply {
                    setOnCancelListener {
                        cancelEditor()
                    }
                }
            }
        } else {
            dismiss(ProgressDialog.TAG)
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
                if (it.isNotEmpty()) {
                    editorGifticonAdapter.submitList(it)
                    editorPreviewAdapter.submitList(it)
                }
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
                binding.listPreview.isVisible = it == EditorPage.PREVIEW

                setVisibleCrop(it == EditorPage.CROP)
            }
        }

        repeatOnStarted {
            viewModel.recognizeLoading.collect { isLoading ->
                showProgress(isLoading)
            }
        }

        repeatOnStarted {
            viewModel.validGifticonList.collect { list ->
                binding.btnRegister.isActivated = list.isNotEmpty()
                binding.btnRegister.text = when (list.size) {
                    0 -> getString(R.string.editor_gifticon_register)
                    else -> getString(R.string.editor_gifticon_register_valid, list.size)
                }
            }
        }
    }

    private fun setUpOnClickEvent() {
        binding.btnBack.setOnClickListener(createThrottleClickListener {
            showExitEditorDialog()
        })

        binding.btnPreview.setOnClickListener(createThrottleClickListener {
            binding.listEditorChip.smoothScrollToPosition(0)
            viewModel.selectEditorChip(EditorChip.Preview)
        })

        binding.btnRegister.setOnClickListener(createThrottleClickListener {
            val validMap = viewModel.validGifticonList.value
            if (validMap.isNotEmpty()) {
                viewModel.registerGifticon(validMap)

                if (viewModel.galleryImage.value.isNotEmpty()) {
                    beepSnackBar.info()
                        .setText(getString(R.string.editor_gifticon_register_partial_success, validMap.size))
                        .setAction(BeepSnackBarAction.Text(
                            textResId = R.string.editor_gifticon_register_revert,
                            listener = {
                                viewModel.revertRegisterGifticon(validMap)
                            }
                        )).show()
                } else {
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
                    .setTextResId(R.string.editor_gifticon_register_failed)
                    .show()
            }
        })
    }

    private fun setVisibleCrop(visible: Boolean) {
        setVisible(binding.containerCrop.id, EditorPage.CROP.name, visible) {
            EditorCropFragment()
        }
    }

    private fun showExitEditorDialog() {
        show(TAG_EXIT_EDITOR) {
            val param = ConfirmationParam(
                message = getString(R.string.editor_gifticon_exit_message),
                okText = getString(R.string.editor_gifticon_exit_ok),
                cancelText = getString(R.string.editor_gifticon_exit_cancel)
            )
            ConfirmationDialog.newInstance(param).apply {
                setOnOkClickListener {
                    cancelEditor()
                }
            }
        }
    }

    private fun completeEditor() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun cancelEditor() {
        val result = EditorResult(viewModel.galleryImage.value)
        setResult(Activity.RESULT_CANCELED, result.createIntent())
        finish()
    }
}