package com.example.minipromoter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.core.cartesian.series.Column
import com.anychart.enums.Anchor
import com.anychart.enums.HoverMode
import com.anychart.enums.Position
import com.anychart.enums.TooltipPositionMode
import com.example.minipromoter.databinding.AnalyticsFragmentBinding
import com.example.minipromoter.viewmodels.AnalyticsViewModel

class Analytics : Fragment() {

    private lateinit var binding: AnalyticsFragmentBinding

    private val viewModel: AnalyticsViewModel by lazy {
        ViewModelProvider(
            this,
            AnalyticsViewModel.Factory()
        ).get(AnalyticsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AnalyticsFragmentBinding.inflate(inflater)

        observeVariables()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
    }

    private fun observeVariables() {

        viewModel.todayMostActiveProduct.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            todayMostActiveProducts(it)

        })

        viewModel.todayMostActiveCampaign.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            todayMostActiveCampaign(it)
        })

    }

    private fun todayMostActiveProducts(list: List<ValueDataEntry>?) {

        val anyChartView: AnyChartView = binding.anyChartView

        val cartesian: Cartesian = AnyChart.column()

        val column: Column = cartesian.column(list)

        column.tooltip()
            .titleFormat("{%X}")
            .position(Position.CENTER_BOTTOM)
            .anchor(Anchor.CENTER_BOTTOM)
            .offsetX(0.0)
            .offsetY(5.0)
            .format("{%Value}{groupsSeparator: }")

        cartesian.animation(true)

        cartesian.yScale().minimum(0.0)

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }")

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.interactivity().hoverMode(HoverMode.BY_X)

        cartesian.xAxis(0).title("Product")
        cartesian.yAxis(0).title("Messages")

        anyChartView.setChart(cartesian)


    }

    private fun todayMostActiveCampaign(list: List<ValueDataEntry>?) {

        val anyChartView: AnyChartView = binding.anyChartView2

        val cartesian: Cartesian = AnyChart.column()

        val column: Column = cartesian.column(list)

        column.tooltip()
            .titleFormat("{%X}")
            .position(Position.CENTER_BOTTOM)
            .anchor(Anchor.CENTER_BOTTOM)
            .offsetX(0.0)
            .offsetY(5.0)
            .format("{%Value}{groupsSeparator: }")

        cartesian.animation(true)

        cartesian.yScale().minimum(0.0)

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }")

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.interactivity().hoverMode(HoverMode.BY_X)

        cartesian.xAxis(0).title("Product")
        cartesian.yAxis(0).title("Messages")

        anyChartView.setChart(cartesian)


    }


}