package com.superpow.skill.ui.core

import android.app.Activity
import android.content.Context
import android.util.Log
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class Storage(context: Context) {

    private val preferences = context.getSharedPreferences("Default", Context.MODE_PRIVATE)

    var isUserSigned by PrefDelegate.Boolean("isUserAnon")

    var sounds by PrefDelegate.Boolean("sounds", true)

    var vibrates by PrefDelegate.Boolean("vibrates", true)

    var balance by PrefDelegate.Long("balance", 5000L)

    var lastBetGame1 by PrefDelegate.Long("lastBetGame1", 500L)

    var lastBetGame2 by PrefDelegate.Long("lastBetGame2", 500L)

    var lastBetGame3 by PrefDelegate.Long("lastBetGame3", 500L)

    var lastWinGame1 by PrefDelegate.Long("lastWinGame1", 0L)

    var lastWinGame2 by PrefDelegate.Long("lastWinGame2", 0L)

    var lastWinGame3 by PrefDelegate.Long("lastWinGame3", 0L)

    private sealed class PrefDelegate<T>(protected val key: kotlin.String) :
        ReadWriteProperty<Storage, T> {

        class Boolean(key: kotlin.String, val defaultValue: kotlin.Boolean = false) : PrefDelegate<kotlin.Boolean>(key) {

            override fun getValue(thisRef: Storage, property: KProperty<*>) =
                thisRef.preferences.getBoolean(key, defaultValue)

            override fun setValue(
                thisRef: Storage,
                property: KProperty<*>,
                value: kotlin.Boolean,
            ) = thisRef.preferences.edit().putBoolean(key, value).apply()
        }

        class Int(
            key: kotlin.String,
            private val defaultValue: kotlin.Int
        ) : PrefDelegate<kotlin.Int>(key) {

            override fun getValue(thisRef: Storage, property: KProperty<*>) =
                thisRef.preferences.getInt(key, defaultValue)

            override fun setValue(thisRef: Storage, property: KProperty<*>, value: kotlin.Int) =
                thisRef.preferences.edit().putInt(key, value).apply()
        }

        class Long(
            key: kotlin.String,
            private val defaultValue: kotlin.Long,
        ) : PrefDelegate<kotlin.Long>(key) {

            override fun getValue(thisRef: Storage, property: KProperty<*>) =
                thisRef.preferences.getLong(key, defaultValue)

            override fun setValue(
                thisRef: Storage,
                property: KProperty<*>,
                value: kotlin.Long,
            ) = thisRef.preferences.edit().putLong(key, value).apply()
        }

        class String(key: kotlin.String) : PrefDelegate<kotlin.String>(key) {

            override fun getValue(thisRef: Storage, property: KProperty<*>) =
                thisRef.preferences.getString(key, "") ?: ""

            override fun setValue(
                thisRef: Storage,
                property: KProperty<*>,
                value: kotlin.String,
            ) = thisRef.preferences.edit().putString(key, value).apply()
        }

        class IntSet(key: kotlin.String) :
            PrefDelegate<Set<kotlin.Int>>(key) {

            override fun getValue(
                thisRef: Storage,
                property: KProperty<*>,
            ): Set<kotlin.Int> {
                val stringSet = thisRef.preferences.getStringSet(key, setOf()) ?: setOf()
                return stringSet.map { string ->
                    try {
                        string.toInt()
                    } catch (e: NumberFormatException) {
                        Log.e("tag", e.message.toString())
                        -1
                    }
                }.toSet()
            }

            override fun setValue(
                thisRef: Storage,
                property: KProperty<*>,
                value: Set<kotlin.Int>,
            ) = thisRef.preferences.edit().putStringSet(key, value.map { it.toString() }.toSet())
                .apply()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: Storage? = null

        fun getInstance(activity: Activity): Storage =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Storage(activity).also { INSTANCE = it }
            }
    }
}
