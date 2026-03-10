package com.artificialss.showcase.ui.feature.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.ViewModule
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import com.artificialss.showcase.domain.model.GalleryItem
import com.artificialss.showcase.ui.components.ErrorMessage
import com.artificialss.showcase.ui.components.LoadingIndicator
import com.artificialss.showcase.ui.localization.LocalAppStrings
import com.artificialss.showcase.ui.theme.ShapeStyle

private enum class GalleryLayout { GRID_2, GRID_3, LIST }
private enum class GallerySortOrder { TITLE_ASC, TITLE_DESC, ALBUM }

@Composable
fun GalleryScreen(
    presenter: GalleryPresenter,
    shapeStyle: ShapeStyle = ShapeStyle.ROUNDED,
    modifier: Modifier = Modifier,
) {
    val state by presenter.uiState.collectAsStateWithLifecycle()

    when (val current = state) {
        is GalleryUiState.Loading -> LoadingIndicator(modifier = modifier)
        is GalleryUiState.Error -> ErrorMessage(message = current.message, modifier = modifier)
        is GalleryUiState.Success -> {
            Box(modifier = modifier.fillMaxSize()) {
                GalleryContent(
                    items = current.items,
                    shapeStyle = shapeStyle,
                    onItemSelected = { presenter.onItemSelected(it) },
                )
                if (current.selectedItem != null) {
                    FullScreenViewer(
                        item = current.selectedItem,
                        onDismiss = { presenter.onDismissViewer() },
                    )
                }
            }
        }
    }
}

@Composable
private fun GalleryContent(
    items: List<GalleryItem>,
    shapeStyle: ShapeStyle,
    onItemSelected: (GalleryItem) -> Unit,
) {
    var layout by remember { mutableStateOf(GalleryLayout.GRID_2) }
    var sortOrder by remember { mutableStateOf(GallerySortOrder.TITLE_ASC) }

    val sorted = remember(items, sortOrder) {
        when (sortOrder) {
            GallerySortOrder.TITLE_ASC -> items.sortedBy { it.title }
            GallerySortOrder.TITLE_DESC -> items.sortedByDescending { it.title }
            GallerySortOrder.ALBUM -> items.sortedBy { it.albumTitle }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(CONTENT_PADDING),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = LocalAppStrings.current.galleryTitle,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            Row {
                IconButton(
                    onClick = {
                        sortOrder = when (sortOrder) {
                            GallerySortOrder.TITLE_ASC -> GallerySortOrder.TITLE_DESC
                            GallerySortOrder.TITLE_DESC -> GallerySortOrder.ALBUM
                            GallerySortOrder.ALBUM -> GallerySortOrder.TITLE_ASC
                        }
                    },
                    modifier = Modifier.size(ACTION_SIZE),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Sort,
                        contentDescription = SORT_DESC,
                        tint = if (sortOrder != GallerySortOrder.TITLE_ASC) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                    )
                }
                IconButton(
                    onClick = {
                        layout = when (layout) {
                            GalleryLayout.GRID_2 -> GalleryLayout.GRID_3
                            GalleryLayout.GRID_3 -> GalleryLayout.LIST
                            GalleryLayout.LIST -> GalleryLayout.GRID_2
                        }
                    },
                    modifier = Modifier.size(ACTION_SIZE),
                ) {
                    Icon(
                        imageVector = when (layout) {
                            GalleryLayout.GRID_2 -> Icons.Default.GridView
                            GalleryLayout.GRID_3 -> Icons.Default.ViewModule
                            GalleryLayout.LIST -> Icons.AutoMirrored.Filled.List
                        },
                        contentDescription = LAYOUT_DESC,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        when (layout) {
            GalleryLayout.GRID_2 -> GalleryGrid(
                items = sorted,
                columns = GRID_COLUMNS_2,
                shapeStyle = shapeStyle,
                onItemSelected = onItemSelected,
            )
            GalleryLayout.GRID_3 -> GalleryGrid(
                items = sorted,
                columns = GRID_COLUMNS_3,
                shapeStyle = shapeStyle,
                onItemSelected = onItemSelected,
            )
            GalleryLayout.LIST -> GalleryList(
                items = sorted,
                shapeStyle = shapeStyle,
                onItemSelected = onItemSelected,
            )
        }
    }
}

@Composable
private fun GalleryGrid(
    items: List<GalleryItem>,
    columns: Int,
    shapeStyle: ShapeStyle,
    onItemSelected: (GalleryItem) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        contentPadding = PaddingValues(top = GRID_TOP_PADDING, bottom = GRID_BOTTOM_PADDING),
        horizontalArrangement = Arrangement.spacedBy(GRID_SPACING),
        verticalArrangement = Arrangement.spacedBy(GRID_SPACING),
    ) {
        items(items = items, key = { it.id }) { item ->
            GalleryItemCard(
                item = item,
                shapeStyle = shapeStyle,
                onClick = { onItemSelected(item) },
            )
        }
    }
}

@Composable
private fun GalleryList(
    items: List<GalleryItem>,
    shapeStyle: ShapeStyle,
    onItemSelected: (GalleryItem) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(top = GRID_TOP_PADDING, bottom = GRID_BOTTOM_PADDING),
        verticalArrangement = Arrangement.spacedBy(GRID_SPACING),
    ) {
        items(items = items, key = { it.id }) { item ->
            GalleryListItem(
                item = item,
                shapeStyle = shapeStyle,
                onClick = { onItemSelected(item) },
            )
        }
    }
}

@Composable
private fun GalleryItemCard(
    item: GalleryItem,
    shapeStyle: ShapeStyle,
    onClick: () -> Unit,
) {
    if (shapeStyle == ShapeStyle.PILL) {
        SubcomposeAsyncImage(
            model = item.thumbnailUrl,
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            loading = { ImagePlaceholder() },
            error = { ImageErrorPlaceholder() },
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(CircleShape)
                .clickable(onClick = onClick),
        )
    } else {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
        ) {
            SubcomposeAsyncImage(
                model = item.thumbnailUrl,
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                loading = { ImagePlaceholder() },
                error = { ImageErrorPlaceholder() },
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
            )
        }
    }
}

