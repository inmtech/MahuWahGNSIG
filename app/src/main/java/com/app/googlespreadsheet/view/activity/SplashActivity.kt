package com.app.googlespreadsheet.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat.startActivity
import com.app.googlespreadsheet.R
import com.app.googlespreadsheet.util.SharedPrefsUtil
import com.app.kotlindemo.util.BaseActivity

class SplashActivity : BaseActivity() {

    private val SPLASH_TIME_OUT: Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity

            if (SharedPrefsUtil.getInstance().isLogin(this)) {
                startActivity(Intent(this, UnitSelectionActivity::class.java))
            } else {
                startActivity(Intent(this, LoginAcitivity::class.java))
            }
            finish()
        }, SPLASH_TIME_OUT)
    }
}
