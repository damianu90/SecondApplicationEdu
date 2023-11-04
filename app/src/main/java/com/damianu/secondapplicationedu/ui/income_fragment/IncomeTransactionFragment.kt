package com.damianu.secondapplicationedu.ui.income_fragment

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.damianu.secondapplicationedu.MainViewModel
import com.damianu.secondapplicationedu.R
import com.damianu.secondapplicationedu.databinding.FragmentIncomeTransactionBinding
import com.damianu.secondapplicationedu.databinding.FragmentTransactionBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

class IncomeTransactionFragment : Fragment() {

    private val viewModel by viewModels<IncomeTransactionViewModel>()
    private val mainVm by activityViewModels<MainViewModel>()
    private var _binding: FragmentIncomeTransactionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIncomeTransactionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.incomePieChart.apply {
            isDrawHoleEnabled = true
            setUsePercentValues(true)
            setEntryLabelTextSize(18f)
            setEntryLabelColor(Color.WHITE)
            centerText = "Przychody"
            setCenterTextSize(24f)
            description.isEnabled = false
            setTransparentCircleAlpha(50)

            setOnChartValueSelectedListener(object: OnChartValueSelectedListener{
                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    binding.incomePieChart.centerText = e?.y.toString() + "PLN"
                    binding.incomePieChart.invalidate()
                }

                override fun onNothingSelected() {
                    binding.incomePieChart.centerText = "Przychody"
                    binding.incomePieChart.invalidate()
                }

            })
        }

        mainVm.getSumOfIncomeByCategory().observe(viewLifecycleOwner) {transactions ->
            Log.d("test", "Wykonalo sie")
            val entries = ArrayList<PieEntry>()
            for (trans in transactions) {
                Log.d("test", "Total: ${trans.total}, ${trans.category.name}")
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
            pieData.apply {
                setDrawValues(true)
                setValueFormatter(PercentFormatter(binding.incomePieChart))
                setValueTextSize(12f)
                setValueTextColor(Color.WHITE)

                binding.incomePieChart.legend.isEnabled = false
                binding.incomePieChart.data = pieData
                binding.incomePieChart.animateY(500, Easing.EaseInOutQuad)

                binding.incomePieChart.invalidate()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}