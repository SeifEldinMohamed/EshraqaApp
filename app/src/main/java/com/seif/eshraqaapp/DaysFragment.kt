package com.seif.eshraqaapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
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
        viewModel.azkar.observe(viewLifecycleOwner) {
            Log.d("day", it.toString())
            myAdapter.addAzkar(it!!)
        }
        viewModel.isAppFirstTimeRun(requireContext())
        binding.rvAzkarDays.adapter = myAdapter

    }
}