package com.seif.eshraqaapp.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.seif.eshraqaapp.R
import com.seif.eshraqaapp.data.sharedPreference.AppSharedPref
import com.seif.eshraqaapp.data.sharedPreference.IntroSharedPref
import com.seif.eshraqaapp.databinding.FragmentHomeBinding
import com.seif.eshraqaapp.viewmodels.HomeViewModel
import com.seif.eshraqaapp.ui.fragments.HomeFragmentDirections

import kotlinx.parcelize.Parcelize

private const val maxNumberOfWeeks: Int = 500
var sonnHashMap: HomeFragment.SonnHashMap = HomeFragment.SonnHashMap()

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
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
                "${IntroSharedPref.readPersonalInfo("Username", "")}"

        AppSharedPref.init(requireContext())

        // ackar card
        binding.azkarCardView.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_daysFragment)
        }
        // quran card
        binding.quranCardView.setOnClickListener {
            if (homeViewModel.isAppFirstTimeRun(requireContext())) {
                showQuranBottomSheetDialog()
            } else {
                findNavController().navigate(R.id.action_homeFragment_to_quranDaysFragment)
            }
        }
        // prayers card
        binding.prayerCardView.setOnClickListener {
            if (homeViewModel.isAppFirstTimeRunPrayer(requireContext())) {
                showPrayerBottomSheetDialog()
            } else {
                findNavController().navigate(R.id.action_homeFragment_to_prayerDaysFragment)
            }
        }

    }

    private fun showPrayerBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val bottomSheetView = LayoutInflater.from(requireContext()).inflate(
            R.layout.prayer_bottom_sheet_dialog,
            view?.findViewById<ConstraintLayout>(R.id.bottom_sheet_prayer)
        )
        bottomSheetView.findViewById<Button>(R.id.btn_yes_prayer).setOnClickListener {
            showQadaaBottomSheetDialog()
            bottomSheetDialog.dismiss()
        }
        bottomSheetView.findViewById<Button>(R.id.btn_no_prayer).setOnClickListener {
            writeSharedPrefPrayer()
            AppSharedPref.writePrayerOnly("prayerOnly", true)
            AppSharedPref.writePrayerAndQadaa("prayerAndQadaa", false)
            AppSharedPref.writePrayerAndSonn("prayerAndSonn", false)
            val action = HomeFragmentDirections.actionHomeFragmentToPrayerDaysFragment(sonnHashMap)
            findNavController().navigate(action)
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()

    }

    private fun showQadaaBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val bottomSheetView = LayoutInflater.from(requireContext()).inflate(
            R.layout.prayer_bottom_sheet_dialog,
            view?.findViewById<ConstraintLayout>(R.id.bottom_sheet_prayer)
        )
        bottomSheetView.findViewById<TextView>(R.id.txt_prayer_question).text =
            getString(R.string.qadaa_question1)
        bottomSheetView.findViewById<Button>(R.id.btn_yes_prayer).setOnClickListener {
            showQadaa2BottomSheetDialog() // to know how many weeks the qadaa schedule will be available
            bottomSheetDialog.dismiss()
        }
        bottomSheetView.findViewById<Button>(R.id.btn_no_prayer).setOnClickListener {
            showChooseSonnBottomSheetDialog() // go to sonn bottom sheet
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    @Parcelize
    class SonnHashMap : HashMap<String, Boolean>(), Parcelable

    private fun showChooseSonnBottomSheetDialog() {
        sonnHashMap = SonnHashMap()
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val bottomSheetView = LayoutInflater.from(requireContext()).inflate(
            R.layout.sonn_bottom_sheet_dialog,
            view?.findViewById<ConstraintLayout>(R.id.bottom_sheet_sonn)
        )
        sonnHashMap["s_doha"] = false
        sonnHashMap["s_esha"] = false
        sonnHashMap["s_fagr"] = false
        sonnHashMap["s_keyam"] = false
        sonnHashMap["s_maghreb"] = false
        sonnHashMap["s_wetr"] = false
        sonnHashMap["s_zuhr"] = false

        bottomSheetView.findViewById<CheckBox>(R.id.sont_doha_check)
            .setOnCheckedChangeListener { buttonView, isChecked ->
                sonnHashMap["s_doha"] = isChecked
            }
        bottomSheetView.findViewById<CheckBox>(R.id.sont_esha_check)
            .setOnCheckedChangeListener { buttonView, isChecked ->
                sonnHashMap["s_esha"] = isChecked
            }
        bottomSheetView.findViewById<CheckBox>(R.id.sont_fagr_check)
            .setOnCheckedChangeListener { buttonView, isChecked ->
                sonnHashMap["s_fagr"] = isChecked
            }
        bottomSheetView.findViewById<CheckBox>(R.id.sont_keyam_check)
            .setOnCheckedChangeListener { buttonView, isChecked ->
                sonnHashMap["s_keyam"] = isChecked
            }
        bottomSheetView.findViewById<CheckBox>(R.id.sont_maghreb_check)
            .setOnCheckedChangeListener { buttonView, isChecked ->
                sonnHashMap["s_maghreb"] = isChecked
            }
        bottomSheetView.findViewById<CheckBox>(R.id.sont_wetr_check)
            .setOnCheckedChangeListener { buttonView, isChecked ->
                sonnHashMap["s_wetr"] = isChecked
            }
        bottomSheetView.findViewById<CheckBox>(R.id.sont_zuhr_check)
            .setOnCheckedChangeListener { buttonView, isChecked ->
                sonnHashMap["s_zuhr"] = isChecked
            }
        bottomSheetView.findViewById<Button>(R.id.btn_save_sonn).setOnClickListener {
            /** also used in alert dialog when user can add sonn to his schedule**/
            if (sonnHashMap["s_doha"] == false &&
                sonnHashMap["s_esha"] == false &&
                sonnHashMap["s_fagr"] == false &&
                sonnHashMap["s_keyam"] == false &&
                sonnHashMap["s_maghreb"] == false &&
                sonnHashMap["s_wetr"] == false &&
                sonnHashMap["s_zuhr"] == false
            ) { // check if there is no sonn choosed
//                AppSharedPref.writePrayerOnly("prayerOnly", true )
//                AppSharedPref.writePrayerAndQadaa("prayerAndQadaa", false)
//                AppSharedPref.writePrayerAndSonn("prayerAndSonn", false)
//                sonnHashMap = SonnHashMap() // make it empty to know that the user didn't choose any sonn so the system will create prayers only schedule
//                val action = HomeFragmentDirections.actionHomeFragmentToPrayerDaysFragment(sonnHashMap)
//                findNavController().navigate(action)
                Toast.makeText(
                    requireContext(),
                    "يجب أختيار سنة واحدة كبداية !",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                writeSharedPrefPrayer()
                AppSharedPref.writePrayerOnly("prayerOnly", false)
                AppSharedPref.writePrayerAndQadaa("prayerAndQadaa", false)
                AppSharedPref.writePrayerAndSonn("prayerAndSonn", true)
                val action =
                    HomeFragmentDirections.actionHomeFragmentToPrayerDaysFragment(sonnHashMap)
                findNavController().navigate(action)
                bottomSheetDialog.dismiss()
            }
        }
        bottomSheetView.findViewById<Button>(R.id.btn_back_sonn).setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    private fun showQadaa2BottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val bottomSheetView = LayoutInflater.from(requireContext()).inflate(
            R.layout.qadaa2_bottom_sheet_dialog,
            view?.findViewById<ConstraintLayout>(R.id.bottom_sheet_qadaa2)
        )
        val numberOfWeeksEditText =
            bottomSheetView.findViewById<EditText>(R.id.et_number_weeks_qadaa2)
        bottomSheetView.findViewById<Button>(R.id.btn_save_qadaa2).setOnClickListener {
            if (numberOfWeeksEditText.text.toString().toInt() > maxNumberOfWeeks) {
                numberOfWeeksEditText.error = "أقصي عدد 500" + " !"
            } else {
                writeSharedPrefPrayer()
                AppSharedPref.writePrayerOnly("prayerOnly", false)
                AppSharedPref.writePrayerAndQadaa("prayerAndQadaa", true)
                AppSharedPref.writePrayerAndSonn("prayerAndSonn", false)
                val action =
                    HomeFragmentDirections.actionHomeFragmentToPrayerDaysFragment(sonnHashMap)
                findNavController().navigate(action)
                bottomSheetDialog.dismiss()
            }
        }
        bottomSheetView.findViewById<Button>(R.id.btn_back_qadaa2).setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()

    }

    private fun showQuranBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val bottomSheetView = LayoutInflater.from(requireContext()).inflate(
            R.layout.buttomsheet_layout,
            view?.findViewById<ConstraintLayout>(R.id.buttom_sheet)
        )
        val saveSpinner = bottomSheetView.findViewById<Spinner>(R.id.spinner_save)
        val readSpinner = bottomSheetView.findViewById<Spinner>(R.id.spinner_read)
        val revisionSpinner = bottomSheetView.findViewById<Spinner>(R.id.spinner_revision)
        var numberOfSaveDays = ""
        var numberOfReadDays = ""
        var numberOfRevisionDays = ""
        saveSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
        readSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
        revisionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
            writeSharedPrefQuran()

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

    private fun writeSharedPrefQuran() {
        shared = requireContext().getSharedPreferences("isFirstTimeQuran", Context.MODE_PRIVATE)
        pref = requireContext().getSharedPreferences("homePrefs", Context.MODE_PRIVATE)
        edit = pref.edit()
        edit.putInt("nweek", 1)
        edit.apply()
        val editor = shared.edit()
        editor.putBoolean("check", false)
        editor.apply()
    }

    private fun writeSharedPrefPrayer() {
        shared = requireContext().getSharedPreferences("isFirstTimePrayer", Context.MODE_PRIVATE)
        pref = requireContext().getSharedPreferences("prayerPrefs", Context.MODE_PRIVATE)
        edit = pref.edit()
        edit.putInt("nweekPrayer", 1)
        edit.apply()
        val editor = shared.edit()
        editor.putBoolean("checkPrayer", false)
        editor.apply()
    }

}