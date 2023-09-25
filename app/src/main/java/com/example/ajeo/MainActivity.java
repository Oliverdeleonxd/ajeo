package com.example.ajeo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    long preBackPressTime;
    /* Number of times to press the return button*/
    long pressTimes;

    TextInputEditText EdCorreo,EdContraseña,EdTelefono,EdNombre;
    Button btnRegistrar;
    TextView tvYatienecuenta;

    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


     EdCorreo = findViewById(R.id.TextInputEditTextCorreo);
     EdContraseña = findViewById(R.id.TextInputEditTextContraseña);
     EdTelefono = findViewById(R.id.TextInputEditTextNumero);
     EdNombre = findViewById(R.id.TextInputEditTextNombre);
     btnRegistrar = findViewById(R.id.btnRegistrar);
     tvYatienecuenta = findViewById(R.id.tvYatienecuenta);


//        PhoneNumberUtils.formatNumber(Editable EdTelefono,int defaultFormattingType);
     progressDialog = new ProgressDialog(this);
     progressDialog.setMessage("Registrando usuario...");

        mAuth = FirebaseAuth.getInstance();

//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            Intent intent = new Intent(MainActivity.this, Home.class);
////            SystemClock.sleep(3000);
//            startActivity(intent);
//        } else {
//            Intent intent = new Intent(MainActivity.this, MainActivity.class);
////            SystemClock.sleep(3000);
//            startActivity(intent);
//        }
//        EdTelefono = PhoneNumberUtils.formatNumber(EdNombre ,"PA");
//        EdTelefono.setText(text);
//        EdTelefono.addTextChangedListener();


//        EdTelefono.addTextChangedListener(new TE() {});







     btnRegistrar.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

             String Correo = EdCorreo.getText().toString().trim();
             String Contraseña = EdContraseña.getText().toString().trim();
             String Telefono = EdTelefono.getText().toString().trim();
             String Nombre = EdNombre.getText().toString().trim();
             Toast.makeText(MainActivity.this, ""+EdNombre.getText().toString().length(), Toast.LENGTH_SHORT).show();
//             formatNumber(String phoneNumber, String defaultCountryIso)
//                            PhoneNumberUtils.formatNumber(EdTelefono, "PA");
//             String text  = PhoneNumberUtils.formatNumber(Telefono,"PA");
//             PA	PAN	591	ISO 3166-2:PA	.pa

//             String text  = PhoneNumberUtils.formatNumber(Telefono,"PA");
//             Toast.makeText(MainActivity.this, ""+text, Toast.LENGTH_SHORT).show();
//             EdTelefono.setText(text);
             if(Nombre.length() <3){
                 EdNombre.setError("Ponte una mas largo :c");
             } else if (!Patterns.EMAIL_ADDRESS.matcher(Correo).matches()) {
                 EdCorreo.setError("Correo invalido :(");
                 EdCorreo.setFocusable(true);
                 EdCorreo.requestFocus();
             }else if (Telefono.length() != 8){
                 EdTelefono.setError("Telefono invalido");
                 EdTelefono.requestFocus();


             }else if(Contraseña.length()<6){
                 EdContraseña.setError("La Contraseña debe ser 6 caracteres o más");
                 EdContraseña.setFocusable(true);
                 EdContraseña.requestFocus();


             }else{
                 registerUser(Correo,Contraseña,Telefono,Nombre);
             }


         }
     });

        tvYatienecuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
            }
        });



    }//onCreate

    private void registerUser(String correo, String contraseña,String telefono,String Nombre) {

       progressDialog.show();

        mAuth.createUserWithEmailAndPassword(correo,contraseña)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Usuario creado Exitosamente", Toast.LENGTH_SHORT).show();
                            Log.d("Registrar usuario", "Usuario creado Exitosamente");
                            FirebaseUser user = mAuth.getCurrentUser();

                            String email = user.getEmail();
                            String uid = user.getUid();

                            HashMap<Object,String> hashMap = new HashMap<>();
                            hashMap.put("email", email);
                            hashMap.put("uid", uid);
                            hashMap.put("name",Nombre);
                            hashMap.put("phone",telefono);
                            hashMap.put("image","");

                            FirebaseDatabase db = FirebaseDatabase.getInstance();

                            DatabaseReference reference = db.getReference("Users");

                            reference.child(uid).setValue(hashMap);



                            startActivity(new Intent(MainActivity.this,Inicio.class));
                            finish();
                           // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                            Log.w("Registrar usuario", "Registro de usuario fallido", task.getException());
//                            updateUI(null);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }//Registrar

    @Override
    public void onBackPressed() {
        long cBackPressTime = SystemClock.uptimeMillis();
        if (cBackPressTime - preBackPressTime < 3000) {
            pressTimes++;
            if (pressTimes >= 2) {
                super.onBackPressed();
                finish();
                finishAffinity();
            }
        } else {
            pressTimes = 1;
        }
        if (pressTimes == 1) {
            Toast. makeText (this,"Presion salir de nuevo para salir",Toast.LENGTH_SHORT).show ();
        }
        preBackPressTime = cBackPressTime;

    }



//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
//    }
}//MainActivity