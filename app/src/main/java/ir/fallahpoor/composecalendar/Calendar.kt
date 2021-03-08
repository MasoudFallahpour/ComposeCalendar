package ir.fallahpoor.composecalendar

import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.fallahpoor.composecalendar.ui.theme.ComposeCalendarTheme
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

    val selectedCalendarStartOfMonth = Calendar.getInstance().apply {
        set(Calendar.DATE, 1)
    }

    val selectedCalendar = Calendar.getInstance()
    val selectedYear: Int = selectedCalendar.get(Calendar.YEAR)
    var selectedMonth: Int by remember { mutableStateOf(selectedCalendar.get(Calendar.MONTH)) }
    var selectedDayOfMonth: Int by remember { mutableStateOf(selectedCalendar.get(Calendar.DAY_OF_MONTH)) }

    val todayCalendar = Calendar.getInstance()
    val todayYear = todayCalendar.get(Calendar.YEAR)
    val todayMonth = todayCalendar.get(Calendar.MONTH)
    val todayDayOfMonth = todayCalendar.get(Calendar.DAY_OF_MONTH)

    ComposeCalendarTheme {
        Surface {
            CalendarScreen(
                selectedYear = selectedYear,
                selectedMonth = selectedMonth,
                selectedDayOfMonth = selectedDayOfMonth,
                todayYear = todayYear,
                todayMonth = todayMonth,
                todayDayOfMonth = todayDayOfMonth,
                selectedMonthNumberOfDays = selectedCalendar.getActualMaximum(Calendar.DAY_OF_MONTH),
                startOfMonthDayOfWeek = getDayOfWeek(selectedCalendarStartOfMonth.get(Calendar.DAY_OF_WEEK)),
                onDayClick = { dayOfMonth: Int ->
                    if (selectedDayOfMonth != dayOfMonth) {
                        selectedDayOfMonth = dayOfMonth
                    }
                },
                onPreviousMonthClick = {
                    selectedCalendar.add(Calendar.MONTH, -1)
                    selectedCalendarStartOfMonth.add(Calendar.MONTH, -1)
                    selectedMonth = selectedCalendar.get(Calendar.MONTH)
                },
                onNextMonthClick = {
                    selectedCalendar.add(Calendar.MONTH, 1)
                    selectedCalendarStartOfMonth.add(Calendar.MONTH, 1)
                    selectedMonth = selectedCalendar.get(Calendar.MONTH)
                }
            )
        }
    }

}

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
    selectedYear: Int,
    selectedMonth: Int,
    selectedDayOfMonth: Int,
    todayYear: Int,
    todayMonth: Int,
    todayDayOfMonth: Int,
    selectedMonthNumberOfDays: Int,
    startOfMonthDayOfWeek: DayOfWeek,
    onDayClick: (Int) -> Unit,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Header(
            selectedYear = selectedYear,
            selectedMonth = selectedMonth,
            onPreviousMonthClick = onPreviousMonthClick,
            onNextMonthClick = onNextMonthClick
        )
        WeekDays()
        Divider()
        MonthDays(
            selectedYear = selectedYear,
            selectedMonth = selectedMonth,
            selectedDayOfMonth = selectedDayOfMonth,
            todayYear = todayYear,
            todayMonth = todayMonth,
            todayDayOfMonth = todayDayOfMonth,
            selectedMonthNumberOfDays = selectedMonthNumberOfDays,
            startOfMonthDayOfWeek = startOfMonthDayOfWeek,
            onDayClick = onDayClick
        )
    }
}

@Composable
private fun Header(
    selectedYear: Int,
    selectedMonth: Int,
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
            text = "${getMonthName(selectedMonth)} $selectedYear",
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

private fun getMonthName(month: Int): String =
    when (month) {
        0 -> "January"
        1 -> "February"
        2 -> "March"
        3 -> "April"
        4 -> "May"
        5 -> "June"
        6 -> "July"
        7 -> "August"
        8 -> "September"
        9 -> "October"
        10 -> "November"
        11 -> "December"
        else -> throw IllegalArgumentException("month must be in range [0..11]")
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
    selectedYear: Int,
    selectedMonth: Int,
    selectedDayOfMonth: Int,
    todayYear: Int,
    todayMonth: Int,
    todayDayOfMonth: Int,
    selectedMonthNumberOfDays: Int,
    startOfMonthDayOfWeek: DayOfWeek,
    onDayClick: (Int) -> Unit
) {
    Column {
        val numberOfRows =
            ceil((selectedMonthNumberOfDays + startOfMonthDayOfWeek.value) / NUM_WEEK_DAYS.toDouble()).toInt()
        var startDay = 1
        repeat(numberOfRows) {
            val startColumn = if (it == 0) startOfMonthDayOfWeek.value else 0
            val endColumn = if (it != numberOfRows - 1) 6 else selectedMonthNumberOfDays - startDay
            MonthDaysRow(
                startColumn = startColumn,
                endColumn = endColumn,
                startDay = startDay,
                selectedYear = selectedYear,
                selectedMonth = selectedMonth,
                selectedDayOfMonth = selectedDayOfMonth,
                todayYear = todayYear,
                todayMonth = todayMonth,
                todayDayOfMonth = todayDayOfMonth,
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
    selectedYear: Int,
    selectedMonth: Int,
    selectedDayOfMonth: Int,
    todayYear: Int,
    todayMonth: Int,
    todayDayOfMonth: Int,
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
                    Day(
                        modifier = modifier,
                        day = startDayLocal,
                        isSelected = startDayLocal == selectedDayOfMonth,
                        isToday = selectedYear == todayYear && selectedMonth == todayMonth && startDayLocal == todayDayOfMonth,
                        isHoliday = index == NUM_WEEK_DAYS - 1,
                        onDayClick = onDayClick
                    )
                    startDayLocal++
                } else {
                    EmptyDay(
                        modifier = modifier
                    )
                }
            }
        }

    }
}

@Composable
private fun Day(
    modifier: Modifier = Modifier,
    day: Int,
    isSelected: Boolean,
    isToday: Boolean,
    isHoliday: Boolean,
    onDayClick: (Int) -> Unit
) {
    val color: Color
    val shape: Shape
    if (isSelected) {
        color = MaterialTheme.colors.primary
        shape = MaterialTheme.shapes.medium
    } else {
        color = Color.Unspecified
        shape = RectangleShape
    }
    Surface(
        color = color,
        shape = shape
    ) {
        var m = modifier
            .clickable {
                onDayClick(day)
            }
        if (isToday) {
            m = modifier
                .border(
                    width = 1.dp,
                    color = Color.Red,
                    shape = MaterialTheme.shapes.medium
                )
        }
        Box(
            modifier = m,
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
private fun EmptyDay(
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
                selectedYear = 2021,
                selectedMonth = 2,
                selectedDayOfMonth = 12,
                todayYear = 2021,
                todayMonth = 2,
                todayDayOfMonth = 12,
                selectedMonthNumberOfDays = 30,
                startOfMonthDayOfWeek = DayOfWeek.Sunday,
                onDayClick = {},
                onPreviousMonthClick = {},
                onNextMonthClick = {}
            )
        }
    }
}