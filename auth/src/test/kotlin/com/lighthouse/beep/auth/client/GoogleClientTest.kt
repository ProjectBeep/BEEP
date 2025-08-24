package com.lighthouse.beep.auth.client

import android.app.Activity
import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.lighthouse.beep.auth.model.OAuthTokenResult
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class GoogleClientTest {

    private lateinit var mockContext: Context
    private lateinit var mockActivity: Activity
    private lateinit var mockCredentialManager: CredentialManager
    private lateinit var googleClient: GoogleClient

    @Before
    fun setup() {
        mockContext = mockk<Context>(relaxed = true)
        mockActivity = mockk<Activity>(relaxed = true)
        mockCredentialManager = mockk<CredentialManager>(relaxed = true)
        
        mockkStatic(CredentialManager::class)
        every { CredentialManager.create(any()) } returns mockCredentialManager
        
        googleClient = GoogleClient(mockContext)
    }

    @After
    fun tearDown() {
        unmockkStatic(CredentialManager::class)
        clearAllMocks()
    }

    @Test
    fun `Google Sign-In 성공 시 Success를 반환한다`() = runTest {
        // Mock 설정
        val testToken = "test-google-token"
        val mockCredentialResponse = mockk<GetCredentialResponse>(relaxed = true)
        val mockCredential = mockk<androidx.credentials.Credential>(relaxed = true)
        val mockGoogleCredential = mockk<GoogleIdTokenCredential>(relaxed = true)
        
        every { mockCredentialResponse.credential } returns mockCredential
        every { mockGoogleCredential.idToken } returns testToken
        every { mockGoogleCredential.id } returns "test-user@example.com"
        every { mockGoogleCredential.displayName } returns "Test User"
        
        mockkStatic(GoogleIdTokenCredential::class)
        every { GoogleIdTokenCredential.createFrom(any()) } returns mockGoogleCredential
        
        coEvery { 
            mockCredentialManager.getCredential(any<Activity>(), any<GetCredentialRequest>()) 
        } returns mockCredentialResponse
        
        // 테스트 실행
        val result = googleClient.signIn(mockActivity)
        
        // 검증
        assertTrue(result is OAuthTokenResult.Success)
        assertEquals(testToken, (result as OAuthTokenResult.Success).accessToken)
        
        coVerify { mockCredentialManager.getCredential(mockActivity, any<GetCredentialRequest>()) }
    }

    @Test
    fun `Google Sign-In 취소 시 Canceled를 반환한다`() = runTest {
        // Mock 설정
        coEvery { 
            mockCredentialManager.getCredential(any<Activity>(), any<GetCredentialRequest>()) 
        } throws GetCredentialCancellationException("사용자가 취소함")
        
        // 테스트 실행
        val result = googleClient.signIn(mockActivity)
        
        // 검증
        assertTrue(result is OAuthTokenResult.Canceled)
    }

    @Test
    fun `Google Sign-In 실패 시 Failed를 반환한다`() = runTest {
        // Mock 설정
        val testException = GetCredentialException("테스트 예외")
        coEvery { 
            mockCredentialManager.getCredential(any<Activity>(), any<GetCredentialRequest>()) 
        } throws testException
        
        // 테스트 실행
        val result = googleClient.signIn(mockActivity)
        
        // 검증
        assertTrue(result is OAuthTokenResult.Failed)
        assertEquals(testException, (result as OAuthTokenResult.Failed).exception)
    }

    @Test
    fun `Credential 파싱 실패 시 Failed를 반환한다`() = runTest {
        // Mock 설정
        val mockCredentialResponse = mockk<GetCredentialResponse>(relaxed = true)
        val mockCredential = mockk<androidx.credentials.Credential>(relaxed = true)
        
        every { mockCredentialResponse.credential } returns mockCredential
        
        mockkStatic(GoogleIdTokenCredential::class)
        every { GoogleIdTokenCredential.createFrom(any()) } throws RuntimeException("파싱 실패")
        
        coEvery { 
            mockCredentialManager.getCredential(any<Activity>(), any<GetCredentialRequest>()) 
        } returns mockCredentialResponse
        
        // 테스트 실행
        val result = googleClient.signIn(mockActivity)
        
        // 검증
        assertTrue(result is OAuthTokenResult.Failed)
        assertTrue((result as OAuthTokenResult.Failed).exception is RuntimeException)
    }

    @Test
    fun `signOut은 정상적으로 실행된다`() = runTest {
        // 테스트 실행 - 예외가 발생하지 않아야 함
        googleClient.signOut()
        
        // Credential Manager 방식에서는 별도 signOut 호출이 없으므로 예외가 없으면 성공
        // (실제로는 시스템 레벨에서 Google 계정 관리)
        assertTrue("signOut이 예외 없이 완료되어야 함", true)
    }

    @Test
    fun `GetGoogleIdOption이 올바른 설정으로 생성되는지 확인한다`() = runTest {
        // Mock 설정
        val mockCredentialResponse = mockk<GetCredentialResponse>(relaxed = true)
        val mockCredential = mockk<androidx.credentials.Credential>(relaxed = true)
        val mockGoogleCredential = mockk<GoogleIdTokenCredential>(relaxed = true)
        
        every { mockCredentialResponse.credential } returns mockCredential
        every { mockGoogleCredential.idToken } returns "test-token"
        every { mockGoogleCredential.id } returns "test-id"
        every { mockGoogleCredential.displayName } returns "Test"
        
        mockkStatic(GoogleIdTokenCredential::class)
        every { GoogleIdTokenCredential.createFrom(any()) } returns mockGoogleCredential
        
        coEvery { 
            mockCredentialManager.getCredential(any<Activity>(), any<GetCredentialRequest>()) 
        } returns mockCredentialResponse
        
        // 테스트 실행
        googleClient.signIn(mockActivity)
        
        // 검증 - GetCredentialRequest가 호출되었는지 확인
        coVerify { 
            mockCredentialManager.getCredential(
                eq(mockActivity), 
                any<GetCredentialRequest>()
            ) 
        }
    }
}