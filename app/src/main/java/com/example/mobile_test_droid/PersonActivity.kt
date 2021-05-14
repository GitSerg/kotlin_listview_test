package com.example.mobile_test_droid

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class PersonActivity : AppCompatActivity() {
    val REQUEST_PHONE_CALL = 1
    companion object {
        const val NAME = "person_name"
        const val PHONE = "person_phone"
        const val BIO = "person_bio"
        const val TEMPERAMENT = "person_temperament"
        const val PERIOD = "person_period"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.person)
        setupPermissions()
        val personName = intent.getStringExtra(NAME)
        val personPhone = intent.getStringExtra(PHONE)
        val personBio = intent.getStringExtra(BIO)
        val personTemperament = intent.getStringExtra(TEMPERAMENT)
        val personPeriod = intent.getStringExtra(PERIOD)
        Log.i("mactivity:sa pname", "${personName}")
        val mPersonName: TextView = findViewById(R.id.person_name)
        mPersonName.setText(personName)
        val mPersonPhone: TextView = findViewById(R.id.person_phone)
        mPersonPhone.setText(personPhone)
        val mPersonBio: TextView = findViewById(R.id.person_bio)
        mPersonBio.setText(personBio)
        val mPersonTemperament: TextView = findViewById(R.id.person_temperament)
        mPersonTemperament.setText(personTemperament)
        val mPersonPeriod: TextView = findViewById(R.id.person_educationPeriod)
        mPersonPeriod.setText(personPeriod)
        val mBackButton: ImageView = findViewById(R.id.person_back)
        mPersonPhone.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_CALL);
            callIntent.data = Uri.parse("tel:$personPhone")
            startActivity(callIntent)
        }
        mBackButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
//            count++
////            mSearchInput.setText("count ${count}")
//            val inputText: String = mSearchInput.getText().toString()
//            val person = dataPersons.find { it.name.contains(inputText, ignoreCase = true) }
////            val person = dataPersons.get(count)
//            Log.i("mactivity countdata", "${count} : ${person?.name} ${person?.id}")
//            val t = Toast.makeText(applicationContext, person?.temperament, Toast.LENGTH_LONG)
//            t.show()
        }
    }
    fun setupPermissions() {
        val permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("mactivity callError", "Permission to call denied")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_PHONE_CALL)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PHONE_CALL) {
            val callIntent = Intent(Intent.ACTION_CALL);
            callIntent.data = Uri.parse("tel:12345678")
            startActivity(intent)
        }
    }
}