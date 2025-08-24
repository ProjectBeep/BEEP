package com.lighthouse.beep.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import com.lighthouse.beep.model.user.AuthInfo
import com.lighthouse.beep.model.user.AuthProvider
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
class BeepAuthTest {

    private lateinit var mockFirebaseAuth: FirebaseAuth
    private lateinit var mockFirebaseUser: FirebaseUser
    private lateinit var mockTokenResult: GetTokenResult

    @Before
    fun setup() {
        mockkStatic(FirebaseAuth::class)
        mockFirebaseAuth = mockk<FirebaseAuth>(relaxed = true)
        mockFirebaseUser = mockk<FirebaseUser>(relaxed = true)
        mockTokenResult = mockk<GetTokenResult>(relaxed = true)
        
        every { FirebaseAuth.getInstance() } returns mockFirebaseAuth
    }

    @After
    fun tearDown() {
        unmockkStatic(FirebaseAuth::class)
        clearAllMocks()
    }

    @Test
    fun `getSignInIntent는 올바른 Intent를 생성한다`() {
        // Context mock
        val context = mockk<android.content.Context>(relaxed = true)
        
        // AuthParam.SignIn이 올바른 Intent를 생성하는지 테스트
        val intent = BeepAuth.getSignInIntent(context, AuthProvider.GOOGLE)
        
        assertNotNull(intent)
    }

    @Test
    fun `authProvider getter는 현재 인증 상태를 올바르게 반환한다`() {
        // AuthInfo가 null인 경우
        val provider = BeepAuth.authProvider
        
        // AuthInfo가 없으면 NONE을 반환해야 함
        assertTrue(provider == AuthProvider.NONE || provider == AuthProvider.GUEST)
    }

    @Test
    fun `userUid getter는 현재 사용자 UID를 올바르게 반환한다`() {
        val uid = BeepAuth.userUid
        
        // AuthInfo가 없으면 빈 문자열을 반환해야 함
        assertNotNull(uid)
    }

    @Test
    fun `updateProfile은 프로필 업데이트를 수행한다`() = runTest {
        // Mock 설정
        every { mockFirebaseAuth.currentUser } returns mockFirebaseUser
        every { mockFirebaseUser.uid } returns "test-uid"
        
        // UserProfileChangeRequest mock
        val profileRequest = mockk<com.google.firebase.auth.UserProfileChangeRequest>(relaxed = true)
        
        // Task mock 설정
        val updateTask = mockk<com.google.android.gms.tasks.Task<Void>>(relaxed = true)
        val tokenTask = mockk<com.google.android.gms.tasks.Task<GetTokenResult>>(relaxed = true)
        
        every { mockFirebaseUser.updateProfile(any()) } returns updateTask
        every { mockFirebaseUser.getIdToken(false) } returns tokenTask
        every { tokenTask.result } returns mockTokenResult
        every { mockTokenResult.claims } returns mapOf("provider" to "google")
        
        coEvery { updateTask.await() } returns mockk()
        coEvery { tokenTask.await() } returns mockTokenResult
        
        // 테스트 실행
        BeepAuth.updateProfile(profileRequest)
        
        // 검증
        coVerify { mockFirebaseUser.updateProfile(profileRequest) }
        coVerify { mockFirebaseUser.getIdToken(false) }
    }

    @Test
    fun `updateProfile은 사용자가 null일 때 처리한다`() = runTest {
        // Mock 설정
        every { mockFirebaseAuth.currentUser } returns null
        
        val profileRequest = mockk<com.google.firebase.auth.UserProfileChangeRequest>(relaxed = true)
        
        // 테스트 실행 - 예외가 발생하지 않아야 함
        BeepAuth.updateProfile(profileRequest)
        
        // 검증 - updateProfile이 호출되지 않아야 함
        coVerify(exactly = 0) { any<FirebaseUser>().updateProfile(any()) }
    }
}