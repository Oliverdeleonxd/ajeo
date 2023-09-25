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
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class BusquedaAdapter extends RecyclerView.Adapter<BusquedaAdapter.myHolder>{
    Context context;
    List<ModelPost> postList;
    String myUid;
    private DatabaseReference likeRef;
    private DatabaseReference postRef;
    boolean mProcessLike=false ;

    public BusquedaAdapter(Context context, List<ModelPost> postList) {
        this.context = context;
        this.postList = postList;
//        myUid= FirebaseAuth.getInstance().getCurrentUser().getUid();
//        likeRef=FirebaseDatabase.getInstance().getReference().child("Likes");
//        postRef=FirebaseDatabase.getInstance().getReference().child("Posts");
    }


    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.recyclerhome_row,parent,false);

        return new myHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final myHolder holder, final int position) {

        holder.pTitulo.setText(postList.get(position).getTitulo());
        holder.pPrecio.setText(postList.get(position).getPrecio());
//        holder.pPrecio
        holder.pRaza.setText(postList.get(position).getRaza());

        String ruta = postList.get(position).getRuta();
//        Glide.with(holder.pImage.getContext()).load(ruta).into(holder.pImage);
        Picasso.get().load(ruta).placeholder(R.drawable.logoajeo).into(holder.pImage);
        try {
            Picasso.get().load(ruta).placeholder(R.drawable.logoajeo).into(holder.pImage);
        } catch (Exception exception) {
            exception.printStackTrace();
        }


//        final String uid=postList.get(position).getUid();
//        String UEmail=postList.get(position).getuEmail();
//        String uName=postList.get(position).getuName();
//        final String pDerscription=postList.get(position).getpDescr();
//        String uDp=postList.get(position).getuDp();
//        final String pTitle=postList.get(position).getpTitle();
//        final String pId=postList.get(position).getpId();
//        final String pImage=postList.get(position).getpImage();
        final String pTimeStamp=postList.get(position).getTimestamp();
//        String pLikes=postList.get(position).getpLikes();
//        String pComments=postList.get(position).getpComments();

        final String pId = postList.get(position).getTimestamp();

        Log.d("BusquedaAdapter : 112", " pTimeStamp: "+pTimeStamp);
//
        Calendar calendar=Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
        String pTime= DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();

//        String timee = model.getTimestamp();
//        Calendar calendar = Calendar.getInstance(Locale.getDefault());
////        calendar.setTimeInMillis(Long.parseLong(timee));
//        String ptime = DateFormat.format("dd/MM/yyyy HH:mm aa",calendar).toString();

//        holder.uNameTv.setText(uName);
//        holder.pTimeTv.setText(pTime);
//        holder.pTitletv.setText(pTitle);
//        holder.pDescriptionTv.setText(pDerscription);
//        holder.pLikeTv.setText(pLikes+" Likes");
//        holder.pCommentsTv.setText(pComments+" Comments");
//
//        setLiked(holder,pId);
//
//        try{
//            Picasso.get().load(uDp).placeholder(R.drawable.logoajeo).into(holder.uPictureIv);
//
//        }catch (Exception e){
//
//        }
//        if (pImage.equals("noImage"))
//        {
//            holder.pImageIv.setVisibility(View.GONE);
//
//        }else {
//            holder.pImageIv.setVisibility(View.VISIBLE);
//            try{
//                Picasso.get().load(pImage).into(holder.pImageIv);
//
//            }catch (Exception e){
//
//            }
//        }


//        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//            @Override
//            public void onClick(View view) {
//                showMoreOption(holder.moreBtn,uid,myUid,pId,pImage);
//            }
//        });



        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(activity, "Like", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(activity.getApplicationContext(), EditProfileActivity.class);
                Intent intent=new Intent(context, PostDetailActivity.class);
                intent.putExtra("timestamp",pTimeStamp);
                v.getContext().startActivity(intent);
                //  v.getContext().startActivity(new Intent(activity, PostDetailActivity.class));
            }
        });

        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "like", Toast.LENGTH_SHORT).show();


                Intent intent=new Intent(context, PostDetailActivity.class);
                intent.putExtra("timestamp",pId);
                context.startActivity(intent);

