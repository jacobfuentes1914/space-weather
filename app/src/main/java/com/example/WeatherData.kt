package com.example

import java.util.Locale
import kotlin.math.abs

data class WeatherInfo(
    val name: String,
    val hostBody: String, // e.g., "Terra-3", "Sol-4 [Mars]", "Kepler System"
    val tempCelsius: Float,
    val tempMinCelsius: Float,
    val tempMaxCelsius: Float,
    val condition: String, // e.g. "Sunny Sol", "Heavy Methane Storm"
    val precipitationType: String, // e.g. "Water (H₂O)", "Methane (CH₄)", "Sulfuric Acid (H₂SO₄)"
    val precipitationProb: Int, // percentage
    val gravity: String, // e.g. "1.00G [Earth Normal]", "0.38G [Low Gravity]"
    val solarWindSpeed: Float, // km/s
    val radiationIndex: String, // "NORMAL", "ELEVATED", "CRITICAL"
    val atmosphere: String, // e.g., "N₂: 78%, O₂: 21% [Breathable]"
    val shieldStatus: String, // "Optimal Deflection", "Warning - Active Deflection"
    val comfortRating: String, // "Habitable", "Class-4 Atmosphere Suit Req"
    val weatherType: SpaceWeatherType,
    val forecast: List<ForecastDay>
)

enum class SpaceWeatherType {
    SUNNY_SOL,
    METHANE_RAIN,
    SULFURIC_FOG,
    ORBITAL_ICE,
    COSMIC_AURORA,
    DUST_STREAKS,
    STANDARD_CLOUDY,
    STANDARD_RAINY,
    STANDARD_CLEAR
}

data class ForecastDay(
    val dayLabel: String, // e.g. "SOL 01", "SOL 02"
    val tempCelsius: Float,
    val condition: String,
    val weatherType: SpaceWeatherType
)

object WeatherData {

