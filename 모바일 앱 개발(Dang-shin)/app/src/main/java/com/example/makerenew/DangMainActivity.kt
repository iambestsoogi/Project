package com.example.makerenew

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.dang_main.*
import java.text.SimpleDateFormat
import java.util.*

class DangMainActivity : AppCompatActivity() {

    var now_Date = System.currentTimeMillis()
    var current_Date = Date(now_Date)
    var simple_Date = SimpleDateFormat("yyyy.MM.DD")
    var get_Time = simple_Date.format(current_Date)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dang_main)

        // 메뉴버튼
        val sub_menu = Intent(this, SubMainActivity::class.java)
        menu_btn.setOnClickListener() {
            startActivity(sub_menu)
        }

        // 현재시간 기입
        var tv1: TextView = findViewById(R.id.main_date)
        tv1.text = get_Time

        // 혈당 버튼 지정.
        val mglucose = Intent(this, Glucose::class.java)
        showing_glucose.setOnClickListener() {
            startActivity(mglucose)
        }

        // 운동 버튼 지정
        val exercising = Intent(this, ExerciseActivity::class.java)
        showing_exercise.setOnClickListener() {
            startActivity(exercising)
        }

        // 음식 버튼 지정
        val mfood = Intent(this, FoodValueActivity::class.java)
        showing_food.setOnClickListener() {
            startActivity(mfood)
        }

//        var m_glucose
//        var m_exercise
//        값을 받아 메인 화면에 띄워야함. 수정해야할 부분

//        var m_walk_kcal
//        var m_exercise_kcal
//        함수 선언해서 처리하는 것으로 수정.

//        var m_exercise_tim - 운동시간 계산해서 입력받아 표시

//        var exercise_1
//        var exercise_2
//        var exercise_3     -- 운동 목록 리스트 입력 시 바로 입력값 받아서 표시.
    }

}