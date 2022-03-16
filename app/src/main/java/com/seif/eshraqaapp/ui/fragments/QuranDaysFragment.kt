package com.seif.eshraqaapp.ui.fragments

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.seif.eshraqaapp.R
import com.seif.eshraqaapp.data.models.Quran
import com.seif.eshraqaapp.data.sharedPreference.IntroSharedPref
import com.seif.eshraqaapp.ui.adapters.QuranAdapter
import com.seif.eshraqaapp.viewmodels.QuranViewModel
import com.seif.eshraqaapp.ui.fragments.QuranDaysFragmentArgs.fromBundle
import com.seif.eshraqaapp.databinding.FragmentQuranDaysBinding
import jp.wasabeef.recyclerview.animators.ScaleInTopAnimator


class QuranDaysFragment : Fragment() {
    lateinit var binding: FragmentQuranDaysBinding
    private val myAdapter: QuranAdapter by lazy { QuranAdapter() }
    private lateinit var quranViewModel: QuranViewModel

    private var numberOfQuran = 0
    private var totalWeekScore = 0
    lateinit var lastQuranDay: Quran
    private lateinit var pref: SharedPreferences
    private lateinit var edit: SharedPreferences.Editor
    private var numberOfWeeks: Int = 1
    private var currentQuranHashMap = HashMap<String, String>()
    lateinit var afterMonthDialog: Dialog
    private var weeklyMessage: String = ""
    private var vacationDaysNumber: Int = 0

    private var numberOfSaveDays: Int = 0
    private var numberOfReadDays: Int = 0
    private var numberOfRevisionDays: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentQuranDaysBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        quranViewModel = ViewModelProvider(requireActivity())[QuranViewModel::class.java]

        setHasOptionsMenu(true)

        if (quranViewModel.isAppFirstTimeRun(requireContext())) {
            quranViewModel.addQuran(
                quranViewModel.getSevenDaysData(
                    fromBundle(requireArguments()).numberOfSaveDays,
                    fromBundle(requireArguments()).numberOfReadDays,
                    fromBundle(requireArguments()).numberOfRevisionDays
                )
            )
            Log.d("quran", "${fromBundle(requireArguments()).numberOfSaveDays}")
            Log.d("quran", "${fromBundle(requireArguments()).numberOfReadDays}")
            Log.d("quran", "${fromBundle(requireArguments()).numberOfRevisionDays}")
        }
        quranViewModel.quran.observe(viewLifecycleOwner, Observer { it ->
            myAdapter.addQuran(it)
            if (it.isNotEmpty()) {
                //  numberOfQuran = it[0].quran.size
                currentQuranHashMap = it[0].quran
                if (it.size == 7)
                    lastQuranDay = it[6]

                numberOfSaveDays = it[0].numberOfSaveDaysToWork
                numberOfReadDays = it[0].numberOfReadDaysToWork
                numberOfRevisionDays = it[0].numberOfRevisionDaysToWork
            }
        })
        quranViewModel.getAllQuranWeekScore().observe(viewLifecycleOwner, Observer {
            totalWeekScore = quranViewModel.getSumOfQuranWeekScore(it)
        })
        quranViewModel.vacationDaysNumber.observe(viewLifecycleOwner, Observer {
            vacationDaysNumber = it ?: 0
        })

