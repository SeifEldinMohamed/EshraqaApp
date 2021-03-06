package com.seif.eshraqh.ui.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.seif.eshraqh.viewmodels.AzkarViewModel
import com.seif.eshraqh.R
import com.seif.eshraqh.data.models.Azkar
import com.seif.eshraqh.data.sharedPreference.IntroSharedPref
import com.seif.eshraqh.databinding.FragmentDaysBinding
import com.seif.eshraqh.ui.adapters.AzkarAdapter
import jp.wasabeef.recyclerview.animators.ScaleInTopAnimator


class AzkarDaysFragment : Fragment() {
    lateinit var binding: FragmentDaysBinding
    private val myAdapter: AzkarAdapter by lazy { AzkarAdapter() }
    lateinit var viewModel: AzkarViewModel
    private var numberOfAzkar = 0
    private var totalWeekScore = 0
    lateinit var lastAzkarDay: Azkar
    private lateinit var pref: SharedPreferences
    private lateinit var edit: SharedPreferences.Editor
    private var numberOfWeeks: Int = 1
    private var currentAzkarHashMap = HashMap<String, Boolean>()
    lateinit var afterMonthDialog: Dialog
    private var weeklyMessage: String = ""

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
            if (it.isNotEmpty()) {
                Log.d("day", it.size.toString())
                numberOfAzkar = it[0].azkar.size
                currentAzkarHashMap = it[0].azkar
            }
            if (it.size == 7) {  // to avoid drop down of recycler view items
                lastAzkarDay = it[6]
                myAdapter.addAzkar(it)
            }
        }
        viewModel.getAllWeekScore().observe(viewLifecycleOwner) {
            totalWeekScore = viewModel.getSumOfWeekScore(it)
        }
        viewModel.isAppFirstTimeRun(requireContext())

        binding.rvAzkarDays.adapter = myAdapter
        binding.rvAzkarDays.itemAnimator = ScaleInTopAnimator().apply {
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
                pref = requireContext().getSharedPreferences("settingPrefs", Context.MODE_PRIVATE)
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
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.confirmation_dialog)
        val btnOk = dialog.findViewById<Button>(R.id.btn_ok)
        val btnBack = dialog.findViewById<Button>(R.id.btn_back_counters)

        val txtNewWeek = dialog.findViewById<TextView>(R.id.txt_new_week)
        if (IntroSharedPref.readGander("Male", false)) {
            txtNewWeek.text = getString(R.string.end_of_week_confirmation)
        } else {
            txtNewWeek.text = getString(R.string.end_of_week_confirmation_female)
        }

        btnOk.setOnClickListener {
            // logic to start new week and save score of prev week
            val scoreWeekPercentage: Int
            if (isEndOfMonth) {
                scoreWeekPercentage = calculateScore()
                Log.d("azkar", scoreWeekPercentage.toString())
            } else { // normal weeks
                scoreWeekPercentage = calculateNoramlWeekScore()
                Log.d("azkar", scoreWeekPercentage.toString())
            }
            showDialogAccordingToPercentage(scoreWeekPercentage, isEndOfMonth)
            dialog.dismiss()
        }
        btnBack.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun calculateNoramlWeekScore(): Int {
        Log.d("days", "number of azkar $numberOfAzkar")
        val totalValueOfWeek = numberOfAzkar * 7

        var previousTotalNumberAzkar = pref.getLong("totalNumberAzkar", 0L)
        edit.putLong("totalNumberAzkar", (previousTotalNumberAzkar + totalValueOfWeek))
        Log.d(
            "days", "previousTotalNumberAzkar: $previousTotalNumberAzkar" +
                    "+ totalValueOfWeek $totalValueOfWeek"
        )
        previousTotalNumberAzkar += totalValueOfWeek
        var previousTotalScore = pref.getLong("totalScore", 0L)
        edit.putLong("totalScore", (previousTotalScore + totalWeekScore))
        Log.d(
            "days", "previousTotalScore: $previousTotalScore" +
                    "+ totalScore $totalWeekScore"
        )
        previousTotalScore += totalWeekScore
        edit.apply()

        Log.d("days", "total number of azkar $totalValueOfWeek")

        val scoreWeekPercentage =
            ((totalWeekScore.toDouble() / totalValueOfWeek.toDouble()) * 100).toInt() // using this week score(not previous)

        Log.d("dayss", "total week score  $totalWeekScore")
        Log.d("dayss", "percentage  $scoreWeekPercentage")
        return scoreWeekPercentage
    }

    private fun showDialogAccordingToPercentage(scoreWeekPercentage: Int, isEndOfMonth: Boolean) {
        val image: Int
        val addNewAzkarToYourSchedule: String = if (IntroSharedPref.readGander("Male", false)) {
            getString(R.string.add_new_azkar_to_your_schedule)
        } else {
            getString(R.string.add_new_azkar_to_your_schedule_female)
        }
        when (scoreWeekPercentage) {
            in 80..100 -> {
                image = if (IntroSharedPref.readGander("Male", false)) {
                    R.drawable.gheth_happy
                } else {
                    R.drawable.zahra_happy
                }
                weeklyMessage = generateRandomSuccessMessage()
                if (isEndOfMonth) {
                    showEndMonthCongratulationMessage(
                        addNewAzkarToYourSchedule,
                        weeklyMessage,
                        image,
                        isEndOfMonth,
                        scoreWeekPercentage
                    )
                } else {
                    showNormalCongratulationMessage(
                        weeklyMessage,
                        image,
                        isEndOfMonth
                    )
                }
                Log.d("days", "success")
            }
            in 65..79 -> { // handle adding
                image = if (IntroSharedPref.readGander("Male", false)) {
                    R.drawable.gheth_normal_yellow
                } else {
                    R.drawable.zahra_normal_yellow
                }
                weeklyMessage = generateRandomMediumMessage()
                if (isEndOfMonth) {
                    edit.putLong("totalScore", 0L)
                    edit.putLong("totalNumberAzkar", 0L)
                    edit.apply()
                    showEndMonthCongratulationMessage(
                        addNewAzkarToYourSchedule,
                        weeklyMessage,
                        image,
                        isEndOfMonth,
                        scoreWeekPercentage
                    )
                } else {
                    showNormalCongratulationMessage(
                        weeklyMessage,
                        image,
                        isEndOfMonth
                    )
                }
                Log.d("days", "medium")
            }
            in 0..64 -> {
                image = if (IntroSharedPref.readGander("Male", false)) {
                    R.drawable.gheth_sad
                } else {
                    R.drawable.zahra_sad
                }
                weeklyMessage = generateRandomFailMessage()
                if (isEndOfMonth) {

                    showEndMonthCongratulationMessage(
                        addNewAzkarToYourSchedule,
                        weeklyMessage,
                        image,
                        isEndOfMonth,
                        scoreWeekPercentage
                    )
                } else {
                    showNormalCongratulationMessage(
                        weeklyMessage,
                        image,
                        isEndOfMonth
                    )
                }
                Log.d("days", "fail")
            }
        }
    }

    private fun calculateScore(): Int {
        Log.d("days", "number of azkar $numberOfAzkar")
        val totalValueOfWeek = numberOfAzkar * 7

        var previousTotalNumberAzkar = pref.getLong("totalNumberAzkar", 0L)
        edit.putLong("totalNumberAzkar", (previousTotalNumberAzkar + totalValueOfWeek))
        Log.d(
            "days", "previousTotalNumberAzkar: $previousTotalNumberAzkar" +
                    "+ totalValueOfWeek $totalValueOfWeek"
        )
        previousTotalNumberAzkar += totalValueOfWeek
        var previousTotalScore = pref.getLong("totalScore", 0L)
        edit.putLong("totalScore", (previousTotalScore + totalWeekScore))
        Log.d(
            "days", "previousTotalScore: $previousTotalScore" +
                    "+ totalScore $totalWeekScore"
        )
        previousTotalScore += totalWeekScore
        edit.apply()

        Log.d("days", "total number of azkar $totalValueOfWeek")
        val scoreWeekPercentage =
            ((previousTotalScore.toDouble() / previousTotalNumberAzkar.toDouble()) * 100).toInt()

        Log.d("days", "total week score  $totalWeekScore")
        Log.d("days", "percentage  $scoreWeekPercentage")
        return scoreWeekPercentage
    }


    // show message after end of week but not end of month
    private fun showNormalCongratulationMessage(
        message: String,
        image: Int,
        isEndOfMonth: Boolean
    ) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.zahra_message_dialog)
        val btnOk = dialog.findViewById<Button>(R.id.btn_ok_message)
        val txtMessage = dialog.findViewById<TextView>(R.id.txt_message)
        val characterImage = dialog.findViewById<ImageView>(R.id.characterImage)
        val frameImage = dialog.findViewById<ImageView>(R.id.img_frame_message)
        if (IntroSharedPref.readGander("Male", false)) {
            frameImage.setImageResource(R.drawable.gheth_frame_dialog)
            btnOk.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.darkBlue))
        } else {
            frameImage.setImageResource(R.drawable.zahra_frame_dialog)
            btnOk.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.pink))
        }
        characterImage.setImageResource(image)
        txtMessage.text = message

        if (isEndOfMonth) {
            edit.putInt("nweek", 1)
            edit.apply()
        } else {
            edit.putInt("nweek", numberOfWeeks + 1)
            edit.apply()
        }

        // logic to start new week and save score of prev week
        viewModel.createNewWeekSchedule(
            lastAzkarDay,
            currentAzkarHashMap,
            weeklyMessage,
            lastAzkarDay.mCalendar,
        )
        btnOk.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun showEndMonthCongratulationMessage(
        addOrDeleteMessage: String,
        message: String,
        image: Int,
        isEndOfMonth: Boolean,
        scoreWeekPercentage: Int
    ) {
        afterMonthDialog = Dialog(requireContext())
        afterMonthDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        afterMonthDialog.setContentView(R.layout.zahra_message_end_of_month)
        val btnYes = afterMonthDialog.findViewById<Button>(R.id.btn_ok_message_end_of_month)
        val btnNo = afterMonthDialog.findViewById<Button>(R.id.btn_no)
        val txtMessage = afterMonthDialog.findViewById<TextView>(R.id.txt_message_end_of_month)

        val txtPercentage = afterMonthDialog.findViewById<TextView>(R.id.txt_month_percentage)
        txtPercentage.text = "?????????????? ???????????? " +
                "%$scoreWeekPercentage"

        val txtMessageAddOrDelete =
            afterMonthDialog.findViewById<TextView>(R.id.txt_add_or_delete_message)
        val characterImage =
            afterMonthDialog.findViewById<ImageView>(R.id.character_image_end_of_month)
        characterImage.setImageResource(image)

        val frameImage = afterMonthDialog.findViewById<ImageView>(R.id.img_frame_message)
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
            showAddAzkarDialog(isEndOfMonth)
        }
        btnNo.setOnClickListener {
            if (isEndOfMonth) {
                edit.putInt("nweek", 1)
                edit.apply()
            } else {
                edit.putInt("nweek", numberOfWeeks + 1)
                edit.apply()
            }

            edit.putLong("totalScore", 0L)
            edit.putLong("totalNumberAzkar", 0L)
            edit.apply()

            viewModel.createNewWeekSchedule(
                lastAzkarDay,
                currentAzkarHashMap,
                weeklyMessage,
                lastAzkarDay.mCalendar
            )
            afterMonthDialog.dismiss()
        }
        afterMonthDialog.show()
    }

    private fun showAddAzkarDialog(isEndOfMonth: Boolean) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.add_azkar_dialog)
        val btnSave = dialog.findViewById<Button>(R.id.btn_save)
        val btnBack = dialog.findViewById<Button>(R.id.btn_back_no_adding)

        val hamdCheck = dialog.findViewById<CheckBox>(R.id.hamd_check)
        val tsbehCheck = dialog.findViewById<CheckBox>(R.id.tsbeh_check)
        val tkberCheck = dialog.findViewById<CheckBox>(R.id.tkber_check)
        val estghCheck = dialog.findViewById<CheckBox>(R.id.estghphar_check)

        val txtChooseZekr = dialog.findViewById<TextView>(R.id.txt_choose_zekr)
        if (IntroSharedPref.readGander("Male", false)) {
            txtChooseZekr.text = getString(R.string.choose_akar_to_add_message)
        } else {
            txtChooseZekr.text = getString(R.string.choose_akar_to_add_message_female)
        }
        pref = requireContext().getSharedPreferences("settingPrefs", Context.MODE_PRIVATE)
        hamdCheck.isChecked = pref.getBoolean("hamd", false)
        tsbehCheck.isChecked = pref.getBoolean("tsbeh", false)
        tkberCheck.isChecked = pref.getBoolean("tkber", false)
        estghCheck.isChecked = pref.getBoolean("estgh", false)

        btnSave.setOnClickListener {
            // logic to start new week and save score of prev week
            if (isEndOfMonth) {
                edit.putInt("nweek", 1)
                edit.apply()
            } else {
                edit.putInt("nweek", numberOfWeeks + 1)
                edit.apply()
            }

            edit.putLong("totalScore", 0L)
            edit.putLong("totalNumberAzkar", 0L)
            edit.apply()

            edit = pref.edit()

            if (hamdCheck.isChecked) {
                currentAzkarHashMap["?????? ??????"] = false
                edit.putBoolean("hamd", true)
            } else {
                edit.putBoolean("hamd", false)
                currentAzkarHashMap.remove("?????? ??????")
            }

            if (tsbehCheck.isChecked) {
                currentAzkarHashMap["?????? ??????????"] = false
                edit.putBoolean("tsbeh", true)
            } else {
                edit.putBoolean("tsbeh", false)
                currentAzkarHashMap.remove("?????? ??????????")
            }

            if (tkberCheck.isChecked) {
                currentAzkarHashMap["?????? ??????????"] = false
                edit.putBoolean("tkber", true)
            } else {
                edit.putBoolean("tkber", false)
                currentAzkarHashMap.remove("?????? ??????????")
            }

            if (estghCheck.isChecked) {
                currentAzkarHashMap["?????? ??????????????"] = false
                edit.putBoolean("estgh", true)
            } else {
                edit.putBoolean("estgh", false)
                currentAzkarHashMap.remove("?????? ??????????????")
            }
            edit.apply()

            viewModel.createNewWeekSchedule(
                lastAzkarDay,
                currentAzkarHashMap,
                weeklyMessage,
                lastAzkarDay.mCalendar
            )
            dialog.dismiss()
            afterMonthDialog.dismiss()
        }
        btnBack.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    fun generateRandomSuccessMessage(): String {
        val strings = ArrayList<String>()
        if (IntroSharedPref.readGander("Male", false)) {
            strings.add(getString(R.string.success_message1_azkar))
            strings.add(getString(R.string.success_message2_azkar))
            strings.add(getString(R.string.success_message3_azkar))
            strings.add(getString(R.string.success_message4_azkar))
        } else { // female
            strings.add(getString(R.string.success_message1_azkar_female))
            strings.add(getString(R.string.success_message2_azkar_female))
            strings.add(getString(R.string.success_message3_azkar_female))
            strings.add(getString(R.string.success_message4_azkar_female))
        }
        return strings.random()
    }

    private fun generateRandomMediumMessage(): String {
        val strings = ArrayList<String>()
        if (IntroSharedPref.readGander("Male", false)) {
            strings.add(getString(R.string.medium_message1_azkar))
            strings.add(getString(R.string.medium_message2_azkar))
            strings.add(getString(R.string.medium_message3_azkar))
            strings.add(getString(R.string.medium_message4_azkar))
        } else { // female
            strings.add(getString(R.string.medium_message1_azkar_female))
            strings.add(getString(R.string.medium_message2_azkar_female))
            strings.add(getString(R.string.medium_message3_azkar_female))
            strings.add(getString(R.string.medium_message4_azkar_female))
        }
        return strings.random()
    }

    private fun generateRandomFailMessage(): String {
        val strings = ArrayList<String>()
        if (IntroSharedPref.readGander("Male", false)) {
            strings.add(getString(R.string.fail_message1_azkar))
            strings.add(getString(R.string.fail_message2_azkar))
            strings.add(getString(R.string.fail_message3_azkar))
            strings.add(getString(R.string.fail_message4_azkar))
        } else { // female
            strings.add(getString(R.string.fail_message1_azkar_female))
            strings.add(getString(R.string.fail_message2_azkar_female))
            strings.add(getString(R.string.fail_message3_azkar_female))
            strings.add(getString(R.string.fail_message4_azkar_female))
        }
        return strings.random()
    }
}