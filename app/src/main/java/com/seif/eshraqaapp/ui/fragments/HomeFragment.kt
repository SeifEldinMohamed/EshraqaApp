package com.seif.eshraqaapp.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = IntroSharedPref.readPersonalInfo("Username","")
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
        val numberOfDaysEditText = bottomSheetView.findViewById<EditText>(R.id.number_days_edit)
        bottomSheetView.findViewById<Button>(R.id.btn_next).setOnClickListener {

            if(numberOfDaysEditText.text.isEmpty()){
                numberOfDaysEditText.error = "رجاء إدخخال عدد ايام العمل قبل المتابعة !"
            }
            else{
                writeSharedPref()
                val days:Int = numberOfDaysEditText.text.toString().toInt()
                val action = HomeFragmentDirections.actionHomeFragmentToQuranDaysFragment(days)
                findNavController().navigate(action)
                bottomSheetDialog.dismiss()
            }
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