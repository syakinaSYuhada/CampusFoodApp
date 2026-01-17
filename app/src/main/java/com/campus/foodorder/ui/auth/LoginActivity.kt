package com.campus.foodorder.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.campus.foodorder.R
import com.campus.foodorder.ui.student.StudentDashboardActivity
import com.campus.foodorder.ui.vendor.VendorDashboardActivity

// Lab: Activities & Intents, SharedPreferences (Phase 1)
// Purpose: Entry point for Student/Vendor login and role selection
class LoginActivity : AppCompatActivity() {
    private lateinit var rgUserRole: RadioGroup
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        rgUserRole = findViewById(R.id.rgUserRole)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val selectedRoleId = rgUserRole.checkedRadioButtonId
            if (selectedRoleId == -1) {
                Toast.makeText(this, "Please select a role", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedRole = findViewById<RadioButton>(selectedRoleId).text.toString()
            navigateToDashboard(selectedRole)
        }
    }

    private fun navigateToDashboard(role: String) {
        val intent = when (role.lowercase()) {
            "student" -> Intent(this, StudentDashboardActivity::class.java)
            "vendor" -> Intent(this, VendorDashboardActivity::class.java)
            else -> return
        }
        startActivity(intent)
        finish()
    }
}
