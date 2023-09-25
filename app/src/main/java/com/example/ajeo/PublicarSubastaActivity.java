package com.example.ajeo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ajeo.Adapter.ImgAdapter;
import com.example.ajeo.Models.ModalClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PublicarSubastaActivity extends AppCompatActivity {

    private static final int RESULT_OK = -1;
    FirebaseAuth mAuth;

    TextInputEditText EtTitulo,EtDescripcion,EtPrecio,EtPeso;

    ImageButton imgButton;

    MaterialButton btnPublicar;

    FirebaseUser user;
    //DatabaseReference
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;

    //FireStorage
    FirebaseStorage mStorage;
    StorageReference storageReference;
    String StoragePath = "Users_Subastas/";

    Uri fileUri,fileUri2;

    List<ModalClass> modalClassList;
    ImgAdapter imgAdapter;

    RecyclerView imgRecyclerView;

    ClipData intData;
    Intent data2;

    String name, email,uid,dp;
    String ruta,Raza,da;

    ProgressDialog progressDialog;

    AutoCompleteTextView autocompleteTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar_subasta);


        mAuth = FirebaseAuth.getInstance();
        CheckUserStatus();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance();
//        mReference = mDatabase.getReference("Publicaciones");

        storageReference = FirebaseStorage.getInstance().getReference();

        mStorage = FirebaseStorage.getInstance();

        EtTitulo = findViewById(R.id.TextInputEditTextTitulo);
        EtDescripcion = findViewById(R.id.TextInputEditTextDescripcion);
        EtPeso = findViewById(R.id.TextInputEditTextPeso);
        EtPrecio = findViewById(R.id.TextInputEditTextPrecio);

        imgButton = findViewById(R.id.imgBtn);
        imgRecyclerView = findViewById(R.id.RecyclerViewImg);
        imgRecyclerView.setHasFixedSize(true);
        imgRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        btnPublicar = findViewById(R.id.btnPublicar);

        autocompleteTV = findViewById(R.id.autoCompleteTextViewRaza);

//                // set adapter to the autocomplete tv to the arrayAdapter
//                autocompleteTV.setAdapter(arrayAdapter);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.Razas, android.R.layout.simple_spinner_dropdown_item);
        autocompleteTV.setAdapter(adapter);
        autocompleteTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                Raza = item.toString();
//                Toast.makeText(getContext(), "raxa"+Raza, Toast.LENGTH_SHORT).show();
            }
        });




        modalClassList = new ArrayList<>();

        progressDialog = new ProgressDialog(this);

        btnPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Titulo = EtTitulo.getText().toString();
                String Descripcion = EtDescripcion.getText().toString();
                String Precio = EtPrecio.getText().toString();
                String Peso = EtPeso.getText().toString();

                if (Titulo.isEmpty()){
                    EtTitulo.setError("Titulo esta vacio");
                    EtTitulo.setFocusable(true);
                }else if (Descripcion.isEmpty()){
                    EtDescripcion.setError("Descripcion esta vacio");
//               }else if(fileUri2== null) {
                }else if(data2== null) {
                    Toast.makeText(PublicarSubastaActivity.this, "Seleccione imagenes a subir", Toast.LENGTH_SHORT).show();
                }else if(Precio.isEmpty()){
                    EtPrecio.setError("Precio esta vacio");
                }else if(Peso.isEmpty()){
                    EtPeso.setError("Peso esta vacio");
                }else if(Raza == null) {
                    autocompleteTV.setError("Raza esta vacio");
                    Toast.makeText(PublicarSubastaActivity.this, "Escoge la Raza", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(PublicarSubastaActivity.this, "Raza"+Raza,Toast.LENGTH_SHORT).show();
//                   PublicarPost(Titulo, Descripcion,fileUri2);//
                    PublicarPost(Titulo,Descripcion,data2,Precio,Peso,Raza);
                }



            }
        });

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AskPermision();
                UploadPhoto();
            }
        });

        mReference = FirebaseDatabase.getInstance().getReference("Subastas");

        Query query = mReference.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    name = "" + dataSnapshot.child("name").getValue();
                    email = "" + dataSnapshot.child("email").getValue();
                    dp = "" + dataSnapshot.child("image").getValue();



                }

            }//onDataChange

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







