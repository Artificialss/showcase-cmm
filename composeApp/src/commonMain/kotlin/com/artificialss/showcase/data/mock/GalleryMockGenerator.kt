package com.artificialss.showcase.data.mock

import com.artificialss.showcase.domain.model.GalleryItem

object GalleryMockGenerator {

    private val GALLERY_ITEMS = listOf(
        GalleryItem("img_0", "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=600", "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=200", "Mountain Lake", "Nature"),
        GalleryItem("img_1", "https://images.unsplash.com/photo-1469474968028-56623f02e42e?w=600", "https://images.unsplash.com/photo-1469474968028-56623f02e42e?w=200", "Sunlit Valley", "Nature"),
        GalleryItem("img_2", "https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=600", "https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=200", "Autumn Road", "Travel"),
        GalleryItem("img_3", "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?w=600", "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?w=200", "Foggy Forest", "Nature"),
        GalleryItem("img_4", "https://images.unsplash.com/photo-1447752875215-b2761acb3c5d?w=600", "https://images.unsplash.com/photo-1447752875215-b2761acb3c5d?w=200", "Forest Path", "Nature"),
        GalleryItem("img_5", "https://images.unsplash.com/photo-1472214103451-9374bd1c798e?w=600", "https://images.unsplash.com/photo-1472214103451-9374bd1c798e?w=200", "Green Hills", "Landscape"),
        GalleryItem("img_6", "https://images.unsplash.com/photo-1433086966358-54859d0ed716?w=600", "https://images.unsplash.com/photo-1433086966358-54859d0ed716?w=200", "Waterfall", "Nature"),
        GalleryItem("img_7", "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=600", "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=200", "Tropical Beach", "Travel"),
        GalleryItem("img_8", "https://images.unsplash.com/photo-1519681393784-d120267933ba?w=600", "https://images.unsplash.com/photo-1519681393784-d120267933ba?w=200", "Starry Mountains", "Night"),
        GalleryItem("img_9", "https://images.unsplash.com/photo-1475924156734-496f401b0bcc?w=600", "https://images.unsplash.com/photo-1475924156734-496f401b0bcc?w=200", "Desert Dunes", "Landscape"),
        GalleryItem("img_10", "https://images.unsplash.com/photo-1505144808419-1957a94ca61e?w=600", "https://images.unsplash.com/photo-1505144808419-1957a94ca61e?w=200", "Ocean Sunset", "Travel"),
        GalleryItem("img_11", "https://images.unsplash.com/photo-1439853949127-fa647821eba0?w=600", "https://images.unsplash.com/photo-1439853949127-fa647821eba0?w=200", "Blue Sky Lake", "Nature"),
    )

    @Suppress("UNUSED_PARAMETER")
    fun generate(count: Int = GALLERY_ITEMS.size, seed: Long = 0L): List<GalleryItem> {
        return GALLERY_ITEMS.take(count)
    }
}
