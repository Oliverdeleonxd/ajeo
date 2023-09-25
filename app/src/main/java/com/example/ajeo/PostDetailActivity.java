package com.example.ajeo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
public class PostDetailActivity extends AppCompatActivity {



    String  hisuid,myUid,myName,myEmail,myDp,postId,pLikes,hisDp,hisName, pImage;

    ImageView uPictureIv,pImageIv,imgVendedor;
    TextView tvTitulo,tvNombre,tvPrecio,tvDesripcion,tvFecha,tvRaza,tvRaza2,tvPrecio2,tvPeso;
    TextView uNameTv,pTimeTv,pTitleTv,pDescriptionTv,pLikesTv,pcommentsTv;
    ImageButton moreBtn;
    Button likeBtn,shareBtn;
    LinearLayout profileLayout;
    RecyclerView recyclerView;


//    List<ModelComments> commentList;
//    AdapterComments adapterComments;

    boolean mProcessComment=false;
    boolean mProcessLike=false;


    ProgressDialog pd;

    EditText commentEt;
    ImageButton sendBtn;
    ImageView cAvatarIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_post_detail);
//        }

//        ActionBar actionBar=getSupportActionBar();
//        actionBar.setTitle("Post Details");
//        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        postId=intent.getStringExtra("timestamp");

        tvTitulo = findViewById(R.id.tv_title);
        tvNombre = findViewById(R.id.tv_Nombre);

        tvDesripcion = findViewById(R.id.tv_descripcion);
        tvFecha =   findViewById(R.id.tv_dfecha);
        tvPrecio = findViewById(R.id.tv_precio_row);
        tvPrecio2 = findViewById(R.id.tv_dPrecio);


        tvRaza = findViewById(R.id.tv_Raza);
        tvRaza2 = findViewById(R.id.tv_dRaza);

        tvPeso = findViewById(R.id.tv_Peso);

        imgVendedor = findViewById(R.id.imgVendedor);

        recyclerView = findViewById(R.id.RecyclerViewImg);



//        uPictureIv=findViewById(R.id.);
//        pImageIv=findViewById(R.id.pImageIv);
//        uNameTv=findViewById(R.id.uNameTv);
//        pTimeTv=findViewById(R.id.pTimeTv);
//        pTitleTv=findViewById(R.id.pTitleTv);
//        pDescriptionTv=findViewById(R.id.pDescriptionTv);
//        pLikesTv=findViewById(R.id.pLikeTv);
//        pcommentsTv=findViewById(R.id.pCommentsTv);
//        moreBtn=findViewById(R.id.moreBtn);
//        likeBtn=findViewById(R.id.likeBtn);
//        shareBtn=findViewById(R.id.shareBtn);
//        profileLayout=findViewById(R.id.profileLayout);
//        recyclerView=findViewById(R.id.recyclerView);


//        commentEt=findViewById(R.id.commentEt);
//        sendBtn=findViewById(R.id.sendBtn);
//        cAvatarIv=findViewById(R.id.cAvatarIv);

        loadPostInfo();

        checkUserStatus();

//        loadUserInfo();
//
//        setLiked();


//        actionBar.setSubtitle("SignIn as: "+myEmail);
//
//        loadComment();
//
//        sendBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                postComment();
//            }
//        });
//
//        likeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LikePost();
//            }
//        });
//
//        moreBtn.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//            @Override
//            public void onClick(View view) {
//
//                showMoreOption();
//
//            }
//        });

//        shareBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String pTitle=pTitleTv.getText().toString().trim();
//                String pDescription=pDescriptionTv.getText().toString().trim();
//
//                BitmapDrawable bitmapDrawable=(BitmapDrawable)pImageIv.getDrawable();
//                if (bitmapDrawable==null)
//                {
//                    shareTextOnly(pTitle,pDescription);
//
//                }else {
//                    Bitmap bitmap=bitmapDrawable.getBitmap();
//                    shareImageAndText(pTitle,pDescription,bitmap);

//                }
//
//            }
//        });
//
//        pLikesTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(PostDetailsActivity.this, PostLikedByActivity.class);
//                intent.putExtra("postId",postId);
//                startActivity(intent);
//
//            }
//        });
//
    }

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

//    }

