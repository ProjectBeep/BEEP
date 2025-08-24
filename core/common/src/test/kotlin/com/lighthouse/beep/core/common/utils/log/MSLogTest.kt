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
class MSLogTest {

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.v(any(), any()) } returns 0
        every { Log.w(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.w(any(), any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
    }

    @Test
    fun `MSLog debug 메서드가 올바르게 동작한다`() {
        val message = "Debug test message"
        
        MSLog.d(message)
        
        verify { Log.d("kim_ms", match { it.contains(message) }) }
    }

    @Test
    fun `MSLog debug with tag 메서드가 올바르게 동작한다`() {
        val tag = "CustomTag"
        val message = "Debug with tag test"
        
        MSLog.d(tag, message)
        
        verify { Log.d(tag, match { it.contains(message) }) }
    }

    @Test
    fun `MSLog info 메서드가 올바르게 동작한다`() {
        val message = "Info test message"
        
        MSLog.i(message)
        
        verify { Log.i("kim_ms", match { it.contains(message) }) }
    }

    @Test
    fun `MSLog error 메서드가 올바르게 동작한다`() {
        val message = "Error test message"
        
        MSLog.e(message)
        
        verify { Log.e("kim_ms", match { it.contains(message) }) }
    }

    @Test
    fun `MSLog error with throwable이 올바르게 동작한다`() {
        val message = "Error with exception"
        val exception = RuntimeException("Test runtime exception")
        
        MSLog.e(message, exception)
        
        verify { Log.e("kim_ms", match { it.contains(message) }, exception) }
    }

    @Test
    fun `MSLog warning 메서드가 올바르게 동작한다`() {
        val tag = "WarnTag"
        val message = "Warning test message"
        
        MSLog.w(tag, message)
        
        verify { Log.w(tag, match { it.contains(message) }) }
    }

    @Test
    fun `MSLog warning with throwable이 올바르게 동작한다`() {
        val tag = "WarnTag"
        val message = "Warning with exception"
        val exception = IllegalArgumentException("Test illegal argument")
        
        MSLog.w(tag, message, exception)
        
        verify { Log.w(tag, match { it.contains(message) }, exception) }
    }

    @Test
    fun `MSLog verbose 메서드가 올바르게 동작한다`() {
        val tag = "VerboseTag"
        val message = "Verbose test message"
        
        MSLog.v(tag, message)
        
        verify { Log.v(tag, match { it.contains(message) }) }
    }

    @Test
    fun `MSLog stackTrace가 올바르게 동작한다`() {
        val message = "Stack trace test"
        
        MSLog.stackTrace(message)
        
        verify(atLeast = 1) { Log.e("kim_ms", match { it.contains("[Stack Trace] - $message") }) }
    }
}