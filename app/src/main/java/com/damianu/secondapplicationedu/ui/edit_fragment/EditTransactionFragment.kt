package com.damianu.secondapplicationedu.ui.edit_fragment

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
import com.damianu.secondapplicationedu.databinding.FragmentEditTransactionBinding
import com.damianu.secondapplicationedu.ui.date_picker.TransactionDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar

class EditTransactionFragment : Fragment() {

    private val viewModel by viewModels<EditTransactionViewModel>()
    private val mainVm by activityViewModels<MainViewModel>()
    private var _binding: FragmentEditTransactionBinding? = null
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
        _binding = FragmentEditTransactionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleOnBackPressed()
        setTransactionData(mainVm.getSelectedTransaction()!!)
        setupOnClicks()

    }

    private fun setupOnClicks() {

        binding.calendarImg.setOnClickListener {
            showDatePickerDialog()
        }
        binding.deleteBtn.setOnClickListener {

            mainVm.getSelectedTransaction()?.let { trans ->
                mainVm.deleteTransactions(listOf(trans))
                mainVm.unselectTransaction()
            }
            requireActivity().onBackPressed()
        }
        binding.saveBtn.setOnClickListener {
            updateTransaction()
        }
    }

    private fun updateTransaction() {
        val updateTrans = createTransaction()
        mainVm.updateTransaction(updateTrans)
        requireActivity().onBackPressed()
    }

    private fun createTransaction(): Transaction {
        val type = when (binding.typeRq.checkedRadioButtonId) {
            binding.incomeRb.id -> TransactionType.INCOME
            else -> TransactionType.OUTCOME
        }

        val category = when (binding.categorySpinner.selectedItem.toString()) {
            "FOOD" -> TransactionCategory.FOOD
            "OTHERS" -> TransactionCategory.OTHERS
            "HOUSEHOLD" -> TransactionCategory.HOUSEHOLD
            "TRANSPORTATION" -> TransactionCategory.TRANSPORTATION
            else -> TransactionCategory.OTHERS
        }

        val amount = binding.amountEt.text.toString()
        val desc = binding.descEt.text.toString()

        return Transaction(mainVm.getSelectedTransaction()!!.uid, viewModel.date, amount.toFloat(), desc, type, category)

    }

    private fun setTransactionData(transaction: Transaction) {
        setCurrentAmount(transaction.price)
        setCurrentType(transaction.type)
        setCurrentCategory(transaction.category)
        setCurrentDate(transaction.date)
        setCurrentDescription(transaction.desc)
    }

    private fun showDatePickerDialog() {
        val newDatePicker = TransactionDatePicker { day, month, year ->
            val dayPlaceholder = if (day < 10) "0$day" else "$day"
            binding.dayTv.text = dayPlaceholder

            val monthPlaceholder = if (month + 1 < 10) "0${month + 1}" else "${month + 1}"

            binding.monthTv.text = monthPlaceholder
            binding.yearTv.text = year.toString()

            val date = Calendar.getInstance()
            date.set(year, month, day)
            viewModel.date = date.timeInMillis

        }

        newDatePicker.show(parentFragmentManager, "DatePicker")
    }

    private fun setCurrentDescription(desc: String) {
        binding.descEt.setText(desc)
    }

    private fun setCurrentDate(date: Long) {
        viewModel.date = date
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val datePlaceholder = sdf.format(date)
        val list = datePlaceholder.split("-")

        binding.dayTv.text = list[0]
        binding.monthTv.text = list[1]
        binding.yearTv.text = list[2]
    }

    private fun setCurrentCategory(category: TransactionCategory) {
        val adapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            TransactionCategory.values().map { enum -> enum.name }
        )
        binding.categorySpinner.adapter = adapter

        val position = adapter.getPosition(category.name)
        binding.categorySpinner.setSelection(position)
    }

    private fun setCurrentType(type: TransactionType) {
        val checkId = when(type) {
            TransactionType.INCOME -> binding.incomeRb.id
            TransactionType.OUTCOME -> binding.outcomeRb.id
        }
        binding.typeRq.check(checkId)
    }

    private fun setCurrentAmount(price: Float) {
        binding.amountEt.setText(price.toString())
    }

    private fun handleOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}