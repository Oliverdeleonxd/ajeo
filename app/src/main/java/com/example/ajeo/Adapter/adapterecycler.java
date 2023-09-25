package com.example.ajeo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ajeo.Models.ModalClass;
import com.example.ajeo.R;

import java.util.List;

public class adapterecycler extends RecyclerView.Adapter<adapterecycler.MyViewHolder>  {

        Context context;
        List<ModalClass> imgList1;

        public adapterecycler(Context context, List<ModalClass> imgList1) {
            this.context = context;
            this.imgList1 = imgList1;
        }

        @NonNull
        @Override
        public adapterecycler.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.img_row,parent,false);

            return new adapterecycler.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull adapterecycler.MyViewHolder holder, int position) {

            holder.textView.setText(imgList1.get(position).getImageName());
//        holder.textView.setText(imgList1.get(position).toString());
            holder.imageView.setImageURI(imgList1.get(position).getImgModel());


        }

        @Override
        public int getItemCount() {
            return imgList1.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView imageView ;
            TextView textView ;


            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.imageView);
                textView = (TextView) itemView.findViewById(R.id.tvImgDesp);



            }
        }


}
