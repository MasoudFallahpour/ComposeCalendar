package ir.fallahpoor.composecalendar.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun Day(
    modifier: Modifier = Modifier,
    day: Int,
    isSelected: Boolean,
    isToday: Boolean,
    isHoliday: Boolean,
    onDayClick: (Int) -> Unit
) {
    Surface(
        color = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
        shape = if (isSelected) MaterialTheme.shapes.medium else RectangleShape
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
            Text(
                text = day.toString(),
                maxLines = 1,
                textAlign = TextAlign.Center,
                color = if (isHoliday) Color.Red else Color.Unspecified
            )
        }
    }
}

@Composable
fun EmptyDay(
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = "",
        maxLines = 1,
        textAlign = TextAlign.Center
    )
}