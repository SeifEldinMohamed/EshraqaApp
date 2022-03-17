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

import kotlinx.parcelize.Parcelize
import org.w3c.dom.Text

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
        binding.sebhaCardView.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_sebhaFragment)
        }

    }

    private fun showPrayerBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val bottomSheetView = LayoutInflater.from(requireContext()).inflate(
            R.layout.prayer_bottom_sheet_dialog,
            view?.findViewById<ConstraintLayout>(R.id.bottom_sheet_prayer)
        )
        val question = bottomSheetView.findViewById<TextView>(R.id.txt_prayer_question)
        if (IntroSharedPref.readGander("Male", false)) {
            question.text = getString(R.string.prayer_question1)
        }
        else{
            question.text = getString(R.string.prayer_question1_female)
        }
            bottomSheetView.findViewById<Button>(R.id.btn_yes_prayer).setOnClickListener {
            showQadaaBottomSheetDialog()
            bottomSheetDialog.dismiss()
        }
        bottomSheetView.findViewById<Button>(R.id.btn_no_prayer).setOnClickListener { // create only prayer schedule
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
        val sontDohaCheckBox = bottomSheetView.findViewById<CheckBox>(R.id.sont_doha_check)
        val sontEshaCheckBox = bottomSheetView.findViewById<CheckBox>(R.id.sont_esha_check)
        val sontfagrCheckBox = bottomSheetView.findViewById<CheckBox>(R.id.sont_fagr_check)
        val sontKeyamCheckBox = bottomSheetView.findViewById<CheckBox>(R.id.sont_keyam_check)
        val sontMaghrebCheckBox = bottomSheetView.findViewById<CheckBox>(R.id.sont_maghreb_check)
        val sontZuhrCheckBox = bottomSheetView.findViewById<CheckBox>(R.id.sont_zuhr_check)
        val sontWetrCheckBox = bottomSheetView.findViewById<CheckBox>(R.id.sont_wetr_check)

        val txtChooseSonn = bottomSheetView.findViewById<TextView>(R.id.txt_choose_sonn)
        if (IntroSharedPref.readGander("Male", false)) {
            txtChooseSonn.text = getString(R.string.choose_sonn_text)
        }
        else{
            txtChooseSonn.text = getString(R.string.choose_sonn_text_female)
        }


            bottomSheetView.findViewById<Button>(R.id.btn_save_sonn).setOnClickListener {
            /** also used in alert dialog when user can add sonn to his schedule**/
            if (!sontWetrCheckBox.isChecked &&
                !sontDohaCheckBox.isChecked &&
                !sontKeyamCheckBox.isChecked &&
                !sontZuhrCheckBox.isChecked &&
                !sontMaghrebCheckBox.isChecked &&
                !sontfagrCheckBox.isChecked &&
                !sontEshaCheckBox.isChecked
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

                AppSharedPref.init(requireContext())
                if (sontfagrCheckBox.isChecked) {
                    sonnHashMap["s_fagr"] = false // initializing
                    AppSharedPref.writeSontFagr("s_fagr", true)
                } else {
                    AppSharedPref.writeSontFagr("s_fagr", false)
                    sonnHashMap.remove("s_fagr")
                }
                if (sontZuhrCheckBox.isChecked) {
                    sonnHashMap["s_zuhr"] = false // initializing
                    AppSharedPref.writeSontZuhr("s_zuhr", true)
                } else {
                    AppSharedPref.writeSontZuhr("s_zuhr", false)
                    sonnHashMap.remove("s_zuhr")
                }
                if (sontMaghrebCheckBox.isChecked) {
                    sonnHashMap["s_maghreb"] = false // initializing
                    AppSharedPref.writeSontMaghreb("s_maghreb", true)
                } else {
                    AppSharedPref.writeSontMaghreb("s_maghreb", false)
                    sonnHashMap.remove("s_maghreb")
                }
                if (sontEshaCheckBox.isChecked) {
                    sonnHashMap["s_esha"] = false // initializing
                    AppSharedPref.writeSontEsha("s_esha", true)
                } else {
                    AppSharedPref.writeSontEsha("s_esha", false)
                    sonnHashMap.remove("s_esha")
                }
                if (sontWetrCheckBox.isChecked) {
                    sonnHashMap["s_wetr"] = false // initializing
                    AppSharedPref.writeSontWetr("s_wetr", true)
                } else {
                    AppSharedPref.writeSontWetr("s_wetr", false)
                    sonnHashMap.remove("s_wetr")
                }
                if (sontDohaCheckBox.isChecked) {
                    sonnHashMap["s_doha"] = false // initializing
                    AppSharedPref.writeSontDoha("s_doha", true)
                } else {
                    AppSharedPref.writeSontDoha("s_doha", false)
                    sonnHashMap.remove("s_doha")
                }
                if (sontKeyamCheckBox.isChecked) {
                    sonnHashMap["s_keyam"] = false // initializing
                    AppSharedPref.writeSontKeyam("s_keyam", true)
                } else {
                    AppSharedPref.writeSontKeyam("s_keyam", false)
                    sonnHashMap.remove("s_keyam")
                }

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
            when {
                numberOfWeeksEditText.text.toString().toInt() > maxNumberOfWeeks -> {
                    numberOfWeeksEditText.error = "أقصي عدد 500" + " !"
                }
                numberOfWeeksEditText.text.toString().toInt() <=0 -> {
                    numberOfWeeksEditText.error = " أقل عدد 1" + " !"
                }
                else -> {
                    writeSharedPrefPrayer()
                    AppSharedPref.writePrayerOnly("prayerOnly", false)
                    AppSharedPref.writePrayerAndQadaa("prayerAndQadaa", true)
                    AppSharedPref.writePrayerAndSonn("prayerAndSonn", false)

                    AppSharedPref.writeQadaaPeriod("qadaa_period", numberOfWeeksEditText.text.toString().toInt())
                    val action =
                        HomeFragmentDirections.actionHomeFragmentToPrayerDaysFragment(sonnHashMap)
                    findNavController().navigate(action)
                    bottomSheetDialog.dismiss()
                }
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

        val txtReadCounter = bottomSheetView.findViewById<TextView>(R.id.txt_read_counters)
        val txtSaveCounter = bottomSheetView.findViewById<TextView>(R.id.txt_save_counters)
        val txtRevisionCounter = bottomSheetView.findViewById<TextView>(R.id.txt_revise_counters)
        if (IntroSharedPref.readGander("Male", false)) {
            txtReadCounter.text = getString(R.string.enter_number_of_days_read)
            txtSaveCounter.text = getString(R.string.enter_number_of_days_save)
            txtRevisionCounter.text = getString(R.string.enter_number_of_days_revision)
        }
        else{
            txtReadCounter.text = getString(R.string.enter_number_of_days_read_female)
            txtSaveCounter.text = getString(R.string.enter_number_of_days_save_female)
            txtRevisionCounter.text = getString(R.string.enter_number_of_days_revision_female)

        }

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
            if (numberOfSaveDays.toInt() == 0 && numberOfReadDays.toInt() == 0 && numberOfRevisionDays.toInt() == 0){
                Toast.makeText(requireContext(), "لا يمكن أن يكونوا الثلاثة أصفارا !", Toast.LENGTH_SHORT).show()
            }
            else{ // valid input
                writeSharedPrefQuran()
                val action = HomeFragmentDirections.actionHomeFragmentToQuranDaysFragment(
                    numberOfSaveDays.toInt(),
                    numberOfReadDays.toInt(),
                    numberOfRevisionDays.toInt()
                )
                findNavController().navigate(action)
                bottomSheetDialog.dismiss()
            }
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