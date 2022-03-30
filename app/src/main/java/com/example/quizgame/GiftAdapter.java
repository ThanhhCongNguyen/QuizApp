package com.example.quizgame;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.GiftViewHolder> {

    Context context;
    ArrayList<Gift> giftArrayList;
    long coins;

    public GiftAdapter(Context context, ArrayList<Gift> giftArrayList, long coins) {
        this.context = context;
        this.giftArrayList = giftArrayList;
        this.coins = coins;
    }

    @NonNull
    @Override
    public GiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_gift, parent, false);
        return new GiftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftViewHolder holder, int position) {
        Gift gift = giftArrayList.get(position);
        holder.nameGiftText.setText(gift.getGiftName());
        holder.priceGiftText.setText(String.valueOf(gift.getGiftPrice()));

        Glide
                .with(context)
                .load(gift.getGiftImage())
                .into(holder.imageGift);
        holder.selectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Đổi quà")
                        .setMessage("Bạn có muốn đổi quà này không?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                if(coins >= gift.getGiftPrice()){
//                                    Toast.makeText(context, "Successfully", Toast.LENGTH_LONG).show();
//                                }else {
//                                    Toast.makeText(context, "Fail", Toast.LENGTH_LONG).show();
//                                }
                               // Toast.makeText(context, coins+"", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return giftArrayList.size();
    }

    class GiftViewHolder extends RecyclerView.ViewHolder{
        TextView nameGiftText, priceGiftText, selectText;
        ImageView imageGift;
        public GiftViewHolder(@NonNull View itemView) {
            super(itemView);

            nameGiftText = itemView.findViewById(R.id.name_gift_text);
            priceGiftText = itemView.findViewById(R.id.price_gift_text);
            selectText = itemView.findViewById(R.id.select_text);
            imageGift = itemView.findViewById(R.id.image_gift);

        }
    }

}
