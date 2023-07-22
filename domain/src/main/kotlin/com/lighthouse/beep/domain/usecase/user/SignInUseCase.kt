package com.lighthouse.beep.domain.usecase.user

import com.lighthouse.beep.auth.model.OAuthRequest
import com.lighthouse.beep.auth.repository.AuthRepository
import com.lighthouse.beep.data.repository.user.UserRepository
import com.lighthouse.beep.model.deviceconfig.AuthProvider
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(provider: AuthProvider, accessToken: String = ""): Result<Unit> {
        return runCatching {
            val authInfo = authRepository.signIn(OAuthRequest(provider, accessToken))
            userRepository.setAuthInfo(authInfo)
        }.onFailure {
            it.printStackTrace()
        }
    }
}
