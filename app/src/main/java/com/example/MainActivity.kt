package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CardBackground
import com.example.ui.theme.CelestialCyan
import com.example.ui.theme.CosmicIndigo
import com.example.ui.theme.GlowOutline
import com.example.ui.theme.GrayTelemetry
import com.example.ui.theme.GridPanelBg
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NebulaViolet
import com.example.ui.theme.ShieldGreen
import com.example.ui.theme.SpaceVoid
import com.example.ui.theme.StarfieldDark
import com.example.ui.theme.StardustAmber
import com.example.ui.theme.TextCyan
import com.example.ui.theme.WarningRed
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets.systemBars
                ) { innerPadding ->
                    SpaceWeatherDashboard(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// Sparkle/Star effect container model
data class StarElement(
    val xRatio: Float,
    val yRatio: Float,
    val size: Float,
    val twinklePhase: Float,
    val color: Color
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SpaceWeatherDashboard(modifier: Modifier = Modifier) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedWeather by remember { mutableStateOf(WeatherData.predefinedLocations[0]) }
    var temperatureInKelvin by remember { mutableStateOf(false) }
    var shieldsActive by remember { mutableStateOf(false) }
    var warpFlashAlpha by remember { mutableFloatStateOf(0f) }
    var currentStardate by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    // Keep Stardate ticking for space atmosphere
    LaunchedEffect(Unit) {
        val dateFormat = SimpleDateFormat("HHmm.ss", Locale.US)
        while (true) {
            val date = Date()
            val baseSD = 2248 // Futuristic space year base
            val currentMilli = date.time % 100000 / 100
            currentStardate = "SD $baseSD.${dateFormat.format(date)}"
            delay(100)
        }
    }

    // Warped visual feedback effect when changing station
    LaunchedEffect(selectedWeather) {
        warpFlashAlpha = 1f
        while (warpFlashAlpha > 0f) {
            warpFlashAlpha -= 0.15f
            delay(30)
        }
    }

    // Generate fixed randomized points for starfield
    val starsList = remember {
        mutableStateListOf<StarElement>().apply {
            val random = Random(42)
            for (i in 0..120) {
                val color = when (random.nextInt(4)) {
                    0 -> CelestialCyan.copy(alpha = 0.6f + random.nextFloat() * 0.4f)
                    1 -> StardustAmber.copy(alpha = 0.5f + random.nextFloat() * 0.5f)
                    2 -> NebulaViolet.copy(alpha = 0.6f + random.nextFloat() * 0.4f)
                    else -> Color.White.copy(alpha = 0.7f + random.nextFloat() * 0.3f)
                }
                add(
                    StarElement(
                        xRatio = random.nextFloat(),
                        yRatio = random.nextFloat(),
                        size = 1f + random.nextFloat() * 3.5f,
                        twinklePhase = random.nextFloat() * 2f * Math.PI.toFloat(),
                        color = color
                    )
                )
            }
        }
    }

    // Twinkling offset update loop for background animation of stars
    var twinkleTicks by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            twinkleTicks = (twinkleTickCount + 1)
            delay(60)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(SpaceVoid, StarfieldDark, CosmicIndigo)
                )
            )
            .clickable(onClick = { focusManager.clearFocus() })
    ) {
        // 1. STARFIELD CONSOLE BACKGROUND
        StarfieldCanvas(
            stars = starsList,
            ticks = twinkleTicks,
            modifier = Modifier.fillMaxSize()
        )

        // 2. HUD STREAM LAYOUT
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // TOP HUB CABIN: Stardate and system telemetry readouts
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "INTERSTELLAR CABIN HUD v4.81",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = CelestialCyan.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = currentStardate,
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextCyan,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .border(1.dp, CelestialCyan.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .background(Color(0x3300C3FF))
                ) {
                    Canvas(modifier = Modifier.size(8.dp)) {
                        drawCircle(color = if (shieldsActive) ShieldGreen else StardustAmber)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (shieldsActive) "SHIELDS: ACTIVE" else "SHIELDS: STANDBY",
                        fontSize = 10.sp,
                        color = TextCyan,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // QUERY AND SEARCH COMPONENT
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_station_input"),
                placeholder = {
                    Text(
                        text = "Scan cosmos or Earth city coordinates...",
                        color = GrayTelemetry,
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Monospace
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Radar",
                        tint = CelestialCyan
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                selectedWeather = WeatherData.getWeatherFor(searchQuery)
                                focusManager.clearFocus()
                            },
                            modifier = Modifier.testTag("search_radar_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Calibrate Sensor",
                                tint = CelestialCyan
                            )
                        }
                    }
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CelestialCyan,
                    unfocusedBorderColor = GlowOutline.copy(alpha = 0.4f),
                    focusedContainerColor = GridPanelBg,
                    unfocusedContainerColor = Color(0x15000000),
                    focusedTextColor = TextCyan,
                    unfocusedTextColor = TextCyan
                ),
                shape = RoundedCornerShape(10.dp),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 15.sp,
                    letterSpacing = 0.5.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // AUTOCOMPLETE PRESETS / INSTANT WARP CAPSULES
            Text(
                text = "COORDINATES MEMORY:",
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                color = GrayTelemetry,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(WeatherData.predefinedLocations) { loc ->
                    val isActive = selectedWeather.name == loc.name
                    val borderAlpha by animateFloatAsState(if (isActive) 1f else 0.3f)
                    val bgColor = if (isActive) NebulaViolet.copy(alpha = 0.4f) else GridPanelBg

                    Box(
                        modifier = Modifier
                            .border(
                                width = if (isActive) 1.5.dp else 1.dp,
                                color = if (isActive) CelestialCyan else GlowOutline.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .background(bgColor, RoundedCornerShape(20.dp))
                            .clickable {
                                selectedWeather = loc
                                focusManager.clearFocus()
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = loc.name,
                            color = if (isActive) CelestialCyan else TextCyan.copy(alpha = 0.8f),
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = if (isActive) FontWeight.ExtraBold else FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // CABIN MONITOR: TELEMETRY DISPLAY AND METRICS
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    // MAIN COGNITIVE DISPLAY CORE CARD
                    Box(modifier = Modifier.fillMaxWidth()) {
                        MainWeatherTelemetryCard(
                            weather = selectedWeather,
                            tempInKelvin = temperatureInKelvin,
                            shieldsActive = shieldsActive
                        )

                        // Wrap visual flash alignment overlay
                        if (warpFlashAlpha > 0.01f) {
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(Color.White.copy(alpha = warpFlashAlpha * 0.4f))
                                    .border(4.dp, CelestialCyan.copy(alpha = warpFlashAlpha))
                            )
                        }
                    }
                }

                item {
                    // HUD ACTION PANEL (FOR DEFLECTOR SHIELDS AND STANDARD CODES)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, GlowOutline.copy(alpha = 0.25f), RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = CardBackground)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "STATION CONSOLE SYSTEMS",
                                color = CelestialCyan,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Canvas(modifier = Modifier.size(24.dp)) {
                                        drawCircle(
                                            color = if (shieldsActive) ShieldGreen else GrayTelemetry,
                                            style = Stroke(width = 2.dp.toPx())
                                        )
                                        drawCircle(
                                            color = if (shieldsActive) ShieldGreen.copy(alpha = 0.4f) else Color.Transparent,
                                            radius = 6.dp.toPx()
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "Atmospheric Deflector",
                                            color = TextCyan,
                                            fontSize = 13.sp,
                                            fontFamily = FontFamily.Monospace
                                        )
                                        Text(
                                            text = if (shieldsActive) "Dampens extreme storm alerts" else "Enables shield containment overlay",
                                            color = GrayTelemetry,
                                            fontSize = 9.sp,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }

                                Switch(
                                    checked = shieldsActive,
                                    onCheckedChange = { shieldsActive = it },
                                    modifier = Modifier.testTag("shield_toggle"),
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = SpaceVoid,
                                        checkedTrackColor = ShieldGreen,
                                        uncheckedThumbColor = SpaceVoid,
                                        uncheckedTrackColor = GrayTelemetry.copy(alpha = 0.5f)
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Canvas(modifier = Modifier.size(24.dp)) {
                                        drawCircle(
                                            color = if (temperatureInKelvin) StardustAmber else GrayTelemetry,
                                            style = Stroke(width = 2.dp.toPx())
                                        )
                                        // letter K inside
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "True Kelvin Standards",
                                            color = TextCyan,
                                            fontSize = 13.sp,
                                            fontFamily = FontFamily.Monospace
                                        )
                                        Text(
                                            text = "Converts metrics into deep space Kelvin scales",
                                            color = GrayTelemetry,
                                            fontSize = 9.sp,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }

                                Switch(
                                    checked = temperatureInKelvin,
                                    onCheckedChange = { temperatureInKelvin = it },
                                    modifier = Modifier.testTag("kelvin_toggle"),
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = SpaceVoid,
                                        checkedTrackColor = StardustAmber,
                                        uncheckedThumbColor = SpaceVoid,
                                        uncheckedTrackColor = GrayTelemetry.copy(alpha = 0.5f)
                                    )
                                )
                            }
                        }
                    }
                }

                item {
                    // TELEMETRY METRIC CABIN GRID
                    Text(
                        text = "ENVIRONMENTAL STATIONS DATA:",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = GrayTelemetry,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                        val screenWidth = maxWidth
                        val columns = if (screenWidth > 600.dp) 3 else 2
                        val rows = if (columns == 3) 2 else 3

                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            if (columns == 3) {
                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    GridTelemetryItem(
                                        label = "Atmosphere",
                                        value = selectedWeather.atmosphere,
                                        subValue = selectedWeather.comfortRating,
                                        statusColor = if (selectedWeather.comfortRating.contains("Fatal") || selectedWeather.comfortRating.contains("Toxicity")) WarningRed else ShieldGreen,
                                        modifier = Modifier.weight(1f)
                                    )
                                    GridTelemetryItem(
                                        label = "Solar Gale Force",
                                        value = "${selectedWeather.solarWindSpeed} km/s",
                                        subValue = if (selectedWeather.solarWindSpeed > 350f) "SEVERE RAD FLOW" else "NORMAL DUST VELOCITY",
                                        statusColor = if (selectedWeather.solarWindSpeed > 350f) WarningRed else CelestialCyan,
                                        modifier = Modifier.weight(1f)
                                    )
                                    GridTelemetryItem(
                                        label = "Gravity Level",
                                        value = selectedWeather.gravity,
                                        subValue = "Structural Stress 0.05%",
                                        statusColor = CelestialCyan,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    GridTelemetryItem(
                                        label = "Radiation Dosage",
                                        value = selectedWeather.radiationIndex,
                                        subValue = if (selectedWeather.radiationIndex.contains("CRITICAL")) "SHIELDS OVERBURDEN" else "DEFLECTION SATISFACTORY",
                                        statusColor = if (selectedWeather.radiationIndex.contains("CRITICAL")) WarningRed else ShieldGreen,
                                        modifier = Modifier.weight(1f)
                                    )
                                    GridTelemetryItem(
                                        label = "Vapor Material",
                                        value = selectedWeather.precipitationType,
                                        subValue = "${selectedWeather.precipitationProb}% Condensed Density",
                                        statusColor = if (selectedWeather.precipitationProb > 70) StardustAmber else CelestialCyan,
                                        modifier = Modifier.weight(1f)
                                    )
                                    GridTelemetryItem(
                                        label = "Shield Boundary",
                                        value = selectedWeather.shieldStatus,
                                        subValue = "Deflector Core Temp: Normal",
                                        statusColor = if (selectedWeather.shieldStatus.contains("Warning") || selectedWeather.shieldStatus.contains("URGENT")) StardustAmber else ShieldGreen,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            } else {
                                // Default grid format for compact screens
                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    GridTelemetryItem(
                                        label = "Atmosphere",
                                        value = selectedWeather.atmosphere,
                                        subValue = selectedWeather.comfortRating,
                                        statusColor = if (selectedWeather.comfortRating.contains("Fatal") || selectedWeather.comfortRating.contains("Toxicity")) WarningRed else ShieldGreen,
                                        modifier = Modifier.weight(1.2f)
                                    )
                                    GridTelemetryItem(
                                        label = "Solar Gale",
                                        value = "${selectedWeather.solarWindSpeed} km/s",
                                        subValue = if (selectedWeather.solarWindSpeed > 350f) "SEVERE RAD FLOW" else "NORMAL GALE VEL",
                                        statusColor = if (selectedWeather.solarWindSpeed > 350f) WarningRed else CelestialCyan,
                                        modifier = Modifier.weight(0.8f)
                                    )
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    GridTelemetryItem(
                                        label = "Gravity Level",
                                        value = selectedWeather.gravity,
                                        subValue = "Station Core Stress: 0.0%",
                                        statusColor = CelestialCyan,
                                        modifier = Modifier.weight(1f)
                                    )
                                    GridTelemetryItem(
                                        label = "Radiation",
                                        value = selectedWeather.radiationIndex,
                                        subValue = if (selectedWeather.radiationIndex.contains("CRITICAL")) "CORE RAD ALERTER" else "DEFLECTION NORMAL",
                                        statusColor = if (selectedWeather.radiationIndex.contains("CRITICAL")) WarningRed else ShieldGreen,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    GridTelemetryItem(
                                        label = "Vapor Material",
                                        value = selectedWeather.precipitationType,
                                        subValue = "${selectedWeather.precipitationProb}% Condensed Vol",
                                        statusColor = if (selectedWeather.precipitationProb > 70) StardustAmber else CelestialCyan,
                                        modifier = Modifier.weight(1f)
                                    )
                                    GridTelemetryItem(
                                        label = "System Shield",
                                        value = selectedWeather.shieldStatus,
                                        subValue = "Station Grid Stabilized",
                                        statusColor = if (selectedWeather.shieldStatus.contains("Warning") || selectedWeather.shieldStatus.contains("URGENT")) StardustAmber else ShieldGreen,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    // DYNAMIC DECO SINE OSCILLATOR BAR (atmospheric scanner stream - frequency adjusts with wind velocity!)
                    AtmosphericFrequencyOscillator(solarWindSpeed = selectedWeather.solarWindSpeed)
                }

                item {
                    // ORBITAL PROJECTIONS FOR FUTURE SOLS
                    Text(
                        text = "FUTURE ORBITAL CYCLES [PROJECTIONS]:",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = GrayTelemetry,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(selectedWeather.forecast) { day ->
                            FutureForecastCard(
                                forecastDay = day,
                                tempInKelvin = temperatureInKelvin
                            )
                        }
                    }
                }
            }
        }
    }
}

// Sparkle draw operation helper
private var twinkleTickCount = 0

@Composable
fun StarfieldCanvas(
    stars: List<StarElement>,
    ticks: Int,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        twinkleTickCount = ticks // Trigger redraw
        val canvasWidth = size.width
        val canvasHeight = size.height

        stars.forEach { star ->
            // twinkle alpha calculation
            val phaseOffset = star.twinklePhase
            val twinkleFactor = kotlin.math.sin(ticks * 0.15f + phaseOffset) * 0.45f + 0.55f
            val calculatedAlpha = star.color.alpha * twinkleFactor

            drawCircle(
                color = star.color.copy(alpha = calculatedAlpha),
                radius = star.size,
                center = Offset(star.xRatio * canvasWidth, star.yRatio * canvasHeight)
            )
        }
    }
}

@Composable
fun MainWeatherTelemetryCard(
    weather: WeatherInfo,
    tempInKelvin: Boolean,
    shieldsActive: Boolean,
    modifier: Modifier = Modifier
) {
    // Celsius conversion to Kelvin values
    val currentTempLabel = if (tempInKelvin) {
        "${(weather.tempCelsius + 273.15f).toInt()} K"
    } else {
        "${weather.tempCelsius.toInt()} °C"
    }

    val maxTempLabel = if (tempInKelvin) {
        "MAX: ${(weather.tempMaxCelsius + 273.15f).toInt()} K"
    } else {
        "MAX: ${weather.tempMaxCelsius.toInt()} °C"
    }

    val minTempLabel = if (tempInKelvin) {
        "MIN: ${(weather.tempMinCelsius + 273.15f).toInt()} K"
    } else {
        "MIN: ${weather.tempMinCelsius.toInt()} °C"
    }

    val shieldAnimationAlpha by animateFloatAsState(
        targetValue = if (shieldsActive) 0.95f else 0.0f,
        animationSpec = tween(500, easing = FastOutSlowInEasing)
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = if (shieldsActive) 2.dp else 1.dp,
                color = if (shieldsActive) ShieldGreen else CelestialCyan.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = weather.name.uppercase(Locale.ROOT),
                            color = CelestialCyan,
                            fontSize = 24.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "HOST ORBIT: ${weather.hostBody.uppercase(Locale.ROOT)}",
                                color = TextCyan.copy(alpha = 0.7f),
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    // Simple signal locator box
                    Box(
                        modifier = Modifier
                            .background(Color(0x2200FFCC), RoundedCornerShape(4.dp))
                            .border(1.dp, CelestialCyan.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "SEC-LOC: ${abs(weather.name.hashCode()) % 1000}",
                            color = CelestialCyan,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Programmatic vector illustration drawn inside a canvas depending on the space weather type
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color(0x1100C3FF), RoundedCornerShape(12.dp))
                            .border(1.dp, CelestialCyan.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        SpaceWeatherIconCanvas(
                            weatherType = weather.weatherType,
                            modifier = Modifier.size(75.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = currentTempLabel,
                            color = TextCyan,
                            fontSize = 38.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Black
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = minTempLabel,
                                color = GrayTelemetry,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "•",
                                color = GrayTelemetry,
                                fontSize = 11.sp
                            )
                            Text(
                                text = maxTempLabel,
                                color = StardustAmber,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Box(
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = if (weather.condition.contains("STABLE")) ShieldGreen.copy(alpha = 0.6f) else WarningRed.copy(alpha = 0.6f),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = weather.condition,
                                color = if (weather.condition.contains("STABLE")) ShieldGreen else StardustAmber,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Beautiful glowing shield mesh overlay
            if (shieldAnimationAlpha > 0.01f) {
                Canvas(
                    modifier = Modifier
                        .matchParentSize()
                ) {
                    val w = size.width
                    val h = size.height
                    // Draw outer energy arcs
                    drawRoundRect(
                        color = ShieldGreen.copy(alpha = shieldAnimationAlpha * 0.15f),
                        size = size,
                        cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx())
                    )

                    // Top left arc marker
                    drawLine(
                        color = ShieldGreen.copy(alpha = shieldAnimationAlpha),
                        start = Offset(10f, 30f),
                        end = Offset(30f, 10f),
                        strokeWidth = 3.dp.toPx()
                    )
                    // Bottom right arc marker
                    drawLine(
                        color = ShieldGreen.copy(alpha = shieldAnimationAlpha),
                        start = Offset(w - 30f, h - 10f),
                        end = Offset(w - 10f, h - 30f),
                        strokeWidth = 3.dp.toPx()
                    )
                }
            }
        }
    }
}

@Composable
fun SpaceWeatherIconCanvas(
    weatherType: SpaceWeatherType,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "icon_rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val pulsingFactor by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2
        val cy = h / 2

        when (weatherType) {
            SpaceWeatherType.SUNNY_SOL -> {
                // Programmatic brilliant solar flares and corona
                drawCircle(
                    color = StardustAmber.copy(alpha = 0.2f),
                    radius = (cx * 0.8f) * pulsingFactor
                )
                drawCircle(
                    color = StardustAmber,
                    radius = cx * 0.45f
                )
                // Draw radiating thermal spikes
                for (angle in 0 until 360 step 45) {
                    val angleRad = Math.toRadians((angle + rotation).toDouble())
                    val startDist = cx * 0.55f
                    val endDist = cx * 0.85f
                    val sx = cx + (kotlin.math.cos(angleRad) * startDist).toFloat()
                    val sy = cy + (kotlin.math.sin(angleRad) * startDist).toFloat()
                    val ex = cx + (kotlin.math.cos(angleRad) * endDist).toFloat()
                    val ey = cy + (kotlin.math.sin(angleRad) * endDist).toFloat()
                    drawLine(
                        color = StardustAmber,
                        start = Offset(sx, sy),
                        end = Offset(ex, ey),
                        strokeWidth = 2.dp.toPx()
                    )
                }
            }

            SpaceWeatherType.METHANE_RAIN -> {
                // Methane planet with orbital rings and droplets
                drawCircle(
                    color = CelestialCyan,
                    radius = cx * 0.35f
                )
                // Draw sloping planetary ring path
                drawOval(
                    color = NebulaViolet.copy(alpha = 0.5f),
                    topLeft = Offset(cx - cx * 0.8f, cy - cy * 0.2f),
                    size = Size(cx * 1.6f, cy * 0.4f),
                    style = Stroke(width = 2.5.dp.toPx())
                )
                // Dropping methane diagonal sticks
                for (i in 0..4) {
                    val rx = cx - 30f + (i * 18f)
                    val ry = cy - 40f + (i * 20f) % 45
                    drawLine(
                        color = CelestialCyan.copy(alpha = 0.7f),
                        start = Offset(rx, ry),
                        end = Offset(rx - 8f, ry + 16f),
                        strokeWidth = 1.7.dp.toPx()
                    )
                }
            }

            SpaceWeatherType.SULFURIC_FOG -> {
                // Corrosive cloud deck arcs
                val path1 = Path().apply {
                    moveTo(cx - cx * 0.7f, cy + cy * 0.3f)
                    quadraticTo(cx - cx * 0.3f, cy - cy * 0.3f, cx, cy + cy * 0.3f)
                    quadraticTo(cx + cx * 0.3f, cy - cy * 0.3f, cx + cx * 0.7f, cy + cy * 0.3f)
                }
                val path2 = Path().apply {
                    moveTo(cx - cx * 0.5f, cy + cy * 0.5f)
                    quadraticTo(cx - cx * 0.1f, cy + cy * 0.0f, cx + cx * 0.3f, cy + cy * 0.5f)
                }
                drawPath(
                    path = path1,
                    color = StardustAmber,
                    style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                )
                drawPath(
                    path = path2,
                    color = WarningRed.copy(alpha = 0.7f),
                    style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                )
                // Haze horizontal speed streaks
                drawLine(
                    color = GrayTelemetry.copy(alpha = 0.4f),
                    start = Offset(cx - cx * 0.8f, cy - cy * 0.5f),
                    end = Offset(cx + cx * 0.3f, cy - cy * 0.5f),
                    strokeWidth = 1.5.dp.toPx()
                )
                drawLine(
                    color = GrayTelemetry.copy(alpha = 0.4f),
                    start = Offset(cx - cx * 0.3f, cy - cy * 0.3f),
                    end = Offset(cx + cx * 0.8f, cy - cy * 0.3f),
                    strokeWidth = 1.5.dp.toPx()
                )
            }

            SpaceWeatherType.ORBITAL_ICE -> {
                // Sharp technical revolving ice crystal rings
                drawCircle(
                    color = CelestialCyan.copy(alpha = 0.12f),
                    radius = cx * 0.8f,
                    style = Stroke(width = 1.dp.toPx(), pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(10f, 10f)))
                )

                // Crystal polygon hex representation
                val polyPath = Path().apply {
                    moveTo(cx, cy - 25f * pulsingFactor)
                    lineTo(cx + 22f * pulsingFactor, cy - 10f)
                    lineTo(cx + 22f * pulsingFactor, cy + 15f)
                    lineTo(cx, cy + 28f * pulsingFactor)
                    lineTo(cx - 22f * pulsingFactor, cy + 15f)
                    lineTo(cx - 22f * pulsingFactor, cy - 10f)
                    close()
                }
                drawPath(
                    path = polyPath,
                    color = CelestialCyan,
                    style = Stroke(width = 2.dp.toPx())
                )
                // Draw orbital satellite dot
                val satAngleRad = Math.toRadians(rotation.toDouble())
                val satX = cx + (kotlin.math.cos(satAngleRad) * cx * 0.8f).toFloat()
                val satY = cy + (kotlin.math.sin(satAngleRad) * cx * 0.8f).toFloat()
                drawCircle(
                    color = WarningRed,
                    radius = 4.dp.toPx(),
                    center = Offset(satX, satY)
                )
            }

            SpaceWeatherType.COSMIC_AURORA -> {
                // Sine wave colorful aurora curtain lines
                val pulseOffset = rotation * 0.05f
                val aurColors = listOf(CelestialCyan, ShieldGreen, NebulaViolet)
                aurColors.forEachIndexed { i, col ->
                    val wavePath = Path()
                    val dyOffset = (i * 12) - 12
                    wavePath.moveTo(cx - cx * 0.8f, cy + dyOffset)
                    for (x in 2..100) {
                        val screenX = cx - cx * 0.8f + (x * w * 1.6f / 100) * 0.5f
                        val screenY = cy + dyOffset + (kotlin.math.sin(x * 0.15f + pulseOffset + i) * 14).toFloat()
                        wavePath.lineTo(screenX, screenY)
                    }
                    drawPath(
                        path = wavePath,
                        color = col.copy(alpha = 0.65f),
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
            }

            SpaceWeatherType.DUST_STREAKS -> {
                // Martian dusty atmosphere whirl
                drawCircle(
                    color = WarningRed.copy(alpha = 0.3f),
                    radius = cx * 0.55f
                )
                // Diagonal sweeping dust streaks
                for (i in -2..2) {
                    val y = cy + i * 16f
                    drawLine(
                        color = StardustAmber,
                        start = Offset(cx - cx * 0.7f, y - 10f),
                        end = Offset(cx + cx * 0.6f, y + 10f),
                        strokeWidth = (2 - abs(i)).coerceAtLeast(1).toFloat().dp.toPx()
                    )
                }
                // Little rotating pebble specs
                drawCircle(
                    color = StardustAmber,
                    radius = 2.dp.toPx(),
                    center = Offset(cx + 25f, cy - 25f)
                )
                drawCircle(
                    color = WarningRed,
                    radius = 1.5.dp.toPx(),
                    center = Offset(cx - 30f, cy + 20f)
                )
            }

            SpaceWeatherType.STANDARD_CLOUDY -> {
                // Technical retro cloud
                val cloudPath = Path().apply {
                    moveTo(cx - 20f, cy + 10f)
                    quadraticTo(cx - 35f, cy, cx - 20f, cy - 15f)
                    quadraticTo(cx, cy - 30f, cx + 20f, cy - 15f)
                    quadraticTo(cx + 35f, cy, cx + 20f, cy + 10f)
                    close()
                }
                drawPath(
                    path = cloudPath,
                    color = GrayTelemetry,
                    style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                )
                // Backing shadow cloud trace
                drawPath(
                    path = cloudPath,
                    color = CelestialCyan.copy(alpha = 0.25f),
                    alpha = 0.4f
                )
            }

            SpaceWeatherType.STANDARD_RAINY -> {
                // Cloud with rain streaks
                val cloudPath = Path().apply {
                    moveTo(cx - 20f, cy + 5f)
                    quadraticTo(cx - 35f, cy - 5f, cx - 20f, cy - 20f)
                    quadraticTo(cx, cy - 35f, cx + 20f, cy - 20f)
                    quadraticTo(cx + 35f, cy - 5f, cx + 20f, cy + 5f)
                    close()
                }
                drawPath(
                    path = cloudPath,
                    color = GrayTelemetry,
                    style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                )
                // Downward water ticks
                for (dx in -15..15 step 10) {
                    val rx = cx + dx
                    drawLine(
                        color = CelestialCyan,
                        start = Offset(rx, cy + 10f),
                        end = Offset(rx - 3f, cy + 22f),
                        strokeWidth = 1.5.dp.toPx()
                    )
                }
            }

            SpaceWeatherType.STANDARD_CLEAR -> {
                // Clean orbital satellite ring around a planet
                drawCircle(
                    color = CelestialCyan,
                    radius = cx * 0.45f,
                    style = Stroke(width = 2.dp.toPx())
                )
                drawOval(
                    color = StardustAmber,
                    topLeft = Offset(cx - cx * 0.8f, cy - cy * 0.15f),
                    size = Size(cx * 1.6f, cy * 0.3f),
                    style = Stroke(width = 1.3.dp.toPx())
                )
            }
        }
    }
}

@Composable
fun GridTelemetryItem(
    label: String,
    value: String,
    subValue: String,
    statusColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, GlowOutline.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = GridPanelBg)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label.uppercase(Locale.ROOT),
                    color = GrayTelemetry,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                Canvas(modifier = Modifier.size(6.dp)) {
                    drawCircle(color = statusColor)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = value,
                color = TextCyan,
                fontSize = 13.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subValue,
                color = TextCyan.copy(alpha = 0.6f),
                fontSize = 8.sp,
                fontFamily = FontFamily.Monospace,
                lineHeight = 11.sp
            )
        }
    }
}

@Composable
fun AtmosphericFrequencyOscillator(
    solarWindSpeed: Float,
    modifier: Modifier = Modifier
) {
    val speedScaleFactor = (solarWindSpeed / 100f).coerceIn(0.5f, 6.0f)
    val infiniteTransition = rememberInfiniteTransition(label = "osc_wave")
    val sweepOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sweep"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, GlowOutline.copy(alpha = 0.12f), RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0x22050518))
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ATMOSPHERIC IONIZED WAVE SCANNER",
                    color = CelestialCyan,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "FREQ OSC: ${(8.4 * speedScaleFactor).toInt()} GHz",
                    color = GrayTelemetry,
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Canvas drawing live wavy sine lines representing the breeze of the atmosphere
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
            ) {
                val cw = size.width
                val ch = size.height
                val midY = ch / 2
                val path = Path()

                path.moveTo(0f, midY)
                for (x in 0..100) {
                    val px = (x / 100f) * cw
                    val py = midY + (kotlin.math.sin(x * 0.18f - sweepOffset) * 10f * (speedScaleFactor * 0.8f)).toFloat()
                    path.lineTo(px, py)
                }

                drawPath(
                    path = path,
                    color = CelestialCyan.copy(alpha = 0.9f),
                    style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round)
                )

                // Background scan shadow
                val pathShadow = Path()
                pathShadow.moveTo(0f, midY)
                for (x in 0..100) {
                    val px = (x / 100f) * cw
                    val py = midY + (kotlin.math.sin(x * 0.18f - sweepOffset - 0.4f) * 10f * (speedScaleFactor * 0.8f)).toFloat()
                    pathShadow.lineTo(px, py)
                }
                drawPath(
                    path = pathShadow,
                    color = NebulaViolet.copy(alpha = 0.4f),
                    style = Stroke(width = 1.2.dp.toPx(), cap = StrokeCap.Round)
                )
            }
        }
    }
}

@Composable
fun FutureForecastCard(
    forecastDay: ForecastDay,
    tempInKelvin: Boolean,
    modifier: Modifier = Modifier
) {
    val tempLabel = if (tempInKelvin) {
        "${(forecastDay.tempCelsius + 273.15f).toInt()} K"
    } else {
        "${forecastDay.tempCelsius.toInt()} °C"
    }

    Card(
        modifier = modifier
            .width(92.dp)
            .border(1.dp, GlowOutline.copy(alpha = 0.12f), RoundedCornerShape(10.dp)),
        colors = CardDefaults.cardColors(containerColor = GridPanelBg)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = forecastDay.dayLabel,
                color = GrayTelemetry,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .size(45.dp)
                    .background(Color(0x0F00C3FF), RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                SpaceWeatherIconCanvas(
                    weatherType = forecastDay.weatherType,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = tempLabel,
                color = TextCyan,
                fontSize = 13.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = forecastDay.condition.split(" ").firstOrNull()?.uppercase(Locale.ROOT) ?: "SEC-X",
                color = StardustAmber,
                fontSize = 8.sp,
                fontFamily = FontFamily.Monospace,
                maxLines = 1
            )
        }
    }
}
