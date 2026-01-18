package com.campus.foodorder.ui.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.campus.foodorder.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

// Lab: Login with authentication
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

        val etUsername = view.findViewById<TextInputEditText>(R.id.etUsername)
        val etPassword = view.findViewById<TextInputEditText>(R.id.etPassword)
        val btnLogin = view.findViewById<MaterialButton>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter username and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Simple authentication (in production, use proper auth service)
            when {
                username == "student" && password == "pass123" -> {
                    saveUserSession("student", "student@campus.edu")
                    findNavController().navigate(R.id.action_login_to_student)
                }
                username == "vendor" && password == "pass123" -> {
                    saveUserSession("vendor", "vendor1")
                    findNavController().navigate(R.id.action_login_to_vendor)
                }
                else -> {
                    Toast.makeText(requireContext(), "Invalid credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveUserSession(role: String, userId: String) {
        val prefs = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putString("role", role)
            putString("userId", userId)
            putBoolean("isLoggedIn", true)
            apply()
        }
    }
}
