package com.damianu.secondapplicationedu.ui.transaction_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.damianu.secondapplicationedu.MainActivity
import com.damianu.secondapplicationedu.MainViewModel
import com.damianu.secondapplicationedu.R
import com.damianu.secondapplicationedu.databinding.FragmentTransactionBinding
import com.damianu.secondapplicationedu.ui.adapters.TransactionsAdapter

class TransactionFragment : Fragment() {


    private val viewModel by viewModels<TransactionViewModel>()
    private val mainVm by activityViewModels<MainViewModel>()
    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransactionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mainVm.getAllTransactions().observe(viewLifecycleOwner) { transactions ->
            binding.recyclerView.adapter = TransactionsAdapter(transactions) { transaction, position ->
                mainVm.selectTransaction(transaction)
                (requireActivity() as MainActivity).setBottomNavVisibility(false)
                findNavController().navigate(R.id.editTransactionFragment)
            }
        }
    }

}