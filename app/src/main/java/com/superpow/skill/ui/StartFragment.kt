package com.superpow.skill.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.superpow.skill.databinding.FragmentStartBinding
import com.superpow.skill.ui.core.BaseFragment
import com.superpow.skill.ui.core.Storage
import com.superpow.skill.ui.extensions.navigateSafe


class StartFragment: BaseFragment<FragmentStartBinding>(
    FragmentStartBinding::inflate
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btGo.setOnClickListener {
            if (Storage.getInstance(requireActivity()).isUserSigned) {
                findNavController().navigateSafe(
                    StartFragmentDirections.actionStartFragmentToMainFragment()
                )
            } else {
                Storage.getInstance(requireActivity()).balance = 5000
                findNavController().navigateSafe(
                StartFragmentDirections.actionStartFragmentToLoginFragment()
                )
            }
        }
    }
}