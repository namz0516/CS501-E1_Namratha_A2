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
import androidx.compose.material3.Card
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.res.stringResource


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

    var plan by remember{
        mutableStateOf<FocusPlan?>(null)
    }

    FocusPlanScreen(
        subject = subject,
        minutesText = minutesText,
        plan = plan,

        onSubjectChange = { newSubject ->
            subject = newSubject
            plan = null
        },

        onMinutesChange = { newMinutes ->
            minutesText = newMinutes
            plan = null
        },

        canCreatePlan = canCreatePlan,

        onCreatePlan = {
            if (minutes != null) {
                plan = FocusPlan(
                    subject = subject.trim(),
                    minutes = minutes,
                    category = durationCategory(minutes),
                    breakMinutes = recommendedBreak(minutes)
                )
            }
        },

        modifier = modifier
    )
}

@Composable
fun FocusPlanScreen(
    subject: String,
    minutesText: String,
    plan: FocusPlan?,
    onSubjectChange: (String) -> Unit,
    onMinutesChange: (String) -> Unit,
    canCreatePlan: Boolean,
    onCreatePlan: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {

        Text(
            text = stringResource(R.string.focus_plan_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.tertiary
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = stringResource(R.string.focus_plan_instructions)
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        OutlinedTextField(
            value = subject,
            onValueChange = onSubjectChange,
            label = {
                Text(stringResource(R.string.subject_label))
            },
            placeholder = {
                Text(stringResource(R.string.subject_placeholder))
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
                Text(stringResource(R.string.minutes_label))
            },
            placeholder = {
                Text(stringResource(R.string.minutes_placeholder))
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
                onClick = onCreatePlan,
                enabled = canCreatePlan,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.create_plan))
            }
        }

        if (plan != null) {

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Text(
                        text = stringResource(
                            R.string.study_result,
                            plan.subject
                        ),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = stringResource(
                            R.string.duration_result,
                            plan.minutes
                        )
                    )

                    Text(
                        text = stringResource(
                            R.string.category_result,
                            plan.category
                        )
                    )

                    Text(
                        text = stringResource(
                            R.string.break_result,
                            plan.breakMinutes
                        )
                    )

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    Text(
                        text = stringResource(
                            R.string.plan_summary,
                            plan.subject,
                            plan.minutes,
                            plan.breakMinutes
                        ),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LightFocusPlanScreenPreview() {
    FocusPlanBuilderTheme(darkTheme = false) {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            FocusPlanScreen(
                subject = "Kotlin",
                minutesText = "30",
                plan = FocusPlan(
                    subject = "Kotlin",
                    minutes = 30,
                    category = "Focused session",
                    breakMinutes = 10
                ),
                onSubjectChange = {},
                onMinutesChange = {},
                canCreatePlan = true,
                onCreatePlan = {},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DarkFocusPlanScreenPreview() {
    FocusPlanBuilderTheme(darkTheme = true) {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            FocusPlanScreen(
                subject = "Kotlin",
                minutesText = "30",
                plan = FocusPlan(
                    subject = "Kotlin",
                    minutes = 30,
                    category = "Focused session",
                    breakMinutes = 10
                ),
                onSubjectChange = {},
                onMinutesChange = {},
                canCreatePlan = true,
                onCreatePlan = {},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