//                final int pLikes=Integer.parseInt(postList.get(position).getpLikes());
//                mProcessLike=true;
//                final String postIde=postList.get(position).getpId();
//                likeRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (mProcessLike){
//                            if (snapshot.child(postIde).hasChild(myUid)){
//                                postRef.child(postIde).child("pLikes").setValue(""+(pLikes-1));
//                                likeRef.child(postIde).child(myUid).removeValue();
//                                mProcessLike=false;
//                            }
//                            else {
//                                postRef.child(postIde).child("pLikes").setValue(""+(pLikes+1));
//                                likeRef.child(postIde).child(myUid).setValue("Liked");
//                                mProcessLike=false;
//
//                                addToHisNotifiction(""+uid,""+pId,"Liked your post");
//
//                            }
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

            }
        });

//        holder.commentBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(context, PostDetailsActivity.class);
//                intent.putExtra("postId",pId);
//                context.startActivity(intent);
//            }
//        });

//        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                BitmapDrawable bitmapDrawable=(BitmapDrawable)holder.pImageIv.getDrawable();
//                if (bitmapDrawable==null)
//                {
//                    shareTextOnly(pTitle,pDerscription);
//
//                }else {
//                    Bitmap bitmap=bitmapDrawable.getBitmap();
//                    shareImageAndText(pTitle,pDerscription,bitmap);
//
//                }
//
//            }
//        });

//        holder.profileLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(context, ThereProfileActivity.class);
//                intent.putExtra("uid",uid);
//                context.startActivity(intent);
//            }
//        });
//
//        holder.pLikeTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(context, PostLikedByActivity.class);
//                intent.putExtra("postId",pId);
//                context.startActivity(intent);
//
//            }
//        });
//
//    }


//    private void addToHisNotifiction(String hisUid, String pId,String notification){
//
//        String timestamp=""+System.currentTimeMillis();
//        HashMap<Object,String> hashMap=new HashMap<>();
//        hashMap.put("pId",pId);
//        hashMap.put("timestamp",timestamp);
//        hashMap.put("pUid",hisUid);
//        hashMap.put("notification",notification);
//        hashMap.put("sUid",myUid);
//
//
//        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users");
//        ref.child(hisUid).child("Notifications").child(timestamp).setValue(hashMap)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });
//
//    }


//    private void shareImageAndText(String pTitle, String pDerscription, Bitmap bitmap) {
//        String sharebody=pTitle+"\n"+pDerscription;
//
//        Uri uri=saveImgeToShare(bitmap);
//
//
//        Intent sIntent=new Intent(Intent.ACTION_SEND);
//        sIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        sIntent.putExtra(Intent.EXTRA_STREAM,uri);
//        sIntent.putExtra(Intent.EXTRA_TEXT,sharebody);
//        sIntent.putExtra(Intent.EXTRA_SUBJECT,"Subject Here");
//        sIntent.setType("image/png");
//        context.startActivity(Intent.createChooser(sIntent,"Share Via"));
//
//    }

//    private Uri saveImgeToShare(Bitmap bitmap) {
//        File imageFolder=new File(context.getCacheDir(),"images");
//        Uri uri=null;
//        try {
//            imageFolder.mkdir();
//            File file=new File(imageFolder,"shared_image.png");
//
//            FileOutputStream outputStream=new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.PNG,90,outputStream);
//            outputStream.flush();
//            outputStream.close();
//            uri= FileProvider.getUriForFile(context,"com.firebase.socialblogs.fileprovider",file);
//
//        }catch (Exception e){
//            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//
//        }
//        return uri;
//    }

//    private void shareTextOnly(String pTitle, String pDerscription) {
//        String sharebody=pTitle+"\n"+pDerscription;
//
//        Intent sIntent=new Intent(Intent.ACTION_SEND);
//        sIntent.setType("text/plane");
//        sIntent.putExtra(Intent.EXTRA_SUBJECT,"Subject Here");
//        sIntent.putExtra(Intent.EXTRA_TEXT,sharebody);
//        context.startActivity(Intent.createChooser(sIntent,"Share Via"));
//
//    }