//        return root;
    }// onCreateView

    private void UploadPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"selecct"),101);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 101){

//                if (data.getClipData()!= null) {
//                    data2 = data;
//                } }else if(data.getData()!=null){
//                  //  intData = data.getData();
//                    Toast.makeText(getContext(), "one", Toast.LENGTH_SHORT).show();
//                }// else if


                if (data.getClipData()!= null){
                    data2 = data;
                    imgRecyclerView.setBackground(null);
//                    Toast.makeText(getContext(), "multiple", Toast.LENGTH_SHORT).show();

                    int totalItem = data.getClipData().getItemCount();

                    for (int i = 0; i < totalItem;i++) {

                        fileUri = data.getClipData().getItemAt(i).getUri();

//                        String imgName = data.getClipData().getItemAt(i).toString() + "hola";


                        String imgName2 = getfilename(fileUri);


                        String imgName3 = String.valueOf(i+1);

//                        Toast.makeText(getContext(), "name"+imgName2, Toast.LENGTH_SHORT).show();

                        ModalClass modalClass = new ModalClass(imgName3,fileUri);
                        modalClassList.add(modalClass);
                        imgAdapter = new ImgAdapter(PublicarSubastaActivity.this,modalClassList);
                        imgRecyclerView.setAdapter(imgAdapter);

                        //String Post =

//                        StorageReference mRef2 = storageReference.child(StoragePath).child(user.getEmail()+String.valueOf(ServerValue.TIMESTAMP)).child(imgName3);
//
//                        mRef2.putFile(fileUri).addOnSuccessListener(new OnSuccessListener() {
//
//                            @Override
//                            public void onSuccess(Object o) {
//
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//
//                            }
//                        });



//                        fileUri = data.getClipData().getItemAt(i).getUri();
//                        fileUri2 = fileUri;

                    }
                }else if(data.getData()!=null){
//                    Toast.makeText(getContext(), "one", Toast.LENGTH_SHORT).show();
                    if (data.getData()!= null) {
                        data2 = data;
                        imgRecyclerView.setBackground(null);
                        fileUri = data.getData();

                        String imgName3 = "1";

//                        Toast.makeText(getContext(), "name"+imgName2, Toast.LENGTH_SHORT).show();

                        ModalClass modalClass = new ModalClass(imgName3,fileUri);
                        modalClassList.add(modalClass);
                        imgAdapter = new ImgAdapter(PublicarSubastaActivity.this,modalClassList);
                        imgRecyclerView.setAdapter(imgAdapter);


                    }

                }// else if


//                imgUri = data.getData();
//                btImgUser.setImageURI(imgUri);
//                SubirImgen();
            }//result


        }//Result ok
    }//OnActivityResult

    @SuppressLint("Range")
    public String getfilename(Uri filepath)
    {
        String result = null;
        if (filepath.getScheme().equals("content")) {
            Cursor cursor = getApplicationContext().getApplicationContext().getContentResolver().query(filepath, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = filepath.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void PublicarPost(String titulo, String descripcion,Intent data,String precio, String peso,String raza) {

        progressDialog.setMax(100);
        progressDialog.setTitle("Subiendo imagenes...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

//        Date date = new Date();
        final String da = String.valueOf(System.currentTimeMillis());



        if (data.getClipData()!= null){
            imgRecyclerView.setBackground(null);

            int totalItem = data.getClipData().getItemCount();



            StorageReference mRefRuta = storageReference.child(StoragePath).child("Subastas de "+user.getEmail()).child("Publicion fecha : "+da);

            for (int i = 0; i < totalItem;i++) {

                fileUri = data.getClipData().getItemAt(i).getUri();



                ruta = String.valueOf(mRefRuta);

                String imgName3 = String.valueOf(i+1);

                StorageReference mRef2 = storageReference.child(StoragePath).child("Subastas de "+user.getEmail()).child("Publicion fecha : "+da).child(imgName3);

                mRef2.putFile(fileUri).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(PublicarSubastaActivity.this, "Imagenes subidas", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        final Uri downloadUri = uriTask.getResult();
                        String urii = downloadUri.toString();


//                        database(mRef2);

                        if (uriTask.isSuccessful()) {
                            uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    database(titulo,descripcion,peso,precio,raza,ruta,urii,da);



                                }//uri success
                            });
                        }//if


                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress( UploadTask.TaskSnapshot taskSnapshot)
                    {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        //while( progress != 100.0){
                        progressDialog.setMessage("Subiendo " +  progress + "%");
//                        Log.d("Subir_Fragment", "onProgress: "+progress);

                        if (progress == 100.0) {
                            progressDialog.dismiss();




                        }
                        //  }


                    }//ProgressDialog
                });


//                        fileUri = data.getClipData().getItemAt(i).getUri();
//                        fileUri2 = fileUri;

            }//for

            EtDescripcion.setText("");
            EtTitulo.setText("");
            EtTitulo.clearFocus();
            EtDescripcion.clearFocus();
            EtPrecio.setText("");
            EtPrecio.clearFocus();
            EtPeso.setText("");
            EtPeso.clearFocus();
            autocompleteTV.setText(null);
            imgRecyclerView.setAdapter(null);
            imgRecyclerView.setBackgroundResource(R.drawable.ic_round_photo_camera_back_24);
            modalClassList.clear();

        }else if(data.getData()!=null){
            Toast.makeText(PublicarSubastaActivity.this, "one", Toast.LENGTH_SHORT).show();
            if (data.getData()!= null) {
                imgRecyclerView.setBackground(null);
                fileUri = data.getData();


                StorageReference mRefRuta = storageReference.child(StoragePath).child("Subastas de " + user.getEmail()).child("Publicion fecha : " + da);

                ruta = String.valueOf(mRefRuta);

                String imgName1 = "1";

                StorageReference mRef2 = storageReference.child(StoragePath).child("Subastas de " + user.getEmail()).child("Publicion fecha : " + da).child(imgName1);

                mRef2.putFile(fileUri).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        EtDescripcion.setText("");
                        EtTitulo.setText("");
                        EtTitulo.clearFocus();
                        EtDescripcion.clearFocus();
                        EtPrecio.setText("");
                        EtPrecio.clearFocus();
                        EtPeso.setText("");
                        EtPeso.clearFocus();
                        autocompleteTV.setText(null);
                        imgRecyclerView.setAdapter(null);
                        imgRecyclerView.setBackgroundResource(R.drawable.ic_round_photo_camera_back_24);
                        modalClassList.clear();
//                        Toast.makeText(PublicarSubastaActivity.this, "Imagen", Toast.LENGTH_SHORT).show();

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        final Uri downloadUri = uriTask.getResult();
                        String urii = downloadUri.toString();


//                        database(mRef2);

                        if (uriTask.isSuccessful()) {
                            uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    database(titulo,descripcion,peso,precio,raza,ruta,urii,da);



                                }//uri success
                            });
                        }//if


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        //while( progress != 100.0){
//                        progressDialog.setMessage("Subiendo " +  progress + "%");
                        Log.d("Subir_Fragment", "onProgress: " + progress);

                        if (progress == 100.0) {
                            progressDialog.dismiss();
                        }



                    }//ProgressDialog

                });



            }



        }// else if



    }//Publicar


    private void database(String titulo,String descripcion,String peso,String precio,String raza,String ruta, String urii,String da){

        HashMap<String, Object> results = new HashMap<>();
//                            results.put("image", urii);
        results.put("Titulo",titulo);
        results.put("email", email);
        results.put("uid", uid);
        results.put("Descripcion",descripcion);
        results.put("PrecioInicial",precio);
        results.put("Peso",peso);
        results.put("Raza",raza);
        results.put("UltimaPuja",0);
//                            results.put("timestamp", ServerValue.TIMESTAMP);
//                            results.put("pId", ServerValue.TIMESTAMP);
        results.put("timestamp", da);
        results.put("pId", da);
        results.put("ruta",urii);
        results.put("rutaImg",ruta);
        results.put("Ganador","");

//


        //mReference = mDatabase.getReference("Publicaciones");
        mReference.child(String.valueOf(da)).updateChildren(results).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(PublicarSubastaActivity.this, "completado", Toast.LENGTH_SHORT).show();

                fileUri = null;
                fileUri2 = null;


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PublicarSubastaActivity.this, "algo salio mal", Toast.LENGTH_SHORT).show();

            }
        });

    }//database

//    Handler handle = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            progressDialog.incrementProgressBy(1);
//        }
//    };

    private void AskPermision() {
        Dexter.withContext(PublicarSubastaActivity.this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                Toast.makeText(PublicarSubastaActivity.this,"Bienn",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    private void CheckUserStatus(){

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            email = user.getEmail();
            uid = user.getUid();
//            Toast.makeText(getContext(), "user "+user, Toast.LENGTH_SHORT).show();

        }
        else {
            startActivity(new Intent(PublicarSubastaActivity.this, LoginActivity.class));
        }

    }

}