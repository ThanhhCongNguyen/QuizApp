package com.example.quizgame;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.LSLBViewHolder>{

    Context context;
    ArrayList<History> histories;

    public HistoryAdapter(Context context, ArrayList<History> histories) {
        this.context = context;
        this.histories = histories;
    }

    @NonNull
    @Override
    public LSLBViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_history, parent, false);
        return new LSLBViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LSLBViewHolder holder, int position) {
        History history = histories.get(position);
        holder.name_text.setText(history.getCategoryName());
        holder.correct_text.setText("Correct answer: "+ String.valueOf(history.getCorrectAnswer()) + "/"
                + String.valueOf(history.getTotal()));
        holder.time.setText("Date: " + history.getTime());

        if(history.getCategoryName().equals("History")){
            holder.imageView_lslb.setImageResource(R.drawable.history);
        }else if(history.getCategoryName().equals("Science")){
            holder.imageView_lslb.setImageResource(R.drawable.science);
        }else if(history.getCategoryName().equals("Maths")){
            holder.imageView_lslb.setImageResource(R.drawable.mathematics);
        }else if(history.getCategoryName().equals("English")){
            holder.imageView_lslb.setImageResource(R.drawable.language);
        }else if(history.getCategoryName().equals("Puzzle")){
            holder.imageView_lslb.setImageResource(R.drawable.puzzle);
        }else if(history.getCategoryName().equals("Drama")){
            holder.imageView_lslb.setImageResource(R.drawable.drama);
        }

        float value = (float) history.getCorrectAnswer() / history.getTotal();
        if(value >= 0.8){
            holder.value.setText("GOOD" );
            holder.value.setTextColor(Color.GREEN);
        }else if(value >= 0.5){
            holder.value.setText("MEDIUM");
            holder.value.setTextColor(Color.MAGENTA);
        }else if(value < 0.5){
            holder.value.setText("SAD");
            holder.value.setTextColor(Color.RED);
        }


    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    class LSLBViewHolder extends RecyclerView.ViewHolder{
        //RowLslbBinding binding;
        TextView name_text, correct_text, time, value;
        ImageView imageView_lslb;

        public LSLBViewHolder(@NonNull View itemView) {
            super(itemView);
           // binding = RowLslbBinding.bind(itemView);
            name_text = itemView.findViewById(R.id.categoryName);
            correct_text = itemView.findViewById(R.id.correct_answer);
            time = itemView.findViewById(R.id.time);
            imageView_lslb = itemView.findViewById(R.id.imageView_lslb);
            value = itemView.findViewById(R.id.value);



        }
    }
}
