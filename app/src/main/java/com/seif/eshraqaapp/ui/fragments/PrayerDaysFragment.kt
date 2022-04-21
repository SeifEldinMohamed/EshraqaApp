package com.seif.eshraqaapp.ui.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.seif.eshraqaapp.R
import com.seif.eshraqaapp.data.models.Prayer
import com.seif.eshraqaapp.data.sharedPreference.AppSharedPref
import com.seif.eshraqaapp.data.sharedPreference.IntroSharedPref
import com.seif.eshraqaapp.databinding.FragmentPrayerDaysBinding
import com.seif.eshraqaapp.ui.adapters.PrayerAdapter
import com.seif.eshraqaapp.ui.fragments.PrayerDaysFragmentArgs.fromBundle
import com.seif.eshraqaapp.viewmodels.PrayerViewModel
import jp.wasabeef.recyclerview.animators.ScaleInTopAnimator
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


private const val maxNumberOfWeeks: Int = 500

class PrayerDaysFragment : Fragment() {
    lateinit var binding: FragmentPrayerDaysBinding
    private val myAdapter: PrayerAdapter by lazy { PrayerAdapter() }
    private lateinit var prayerViewModel: PrayerViewModel
    private var numberOfPrayer = 0
    private var totalWeekScore = 0
    lateinit var lastPrayerDay: Prayer
    private lateinit var pref: SharedPreferences
    private lateinit var edit: SharedPreferences.Editor
    private var numberOfWeeks: Int = 1
    private var currentPrayerHashMap = HashMap<String, Boolean>()
    private var currentQadaaHashMap = HashMap<String, Boolean>()
    private var currentSonnHashMap = HashMap<String, Boolean>()
    lateinit var afterMonthDialog: Dialog
    private var weeklyMessage: String = ""
    private var prayerVacationDaysNumber: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPrayerDaysBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prayerViewModel = ViewModelProvider(requireActivity())[PrayerViewModel::class.java]

        setHasOptionsMenu(true)

        if (prayerViewModel.isAppFirstTimeRun(requireContext())) {
            prayerViewModel.getSevenDaysPrayerData(
                fromBundle(requireArguments()).sonnHashMap
            )
        }
        prayerViewModel.prayer.observe(viewLifecycleOwner) { it ->
            if (it.isNotEmpty()) {
                //  numberOfQuran = it[0].quran.size
                currentPrayerHashMap = it[0].prayersHashMap
                currentQadaaHashMap = it[0].qadaaHashMap
                currentSonnHashMap = it[0].sonnHashMap
            }
            if (it.size == 7) {
                lastPrayerDay = it[6]
                myAdapter.addPrayerList(it)
            }
        }
        prayerViewModel.getAllPrayerWeekScore().observe(viewLifecycleOwner) {
            totalWeekScore = prayerViewModel.getSumOfPrayerWeekScore(it)
        }
        prayerViewModel.vacationDaysNumber.observe(viewLifecycleOwner) {
            prayerVacationDaysNumber = it ?: 0
        }

        prayerViewModel.isAppFirstTimeRun(requireContext())
        binding.rvPrayer.adapter = myAdapter
        binding.rvPrayer.itemAnimator = ScaleInTopAnimator().apply {
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
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.confirmation_dialog)
        val btnOk = dialog.findViewById<Button>(R.id.btn_ok)
        val btnBack = dialog.findViewById<Button>(R.id.btn_back_counters)
        val txtNewWeekMessage = dialog.findViewById<TextView>(R.id.txt_new_week)

        if (IntroSharedPref.readGander("Male", false)) {
            txtNewWeekMessage.text = getString(R.string.end_of_week_confirmation)
        } else {
            txtNewWeekMessage.text = getString(R.string.end_of_week_confirmation_female)
        }

