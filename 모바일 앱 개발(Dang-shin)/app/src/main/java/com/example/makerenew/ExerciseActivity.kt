package com.example.makerenew

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.exercise_main.*

class ExerciseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exercise_main)

        exercise_arrow.setOnClickListener() {
            finish()
        }

        val add_list = Intent(this, AddExerciseActivity::class.java)

        exercise_list_add.setOnClickListener() {
            startActivity(add_list)
        }
    }
}