    val predefinedLocations = listOf(
        WeatherInfo(
            name = "Elysium Outpost",
            hostBody = "Sol-4 [Mars]",
            tempCelsius = -63f,
            tempMinCelsius = -110f,
            tempMaxCelsius = -15f,
            condition = "IRON DUST TRAILS & THIN CO₂ COLD",
            precipitationType = "Carbon Dioxide Ice (CO₂)",
            precipitationProb = 15,
            gravity = "0.38G [Low Gravity]",
            solarWindSpeed = 480.2f,
            radiationIndex = "ELEVATED",
            atmosphere = "CO₂: 95%, N₂: 2.8%, Ar: 2%",
            shieldStatus = "Warning: Active Particle Influx",
            comfortRating = "Fatal Breathable State - Full Suit Required",
            weatherType = SpaceWeatherType.DUST_STREAKS,
            forecast = listOf(
                ForecastDay("SOL 01", -60f, "Dust Dust Trails", SpaceWeatherType.DUST_STREAKS),
                ForecastDay("SOL 02", -58f, "Sublimation Mist", SpaceWeatherType.DUST_STREAKS),
                ForecastDay("SOL 03", -68f, "Extreme Core Freeze", SpaceWeatherType.ORBITAL_ICE),
                ForecastDay("SOL 04", -75f, "Carbonate Scurry", SpaceWeatherType.ORBITAL_ICE),
                ForecastDay("SOL 05", -55f, "Faint Solar Glint", SpaceWeatherType.SUNNY_SOL)
            )
        ),
        WeatherInfo(
            name = "Kraken Mare Base",
            hostBody = "Sol-6c [Titan]",
            tempCelsius = -179f,
            tempMinCelsius = -185f,
            tempMaxCelsius = -175f,
            condition = "METHANE DELUGE & CRYOPOLAR HAZE",
            precipitationType = "Methane Liquid (CH₄)",
            precipitationProb = 95,
            gravity = "0.14G [Ultra-Low Gravity]",
            solarWindSpeed = 125.4f,
            radiationIndex = "SAFE / MAGNETO SHELTERED",
            atmosphere = "N₂: 95%, CH₄: 4.9% [Hyper Pressurized]",
            shieldStatus = "Stable: Heavy Canopy Cover",
            comfortRating = "Cryogenic Fluid Hazard - Thermal Suit Req",
            weatherType = SpaceWeatherType.METHANE_RAIN,
            forecast = listOf(
                ForecastDay("SOL 01", -178f, "Liquid Downpour", SpaceWeatherType.METHANE_RAIN),
                ForecastDay("SOL 02", -180f, "Static Orange Fog", SpaceWeatherType.SULFURIC_FOG),
                ForecastDay("SOL 03", -179f, "Methane Mist Ripple", SpaceWeatherType.METHANE_RAIN),
                ForecastDay("SOL 04", -182f, "High Condensation", SpaceWeatherType.SULFURIC_FOG),
                ForecastDay("SOL 05", -177f, "Haze Clearing", SpaceWeatherType.STANDARD_CLOUDY)
            )
        ),
        WeatherInfo(
            name = "Ishtar Cloud Lab",
            hostBody = "Sol-2 [Venus Clouddeck]",
            tempCelsius = 27f,
            tempMinCelsius = 15f,
            tempMaxCelsius = 42f,
            condition = "CORROSIVE ACID DRIZZLE & SPEED-7 STORM",
            precipitationType = "Sulfuric Acid (H₂SO₄)",
            precipitationProb = 80,
            gravity = "0.90G [Near-Earth Normal]",
            solarWindSpeed = 520.8f,
            radiationIndex = "CRITICAL FLARE DIRECT EVENT",
            atmosphere = "CO₂: 96.5%, N₂: 3.5% [Corrosive Acid Lines]",
            shieldStatus = "URGENT REINFORCE: Corrosive Ions Peak",
            comfortRating = "Extreme Atmos Toxicity - Shield Bio-Suite Req",
            weatherType = SpaceWeatherType.SULFURIC_FOG,
            forecast = listOf(
                ForecastDay("SOL 01", 28f, "Dense Acid Fumes", SpaceWeatherType.SULFURIC_FOG),
                ForecastDay("SOL 02", 30f, "Cyclone Gale Rain", SpaceWeatherType.SULFURIC_FOG),
                ForecastDay("SOL 03", 26f, "Supercritical Fog", SpaceWeatherType.SULFURIC_FOG),
                ForecastDay("SOL 04", 25f, "Electrostatic Discharge", SpaceWeatherType.COSMIC_AURORA),
                ForecastDay("SOL 05", 35f, "Yellow Sun Filtered", SpaceWeatherType.STANDARD_CLOUDY)
            )
        ),
        WeatherInfo(
            name = "Shackleton Crater Intel",
            hostBody = "Terra-Luna [Moon]",
            tempCelsius = -50f,
            tempMinCelsius = -173f,
            tempMaxCelsius = 120f,
            condition = "PURE CORE VACUUM & COSMIC FLARES",
            precipitationType = "None (Vacuum)",
            precipitationProb = 0,
            gravity = "0.16G [Low-G Bounce]",
            solarWindSpeed = 580.4f,
            radiationIndex = "CRITICAL / IONIZED CORONA",
            atmosphere = "Exosphere traces strictly [Noble Gases]",
            shieldStatus = "Warning: Magnetosphere Deflection Empty",
            comfortRating = "Absolute Vacuum - Sealed Oxygen Suit Req",
            weatherType = SpaceWeatherType.ORBITAL_ICE,
            forecast = listOf(
                ForecastDay("SOL 01", -45f, "Pristine Vacuum", SpaceWeatherType.ORBITAL_ICE),
                ForecastDay("SOL 02", 30f, "Intense Heat Stroke", SpaceWeatherType.SUNNY_SOL),
                ForecastDay("SOL 03", 110f, "Extreme Core Glow", SpaceWeatherType.SUNNY_SOL),
                ForecastDay("SOL 04", -120f, "Abyssal Shadow Freeze", SpaceWeatherType.ORBITAL_ICE),
                ForecastDay("SOL 05", -140f, "Starlight Echo Space", SpaceWeatherType.ORBITAL_ICE)
            )
        ),
        WeatherInfo(
            name = "Colony Prime, Kepler-186f",
            hostBody = "Kepler-186 [Red Dwarf System]",
            tempCelsius = 8f,
            tempMinCelsius = -5f,
            tempMaxCelsius = 18f,
            condition = "LUMINESCENT RED TWILIGHT & ACTIVE GEOMAGNETIC SHIFT",
            precipitationType = "Water Dew (H₂O)",
            precipitationProb = 40,
            gravity = "1.12G [High-Mass Pull]",
            solarWindSpeed = 95.8f,
            radiationIndex = "SAFE / HEAVY MAGNETOSPHERE",
            atmosphere = "N₂: 74%, O₂: 19%, Ne: 4% [Oxygen Stable]",
            shieldStatus = "Optimal Deflection: Auroral Shield Active",
            comfortRating = "Breathable Atmosphere - Filter Respirator Only",
            weatherType = SpaceWeatherType.COSMIC_AURORA,
            forecast = listOf(
                ForecastDay("SOL 01", 8f, "Aurora Ribbons Glow", SpaceWeatherType.COSMIC_AURORA),
                ForecastDay("SOL 02", 10f, "Red Sun Dawn Mist", SpaceWeatherType.COSMIC_AURORA),
                ForecastDay("SOL 03", 12f, "High Dew Condensation", SpaceWeatherType.STANDARD_CLOUDY),
                ForecastDay("SOL 04", 7f, "Nebula Stream Shower", SpaceWeatherType.STANDARD_RAINY),
                ForecastDay("SOL 05", 9f, "Celestial Clear Sky", SpaceWeatherType.STANDARD_CLEAR)
            )
        ),
        WeatherInfo(
            name = "New York",
            hostBody = "Terra-3 [Primary Earth Sector]",
            tempCelsius = 18f,
            tempMinCelsius = 11f,
            tempMaxCelsius = 25f,
            condition = "STABLE TERRESTRIAL HARMONY & DRIZZLE OUTLOOK",
            precipitationType = "Water Droplets (H₂O)",
            precipitationProb = 65,
            gravity = "1.00G [Standard Base Value]",
            solarWindSpeed = 12.5f,
            radiationIndex = "NORMAL / OZONE FILTERED",
            atmosphere = "N₂: 78.1%, O₂: 20.9%, Ar: 0.9%, CO₂: 0.04%",
            shieldStatus = "Standby: Atmospheric Deflectors Passive",
            comfortRating = "Standard Ambient Biosphere - Zero Suit Req",
            weatherType = SpaceWeatherType.STANDARD_RAINY,
            forecast = listOf(
                ForecastDay("SOL 01", 19f, "Mild Drizzle Traces", SpaceWeatherType.STANDARD_RAINY),
                ForecastDay("SOL 02", 22f, "Cloud Cover Overlay", SpaceWeatherType.STANDARD_CLOUDY),
                ForecastDay("SOL 03", 24f, "Solar Thermal Flare", SpaceWeatherType.STANDARD_CLEAR),
                ForecastDay("SOL 04", 17f, "Vapor Condensation", SpaceWeatherType.STANDARD_RAINY),
                ForecastDay("SOL 05", 15f, "Crisp Terrestrial Dawn", SpaceWeatherType.STANDARD_CLEAR)
            )
        ),
        WeatherInfo(
            name = "Tokyo",
            hostBody = "Terra-3 [Primary Earth Sector]",
            tempCelsius = 22f,
            tempMinCelsius = 16f,
            tempMaxCelsius = 28f,
            condition = "MODERATE GALE AND HIGH TECH OVERCAST COFFEE",
            precipitationType = "Water Droplets (H₂O)",
            precipitationProb = 30,
            gravity = "1.00G [Standard Base Value]",
            solarWindSpeed = 15.1f,
            radiationIndex = "NORMAL / OZONE FILTERED",
            atmosphere = "N₂: 78.1%, O₂: 20.9%, Ar: 0.9%",
            shieldStatus = "Standby: Shield Enforcer Inactive",
            comfortRating = "Standard Ambient Biosphere - Space-Tee Wear",
            weatherType = SpaceWeatherType.STANDARD_CLOUDY,
            forecast = listOf(
                ForecastDay("SOL 01", 21f, "Cumulus Congestion", SpaceWeatherType.STANDARD_CLOUDY),
                ForecastDay("SOL 02", 23f, "High Altitude Fog", SpaceWeatherType.STANDARD_CLOUDY),
                ForecastDay("SOL 03", 26f, "Optimal Sunny Sol", SpaceWeatherType.STANDARD_CLEAR),
                ForecastDay("SOL 04", 25f, "Humid Overcast Cycle", SpaceWeatherType.STANDARD_CLOUDY),
                ForecastDay("SOL 05", 20f, "Gentle Thermal Shower", SpaceWeatherType.STANDARD_RAINY)
            )
        ),
        WeatherInfo(
            name = "London",
            hostBody = "Terra-3 [Primary Earth Sector]",
            tempCelsius = 14f,
            tempMinCelsius = 9f,
            tempMaxCelsius = 18f,
            condition = "ENDLESS FOG MATRIX & PERPETUAL STEAM DRIZZLE",
            precipitationType = "Water Vapor H₂O",
            precipitationProb = 85,
            gravity = "1.00G [Standard Base Value]",
            solarWindSpeed = 8.4f,
            radiationIndex = "NORMAL / MAGNETOSPHERE BOUND",
            atmosphere = "N₂: 78.1%, O₂: 20.9%, H₂O Heavy Vapor",
            shieldStatus = "Standby: Comfort Shielding 100%",
            comfortRating = "Standard Ambient Biosphere - Umbrella Recommended",
            weatherType = SpaceWeatherType.STANDARD_RAINY,
            forecast = listOf(
                ForecastDay("SOL 01", 13f, "Saturated Overcast", SpaceWeatherType.STANDARD_RAINY),
                ForecastDay("SOL 02", 14f, "Classic London Fog", SpaceWeatherType.SULFURIC_FOG),
                ForecastDay("SOL 03", 15f, "Breathing Precipitate", SpaceWeatherType.STANDARD_RAINY),
                ForecastDay("SOL 04", 16f, "Fleeting Glint Sol", SpaceWeatherType.STANDARD_CLOUDY),
                ForecastDay("SOL 05", 12f, "Dew Drops Density", SpaceWeatherType.STANDARD_RAINY)
            )
        )
    )

