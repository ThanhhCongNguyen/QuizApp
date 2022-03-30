package com.example.quizgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.quizgame.databinding.ActivityHistoryBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    ActivityHistoryBinding binding;
    ArrayList<History> histories;
    HistoryAdapter historyAdapter;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.historyToolbar);

        database = FirebaseFirestore.getInstance();

        histories = new ArrayList<>();
        historyAdapter = new HistoryAdapter(this, histories);

        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("History")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot snapshot : queryDocumentSnapshots){
                    History history = snapshot.toObject(History.class);
                    histories.add(history);
                }

                historyAdapter.notifyDataSetChanged();
            }
        });

        binding.recyclerviewLslb.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerviewLslb.setAdapter(historyAdapter);


    }
}