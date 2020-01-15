package com.commonsware.todo2.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.commonsware.todo2.R
import kotlinx.android.synthetic.main.activity_about.*


class MainActivity : AppCompatActivity() {

  private lateinit var appBarConfiguration: AppBarConfiguration

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    setSupportActionBar(toolbar)

    /*
    Create an AppBarConfiguration for the navigation graph managed by that
    NavController
    • Call setupActionBarWithNavController(), to tell the Navigation
    component that we want it to automatically update the app bar based on
    our navigation through our navigation graph
     */
    findNavController(R.id.nav_host).let { nav ->
      appBarConfiguration = AppBarConfiguration(nav.graph)
      setupActionBarWithNavController(nav, appBarConfiguration)
    }


  }

  override fun onSupportNavigateUp() = navigateUp(findNavController(R.id.nav_host), appBarConfiguration)


  override fun onCreateOptionsMenu(menu: Menu?): Boolean {

    menuInflater.inflate(R.menu.actions, menu)

    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {

    if(item.itemId == R.id.about){
      startActivity(Intent(this, AboutActivity::class.java))
      return true
    }

    return super.onOptionsItemSelected(item)
  }

}
