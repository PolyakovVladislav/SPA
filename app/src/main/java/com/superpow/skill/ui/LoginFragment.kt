package com.superpow.skill.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.superpow.skill.R
import com.superpow.skill.databinding.FragmentLoginBinding
import com.superpow.skill.ui.core.BaseFragment
import com.superpow.skill.ui.core.Storage
import com.superpow.skill.ui.extensions.navigateSafe

class LoginFragment : BaseFragment<FragmentLoginBinding>(
    FragmentLoginBinding::inflate,
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            tvWantAnon.setOnClickListener {
                goMain()
            }

            btGo.setOnClickListener {
                if (etMail.text?.matches(Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")) == true) {
                    Storage.getInstance(requireActivity()).isUserSigned = true
                    goMain()
                } else if (etMail.text?.matches(Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")) == false) {
                    Toast.makeText(
                        requireContext(),
                        R.string.email_is_not_valid,
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        }
    }

    private fun goMain() {
        findNavController().navigateSafe(
            LoginFragmentDirections.actionLoginFragmentToMainFragment()
        )
    }
}
