package com.example.ajeo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ajeo.Models.ModelPost;
import com.example.ajeo.PostDetailActivity;
import com.example.ajeo.R;
import com.example.ajeo.SubastaActivity;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SubastaApdapter extends RecyclerView.Adapter<SubastaApdapter.myHolder>{
    Context context;
    List<ModelPost> postList;
    String myUid;
    private DatabaseReference likeRef;
    private DatabaseReference postRef;
    boolean mProcessLike=false ;

    public SubastaApdapter(Context context, List<ModelPost> postList) {
        this.context = context;
        this.postList = postList;

    }


    @NonNull
    @Override
    public SubastaApdapter.myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.recycler_subasta_row,parent,false);

        return new SubastaApdapter.myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SubastaApdapter.myHolder holder, final int position) {

        holder.pTitulo.setText(postList.get(position).getTitulo());

        holder.pRaza.setText(postList.get(position).getRaza());
        holder.pPrecio.setText(postList.get(position).getPrecioInicial());




        final String pImage=postList.get(position).getImage();

        Picasso.get().load(pImage).placeholder(R.drawable.logoajeo).into(holder.pImageSubasta);
        try {
            Picasso.get().load(pImage).placeholder(R.drawable.logoajeo).into(holder.pImageSubasta);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

         String ruta = postList.get(position).getRuta();
////        String ruta = postList.get(position).getRuta();
////        Glide.with(holder.pImage.getContext()).load(ruta).into(holder.pImage);
        Picasso.get().load(ruta).placeholder(R.drawable.logoajeo).into(holder.pImageSubasta);
        try {
            Picasso.get().load(ruta).placeholder(R.drawable.logoajeo).into(holder.pImageSubasta);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        final String pTimeStamp=postList.get(position).getTimestamp();


        final String pId = postList.get(position).getTimestamp();

        Log.d("BusquedaAdapter : 112", " pTimeStamp: "+pTimeStamp);
//
        Calendar calendar=Calendar.getInstance(Locale.getDefault());
//        calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
        String pTime= DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, SubastaActivity.class);
                intent.putExtra("Subasta",pTimeStamp);
                v.getContext().startActivity(intent);
            }
        });

        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "like", Toast.LENGTH_SHORT).show();

            }
        });


//
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class myHolder extends RecyclerView.ViewHolder{



        TextView pTitulo,pPrecio,pRaza;
        ImageButton fav;
        ImageView pImageSubasta,ImageVendedor;

        CardView cardView;

        public myHolder(@NonNull View itemView) {
            super(itemView);

            pTitulo =itemView.findViewById(R.id.tv_title_row);
            pPrecio = itemView.findViewById(R.id.tv_precio_RrowSu);
            pRaza = itemView.findViewById(R.id.tv_Raza_row);

            fav = itemView.findViewById(R.id.tv_imgFav_row);
            pImageSubasta = itemView.findViewById(R.id.img_rowSubasta);
            ImageVendedor = itemView.findViewById(R.id.img_Vendedor);

            cardView = itemView.findViewById(R.id.cardView);

        }
    }
}