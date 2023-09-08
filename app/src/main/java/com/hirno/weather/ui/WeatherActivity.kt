package com.hirno.weather.ui

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.hirno.weather.utils.iconicsDrawable
import com.hirno.weather.utils.imageDrawable
import com.hirno.weather.utils.lcm
import com.hirno.weather.utils.spanSizeLookup
import com.hirno.weather.R
import com.hirno.weather.databinding.ActivityWeatherBinding
import com.hirno.weather.model.ForecastDTO
import com.hirno.weather.model.WeatherEvent
import com.hirno.weather.model.WeatherState
import com.hirno.weather.model.UiOptionsDTO
import com.hirno.weather.model.UiOptionsDTO.Block.NEXT_DAYS_FORECAST
import com.hirno.weather.model.UiOptionsDTO.Block.UV_SUNRISE_SUNSET
import com.hirno.weather.model.UiOptionsDTO.Block.WIND_PRECIPITATION_HUMIDITY
import com.hirno.weather.model.UiOptionsDTO.RefreshStyle.FAB
import com.hirno.weather.model.UiOptionsDTO.RefreshStyle.SWIPE
import com.hirno.weather.model.WeatherListBlock.Companion.VIEW_TYPE_CURRENT_TEMP
import com.hirno.weather.model.WeatherListBlock.Companion.VIEW_TYPE_HIGH_LOW_TEMP
import com.hirno.weather.model.WeatherListBlock.Companion.VIEW_TYPE_NEXT_DAY_FORECAST
import com.hirno.weather.model.WeatherListBlock.Companion.VIEW_TYPE_UV_SUNRISE_SUNSET
import com.hirno.weather.model.WeatherListBlock.Companion.VIEW_TYPE_WIND_PRECIPITATION_HUMIDITY
import com.hirno.weather.utils.ItemOffsetDecoration
import com.hirno.weather.viewmodel.WeatherViewModel
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherActivity : AppCompatActivity() {

    private val viewModel: WeatherViewModel by viewModel()

    private lateinit var binding: ActivityWeatherBinding

    private lateinit var listAdapter: WeatherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSwipeRefresh()
        setupRefreshFab()

        setupWeatherList()

        setupState()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        viewModel.event(WeatherEvent.ScreenLoad)
    }

    private fun setupSwipeRefresh() = with(binding.swipeRefresh) {
        setOnRefreshListener {
            isRefreshing = false
            viewModel.event(WeatherEvent.Refresh)
        }
    }

    private fun setupRefreshFab() = with(binding.fabRefresh) {
        imageDrawable = iconicsDrawable(CommunityMaterial.Icon3.cmd_refresh)
        setOnClickListener {
            viewModel.event(WeatherEvent.Refresh)
        }
    }

    private fun setupWeatherList() {
        listAdapter = WeatherAdapter()
        binding.list.apply {
            layoutManager = GridLayoutManager(context, 1)
            addItemDecoration(ItemOffsetDecoration())
            adapter = listAdapter
        }
    }

    private fun setupState() {
        viewModel.obtainState.observe(this) { state ->
            when (state) {
                is WeatherState.Loading -> showLoading()
                is WeatherState.Error -> showError(state)
                is WeatherState.Success -> showWeatherInfo(state)
            }
        }
    }

    private fun showLoading() = with(binding) {
        progress.show()
        swipeRefresh.isEnabled = false
        fabRefresh.hide()
        message.isVisible = false
        list.isVisible = false
    }

    private fun showError(error: WeatherState.Error) = with(binding) {
        progress.hide()
        list.isVisible = false
        val refreshStyle = error.refreshStyle
        setRefreshStyle(refreshStyle)
        showErrorMessage(refreshStyle, error)
    }

    private fun ActivityWeatherBinding.showErrorMessage(
        refreshStyle: UiOptionsDTO.RefreshStyle,
        error: WeatherState.Error,
    ) {
        val refreshHintText = when (refreshStyle) {
            SWIPE -> R.string.swipe_to_refresh_hint
            FAB -> R.string.fab_refresh_hint
        }
        val errorText = error.text ?: getString(error.resId!!)
        val messageText = errorText + getString(refreshHintText)
        message.apply {
            text = messageText
            isVisible = true
        }
    }

    private fun showWeatherInfo(state: WeatherState.Success) = with(binding) {
        progress.hide()
        message.isVisible = false
        list.isVisible = true
        with(state.weather.ui) {
            setRefreshStyle(refreshStyle)
            setupBackground(background)
        }
        updateWeatherList(state)
    }

    private fun updateWeatherList(state: WeatherState.Success) {
        listAdapter.submitList(state.listBlocks) {
            setGridSpanSize(state.weather)
        }
    }

    private fun setGridSpanSize(weather: ForecastDTO) {
        val gridSpanCount = calculateListSpanCount(weather)
        val gridLayoutManager = binding.list.layoutManager as GridLayoutManager
        gridLayoutManager.apply {
            spanCount = gridSpanCount
            spanSizeLookup { position ->
                when (listAdapter.getItemViewType(position)) {
                    VIEW_TYPE_CURRENT_TEMP -> {
                        if (weather.ui.hasHighLowTemperatureBlock) spanCount * 2 / 3 else spanCount
                    }
                    VIEW_TYPE_NEXT_DAY_FORECAST -> spanCount / weather.nextDaysCount
                    VIEW_TYPE_HIGH_LOW_TEMP, VIEW_TYPE_WIND_PRECIPITATION_HUMIDITY,
                    VIEW_TYPE_UV_SUNRISE_SUNSET -> spanCount / 3
                    else -> spanCount
                }
            }
        }
    }

    private fun calculateListSpanCount(weather: ForecastDTO): Int {
        val columnsCount = weather.ui.blocksOrder.mapNotNull { block ->
            when (block) {
                NEXT_DAYS_FORECAST -> weather.nextDaysCount
                WIND_PRECIPITATION_HUMIDITY, UV_SUNRISE_SUNSET -> 3
                else -> null
            }
        }.toMutableSet().also {
            if (weather.ui.hasHighLowTemperatureBlock) it.add(3)
        }
        return columnsCount.takeIf { it.isNotEmpty() }?.let { lcm(it) } ?: 1
    }

    private fun setRefreshStyle(refreshStyle: UiOptionsDTO.RefreshStyle) = with(binding) {
         when (refreshStyle) {
             SWIPE -> {
                 swipeRefresh.isEnabled = true
                 fabRefresh.hide()
             }
             FAB -> {
                 swipeRefresh.isEnabled = false
                 fabRefresh.show()
             }
         }
    }

    private fun setupBackground(background: UiOptionsDTO.BackgroundDTO) {
        binding.root.background = GradientDrawable(
            background.direction.orientation,
            intArrayOf(
                ContextCompat.getColor(this, background.color.start),
                ContextCompat.getColor(this, background.color.end),
            )
        )
    }
}