package ir.fallahpoor.composecalendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.fallahpoor.composecalendar.composables.DaysSheet
import ir.fallahpoor.composecalendar.composables.WeekDayNames
import ir.fallahpoor.composecalendar.theme.ComposeCalendarTheme
import java.util.*

enum class DayOfWeek(val value: Int) {
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

    val currentCalendarStartOfMonth = rememberSaveable {
        Calendar.getInstance().apply {
            set(Calendar.DATE, 1)
        }
    }

    val currentDate = rememberSaveable { Calendar.getInstance() }
    var currentYear by rememberSaveable { mutableStateOf(currentDate.get(Calendar.YEAR)) }
    var currentMonth by rememberSaveable { mutableStateOf(currentDate.get(Calendar.MONTH)) }
    val currentMonthNumberOfDays =
        rememberSaveable { currentDate.getActualMaximum(Calendar.DAY_OF_MONTH) }

    val todayDate = Calendar.getInstance()
    val todayYear = todayDate.get(Calendar.YEAR)
    val todayMonth = todayDate.get(Calendar.MONTH)
    val todayDayOfMonth = todayDate.get(Calendar.DAY_OF_MONTH)

    var selectedYear by rememberSaveable { mutableStateOf(todayDate.get(Calendar.YEAR)) }
    var selectedMonth by rememberSaveable { mutableStateOf(todayDate.get(Calendar.MONTH)) }
    var selectedDayOfMonth by rememberSaveable { mutableStateOf(currentDate.get(Calendar.DAY_OF_MONTH)) }

    ComposeCalendarTheme {
        Surface {
            CalendarScreen(
                currentYear = currentYear,
                currentMonth = currentMonth,
                currentMonthNumberOfDays = currentMonthNumberOfDays,
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

private fun getDayOfWeek(dayOfWeek: Int): DayOfWeek = when (dayOfWeek) {
    1 -> DayOfWeek.Sunday
    2 -> DayOfWeek.Monday
    3 -> DayOfWeek.Tuesday
    4 -> DayOfWeek.Wednesday
    5 -> DayOfWeek.Thursday
    6 -> DayOfWeek.Friday
    7 -> DayOfWeek.Saturday
    else -> throw IllegalArgumentException("dayOfWeek must be in range [1..7]")
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
        WeekDayNames()
        Divider()
        DaysSheet(
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
                selectedMonth = 2,
                selectedDayOfMonth = 20,
                startOfMonthDayOfWeek = DayOfWeek.Sunday,
                onDateClick = { _, _, _ -> },
                onPreviousMonthClick = {},
                onNextMonthClick = {}
            )
        }
    }
}