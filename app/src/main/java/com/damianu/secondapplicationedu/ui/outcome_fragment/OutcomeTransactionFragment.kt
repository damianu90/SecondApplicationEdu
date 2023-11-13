package com.damianu.secondapplicationedu.ui.outcome_fragment

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.damianu.secondapplicationedu.MainViewModel
import com.damianu.secondapplicationedu.R
import com.damianu.secondapplicationedu.databinding.FragmentOutcomeTransactionBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

class OutcomeTransactionFragment : Fragment() {

    private val viewModel by viewModels<OutcomeTransactionViewModel>()
    private val mainVm by activityViewModels<MainViewModel>()
    private var _binding: FragmentOutcomeTransactionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOutcomeTransactionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.outcomePieChart.apply {
            isDrawHoleEnabled = true
            setUsePercentValues(true)
            setEntryLabelTextSize(18f)
            setEntryLabelColor(Color.WHITE)
            centerText = "Wydatki"
            setCenterTextSize(24f)
            description.isEnabled = false
            setTransparentCircleAlpha(50)

            setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    binding.outcomePieChart.centerText = e?.y.toString() + "PLN"
                    binding.outcomePieChart.invalidate()
                }

                override fun onNothingSelected() {
                    binding.outcomePieChart.centerText = "Wydatki"
                    binding.outcomePieChart.invalidate()
                }

            })
        }

        mainVm.getSumOfOutcomeByCategory().observe(viewLifecycleOwner) { transactions ->

            val entries = ArrayList<PieEntry>()
            for (trans in transactions) {
                val pieEntry = PieEntry(trans.total, trans.category.name.lowercase())
                entries.add(pieEntry)
            }

            val pieDateSet = PieDataSet(entries, "")
            val colors = listOf(
                Color.parseColor("#9038ff"),
                Color.parseColor("#45197d"),
                Color.parseColor("#E536AB"),
                Color.parseColor("#5C03BC")
            )
            pieDateSet.colors = colors

            val pieData = PieData(pieDateSet)
            pieData.setDrawValues(true)
            pieData.setValueFormatter(PercentFormatter(binding.outcomePieChart))
            pieData.setValueTextSize(12f)
            pieData.setValueTextColor(Color.WHITE)

            binding.outcomePieChart.legend.isEnabled = false
            binding.outcomePieChart.data = pieData
            binding.outcomePieChart.animateY(500, Easing.EaseInOutQuad)

            binding.outcomePieChart.invalidate()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}