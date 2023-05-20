package com.lighthouse.beep.library.recognizer.recognizer.kakao

import com.lighthouse.beep.library.recognizer.recognizer.TemplateRecognizer

internal class KakaoRecognizer : TemplateRecognizer() {

    override val parser = KakaoParser()

    override val processor = KakaoProcessor()
}
