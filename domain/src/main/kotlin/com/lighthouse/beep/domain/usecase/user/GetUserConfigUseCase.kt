package com.lighthouse.beep.domain.usecase.user

import com.lighthouse.beep.data.repository.user.UserRepository
import com.lighthouse.beep.model.user.UserConfig
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserConfigUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {

    operator fun invoke(): Flow<UserConfig> {
        return userRepository.userConfig
    }
}
