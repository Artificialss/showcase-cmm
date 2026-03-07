package com.artificialss.showcase.ui.feature.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.artificialss.showcase.domain.model.ChartDataPoint
import com.artificialss.showcase.domain.model.ChartPeriod
import com.artificialss.showcase.domain.model.Transaction
import com.artificialss.showcase.domain.model.TransactionCategory
import com.artificialss.showcase.ui.components.ErrorMessage
import com.artificialss.showcase.ui.components.LoadingIndicator
import com.artificialss.showcase.ui.components.charts.BarChart
import com.artificialss.showcase.ui.components.charts.BarChartEntry
import com.artificialss.showcase.ui.components.charts.DonutChart
import com.artificialss.showcase.ui.components.charts.DonutChartSegment
import com.artificialss.showcase.ui.components.charts.LineChart
import com.artificialss.showcase.ui.components.charts.LineChartEntry
import com.artificialss.showcase.ui.feature.analytics.AnalyticsPresenter
import com.artificialss.showcase.ui.feature.analytics.AnalyticsUiState
import com.artificialss.showcase.ui.theme.BluePrimary
import com.artificialss.showcase.ui.theme.CategoryBills
import com.artificialss.showcase.ui.theme.CategoryEntertainment
import com.artificialss.showcase.ui.theme.CategoryFood
import com.artificialss.showcase.ui.theme.CategoryHealth
import com.artificialss.showcase.ui.theme.CategoryOther
import com.artificialss.showcase.ui.theme.CategoryShopping
import com.artificialss.showcase.ui.theme.CategoryTransport
import com.artificialss.showcase.ui.theme.CategoryTravel
import com.artificialss.showcase.ui.theme.GreenPrimary
import com.artificialss.showcase.ui.theme.PurplePrimary

@Composable
fun DashboardScreen(
    dashboardPresenter: DashboardPresenter,
    analyticsPresenter: AnalyticsPresenter,
    modifier: Modifier = Modifier,
) {
    val dashState by dashboardPresenter.uiState.collectAsStateWithLifecycle()
    val analyticsState by analyticsPresenter.uiState.collectAsStateWithLifecycle()

    when (val current = dashState) {
        is DashboardUiState.Loading -> LoadingIndicator(modifier = modifier)
        is DashboardUiState.Error -> ErrorMessage(
            message = current.message,
            onRetry = { dashboardPresenter.onRefresh() },
            modifier = modifier,
        )
        is DashboardUiState.Success -> DashboardContent(
            dashState = current,
            analyticsState = analyticsState,
            onPeriodSelected = { analyticsPresenter.onPeriodSelected(it) },
            modifier = modifier,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardContent(
    dashState: DashboardUiState.Success,
    analyticsState: AnalyticsUiState,
    onPeriodSelected: (ChartPeriod) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showAllTransactions by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = CONTENT_PADDING),
            verticalArrangement = Arrangement.spacedBy(ITEM_SPACING),
        ) {
            item { Spacer(modifier = Modifier.height(CONTENT_PADDING)) }
            item { BalanceCard(dashState.userName, dashState.balance, dashState.cardNumber) }
            item { QuickActionsRow() }
            item { SpendingOverview(dashState.spendingByCategory) }
            item {
                RecentTransactionsCard(
                    transactions = dashState.transactions,
                    onSeeAllClick = { showAllTransactions = true },
                )
            }

            // Analytics section
            item { Spacer(modifier = Modifier.height(SECTION_DIVIDER)) }
            item {
                Text(
                    text = "Analytics",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
            }

            when (analyticsState) {
                is AnalyticsUiState.Loading -> item {
                    LoadingIndicator(modifier = Modifier.fillMaxWidth().height(CHART_LOADING_HEIGHT))
                }
                is AnalyticsUiState.Error -> item {
                    Text(
                        text = analyticsState.message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
                is AnalyticsUiState.Success -> {
                    item {
                        PeriodSelector(
                            selectedPeriod = analyticsState.selectedPeriod,
                            onPeriodSelected = onPeriodSelected,
                        )
                    }
                    item { LineChartCard(title = "Revenue Trend", data = analyticsState.lineData) }
                    item { BarChartCard(title = "Weekly Activity", data = analyticsState.barData) }
                    item { AnalyticsDonutCard(title = "Category Breakdown", data = analyticsState.barData) }
                }
            }

            item { Spacer(modifier = Modifier.height(CONTENT_PADDING)) }
        }

        if (showAllTransactions) {
            val sheetState = rememberModalBottomSheetState()
            ModalBottomSheet(
                onDismissRequest = { showAllTransactions = false },
                sheetState = sheetState,
            ) {
                AllTransactionsSheet(transactions = dashState.transactions)
            }
        }
    }
}

// region — Balance & Quick Actions

@Composable
private fun BalanceCard(
    userName: String,
    balance: Double,
    cardNumber: String,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
    ) {
        Column(modifier = Modifier.padding(CARD_PADDING)) {
            Text(
                text = "Welcome, $userName",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = LABEL_ALPHA),
            )
            Spacer(modifier = Modifier.height(SPACING_SM))
            Text(
                text = "$${"%,.2f".format(balance)}",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
            )
            Spacer(modifier = Modifier.height(SPACING_MD))
            Text(
                text = cardNumber,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = LABEL_ALPHA),
            )
        }
    }
}

