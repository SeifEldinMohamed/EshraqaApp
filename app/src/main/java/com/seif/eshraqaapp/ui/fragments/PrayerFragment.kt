package com.seif.eshraqaapp.ui.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.seif.eshraqaapp.R
import com.seif.eshraqaapp.data.models.Prayer
import com.seif.eshraqaapp.data.sharedPreference.AppSharedPref
import com.seif.eshraqaapp.data.sharedPreference.IntroSharedPref
import com.seif.eshraqaapp.databinding.FragmentPrayerBinding
import com.seif.eshraqaapp.ui.fragments.PrayerFragmentArgs.fromBundle
import com.seif.eshraqaapp.viewmodels.PrayerViewModel


class PrayerFragment : Fragment() {
    lateinit var binding: FragmentPrayerBinding
    lateinit var prayer: Prayer
    private lateinit var viewModel: PrayerViewModel
    private var prayerScore = 0
    private var qadaaScore = 0
    private var sonnScore = 0
    private var prayerHashMap = HashMap<String, Boolean>()
    private var qadaaHashMap = HashMap<String, Boolean>()
    private var sonnHashMap = HashMap<String, Boolean>()
    private lateinit var pref: SharedPreferences
    private var isVacation = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPrayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prayer = fromBundle(requireArguments()).prayer

        viewModel = ViewModelProvider(requireActivity())[PrayerViewModel::class.java]
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = prayer.date

        // vacation
        isVacation = prayer.isVacation
        binding.checkVacationPrayer.isChecked = isVacation

        binding.checkVacationPrayer.setOnCheckedChangeListener { buttonView, isChecked ->
            isVacation = isChecked
        }

        // copy values to prayerHashMap so i can use in update prayer
        prayerHashMap["fagr"] = prayer.prayersHashMap["fagr"] ?: false
        prayerHashMap["zuhr"] = prayer.prayersHashMap["zuhr"] ?: false
        prayerHashMap["asr"] = prayer.prayersHashMap["asr"] ?: false
        prayerHashMap["maghreb"] = prayer.prayersHashMap["maghreb"] ?: false
        prayerHashMap["esha"] = prayer.prayersHashMap["esha"] ?: false
        // qadaa
        qadaaHashMap["q_fagr"] = prayer.qadaaHashMap["q_fagr"] ?: false
        qadaaHashMap["q_zuhr"] = prayer.qadaaHashMap["q_zuhr"] ?: false
        qadaaHashMap["q_asr"] = prayer.qadaaHashMap["q_asr"] ?: false
        qadaaHashMap["q_maghreb"] = prayer.qadaaHashMap["q_maghreb"] ?: false
        qadaaHashMap["q_esha"] = prayer.qadaaHashMap["q_esha"] ?: false
        // sonn

        binding.fagrCheck.isChecked = prayerHashMap["fagr"] ?: false
        binding.zuhrCheck.isChecked = prayerHashMap["zuhr"] ?: false
        binding.asrCheck.isChecked = prayerHashMap["asr"] ?: false
        binding.maghrebCheck.isChecked = prayerHashMap["maghreb"] ?: false
        binding.eshaCheck.isChecked = prayerHashMap["esha"] ?: false

