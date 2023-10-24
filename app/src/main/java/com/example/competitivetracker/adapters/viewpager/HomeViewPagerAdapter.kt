package com.example.competitivetracker.adapters.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.competitivetracker.R
import com.example.competitivetracker.ui.FriendsFragment
import com.example.competitivetracker.ui.HomeFragment
import com.example.competitivetracker.ui.UserRatingChangesFragment
import com.example.competitivetracker.ui.UserSubmissionsFragment

class HomeViewPagerAdapter(
    fragmentManager: FragmentManager, lifecycle: Lifecycle
): FragmentStateAdapter(fragmentManager, lifecycle) {

    val icons = listOf<Int>(
        R.drawable.ic_home,
        R.drawable.ic_friends,
        R.drawable.ic_rating_changes,
        R.drawable.ic_submissions,
    )
    override fun getItemCount(): Int {
        return icons.size
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> HomeFragment()
            1-> FriendsFragment()
            2->UserRatingChangesFragment()
            else -> UserSubmissionsFragment()
        }
    }

}