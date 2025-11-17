package com.example.a23012021015_mad_practical_7.db

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.Serializable
import java.util.ArrayList
import com.example.a23012021015_mad_practical_7.R


class PersonAdapter(private val context: MainActivity, private val array: ArrayList<Person>):
    RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {
    inner class PersonViewHolder(val bindingView: View) : RecyclerView.ViewHolder(bindingView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PersonViewHolder {
        val bindingView =
            LayoutInflater.from(parent.context).inflate(R.layout.person_list_view, parent, false)
        return PersonViewHolder(bindingView)
    }

    override fun onBindViewHolder(
        holder: PersonViewHolder,
        position: Int
    ) {
        with(holder) {
            with(array[position]) {
                bindingView.findViewById<TextView>(R.id.textView_name).text = this.name
                bindingView.findViewById<TextView>(R.id.textView_phone_no).text = this.phoneNo
                bindingView.findViewById<TextView>(R.id.textView_email).text = this.emailId
                bindingView.findViewById<TextView>(R.id.textView_address).text = this.address
                val obj = this as Serializable
                bindingView.findViewById<Button>(R.id.button_delete).setOnClickListener {
                    context.deletePerson(holder.adapterPosition)
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return array.size
    }
}