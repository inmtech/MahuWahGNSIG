package com.app.googlespreadsheet.view.fragment


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast

import com.app.googlespreadsheet.R
import com.app.googlespreadsheet.util.SharedPrefsUtil
import com.app.googlespreadsheet.view.activity.MainActivity
import com.app.kotlindemo.util.BaseActivity
import com.app.kotlindemo.util.BaseFragment
import kotlinx.android.synthetic.main.activity_unit_selection.*
import kotlinx.android.synthetic.main.content_main2.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 *
 */
class UnitSelectionFragment : BaseFragment() {

    var unitName: String = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        return inflater.inflate(R.layout.fragment_unit_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        radio_group.setOnCheckedChangeListener(
                RadioGroup.OnCheckedChangeListener { group, checkedId ->
                    if (checkedId == rbKhakhara.id) {
                        unitName = rbKhakhara.text.toString()
                    } else if (checkedId == rbMasala.id) {
                        unitName = rbMasala.text.toString()
                    } else if (checkedId == rbBakery.id) {
                        unitName = rbBakery.text.toString()
                    }
                })

        btnSubmitUnit.setOnClickListener(View.OnClickListener {

            if (unitName.length > 0) {
                SharedPrefsUtil.getInstance().setSheetName(requireContext(), unitName)
            }
            replaceFragment(requireContext(), AddRecordFragment(), activity as BaseActivity)
        })


    }


}
