package com.artificialss.showcase.domain.model

data class UserProfile(
    val id: String,
    val name: String,
    val role: String,
    val email: String,
    val avatarUrl: String,
    val projectCount: Int,
    val reviewCount: Int,
    val starCount: Int,
)
