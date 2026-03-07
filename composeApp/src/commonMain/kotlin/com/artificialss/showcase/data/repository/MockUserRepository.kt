package com.artificialss.showcase.data.repository

import com.artificialss.showcase.data.mock.UserMockGenerator
import com.artificialss.showcase.domain.model.ActivityItem
import com.artificialss.showcase.domain.model.UserProfile

class MockUserRepository : UserRepository {

    private var cachedProfile: UserProfile = UserMockGenerator.generateProfile()

    override fun getProfile(): UserProfile = cachedProfile

    override fun getRecentActivity(): List<ActivityItem> = UserMockGenerator.generateActivity()

    override fun updateProfile(name: String, role: String, email: String): UserProfile {
        cachedProfile = cachedProfile.copy(name = name, role = role, email = email)
        return cachedProfile
    }

    override fun updateAvatarUrl(url: String): UserProfile {
        cachedProfile = cachedProfile.copy(avatarUrl = url)
        return cachedProfile
    }
}