        if (AppSharedPref.readPrayerOnly("prayerOnly", false)) {
            binding.qadaaSchecule.visibility = View.GONE
            binding.sonnSchecule.visibility = View.GONE

        } else if (AppSharedPref.readPrayerAndQadaa("prayerAndQadaa", false)) {
            binding.qadaaSchecule.visibility = View.VISIBLE
            binding.sonnSchecule.visibility = View.GONE

            binding.qFagrCheck.isChecked = qadaaHashMap["q_fagr"] ?: false
            binding.qZuhrCheck.isChecked = qadaaHashMap["q_zuhr"] ?: false
            binding.qAsrCheck.isChecked = qadaaHashMap["q_asr"] ?: false
            binding.qMaghrebCheck.isChecked = qadaaHashMap["q_maghreb"] ?: false
            binding.qEshaCheck.isChecked = qadaaHashMap["q_esha"] ?: false
        } else { // sonn
            binding.qadaaSchecule.visibility = View.GONE
            binding.sonnSchecule.visibility = View.VISIBLE

            if (AppSharedPref.readSontFagr("s_fagr", false)) {
                binding.sontFagrCheck.visibility = View.VISIBLE
                sonnHashMap["s_fagr"] = prayer.sonnHashMap["s_fagr"] ?: false
                binding.sontFagrCheck.isChecked = sonnHashMap["s_fagr"] ?: false
            }
            if (AppSharedPref.readSontZuhr("s_zuhr", false)) {
                binding.sontZuhrCheck.visibility = View.VISIBLE
                sonnHashMap["s_zuhr"] = prayer.sonnHashMap["s_zuhr"] ?: false
                binding.sontZuhrCheck.isChecked = sonnHashMap["s_zuhr"] ?: false
            }
            if (AppSharedPref.readSontMaghreb("s_maghreb", false)) {
                binding.sontMaghrebCheck.visibility = View.VISIBLE
                sonnHashMap["s_maghreb"] = prayer.sonnHashMap["s_maghreb"] ?: false
                binding.sontMaghrebCheck.isChecked = sonnHashMap["s_maghreb"] ?: false
            }
            if (AppSharedPref.readSontEsha("s_esha", false)) {
                binding.sontEshaCheck.visibility = View.VISIBLE
                sonnHashMap["s_esha"] = prayer.sonnHashMap["s_esha"] ?: false
                binding.sontEshaCheck.isChecked = sonnHashMap["s_esha"] ?: false
            }
            if (AppSharedPref.readSontWetr("s_wetr", false)) {
                binding.sontWetrCheck.visibility = View.VISIBLE
                sonnHashMap["s_wetr"] = prayer.sonnHashMap["s_wetr"] ?: false
                binding.sontWetrCheck.isChecked = sonnHashMap["s_wetr"] ?: false
            }
            if (AppSharedPref.readSontDoha("s_doha", false)) {
                binding.sontDohaCheck.visibility = View.VISIBLE
                sonnHashMap["s_doha"] = prayer.sonnHashMap["s_doha"] ?: false
                binding.sontDohaCheck.isChecked = sonnHashMap["s_doha"] ?: false
            }
            if (AppSharedPref.readSontDoha("s_keyam", false)) {
                binding.sontKeyamCheck.visibility = View.VISIBLE
                sonnHashMap["s_keyam"] = prayer.sonnHashMap["s_keyam"] ?: false
                binding.sontKeyamCheck.isChecked = sonnHashMap["s_keyam"] ?: false
            }
        }

        // set Menu
        setHasOptionsMenu(true)
        // show text day and score from coming data
        binding.txtDayPrayer.text = prayer.dayName
        binding.textScorePrayerOnly.text = "${prayer.prayerScore}/5"
        binding.textScoreQadaa.text = "${prayer.qadaaScore}/5"
        binding.textScoreSonn.text = "${prayer.sonnScore}/${prayer.sonnHashMap.size}"

        prayerScore = prayer.prayerScore
        qadaaScore = prayer.qadaaScore
        sonnScore = prayer.sonnScore

