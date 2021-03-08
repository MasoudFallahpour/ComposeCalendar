package ir.fallahpoor.composecalendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.fallahpoor.composecalendar.ui.theme.ComposeCalendarTheme
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

// FIXME: In landscape mode, calendar is not scrollable

private const val NUM_WEEK_DAYS = 7

private enum class DayOfWeek(val value: Int) {
    Monday(0),
    Tuesday(1),
    Wednesday(2),
    Thursday(3),
    Friday(4),
    Saturday(5),
    Sunday(6),
}

@Composable
fun Calendar() {

    val calendar = Calendar.getInstance()
    val calendarStartOfMonth = Calendar.getInstance().apply {
        set(Calendar.DATE, 1)
    }
    var currentDayOfMonth: Int by remember { mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }
    var currentMonthName: String by remember { mutableStateOf(getMonthName(calendar)) }

    ComposeCalendarTheme {
        Surface {
            CalendarScreen(
                year = calendar.get(Calendar.YEAR),
                monthName = currentMonthName,
                dayOfMonth = currentDayOfMonth,
                numberOfDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH),
                startOfMonthDayOfWeek = getDayOfWeek(calendarStartOfMonth.get(Calendar.DAY_OF_WEEK)),
                onDayClick = { selectedDayOfMonth: Int ->
                    if (selectedDayOfMonth != currentDayOfMonth) {
                        currentDayOfMonth = selectedDayOfMonth
                    }
                },
                onPreviousMonthClick = {
                    calendar.add(Calendar.MONTH, -1)
                    calendarStartOfMonth.add(Calendar.MONTH, -1)
                    currentMonthName = getMonthName(calendar)
                },
                onNextMonthClick = {
                    calendar.add(Calendar.MONTH, 1)
                    calendarStartOfMonth.add(Calendar.MONTH, 1)
                    currentMonthName = getMonthName(calendar)
                }
            )
        }
    }

}

private fun getMonthName(calendar: Calendar): String =
    SimpleDateFormat("MMMM").format(calendar.time)

private fun getDayOfWeek(dayOfWeek: Int): DayOfWeek {
    return when (dayOfWeek) {
        1 -> DayOfWeek.Sunday
        2 -> DayOfWeek.Monday
        3 -> DayOfWeek.Tuesday
        4 -> DayOfWeek.Wednesday
        5 -> DayOfWeek.Thursday
        6 -> DayOfWeek.Friday
        7 -> DayOfWeek.Saturday
        else -> throw IllegalArgumentException("dayOfWeek must be in range [1..7]")
    }
}

@Composable
private fun CalendarScreen(
    year: Int,
    monthName: String,
    dayOfMonth: Int,
    numberOfDaysInMonth: Int,
    startOfMonthDayOfWeek: DayOfWeek,
    onDayClick: (Int) -> Unit,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Header(
            year = year,
            month = monthName,
            onPreviousMonthClick = onPreviousMonthClick,
            onNextMonthClick = onNextMonthClick
        )
        WeekDays()
        Divider()
        MonthDays(
            dayOfMonth = dayOfMonth,
            numberOfDaysInMonth = numberOfDaysInMonth,
            startOfMonthDayOfWeek = startOfMonthDayOfWeek,
            onDayClick = onDayClick
        )
    }
}

@Composable
private fun Header(
    year: Int,
    month: String,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onPreviousMonthClick
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Previous Month"
            )
        }
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            text = "$month $year",
            textAlign = TextAlign.Center
        )
        IconButton(
            onClick = onNextMonthClick
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Next Month"
            )
        }
    }
}

@Composable
private fun WeekDays() {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {
        val size = maxWidth / NUM_WEEK_DAYS
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            val weekdays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
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
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun MonthDays(
    dayOfMonth: Int,
    numberOfDaysInMonth: Int,
    startOfMonthDayOfWeek: DayOfWeek,
    onDayClick: (Int) -> Unit
) {
    Column {
        val numberOfRows =
            ceil((numberOfDaysInMonth + startOfMonthDayOfWeek.value) / NUM_WEEK_DAYS.toDouble()).toInt()
        var startDay = 1
        repeat(numberOfRows) {
            val startColumn = if (it == 0) startOfMonthDayOfWeek.value else 0
            val endColumn = if (it != numberOfRows - 1) 6 else numberOfDaysInMonth - startDay
            MonthDaysRow(
                startColumn = startColumn,
                endColumn = endColumn,
                startDay = startDay,
                currentDayOfMonth = dayOfMonth,
                onDayClick = onDayClick
            )
            startDay += endColumn - startColumn + 1
        }
    }
}

@Composable
private fun MonthDaysRow(
    startColumn: Int, // 0..6
    endColumn: Int,   // 0..6 && startColumn <= endColumn
    startDay: Int,    // 1..31
    currentDayOfMonth: Int,
    onDayClick: (Int) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {

        val size = maxWidth / NUM_WEEK_DAYS
        val modifier = Modifier
            .requiredWidth(size)
            .requiredHeight(size)
        var startDayLocal = startDay

        Row {
            repeat(NUM_WEEK_DAYS) { index: Int ->
                if (index in startColumn..endColumn) {
                    MonthDay(
                        modifier = modifier,
                        day = startDayLocal,
                        isSelected = startDayLocal == currentDayOfMonth,
                        isHoliday = index == NUM_WEEK_DAYS - 1,
                        onDayClick = onDayClick
                    )
                    startDayLocal++
                } else {
                    EmptyMonthDay(
                        modifier = modifier
                    )
                }
            }
        }

    }
}

@Composable
private fun MonthDay(
    modifier: Modifier = Modifier,
    day: Int,
    isSelected: Boolean,
    isHoliday: Boolean,
    onDayClick: (Int) -> Unit
) {
    val color = if (isSelected) MaterialTheme.colors.primary else Color.Unspecified
    Surface(
        color = color
    ) {
        Box(
            modifier = modifier.clickable {
                onDayClick(day)
            },
            contentAlignment = Alignment.Center
        ) {
            val textColor = if (isHoliday) Color.Red else Color.Unspecified
            Text(
                text = day.toString(),
                maxLines = 1,
                textAlign = TextAlign.Center,
                color = textColor
            )
        }
    }
}

@Composable
private fun EmptyMonthDay(
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = "",
        maxLines = 1,
        textAlign = TextAlign.Center
    )
}

@Preview
@Composable
private fun CalendarScreenPreview() {
    ComposeCalendarTheme {
        Surface {
            CalendarScreen(
                year = 2021,
                monthName = "June",
                numberOfDaysInMonth = 30,
                startOfMonthDayOfWeek = DayOfWeek.Sunday,
                dayOfMonth = 20,
                onDayClick = {},
                onPreviousMonthClick = {},
                onNextMonthClick = {}
            )
        }
    }
}