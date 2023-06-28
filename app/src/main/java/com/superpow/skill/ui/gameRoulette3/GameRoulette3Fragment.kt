package com.superpow.skill.ui.gameRoulette3

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.superpow.skill.databinding.FragmentRouletteGame3Binding
import com.superpow.skill.ui.core.BaseFragment
import com.superpow.skill.ui.core.Storage
import com.superpow.skill.ui.extensions.handleBackPress
import com.superpow.skill.ui.extensions.navigateSafe


class GameRoulette3Fragment: BaseFragment<FragmentRouletteGame3Binding>(
    FragmentRouletteGame3Binding::inflate
) {

    private val storage by lazy {
        Storage.getInstance(requireActivity())
    }
    private val viewModel by viewModels<RouletteGame3ViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleBackPress {
            findNavController().navigateSafe(
                GameRoulette3FragmentDirections.actionGameRoulette3FragmentToMainFragment()
            )
        }

        with(binding) {
            tvBet.text = storage.lastBetGame3.toString()

            ivBack.setOnClickListener {
                findNavController().navigateSafe(
                    GameRoulette3FragmentDirections.actionGameRoulette3FragmentToMainFragment()
                )
            }

            viewModel.totalBalance.observe(viewLifecycleOwner) { balance ->
                storage.balance = balance
                if (storage.balance <= tvBet.text.toString().toLong()) {
                    tvBet.text = storage.balance.toString()
                }
            }

            viewModel.degree.observe(viewLifecycleOwner) { degree ->
                ivRoulette.rotation = degree
            }

            ivPlus.setOnClickListener {
                var bet = tvBet.text.toString().toLong() + 100L
                if (bet > storage.balance) bet = storage.balance
                storage.lastBetGame3 = bet
                tvBet.text = bet.toString()
            }

            ivMinus.setOnClickListener {
                var bet = tvBet.text.toString().toLong() - 100L
                if (bet < 0) bet = 0
                storage.lastBetGame3 = bet
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
}