    fun getWeatherFor(query: String): WeatherInfo {
        val cleanQuery = query.trim().lowercase(Locale.ROOT)
        if (cleanQuery.isEmpty()) {
            return predefinedLocations[0] // Default to Elysium Outpost
        }

        // Search predefined
        val match = predefinedLocations.find {
            it.name.lowercase(Locale.ROOT).contains(cleanQuery) ||
            it.hostBody.lowercase(Locale.ROOT).contains(cleanQuery)
        }
        if (match != null) {
            return match
        }

        // Deterministically generate based on query string to ensure ANY typed city works!
        val seed = abs(query.hashCode())
        val isSpaceQuery = cleanQuery.contains("base") || 
                           cleanQuery.contains("outpost") || 
                           cleanQuery.contains("crater") || 
                           cleanQuery.contains("orbit") || 
                           cleanQuery.contains("quadrant") || 
                           cleanQuery.contains("system") ||
                           seed % 5 == 0

        val formattedName = query.split(" ").joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }

        return if (isSpaceQuery) {
            // Generate customized Sci-Fi location
            val bodies = listOf("Sol-4 [Mars]", "Jupiter-II [Europa]", "Saturn-VI [Enceladus]", "Kepler-452b System", "Beta-Geminorum Prime", "Orion Nebula Segment-12")
            val selectedBody = bodies[seed % bodies.size]
            val baseTemp = (seed % 150) - 130f // range: -130C to +20C
            val conditionOptions = listOf("ACTIVE PROTON WIND COLLUSION", "METEOR SHOWER CORONA RAIN", "FROZEN HELIUM CRYOCONVEX", "HIGH INTENSITY COSMIC SPARKING", "AMMONIA CONDENSATION BLAST")
            val chosenCondition = conditionOptions[seed % conditionOptions.size]
            val atmosOptions = listOf("CH₄: 85%, NH₃: 10%", "He: 75%, H₂: 24%", "Ar: 92%, Ne: 5%, Kr: 1%", "No atmosphere [COSMIC VACUUM]", "N₂: 88%, Methane: 12% Vapor")
            val precipOptions = listOf("Methane Ice Drops (CH₄)", "Frozen Helium Hail", "Ammonia Drizzle", "Silicate Sand Trails", "Space Ice Crystals")
            val selectedWeatherType = SpaceWeatherType.entries[seed % SpaceWeatherType.entries.size]

            WeatherInfo(
                name = formattedName,
                hostBody = selectedBody,
                tempCelsius = baseTemp,
                tempMinCelsius = baseTemp - 25f,
                tempMaxCelsius = baseTemp + 15f,
                condition = chosenCondition,
                precipitationType = precipOptions[seed % precipOptions.size],
                precipitationProb = (seed % 90) + 10,
                gravity = "0.${(seed % 80) + 10}G [Reduced Gravity Cabin]",
                solarWindSpeed = (seed % 650) + 80f,
                radiationIndex = if (seed % 3 == 0) "CRITICAL PROTONS" else "ELEVATED CORONA",
                atmosphere = atmosOptions[seed % atmosOptions.size],
                shieldStatus = "Evasive Calibration Active",
                comfortRating = "Hostiles Detected - Heavy Hazard Space Wear",
                weatherType = selectedWeatherType,
                forecast = (1..5).map { index ->
                    ForecastDay(
                        dayLabel = "SOL 0$index",
                        tempCelsius = baseTemp + (seed % 10) - 5f - index,
                        condition = "Cosmic Projection $index",
                        weatherType = SpaceWeatherType.entries[(seed + index) % SpaceWeatherType.entries.size]
                    )
                }
            )
        } else {
            // Generate typical Earth-like coordinates styled in Telemetry UI
            val baseTemp = (seed % 35) + 0f // range: 0C to 35C
            val conditionOptions = listOf("LOCAL RAIN PRECIPITATE CYCLONE", "CUMULUS STRATIFIED MIST LEVEL-2", "THERMAL RADIAL SOL STALWART", "MICRO-PARTICLE HUMID DUST", "WARM SHADES OUTLOOK STABLE")
            val chosenCondition = conditionOptions[seed % conditionOptions.size]
            val precipProb = (seed % 85) + 5
            val selectedWeatherType = when {
                precipProb > 60 -> SpaceWeatherType.STANDARD_RAINY
                precipProb > 30 -> SpaceWeatherType.STANDARD_CLOUDY
                else -> SpaceWeatherType.STANDARD_CLEAR
            }

            WeatherInfo(
                name = formattedName,
                hostBody = "Terra-3 [Primary Earth Sector]",
                tempCelsius = baseTemp,
                tempMinCelsius = baseTemp - 8f,
                tempMaxCelsius = baseTemp + 9f,
                condition = chosenCondition,
                precipitationType = "Water Droplets (H₂O)",
                precipitationProb = precipProb,
                gravity = "1.00G [Standard Base Value]",
                solarWindSpeed = (seed % 28) + 5f,
                radiationIndex = "NORMAL / EXOSPHERE ABSORBED",
                atmosphere = "N₂: 78.1%, O₂: 20.9%, H₂O Moisture traces",
                shieldStatus = "Standby: Biosphere Buffer Active",
                comfortRating = "Standard Ambient Biosphere - Safe Base Clothing",
                weatherType = selectedWeatherType,
                forecast = (1..5).map { index ->
                    ForecastDay(
                        dayLabel = "SOL 0$index",
                        tempCelsius = baseTemp + (seed % 6) - 3f - (index * 0.4f),
                        condition = "Atmospheric Cycle $index",
                        weatherType = if ((seed + index) % 4 == 0) SpaceWeatherType.STANDARD_RAINY else SpaceWeatherType.STANDARD_CLEAR
                    )
                }
            )
        }
    }
}
