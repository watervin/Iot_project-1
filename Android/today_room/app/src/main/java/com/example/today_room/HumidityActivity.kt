package com.example.today_room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import kotlinx.android.synthetic.main.activity_humidity.*
import kotlinx.android.synthetic.main.activity_led.*
import kotlinx.android.synthetic.main.activity_led.btn_back

class HumidityActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_humidity)

        btn_back.setOnClickListener {
            finish()
        }

        //array속 내가 만든 array
        var Humidity = resources.getStringArray(R.array.my_array)
        spinner.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, Humidity)


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

                //Humidity[position]이런식으로 쓰면 될거 같은데..

            }
        }
    }
}