package com.artificialss.showcase.ui.components.map

data class CameraState(
    val latitude: Double = DEFAULT_LAT,
    val longitude: Double = DEFAULT_LNG,
    val zoom: Float = DEFAULT_ZOOM,
) {
    companion object {
        const val DEFAULT_LAT = 40.4168
        const val DEFAULT_LNG = -3.7038
        const val DEFAULT_ZOOM = 14f
    }
}
