package com.artificialss.showcase.data.repository

import com.artificialss.showcase.domain.model.ActivityItem
import com.artificialss.showcase.domain.model.UserProfile

interface UserRepository {

    fun getProfile(): UserProfile

    fun getRecentActivity(): List<ActivityItem>

    fun updateProfile(name: String, role: String, email: String): UserProfile

    fun updateAvatarUrl(url: String): UserProfile
}
