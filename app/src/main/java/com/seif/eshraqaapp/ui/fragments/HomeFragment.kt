package com.seif.eshraqaapp.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.seif.eshraqaapp.R
import com.seif.eshraqaapp.data.sharedPreference.IntroSharedPref
import com.seif.eshraqaapp.databinding.FragmentHomeBinding
import com.seif.eshraqaapp.viewmodels.HomeViewModel

class HomeFragment : Fragment() {
lateinit var binding : FragmentHomeBinding
lateinit var homeViewModel: HomeViewModel
    private lateinit var shared: SharedPreferences
    // private lateinit var  scoreList:List<Int>
    private lateinit var pref: SharedPreferences
    private lateinit var edit: SharedPreferences.Editor
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "مرحبا " +
                "${IntroSharedPref.readPersonalInfo("Username","")}"
        binding.azkarCardView.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_daysFragment)
        }
        binding.quranCardView.setOnClickListener {
            if(homeViewModel.isAppFirstTimeRun(requireContext())){
                showBottomSheetDialog()
            }
            else{
                findNavController().navigate(R.id.action_homeFragment_to_quranDaysFragment)
            }
        }
    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val bottomSheetView = LayoutInflater.from(requireContext()).inflate(
            R.layout.buttomsheet_layout,
            view?.findViewById<ConstraintLayout>(R.id.buttom_sheet)
        )
        val saveSpinner = bottomSheetView.findViewById<Spinner>(R.id.spinner_save)
        val readSpinner = bottomSheetView.findViewById<Spinner>(R.id.spinner_read)
        val revisionSpinner = bottomSheetView.findViewById<Spinner>(R.id.spinner_revision)
        var numberOfSaveDays = ""
        var numberOfReadDays  = ""
        var numberOfRevisionDays  = ""
        saveSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
               numberOfSaveDays = parent?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        readSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                numberOfReadDays = parent?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        revisionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                numberOfRevisionDays = parent?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        bottomSheetView.findViewById<Button>(R.id.btn_save_counters).setOnClickListener {
                writeSharedPref()

                val action = HomeFragmentDirections.actionHomeFragmentToQuranDaysFragment(
                    numberOfSaveDays.toInt(),
                    numberOfReadDays.toInt(),
                    numberOfRevisionDays.toInt()
                )
                findNavController().navigate(action)
                bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    private fun writeSharedPref() {
        shared = requireContext().getSharedPreferences("isFirstTimeQuran", Context.MODE_PRIVATE)
        pref = requireContext().getSharedPreferences("homePrefs", Context.MODE_PRIVATE)
        edit = pref.edit()
        edit.putInt("nweek",1)
        edit.apply()
        val editor = shared.edit()
        editor.putBoolean("check", false)
        editor.apply()
    }
}