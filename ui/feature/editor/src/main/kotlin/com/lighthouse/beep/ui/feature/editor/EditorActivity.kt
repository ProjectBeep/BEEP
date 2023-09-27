package com.lighthouse.beep.ui.feature.editor

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.core.ui.decoration.LinearItemDecoration
import com.lighthouse.beep.core.ui.exts.createThrottleClickListener
import com.lighthouse.beep.core.ui.exts.repeatOnStarted
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.navs.result.EditorResult
import com.lighthouse.beep.ui.dialog.confirmation.ConfirmationDialog
import com.lighthouse.beep.ui.dialog.confirmation.ConfirmationParam
import com.lighthouse.beep.ui.feature.editor.adapter.OnSelectedGifticonListener
import com.lighthouse.beep.ui.feature.editor.adapter.SelectedGifticonAdapter
import com.lighthouse.beep.ui.feature.editor.databinding.ActivityEditorBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@AndroidEntryPoint
class EditorActivity : AppCompatActivity() {

    companion object {
        private const val TAG_SELECTED_GIFTICON_DELETE = "Tag.SelectedGifticonDelete"
    }

    private lateinit var binding: ActivityEditorBinding

    private val viewModel by viewModels<EditorViewModel>()

    private val onSelectedGifticonListener = object : OnSelectedGifticonListener {
        override fun isSelectedFlow(item: GalleryImage): Flow<Boolean> {
            return flow {
                emit(true)
            }
        }

        override fun isInvalidFlow(item: GalleryImage): Flow<Boolean> {
            return flow {
                emit(true)
            }
        }

        override fun onClick(item: GalleryImage) {

        }

        override fun onDeleteClick(item: GalleryImage) {
            showSelectedGifticonDeleteDialog(item)
        }
    }

    private fun showSelectedGifticonDeleteDialog(item: GalleryImage) {
        var dialog =
            supportFragmentManager.findFragmentByTag(TAG_SELECTED_GIFTICON_DELETE) as? DialogFragment
        if (dialog == null) {
            val param = ConfirmationParam(
                message = getString(R.string.editor_gifticon_delete_message),
                okText = getString(R.string.editor_gifticon_delete_ok),
                cancelText = getString(R.string.editor_gifticon_delete_cancel)
            )
            dialog = ConfirmationDialog.newInstance(param).apply {
                setOnOkClickListener {
                    viewModel.deleteItem(item)
                }
            }
        }
        dialog.show(supportFragmentManager, TAG_SELECTED_GIFTICON_DELETE)
    }

    private val selectedGifticonAdapter = SelectedGifticonAdapter(
        onSelectedGalleryListener = onSelectedGifticonListener
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setUpSelectedList()
        setUpCollectState()
        setUpOnClickEvent()
    }

    private fun setUpSelectedList() {
        binding.listSelected.adapter = selectedGifticonAdapter
        binding.listSelected.setHasFixedSize(true)
        binding.listSelected.addItemDecoration(LinearItemDecoration(1.5f.dp))
    }

    private fun setUpCollectState() {
        repeatOnStarted {
            viewModel.galleryImage.collect {
                selectedGifticonAdapter.submitList(it)
            }
        }
    }

    private fun setUpOnClickEvent() {
        binding.btnBack.setOnClickListener(createThrottleClickListener {
            val result = EditorResult(viewModel.galleryImage.value)
            setResult(Activity.RESULT_CANCELED, result.createIntent())
            finish()
        })
    }
}