@Composable
private fun QuickActionsRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        QuickActionButton(icon = Icons.AutoMirrored.Filled.Send, label = "Send")
        QuickActionButton(icon = Icons.Default.Star, label = "Receive")
        QuickActionButton(icon = Icons.Default.ShoppingCart, label = "Pay")
        QuickActionButton(icon = Icons.Default.DateRange, label = "History")
    }
}

@Composable
private fun QuickActionButton(icon: ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            onClick = { },
            modifier = Modifier
                .size(ACTION_BUTTON_SIZE)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
        Spacer(modifier = Modifier.height(SPACING_XS))
        Text(text = label, style = MaterialTheme.typography.labelSmall)
    }
}

// endregion

// region — Spending & Transactions

@Composable
private fun SpendingOverview(spendingByCategory: Map<TransactionCategory, Double>) {
    val total = spendingByCategory.values.sum()
    val segments = spendingByCategory.entries
        .sortedByDescending { it.value }
        .map { (category, amount) ->
            DonutChartSegment(
                label = category.label,
                value = amount.toFloat(),
                color = colorForCategory(category),
            )
        }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(CARD_PADDING)) {
            Text(
                text = "Spending by Category",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(SPACING_MD))
            DonutChart(
                segments = segments,
                centerValue = "$${"%,.0f".format(total)}",
                centerLabel = "Total",
            )
        }
    }
}

@Composable
private fun RecentTransactionsCard(
    transactions: List<Transaction>,
    onSeeAllClick: () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(CARD_INNER_PADDING)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Recent Transactions",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = "See all",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable(onClick = onSeeAllClick),
                )
            }
            Spacer(modifier = Modifier.height(SPACING_SM))
            transactions.take(PREVIEW_TRANSACTION_COUNT).forEachIndexed { index, transaction ->
                if (index > 0) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = TRANSACTION_DIVIDER_PADDING),
                        color = MaterialTheme.colorScheme.outlineVariant,
                    )
                }
                CompactTransactionRow(transaction = transaction)
            }
        }
    }
}

@Composable
private fun AllTransactionsSheet(transactions: List<Transaction>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = SHEET_MAX_HEIGHT)
            .padding(horizontal = CONTENT_PADDING),
    ) {
        Text(
            text = "All Transactions",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = SPACING_MD),
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            items(items = transactions, key = { it.id }) { transaction ->
                CompactTransactionRow(transaction = transaction)
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = TRANSACTION_DIVIDER_PADDING),
                    color = MaterialTheme.colorScheme.outlineVariant,
                )
            }
            item { Spacer(modifier = Modifier.height(CONTENT_PADDING)) }
        }
    }
}

