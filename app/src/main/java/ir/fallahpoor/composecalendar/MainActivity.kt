package ir.fallahpoor.composecalendar

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val calendar = CalendarFactory.newInstance(CalendarType.PERSIAN)

        setContent {
            CalendarScreen(
                currentYear = calendar.year,
                currentMonth = calendar.monthName
            )
        }

    }

}