        quranViewModel.isAppFirstTimeRun(requireContext())
        binding.rvQuranDays.adapter = myAdapter
        binding.rvQuranDays.itemAnimator = ScaleInTopAnimator().apply {
            addDuration = 200
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.days_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_end_of_week -> {
                // increase number of week by one when user create new week
                pref = requireContext().getSharedPreferences("quranPrefs", Context.MODE_PRIVATE)
                edit = pref.edit()
                numberOfWeeks = pref.getInt("nweek", 1)
                if (numberOfWeeks == 4) {
                    showConfirmationDialog(true)
                } else {
                    showConfirmationDialog(false)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showConfirmationDialog(isEndOfMonth: Boolean) {
        if (isEndOfMonth) {
            edit.putInt("nweek", 1)
            edit.apply()
        } else {
            edit.putInt("nweek", numberOfWeeks + 1)
            edit.apply()
        }
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.confirmation_dialog)
        val btnOk = dialog.findViewById<Button>(R.id.btn_ok)
        val btnBack = dialog.findViewById<Button>(R.id.btn_back_counters)
        btnOk.setOnClickListener { //////////
            // logic to start new week and save score of prev week
            val scoreWeekPercentage = calculateScore()
            showDialogAccordingToPercentage(scoreWeekPercentage, isEndOfMonth)
            dialog.dismiss()
        }
        btnBack.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showDialogAccordingToPercentage(scoreWeekPercentage: Int, isEndOfMonth: Boolean) {
        val image: Int
        val addOrDeleteMessage : String
        when (scoreWeekPercentage) {
            in 80..100 -> {
                 if (IntroSharedPref.readGander("Male", false)) {
                     image = R.drawable.gheth_happy
                     addOrDeleteMessage = getString(R.string.updateWorkCounter)
                } else {
                     image =  R.drawable.zahra_happy
                     addOrDeleteMessage = getString(R.string.updateWorkCounter_female)

                 }
                weeklyMessage = generateRandomSuccessMessageQuran()
                if (isEndOfMonth) {
                    edit.putLong("totalScoreQuran", 0L)
                    edit.putLong("totalNumberQuran", 0L)
                    edit.apply()
                    showEndMonthCongratulationMessage(
                        addOrDeleteMessage,
                        weeklyMessage,
                        image,
                    )
                } else {
                    showNormalCongratulationMessage(
                        weeklyMessage,
                        image,
                    )
                }
                Log.d("days", "success")
            }
            in 65..79 -> { // handle adding
                 if (IntroSharedPref.readGander("Male", false)) {
                      image = R.drawable.gheth_normal
                      addOrDeleteMessage = getString(R.string.updateWorkCounter)

                } else { // female
                     image = R.drawable.zahra_normal
                     addOrDeleteMessage = getString(R.string.updateWorkCounter_female)
                }
                weeklyMessage = generateRandomMediumMessageQuran()
                if (isEndOfMonth) {
                    edit.putLong("totalScoreQuran", 0L)
                    edit.putLong("totalNumberQuran", 0L)
                    edit.apply()
                    showEndMonthCongratulationMessage(
                        addOrDeleteMessage,
                        weeklyMessage,
                        image,
                    )
                } else {
                    showNormalCongratulationMessage(
                        weeklyMessage,
                        image,
                    )
                }

                Log.d("days", "medium")
            }
            in 0..64 -> {
                 if (IntroSharedPref.readGander("Male", false)) {
                     image = R.drawable.gheth_sad
                     addOrDeleteMessage = getString(R.string.updateWorkCounter)

                 } else {
                     image = R.drawable.zahra_sad
                     addOrDeleteMessage = getString(R.string.updateWorkCounter)

                 }
                weeklyMessage = generateRandomFailMessageQuran()
                if (isEndOfMonth) {
                    edit.putLong("totalScoreQuran", 0L)
                    edit.putLong("totalNumberQuran", 0L)
                    edit.apply()
                    showEndMonthCongratulationMessage(
                        addOrDeleteMessage,
                        weeklyMessage,
                        image,
                    )
                } else {
                    showNormalCongratulationMessage(
                        weeklyMessage,
                        image,
                    )
                }
                Log.d("days", "fail")
            }
        }
    }

    private fun calculateScore(): Int {
        numberOfQuran =
            numberOfSaveDays + numberOfReadDays + numberOfRevisionDays  // ex: 5+5+2 = 12
        Log.d("day", "vacationDays = $vacationDaysNumber")

        var totalValueOfWeek = numberOfQuran - (vacationDaysNumber * 3)  // ex: 12
        if (totalValueOfWeek <= 0)
            totalValueOfWeek = 1

        if (totalWeekScore > totalValueOfWeek && vacationDaysNumber != 7) // to keep percentage in range of 0 to 100
            totalWeekScore = totalValueOfWeek
        else if (vacationDaysNumber == 7) {
            totalWeekScore = totalValueOfWeek
        }

        Log.d("day", "totalValueOfWeek = $totalValueOfWeek")
        var previousTotalNumberQuran = pref.getLong("totalNumberQuran", 0L)
        Log.d("day", "previousTotalNumberQuran Old = $previousTotalNumberQuran")

        edit.putLong("totalNumberQuran", (previousTotalNumberQuran + totalValueOfWeek))
        edit.apply()

        previousTotalNumberQuran += totalValueOfWeek
        Log.d("day", "previousTotalNumberQuran new = $previousTotalNumberQuran")

        var previousTotalScore = pref.getLong("totalScoreQuran", 0L)
        Log.d("day", "previousTotalScore old = $previousTotalScore")

        edit.putLong("totalScoreQuran", (previousTotalScore + totalWeekScore))
        edit.apply()



        previousTotalScore += totalWeekScore
        Log.d("day", "previousTotalScore new = $previousTotalScore")

        Log.d(
            "day",
            "percentage = ${(previousTotalScore.toDouble() / previousTotalNumberQuran.toDouble()) * 100} %"
        )
        return ((previousTotalScore.toDouble() / previousTotalNumberQuran.toDouble()) * 100).toInt()
    }

    // show message after end of week but not end of month
    private fun showNormalCongratulationMessage(
        message: String,
        image: Int,
    ) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.zahra_message_dialog)
        val btnOk = dialog.findViewById<Button>(R.id.btn_ok_message)
        val txtMessage = dialog.findViewById<TextView>(R.id.txt_message)
        val characterImage = dialog.findViewById<ImageView>(R.id.characterImage)
        val frameImage = dialog.findViewById<ImageView>(R.id.img_frame_message)
        //val txtScore = dialog.findViewById<TextView>(R.id.txt_score_percentage_normal)
        //txtScore.text = "$scorePercentage %"
        if (IntroSharedPref.readGander("Male", false)) {
            frameImage.setImageResource(R.drawable.gheth_frame_dialog)
            btnOk.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.darkBlue))
        } else {
            frameImage.setImageResource(R.drawable.zahra_frame_dialog)
            btnOk.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.pink))
        }
        characterImage.setImageResource(image)
        txtMessage.text = message
        quranViewModel.addQuran(
            quranViewModel.createNewWeekSchedule(
                lastQuranDay,
                currentQuranHashMap,
                weeklyMessage,
                numberOfSaveDays,
                numberOfReadDays,
                numberOfRevisionDays
            )
        )
        btnOk.setOnClickListener {
            // logic to start new week and save score of prev week
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showEndMonthCongratulationMessage(
        addOrDeleteMessage: String,
        message: String,
        image: Int,
    ) {

        afterMonthDialog = Dialog(requireContext())
        afterMonthDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        afterMonthDialog.setContentView(R.layout.zahra_message_end_of_month)
        val btnYes = afterMonthDialog.findViewById<Button>(R.id.btn_ok_message_end_of_month)
        val btnNo = afterMonthDialog.findViewById<Button>(R.id.btn_no)
        val txtMessage = afterMonthDialog.findViewById<TextView>(R.id.txt_message_end_of_month)
        val txtMessageAddOrDelete =
            afterMonthDialog.findViewById<TextView>(R.id.txt_add_or_delete_message)
        val characterImage =
            afterMonthDialog.findViewById<ImageView>(R.id.character_image_end_of_month)
        characterImage.setImageResource(image)

        val frameImage = afterMonthDialog.findViewById<ImageView>(R.id.img_frame_message)
       // val txtScore = afterMonthDialog.findViewById<TextView>(R.id.txt_score_percentage_end_month)
       // txtScore.text = "$scorePercentage %"
        if (IntroSharedPref.readGander("Male", false)) {
            frameImage.setImageResource(R.drawable.gheth_frame_dialog)
            btnYes.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.darkBlue))
            btnNo.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.darkBlue))
        } else {
            frameImage.setImageResource(R.drawable.zahra_frame_dialog)
            btnYes.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.pink))
            btnNo.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.pink))
        }
        // change character and frame according to male of user
        txtMessage.text = message
        txtMessageAddOrDelete.text = addOrDeleteMessage
        btnYes.setOnClickListener {
            // logic to start new week and save score of prev week
             showUpdateQuranCountersDialog()
        }
        btnNo.setOnClickListener {
            quranViewModel.addQuran(
                quranViewModel.createNewWeekSchedule(
                    lastQuranDay,
                    currentQuranHashMap,
                    weeklyMessage,
                    numberOfSaveDays,
                    numberOfReadDays,
                    numberOfRevisionDays
                )
            )
            afterMonthDialog.dismiss()
        }
        afterMonthDialog.show()
    }

    private fun showUpdateQuranCountersDialog() {

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.update_counters_dialog)
        val btnSave = dialog.findViewById<Button>(R.id.btn_save_counters)
        val btnBack = dialog.findViewById<Button>(R.id.btn_back_counters)

        val saveSpinner = dialog.findViewById<Spinner>(R.id.spinner_save_counterr)
        saveSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                numberOfSaveDays = parent?.getItemAtPosition(position).toString().toInt()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        val readSpinner = dialog.findViewById<Spinner>(R.id.spinner_read_counter)
        readSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                numberOfReadDays = parent?.getItemAtPosition(position).toString().toInt()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        val revisionSpinner = dialog.findViewById<Spinner>(R.id.spinner_revision_counter)
        revisionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                numberOfRevisionDays = parent?.getItemAtPosition(position).toString().toInt()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        btnSave.setOnClickListener {
            // logic to start new week and save score of prev week
            quranViewModel.addQuran(
                quranViewModel.createNewWeekSchedule(
                    lastQuranDay,
                    currentQuranHashMap,
                    weeklyMessage,
                    numberOfSaveDays,
                    numberOfReadDays,
                    numberOfRevisionDays
                )
            )
            dialog.dismiss()
            afterMonthDialog.dismiss()
        }
        btnBack.setOnClickListener {
            dialog.dismiss()
            afterMonthDialog.dismiss()
        }
        dialog.show()
    }

    fun generateRandomSuccessMessageQuran(): String {
        val strings = ArrayList<String>()
        if (IntroSharedPref.readGander("Male", false)) {
            strings.add(getString(R.string.success_message1_quran))
            strings.add(getString(R.string.success_message2_quran))
            strings.add(getString(R.string.success_message3_quran))
        }
        else{ // female
            strings.add(getString(R.string.success_message1_quran_female))
            strings.add(getString(R.string.success_message2_quran_female))
            strings.add(getString(R.string.success_message3_quran_female))
        }

        return strings.random()
    }

    private fun generateRandomMediumMessageQuran(): String {
        val strings = ArrayList<String>()
        if (IntroSharedPref.readGander("Male", false)) {
            strings.add(getString(R.string.medium_message1_quran))
            strings.add(getString(R.string.medium_message2_quran))
            strings.add(getString(R.string.medium_message3_quran))
        }
        else{ // female
            strings.add(getString(R.string.medium_message1_quran_female))
            strings.add(getString(R.string.medium_message2_quran_female))
            strings.add(getString(R.string.medium_message3_quran_female))
        }


        return strings.random()
    }

    private fun generateRandomFailMessageQuran(): String {
        val strings = ArrayList<String>()
        if (IntroSharedPref.readGander("Male", false)) {
            strings.add(getString(R.string.fail_message1_quran))
            strings.add(getString(R.string.fail_message2_quran))
            strings.add(getString(R.string.fail_message3_quran))
        }
        else{
            strings.add(getString(R.string.fail_message1_quran_female))
            strings.add(getString(R.string.fail_message2_quran_female))
            strings.add(getString(R.string.fail_message3_quran_female))
        }

        return strings.random()
    }
}