        // prayer
        binding.fagrCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                prayerScore++
                prayerHashMap["fagr"] = true
            } else {
                prayerScore--
                prayerHashMap["fagr"] = false
            }
            binding.textScorePrayerOnly.text = "$prayerScore/5"
        }
        binding.zuhrCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                prayerScore++
                prayerHashMap["zuhr"] = true
            } else {
                prayerScore--
                prayerHashMap["zuhr"] = false
            }
            binding.textScorePrayerOnly.text = "$prayerScore/5"
        }
        binding.asrCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                prayerScore++
                prayerHashMap["asr"] = true
            } else {
                prayerScore--
                prayerHashMap["asr"] = false
            }
            binding.textScorePrayerOnly.text = "$prayerScore/5"
        }
        binding.maghrebCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                prayerScore++
                prayerHashMap["maghreb"] = true
            } else {
                prayerScore--
                prayerHashMap["maghreb"] = false
            }
            binding.textScorePrayerOnly.text = "$prayerScore/5"
        }
        binding.eshaCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                prayerScore++
                prayerHashMap["esha"] = true
            } else {
                prayerScore--
                prayerHashMap["esha"] = false
            }
            binding.textScorePrayerOnly.text = "$prayerScore/5"
        }

        binding.qFagrCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                qadaaScore++
                qadaaHashMap["q_fagr"] = true
            } else {
                qadaaScore--
                qadaaHashMap["q_fagr"] = false
            }
            binding.textScoreQadaa.text = "$qadaaScore/5"
        }
        binding.qZuhrCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                qadaaScore++
                qadaaHashMap["q_zuhr"] = true
            } else {
                qadaaScore--
                qadaaHashMap["q_zuhr"] = false
            }
            binding.textScoreQadaa.text = "$qadaaScore/5"
        }
        binding.qAsrCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                qadaaScore++
                qadaaHashMap["q_asr"] = true
            } else {
                qadaaScore--
                qadaaHashMap["q_asr"] = false
            }
            binding.textScoreQadaa.text = "$qadaaScore/5"
        }
        binding.qMaghrebCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                qadaaScore++
                qadaaHashMap["q_maghreb"] = true
            } else {
                qadaaScore--
                qadaaHashMap["q_maghreb"] = false
            }
            binding.textScoreQadaa.text = "$qadaaScore/5"
        }
        binding.qEshaCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                qadaaScore++
                qadaaHashMap["q_esha"] = true
            } else {
                qadaaScore--
                qadaaHashMap["q_esha"] = false
            }
            binding.textScoreQadaa.text = "$qadaaScore/5"
        }

        // sonn
        binding.sontFagrCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                sonnScore++
                sonnHashMap["s_fagr"] = true
            } else {
                sonnScore--
                sonnHashMap["s_fagr"] = false
            }
            binding.textScoreSonn.text = "$sonnScore/${prayer.sonnHashMap.size}"
        }
        binding.sontZuhrCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                sonnScore++
                sonnHashMap["s_zuhr"] = true
            } else {
                sonnScore--
                sonnHashMap["s_zuhr"] = false
            }
            binding.textScoreSonn.text = "$sonnScore/${prayer.sonnHashMap.size}"
        }
        binding.sontMaghrebCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                sonnScore++
                sonnHashMap["s_maghreb"] = true
            } else {
                sonnScore--
                sonnHashMap["s_maghreb"] = false
            }
            binding.textScoreSonn.text = "$sonnScore/${prayer.sonnHashMap.size}"
        }
        binding.sontEshaCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                sonnScore++
                sonnHashMap["s_esha"] = true
            } else {
                sonnScore--
                sonnHashMap["s_esha"] = false
            }
            binding.textScoreSonn.text = "$sonnScore/${prayer.sonnHashMap.size}"
        }
        binding.sontWetrCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                sonnScore++
                sonnHashMap["s_wetr"] = true
            } else {
                sonnScore--
                sonnHashMap["s_wetr"] = false
            }
            binding.textScoreSonn.text = "$sonnScore/${prayer.sonnHashMap.size}"
        }
        binding.sontDohaCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                sonnScore++
                sonnHashMap["s_doha"] = true
            } else {
                sonnScore--
                sonnHashMap["s_doha"] = false
            }
            binding.textScoreSonn.text = "$sonnScore/${prayer.sonnHashMap.size}"
        }
        binding.sontKeyamCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                sonnScore++
                sonnHashMap["s_keyam"] = true
            } else {
                sonnScore--
                sonnHashMap["s_keyam"] = false
            }
            binding.textScoreSonn.text = "$sonnScore/${prayer.sonnHashMap.size}"
        }

        IntroSharedPref.init(requireContext())

        if (IntroSharedPref.readGander("Male", false)) {
            binding.zahraHeadPrayer.visibility = View.GONE
            binding.ghethHeadPrayer.visibility = View.VISIBLE
            binding.checkVacationPrayer.visibility = View.GONE
        }
        else { // female
            binding.zahraHeadPrayer.visibility = View.VISIBLE
            binding.ghethHeadPrayer.visibility = View.GONE
            binding.checkVacationPrayer.visibility = View.VISIBLE
        }

        binding.characterBackground.setOnClickListener {
            showEshrakaMessageDialog()
        }
    }

    private fun showEshrakaMessageDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.zahra_message_dialog)
        val btnOk = dialog.findViewById<Button>(R.id.btn_ok_message)
        val txtMessage = dialog.findViewById<TextView>(R.id.txt_message)
        txtMessage.text = prayer.weeklyUserMessage
        val frameImage = dialog.findViewById<ImageView>(R.id.img_frame_message)
        val characterImage = dialog.findViewById<ImageView>(R.id.characterImage)
        if (IntroSharedPref.readGander("Male", false)) {
            frameImage.setImageResource(R.drawable.gheth_frame_dialog)
            characterImage.setImageResource(R.drawable.gheth_normal)
            btnOk.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.darkBlue))
        } else { // female
            frameImage.setImageResource(R.drawable.zahra_frame_dialog)
            characterImage.setImageResource(R.drawable.zahra_normal)
            btnOk.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.pink))
        }
        btnOk.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.azkar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> updatePrayerItem()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updatePrayerItem() {
        val newPrayer = Prayer(
            prayer.id,
            prayerHashMap,
            qadaaHashMap,
            sonnHashMap,
            prayer.date,
            prayer.dayName,
            prayer.mCalendar,
            prayerScore,
            qadaaScore,
            sonnScore,
            (prayerScore + qadaaScore + sonnScore),
            prayer.weeklyUserMessage,
            isVacation
        )
        viewModel.updatePrayer(newPrayer)
        findNavController().navigate(R.id.action_prayerFragment_to_prayerDaysFragment)
    }

}