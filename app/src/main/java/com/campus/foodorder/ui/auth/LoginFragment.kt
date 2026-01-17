package com.campus.foodorder.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.campus.foodorder.R

// Lab: Fragment with Navigation (Phase 4)
// Purpose: Login screen with role selection - navigates using NavController
class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rgUserRole = view.findViewById<RadioGroup>(R.id.rgUserRole)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            when (rgUserRole.checkedRadioButtonId) {
                R.id.rbStudent -> {
                    findNavController().navigate(R.id.action_login_to_student)
                }
                R.id.rbVendor -> {
                    findNavController().navigate(R.id.action_login_to_vendor)
                }
                else -> {
                    Toast.makeText(requireContext(), "Please select a role", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
