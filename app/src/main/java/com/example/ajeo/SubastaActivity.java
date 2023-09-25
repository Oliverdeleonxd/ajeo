package com.example.ajeo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.numpad.NumPad;
import com.example.numpad.NumPadClick;
import com.example.numpad.numPadClickListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class SubastaActivity extends AppCompatActivity {

    ImageView btnPujar;
    RecyclerView RecyclerViewSubasta;

    String  hisuid,myUid,myName,myEmail,myDp,postId,pLikes,hisDp,hisName, pImage;

    ImageView uPictureIv,pImageIv,imgVendedor;
    TextView tvTitulo,tvNombre,tvPrecio,tvPrecioInicial,tvDesripcion,tvFecha,tvRaza,tvRaza2,tvPrecio2,tvPeso,tvUltimaPuja,tv_Ganador,tv_timer;
    TextView uNameTv,pTimeTv,pTitleTv,pDescriptionTv,pLikesTv,pcommentsTv;
    ImageButton moreBtn;
    Button likeBtn,shareBtn;
    LinearLayout profileLayout;
    RecyclerView recyclerView;
    Long intUltimaPuja;
    Long PrecioIni;

    FirebaseAuth mAuth;
    FirebaseUser user;

    BottonSheet bottonSheet;
    BottomSheetDialog btnSheet;
//    String jugador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subasta);

        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();

//        user.getDisplayName();


        bottonSheet = new BottonSheet();
        btnSheet = new BottomSheetDialog(this);






        Intent intent=getIntent();
        postId=intent.getStringExtra("Subasta");

        tvTitulo = findViewById(R.id.tv_title);
        tvNombre = findViewById(R.id.tv_Nombre);

        tvDesripcion = findViewById(R.id.tv_descripcion);
        tvFecha =   findViewById(R.id.tv_dfecha);
        tvPrecio = findViewById(R.id.tv_precio_row);
        tvPrecio2 = findViewById(R.id.tv_dPrecio);
//        tvPrecioInicial = findViewById(R.id.tvpr)


        tvRaza = findViewById(R.id.tv_Raza);
        tvRaza2 = findViewById(R.id.tv_dRaza);

        tvPeso = findViewById(R.id.tv_Peso);

        tvUltimaPuja = findViewById(R.id.tv_UltimaPuja);
        tv_Ganador =   findViewById(R.id.tv_Ganador);
        tv_timer = findViewById(R.id.tv_time);

        imgVendedor = findViewById(R.id.img_Vendedor);

        recyclerView = findViewById(R.id.RecyclerViewImg);

        btnPujar=findViewById(R.id.btnPujar);
//        createdialog();
//        btnSheet.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        btnPujar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//

                BottomSheetDialog bottomsheetDialog  = new BottomSheetDialog(SubastaActivity.this, R.style.BottomSheetDialogTheme);
                View bottomsheetView = LayoutInflater.from(SubastaActivity.this).inflate(R.layout.shett_dialog,null);

                EditText EtPujar = (EditText)bottomsheetView.findViewById(R.id.Et_puja);
                EtPujar.setFocusable(false);
                NumPad numPad = bottomsheetView.findViewById(R.id.numpad_id);
//                    numPad.setButtonTextColor(@NonNull Context context, @NonNull int colorId);
                numPad.setOnNumPadClickListener(new NumPadClick(new numPadClickListener() {
                    @Override
                    public void onNumpadClicked(ArrayList<Integer> nums) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < nums.size(); i++){
                            int num = nums.get(i);
                            stringBuilder.append(num);
                        }
                        String resul = stringBuilder.toString();
                        EtPujar.setText(resul);


                        Log.d("MYTAG", "onNumpadClicked: " + nums);
                    }
                }));
                bottomsheetView.findViewById(R.id.btnPuja).setOnClickListener(v1 -> {
                    String monto = EtPujar.getText().toString();
                    int puja = Integer.parseInt(monto);
                    DatabaseReference refuser= FirebaseDatabase.getInstance().getReference("Users");
                    Query query=refuser.orderByChild("uid").equalTo(user.getUid());
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot ds:snapshot.getChildren()) {

                                String name=""+ds.child("name").getValue();
                                Pujar(puja,name);
                                Toast.makeText(SubastaActivity.this, "name "+name, Toast.LENGTH_SHORT).show();

                            }//FOR

                        }//ONDATACHANE
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    Toast.makeText(SubastaActivity.this, "Pujar: "+ EtPujar.getText().toString(), Toast.LENGTH_SHORT).show();

                    bottomsheetDialog.dismiss();

                });
                bottomsheetDialog.setContentView(bottomsheetView);
                bottomsheetDialog.show();


            }
        });



        loadPostInfo();

        checkUserStatus();


    }//Oncreate







    public void Pujar(int puja,String name){


        int puja1 = puja;

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Subastas");
//        Toast.makeText(, "Puuuu", Toast.LENGTH_SHORT).show();


//        String uid = user.getUid();
        HashMap<String, Object> results = new HashMap<>();
        results.put("UltimaPuja",puja1);



                    results.put("Ganador",name);

//                       PrecioIni = (Long) Precio;

                    if(puja1 == intUltimaPuja){
                        Toast.makeText(SubastaActivity.this, "La Puja debe ser mayor a la actual",Toast.LENGTH_SHORT).show();
                    }
                    else if( puja1 > intUltimaPuja && puja1 > PrecioIni){
                        Toast.makeText(SubastaActivity.this, "Puja valida "+ puja1, Toast.LENGTH_SHORT).show();

                        ref.child(postId).updateChildren(results).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

//                                String UltimaPujaAct = ""+ds.child("UltimaPuja").getValue();
//                                tvUltimaPuja.setText(UltimaPujaAct);
                                Toast.makeText(SubastaActivity.this, " Puja completada " +puja1, Toast.LENGTH_SHORT).show();


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SubastaActivity.this, "algo salio mal en la puja", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }else {
                        Toast.makeText(SubastaActivity.this, "Puja invalida "+puja1, Toast.LENGTH_SHORT).show();
                    }//else puja


    }//Pujar

    private void loadPostInfo() {

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Subastas");


        Query query=ref.orderByChild("timestamp").equalTo(postId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){
                    String pTitle=""+ds.child("Titulo").getValue();
                    String uid=""+ds.child("uid").getValue();

//                    Toast.makeText(SubastaActivity.this, "DATA CHANGE"+pTitle, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(SubastaActivity.this, "DATA CHANGE", Toast.LENGTH_SHORT).show();

                    String pDescr=""+ds.child("Descripcion").getValue();
                    String pTimeStamp=""+ds.child("timestamp").getValue();
                    String Precio = ""+ds.child("PrecioInicial").getValue();
                    String img = ""+ds.child("ruta").getValue();
                    String Peso = ""+ds.child("Peso").getValue();
                    String UltimaPuja = ""+ds.child("Ultima puja").getValue();
                    String ganador = ""+ds.child("Ganador").getValue();
                    String time = ""+ds.child("timer").getValue();
                    String raza = ""+ds.child("Raza").getValue();


//                    String UltimaPuja = ""+ds.child("Ultima puja").getValue();

                    PrecioIni = Long.valueOf((String) ds.child("PrecioInicial").getValue()) ;
                    intUltimaPuja = (Long) ds.child("UltimaPuja").getValue();


                    tvUltimaPuja.setText(""+intUltimaPuja);

                    if(intUltimaPuja==0){
                        tvUltimaPuja.setText("0");
                    }else {
                        tvUltimaPuja.setText(""+intUltimaPuja);
                    }
                    tv_Ganador.setText(ganador);


                    tvRaza.setText(raza);
                    Calendar calendar=Calendar.getInstance(Locale.getDefault());
                    calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
                    String pTime= DateFormat.format("dd/MM/yyyy",calendar).toString();

                    tv_timer.setText(time);
                    tvTitulo.setText(pTitle);
                    tvDesripcion.setText(pDescr);
//                    pLikesTv.setText(pLikes+" Likes");
                    tvFecha.setText(pTime);
                    tvPrecio.setText(Precio);
                    tvPrecio2.setText(Precio);
                    tvPeso.setText(Peso);



                    DatabaseReference refUser= FirebaseDatabase.getInstance().getReference("Users");
//                  Query queryUser =refUser.orderByChild("uid").equalTo(uid);
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

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id=item.getItemId();
//        if (id==R.id.action_logout){
//
//            FirebaseAuth.getInstance().signOut();
//            checkUserStatus();
//        }
//        return super.onOptionsItemSelected(item);
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main,menu);
//        menu.findItem(R.id.action_add_post).setVisible(false);
//        menu.findItem(R.id.action_search).setVisible(false);
//        menu.findItem(R.id.action_groupinfo).setVisible(false);
//        menu.findItem(R.id.action_add_participant).setVisible(false);
//        menu.findItem(R.id.action_create_group).setVisible(false);
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}