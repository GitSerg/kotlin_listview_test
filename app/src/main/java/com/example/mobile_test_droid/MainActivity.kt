package com.example.mobile_test_droid

//import android.R
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
//import android.view.LayoutInflater
//import android.widget.ArrayAdapter
import android.view.View
import androidx.annotation.RequiresApi
//import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
fun parseDate(date: String): String {
    val newDate = LocalDate.parse(date.substring(0, 10), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    newDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    return newDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
}
class MainActivity : AppCompatActivity() {
    private lateinit var mSearchInput: EditText
//    private lateinit var mLoadButton: Button
    private lateinit var mListView: ListView
    private var count: Int = 0
    private lateinit var dataPersons: MutableList<Person>
    private lateinit var handler: Handler
    private fun readRawTextFile(context: Context, resId: Int): String? {
        val inputStream = context.resources.openRawResource(resId)
        val inputReader = InputStreamReader(inputStream)
        val buffReader = BufferedReader(inputReader)
        var line: String?
        val builder = StringBuilder()
        try {
            while (buffReader.readLine().also { line = it } != null) {
                builder.append(line)
                builder.append("\n")
            }
        } catch (e: IOException) {
            return null
        }
        return builder.toString()
    }
    fun loadData(context: Context) {
        val gson = Gson()
        var fileId: Int = context.getResources().getIdentifier("generated01", "raw", getPackageName())
        var data = readRawTextFile(context, fileId)
        val listPersonType = object : TypeToken<MutableList<Person>>() {}.type

        dataPersons = gson.fromJson(data, listPersonType)
        fileId = context.getResources().getIdentifier("generated02", "raw", getPackageName())
        data = readRawTextFile(context, fileId)
        dataPersons.union(gson.fromJson(data, listPersonType))
        fileId = context.getResources().getIdentifier("generated03", "raw", getPackageName())
        data = readRawTextFile(context, fileId)
        dataPersons.union(gson.fromJson(data, listPersonType))
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handler = Handler()
        val handlerLoadData = Handler()
        mSearchInput = findViewById(R.id.search_input)
        val context: Context = getBaseContext()
        handlerLoadData.postDelayed(Runnable{
            loadData(context)
            val pb: ProgressBar = findViewById(R.id.progressBar)
            pb.setVisibility(View.GONE)
            Log.i("mactivity dataPersons", "${dataPersons.size}")
//        dataPersons.forEachIndexed { idx, person -> Log.i("data", "> Item $idx:\n$person") }
            mListView = findViewById(R.id.person_list)
            val adapter = MyListAdapter(this, R.layout.custom_list, dataPersons)
            mListView.setAdapter(adapter)
            mListView.setOnItemClickListener {
                    parent: AdapterView<*>?, view: View?,
                    position: Int, id: Long ->
                val person = dataPersons[position]
                val intent = Intent(this, PersonActivity::class.java)
//            Log.i("mactivity p name", person.name)
                intent.putExtra(PersonActivity.NAME, person.name)
                intent.putExtra(PersonActivity.PHONE, person.phone)
                intent.putExtra(PersonActivity.BIO, person.biography)
                val ePeriod = person.educationPeriod
                intent.putExtra(PersonActivity.PERIOD, "${parseDate(ePeriod.start)} - ${parseDate(ePeriod.end)}")
                intent.putExtra(PersonActivity.TEMPERAMENT, person.temperament)
                startActivity(intent)
//            Toast.makeText(this, "Position: ${position}: ${dataPersons[position]}", Toast.LENGTH_LONG).show()
            }
            mSearchInput.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    Log.i("mactivity ontextchanged", "${s}")
                    adapter.getFilter().filter(s)
                }
                override fun afterTextChanged(s: Editable?) {
                }
            })
        }, 1000L)
        val mCloseButton: ImageButton = findViewById(R.id.drop_search_button)
        mCloseButton.setOnClickListener {
            mSearchInput.setText("")
        }
        val mSwipeRefreshLayout: SwipeRefreshLayout = findViewById(R.id.swiperefresh)
        mSwipeRefreshLayout.setOnRefreshListener {
            handler.postDelayed(Runnable {
                Snackbar.make(findViewById(R.id.content_main_layout), "Нет подключения сети", Snackbar.LENGTH_LONG).show()
                mSwipeRefreshLayout.isRefreshing = false
            }, 3000.toLong())
        }
    }
}