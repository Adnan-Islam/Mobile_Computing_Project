package com.example.mobile_computing_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var fab_addOpened = false

        fab_add.setOnClickListener{

            if (!fab_addOpened) {

                fab_addOpened = true
                fab_add.animate().translationY(-resources.getDimension((R.dimen.standard_116)))
                fab_log.animate().translationY(-resources.getDimension((R.dimen.standard_66)))

            } else {

                fab_addOpened = false
                fab_add.animate().translationY(0f)
                fab_log.animate().translationY(0f)

            }
        }

        button_log.setOnClickListener {
            startActivity(Intent(applicationContext, TimeActivity::class.java))

        }

        button_sign.setOnClickListener {
            startActivity(Intent(applicationContext, MapActivity::class.java))

        }

    }
}
