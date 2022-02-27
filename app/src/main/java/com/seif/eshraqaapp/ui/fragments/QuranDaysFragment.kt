package com.seif.eshraqaapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.seif.eshraqaapp.databinding.FragmentQuranDaysBinding
import com.seif.eshraqaapp.ui.adapters.QuranAdapter
import com.seif.eshraqaapp.viewmodels.QuranViewModel
import com.seif.eshraqaapp.ui.fragments.QuranDaysFragmentArgs.fromBundle



class QuranDaysFragment : Fragment() {
    lateinit var binding :FragmentQuranDaysBinding
    private val myAdapter: QuranAdapter by lazy { QuranAdapter() }
    private lateinit var quranViewModel: QuranViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentQuranDaysBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        quranViewModel = ViewModelProvider(requireActivity())[QuranViewModel::class.java]

        if(quranViewModel.isAppFirstTimeRun(requireContext())){
            quranViewModel.addQuran(quranViewModel.getSevenDaysData(
                fromBundle(requireArguments()).numberOfWorkDays
            ))
        }
        quranViewModel.quran.observe(viewLifecycleOwner, Observer {
            myAdapter.addQuran(it)
        })
        binding.rvQuranDays.adapter = myAdapter

    }
}