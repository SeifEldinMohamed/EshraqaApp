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
import com.seif.eshraqaapp.data.models.Prayer
import com.seif.eshraqaapp.data.sharedPreference.IntroSharedPref
import com.seif.eshraqaapp.databinding.FragmentPrayerDaysBinding
import com.seif.eshraqaapp.ui.adapters.PrayerAdapter
import com.seif.eshraqaapp.ui.fragments.PrayerDaysFragmentArgs.fromBundle
import com.seif.eshraqaapp.viewmodels.PrayerViewModel
import com.seif.eshraqaapp.viewmodels.QuranViewModel
import jp.wasabeef.recyclerview.animators.ScaleInTopAnimator


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

       prayerViewModel =  ViewModelProvider(requireActivity())[PrayerViewModel::class.java]

        setHasOptionsMenu(true)

        if (prayerViewModel.isAppFirstTimeRun(requireContext())) {
            prayerViewModel.addPrayer(
                prayerViewModel.getSevenDaysPrayerData(
                    fromBundle(requireArguments()).sonnHashMap
                )
            )
        }
        prayerViewModel.prayer.observe(viewLifecycleOwner, Observer { it ->
            myAdapter.addPrayerList(it)
            if (it.isNotEmpty()) {
                //  numberOfQuran = it[0].quran.size
                currentPrayerHashMap = it[0].prayersHashMap
                if (it.size == 7)
                    lastPrayerDay = it[6]

            }
        })
//        prayerViewModel.getAllQuranWeekScore().observe(viewLifecycleOwner, Observer {
//            totalWeekScore = prayerViewModel.getSumOfQuranWeekScore(it)
//        })
        prayerViewModel.vacationDaysNumber.observe(viewLifecycleOwner, Observer {
            prayerVacationDaysNumber = it ?: 0
        })

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
//            val scoreWeekPercentage = calculateScore()
//            showDialogAccordingToPercentage(scoreWeekPercentage, isEndOfMonth)
            dialog.dismiss()
        }
        btnBack.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showDialogAccordingToPercentage(scoreWeekPercentage: Int, isEndOfMonth: Boolean) {
        val image: Int
        when (scoreWeekPercentage) {
            in 80..100 -> {
                image = if (IntroSharedPref.readGander("Male", false)) {
                    R.drawable.gheth_happy
                } else {
                    R.drawable.zahra_happy
                }
                weeklyMessage = generateRandomSuccessMessageQuran()
                if (isEndOfMonth) {
                    edit.putLong("totalScoreQuran", 0L)
                    edit.putLong("totalNumberQuran", 0L)
                    edit.apply()
//                    showEndMonthCongratulationMessage(
//                        getString(R.string.updateWorkCounter),
//                        weeklyMessage,
//                        image,
//                    )
                } else {
                    showNormalCongratulationMessage(
                        weeklyMessage,
                        image,
                    )
                }
                Log.d("days", "success")
            }
            in 65..79 -> { // handle adding
                image = if (IntroSharedPref.readGander("Male", false)) {
                    R.drawable.gheth_normal
                } else {
                    R.drawable.zahra_normal
                }
                weeklyMessage = generateRandomMediumMessageQuran()
                if (isEndOfMonth) {
                    edit.putLong("totalScoreQuran", 0L)
                    edit.putLong("totalNumberQuran", 0L)
                    edit.apply()
//                    showEndMonthCongratulationMessage(
//                        getString(R.string.updateWorkCounter),
//                        weeklyMessage,
//                        image,
//                    )
                } else {
                    showNormalCongratulationMessage(
                        weeklyMessage,
                        image,
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
                weeklyMessage = generateRandomFailMessageQuran()
                if (isEndOfMonth) {
                    edit.putLong("totalScoreQuran", 0L)
                    edit.putLong("totalNumberQuran", 0L)
                    edit.apply()
//                    showEndMonthCongratulationMessage(
//                        getString(R.string.updateWorkCounter),
//                        weeklyMessage,
//                        image,
//                    )
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
 //   private fun calculateScore(): Int {
//        numberOfPrayer =
//        Log.d("day", "vacationDays = $prayerVacationDaysNumber")
//
//        var totalValueOfWeek = numberOfQuran - (prayerVacationDaysNumber * 3)  // ex: 12
//        if (totalValueOfWeek <= 0)
//            totalValueOfWeek = 1
//
//        if (totalWeekScore > totalValueOfWeek && prayerVacationDaysNumber != 7) // to keep percentage in range of 0 to 100
//            totalWeekScore = totalValueOfWeek
//        else if (prayerVacationDaysNumber == 7) {
//            totalWeekScore = totalValueOfWeek
//        }
//
//        Log.d("day", "totalValueOfWeek = $totalValueOfWeek")
//        var previousTotalNumberQuran = pref.getLong("totalNumberQuran", 0L)
//        Log.d("day", "previousTotalNumberQuran Old = $previousTotalNumberQuran")
//
//        edit.putLong("totalNumberQuran", (previousTotalNumberQuran + totalValueOfWeek))
//        edit.apply()
//
//        previousTotalNumberQuran += totalValueOfWeek
//        Log.d("day", "previousTotalNumberQuran new = $previousTotalNumberQuran")
//
//        var previousTotalScore = pref.getLong("totalScoreQuran", 0L)
//        Log.d("day", "previousTotalScore old = $previousTotalScore")
//
//        edit.putLong("totalScoreQuran", (previousTotalScore + totalWeekScore))
//        edit.apply()
//
//
//
//        previousTotalScore += totalWeekScore
//        Log.d("day", "previousTotalScore new = $previousTotalScore")
//
//        Log.d(
//            "day",
//            "percentage = ${(previousTotalScore.toDouble() / previousTotalNumberQuran.toDouble()) * 100} %"
//        )
//        return ((previousTotalScore.toDouble() / previousTotalNumberQuran.toDouble()) * 100).toInt()
 //   }

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
        btnOk.setOnClickListener {
            // logic to start new week and save score of prev week
//            prayerViewModel.addPrayer(
//                prayerViewModel.createNewWeekSchedule(
//                    lastPrayerDay,
//                    prayer,
//                    weeklyMessage,
//                )
//            )
            dialog.dismiss()
        }
        dialog.show()
    }

    fun generateRandomSuccessMessageQuran(): String {
        val strings = ArrayList<String>()
        strings.add(getString(R.string.success_message1_quran))
        strings.add(getString(R.string.success_message2_quran))
        strings.add(getString(R.string.success_message3_quran))

        return strings.random()
    }

    private fun generateRandomMediumMessageQuran(): String {
        val strings = ArrayList<String>()
        strings.add(getString(R.string.medium_message1_quran))
        strings.add(getString(R.string.medium_message2_quran))
        strings.add(getString(R.string.medium_message3_quran))

        return strings.random()
    }

    private fun generateRandomFailMessageQuran(): String {
        val strings = ArrayList<String>()
        strings.add(getString(R.string.fail_message1_quran))
        strings.add(getString(R.string.fail_message2_quran))
        strings.add(getString(R.string.fail_message3_quran))

        return strings.random()
    }
}