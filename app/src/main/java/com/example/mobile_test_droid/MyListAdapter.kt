package com.example.mobile_test_droid

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.LayoutRes
// https://spapas.github.io/2019/04/05/android-custom-filter-adapter/
class MyListAdapter(private val context: Activity, @LayoutRes private val layoutResource: Int, private val allPersons: List<Person>)
    : ArrayAdapter<Person>(context, layoutResource, allPersons), Filterable {
    private var mPersons: List<Person> = allPersons
    override fun getCount(): Int {
        return mPersons.size
    }
    override fun getItem(p0: Int): Person? {
        return mPersons.get(p0)
    }
//    override fun getItemId(p0: Int): Long {
//        return mPersons.get(p0).id.toLong()
//    }
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom_list, null, true)

        val titleText = rowView.findViewById(R.id.title) as TextView
        val subtitleText = rowView.findViewById(R.id.description) as TextView
        val heightText = rowView.findViewById(R.id.height) as TextView
        val person = mPersons[position]
        titleText.setText(person.name)
        subtitleText.setText(person.phone)
        heightText.setText(person.height.toString())

        return rowView
    }
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: Filter.FilterResults) {
                mPersons = filterResults.values as List<Person>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): Filter.FilterResults {
                val queryString = charSequence?.toString()?.toLowerCase()

                val filterResults = Filter.FilterResults()
                filterResults.values = if (queryString==null || queryString.isEmpty())
                    allPersons
                else
                    allPersons.filter {
                        it.name.toLowerCase().contains(queryString) ||
                                it.phone.toLowerCase().contains(queryString)
                    }
                return filterResults
            }
        }
    }
}