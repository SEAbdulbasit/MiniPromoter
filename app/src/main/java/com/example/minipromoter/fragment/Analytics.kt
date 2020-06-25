package com.example.minipromoter.fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.anychart.APIlib
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.core.cartesian.series.Column
import com.anychart.enums.Anchor
import com.anychart.enums.HoverMode
import com.anychart.enums.Position
import com.anychart.enums.TooltipPositionMode
import com.example.minipromoter.R
import com.example.minipromoter.databinding.AnalyticsFragmentBinding
import com.example.minipromoter.viewmodels.AnalyticsViewModel

class Analytics : AppCompatActivity() {

    private lateinit var binding: AnalyticsFragmentBinding

    private val viewModel: AnalyticsViewModel by lazy {
        ViewModelProvider(
            this,
            AnalyticsViewModel.Factory()
        ).get(AnalyticsViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.analytics_fragment)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        observeVariables()


    }

    private fun observeVariables() {

        viewModel.todayMostActiveProduct.observe(this, androidx.lifecycle.Observer {
            todayMostActiveProducts(it)

        })

        viewModel.todayMostActiveCampaign.observe(this, androidx.lifecycle.Observer {
            todayMostActiveCampaign(it)
        })

    }

    private fun todayMostActiveProducts(list: List<ValueDataEntry>?) {


        val cartesian: Cartesian = AnyChart.column()

        val column: Column = cartesian.column(list)

        column.tooltip()
            .titleFormat("{%X}")
            .position(Position.CENTER_BOTTOM)
            .anchor(Anchor.CENTER_BOTTOM)
            .offsetX(0.0)
            .offsetY(100.0)
            .format("{%Value}{groupsSeparator: }")

        cartesian.animation(true)

        cartesian.yScale().minimum(0.0)

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }")

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.interactivity().hoverMode(HoverMode.BY_X)

        cartesian.xAxis(0).title("Product")
        cartesian.yAxis(0).title("Messages")

        binding.anyChartView1.setChart(cartesian)

    }

    private fun todayMostActiveCampaign(list: List<ValueDataEntry>?) {

        val cartesian: Cartesian = AnyChart.column()

        val column: Column = cartesian.column(list)

        column.tooltip()
            .titleFormat("{%X}")
            .position(Position.CENTER_BOTTOM)
            .anchor(Anchor.CENTER_BOTTOM)
            .offsetX(0.0)
            .offsetY(100.0)
            .format("{%Value}{groupsSeparator: }")

        cartesian.animation(true)

        cartesian.yScale().minimum(0.0)

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }")

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.interactivity().hoverMode(HoverMode.BY_X)

        cartesian.xAxis(0).title("Campaign")
        cartesian.yAxis(0).title("Messages")

        binding.anyChartView2.setChart(cartesian)


    }


}