@Composable
private fun GalleryListItem(
    item: GalleryItem,
    shapeStyle: ShapeStyle,
    onClick: () -> Unit,
) {
    val isPill = shapeStyle == ShapeStyle.PILL
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val thumbnailModifier = if (isPill) {
                Modifier.size(LIST_IMAGE_SIZE).clip(CircleShape)
            } else {
                Modifier.size(LIST_IMAGE_SIZE)
            }
            SubcomposeAsyncImage(
                model = item.thumbnailUrl,
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                loading = { ImagePlaceholder() },
                error = { ImageErrorPlaceholder() },
                modifier = thumbnailModifier,
            )
            Spacer(modifier = Modifier.width(LIST_TEXT_PADDING))
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun FullScreenViewer(
    item: GalleryItem,
    onDismiss: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable(onClick = onDismiss),
    ) {
        SubcomposeAsyncImage(
            model = item.imageUrl,
            contentDescription = item.title,
            contentScale = ContentScale.Fit,
            loading = {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            },
            error = {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.BrokenImage,
                        contentDescription = IMAGE_ERROR_DESC,
                        tint = Color.White.copy(alpha = PLACEHOLDER_ALPHA),
                        modifier = Modifier.size(VIEWER_ERROR_ICON_SIZE),
                    )
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(VIEWER_PADDING),
        )
        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(CLOSE_BUTTON_PADDING),
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = CLOSE_DESC,
                tint = Color.White,
            )
        }
        Text(
            text = item.title,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(VIEWER_PADDING),
        )
    }
}

@Composable
private fun ImagePlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.errorContainer),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Default.BrokenImage,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = PLACEHOLDER_ALPHA),
            modifier = Modifier.size(PLACEHOLDER_ICON_SIZE),
        )
    }
}

@Composable
private fun ImageErrorPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.errorContainer),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Default.BrokenImage,
            contentDescription = IMAGE_ERROR_DESC,
            tint = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = PLACEHOLDER_ALPHA),
            modifier = Modifier.size(PLACEHOLDER_ICON_SIZE),
        )
    }
}

private val CONTENT_PADDING = 16.dp
private val GRID_TOP_PADDING = 12.dp
private val GRID_BOTTOM_PADDING = 16.dp
private val GRID_SPACING = 8.dp
private val VIEWER_PADDING = 16.dp
private val CLOSE_BUTTON_PADDING = 8.dp
private val ACTION_SIZE = 36.dp
private val LIST_IMAGE_SIZE = 64.dp
private val LIST_TEXT_PADDING = 12.dp
private val PLACEHOLDER_ICON_SIZE = 32.dp
private val VIEWER_ERROR_ICON_SIZE = 48.dp
private const val GRID_COLUMNS_2 = 2
private const val GRID_COLUMNS_3 = 3
private const val PLACEHOLDER_ALPHA = 0.5f

private const val SORT_DESC = "Sort"
private const val LAYOUT_DESC = "Layout"
private const val CLOSE_DESC = "Close viewer"
private const val IMAGE_ERROR_DESC = "Failed to load image"
