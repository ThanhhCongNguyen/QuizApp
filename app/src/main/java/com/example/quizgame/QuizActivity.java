package com.example.quizgame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizgame.databinding.ActivityQuizBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    ActivityQuizBinding binding;
    ArrayList<Question> questions;
    int index = 0;
    Question question;
    CountDownTimer timer;
    FirebaseFirestore database;
    int correctAnswer = 0;
    String catId;
    String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseFirestore.getInstance();
        catId = getIntent().getStringExtra("catId");
        categoryName = getIntent().getStringExtra("categoryName");

       // final String catIdAgain = getIntent().getStringExtra("catIdAgain");

        Random random = new Random();
        final int rand = random.nextInt(12);

//        database
//                .collection("categories")
//                .document(catId)
//                .collection("questions")
//                .whereGreaterThanOrEqualTo("index", rand)
//                .orderBy("index")
//                .limit(5).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                if(queryDocumentSnapshots.getDocuments().size() < 5){
//
//                    database
//                            .collection("categories")
//                            .document(catId)
//                            .collection("questions")
//                            .whereLessThanOrEqualTo("index", rand)
//                            .orderBy("index")
//                            .limit(5).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                        @Override
//                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                            for (DocumentSnapshot snapshot : queryDocumentSnapshots){
//                                Question question = snapshot.toObject(Question.class);
//                                questions.add(question);
//                            }
//                            setNextQuestion();
//                        }
//                    });
//                }else {
//                    for (DocumentSnapshot snapshot : queryDocumentSnapshots){
//                        Question question = snapshot.toObject(Question.class);
//                        questions.add(question);
//                    }
//                }
//            }
//        });
        questions = new ArrayList<>();

        database.collection("categories")
                .document(catId)
                .collection("questions")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.isEmpty()){
                    Log.d("Firebase", "List is empty");
                    return;
                }else {
                    for(DocumentSnapshot snapshot : queryDocumentSnapshots){
                        Question question = snapshot.toObject(Question.class);
                        questions.add(question);
                    }
                    Collections.shuffle(questions);
                    setNextQuestion();
                    //Log.d("Fire", queryDocumentSnapshots.toString());
                }
            }
        });

//        database.collection("categories")
//                .document(catId)
//                .collection("questions")
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        for(DocumentSnapshot snapshot : value.getDocuments()){
//                            Question question = snapshot.toObject(Question.class);
//                            questions.add(question);
//                        }
//                    }
//                });

        resetTimer();
        setNextQuestion();
    }

    void setNextQuestion(){

        if(timer != null){
            timer.cancel();
        }

        timer.start();
        if(index < questions.size()){
            binding.questionCount.setText(String.format("%d/%d",(index+1),questions.size()));
            question = questions.get(index);
            binding.question.setText(question.getQuestion());
            Collections.shuffle(question.getOptions());
            binding.option1.setText(question.getOptions().get(0));
            binding.option2.setText(question.getOptions().get(1));
            binding.option3.setText(question.getOptions().get(2));
            binding.option4.setText(question.getOptions().get(3));

        }
    }

    void resetTimer(){
        timer = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long l) {
                binding.txtTime.setText(String.valueOf(l/1000));
            }

            @Override
            public void onFinish() {

            }
        };
    }

    void checkAnswer(TextView textView){
        String selectedAnswer = textView.getText().toString();
        if(selectedAnswer.equals(question.getAnswer())){
            correctAnswer++;
            textView.setBackground(getResources().getDrawable(R.drawable.option_right));
        }else {
            showAnswer();
            textView.setBackground(getResources().getDrawable(R.drawable.option_wrong));
        }
    }

    void reset(){
        binding.option1.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option2.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option3.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option4.setBackground(getResources().getDrawable(R.drawable.option_unselected));
    }

    void showAnswer(){
        if(question.getAnswer().equals(binding.option1.getText().toString())){
            binding.option1.setBackground(getResources().getDrawable(R.drawable.option_right));
        }else if(question.getAnswer().equals(binding.option2.getText().toString())){
            binding.option2.setBackground(getResources().getDrawable(R.drawable.option_right));
        }else if(question.getAnswer().equals(binding.option3.getText().toString())){
            binding.option3.setBackground(getResources().getDrawable(R.drawable.option_right));
        }else if(question.getAnswer().equals(binding.option4.getText().toString())){
            binding.option4.setBackground(getResources().getDrawable(R.drawable.option_right));
        }
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.option_1:
            case R.id.option_2:
            case R.id.option_3:
            case R.id.option_4:
                if(timer != null){
                    timer.cancel();
                }
                TextView selected = (TextView) view;
                checkAnswer(selected);
                break;
            case R.id.next_btn:
                if(index < questions.size() - 1){
                    reset();
                    index++;
                    setNextQuestion();
                }else {
                    Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                    intent.putExtra("catId", catId);
                    intent.putExtra("categoryName", categoryName);
                    intent.putExtra("correct", correctAnswer);
                    intent.putExtra("total", questions.size());
                    startActivity(intent);
                    finish();
                }
                break;
            }
    }

}