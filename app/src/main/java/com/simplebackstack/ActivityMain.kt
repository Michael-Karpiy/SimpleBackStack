package com.backstackfragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import com.backstackfragment.BackStack.BackStackActivity
import com.simplebackstack.R
import com.simplebackstack.databinding.ActivityMainBinding

class ActivityMain : BackStackActivity(R.id.container) {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        context = this

        tvTitle = findViewById(R.id.tvTitle)
        cvBack = findViewById(R.id.cvBack)

        showFragment(Fragment1())
    }

    private fun adjustMarginTop() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.clAppbar) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = insets.top
            }

            windowInsets
        }
    }

    private fun adjustPaddingBottom() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.container) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(bottom = insets.bottom)

            windowInsets
        }
    }

    companion object {
        lateinit var context: Context
        lateinit var tvTitle: TextView
        lateinit var cvBack: CardView
    }

    override fun onStart() {
        super.onStart()

        val colorAppBar = TypedValue()
        val themeColor4 = this.theme
        themeColor4.resolveAttribute(R.attr.ColorAppBar, colorAppBar, true)
        @ColorInt val ColorAppBar = colorAppBar.data

        window.navigationBarColor = Color.TRANSPARENT
        window.statusBarColor = ColorAppBar
        adjustMarginTop()
        adjustPaddingBottom()
    }
}