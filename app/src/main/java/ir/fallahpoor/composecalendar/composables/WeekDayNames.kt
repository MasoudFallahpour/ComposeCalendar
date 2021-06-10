package ir.fallahpoor.composecalendar.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private const val NUM_WEEK_DAYS = 7
private val weekdays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

@Composable
fun WeekDayNames() {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val width = maxWidth / NUM_WEEK_DAYS
        Row(modifier = Modifier.fillMaxWidth()) {
            weekdays.forEach { weekDay: String ->
                WeekDay(
                    modifier = Modifier
                        .requiredWidth(width)
                        .requiredHeight(50.dp),
                    text = weekDay
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
            textAlign = TextAlign.Center,
            style = TextStyle(fontWeight = FontWeight.W600, fontSize = 16.sp)
        )
    }
}