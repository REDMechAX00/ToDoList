package com.redmechax00.todolist

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources

class App : Application() {
    companion object {
        lateinit var instance: App private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    object Strings {
        fun get(@StringRes stringRes: Int): String {
            return instance.getString(stringRes)
        }
    }

    object Colors {
        fun get(@ColorRes colorRes: Int): Int {
            return instance.getColor(colorRes)
        }
    }
}

