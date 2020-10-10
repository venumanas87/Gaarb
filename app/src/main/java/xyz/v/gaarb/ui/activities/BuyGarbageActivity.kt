package xyz.v.gaarb.ui.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.jaeger.library.StatusBarUtil
import xyz.v.gaarb.R

class BuyGarbageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_garbage)
        window.statusBarColor = Color.parseColor("#F3340F")
        val back:ImageView = findViewById(R.id.back)
        back.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.scree_slide_in_from_left,R.anim.screen_slide_out_to_right)
    }
}