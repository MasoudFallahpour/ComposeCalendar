package ir.fallahpoor.composecalendar

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.fallahpoor.composecalendar.ui.theme.ComposeCalendarTheme

private const val NUM_WEEK_DAYS = 7

@Composable
fun CalendarScreen(
    currentYear: Int,
    currentMonth: String
) {
    ComposeCalendarTheme {
        Surface {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                CalendarHeader(
                    currentYear = currentYear,
                    currentMonth = currentMonth
                )
                CalendarWeekDays()
                CalendarMonthDays()
            }
        }
    }
}

@Composable
private fun CalendarHeader(currentYear: Int, currentMonth: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        text = "$currentYear $currentMonth",
        textAlign = TextAlign.Center
    )
}

@Composable
private fun CalendarWeekDays() {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {
        val size = maxWidth / NUM_WEEK_DAYS
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            val weekdays = listOf("ش", "ی", "د", "س", "چ", "پ", "ج")
            weekdays.forEach {
                WeekDay(
                    modifier = Modifier
                        .requiredWidth(size)
                        .requiredHeight(size),
                    text = it
                )
            }
        }
    }
}

@Composable
private fun WeekDay(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        maxLines = 1,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun CalendarMonthDays() {
    Column {
        repeat(6) {
            MonthDaysRow()
        }
    }
}

@Composable
private fun MonthDaysRow() {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {
        val size = maxWidth / NUM_WEEK_DAYS
        Row {
            repeat(NUM_WEEK_DAYS) {
                MonthDay(
                    modifier = Modifier
                        .requiredWidth(size)
                        .requiredHeight(size),
                    text = it.toString()
                )
            }
        }
    }
}

@Composable
private fun MonthDay(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        maxLines = 1,
        textAlign = TextAlign.Center
    )
}

@Preview
@Composable
private fun CalendarScreenPreview() {
    CalendarScreen(
        currentYear = 1399,
        currentMonth = "Bahman"
    )
}