package com.artificialss.showcase.data.mock

import com.artificialss.showcase.domain.model.ActivityItem
import com.artificialss.showcase.domain.model.UserProfile

object UserMockGenerator {

    fun generateProfile(): UserProfile = UserProfile(
        id = "user_1",
        name = "Elena Rodriguez",
        role = "Senior Product Designer",
        email = "elena.rodriguez@artificialss.com",
        avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200",
        projectCount = PROJECT_COUNT,
        reviewCount = REVIEW_COUNT,
        starCount = STAR_COUNT,
    )

    fun generateActivity(): List<ActivityItem> = listOf(
        ActivityItem("act_0", "Pushed to main", "Merged feature/dashboard-charts", "2 hours ago"),
        ActivityItem("act_1", "Code review", "Approved PR #142 — Gallery screen refactor", "5 hours ago"),
        ActivityItem("act_2", "Created issue", "Bug: Map markers not loading on iOS", "1 day ago"),
        ActivityItem("act_3", "Released v1.2.0", "Published to internal TestFlight", "2 days ago"),
        ActivityItem("act_4", "Updated docs", "Architecture decision record for Navigation 3", "3 days ago"),
        ActivityItem("act_5", "Sprint planning", "Added 8 stories to Sprint 14 backlog", "4 days ago"),
    )

    private const val PROJECT_COUNT = 12
    private const val REVIEW_COUNT = 48
    private const val STAR_COUNT = 320
}