//    private void setLiked(final myHolder holder, final String postKey) {
//        likeRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.child(postKey).hasChild(myUid)){
//                    holder.likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_likeed,0,0,0);
//                  //  holder.likeBtn.setText("Liked");
//                }else {
//                    holder.likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_black,0,0,0);
//                   // holder.likeBtn.setText("Like");
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    private void showMoreOption(ImageButton moreBtn, String uid, String myUid, final String pId, final String pImage) {
//
//        PopupMenu popupMenu=new PopupMenu(context,moreBtn, Gravity.END);
//        if (uid.equals(myUid)){
//            popupMenu.getMenu().add(Menu.NONE,0,0,"Delete");
//            popupMenu.getMenu().add(Menu.NONE,1,0,"Edit");
//
//        }
//        popupMenu.getMenu().add(Menu.NONE,2,0,"View Details");
//
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                int id=menuItem.getItemId();
//                if (id==0){
//                    beginDelete(pId,pImage);
//
//                }else if (id==1){
//                    Intent intent=new Intent(context, AddPostActivity.class);
//                    intent.putExtra("key","editPost");
//                    intent.putExtra("editPostId",pId);
//                    context.startActivity(intent);
//
//                }
//                else if (id==2){
//                    Intent intent=new Intent(context, PostDetailsActivity.class);
//                    intent.putExtra("postId",pId);
//                    context.startActivity(intent);
//                }
//                return false;
//            }
//        });
//        popupMenu.show();
//    }

//    private void beginDelete(String pId, String pImage) {
//
//        if (pImage.equals("noImage")){
//            deleteWithOutImage(pId);
//
//        }else {
//            deleteWithImage(pId,pImage);
//
//        }
//    }

//    private void deleteWithImage(final String pId, String pImage) {
//        final ProgressDialog progressDialog=new ProgressDialog(context);
//        progressDialog.setMessage("Deleting....");
//
//        StorageReference picRef= FirebaseStorage.getInstance().getReferenceFromUrl(pImage);
//        picRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Query fQuery= FirebaseDatabase.getInstance().getReference("Posts")
//                        .orderByChild("pId").equalTo(pId);
//                fQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot ds:snapshot.getChildren()){
//                            ds.getRef().removeValue();
//                        }
//
//                        Toast.makeText(context, "Deleted Successfully....", Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                progressDialog.dismiss();
//                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }

//    private void deleteWithOutImage(String pId) {
//        final ProgressDialog progressDialog=new ProgressDialog(context);
//        progressDialog.setMessage("Deleting....");
//        Query fQuery= FirebaseDatabase.getInstance().getReference("Posts")
//                .orderByChild("pId").equalTo(pId);
//        fQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot ds:snapshot.getChildren()){
//                    ds.getRef().removeValue();
//                }
//
//                Toast.makeText(context, "Deleted Successfully....", Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class myHolder extends RecyclerView.ViewHolder{

//        ImageView uPictureIv,pImageIv;
//        TextView uNameTv,pTitletv,pTimeTv,pDescriptionTv,pLikeTv,pCommentsTv;
//        ImageButton moreBtn;
//        Button likeBtn,commentBtn,shareBtn;
//        LinearLayout profileLayout;

        TextView pTitulo,pPrecio,pRaza;
        ImageButton fav;
        ImageView pImage;
        CardView cardView;

        public myHolder(@NonNull View itemView) {
            super(itemView);

            pTitulo =itemView.findViewById(R.id.tv_title_row);
            pPrecio = itemView.findViewById(R.id.tv_precio_row);
            pRaza = itemView.findViewById(R.id.tv_Raza_row);

            fav = itemView.findViewById(R.id.tv_imgFav_row);
            pImage = itemView.findViewById(R.id.img_row);

            cardView = itemView.findViewById(R.id.cardView);
//            uPictureIv=itemView.findViewById(R.id.uPictureIv);
//            pImageIv=itemView.findViewById(R.id.pImageIv);
//            uNameTv=itemView.findViewById(R.id.uNameTv);
//            pTitletv=itemView.findViewById(R.id.pTitleTv);
//            pTimeTv=itemView.findViewById(R.id.pTimeTv);
//            pDescriptionTv=itemView.findViewById(R.id.pDescriptionTv);
//            pLikeTv=itemView.findViewById(R.id.pLikeTv);
//            pCommentsTv=itemView.findViewById(R.id.pCommentsTv);
//            moreBtn=itemView.findViewById(R.id.moreBtn);
//            likeBtn=itemView.findViewById(R.id.likeBtn);
//            commentBtn=itemView.findViewById(R.id.commentBtn);
//            shareBtn=itemView.findViewById(R.id.shareBtn);
//            profileLayout=itemView.findViewById(R.id.profileLayout);
        }
    }
}


