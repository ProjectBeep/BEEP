package com.lighthouse.beep.library.recognizer.recognizer.smilecon

import com.lighthouse.beep.library.recognizer.recognizer.TemplateRecognizer

internal class SmileConRecognizer : TemplateRecognizer() {

    override val parser = SmileConParser()

    override val processor = SmileConProcessor()
}
