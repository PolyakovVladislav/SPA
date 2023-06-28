package com.superpow.skill.ui.gameRoulette3

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucky.rush.ui.models.Slot
import com.superpow.skill.ui.core.Storage
import com.superpow.skill.ui.models.State
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.random.Random

class RouletteGame3ViewModel : ViewModel() {

    companion object {
        const val FREQ = 20L
    }

    private val _totalBalance = MutableLiveData<Long>()
    val totalBalance: LiveData<Long> = _totalBalance

    private val _state = MutableLiveData(State.Play)
    val state: LiveData<State> = _state

    private val _degree = MutableLiveData<Float>()
    val degree:LiveData<Float> = _degree

    init {
        _degree. value = getRandom(0f, 360f)
        _state.value = State.Rest
    }

    private fun  getRandom(min: Float, max: Float): Float {
        require(min < max) { "Invalid range: min must be less than max" }
        return Random.nextFloat() * (max - min) + min
    }

    private fun  getRandom(min: Int, max: Int): Int {
        require(min < max) { "Invalid range: min must be less than max" }
        return Random.nextInt(min, max)
    }

    fun spin(betAmount: Long, data: Storage) {
        viewModelScope.launch {
            if (state.value == State.Rest) {
                _totalBalance.value = data.balance - betAmount

                _state.value = State.Play

                internalSpin()

                val koef = getKoef()
                if (koef == 0f) {
                    if (data.balance== 0L) {
                        _totalBalance.value = 5000L
                    }
                } else {
                    _totalBalance.value = (data.balance + koef * betAmount + betAmount).toLong()
                }

                _state.value = State.Rest
            }
        }
    }



    private fun getKoef(): Float {
        return when (degree.value ?: return 0f) {
            in 0f..45f -> 10f
            in 45f..90f -> 4f
            in 90f..135f -> 2f
            in 135f..180f -> 0f
            in 180f..225f -> 10f
            in 225f..270f -> 4f
            in 270f..315f -> 2f
            else -> 0f
        }
    }

    private suspend fun internalSpin() {
        val frames = getRandom(180, 240)
        repeat(frames) { frame ->
            val currentDegree = degree.value ?: 0f
            val adjust = calculateSpeed(frames - frame)
            var newDegree = currentDegree + adjust
            if (newDegree >= 360f) {
                newDegree -= 360
            }
            _degree.value = newDegree
            delay(FREQ)
        }
    }

    private fun calculateSpeed(frame: Int): Float {
        return frame.toFloat().pow(1 / 2.5f)
    }
}
