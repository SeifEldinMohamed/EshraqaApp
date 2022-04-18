package com.seif.eshraqaapp.ui.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.seif.eshraqaapp.R
import com.seif.eshraqaapp.data.models.Azkar
import com.seif.eshraqaapp.data.sharedPreference.IntroSharedPref
import com.seif.eshraqaapp.ui.fragments.AzkarFragmentArgs.fromBundle
import com.seif.eshraqaapp.viewmodels.AzkarViewModel
import com.seif.eshraqaapp.databinding.FragmentAzkarBinding


class AzkarFragment : Fragment() {
    private lateinit var binding: FragmentAzkarBinding
    private lateinit var viewModel: AzkarViewModel
    private var score = 0
    private var azkarHashMap = HashMap<String, Boolean>()
    private lateinit var pref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAzkarBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[AzkarViewModel::class.java]
        (activity as AppCompatActivity?)!!.supportActionBar!!.title =
            fromBundle(requireArguments()).azkar.date

        score = fromBundle(requireArguments()).azkar.score
        // copy values to azkarHashMap so i can use in update azkar
        azkarHashMap["أذكار الصباح"] = fromBundle(requireArguments()).azkar.azkar["أذكار الصباح"] ?: false
        azkarHashMap["أذكار المساء"] = fromBundle(requireArguments()).azkar.azkar["أذكار المساء"] ?: false
        azkarHashMap["أذكار النوم"] = fromBundle(requireArguments()).azkar.azkar["أذكار النوم"] ?: false
        azkarHashMap["أذكار بعد الصلاة"] = fromBundle(requireArguments()).azkar.azkar["أذكار بعد الصلاة"] ?: false

        // first initialization for check boxes
        binding.sabahCheck.isChecked = azkarHashMap["أذكار الصباح"] ?: false
        binding.masaaCheck.isChecked = azkarHashMap["أذكار المساء"] ?: false
        binding.sleepCheck.isChecked = azkarHashMap["أذكار النوم"] ?: false
        binding.afterPrayerCheck.isChecked = azkarHashMap["أذكار بعد الصلاة"] ?: false

        pref = requireContext().getSharedPreferences("settingPrefs", Context.MODE_PRIVATE)
       if(pref.getBoolean("hamd",false)){
           binding.wrdHamdCheck.visibility = View.VISIBLE
           azkarHashMap["ورد حمد"] = fromBundle(requireArguments()).azkar.azkar["ورد حمد"] ?: false
           binding.wrdHamdCheck.isChecked = azkarHashMap["ورد حمد"]?: false

       }
        if(pref.getBoolean("tsbeh",false)){
            azkarHashMap["ورد تسبيح"] = fromBundle(requireArguments()).azkar.azkar["ورد تسبيح"] ?: false
            binding.wrdTsbe7Check.visibility = View.VISIBLE
            binding.wrdTsbe7Check.isChecked = azkarHashMap["ورد تسبيح"]?: false
        }
        if(pref.getBoolean("tkber",false)){
            binding.wrdTkberCheck.visibility = View.VISIBLE
            azkarHashMap["ورد تكبير"] = fromBundle(requireArguments()).azkar.azkar["ورد تكبير"] ?: false
            binding.wrdTkberCheck.isChecked = azkarHashMap["ورد تكبير"]?: false

        }
        if(pref.getBoolean("estgh",false)){
            binding.wrdEstegpharCheck.visibility = View.VISIBLE
            azkarHashMap["ورد استغفار"] = fromBundle(requireArguments()).azkar.azkar["ورد استغفار"] ?: false
            binding.wrdEstegpharCheck.isChecked = azkarHashMap["ورد استغفار"]?: false
        }
/////////////////////////////////////////////////////////
        // set Menu
        setHasOptionsMenu(true)
        // show text day and score from coming data
        val numberOfAzkar = fromBundle(requireArguments()).azkar.azkar.size
        binding.textDay.text = fromBundle(requireArguments()).azkar.dayName
        binding.textScore.text = "${fromBundle(requireArguments()).azkar.score}/$numberOfAzkar"

