package com.example.makerenew

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.exercise_list_registration.*

class AddExerciseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exercise_list_registration)

        back_arrow.setOnClickListener() {
            finish()
        }

        val Adding_exercise = Intent(this, ExerciseActivity::class.java)
        exercise_register.setOnClickListener() {

            startActivity(Adding_exercise)
        }
    }
}