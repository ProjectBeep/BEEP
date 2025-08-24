package com.lighthouse.beep.core.common.utils.log

import android.util.Log
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class MSTimeCheckerTest {

    private lateinit var mockLogger: MSLogger
    private lateinit var timeChecker: MSTimeChecker

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        
        mockLogger = mockk<MSLogger>(relaxed = true)
        timeChecker = MSTimeChecker(mockLogger)
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
    }

    @Test
    fun `tick이 올바르게 호출된다`() {
        val tag = "TestTag"
        val message = "Test tick message"
        
        timeChecker.tick(tag, message)
        
        verify { mockLogger.d("time_checker", "Tick[$tag] - $message") }
    }

    @Test
    fun `tick with empty message가 올바르게 호출된다`() {
        val tag = "TestTag"
        
        timeChecker.tick(tag, "")
        
        verify { mockLogger.d("time_checker", "Tick[$tag]") }
    }

    @Test
    fun `tock이 올바르게 호출된다`() {
        val tag = "TestTag"
        val message = "Test tock message"
        
        // tick 먼저 호출
        timeChecker.tick(tag, "")
        Thread.sleep(10) // 시간 차이를 만들기 위해
        
        timeChecker.tock(tag, message)
        
        verify { mockLogger.d("time_checker", match { it.startsWith("Tock[$tag](") && it.contains("$message") }) }
    }

    @Test
    fun `tock without tick은 에러 로그를 출력한다`() {
        val tag = "NonExistentTag"
        
        timeChecker.tock(tag, "")
        
        verify { mockLogger.e("time_checker", "일치하는 Tag[$tag]가 없습니다.") }
    }

    @Test
    fun `동일한 tag로 tick을 여러 번 호출하면 경고가 출력된다`() {
        val tag = "DuplicateTag"
        
        timeChecker.tick(tag, "First tick")
        timeChecker.tick(tag, "Second tick")
        
        verify { mockLogger.e("time_checker", "동일한 Tag[$tag]가 들어와 시간을 갱신합니다") }
    }

    @Test
    fun `tick-tock 순서가 올바르게 동작한다`() {
        val tag = "OrderTest"
        
        timeChecker.tick(tag, "Start")
        Thread.sleep(5)
        timeChecker.tock(tag, "End")
        
        verify { mockLogger.d("time_checker", "Tick[$tag] - Start") }
        verify { mockLogger.d("time_checker", match { it.startsWith("Tock[$tag](") && it.contains("End") }) }
    }

    @Test
    fun `시간 포맷이 올바르게 생성된다`() {
        val tag = "FormatTest"
        
        timeChecker.tick(tag, "")
        Thread.sleep(100) // 100ms 대기
        timeChecker.tock(tag, "")
        
        verify { 
            mockLogger.d("time_checker", match { 
                val result = it
                // "Tock[FormatTest](0 00:00:00.xxx)" 형식 확인
                result.contains("Tock[$tag](") && 
                result.contains("00:00:00.") &&
                result.contains(")")
            }) 
        }
    }
}