package com.lighthouse.beep.library.recognizer.recognizer.syrup

import com.lighthouse.beep.library.recognizer.recognizer.TemplateRecognizer

internal class SyrupRecognizer : TemplateRecognizer() {

    override val parser = SyrupParser()

    override val processor = SyrupProcessor()
}
