package com.namratha.focusplanbuilder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.namratha.focusplanbuilder.ui.theme.FocusPlanBuilderTheme



data class FocusPlan(
    val subject: String,
    val minutes: Int,
    val category: String,
    val breakMinutes: Int
)

fun durationCategory(minutes: Int): String {
    return when {
        minutes < 10 -> "Invalid"
        minutes <= 29 -> "Quick review"
        minutes <= 60 -> "Focused session"
        else -> "Extended session"
    }
}

fun recommendedBreak(minutes: Int): Int {
    return when {
        minutes < 30 -> 5
        minutes <= 60 -> 10
        else -> 15
    }
}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            FocusPlanBuilderTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    FocusPlanRoute(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun FocusPlanRoute(
    modifier: Modifier = Modifier
) {
    var subject by rememberSaveable {
        mutableStateOf("")
    }

    var minutesText by rememberSaveable {
        mutableStateOf("")
    }

    val minutes: Int? = minutesText.toIntOrNull()

    val canCreatePlan =
        subject.isNotBlank() &&
                minutes != null &&
                minutes in 10..180

    FocusPlanScreen(
        subject = subject,
        minutesText = minutesText,
        onSubjectChange = { newSubject ->
            subject = newSubject
        },
        onMinutesChange = { newMinutes ->
            minutesText = newMinutes
        },
        canCreatePlan = canCreatePlan,
        modifier = modifier
    )
}

@Composable
fun FocusPlanScreen(
    subject: String,
    minutesText: String,
    onSubjectChange: (String) -> Unit,
    onMinutesChange: (String) -> Unit,
    canCreatePlan: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text(
            text = "Focus Plan Builder",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Enter a study subject and the number of minutes available."
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        OutlinedTextField(
            value = subject,
            onValueChange = onSubjectChange,
            label = {
                Text("Study subject")
            },
            placeholder = {
                Text("e.g., Kotlin")
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(
            value = minutesText,
            onValueChange = onMinutesChange,
            label = {
                Text("Available minutes")
            },
            placeholder = {
                Text("10–180")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { },
                enabled = canCreatePlan,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create plan")
            }
        }
    }
}
