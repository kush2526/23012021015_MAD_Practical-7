package com.example.a23012021015_mad_practical_7.db

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.a23012021015_mad_practical_7.db.DatabaseHelper
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import com.example.a23012021015_mad_practical_7.R



class MainActivity : AppCompatActivity() {
    val PersonList = ArrayList<Person>()
    lateinit var PersonRecyclerAdapter: PersonAdapter
    lateinit var db: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        db = DatabaseHelper(applicationContext)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView1)
        PersonRecyclerAdapter = PersonAdapter(this, PersonList)
        recyclerView.adapter = PersonRecyclerAdapter
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            networkDb()
        }
    }
    fun deletePerson(position: Int){
        val deletedPerson = PersonList[position]
        db.deletePerson(deletedPerson)
        PersonList.removeAt(position)
        PersonRecyclerAdapter.notifyItemRemoved(position)
        Toast.makeText(this, "${deletedPerson.name} deleted", Toast.LENGTH_LONG).show()
    }

    private fun networkDb() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val data = HttpRequest().makeServiceCall(
                    "https://api.json-generator.com/templates/nTfXxxiB-I4-/data",
                    "3wuah1sx3q89txhx8g1s10stic8hd2ro8i7ys9h0"
                )

                withContext(Dispatchers.Main) {
                    if (data != null) {
                        getPesonDetailsFromJson(data)
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getPesonDetailsFromJson(data: String) {
        val size = PersonList.size
        PersonList.clear()
        PersonRecyclerAdapter.notifyItemRangeRemoved(0, size)
        try {
            val jsonArray = JSONArray(data)
            for (i in 0 until  jsonArray.length()) {
                val jsonObject = jsonArray[i] as JSONObject
                val person = Person(jsonObject)
                PersonList.add(person)
                try {
                    if (db.getPerson(person.id) == null)
                        db.updatePerson(person)
                    else
                        db.insertPerson(person)
                }catch (e: Exception){
                    e.printStackTrace()
                }

            }
            PersonRecyclerAdapter.notifyItemRangeInserted(0, PersonList.size)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Toast.makeText(this, "Data fetched from network", Toast.LENGTH_LONG).show()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_sqlitedb -> {
                getPesonDetailsFromJson(db.allPersons.toString())
                true
            }
            R.id.action_nwdb -> {
                networkDb()
                true
            }
            else -> super.onOptionsItemSelected(item)

        }

    }

}