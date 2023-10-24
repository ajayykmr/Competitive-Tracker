package com.example.competitivetracker

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.competitivetracker.adapters.viewpager.ContestsViewPagerAdapter
import com.example.competitivetracker.adapters.viewpager.HomeViewPagerAdapter
import com.example.competitivetracker.adapters.viewpager.LeaderboardViewPagerAdapter
import com.example.competitivetracker.adapters.viewpager.SettingsViewPagerAdapter
import com.example.competitivetracker.util.Constants.Companion.API_KEY
import com.example.competitivetracker.util.Constants.Companion.API_SECRET
import com.example.competitivetracker.util.Constants.Companion.USER_ID
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    //used to enable the toggle drawer button on the top left
    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navDrawerView: NavigationView
    private lateinit var toolbar: Toolbar

    private lateinit var viewpager: ViewPager2
    private lateinit var tabLayout: TabLayout

    private  lateinit var homeAdapter: HomeViewPagerAdapter
    private lateinit var leaderboardAdapter: LeaderboardViewPagerAdapter
    private lateinit var settingsAdapter: SettingsViewPagerAdapter
    private lateinit var contestsAdapter: ContestsViewPagerAdapter

    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()

        USER_ID = sharedPref.getString("userID", "").toString()
        API_KEY = sharedPref.getString("apiKey", "").toString()
        API_SECRET = sharedPref.getString("apiSecret", "").toString()

        drawerLayout = findViewById(R.id.drawer_layout)
        navDrawerView = findViewById(R.id.nav_drawer_view)
        toolbar = findViewById(R.id.toolbar)
        viewpager = findViewById(R.id.viewpager)
        tabLayout = findViewById(R.id.tablayout)

        setupNavigationDrawer()
        homeAdapter =  HomeViewPagerAdapter(supportFragmentManager, lifecycle)
        leaderboardAdapter = LeaderboardViewPagerAdapter(supportFragmentManager, lifecycle)
        settingsAdapter = SettingsViewPagerAdapter(supportFragmentManager, lifecycle)
        contestsAdapter = ContestsViewPagerAdapter(supportFragmentManager, lifecycle)

        setupViewPagerTabLayout(homeAdapter, homeAdapter.icons)
    }

    private fun setupViewPagerTabLayout(vpAdapter: FragmentStateAdapter, icons: List<Int> = listOf()){
        viewpager.adapter = vpAdapter
        if (icons.isEmpty()){
            tabLayout.visibility = View.GONE
        } else{
            tabLayout.visibility = View.VISIBLE
            TabLayoutMediator(tabLayout, viewpager) {tab, position ->
                tab.setIcon(icons[position])
            }.attach()
        }


    }

    private fun setupNavigationDrawer() {

        //set custom toolbar
        setSupportActionBar(toolbar)

        //used to enable the toggle drawer button on the top left
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // used to change the toggle button to back button if the drawer is active
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //if the drawer is open, then back button will close the activity instead of closing the drawer
        //to handle that case
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                else
                {
                    finish()
                }
            }
        })

        navDrawerView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nd_mi_home -> {
                    setupViewPagerTabLayout(homeAdapter, homeAdapter.icons)
                }
                R.id.nd_mi_contests -> {
                    setupViewPagerTabLayout(contestsAdapter)
                }
                R.id.nd_mi_leaderboard -> {
                    setupViewPagerTabLayout(leaderboardAdapter)
                }
                R.id.nd_mi_settings -> {
                    setupViewPagerTabLayout(settingsAdapter)
                }

            }

            //to close the drawer after a button has been pressed
            drawerLayout.closeDrawer(GravityCompat.START)

            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)) {
            //for navigation drawer
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}