//    private void shareImageAndText(String pTitle, String pDerscription, Bitmap bitmap) {
//        String sharebody=pTitle+"\n"+pDerscription;
//
//        Uri uri=saveImgeToShare(bitmap);
//
//        Intent sIntent=new Intent(Intent.ACTION_SEND);
//        sIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        sIntent.putExtra(Intent.EXTRA_STREAM,uri);
//        sIntent.putExtra(Intent.EXTRA_TEXT,sharebody);
//        sIntent.putExtra(Intent.EXTRA_SUBJECT,"Subject Here");
//        sIntent.setType("image/png");
//        startActivity(Intent.createChooser(sIntent,"Share Via"));
//
//    }
//
//    private Uri saveImgeToShare(Bitmap bitmap) {
//        File imageFolder=new File(this.getCacheDir(),"images");
//        Uri uri=null;
//        try {
//            imageFolder.mkdir();
//            File file=new File(imageFolder,"shared_image.png");
//
//            FileOutputStream outputStream=new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.PNG,90,outputStream);
//            outputStream.flush();
//            outputStream.close();
//            uri= FileProvider.getUriForFile(this,"com.firebase.socialblogs.fileprovider",file);
//
//        }catch (Exception e){
//            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//
//        }
//        return uri;
//    }
//
//    private void shareTextOnly(String pTitle, String pDerscription) {
//        String sharebody=pTitle+"\n"+pDerscription;
//
//        Intent sIntent=new Intent(Intent.ACTION_SEND);
//        sIntent.setType("text/plane");
//        sIntent.putExtra(Intent.EXTRA_SUBJECT,"Subject Here");
//        sIntent.putExtra(Intent.EXTRA_TEXT,sharebody);
//        startActivity(Intent.createChooser(sIntent,"Share Via"));
//
//    }




