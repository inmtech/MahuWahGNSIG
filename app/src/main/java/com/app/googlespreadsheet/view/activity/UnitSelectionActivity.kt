package com.app.googlespreadsheet.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.app.googlespreadsheet.R
import com.app.googlespreadsheet.util.SharedPrefsUtil
import com.app.kotlindemo.util.BaseActivity
import kotlinx.android.synthetic.main.activity_unit_selection.*

class UnitSelectionActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unit_selection)
        if (!SharedPrefsUtil.getInstance().getSheetName(this).equals("")) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        radio_group.setOnCheckedChangeListener(
                RadioGroup.OnCheckedChangeListener { group, checkedId ->
                    val radio: RadioButton = findViewById(checkedId)
                })

        btnSubmitUnit.setOnClickListener(View.OnClickListener {

            var id: Int = radio_group.checkedRadioButtonId
            if (id != -1) { // If any radio button checked from radio group
                val radio: RadioButton = findViewById(id)
                Toast.makeText(
                        applicationContext, "You have selecte unit : ${radio.text}",
                        Toast.LENGTH_SHORT
                ).show()
                SharedPrefsUtil.getInstance().setSheetName(this, radio.getText().toString())
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(
                        applicationContext, "Please Select any unit.",
                        Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun radio_button_click(view: View) {
        // Get the clicked radio button instance
        val radio: RadioButton = findViewById(radio_group.checkedRadioButtonId)

    }
}
