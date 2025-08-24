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
class MSLoggerTest {

    private lateinit var logger: MSLogger

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.v(any(), any()) } returns 0
        every { Log.w(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        logger = MSLogger(usePrefix = true)
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
    }

    @Test
    fun `debug 메시지가 올바르게 로그된다`() {
        val message = "Test debug message"
        
        logger.d(message)
        
        verify { Log.d("kim_ms", match { it.contains(message) }) }
    }

    @Test
    fun `info 메시지가 올바르게 로그된다`() {
        val tag = "TestTag"
        val message = "Test info message"
        
        logger.i(tag, message)
        
        verify { Log.i(tag, match { it.contains(message) }) }
    }

    @Test
    fun `error 메시지가 throwable과 함께 올바르게 로그된다`() {
        val tag = "ErrorTag"
        val message = "Test error message"
        val exception = RuntimeException("Test exception")
        
        logger.e(tag, message, exception)
        
        verify { Log.e(tag, match { it.contains(message) }, exception) }
    }

    @Test
    fun `prefix가 활성화되었을 때 메시지에 위치 정보가 포함된다`() {
        val loggerWithPrefix = MSLogger(usePrefix = true)
        val message = "Test message with prefix"
        
        loggerWithPrefix.d(message)
        
        verify { 
            Log.d("kim_ms", match { 
                it.contains("[") && it.contains("]") && it.contains(message)
            }) 
        }
    }

    @Test
    fun `prefix가 비활성화되었을 때 메시지에 위치 정보가 포함되지 않는다`() {
        val loggerWithoutPrefix = MSLogger(usePrefix = false)
        val message = "Test message without prefix"
        
        loggerWithoutPrefix.d(message)
        
        verify { Log.d("kim_ms", message) }
    }

    @Test
    fun `stackTrace가 올바르게 출력된다`() {
        val tag = "StackTag"
        val message = "Stack trace test"
        
        logger.stackTrace(tag, message)
        
        verify(atLeast = 1) { Log.e(tag, match { it.contains("[Stack Trace] - $message") }) }
        verify(atLeast = 1) { Log.d(tag, match { it.contains("at ") && it.contains("(") && it.contains(":") }) }
    }
}