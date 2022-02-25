package com.seif.eshraqaapp

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.seif.eshraqaapp.data.models.Azkar
import com.seif.eshraqaapp.databinding.FragmentDaysBinding
import com.seif.eshraqaapp.ui.adapters.AzkarAdapter
import com.seif.eshraqaapp.ui.fragments.AzkarFragmentArgs
import com.seif.eshraqaapp.viewmodels.AzkarViewModel


class DaysFragment : Fragment() {
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
            Log.d("day", it.toString())
            myAdapter.addAzkar(it!!)
            if (it.isNotEmpty()) {
                numberOfAzkar = it[0].azkar.size
                currentAzkarHashMap = it[0].azkar
                if (it.size == 7)
                    lastAzkarDay = it[6]

            }
        }
        viewModel.getAllWeekScore().observe(viewLifecycleOwner, Observer {
            totalWeekScore = viewModel.getSumOfWeekScore(it)
        })
        viewModel.isAppFirstTimeRun(requireContext())

        binding.rvAzkarDays.adapter = myAdapter

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
        if (isEndOfMonth){
            edit.putInt("nweek", 1)
            edit.apply()
        }
        else{
            edit.putInt("nweek", numberOfWeeks + 1)
            edit.apply()
        }
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.confirmation_dialog)
        val btnOk = dialog.findViewById<Button>(R.id.btn_ok)
        val btnBack = dialog.findViewById<Button>(R.id.btn_back)
        btnOk.setOnClickListener {
            // logic to start new week and save score of prev week
            Log.d("days", "number of azkar $numberOfAzkar")
            val totalValueOfWeek = numberOfAzkar * 7

            var previousTotalNumberAzkar = pref.getLong("totalNumberAzkar", 0L)
            edit.putLong("totalNumberAzkar",(previousTotalNumberAzkar + totalValueOfWeek))
            Log.d("days","previousTotalNumberAzkar: $previousTotalNumberAzkar" +
                    "+ totalValueOfWeek $totalValueOfWeek")
            previousTotalNumberAzkar+= totalValueOfWeek
            var previousTotalScore = pref.getLong("totalScore", 0L)
            edit.putLong("totalScore", (previousTotalScore + totalWeekScore))
            Log.d("days","previousTotalScore: $previousTotalScore" +
                    "+ totalScore $totalWeekScore")
            previousTotalScore+= totalWeekScore
            edit.apply()

            Log.d("days", "total number of azkar $totalValueOfWeek")
            val scoreWeekPercentage =
                ((previousTotalScore.toDouble() / previousTotalNumberAzkar.toDouble()) * 100).toInt()

            /**
             * My I need to save total week score (user grade) and number of total score(4(size of azkar hashMap)*7) in shared prefernce
             * **/

            Log.d("days", "total week score  $totalWeekScore")
            Log.d("days", "percentage  $scoreWeekPercentage")

            when (scoreWeekPercentage) {
                in 80..100 -> {
                    if (isEndOfMonth) {
                        val message = generateRandomSuccessMessage()////////////////////////////
                        showEndMonthCongratulationMessage(
                            getString(R.string.add_new_azkar_to_your_schedule),
                            message,
                            R.drawable.zahra_happy
                        )
                    } else {
                        showNormalCongratulationMessage(
                            getString(R.string.success_message1_azkar),
                            R.drawable.zahra_happy
                        )
                    }
                    Log.d("days", "success")
                }
                in 65..79 -> {
                    showNormalCongratulationMessage(
                        getString(R.string.medium_message1_azkar),
                        R.drawable.zahra_normal
                    )

                    Log.d("days", "medium")
                }
                in 0..64 -> {
                    if (isEndOfMonth) {
                        showEndMonthCongratulationMessage(
                            getString(R.string.add_new_azkar_to_your_schedule),
                            getString(R.string.fail_message1_azkar),
                            R.drawable.zahra_sad
                        )
                    } else {
                        showNormalCongratulationMessage(
                            getString(R.string.fail_message1_azkar),
                            R.drawable.zahra_sad
                        )
                    }
                    Log.d("days", "fail")
                }
            }
            dialog.dismiss()
        }
        btnBack.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    // show message after end of week but not end of month
    private fun showNormalCongratulationMessage(message: String, image: Int) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.zahra_message_dialog)
        val btnOk = dialog.findViewById<Button>(R.id.btn_ok_message)
        val txtMessage = dialog.findViewById<TextView>(R.id.txt_message)
        val characterImage = dialog.findViewById<ImageView>(R.id.characterImage)
        characterImage.setImageResource(image)
        txtMessage.text = message
        btnOk.setOnClickListener {
            // logic to start new week and save score of prev week
            viewModel.addZekr(viewModel.createNewWeekSchedule(lastAzkarDay, currentAzkarHashMap))
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showEndMonthCongratulationMessage(
        addOrDeleteMessage: String,
        message: String,
        image: Int
    ) {

        afterMonthDialog = Dialog(requireContext())
        afterMonthDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        afterMonthDialog.setContentView(R.layout.zahra_message_end_of_month)
        val btnYes = afterMonthDialog.findViewById<Button>(R.id.btn_ok_message_end_of_month)
        val btnNo = afterMonthDialog.findViewById<Button>(R.id.btn_no)
        val txtMessage = afterMonthDialog.findViewById<TextView>(R.id.txt_message_end_of_month)
        val txtMessageAddOrDelete = afterMonthDialog.findViewById<TextView>(R.id.txt_add_or_delete_message)
        val characterImage = afterMonthDialog.findViewById<ImageView>(R.id.character_image_end_of_month)
        characterImage.setImageResource(image)
        // change character and frame according to male of user
        txtMessage.text = message
        txtMessageAddOrDelete.text = addOrDeleteMessage
        btnYes.setOnClickListener {
            // logic to start new week and save score of prev week
            showAddAzkarDialog()
        }
        btnNo.setOnClickListener {
            viewModel.addZekr(viewModel.createNewWeekSchedule(lastAzkarDay, currentAzkarHashMap))
            afterMonthDialog.dismiss()
        }

        afterMonthDialog.show()
    }

    private fun showAddAzkarDialog() {

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.add_azkar_dialog)
        val btnSave = dialog.findViewById<Button>(R.id.btn_save)
        val btnBack = dialog.findViewById<Button>(R.id.btn_back_no_adding)

        val hamdCheck = dialog.findViewById<CheckBox>(R.id.hamd_check)
        val tsbehCheck = dialog.findViewById<CheckBox>(R.id.tsbeh_check)
        val tkberCheck = dialog.findViewById<CheckBox>(R.id.tkber_check)
        val estghCheck = dialog.findViewById<CheckBox>(R.id.estghphar_check)

        pref = requireContext().getSharedPreferences("settingPrefs", Context.MODE_PRIVATE)
        hamdCheck.isChecked =  pref.getBoolean("hamd", false)
        tsbehCheck.isChecked =  pref.getBoolean("tsbeh", false)
        tkberCheck.isChecked =   pref.getBoolean("tkber", false)
        estghCheck.isChecked =  pref.getBoolean("estgh", false)
        val txtMessage = dialog.findViewById<TextView>(R.id.txt_choose_zekr)
        //txtMessage.text = message
        btnSave.setOnClickListener {
            // logic to start new week and save score of prev week
            edit = pref.edit()

            if (hamdCheck.isChecked) {
                currentAzkarHashMap["ورد حمد"] = false
                edit.putBoolean("hamd", true)
            }
            else{
                edit.putBoolean("hamd", false)
                currentAzkarHashMap.remove("ورد حمد")
            }

            if (tsbehCheck.isChecked){
                currentAzkarHashMap["ورد تسبيح"] = false
                edit.putBoolean("tsbeh", true)
            }
            else{
                edit.putBoolean("tsbeh", false)
                currentAzkarHashMap.remove("ورد تسبيح")
            }

            if (tkberCheck.isChecked){
                currentAzkarHashMap["ورد تكبير"] = false
                edit.putBoolean("tkber", true)
            }
            else{
                edit.putBoolean("tkber", false)
                currentAzkarHashMap.remove("ورد تكبير")
            }

            if (estghCheck.isChecked){
                currentAzkarHashMap["ورد استغفار"] = false
                edit.putBoolean("estgh", true)
            }
            else{
                edit.putBoolean("estgh", false)
                currentAzkarHashMap.remove("ورد استغفار")
            }

            edit.apply()
            viewModel.addZekr(viewModel.createNewWeekSchedule(lastAzkarDay, currentAzkarHashMap))
            dialog.dismiss()
            afterMonthDialog.dismiss()
        }
        btnBack.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
    fun generateRandomSuccessMessage():String{
        val strings = ArrayList<String>()
        strings.add(getString(R.string.success_message1_azkar))
        strings.add(getString(R.string.success_message2_azkar))
        strings.add(getString(R.string.success_message3_azkar))
        strings.add(getString(R.string.success_message4_azkar))
        strings.random()
       return strings[0]
    }

}