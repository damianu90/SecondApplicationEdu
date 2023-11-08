package com.damianu.secondapplicationedu

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.damianu.secondapplicationedu.data.model.Transaction
import com.damianu.secondapplicationedu.data.model.TransactionCategory
import com.damianu.secondapplicationedu.data.model.TransactionType
import com.damianu.secondapplicationedu.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val mainVm by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBottomNavVisibility(mainVm.isBottomNavVisible)

        val navHostFragment = supportFragmentManager.findFragmentById(binding.fragmentContainerView.id) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)


        binding.addTransactionFb.setOnClickListener {
            setBottomNavVisibility(false)
            navController.navigate(R.id.addTransactionFragment)
        }
        //mainVm.insertTransaction(createTransaction())
    }

    fun setBottomNavVisibility(bottomNavVisible: Boolean) {
        mainVm.isBottomNavVisible = bottomNavVisible

        val isVisible = when (bottomNavVisible) {
            true -> View.VISIBLE
            false -> View.INVISIBLE
        }

        binding.cardView.visibility = isVisible
        binding.addTransactionFb.visibility = isVisible
    }

    private fun createTransaction() =
        Transaction(
            0,
            1L,
            15f,
            "Opis",
            TransactionType.INCOME,
            TransactionCategory.HOUSEHOLD
        )
}
