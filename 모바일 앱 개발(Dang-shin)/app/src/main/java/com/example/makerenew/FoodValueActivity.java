package com.example.makerenew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FoodValueActivity extends AppCompatActivity {
    ProgressBar progressBar; // 전체 칼로리 수치 바
    TextView total_food_cal;
    TextView max_food_cal;

    Food morning;
    Food lunch;
    Food dinner;
    Food snack;

    // 곡률, 어육류, 채소, 지방, 우유, 과일 순
    TextView district1;
    TextView district2;
    TextView district3;
    TextView district4;
    TextView district5;
    TextView district6;
    RecyclerView recyclerView;
    FoodAdapter adapter;

    // DB 에서 불러온 값을 여기에 저장해 주세요 @@@@@@@@@@@@@@@@@@@@@@@@@@
    int totalCal = 0;
    int maxCal = 0;

    public String id = "Shin"; // 추후 DB의 아이디로 변경해주세요. @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    // 출력 형식 지정
    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");

    // 날짜 저장 변수
    String todayDate = new String();

    // 파이어베이스 연결
    private DatabaseReference mDatabase;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference conditionRef = mRootRef.child("User").child(id).child("Last_Login");

    String[] mealTime = {"breakfast", "lunch", "dinner", "snack"};
    String[] mealAttribute = {"classification", "meal_tan", "meal_dan", "meal_ge", "meal_cal",
            "meal_food1", "meal_food2", "meal_food3", "meal_food4", "meal_food5",
            "meal_food1_weight", "meal_food2_weight", "meal_food3_weight", "meal_food4_weight", "meal_food5_weight",
            "district1", "district2", "district3", "district4", "district5", "district6"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_value);

        // 뒤로가기 버튼 구현
        ImageButton button = findViewById(R.id.backButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 등록 버튼 구현
        TextView adding_food = findViewById(R.id.food_register);
        adding_food.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:{
                        Log.d("textview : ","등록버튼 눌려짐");
                        Intent intent = new Intent(getApplicationContext(), FoodSelectActivity.class);
                        startActivity(intent);
                    }
                    default: return false;
                }
            }
        });

        progressBar = findViewById(R.id.progress_bar);
        total_food_cal = findViewById(R.id.total_food_cal);
        max_food_cal = findViewById(R.id.max_food_cal);

        // 리사이클러 뷰 생성
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new FoodAdapter();

        adapter.addItem(new Food("아침"));
        adapter.addItem(new Food("점심"));
        adapter.addItem(new Food("저녁"));
        adapter.addItem(new Food("간식"));

        morning = adapter.getItem(0);
        lunch = adapter.getItem(1);
        dinner = adapter.getItem(2);
        snack = adapter.getItem(3);

        // 금일 최초 로그인 시 DB의 초기 값 생성
        mDatabase = FirebaseDatabase.getInstance().getReference();
        todayDate = simpleDate.format(new Date());
        initDB();

        // 각 변수에 값 입력 이런식으로 사용하시면 됩니다 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        // 최초 DB의 값을 부르는 위치
        // ----- 각 시간대별 데이터 영역 ------
//        dataSync();

        // ----- total 영역 ------
        totalCal = 5000;
        maxCal = 14000;


        setValues();
    }

    // 한 번에 개인의 아 점 저 간식의 정보를 다 불러옵니다. 이걸 전처리하는 방법을 찾아서 사용하는 것도 괜찮을 것 같습니다.
//    protected void onResume() {
//        super.onResume();
//
//        // 최초 데이터 생성 시간보다 Resume이 먼저 실행되므로 sleep 사용
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ValueEventListener mValueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d("@@@@@@@@@@@@@@@@@@@@@@", dataSnapshot.getValue().toString());
//
//                recyclerView.setAdapter(adapter);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };
//
//        // 변경 사항 발생 시 데이터를 읽어 옴
//        mDatabase.child("User").child(id).child(todayDate).child("food").addListenerForSingleValueEvent(mValueEventListener);
//
//    }

    // 파이어베이스 데이터를 변수에 연동 (일단 읽어오긴 하는데 두줄밖에 못읽어옴 + 순서를 확인이 불가함)
//    public void dataSync() {
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        for (int i = 0; i < 4; i++) {
//            ValueEventListener mValueEventListener = new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    Log.d("@@@@@@@@@@@@@@@@@@@@@@", dataSnapshot.getValue().toString());
//                    //                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
//                    //                    String key = postSnapshot.getKey();
//                    //                    index++;
//                    //                    adapter.getItem(index) = postSnapshot.getValue(Food.class);
//                    //                }
//                    recyclerView.setAdapter(adapter);
//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            };
//
//            // 변경 사항 발생 시 데이터를 읽어 옴
//            mDatabase.child("User").child(id).child(todayDate).child("food").child(mealTime[i]).addListenerForSingleValueEvent(mValueEventListener);
//            Log.d("@@@@@@@@@@@@@@@@@@@@@@", String.valueOf(i));
//
//        }
//    }


    // 음식 Dataset 파이어베이스에 구현
    public void initDB() {
        Log.d("firebase", "userId : " + id);
        Log.d("firebase", "todayDate : " + todayDate);

        conditionRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String lastLogin = snapshot.getValue(String.class);

                        Log.d("firebase", "lastLogin : " + lastLogin);

                        if (lastLogin == null || !lastLogin.equals(todayDate)) { // 오늘 날짜와 최근 로그인 날짜가 다르면
                            Log.d("firebase", "First login today");

                            // Dataset 생성
                            for (int i = 0; i < mealTime.length; i++) {
                                for(int j = 0; j < mealAttribute.length; j++) {
                                    mDatabase.child("User").child(id).child(todayDate).child("food")
                                            .child(mealTime[i]).child(mealAttribute[j]).setValue(0);
                                }
                            }
                            mDatabase.child("User").child(id).child(todayDate).child("food")
                                    .child("totalCal").setValue(0);
                            mDatabase.child("User").child(id).child(todayDate).child("food")
                                    .child("maxCal").setValue(0);

                            // 최근 로그인 날짜 변경
                            conditionRef.setValue(todayDate);
                            Log.d("firebase", "Make dataset successfully.");
                        } else {
                            Log.d("firebase", "Already created dataset.");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("firebase", "onCancelled: call");
                    }
                }
        );
    }

    // progress 및 textView 변경 함수
    // DB를 불러올 때 이 함수를 1회 실행하면 됩니다.
    public void setValues() {
        progressBar.setMax(maxCal);
        progressBar.setProgress(totalCal);
        total_food_cal.setText(Integer.toString(totalCal));
        max_food_cal.setText(Integer.toString(maxCal));
    }
}