package com.bharathvishal.messagecommunicationusingwearabledatalayer

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.NumberPicker
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.floatingactionbutton.FloatingActionButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RecordatorioFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var numPickerMin: NumberPicker
    private lateinit var numPickerSeg: NumberPicker
    private lateinit var numPickerAm: NumberPicker
    private var selectedDays: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private fun showBottomDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottomsheet_layout_recordatorio)

        val cancelButton = dialog.findViewById<Button>(R.id.buttonCancelRecordatorio)
        cancelButton.setOnClickListener { dialog.dismiss() }

        numPickerMin = dialog.findViewById(R.id.numPickerMinRec)
        numPickerSeg = dialog.findViewById(R.id.numPickerSegRec)
        numPickerAm = dialog.findViewById(R.id.numPickerAmRec)

        numPickerMin.minValue = 0
        numPickerMin.maxValue = 12

        numPickerSeg.minValue = 0
        numPickerSeg.maxValue = 59

        val str = arrayOf("AM", "PM")
        numPickerAm.minValue = 0
        numPickerAm.maxValue = (str.size - 1)
        numPickerAm.displayedValues = str

        dialog.show()

        dialog.window!!.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.windowAnimations = R.style.DialogAnimation
            setGravity(Gravity.BOTTOM)
        }

        setupDayCheckBoxes(dialog)
    }

    private fun setupDayCheckBoxes(dialog: Dialog) {
        val days = listOf(
            Pair(dialog.findViewById<MaterialCheckBox>(R.id.btnL), PickDayOfWeek.MONDAY),
            Pair(dialog.findViewById<MaterialCheckBox>(R.id.btnM), PickDayOfWeek.TUESDAY),
            Pair(dialog.findViewById<MaterialCheckBox>(R.id.btnX), PickDayOfWeek.WEDNESDAY),
            Pair(dialog.findViewById<MaterialCheckBox>(R.id.btnJ), PickDayOfWeek.THURSDAY),
            Pair(dialog.findViewById<MaterialCheckBox>(R.id.btnV), PickDayOfWeek.FRIDAY),
            Pair(dialog.findViewById<MaterialCheckBox>(R.id.btnS), PickDayOfWeek.SATURDAY),
            Pair(dialog.findViewById<MaterialCheckBox>(R.id.btnD), PickDayOfWeek.SUNDAY)
        )

        days.forEach { (checkBox, day) ->
            checkBox.isChecked = hasDayOfWeek(selectedDays, day)
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedDays += day.value
                } else {
                    selectedDays -= day.value
                }
            }
        }
    }

    private fun hasDayOfWeek(value: Int, day: PickDayOfWeek): Boolean {
        return value and day.value != 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recordatorio, container, false)

        val fab = view.findViewById<FloatingActionButton>(R.id.addRecordatorio)
        fab.setOnClickListener {
            showBottomDialog()
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RecordatorioFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
