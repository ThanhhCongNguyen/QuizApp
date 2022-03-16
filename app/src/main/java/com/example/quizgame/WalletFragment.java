package com.example.quizgame;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.quizgame.databinding.FragmentWalletBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class WalletFragment extends Fragment {

    FragmentWalletBinding binding;
    FirebaseFirestore database;
    User user;
    public WalletFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWalletBinding.inflate(inflater, container, false);
        database = FirebaseFirestore.getInstance();

        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user = documentSnapshot.toObject(User.class);
                        binding.currentCoints.setText(String.valueOf(user.getCoins()));
                    }
                });

        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = FirebaseAuth.getInstance().getUid();
                String payPal = binding.emailAddress.getText().toString();

                WithdrawRequest request = new WithdrawRequest(uid, payPal, user.getName());
                if(user.getCoins() >= 50000){
                    database
                            .collection("withdraws")
                            .document(FirebaseAuth.getInstance().getUid())
                            .set(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getContext(),"Request sent successfully", Toast.LENGTH_LONG).show();
                        }
                    });

                }else {
                    Toast.makeText(getContext(),"You need more coins to get withdraw", Toast.LENGTH_LONG).show();
                }
            }
        });


        return binding.getRoot();
    }
}