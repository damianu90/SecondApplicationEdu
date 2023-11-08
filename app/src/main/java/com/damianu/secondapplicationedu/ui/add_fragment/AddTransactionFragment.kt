package com.damianu.secondapplicationedu.ui.add_fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.damianu.secondapplicationedu.MainActivity
import com.damianu.secondapplicationedu.MainViewModel
import com.damianu.secondapplicationedu.R
import com.damianu.secondapplicationedu.data.model.Transaction
import com.damianu.secondapplicationedu.data.model.TransactionCategory
import com.damianu.secondapplicationedu.data.model.TransactionType
import com.damianu.secondapplicationedu.databinding.FragmentAddTransactionBinding
import com.damianu.secondapplicationedu.ui.date_picker.TransactionDatePicker
import java.util.Calendar

class AddTransactionFragment : Fragment() {

    private val viewModel by viewModels<AddTransactionViewModel>()
    private val mainViewModel by activityViewModels<MainViewModel>()
    private var _binding: FragmentAddTransactionBinding? = null
    private val binding get() = _binding!!

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            (requireActivity() as MainActivity).setBottomNavVisibility(true)
            isEnabled = false
            requireActivity().onBackPressed()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddTransactionBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCurrentDate()

        handleOnBackPressed()

        val adapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            TransactionCategory.values().map { enum -> enum.name }
        )

        binding.categorySpinner.adapter = adapter

        binding.calendarImg.setOnClickListener{
            showDatePickerDialog()
        }

        binding.saveBtn.setOnClickListener{
            val trans = createTransaction()
            mainViewModel.insertTransaction(trans)
            requireActivity().onBackPressed()
        }
    }


    private fun setupCurrentDate() {
        val date = Calendar.getInstance()
        viewModel.date = date.timeInMillis
    }

    private fun handleOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
    }

    private fun showDatePickerDialog() {
        val transactionDatePicker = TransactionDatePicker { day, month, year ->

            val dayPlaceHolder = if (day < 10) "0$day" else "$day"
            binding.dayTv.text = dayPlaceHolder

            val monthPlaceHolder = if (month + 1 < 10) "0${month + 1}" else "${month + 1}"
            binding.monthTv.text = monthPlaceHolder

            binding.yearTv.text = year.toString()

            val date = Calendar.getInstance()
            date.set(year, month + 1, day)
            viewModel.date = date.timeInMillis
        }
        transactionDatePicker.show(parentFragmentManager, "DatePicker")
    }

    private fun createTransaction(): Transaction {
        val type = when(binding.typeRq.checkedRadioButtonId) {
            binding.incomeRb.id -> TransactionType.INCOME
            else -> TransactionType.OUTCOME
        }

        val category = when(binding.categorySpinner.selectedItem.toString()) {
            "FOOD" -> TransactionCategory.FOOD
            "OTHERS" -> TransactionCategory.OTHERS
            "HOUSEHOLD" -> TransactionCategory.HOUSEHOLD
            "TRANSPORTATION" -> TransactionCategory.TRANSPORTATION
            else -> TransactionCategory.OTHERS
        }

        val amount = binding.amountEt.text.toString()
        val desc = binding.descEt.text.toString()

        return Transaction(0, viewModel.date, amount.toFloat(), desc, type, category)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}