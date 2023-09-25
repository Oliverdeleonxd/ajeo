package com.example.ajeo;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity {



    private static final int CAMARA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_REQUEST_CODE = 300;
    private static final int IMAGE_PICK_CAMARA_REQUEST_CODE = 400;

    private static final int CAMERA_REQUEST = 500;

    private static final int ACTIVITY_START_CAMERA_APP = 0;


    FirebaseUser user;
    FirebaseAuth mAuth;
    //DatabaseReference
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;

    //FireStorage
    FirebaseStorage mStorage;
    StorageReference storageReference;
    String StoragePath = "Users_Img_Profile/";

    String Profile = "image";

    TextInputEditText EtNombre, EtTelefono;
    TextView tvCorreo;

    ImageButton btImgUser;

    MaterialButton btnGuardar;

    String[] storagePermissions;
    String[]camaraPermissions;

    Uri imgUri;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Users");

        storageReference = FirebaseStorage.getInstance().getReference();

        mStorage = FirebaseStorage.getInstance();

        EtNombre = findViewById(R.id.TextInputEditTextNombre);
        EtTelefono = findViewById(R.id.TextInputEditTextTelefono);

        btnGuardar = findViewById(R.id.btnGuardar);

        tvCorreo = findViewById(R.id.tvCorreo);

        AskPermision();

        camaraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};


        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuadarCambios();
            }
        });

        btImgUser = findViewById(R.id.imageUser);
        btImgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AskPermision();
                UploadPhoto();

//                ContentValues values = new ContentValues();
//                values.put(MediaStore.Images.Media.TITLE,"Temp picture");
//                values.put(MediaStore.Images.Media.DESCRIPTION,"Temp Description");
//                imgUri = EditProfileActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
//
//
//
//
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT,imgUri);
//                startActivityForResult(intent, CAMERA_REQUEST);
            }
        });

        Query query = mReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()){
                    String email = "" +ds.child("email").getValue();
                    String name = "" +ds.child("name").getValue();
                    String Telefono = "" +ds.child("phone").getValue();
                    String image = "" +ds.child("image").getValue();


                    EtNombre.setText(name);
                    EtTelefono.setText(Telefono);
                    tvCorreo.setText(email);

                    try {
                        Picasso.get().load(image).into(btImgUser);
                    }catch (Exception e) {
                        Picasso.get().load(R.drawable.ic_person).into(btImgUser);

                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }//onCreate

    private void GuadarCambios() {

       String name = EtNombre.getText().toString();
        String telefono = EtTelefono.getText().toString();


        if (!name.isEmpty() && !telefono.isEmpty()) {

        HashMap<String, Object> results = new HashMap<>();

        results.put("name",name);
        results.put("phone",telefono);


        mReference.child(user.getUid()).updateChildren(results).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(EditProfileActivity.this, "completado", Toast.LENGTH_SHORT).show();
                EtNombre.clearFocus();
                EtTelefono.clearFocus();
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfileActivity.this, "algo salio mal", Toast.LENGTH_SHORT).show();

            }
        });
        }else{
            Toast.makeText(this, "Campos Vacios", Toast.LENGTH_SHORT).show();
        }


    }//GuadarCambios

    private void UploadPhoto() {
        //Abrir galeria
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,300);
    }



//Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.CAMERA,
    private void AskPermision() {
        Dexter.withContext(EditProfileActivity.this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                Toast.makeText(EditProfileActivity.this,"Bienn",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }


//    private boolean checkStoragePermissions() {
//
//        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
//        return result;
//    }
//    private void requestStoragePermissions(){
//        ActivityCompat.requestPermissions(this,storagePermissions,STORAGE_REQUEST_CODE);
//    }
//
//    private boolean checkCameraPermissions() {
//
//        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
//        boolean result1 = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
//
//        return result && result1;
//    }
//    private void requestCamaraPermissions(){
//        ActivityCompat.requestPermissions(this,camaraPermissions,CAMARA_REQUEST_CODE);
//    }


    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 300){
                imgUri = data.getData();
                btImgUser.setImageURI(imgUri);
                SubirImgen();
            }


        }
    }

    private void SubirImgen() {

        //mReference.child("Users").setValue("hola");

        String filePathAndName= StoragePath+""+"Image" +"_"+user.getUid();

        StorageReference storageReference2nd = storageReference.child(filePathAndName);

        storageReference2nd.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                   final Uri downloadUri = uriTask.getResult();
                    String urii = downloadUri.toString();

//                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                    while (!uriTask.isSuccessful());
//                    Uri dowloadUrl = uriTask.getResult();
//                    String urii = dowloadUrl.toString();

                    if (uriTask.isSuccessful()) {

                        uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {


//                                mReference.child("Users").setValue("hola");


                                HashMap<String, Object> results = new HashMap<>();
                                results.put("image", urii);
                              //  results.put("name","oli");

                               // mReference = mDatabase.getReference();

//                                mReference.child(mAuth.getUid()).setValue(results);

//                                mReference.child("Users").child(user.getUid()).setValue(results);

                                Log.d("uid", "onSuccess: "+user.getUid());
                                Log.e("uid", "onSuccess: "+user.getUid());

//

//                                HashMap<Object,String> hashMap = new HashMap<>();
//                                hashMap.put("email", email);
//                                hashMap.put("uid", uid);
//                                hashMap.put("name","");
//                                hashMap.put("phone","");
//                                hashMap.put("image","");

//                                FirebaseDatabase db = FirebaseDatabase.getInstance();
//
//                                DatabaseReference reference = db.getReference("Users");
//
//                                reference.child(uid).setValue(hashMap);

                              //  mReference.child(mAuth.getCurrentUser().getUid()).updateChildren(results);

                             //   storageReference.child(user.getUid()).putFile(uri);

                                mReference.child(user.getUid()).updateChildren(results).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Toast.makeText(EditProfileActivity.this, "completado", Toast.LENGTH_SHORT).show();


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EditProfileActivity.this, "algo salio mal", Toast.LENGTH_SHORT).show();

                                    }
                                });




//                                HashMap<String, Object> resultss = new HashMap<>();
//                                results.put("image", urii);

//                                P.setImg(dowloadUrl.toString());



//                                P.setIdProducto(UUID.randomUUID().toString());

//                                P.setNombre(nombre);
//                                mReference.child("Users").child(user.getUid()).updateChildren(results).addOnSuccessListener(new OnSuccessListener<Void>() {
////                                .child("Memes").child(P.getIdProducto()).setValue(P).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void unused) {
//                                        Toast.makeText(EditProfileActivity.this, "Guardado Exitosamente :D",Toast.LENGTH_SHORT).show();
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Toast.makeText(EditProfileActivity.this, "Ah ocurrido un error al subir :( ",Toast.LENGTH_SHORT).show();
//                                        Log.d("Subir_Fragment", "onFailure: "+e.getMessage());
//                                    }
//                                });

//                                Nombre.setText("");

//                                CargarImg.setImageResource(R.drawable.ic_icons8_image_file_add);

//                                Toast.makeText(getContext(), "Listo :3", Toast.LENGTH_SHORT).show();
//                                progressDialog.dismiss();


                            }
                        });


//
//
//

                    }//if


//                }//while

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfileActivity.this, "Algo salio mal"+e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("ima", "onFailure: "+e.getMessage());
            }
        });


    }


}//EditProfileActivity