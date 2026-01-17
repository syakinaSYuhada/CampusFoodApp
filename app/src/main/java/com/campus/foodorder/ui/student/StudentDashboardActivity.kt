package com.campus.foodorder.ui.student

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.campus.foodorder.R

// Lab: Activities, RecyclerView (Phase 1/2)
// Purpose: Student dashboard to access menu, cart, order history
class StudentDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_dashboard)
    }
}