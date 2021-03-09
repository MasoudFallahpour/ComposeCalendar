package ir.fallahpoor.composecalendar.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ir.fallahpoor.composecalendar.DayOfWeek
import kotlin.math.ceil

private const val NUM_WEEK_DAYS = 7

@Composable
fun DaysSheet(
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        val numberOfRows =
            ceil((currentMonthNumberOfDays + startOfMonthDayOfWeek.value) / NUM_WEEK_DAYS.toDouble()).toInt()
        var startDay = 1
        repeat(numberOfRows) {
            val startColumn = if (it == 0) startOfMonthDayOfWeek.value else 0
            val endColumn = if (it != numberOfRows - 1) 6 else currentMonthNumberOfDays - startDay
            DaysRow(
                startColumn = startColumn,
                endColumn = endColumn,
                fromDay = startDay,
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
private fun DaysRow(
    startColumn: Int, // 0..6
    endColumn: Int,   // 0..6 && startColumn <= endColumn
    fromDay: Int,    // 1..31
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
        var currentDay = fromDay

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