@Composable
private fun CompactTransactionRow(transaction: Transaction) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f),
        ) {
            Box(
                modifier = Modifier
                    .size(COMPACT_ICON_SIZE)
                    .clip(CircleShape)
                    .background(colorForCategory(transaction.category).copy(alpha = CATEGORY_BG_ALPHA)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = transaction.merchant.first().toString(),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = colorForCategory(transaction.category),
                )
            }
            Spacer(modifier = Modifier.width(SPACING_SM))
            Column {
                Text(
                    text = transaction.merchant,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = transaction.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Text(
            text = "-$${"%,.2f".format(transaction.amount)}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.error,
        )
    }
}

// endregion

// region — Analytics Charts

@Composable
private fun PeriodSelector(
    selectedPeriod: ChartPeriod,
    onPeriodSelected: (ChartPeriod) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(CHIP_SPACING),
    ) {
        ChartPeriod.entries.forEach { period ->
            FilterChip(
                selected = period == selectedPeriod,
                onClick = { onPeriodSelected(period) },
                label = { Text(period.label) },
            )
        }
    }
}

@Composable
private fun LineChartCard(title: String, data: List<ChartDataPoint>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(CARD_PADDING)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(CHART_TOP_SPACING))
            if (data.isEmpty()) {
                EmptyChartMessage()
            } else {
                LineChart(
                    entries = data.map { LineChartEntry(label = it.label, value = it.value.toFloat()) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun BarChartCard(title: String, data: List<ChartDataPoint>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(CARD_PADDING)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(CHART_TOP_SPACING))
            if (data.isEmpty()) {
                EmptyChartMessage()
            } else {
                BarChart(
                    entries = data.map { BarChartEntry(label = it.label, value = it.value.toFloat()) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun AnalyticsDonutCard(title: String, data: List<ChartDataPoint>) {
    val donutColors = listOf(
        BluePrimary, GreenPrimary, PurplePrimary,
        CategoryFood, CategoryTransport, CategoryEntertainment, CategoryShopping,
    )

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(CARD_PADDING)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(CHART_TOP_SPACING))
            if (data.isEmpty()) {
                EmptyChartMessage()
            } else {
                val total = data.sumOf { it.value }
                DonutChart(
                    segments = data.mapIndexed { index, point ->
                        DonutChartSegment(
                            label = point.label,
                            value = point.value.toFloat(),
                            color = donutColors[index % donutColors.size],
                        )
                    },
                    centerValue = kotlin.math.round(total).toLong().toString(),
                    centerLabel = "Total",
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun EmptyChartMessage() {
    Text(
        text = "No data available",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

// endregion

@Composable
private fun colorForCategory(category: TransactionCategory) = when (category) {
    TransactionCategory.FOOD -> CategoryFood
    TransactionCategory.TRANSPORT -> CategoryTransport
    TransactionCategory.ENTERTAINMENT -> CategoryEntertainment
    TransactionCategory.SHOPPING -> CategoryShopping
    TransactionCategory.BILLS -> CategoryBills
    TransactionCategory.HEALTH -> CategoryHealth
    TransactionCategory.TRAVEL -> CategoryTravel
    TransactionCategory.OTHER -> CategoryOther
}

private val CONTENT_PADDING = 16.dp
private val ITEM_SPACING = 12.dp
private val CARD_PADDING = 20.dp
private val CARD_INNER_PADDING = 12.dp
private val SPACING_XS = 4.dp
private val SPACING_SM = 8.dp
private val SPACING_MD = 12.dp
private val SECTION_DIVIDER = 8.dp
private val ACTION_BUTTON_SIZE = 48.dp
private val COMPACT_ICON_SIZE = 36.dp
private val TRANSACTION_DIVIDER_PADDING = 6.dp
private val CHIP_SPACING = 8.dp
private const val PREVIEW_TRANSACTION_COUNT = 2
private val SHEET_MAX_HEIGHT = 400.dp
private val CHART_TOP_SPACING = 12.dp
private val CHART_LOADING_HEIGHT = 120.dp
private const val LABEL_ALPHA = 0.8f
private const val CATEGORY_BG_ALPHA = 0.15f
