package com.bharathvishal.messagecommunicationusingwearabledatalayer

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView

class Inicio : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inicio)

        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        // Configurar AppBarConfiguration con los destinos de nivel superior
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.cultivoFragment,
                R.id.riegoFragment,
                R.id.fertilizacionFragment,
                R.id.recordatorioFragment,
                R.id.agregarFragment,
                R.id.ajustesFragment
            ), drawerLayout
        )

        // Configurar ActionBar con NavController y AppBarConfiguration
        setupActionBarWithNavController(navController, appBarConfiguration)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setupWithNavController(navController)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)

        if (savedInstanceState == null) {
            navController.navigate(R.id.homeFragment)
            navigationView.setCheckedItem(R.id.nav_inicio)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.fragment_container)
        when (item.itemId) {
            R.id.nav_inicio -> navController.navigate(R.id.homeFragment)
            R.id.nav_cultivos -> navController.navigate(R.id.cultivoFragment)
            R.id.nav_riego -> navController.navigate(R.id.riegoFragment)
            R.id.nav_fertilizacion -> navController.navigate(R.id.fertilizacionFragment)
            R.id.nav_recordatorios -> navController.navigate(R.id.recordatorioFragment)
            R.id.nav_agregar -> navController.navigate(R.id.agregarFragment)
            R.id.nav_ajustes -> navController.navigate(R.id.ajustesFragment)
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment_container)
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
