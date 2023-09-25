package com.example.ajeo.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ajeo.Adapter.ImgAdapter;
import com.example.ajeo.LoginActivity;
import com.example.ajeo.Models.ModalClass;
import com.example.ajeo.R;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PublicarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PublicarFragment extends Fragment {

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
    String StoragePath = "Users_Post/";

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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PublicarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PublicarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PublicarFragment newInstance(String param1, String param2) {
        PublicarFragment fragment = new PublicarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_publicar, container, false);


       mAuth = FirebaseAuth.getInstance();
       CheckUserStatus();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance();
//        mReference = mDatabase.getReference("Publicaciones");

        storageReference = FirebaseStorage.getInstance().getReference();




//        mStorage = FirebaseStorage.getInstance();

        EtTitulo = root.findViewById(R.id.TextInputEditTextTitulo);
        EtDescripcion = root.findViewById(R.id.TextInputEditTextDescripcion);
        EtPeso = root.findViewById(R.id.TextInputEditTextPeso);
        EtPrecio = root.findViewById(R.id.TextInputEditTextPrecio);

        imgButton = root.findViewById(R.id.imgBtn);
        imgRecyclerView = root.findViewById(R.id.RecyclerViewImg);
        imgRecyclerView.setHasFixedSize(true);
        imgRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        btnPublicar = root.findViewById(R.id.btnPublicar);


        autocompleteTV = root.findViewById(R.id.autoCompleteTextViewRaza);

//                // set adapter to the autocomplete tv to the arrayAdapter
//                autocompleteTV.setAdapter(arrayAdapter);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getContext(),R.array.Razas, android.R.layout.simple_spinner_dropdown_item);
        autocompleteTV.setAdapter(adapter);
        autocompleteTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                Raza = item.toString();
//                Toast.makeText(getContext(), "raxa"+Raza, Toast.LENGTH_SHORT).show();
            }
        });


//        autocompleteTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Object item = parent.getItemAtPosition(position);
//                Raza = item.toString();
//            }
//        });
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Object item = parent.getItemAtPosition(position);
//                Raza = item.toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                Raza = null;
//            }
//        });


        modalClassList = new ArrayList<>();

        progressDialog = new ProgressDialog(getContext());

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
                   Toast.makeText(getContext(), "Seleccione imagenes a subir", Toast.LENGTH_SHORT).show();
               }else if(Precio.isEmpty()){
                   EtPrecio.setError("Precio esta vacio");
               }else if(Peso.isEmpty()){
                   EtPeso.setError("Peso esta vacio");
               }else if(Raza == null) {
                   autocompleteTV.setError("Raza esta vacio");
                   Toast.makeText(getContext(), "Escoge la Raza", Toast.LENGTH_SHORT).show();
               }
               else {

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

       mReference = FirebaseDatabase.getInstance().getReference("Posts");

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







        return root;
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



                        String imgName2 = getfilename(fileUri);


                        String imgName3 = String.valueOf(i+1);

//                        Toast.makeText(getContext(), "name"+imgName2, Toast.LENGTH_SHORT).show();

                        ModalClass modalClass = new ModalClass(imgName3,fileUri);
                        modalClassList.add(modalClass);
                        imgAdapter = new ImgAdapter(getContext(),modalClassList);
                        imgRecyclerView.setAdapter(imgAdapter);





                    }
                }else if(data.getData()!=null){
//                    Toast.makeText(getContext(), "one", Toast.LENGTH_SHORT).show();
                    if (data.getData()!= null) {
                        data2 = data;
                        imgRecyclerView.setBackground(null);
                        fileUri = data.getData();

                        String imgName3 = "1";

                        ModalClass modalClass = new ModalClass(imgName3,fileUri);
                        modalClassList.add(modalClass);
                        imgAdapter = new ImgAdapter(getContext(),modalClassList);
                        imgRecyclerView.setAdapter(imgAdapter);


                    }

                }// else if


            }//result


        }//Result ok
    }//OnActivityResult

    @SuppressLint("Range")
    public String getfilename(Uri filepath)
    {
        String result = null;
        if (filepath.getScheme().equals("content")) {
            Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(filepath, null, null, null, null);
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

        Date date = new Date();
        final String da = String.valueOf(date.getTime());



        if (data.getClipData()!= null){
            imgRecyclerView.setBackground(null);

            int totalItem = data.getClipData().getItemCount();



            StorageReference mRefRuta = storageReference.child(StoragePath).child("Publicaciones de "+user.getEmail()).child("Publicion fecha : "+da);

            for (int i = 0; i < totalItem;i++) {

                fileUri = data.getClipData().getItemAt(i).getUri();















                String datebd = da;

                ruta = String.valueOf(mRefRuta);

                String imgName3 = String.valueOf(i+1);

                StorageReference mRef2 = storageReference.child(StoragePath).child("Publicaciones de "+user.getEmail()).child("Publicion fecha : "+da).child(imgName3);

                Log.d("Stogarage 543", "PublicarPost: "+mRef2);

                mRef2.putFile(fileUri).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(getContext(), "Imagenes subidas", Toast.LENGTH_SHORT).show();


//                        database(ruta);
//



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

                                    database(titulo,descripcion,peso,precio,raza,ruta,urii,datebd);



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
                    public void onProgress( UploadTask.TaskSnapshot taskSnapshot)
                    {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        //while( progress != 100.0){
                        progressDialog.setMessage("Subiendo " +  progress + "%");
                        Log.d("Subir_Fragment", "onProgress: "+progress);

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

            if (data.getData()!= null) {
                imgRecyclerView.setBackground(null);
                fileUri = data.getData();

                StorageReference mRefRuta = storageReference.child(StoragePath).child("Publicaciones de " + user.getEmail()).child("Publicion fecha : " + da);

                ruta = String.valueOf(mRefRuta);

                String datebd = da;

                String imgName3 = "1";

                StorageReference mRef2 = storageReference.child(StoragePath).child("Publicaciones de " + user.getEmail()).child("Publicion fecha : " + da).child(imgName3);

                Log.d(" 1 Stogarage 543", "PublicarPost: "+mRef2);

                mRef2.putFile(fileUri).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
//                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                        while (!uriTask.isSuccessful());
//                        final Uri downloadUri = uriTask.getResult();
//                        String urii = downloadUri.toString();

//                        database(mRef2);

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

                                    database(titulo,descripcion,peso,precio,raza,ruta,urii,datebd);



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
                        //  }


                    }//ProgressDialog

                });
                ;



            }//getData()



        }// else if


    }//Publicar


    private void database(String titulo,String descripcion,String peso,String precio,String raza,String ruta, String urii,String da){

        HashMap<String, Object> results = new HashMap<>();
//                            results.put("image", urii);
        results.put("Titulo",titulo);
        results.put("email", email);
        results.put("uid", uid);
        results.put("Descripcion",descripcion);
        results.put("Precio",precio);
        results.put("Peso",peso);
        results.put("Raza",raza);

        results.put("timestamp", da);
        results.put("pId", da);
        results.put("ruta",urii);
        results.put("rutaImg",ruta);


        //mReference = mDatabase.getReference("Publicaciones");
        mReference.child(String.valueOf(da)).updateChildren(results).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(getContext(), "completado", Toast.LENGTH_SHORT).show();

                fileUri = null;
                fileUri2 = null;


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "algo salio mal", Toast.LENGTH_SHORT).show();

            }
        });

    }//database



    private void AskPermision() {
        Dexter.withContext(getContext()).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                Toast.makeText(getContext(),"Bienn",Toast.LENGTH_SHORT).show();
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
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }

    }

}