        binding.sabahCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                score++
                azkarHashMap["أذكار الصباح"] = true
            }
            else{
                score--
                azkarHashMap["أذكار الصباح"] = false
            }
            binding.textScore.text = "$score/$numberOfAzkar"
        }
        binding.masaaCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                score++
                azkarHashMap["أذكار المساء"] = true
            }
            else{
                score--
                azkarHashMap["أذكار المساء"] = false
            }
            binding.textScore.text = "$score/$numberOfAzkar"
        }
        binding.sleepCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                score++
                azkarHashMap["أذكار النوم"] = true
            }
            else{
                score--
                azkarHashMap["أذكار النوم"] = false
            }
            binding.textScore.text = "$score/$numberOfAzkar"
        }
        binding.afterPrayerCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                score++
                azkarHashMap["أذكار بعد الصلاة"] = true
            }
            else{
                score--
                azkarHashMap["أذكار بعد الصلاة"] = false
            }
            binding.textScore.text = "$score/$numberOfAzkar"
        }
        binding.wrdHamdCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                score++
                azkarHashMap["ورد حمد"] = true
            }
            else{
                score--
                azkarHashMap["ورد حمد"] = false
            }
            binding.textScore.text = "$score/$numberOfAzkar"
        }
        binding.wrdTkberCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                score++
                azkarHashMap["ورد تكبير"] = true
            }
            else{
                score--
                azkarHashMap["ورد تكبير"] = false
            }
            binding.textScore.text = "$score/$numberOfAzkar"
        }
        binding.wrdTsbe7Check.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                score++
                azkarHashMap["ورد تسبيح"] = true
            }
            else{
                score--
                azkarHashMap["ورد تسبيح"] = false
            }
            binding.textScore.text = "$score/$numberOfAzkar"
        }
        binding.wrdEstegpharCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                score++
                azkarHashMap["ورد استغفار"] = true
            }
            else{
                score--
                azkarHashMap["ورد استغفار"] = false
            }
            binding.textScore.text = "$score/$numberOfAzkar"
        }

        IntroSharedPref.init(requireContext())

        if(IntroSharedPref.readGander("Male", false)){
            binding.azkarImageZahra.visibility = View.GONE
            binding.azkarImageGheth.visibility = View.VISIBLE
        }
        else{
            binding.azkarImageZahra.visibility = View.VISIBLE
            binding.azkarImageGheth.visibility = View.GONE
        }

        binding.azkarImageGheth.setOnClickListener {
            showEshrakaMessageDialog()
        }
        binding.azkarImageZahra.setOnClickListener {
            showEshrakaMessageDialog()
        }
    }

    private fun showEshrakaMessageDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.zahra_message_dialog)
        val btnOk = dialog.findViewById<Button>(R.id.btn_ok_message)
        val txtMessage = dialog.findViewById<TextView>(R.id.txt_message)
        txtMessage.text = fromBundle(requireArguments()).azkar.weeklyUserMessage
        val frameImage = dialog.findViewById<ImageView>(R.id.img_frame_message)
        val characterImage = dialog.findViewById<ImageView>(R.id.characterImage)
        if(IntroSharedPref.readGander("Male", false)){
            frameImage.setImageResource(R.drawable.gheth_frame_dialog)
            characterImage.setImageResource(R.drawable.gheth_normal)
            btnOk.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.darkBlue))
        }
        else{ // female
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
            R.id.menu_save -> updateAzkarItem()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateAzkarItem() {
        val zekr = Azkar(
            fromBundle(requireArguments()).azkar.id,
            azkarHashMap,
            fromBundle(requireArguments()).azkar.checkDay,
            fromBundle(requireArguments()).azkar.checkMonth,
            fromBundle(requireArguments()).azkar.checkYear,
            fromBundle(requireArguments()).azkar.date,
            fromBundle(requireArguments()).azkar.dayName,
            score,
            fromBundle(requireArguments()).azkar.weeklyUserMessage,
            fromBundle(requireArguments()).azkar.mCalendar,
        )
        viewModel.updateZekr(zekr)
        findNavController().navigate(R.id.action_azkarFragment_to_daysFragment)
    }
}