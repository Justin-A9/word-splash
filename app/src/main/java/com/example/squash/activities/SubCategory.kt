package com.example.squash.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.squash.databinding.ActivitySubCategoryBinding
import com.example.squash.adapters.SubCategoryAdapter
import com.example.squash.R
import com.example.squash.data.SubUsers
import java.util.*
import kotlin.collections.ArrayList

class SubCategory : AppCompatActivity() {

    val sub_professions = ArrayList<SubUsers>()
    val medicineOccupation = ArrayList<SubUsers>()


    private lateinit var binding: ActivitySubCategoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySubCategoryBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            //startActivity(Intent(this, LandingPage::class.java))
            onBackPressed()
        }
      sub_professions.add(SubUsers("Nursing"))
      sub_professions.add(SubUsers("Nursing"))
      sub_professions.add(SubUsers("Medicine"))
      sub_professions.add(SubUsers("Shoe"))
      sub_professions.add(SubUsers("Nursing"))
      sub_professions.add(SubUsers("Nursing"))
        val recycler = binding.subRecycler
        recycler.layoutManager = LinearLayoutManager(this)


        val intent = intent.extras
        val getData =intent?.get("name")
        binding.text.text = getData.toString()

        val adapter = SubCategoryAdapter(medicineOccupation)

        medicineOccupation.addAll(sub_professions)
        recycler.adapter = adapter


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.recyclermenu, menu)
        val menuItem = menu!!.findItem(R.id.search)

        if (menuItem != null){
            val searchView = menuItem.actionView as SearchView

            searchView.setOnQueryTextListener(object  : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    if (newText!!.isNotEmpty()){

                        medicineOccupation.clear()
                        val search = newText.toLowerCase(Locale.getDefault())
                        sub_professions.forEach {

                            if (it.name.toLowerCase(Locale.getDefault()).contains(search)){
                                medicineOccupation.add(it)
                            }

                        }
                        binding.subRecycler.adapter!!.notifyDataSetChanged()

                    }else{
                        medicineOccupation.clear()
                        medicineOccupation.addAll(sub_professions)
                        binding.subRecycler.adapter!!.notifyDataSetChanged()
                    }
                    return true
                }


            })
        }


        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}