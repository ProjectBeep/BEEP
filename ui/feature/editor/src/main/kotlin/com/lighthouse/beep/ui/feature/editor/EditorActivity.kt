package com.lighthouse.beep.ui.feature.editor

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.lighthouse.beep.core.ui.exts.createThrottleClickListener
import com.lighthouse.beep.ui.feature.editor.databinding.ActivityEditorBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditorActivity : AppCompatActivity(){

    private lateinit var binding: ActivityEditorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setUpSelectedList()
        setUpCollectState()
        setUpOnClickEvent()
    }

    private fun setUpSelectedList() {

    }

    private fun setUpCollectState() {

    }

    private fun setUpOnClickEvent() {
        binding.btnBack.setOnClickListener(createThrottleClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        })
    }
}