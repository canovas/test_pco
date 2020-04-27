package com.pedrocanovas.test_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pedrocanovas.test_app.ui.views.MainFragment
import com.pedrocanovas.test_app.utils.IOnBackPressed

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }
    }

    override fun onBackPressed() {
        val fragment = this.supportFragmentManager.findFragmentById(R.id.container)
        (fragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {
            if(!it){super.onBackPressed()}
        }
    }
}
