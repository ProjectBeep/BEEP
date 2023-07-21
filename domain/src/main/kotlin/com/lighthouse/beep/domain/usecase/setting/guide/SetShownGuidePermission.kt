package com.lighthouse.beep.domain.usecase.setting.guide

import com.lighthouse.beep.data.repository.user.UserRepository
import javax.inject.Inject

class SetShownGuidePermission @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(shownGuidePermission: Boolean) {
        val currentShownGuidePage = userRepository.getShownGuidePage()
        userRepository.setShownGuidePage(
            currentShownGuidePage.copy(permission = shownGuidePermission),
        )
    }
}