        btnOk.setOnClickListener { //////////
            // logic to start new week and save score of prev week
            if (AppSharedPref.readPrayerAndQadaa("prayerAndQadaa", false)) {
                var currentQadaaPeriod = AppSharedPref.readQadaaPeriod("qadaa_period", 0)
                currentQadaaPeriod--
                AppSharedPref.writeQadaaPeriod("qadaa_period", currentQadaaPeriod)
            }
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
        numberOfPrayer = 7 * 5
        Log.d("day", "vacationDays = $prayerVacationDaysNumber")

        var totalValueOfWeek = numberOfPrayer - (prayerVacationDaysNumber * 5)
        if (totalValueOfWeek <= 0)
            totalValueOfWeek = 1

        if (totalWeekScore > totalValueOfWeek && prayerVacationDaysNumber != 7) // to keep percentage in range of 0 to 100
            totalWeekScore = totalValueOfWeek
        else if (prayerVacationDaysNumber == 7) {
            totalWeekScore = totalValueOfWeek
        }

        var previousTotalNumberQuran = pref.getLong("totalNumberPrayer", 0L)
        Log.d("day", "previousTotalNumberPrayer Old = $previousTotalNumberQuran")

        edit.putLong("totalNumberPrayer", (previousTotalNumberQuran + totalValueOfWeek))
        edit.apply()

        previousTotalNumberQuran += totalValueOfWeek
        Log.d("day", "TotalNumberPrayer new = $previousTotalNumberQuran")

        var previousTotalScore = pref.getLong("totalScorePrayer", 0L)
        Log.d("day", "previousTotalScore old = $previousTotalScore")

        edit.putLong("totalScorePrayer", (previousTotalScore + totalWeekScore))
        edit.apply()

        previousTotalScore += totalWeekScore
        Log.d("day", "TotalScore new = $previousTotalScore")

        val scoreWeekPercentage =
            ((totalWeekScore.toDouble() / totalValueOfWeek.toDouble()) * 100).toInt() // using this week score(not previous)

        Log.d("days", "total week score  $totalWeekScore")
        Log.d("days", "percentage  $scoreWeekPercentage")
        return scoreWeekPercentage

    }

