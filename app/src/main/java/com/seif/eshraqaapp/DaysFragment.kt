package com.seif.eshraqaapp

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import androidx.core.view.setPadding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.seif.eshraqaapp.databinding.FragmentDaysBinding
import com.seif.eshraqaapp.ui.adapters.AzkarAdapter
import com.seif.eshraqaapp.viewmodels.AzkarViewModel


class DaysFragment : Fragment() {
    lateinit var binding: FragmentDaysBinding
    private val myAdapter: AzkarAdapter by lazy { AzkarAdapter() }
    lateinit var viewModel : AzkarViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDaysBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[AzkarViewModel::class.java]
        setHasOptionsMenu(true)
        viewModel.azkar.observe(viewLifecycleOwner) {
            Log.d("day", it.toString())
            myAdapter.addAzkar(it!!)
        }
        viewModel.isAppFirstTimeRun(requireContext())

        binding.rvAzkarDays.adapter = myAdapter

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.days_menu, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_end_of_week -> showConfirmationDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showConfirmationDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.confirmation_dialog)
        val btnOk = dialog.findViewById<Button>(R.id.btn_ok)
        val btnBack = dialog.findViewById<Button>(R.id.btn_back)
        btnOk.setOnClickListener {
            // logic to start new week and save score of prev week

        }
        btnBack.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}