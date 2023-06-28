package com.superpow.skill.ui.gameSlots2

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.superpow.skill.databinding.FragmentSlotsGame2Binding
import com.superpow.skill.ui.core.BaseFragment
import com.superpow.skill.ui.core.Storage
import com.superpow.skill.ui.extensions.handleBackPress
import com.superpow.skill.ui.extensions.navigateSafe
import com.superpow.skill.ui.extensions.playWin
import com.superpow.skill.ui.extensions.setFullSensor
import com.superpow.skill.ui.extensions.setPortrait
import com.superpow.skill.ui.extensions.vibrate

class SlotsGame2Fragment : BaseFragment<FragmentSlotsGame2Binding>(
    FragmentSlotsGame2Binding::inflate,
) {

    private val viewModel by viewModels<SlotGame2ViewModel>()
    private val storage by lazy { Storage.getInstance(requireActivity()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFullSensor()

        handleBackPress {
            findNavController().navigateSafe(
                SlotsGame2FragmentDirections.actionSlotsGame2FragmentToMainFragment()
            )
        }

        with(binding) {
            tvBalance.text = storage.balance.toString()
            tvWin.text = storage.lastWinGame2.toString()
            tvBet.text = storage.lastBetGame2.toString()

            ivBack.setOnClickListener {
                findNavController().navigateSafe(
                    SlotsGame2FragmentDirections.actionSlotsGame2FragmentToMainFragment()
                )
            }

            viewModel.winned.observe(viewLifecycleOwner) { win ->
                if (win.handled) return@observe
                playWin()
                vibrate()
                storage.lastWinGame1 = win.data
                tvWin.text = win.data.toString()
            }

            viewModel.totalBalance.observe(viewLifecycleOwner) { balance ->
                storage.balance = balance
                if (storage.balance <= tvBet.text.toString().toLong()) {
                    tvBet.text = storage.balance.toString()
                }
                tvBalance.text = storage.balance.toString()
            }

            ivPlus.setOnClickListener {
                var bet = tvBet.text.toString().toLong() + 100L
                if (bet > storage.balance) bet = storage.balance
                storage.lastBetGame2 = bet
                tvBet.text = bet.toString()
            }

            ivMinus.setOnClickListener {
                var bet = tvBet.text.toString().toLong() - 100L
                if (bet < 0) bet = 0
                storage.lastBetGame2 = bet
                tvBet.text = bet.toString()
            }

            btPlay.setOnClickListener {
                val betAmount = tvBet.text.toString().toLong()
                if (betAmount == 0L) return@setOnClickListener
                viewModel.spin(
                    betAmount,
                    storage,
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.slot1.observe(viewLifecycleOwner) {
            binding.sv1.update(it, SlotGame2ViewModel.VISIBLE_AMOUNT_IN_SLOT.toInt())
        }
        viewModel.slot2.observe(viewLifecycleOwner) {
            binding.sv2.update(it, SlotGame2ViewModel.VISIBLE_AMOUNT_IN_SLOT.toInt())
        }
        viewModel.slot3.observe(viewLifecycleOwner) {
            binding.sv3.update(it, SlotGame2ViewModel.VISIBLE_AMOUNT_IN_SLOT.toInt())
        }
        viewModel.slot4.observe(viewLifecycleOwner) {
            binding.sv4.update(it, SlotGame2ViewModel.VISIBLE_AMOUNT_IN_SLOT.toInt())
        }
        viewModel.slot5.observe(viewLifecycleOwner) {
            binding.sv5.update(it, SlotGame2ViewModel.VISIBLE_AMOUNT_IN_SLOT.toInt())
        }
    }

    override fun onStop() {
        super.onStop()
        setPortrait()
    }
}
