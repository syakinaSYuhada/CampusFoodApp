package com.campus.foodorder.ui.vendor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.campus.foodorder.R

// Lab: Fragment (Phase 4)
// Purpose: Vendor dashboard placeholder - can navigate back using back button
class VendorDashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vendor_dashboard, container, false)
    }
}
