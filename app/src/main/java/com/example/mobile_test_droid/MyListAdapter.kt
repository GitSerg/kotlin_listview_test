package com.example.mobile_test_droid

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*
class MyListAdapter(private val context: Activity, private val title: Array<String>, private val phone: Array<String>, private val height: Array<Number>)
    : ArrayAdapter<String>(context, R.layout.custom_list, title) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom_list, null, true)

        val titleText = rowView.findViewById(R.id.title) as TextView
        val subtitleText = rowView.findViewById(R.id.description) as TextView
        val heightText = rowView.findViewById(R.id.height) as TextView

//        titleText.text = title[position]
//        subtitleText.text = description[position]
//        val person: Person = persons[position]
        titleText.setText(title[position])
        subtitleText.setText(phone[position])
        heightText.setText(height[position].toString())

        return rowView
    }
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: Filter.FilterResults) {
                mPois = filterResults.values
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): Filter.FilterResults {
                val queryString = charSequence?.toString()?.toLowerCase()

                val filterResults = Filter.FilterResults()
                filterResults.values = if (queryString==null || queryString.isEmpty())
                    allPois
                else
                    allPois.filter {
                        it.name.toLowerCase().contains(queryString) ||
                                it.city.toLowerCase().contains(queryString) ||
                                it.category_name.toLowerCase().contains(queryString)
                    }
                return filterResults
            }
        }
    }
}