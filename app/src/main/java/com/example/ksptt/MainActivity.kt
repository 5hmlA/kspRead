package com.example.ksptt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import android.view.Menu
import android.view.MenuItem
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import com.example.kspt.Testt

@Testt(value = "MainActivity", data = 100)
open class MainActivity : AppCompatActivity() {

    @Testt
    private lateinit var appBarConfiguration: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface {
                Column {
                    Text(text = "你")
                    Text(text = "好")
                    Text(text = "世")
                    Text(text = "界")

                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
    @Testt
    override fun onSupportNavigateUp(): Boolean {
        return false
    }
    @Testt
    fun testAnnInTest():String{ return "" }
    @Testt
    fun testAnnInTest2(name:String){ }
}

