package com.superpow.skill.ui.gameSlots1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucky.rush.ui.models.Slot
import com.superpow.skill.R
import com.superpow.skill.ui.core.Storage
import com.superpow.skill.ui.models.Event
import com.superpow.skill.ui.models.State
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.pow

class SlotGame1ViewModel : ViewModel() {

    companion object {
        private val drawables = listOf(
            R.drawable.ic_slot_1,
            R.drawable.ic_slot_2,
            R.drawable.ic_slot_3,
            R.drawable.ic_slot_4,
            R.drawable.ic_slot_5
        )
        private const val FREQ = 20L
        const val VISIBLE_AMOUNT_IN_SLOT = 3f
    }

    private val _totalBalance = MutableLiveData<Long>()
    val totalBalance: LiveData<Long> = _totalBalance

    private val _winned = MutableLiveData<Event<Long>>()
    val winned: LiveData<Event<Long>> = _winned

    private var state = State.Rest

    private val _slots1 = MutableLiveData<List<Slot>>()
    val slot1: LiveData<List<Slot>> = _slots1

    private val _slots2 = MutableLiveData<List<Slot>>()
    val slot2: LiveData<List<Slot>> = _slots2

    private val _slots3 = MutableLiveData<List<Slot>>()
    val slot3: LiveData<List<Slot>> = _slots3

    private val _slots4 = MutableLiveData<List<Slot>>()
    val slot4: LiveData<List<Slot>> = _slots4

    private val _slots5 = MutableLiveData<List<Slot>>()
    val slot5: LiveData<List<Slot>> = _slots5

    init {
        randomizeGame()
    }

    private fun randomizeGame() {
        val slots1 = mutableListOf<Slot>().apply {
            addAll(
                _slots1.value?.filter { it.position in 0f..1f } ?: emptyList(),
            )
        }
        val slots2 = mutableListOf<Slot>().apply {
            addAll(
                _slots2.value?.filter { it.position in 0f..1f } ?: emptyList(),
            )
        }
        val slots3 = mutableListOf<Slot>().apply {
            addAll(
                _slots3.value?.filter { it.position in 0f..1f } ?: emptyList(),
            )
        }
        val slots4 = mutableListOf<Slot>().apply {
            addAll(
                _slots4.value?.filter { it.position in 0f..1f } ?: emptyList(),
            )
        }
        val slots5 = mutableListOf<Slot>().apply {
            addAll(
                _slots5.value?.filter { it.position in 0f..1f } ?: emptyList(),
            )
        }
        val offset = 1 / (VISIBLE_AMOUNT_IN_SLOT * 2)
        for (i in slots1.size..48) {
            slots1.add(
                Slot(
                    i,
                    drawables.random(),
                    offset + i * offset * 2,
                ),
            )
            slots2.add(
                Slot(
                    i,
                    drawables.random(),
                    offset + i * offset * 2,
                ),
            )
            slots3.add(
                Slot(
                    i,
                    drawables.random(),
                    offset + i * offset * 2,
                ),
            )
            slots4.add(
                Slot(
                    i,
                    drawables.random(),
                    offset + i * offset * 2,
                ),
            )
            slots5.add(
                Slot(
                    i,
                    drawables.random(),
                    offset + i * offset * 2,
                ),
            )
        }
        for (i in 0..1) {
            slots1.add(
                randomizeSlot(
                    slots1.last().slotId + 1,
                    slots1.last().position + 1 / VISIBLE_AMOUNT_IN_SLOT,
                ),
            )
            slots2.add(
                randomizeSlot(
                    slots2.last().slotId + 1,
                    slots2.last().position + 1 / VISIBLE_AMOUNT_IN_SLOT,
                ),
            )
            slots3.add(
                randomizeSlot(
                    slots3.last().slotId + 1,
                    slots3.last().position + 1 / VISIBLE_AMOUNT_IN_SLOT,
                ),
            )
            slots4.add(
                randomizeSlot(
                    slots4.last().slotId + 1,
                    slots4.last().position + 1 / VISIBLE_AMOUNT_IN_SLOT,
                ),
            )
            slots5.add(
                randomizeSlot(
                    slots5.last().slotId + 1,
                    slots5.last().position + 1 / VISIBLE_AMOUNT_IN_SLOT,
                ),
            )
        }
        _slots1.value = slots1
        _slots2.value = slots2
        _slots3.value = slots3
        _slots4.value = slots4
        _slots5.value = slots5
    }

    private fun randomizeSlot(id: Int, relativePosition: Float): Slot {
        val drawableRes = drawables.random()
        return Slot(
            id,
            drawableRes,
            relativePosition,
        )
    }

    fun test(bet: Long): Long {
        randomizeGame()
        val mult = getKoef()
        val wined = bet * mult
        return wined.toLong()
    }

    fun spin(betAmount: Long, data: Storage) {
        viewModelScope.launch {
            if (state == State.Rest) {
                randomizeGame()
                _totalBalance.value = data.balance - betAmount

                state = State.Play
                launch { spinSlot(_slots1) }
                delay(300)
                launch { spinSlot(_slots2) }
                delay(300)
                launch { spinSlot(_slots3) }
                delay(300)
                launch { spinSlot(_slots4) }
                delay(300)
                spinSlot(_slots5)

                val koef = getKoef()
                if (koef == 0f) {
                    if (data.balance== 0L) {
                        _totalBalance.value = 5000L
                    }
                } else {
                    _totalBalance.value = (data.balance + koef * betAmount + betAmount).toLong()
                    _winned.value = Event((koef * betAmount).toLong())
                }

                state = State.Rest
            }
        }
    }



    private fun getKoef(): Float {
        val result = listOf(
            _slots1.value?.let { slots ->
                slots[slots.size - 2].drawableId
            } ?: return 0f,
            _slots2.value?.let { slots ->
                slots[slots.size - 2].drawableId
            } ?: return 0f,
            _slots3.value?.let { slots ->
                slots[slots.size - 2].drawableId
            } ?: return 0f,
            _slots4.value?.let { slots ->
                slots[slots.size - 2].drawableId
            } ?: return 0f,
            _slots5.value?.let { slots ->
                slots[slots.size - 2].drawableId
            } ?: return 0f,
        )

        if (result.distinct().size == 1) {
            return 13f
        }

        if (result.distinct().size == 2) {
            return 5f
        }

        if (result.distinct().size == 3) {
            return 1.2f
        }

        return 0f
    }

    private suspend fun spinSlot(liveData: MutableLiveData<List<Slot>>) {
        val slots = liveData.value ?: return
        var position =
            slots.reversed().indexOfFirst { it.position in 1 / VISIBLE_AMOUNT_IN_SLOT..2 / VISIBLE_AMOUNT_IN_SLOT }
        while (slots.last().position > 7 / (VISIBLE_AMOUNT_IN_SLOT * 2f) ) {
            delay(FREQ)
            val positionDif = calculateSpeed(position) * FREQ / 1000
            slots.forEach { it.position = it.position - positionDif }
            liveData.value = slots
            position = slots.reversed().indexOfFirst { slot ->
                slot.position in 1 / VISIBLE_AMOUNT_IN_SLOT..2 / VISIBLE_AMOUNT_IN_SLOT
            }
        }
        val positionAdjust = slots.last().position - 7 / (VISIBLE_AMOUNT_IN_SLOT * 2f)
        slots.forEach { it.position = it.position - positionAdjust }
        liveData.value = slots
    }

    private fun calculateSpeed(position: Int): Float {
        return position.toFloat().pow(1 / 2f)
    }
}
