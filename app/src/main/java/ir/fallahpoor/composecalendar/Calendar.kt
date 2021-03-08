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

private val monthNames = listOf(
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December"
)

@Composable
fun Calendar() {

    val currentCalendarStartOfMonth = Calendar.getInstance().apply {
        set(Calendar.DATE, 1)
    }

    val currentDate = Calendar.getInstance()
    var currentYear: Int by remember { mutableStateOf(currentDate.get(Calendar.YEAR)) }
    var currentMonth: Int by remember { mutableStateOf(currentDate.get(Calendar.MONTH)) }

    val todayDate = Calendar.getInstance()
    val todayYear = todayDate.get(Calendar.YEAR)
    val todayMonth = todayDate.get(Calendar.MONTH)
    val todayDayOfMonth = todayDate.get(Calendar.DAY_OF_MONTH)

    var selectedYear: Int = todayDate.get(Calendar.YEAR)
    var selectedMonth: Int = todayDate.get(Calendar.MONTH)
    var selectedDayOfMonth: Int by remember { mutableStateOf(currentDate.get(Calendar.DAY_OF_MONTH)) }

    ComposeCalendarTheme {
        Surface {
            CalendarScreen(
                currentYear = currentYear,
                currentMonth = currentMonth,
                currentMonthNumberOfDays = currentDate.getActualMaximum(Calendar.DAY_OF_MONTH),
                todayYear = todayYear,
                todayMonth = todayMonth,
                todayDayOfMonth = todayDayOfMonth,
                selectedYear = selectedYear,
                selectedMonth = selectedMonth,
                selectedDayOfMonth = selectedDayOfMonth,
                startOfMonthDayOfWeek = getDayOfWeek(currentCalendarStartOfMonth.get(Calendar.DAY_OF_WEEK)),
                onDateClick = { year: Int, month: Int, day: Int ->
                    selectedYear = year
                    selectedMonth = month
                    selectedDayOfMonth = day
                },
                onPreviousMonthClick = {
                    currentDate.add(Calendar.MONTH, -1)
                    currentCalendarStartOfMonth.add(Calendar.MONTH, -1)
                    currentMonth = currentDate.get(Calendar.MONTH)
                    currentYear = currentDate.get(Calendar.YEAR)
                },
                onNextMonthClick = {
                    currentDate.add(Calendar.MONTH, 1)
                    currentCalendarStartOfMonth.add(Calendar.MONTH, 1)
                    currentMonth = currentDate.get(Calendar.MONTH)
                    currentYear = currentDate.get(Calendar.YEAR)
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
    currentYear: Int,
    currentMonth: Int,
    currentMonthNumberOfDays: Int,
    todayYear: Int,
    todayMonth: Int,
    todayDayOfMonth: Int,
    selectedYear: Int,
    selectedMonth: Int,
    selectedDayOfMonth: Int,
    startOfMonthDayOfWeek: DayOfWeek,
    onDateClick: (Int, Int, Int) -> Unit,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Header(
            currentYear = currentYear,
            currentMonth = currentMonth,
            onPreviousMonthClick = onPreviousMonthClick,
            onNextMonthClick = onNextMonthClick
        )
        WeekDays()
        Divider()
        MonthDays(
            currentYear = currentYear,
            currentMonth = currentMonth,
            currentMonthNumberOfDays = currentMonthNumberOfDays,
            todayYear = todayYear,
            todayMonth = todayMonth,
            todayDayOfMonth = todayDayOfMonth,
            selectedYear = selectedYear,
            selectedMonth = selectedMonth,
            selectedDayOfMonth = selectedDayOfMonth,
            startOfMonthDayOfWeek = startOfMonthDayOfWeek,
            onDayClick = {
                onDateClick(currentYear, currentMonth, it)
            }
        )
    }
}

@Composable
private fun Header(
    currentYear: Int,
    currentMonth: Int,
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
            text = "${monthNames[currentMonth]} $currentYear",
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
    currentYear: Int,
    currentMonth: Int,
    currentMonthNumberOfDays: Int,
    todayYear: Int,
    todayMonth: Int,
    todayDayOfMonth: Int,
    selectedYear: Int,
    selectedMonth: Int,
    selectedDayOfMonth: Int,
    startOfMonthDayOfWeek: DayOfWeek,
    onDayClick: (Int) -> Unit
) {
    Column {
        val numberOfRows =
            ceil((currentMonthNumberOfDays + startOfMonthDayOfWeek.value) / NUM_WEEK_DAYS.toDouble()).toInt()
        var startDay = 1
        repeat(numberOfRows) {
            val startColumn = if (it == 0) startOfMonthDayOfWeek.value else 0
            val endColumn = if (it != numberOfRows - 1) 6 else currentMonthNumberOfDays - startDay
            MonthDaysRow(
                startColumn = startColumn,
                endColumn = endColumn,
                startDay = startDay,
                currentYear = currentYear,
                currentMonth = currentMonth,
                todayYear = todayYear,
                todayMonth = todayMonth,
                todayDayOfMonth = todayDayOfMonth,
                selectedYear = selectedYear,
                selectedMonth = selectedMonth,
                selectedDayOfMonth = selectedDayOfMonth,
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
    currentYear: Int,
    currentMonth: Int,
    todayYear: Int,
    todayMonth: Int,
    todayDayOfMonth: Int,
    selectedYear: Int,
    selectedMonth: Int,
    selectedDayOfMonth: Int,
    onDayClick: (Int) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {

        val size = maxWidth / NUM_WEEK_DAYS
        val modifier = Modifier
            .requiredWidth(size)
            .requiredHeight(size)
        var currentDay = startDay

        Row {
            repeat(NUM_WEEK_DAYS) { index: Int ->
                if (index in startColumn..endColumn) {
                    Day(
                        modifier = modifier,
                        day = currentDay,
                        isSelected = currentYear == selectedYear && currentMonth == selectedMonth && currentDay == selectedDayOfMonth,
                        isToday = currentYear == todayYear && currentMonth == todayMonth && currentDay == todayDayOfMonth,
                        isHoliday = index == NUM_WEEK_DAYS - 1,
                        onDayClick = onDayClick
                    )
                    currentDay++
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
        var localModifier = modifier
            .clickable {
                onDayClick(day)
            }
        if (isToday) {
            localModifier = localModifier.border(
                width = 1.dp,
                color = Color.Red,
                shape = MaterialTheme.shapes.medium
            )
        }
        Box(
            modifier = localModifier,
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
                currentYear = 2021,
                currentMonth = 2,
                currentMonthNumberOfDays = 30,
                todayYear = 2021,
                todayMonth = 2,
                todayDayOfMonth = 12,
                selectedYear = 2021,
                selectedMonth = 12,
                selectedDayOfMonth = 20,
                startOfMonthDayOfWeek = DayOfWeek.Sunday,
                onDateClick = { _, _, _ -> },
                onPreviousMonthClick = {},
                onNextMonthClick = {}
            )
        }
    }
}