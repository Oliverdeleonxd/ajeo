package com.example.ajeo.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ajeo.EditProfileActivity;
import com.example.ajeo.Inicio;
import com.example.ajeo.Models.ModelPost;
import com.example.ajeo.PostDetailActivity;
import com.example.ajeo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PostAdapter extends FirebaseRecyclerAdapter<ModelPost,PostAdapter.MyHolder> {


    ModelPost myPost;
    Context context;
//    List<ModelPost> postList;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Activity activity;
    public PostAdapter(@NonNull FirebaseRecyclerOptions<ModelPost> options, Activity activity) {
        super(options);
//        this.context = context;
        this.activity= activity;
//        this.postList = postList;
    }
    

    @Override
    protected void onBindViewHolder(@NonNull MyHolder holder, int position, ModelPost model) {

//


//        String Titulo = postList.get(position).getpTitulo();
//        String Precio = postList.get(position).getpPrecio();
//        String pRaza = postList.get(position).getPRaza();
//
//        String pImg = postList.get(position).getpImage();

        ///Obtener fecha
//        Long time = model.getTimestamp();
        String timee = model.getTimestamp();
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
//        calendar.setTimeInMillis(Long.parseLong(timee));
        String ptime = DateFormat.format("dd/MM/yyyy HH:mm aa",calendar).toString();
        ///////////////////////////////////////////////////////////////////


//        String dataTime = String.valueOf(format("MMMM d, h:mm a", cal));
//        String ptime = DateFormat.format("dd/MM/yyyy HH:mm aa",calendar).toString();



        holder.pTitulo.setText(model.getTitulo());
        holder.pPrecio.setText(model.getPrecio());
        holder.pRaza.setText(model.getRaza());

//        holder.pImage.setImageURI(model.getpImage());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(activity, "Like", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(activity.getApplicationContext(), EditProfileActivity.class);
                Intent intent=new Intent(activity, PostDetailActivity.class);
                intent.putExtra("timestamp",timee);
                v.getContext().startActivity(intent);
                //  v.getContext().startActivity(new Intent(activity, PostDetailActivity.class));
            }
        });

        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "Like", Toast.LENGTH_SHORT).show();

            }
        });

        String ruta = model.getRuta();
        Picasso.get().load(ruta).placeholder(R.drawable.logoajeo).into(holder.pImage);
        try {
            Picasso.get().load(ruta).placeholder(R.drawable.logoajeo).into(holder.pImage);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerhome_row,parent,false);
        return new MyHolder(view);

    }



     class MyHolder extends  RecyclerView.ViewHolder{
        TextView pTitulo,pPrecio,pRaza;
        ImageButton fav;
        ImageView pImage;
        CardView cardView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            pTitulo =itemView.findViewById(R.id.tv_title_row);
            pPrecio = itemView.findViewById(R.id.tv_precio_row);
            pRaza = itemView.findViewById(R.id.tv_Raza_row);

            fav = itemView.findViewById(R.id.tv_imgFav_row);
            pImage = itemView.findViewById(R.id.img_row);


        }
    }//MyHolder


}
