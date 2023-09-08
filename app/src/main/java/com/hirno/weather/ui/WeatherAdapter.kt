package com.hirno.weather.ui

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.hirno.weather.utils.backgroundRes
import com.hirno.weather.utils.drawableStart
import com.hirno.weather.utils.drawableTintRes
import com.hirno.weather.utils.getString
import com.hirno.weather.utils.iconicsDrawable
import com.hirno.weather.utils.imageDrawable
import com.hirno.weather.utils.inflater
import com.hirno.weather.utils.showIfNotNull
import com.hirno.weather.utils.textColorRes
import com.hirno.weather.utils.textRes
import com.hirno.weather.R
import com.hirno.weather.databinding.WeatherItemCurrentTempBinding
import com.hirno.weather.databinding.WeatherItemHighLowTempBinding
import com.hirno.weather.databinding.WeatherItemNextDayForecastBinding
import com.hirno.weather.databinding.WeatherItemTempGraphBinding
import com.hirno.weather.databinding.WeatherItemUvSunriseSunsetBinding
import com.hirno.weather.databinding.WeatherItemWindPrecipitationHumidityBinding
import com.hirno.weather.model.WeatherListBlock
import com.hirno.weather.model.WeatherListBlock.Companion.VIEW_TYPE_CURRENT_TEMP
import com.hirno.weather.model.WeatherListBlock.Companion.VIEW_TYPE_HIGH_LOW_TEMP
import com.hirno.weather.model.WeatherListBlock.Companion.VIEW_TYPE_NEXT_DAY_FORECAST
import com.hirno.weather.model.WeatherListBlock.Companion.VIEW_TYPE_UV_SUNRISE_SUNSET
import com.hirno.weather.model.WeatherListBlock.Companion.VIEW_TYPE_WIND_PRECIPITATION_HUMIDITY
import com.hirno.weather.model.WeatherListBlock.CurrentTemperature
import com.hirno.weather.model.WeatherListBlock.HighLowTemp
import com.hirno.weather.model.WeatherListBlock.HumidityPercentage
import com.hirno.weather.model.WeatherListBlock.NextDayForecast
import com.hirno.weather.model.WeatherListBlock.PrecipitationSum
import com.hirno.weather.model.WeatherListBlock.SunriseTime
import com.hirno.weather.model.WeatherListBlock.SunsetTime
import com.hirno.weather.model.WeatherListBlock.TemperatureGraph
import com.hirno.weather.model.WeatherListBlock.UvIndex
import com.hirno.weather.model.WeatherListBlock.WindSpeed
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import com.mikepenz.iconics.typeface.library.weathericons.WeatherIcons
import com.mikepenz.iconics.utils.colorRes
import com.mikepenz.iconics.utils.sizeDp
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class WeatherAdapter : ListAdapter<WeatherListBlock, ViewHolder>(WeatherBlocksDiffCallback()) {

    override fun getItemViewType(position: Int) = getItem(position).viewType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            VIEW_TYPE_CURRENT_TEMP -> CurrentTempViewHolder(
                binding = WeatherItemCurrentTempBinding.inflate(parent.inflater, parent, false)
            )
            VIEW_TYPE_HIGH_LOW_TEMP -> HighLowTempViewHolder(
                binding = WeatherItemHighLowTempBinding.inflate(parent.inflater, parent, false)
            )
            VIEW_TYPE_NEXT_DAY_FORECAST -> NextDayForecastViewHolder(
                binding = WeatherItemNextDayForecastBinding.inflate(parent.inflater, parent, false)
            )
            VIEW_TYPE_WIND_PRECIPITATION_HUMIDITY -> WindPrecipitationHumidityViewHolder(
                binding = WeatherItemWindPrecipitationHumidityBinding.inflate(parent.inflater, parent, false)
            )
            VIEW_TYPE_UV_SUNRISE_SUNSET -> UvSunriseSunsetViewHolder(
                binding = WeatherItemUvSunriseSunsetBinding.inflate(parent.inflater, parent, false)
            )
            else -> TempGraphViewHolder(
                binding = WeatherItemTempGraphBinding.inflate(parent.inflater, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is CurrentTempViewHolder -> holder.onBind(item as CurrentTemperature)
            is HighLowTempViewHolder -> holder.onBind(item as HighLowTemp)
            is NextDayForecastViewHolder -> holder.onBind(item as NextDayForecast)
            is WindPrecipitationHumidityViewHolder -> holder.onBind(item)
            is UvSunriseSunsetViewHolder -> holder.onBind(item)
            is TempGraphViewHolder -> holder.onBind(item as TemperatureGraph)
        }
    }

    class CurrentTempViewHolder(
        private val binding: WeatherItemCurrentTempBinding,
    ) : WeatherBlockViewHolder<WeatherItemCurrentTempBinding>(binding) {
        fun onBind(model: CurrentTemperature) = with(binding) {
            super.onBind(model)
            location.showIfNotNull(model.locationName) { name ->
                text = name
                textColorRes = model.secondaryTextColorRes
                drawableStart = iconicsDrawable(CommunityMaterial.Icon3.cmd_map_marker).apply {
                    sizeDp = 22
                    colorRes = model.secondaryTextColorRes
                }
            }
            temp.apply {
                text = getString(R.string.temperature_degrees, model.temperature)
                textColorRes = model.primaryTextColorRes
            }
        }
    }

    class HighLowTempViewHolder(
        private val binding: WeatherItemHighLowTempBinding,
    ) : WeatherBlockViewHolder<WeatherItemHighLowTempBinding>(binding) {
        fun onBind(model: HighLowTemp) = with(binding) {
            super.onBind(model)
            highTemp.text = getString(R.string.temperature_degrees, model.maxTemp)
            lowTemp.text = getString(R.string.temperature_degrees, model.minTemp)
            highLabel.textColorRes = model.secondaryTextColorRes
            lowLabel.textColorRes = model.secondaryTextColorRes
        }
    }

    class NextDayForecastViewHolder(
        private val binding: WeatherItemNextDayForecastBinding,
    ) : WeatherBlockViewHolder<WeatherItemNextDayForecastBinding>(binding) {
        fun onBind(model: NextDayForecast) = with(binding) {
            super.onBind(model)
            weekday.apply {
                text = model.weekDay
                textColorRes = model.secondaryTextColorRes
            }
            highTemp.apply {
                text = getString(R.string.temperature_degrees, model.maxTemp)
                drawableStart = iconicsDrawable(CommunityMaterial.Icon.cmd_chevron_up).apply {
                    sizeDp = 8
                }
            }
            lowTemp.apply {
                text = getString(R.string.temperature_degrees, model.minTemp)
                drawableStart = iconicsDrawable(CommunityMaterial.Icon.cmd_chevron_down).apply {
                    sizeDp = 8
                }
            }
        }
    }

    class WindPrecipitationHumidityViewHolder(
        private val binding: WeatherItemWindPrecipitationHumidityBinding,
    ) : WeatherBlockViewHolder<WeatherItemWindPrecipitationHumidityBinding>(binding) {
        override fun onBind(model: WeatherListBlock) = with(binding) {
            super.onBind(model)
            icon.drawableTintRes = model.primaryTextColorRes
            number.textColorRes = model.primaryTextColorRes
            label.textColorRes = model.secondaryTextColorRes
            when (model) {
                is WindSpeed -> {
                    icon.imageDrawable = root.iconicsDrawable(CommunityMaterial.Icon3.cmd_weather_windy)
                    number.text = model.speed.toInt().toString()
                    label.text = model.unit
                }
                is PrecipitationSum -> {
                    icon.imageDrawable = root.iconicsDrawable(WeatherIcons.Icon.wic_umbrella)
                    number.text = model.sum.toString()
                    label.text = model.unit
                }
                is HumidityPercentage -> {
                    icon.imageDrawable = root.iconicsDrawable(WeatherIcons.Icon.wic_humidity)
                    number.text = model.percentage.toString()
                    label.text = "%"
                }
                else -> {}
            }
        }
    }

    class UvSunriseSunsetViewHolder(
        private val binding: WeatherItemUvSunriseSunsetBinding,
    ) : WeatherBlockViewHolder<WeatherItemUvSunriseSunsetBinding>(binding) {
        override fun onBind(model: WeatherListBlock) = with(binding) {
            super.onBind(model)
            title.textColorRes = model.primaryTextColorRes
            number.textColorRes = model.secondaryTextColorRes
            when (model) {
                is UvIndex -> {
                    icon.imageDrawable = root.iconicsDrawable(WeatherIcons.Icon.wic_day_sunny).apply {
                        colorRes = R.color.sun_tint
                        sizeDp = 56
                    }
                    title.textRes = R.string.un_index
                    number.text = model.index.toString()
                }
                is SunriseTime -> {
                    icon.imageDrawable = root.iconicsDrawable(WeatherIcons.Icon.wic_sunrise).apply {
                        colorRes = R.color.sunrise_tint
                        sizeDp = 56
                    }
                    title.textRes = R.string.sunrise
                    number.text = model.time
                }
                is SunsetTime -> {
                    icon.imageDrawable = root.iconicsDrawable(WeatherIcons.Icon.wic_sunset).apply {
                        colorRes = R.color.sunset_tint
                        sizeDp = 56
                    }
                    title.textRes = R.string.sunset
                    number.text = model.time
                }
                else -> {}
            }
        }
    }

    class TempGraphViewHolder(
        private val binding: WeatherItemTempGraphBinding,
    ) : WeatherBlockViewHolder<WeatherItemTempGraphBinding>(binding) {
        init {
            binding.graph.apply {
                isDragEnabled = false
                description.isEnabled = false
                legend.isEnabled = false
                setDrawMarkers(false)
                setScaleEnabled(false)
                setPinchZoom(false)
                setDrawGridBackground(false)
                xAxis.setDrawLabels(true)
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                xAxis.setDrawAxisLine(false)
                axisLeft.setDrawAxisLine(false)
                axisLeft.setDrawZeroLine(false)
                axisLeft.setDrawGridLines(false)
                axisLeft.setDrawTopYLabelEntry(false)
                axisRight.setDrawLabels(false)
                axisRight.setDrawAxisLine(false)
                axisRight.setDrawZeroLine(false)
                axisRight.setDrawGridLines(false)
                axisRight.setDrawTopYLabelEntry(false)
            }
        }
        fun onBind(model: TemperatureGraph) = with(binding.graph) {
            super.onBind(model)
            val primaryColor = ContextCompat.getColor(context, model.primaryTextColorRes)
            val lineData = LineData(
                LineDataSet(model.graphData, "",).apply {
                    cubicIntensity = 1f
                    valueTextColor = primaryColor
                    setDrawCircles(false)
                    setDrawValues(true)
                    setDrawFilled(true)
                    valueFormatter = GraphTempFormatter
                    xAxis.valueFormatter = GraphDateFormatter(model.timeZone)
                    fillDrawable = ContextCompat.getDrawable(context, R.drawable.temp_graph_filling)
                }
            )
            data = lineData
            xAxis.textColor = primaryColor
            axisLeft.textColor = primaryColor
        }

        object GraphTempFormatter : ValueFormatter() {
            override fun getFormattedValue(value: Float) = value.toInt().toString()
        }
        private class GraphDateFormatter(
            timeZone: String,
        ) : ValueFormatter() {
            private val dateFormatter = SimpleDateFormat("hh a", Locale.ENGLISH).apply {
                setTimeZone(TimeZone.getTimeZone(timeZone))
            }
            override fun getAxisLabel(value: Float, axis: AxisBase?) = dateFormatter.format(value.toLong() * 1000)!!
        }
    }

    abstract class WeatherBlockViewHolder<Binding : ViewBinding>(
        private val binding: Binding,
    ) : ViewHolder(binding.root) {
        open fun onBind(model: WeatherListBlock) = with(binding.root) {
            backgroundRes = model.backgroundDrawableRes
        }
    }
}

class WeatherBlocksDiffCallback : DiffUtil.ItemCallback<WeatherListBlock>() {
    override fun areItemsTheSame(oldItem: WeatherListBlock, newItem: WeatherListBlock) =
        oldItem::class == newItem::class

    override fun areContentsTheSame(oldItem: WeatherListBlock, newItem: WeatherListBlock) =
        oldItem == newItem

}