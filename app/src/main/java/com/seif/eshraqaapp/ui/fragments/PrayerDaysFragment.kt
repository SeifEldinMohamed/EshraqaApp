package com.seif.eshraqaapp.ui.fragments

import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.seif.eshraqaapp.data.models.Prayer
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
                currentPrayerHashMap = it[0].prayers
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
}