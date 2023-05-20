package com.lighthouse.beep.library.recognizer.recognizer.giftishow

import com.lighthouse.beep.library.recognizer.recognizer.TemplateRecognizer

internal class GiftishowRecognizer : TemplateRecognizer() {

    override val parser = GiftishowParser()

    override val processor = GiftishowProcessor()
}
