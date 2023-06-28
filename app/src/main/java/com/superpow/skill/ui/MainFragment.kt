package com.superpow.skill.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.superpow.skill.databinding.FragmentMainBinding
import com.superpow.skill.ui.core.BaseFragment
import com.superpow.skill.ui.extensions.navigateSafe


class MainFragment: BaseFragment<FragmentMainBinding>(
    FragmentMainBinding::inflate
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btGame1.setOnClickListener {
                findNavController().navigateSafe(
                    MainFragmentDirections.actionMainFragmentToSlotsGame1Fragment()
                )
            }
            btGame2.setOnClickListener {
                findNavController().navigateSafe(
                    MainFragmentDirections.actionMainFragmentToSlotsGame2Fragment()
                )
            }
            btGame3.setOnClickListener {
                findNavController().navigateSafe(
                    MainFragmentDirections.actionMainFragmentToGameRoulette3Fragment()
                )
            }
            btSettings.setOnClickListener {
                findNavController().navigateSafe(
                    MainFragmentDirections.actionMainFragmentToSettingsFragment()
                )
            }
            tvExit.setOnClickListener {
                requireActivity().finish()
            }
            tvPrivacy.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/"))
                startActivity(browserIntent)
            }
        }
    }
}