    private fun showDialogAccordingToPercentage(scoreWeekPercentage: Int, isEndOfMonth: Boolean) {
        val image: Int
        val addOrDeleteMessage: String
        when (scoreWeekPercentage) {
            100 -> {
                if (IntroSharedPref.readGander("Male", false)) {
                    image = R.drawable.gheth_happy
                    addOrDeleteMessage = getString(R.string.update_prayer_schedule)
                } else {
                    image = R.drawable.zahra_happy
                    addOrDeleteMessage = getString(R.string.update_prayer_schedule_female)
                }
                weeklyMessage = generateRandomSuccessMessagePrayer()
                if (isEndOfMonth && AppSharedPref.readQadaaPeriod("qadaa_period", -1) <= 0) {
//                    edit.putLong("totalScorePrayer", 0L)
//                    edit.putLong("totalNumberPrayer", 0L)
//                    edit.apply()
                    showEndMonthCongratulationMessage(
                        addOrDeleteMessage,
                        weeklyMessage,
                        image,
                        isEndOfMonth,
                        scoreWeekPercentage
                    )
                    Log.d(
                        "day",
                        "qadaa period: " + AppSharedPref.readQadaaPeriod("qadaa_period", -1)
                            .toString()
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

            else -> {

                image = if (IntroSharedPref.readGander("Male", false)) {
                    R.drawable.gheth_sad
                } else {
                    R.drawable.zahra_sad
                }
                weeklyMessage = generateRandomFailMessagePrayer()
                if (isEndOfMonth) {
                    edit.putLong("totalScorePrayer", 0L)
                    edit.putLong("totalNumberPrayer", 0L)
                    edit.apply()

                }
                showNormalCongratulationMessage(
                    weeklyMessage,
                    image,
                    isEndOfMonth
                )
                Log.d("days", "fail")
            }
        }
    }

    private fun calculateScore(): Int {
        numberOfPrayer = 7 * 5
        Log.d("day", "vacationDays = $prayerVacationDaysNumber")

        var totalValueOfWeek = numberOfPrayer - (prayerVacationDaysNumber * 5)
        if (totalValueOfWeek <= 0)
            totalValueOfWeek = 1

        if (totalWeekScore > totalValueOfWeek && prayerVacationDaysNumber != 7) // to keep percentage in range of 0 to 100
            totalWeekScore = totalValueOfWeek
        else if (prayerVacationDaysNumber == 7) {
            totalWeekScore = totalValueOfWeek
        }

        var previousTotalNumberQuran = pref.getLong("totalNumberPrayer", 0L)
        Log.d("day", "previousTotalNumberPrayer Old = $previousTotalNumberQuran")

        edit.putLong("totalNumberPrayer", (previousTotalNumberQuran + totalValueOfWeek))
        edit.apply()

        previousTotalNumberQuran += totalValueOfWeek
        Log.d("day", "TotalNumberPrayer new = $previousTotalNumberQuran")

        var previousTotalScore = pref.getLong("totalScorePrayer", 0L)
        Log.d("day", "previousTotalScore old = $previousTotalScore")

        edit.putLong("totalScorePrayer", (previousTotalScore + totalWeekScore))
        edit.apply()

        previousTotalScore += totalWeekScore
        Log.d("day", "TotalScore new = $previousTotalScore")

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

        if (AppSharedPref.readQadaaPeriod("qadaa_period", -1) == 0
            && !AppSharedPref.readPrayerAndSonn("prayerAndSonn", false)
        ) {
            AppSharedPref.writePrayerAndQadaa("prayerAndQadaa", false)
            AppSharedPref.writePrayerOnly("prayerOnly", true)
        }

        if (isEndOfMonth) {
            edit.putInt("nweek", 1)
            edit.apply()
        } else {
            edit.putInt("nweek", numberOfWeeks + 1)
            edit.apply()
        }

        prayerViewModel.createNewWeekSchedule(
            currentPrayerHashMap,
            currentQadaaHashMap,
            currentSonnHashMap,
            weeklyMessage,
            lastPrayerDay.mCalendar
        )

        btnOk.setOnClickListener {
            // logic to start new week and save score of prev week
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
        val txtMessageAddOrDelete =
            afterMonthDialog.findViewById<TextView>(R.id.txt_add_or_delete_message)
        val characterImage =
            afterMonthDialog.findViewById<ImageView>(R.id.character_image_end_of_month)
        characterImage.setImageResource(image)

        val frameImage = afterMonthDialog.findViewById<ImageView>(R.id.img_frame_message)

        val txtPercentage = afterMonthDialog.findViewById<TextView>(R.id.txt_month_percentage)
        txtPercentage.text = "التقييم الشهري " +
                "%$scoreWeekPercentage"

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

        if (AppSharedPref.readQadaaPeriod("qadaa_period", -1) == 0
            && !AppSharedPref.readPrayerAndSonn("prayerAndSonn", false)
        ) { // check if qadaa period is finished or not
            AppSharedPref.writePrayerAndQadaa("prayerAndQadaa", false)
            AppSharedPref.writePrayerOnly("prayerOnly", true)
        }

        btnYes.setOnClickListener {
            // logic to start new week and save score of prev week
            if (AppSharedPref.readPrayerOnly("prayerOnly", false)) {
                // start ask from qadaa
                showQadaaBottomSheetDialog(isEndOfMonth)
                afterMonthDialog.dismiss()
            } else { // sonn + prayer
                // so he can add or remove sonn from it's schedule
                showQadaaBottomSheetDialog(isEndOfMonth)
                afterMonthDialog.dismiss()
            }
        }
        btnNo.setOnClickListener {
            if (isEndOfMonth) {
                edit.putInt("nweek", 1)
                edit.apply()
            } else {
                edit.putInt("nweek", numberOfWeeks + 1)
                edit.apply()
            }
            edit.putLong("totalScorePrayer", 0L)
            edit.putLong("totalNumberPrayer", 0L)
            edit.apply()

            prayerViewModel.createNewWeekSchedule(
                currentPrayerHashMap,
                currentQadaaHashMap,
                currentSonnHashMap,
                weeklyMessage,
                lastPrayerDay.mCalendar
            )

            afterMonthDialog.dismiss()
        }
        afterMonthDialog.show()
    }


    fun generateRandomSuccessMessagePrayer(): String {
        val strings = ArrayList<String>()
        if (IntroSharedPref.readGander("Male", false)) {
            strings.add(getString(R.string.success_message1_prayer))
            strings.add(getString(R.string.success_message2_prayer))
            strings.add(getString(R.string.success_message3_prayer))
        } else { // female
            strings.add(getString(R.string.success_message1_prayer_female))
            strings.add(getString(R.string.success_message2_prayer_female))
            strings.add(getString(R.string.success_message3_prayer_female))
        }

        return strings.random()
    }

    private fun generateRandomFailMessagePrayer(): String {
        val strings = ArrayList<String>()
        if (IntroSharedPref.readGander("Male", false)) {
            strings.add(getString(R.string.fail_message1_prayer))
            strings.add(getString(R.string.fail_message2_prayer))
            strings.add(getString(R.string.fail_message3_prayer))
        } else { //female
            strings.add(getString(R.string.fail_message1_prayer_female))
            strings.add(getString(R.string.fail_message2_prayer_female))
            strings.add(getString(R.string.fail_message3_prayer_female))
        }

        return strings.random()
    }

    private fun showQadaaBottomSheetDialog(isEndOfMonth: Boolean) {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val bottomSheetView = LayoutInflater.from(requireContext()).inflate(
            R.layout.prayer_bottom_sheet_dialog,
            view?.findViewById<ConstraintLayout>(R.id.bottom_sheet_prayer)
        )
        bottomSheetView.findViewById<TextView>(R.id.txt_prayer_question).text =
            getString(R.string.qadaa_question1)
        bottomSheetView.findViewById<Button>(R.id.btn_yes_prayer).setOnClickListener {
            showQadaa2BottomSheetDialog(isEndOfMonth) // to know how many weeks the qadaa schedule will be available
            bottomSheetDialog.dismiss()
        }
        bottomSheetView.findViewById<Button>(R.id.btn_no_prayer).setOnClickListener {
            showChooseSonnBottomSheetDialog(isEndOfMonth) // go to sonn bottom sheet
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    private fun showChooseSonnBottomSheetDialog(isEndOfMonth: Boolean) {
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

        sontDohaCheckBox.isChecked = AppSharedPref.readSontDoha("s_doha", false)
        sontEshaCheckBox.isChecked = AppSharedPref.readSontEsha("s_esha", false)
        sontfagrCheckBox.isChecked = AppSharedPref.readSontFagr("s_fagr", false)
        sontKeyamCheckBox.isChecked = AppSharedPref.readSontKeyam("s_keyam", false)
        sontMaghrebCheckBox.isChecked = AppSharedPref.readSontMaghreb("s_maghreb", false)
        sontZuhrCheckBox.isChecked = AppSharedPref.readSontZuhr("s_zuhr", false)
        sontWetrCheckBox.isChecked = AppSharedPref.readSontWetr("s_wetr", false)


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
                Toast.makeText(
                    requireContext(),
                    "يجب أختيار سنة واحدة كبداية !",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (isEndOfMonth) {
                    edit.putInt("nweek", 1)
                    edit.apply()
                } else {
                    edit.putInt("nweek", numberOfWeeks + 1)
                    edit.apply()
                }

                edit.putLong("totalScorePrayer", 0L)
                edit.putLong("totalNumberPrayer", 0L)
                edit.apply()

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

                prayerViewModel.createNewWeekSchedule(
                    currentPrayerHashMap,
                    currentQadaaHashMap,
                    currentSonnHashMap,
                    weeklyMessage,
                    lastPrayerDay.mCalendar
                )

                bottomSheetDialog.dismiss()
            }
        }
        bottomSheetView.findViewById<Button>(R.id.btn_back_sonn).setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    private fun showQadaa2BottomSheetDialog(isEndOfMonth: Boolean) {
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
                numberOfWeeksEditText.text.toString().toInt() <= 0 -> {
                    numberOfWeeksEditText.error = " أقل عدد 1" + " !"
                }
                else -> {
                    if (isEndOfMonth) {
                        edit.putInt("nweek", 1)
                        edit.apply()
                    } else {
                        edit.putInt("nweek", numberOfWeeks + 1)
                        edit.apply()
                    }

                    edit.putLong("totalScorePrayer", 0L)
                    edit.putLong("totalNumberPrayer", 0L)
                    edit.apply()

                    AppSharedPref.writePrayerOnly("prayerOnly", false)
                    AppSharedPref.writePrayerAndQadaa("prayerAndQadaa", true)
                    AppSharedPref.writePrayerAndSonn("prayerAndSonn", false)

                    AppSharedPref.writeQadaaPeriod(
                        "qadaa_period",
                        numberOfWeeksEditText.text.toString().toInt()
                    )

                    prayerViewModel.createNewWeekSchedule(
                        currentPrayerHashMap,
                        currentQadaaHashMap,
                        currentSonnHashMap,
                        weeklyMessage,
                        lastPrayerDay.mCalendar
                    )

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
}