//    private void loadComment(){
//        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(linearLayoutManager);
//
//        commentList=new ArrayList<>();
//
//        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("Comments");
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                commentList.clear();
//                for (DataSnapshot ds:snapshot.getChildren()){
//                    ModelComments modelComments=ds.getValue(ModelComments.class);
//                    commentList.add(modelComments);
//
//
//
//                    adapterComments=new AdapterComments(getApplicationContext(),commentList,myUid,postId);
//                    recyclerView.setAdapter(adapterComments);
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    private void showMoreOption() {
//        PopupMenu popupMenu=new PopupMenu(this,moreBtn, Gravity.END);
//        if (hisuid.equals(myUid)){
//            popupMenu.getMenu().add(Menu.NONE,0,0,"Delete");
//            popupMenu.getMenu().add(Menu.NONE,1,0,"Edit");
//
//        }
//
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                int id=menuItem.getItemId();
//                if (id==0){
//                    beginDelete();
//
//                }else if (id==1){
//                    Intent intent=new Intent(PostDetailsActivity.this, AddPostActivity.class);
//                    intent.putExtra("key","editPost");
//                    intent.putExtra("editPostId",postId);
//                    startActivity(intent);
//
//                }
//                return false;
//            }
//        });
//        popupMenu.show();
//    }

//    private void beginDelete() {
//        if (pImage.equals("noImage")){
//            deleteWithOutImage();
//
//        }else {
//            deleteWithImage();
//
//        }
//    }

//    private void deleteWithImage() {
//        final ProgressDialog progressDialog=new ProgressDialog(this);
//        progressDialog.setMessage("Deleting....");
//
//        StorageReference picRef= FirebaseStorage.getInstance().getReferenceFromUrl(pImage);
//        picRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Query fQuery= FirebaseDatabase.getInstance().getReference("Posts")
//                        .orderByChild("pId").equalTo(postId);
//                fQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot ds:snapshot.getChildren()){
//                            ds.getRef().removeValue();
//                        }
//
//                        Toast.makeText(PostDetailsActivity.this, "Deleted Successfully....", Toast.LENGTH_SHORT).show();
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
//                Toast.makeText(PostDetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//
//    }

//    private void deleteWithOutImage() {
//        final ProgressDialog progressDialog=new ProgressDialog(this);
//        progressDialog.setMessage("Deleting....");
//        Query fQuery= FirebaseDatabase.getInstance().getReference("Posts")
//                .orderByChild("pId").equalTo(postId);
//        fQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot ds:snapshot.getChildren()){
//                    ds.getRef().removeValue();
//                }
//
//                Toast.makeText(PostDetailsActivity.this, "Deleted Successfully....", Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

//    private void setLiked() {
//
//        final DatabaseReference likeRef=FirebaseDatabase.getInstance().getReference().child("Likes");
//        likeRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.child(postId).hasChild(myUid)){
//                    likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_likeed,0,0,0);
//                    likeBtn.setText("Liked");
//                }else {
//                    likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_black,0,0,0);
//                    likeBtn.setText("Like");
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
//
//    }

//    private void LikePost(){
//
//        mProcessLike=true;
//
//        final DatabaseReference likeRef=FirebaseDatabase.getInstance().getReference().child("Likes");
//        final DatabaseReference postRef=FirebaseDatabase.getInstance().getReference().child("Posts");
//
//        likeRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (mProcessLike){
//                    if (snapshot.child(postId).hasChild(myUid)){
//                        postRef.child(postId).child("pLikes").setValue(""+(Integer.parseInt(pLikes)-1));
//                        likeRef.child(postId).child(myUid).removeValue();
//                        mProcessLike=false;
//
//                    }
//                    else {
//                        postRef.child(postId).child("pLikes").setValue(""+(Integer.parseInt(pLikes)+1));
//                        likeRef.child(postId).child(myUid).setValue("Liked");
//                        mProcessLike=false;
//
//                        addToHisNotifiction(""+hisuid,""+postId,"Liked your post");
//
//
//                    }
//                }
//
//            }

//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }

//    private void postComment() {
//        pd=new ProgressDialog(this);
//        pd.setMessage("Adding Comment...");
//
//        String comment=commentEt.getText().toString().trim();
//        if (TextUtils.isEmpty(comment)){
//            Toast.makeText(this, "Comment is emputy....", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String timeStamp= String.valueOf(System.currentTimeMillis());
//
//        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("Comments");
//
//        HashMap<String,Object>hashMap=new HashMap<>();
//        hashMap.put("cId",timeStamp);
//        hashMap.put("comment",comment);
//        hashMap.put("timestamp",timeStamp);
//        hashMap.put("uid",myUid);
//        hashMap.put("uEmail",myEmail);
//        hashMap.put("uDp",myDp);
//        hashMap.put("uName",myName);
//
//        ref.child(timeStamp).setValue(hashMap)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        pd.dismiss();
//                        Toast.makeText(PostDetailsActivity.this, "Comment Added....", Toast.LENGTH_SHORT).show();
//                        commentEt.setText("");
//                        updateCommentCount();
//
//                        addToHisNotifiction(""+hisuid,""+postId,"Commented on your post");
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                        pd.dismiss();
//                        Toast.makeText(PostDetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//
//    }

//    private void updateCommentCount() {
//        mProcessComment=true;
//        final DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Posts").child(postId);
//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (mProcessComment){
//                    String comments=""+snapshot.child("pComments").getValue();
//                    int newCommentVal=Integer.parseInt(comments)+1;
//                    ref.child("pComments").setValue(""+newCommentVal);
//                    mProcessComment=false;
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//
//    }

//    private void loadUserInfo() {
//        Query  myRef=FirebaseDatabase.getInstance().getReference("Users");
//        myRef.orderByChild("uid").equalTo(myUid)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot ds:snapshot.getChildren()){
//                            myName=""+ds.child("name").getValue();
//                            myDp=""+ds.child("image").getValue();
//
//
//                            try {
//                                Picasso.get().load(myDp).placeholder(R.drawable.ic_default_img).into(cAvatarIv);
//
//
//                            }catch (Exception e){
//                                Picasso.get().load(R.drawable.ic_default_img).into(cAvatarIv);
//
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
//
//    }

    private void loadPostInfo() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Posts");


        Query query=ref.orderByChild("timestamp").equalTo(postId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){
                    String pTitle=""+ds.child("Titulo").getValue();
                    String uid=""+ds.child("uid").getValue();

                    String pDescr=""+ds.child("Descripcion").getValue();
                    String pTimeStamp=""+ds.child("timestamp").getValue();
                    String Precio = ""+ds.child("Precio").getValue();
                    String img = ""+ds.child("ruta").getValue();
                    String Peso = ""+ds.child("Peso").getValue();
                    String raza = ""+ds.child("Raza").getValue();


                    DatabaseReference refUser= FirebaseDatabase.getInstance().getReference("Users");
//                    Query queryUser =refUser.orderByChild("uid").equalTo(uid);
                    Query queryUser =refUser.orderByChild("uid").equalTo(uid);
                    queryUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dsu:snapshot.getChildren()) {
                                String Nombre = "" + dsu.child("name").getValue();
                                String img = "" + dsu.child("image").getValue();

                                tvNombre.setText(Nombre);
                                Picasso.get().load(img).placeholder(R.drawable.logoajeo).into(imgVendedor);
                                try {
                                    Picasso.get().load(img).placeholder(R.drawable.logoajeo).into(imgVendedor);
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }

                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    Calendar calendar=Calendar.getInstance(Locale.getDefault());
                    calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
                    String pTime= DateFormat.format("dd/MM/yyyy",calendar).toString();

                    tvRaza.setText(raza);
                    tvTitulo.setText(pTitle);
                    tvDesripcion.setText(pDescr);
                    tvFecha.setText(pTime);
                    tvPrecio.setText(Precio);
                    tvPrecio2.setText(Precio);
                    tvPeso.setText(Peso);



                }//for

            }//onDataChange

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }//loadPostInfor

    private void checkUserStatus(){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            myEmail=user.getEmail();
            myUid=user.getUid();

        }else {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}