package com.superpow.skill.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.superpow.skill.R
import com.superpow.skill.databinding.FragmentSettingsBinding
import com.superpow.skill.ui.core.BaseFragment
import com.superpow.skill.ui.core.Storage
import com.superpow.skill.ui.extensions.handleBackPress
import com.superpow.skill.ui.extensions.navigateSafe


class SettingsFragment: BaseFragment<FragmentSettingsBinding>(
    FragmentSettingsBinding::inflate
) {

    private val storage by lazy {
        Storage.getInstance(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()

        handleBackPress {
            findNavController().navigateSafe(
                SettingsFragmentDirections.actionSettingsFragmentToMainFragment()
            )
        }

        with(binding) {
            ivSoundOn.setOnClickListener {
                storage.sounds = true
                setupViews()
            }
            ivSoundOff.setOnClickListener {
                storage.sounds = false
                setupViews()
            }
            ivVibrateOn.setOnClickListener {
                storage.vibrates = true
                setupViews()
            }
            ivVibrateOff.setOnClickListener {
                storage.vibrates = false
                setupViews()
            }
            tvReset.setOnClickListener {
                storage.balance = 5000
                Toast.makeText(
                    requireContext(),
                    R.string.score_resettled,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setupViews() {
        setupVolumeViews()
        setupVibrateViews()
    }

    private fun setupVolumeViews() {
        if (storage.sounds) {
            binding.ivSoundOn.alpha = 1f
            binding.ivSoundOff.alpha = 0.7f
        } else {
            binding.ivSoundOn.alpha = 0.7f
            binding.ivSoundOff.alpha = 1f
        }
    }

    private fun setupVibrateViews() {
        if (storage.vibrates) {
            binding.ivVibrateOn.alpha = 1f
            binding.ivVibrateOff.alpha = 0.7f
        } else {
            binding.ivVibrateOn.alpha = 0.7f
            binding.ivVibrateOff.alpha = 1f
        }
    }
}