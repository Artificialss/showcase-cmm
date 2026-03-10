package com.artificialss.showcase.ui.feature.analytics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.artificialss.showcase.domain.model.ChartDataPoint
import com.artificialss.showcase.domain.model.ChartPeriod
import com.artificialss.showcase.ui.components.ErrorMessage
import com.artificialss.showcase.ui.components.LoadingIndicator
import com.artificialss.showcase.ui.components.charts.BarChart
import com.artificialss.showcase.ui.components.charts.BarChartEntry
import com.artificialss.showcase.ui.components.charts.DonutChart
import com.artificialss.showcase.ui.components.charts.DonutChartSegment
import com.artificialss.showcase.ui.components.charts.LineChart
import com.artificialss.showcase.ui.components.charts.LineChartEntry
import com.artificialss.showcase.ui.theme.BluePrimary
import com.artificialss.showcase.ui.theme.CategoryEntertainment
import com.artificialss.showcase.ui.theme.CategoryFood
import com.artificialss.showcase.ui.theme.CategoryShopping
import com.artificialss.showcase.ui.theme.CategoryTransport
import com.artificialss.showcase.ui.theme.GreenPrimary
import com.artificialss.showcase.ui.theme.PurplePrimary

@Composable
fun AnalyticsScreen(
    presenter: AnalyticsPresenter,
    modifier: Modifier = Modifier,
) {
    val state by presenter.uiState.collectAsStateWithLifecycle()

    when (val current = state) {
        is AnalyticsUiState.Loading -> LoadingIndicator(modifier = modifier)
        is AnalyticsUiState.Error -> ErrorMessage(message = current.message, modifier = modifier)
        is AnalyticsUiState.Success -> AnalyticsContent(
            state = current,
            onPeriodSelected = { presenter.onPeriodSelected(it) },
            modifier = modifier,
        )
    }
}

@Composable
private fun AnalyticsContent(
    state: AnalyticsUiState.Success,
    onPeriodSelected: (ChartPeriod) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(CONTENT_PADDING),
        verticalArrangement = Arrangement.spacedBy(SECTION_SPACING),
    ) {
        Text(
            text = "Analytics",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )

        PeriodSelector(
            selectedPeriod = state.selectedPeriod,
            onPeriodSelected = onPeriodSelected,
        )

        LineChartCard(title = BITCOIN_PRICE_TITLE, data = state.bitcoinData)
        BarChartCard(title = "Weekly Activity", data = state.barData)
        DonutChartCard(title = "Category Breakdown", data = state.barData)
    }
}

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
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        }
    }
}

@Composable
private fun LineChartCard(
    title: String,
    data: List<ChartDataPoint>,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(CARD_PADDING)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
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
private fun BarChartCard(
    title: String,
    data: List<ChartDataPoint>,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(CARD_PADDING)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
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
private fun DonutChartCard(
    title: String,
    data: List<ChartDataPoint>,
) {
    val donutColors = listOf(
        BluePrimary, GreenPrimary, PurplePrimary,
        CategoryFood, CategoryTransport, CategoryEntertainment, CategoryShopping,
    )

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(CARD_PADDING)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
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
                    centerValue = "%,.0f".format(total),
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

private val CONTENT_PADDING = 16.dp
private val SECTION_SPACING = 16.dp
private val CHIP_SPACING = 8.dp
private val CARD_PADDING = 16.dp
private val CHART_TOP_SPACING = 12.dp
private const val BITCOIN_PRICE_TITLE = "Bitcoin Price"
