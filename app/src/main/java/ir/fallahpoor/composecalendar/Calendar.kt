package ir.fallahpoor.composecalendar

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import ir.fallahpoor.composecalendar.ui.theme.ComposeCalendarTheme
import kotlin.math.ceil

private const val NUM_WEEK_DAYS = 7

private enum class DayOfWeek(val value: Int) {
    Shanbe(0),
    YekShanbe(1),
    DoShanbe(2),
    SeShanbe(3),
    ChaharShanbe(4),
    PanjShanbe(5),
    Jome(6)
}

@Composable
fun Calendar() {

    val calendar = CalendarFactory.newInstance(CalendarType.PERSIAN)
    val startOfMonthCalendar = CalendarFactory.newInstance(CalendarType.PERSIAN)
    startOfMonthCalendar.dayOfMonth = 1

    ComposeCalendarTheme {
        Surface {
            CalendarScreen(
                currentYear = calendar.year,
                currentMonthName = calendar.monthName,
                currentDayOfMonth = calendar.dayOfMonth,
                numberOfDaysInMonth = calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH),
                startOfMonthDayOfWeek = getDayOfWeek(startOfMonthCalendar.dayOfWeek)
            )
        }
    }

}

private fun getDayOfWeek(dayOfWeek: Int): DayOfWeek {
    return when (dayOfWeek) {
        0 -> DayOfWeek.Shanbe
        1 -> DayOfWeek.YekShanbe
        2 -> DayOfWeek.DoShanbe
        3 -> DayOfWeek.SeShanbe
        4 -> DayOfWeek.ChaharShanbe
        5 -> DayOfWeek.PanjShanbe
        6 -> DayOfWeek.Jome
        else -> throw IllegalArgumentException("dayOfWeek must be in range [0..6]")
    }
}

@Composable
private fun CalendarScreen(
    currentYear: Int,
    currentMonthName: String,
    currentDayOfMonth: Int,
    numberOfDaysInMonth: Int,
    startOfMonthDayOfWeek: DayOfWeek
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Header(
            currentYear = currentYear,
            currentMonth = currentMonthName
        )
        WeekDays()
        Divider()
        MonthDays(
            currentDayOfMonth = currentDayOfMonth,
            numberOfDaysInMonth = numberOfDaysInMonth,
            startOfMonthDayOfWeek = startOfMonthDayOfWeek,
            onDayClick = {
                Log.d("@@@@@@", "Day $it clicked")
            }
        )
    }
}

@Composable
private fun Header(currentYear: Int, currentMonth: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        text = "$currentYear $currentMonth",
        textAlign = TextAlign.Center
    )
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
    currentDayOfMonth: Int,
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
                currentDayOfMonth = currentDayOfMonth,
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
                currentYear = 1399,
                currentMonthName = "اسفند",
                numberOfDaysInMonth = 30,
                startOfMonthDayOfWeek = DayOfWeek.SeShanbe,
                currentDayOfMonth = 20
            )
        }
    }
}