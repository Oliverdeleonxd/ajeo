package com.example.ajeo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;


public class LoginActivity extends AppCompatActivity {

    long preBackPressTime;
    /* Number of times to press the return button*/
    long pressTimes;


    ImageView google_img;
    SignInButton btnGoogleLogin;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;


    TextInputEditText EdCorreo,EdContraseña;
    Button btnIniciarSesion;
    TextView tvNotienecuenta,tv_OlvidarContraseña;

    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        EdCorreo = findViewById(R.id.TextInputEditTextCorreo);
        EdContraseña = findViewById(R.id.TextInputEditTextContraseña);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        tvNotienecuenta = findViewById(R.id.tvNotienecuenta);
        tv_OlvidarContraseña = findViewById(R.id.tv_OlvidarContraseña);

        btnGoogleLogin= findViewById(R.id.btngoogle);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Iniciando sesion");



        //google_img =(ImageView)findViewById(R.id.google);

        //GoogleSignInOptions
        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this,gso);


//        google_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//              //  SignIn();
//            }
//        });
        mAuth = FirebaseAuth.getInstance();

        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SignIn();
                Intent intent = gsc.getSignInIntent();
                startActivityForResult(intent,100);
            }
        });

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Correo = EdCorreo.getText().toString().trim();
                String Contraseña = EdContraseña.getText().toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(Correo).matches()) {
                    EdCorreo.setError("Correo invalido :(");
                    EdCorreo.setFocusable(true);
                }else if(Contraseña.length()<6){
                    EdContraseña.setError("La Contraseña debe ser 6 caracteres o más");
                    EdContraseña.setFocusable(true);

                }else{
                    LoginUser(Correo,Contraseña);
                }



            }
        });

        tvNotienecuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();
            }
        });

        tv_OlvidarContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecoverPassword();

            }
        });

    }//onCreate

    private void showRecoverPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recuperar contraseña");

        LinearLayout linearLayout = new LinearLayout(this);

        TextInputEditText Etemail = new TextInputEditText(this);
        Etemail.setHint("Correo");
        Etemail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        Etemail.setMinEms(16);

        linearLayout.addView(Etemail);
        linearLayout.setPadding(10,10,10,10);

        builder.setView(linearLayout);

        builder.setPositiveButton("Recuperar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String email = Etemail.getText().toString();
                beginRecovery(email);


            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        builder.create().show();


    }//show Recover

    // enviar correo de recuperacion
    private void beginRecovery(String email) {

        progressDialog.setMessage("Enviando Correo...");
        progressDialog.show();

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                progressDialog.dismiss();
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Correo Enviado", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LoginActivity.this, "Fallo al enviar correo ", Toast.LENGTH_SHORT).show();

                }

            }//onComplete
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });

    }//beginRecovery

    private void LoginUser(String correo, String contraseña) {
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Inicio de Sesion Exitosamente", Toast.LENGTH_SHORT).show();
                            Log.d("Inicio de Sesion", "Inicio de Sesion Exitosamente");
                            FirebaseUser user = mAuth.getCurrentUser();

//                            if (task.getResult().getAdditionalUserInfo().isNewUser()){
//                                // firebase database
//                                String email = user.getEmail();
//                                String uid = user.getUid();
//
//                                HashMap<Object,String> hashMap = new HashMap<>();
//                                hashMap.put("email", email);
//                                hashMap.put("uid", uid);
//                                hashMap.put("name","");
//                                hashMap.put("phone","");
//                                hashMap.put("image","");
//
//                                FirebaseDatabase db = FirebaseDatabase.getInstance();
//                                DatabaseReference reference = db.getReference("Users");
//                                reference.child(uid).setValue(hashMap);
//
//                                ////////////////////////////////
//                            }



                            startActivity(new Intent(LoginActivity.this,Inicio.class));
                            finish();
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "", Toast.LENGTH_SHORT).show();
                            Log.w("Logear usuario", "Inicio de Sesion de usuario fallido", task.getException());
//                            updateUI(null);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }//LoginUser

    private void SignIn() {

//        Intent intent = gsc.getSignInIntent();
//        startActivityForResult(intent,100);
      //  startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode==100){
            Task<GoogleSignInAccount> task= GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
//                SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
//                String idToken = credential.getGoogleIdToken();
//                if (idToken !=  null) {
//                    // Got an ID token from Google. Use it to authenticate
//                    // with Firebase.
//                    Log.d(TAG, "Got ID token.");

               // SignInCredential googleCredential = oneTapClient.getSignInCredentialFromIntent(data);
              //  String idToken = googleCredential.getGoogleIdToken();

                GoogleSignInAccount account = task.getResult(ApiException.class);
//                task.getResult(ApiException.class);
//                FirebaseAuth.getInstance().signInWithCredential();
//                HomeActivity();
                firebaseAuthWithGoogle(account);


            }catch (ApiException e){
                Toast.makeText(this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }//onActivityResult

//    private void HomeActivity() {
//
//        Intent intent=new Intent(getApplicationContext(),Home.class);
//        startActivity(intent);
//        finish();
//    }

private void firebaseAuthWithGoogle(GoogleSignInAccount account){

//    //SignInCredential googleCredential = oneTapClient.getSignInCredentialFromIntent(data);
//    String idToken = googleCredential.getGoogleIdToken();
    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);

//    Toast.makeText(this, "credential "+account.getIdToken(), Toast.LENGTH_SHORT).show();
//    Log.d("Login", "firebaseAuthWithGoogle: " + credential);

//    String idToken = .getGoogleIdToken();
    mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
          if (task.isSuccessful()){

//              FirebaseUser user = mAuth.getCurrentUser();
              boolean newuser = task.getResult().getAdditionalUserInfo().isNewUser();
              if (newuser) {
                  Toast.makeText(LoginActivity.this, "new", Toast.LENGTH_SHORT).show();
              }else {
                  Toast.makeText(LoginActivity.this, "not new", Toast.LENGTH_SHORT).show();

                  finish();
                  finishAffinity();
                  try {
                      finalize();
                  } catch (Throwable throwable) {
                      throwable.printStackTrace();
                  }
                  startActivity(new Intent(LoginActivity.this,Inicio.class));

              }
//              mAuth.getCurrentUser().getEmail();



//              if(user != null){
//                  Toast.makeText(LoginActivity.this, "Bienvenido de nuevo ", Toast.LENGTH_SHORT).show();
////                  startActivity(new Intent(LoginActivity.this,Inicio.class));
////                  finish();
//              }
//              else{
//
//                  Toast.makeText(LoginActivity.this, "No esta en la bd ", Toast.LENGTH_SHORT).show();
//
//              }

//              boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
//              if (isNewUser) {
//                  Toast.makeText(LoginActivity.this, "nuevo ", Toast.LENGTH_SHORT).show();
//
//              } else {
//                  Toast.makeText(LoginActivity.this, "Bienvenido de nuevo ", Toast.LENGTH_SHORT).show();
////                  startActivity(new Intent(LoginActivity.this,Inicio.class));
////                  finish();
//              }


//              if (!task.isSuccessful()) {
//
//              try {
//                  throw Objects.requireNonNull(task.getException());
//              } catch (UserRecoverableAuthException userRecoverableException) {
//                  // GooglePlayServices.apk is either old, disabled, or not present
//                  // so we need to show the user some UI in the activity to recover.
//                  //mActivity.handleException(userRecoverableException);
//                  Log.e("Inicio de Sesion", userRecoverableException.getMessage(), userRecoverableException);
////                  FirebaseCrashlytics.getInstance().recordException(userRecoverableException);
//                  Toast.makeText(LoginActivity.this, "Actualice los servicios de Google Play o revise que estén instalados.", Toast.LENGTH_LONG).show();
//              } catch (GoogleAuthException fatalException) {
//                  // Some other type of unrecoverable exception has occurred.
//                  // Report and log the error as appropriate for your app.
//                  Log.e("Inicio de Sesion", fatalException.getMessage(), fatalException);
////                  FirebaseCrashlytics.getInstance().recordException(fatalException);
//                  Toast.makeText(LoginActivity.this, fatalException.getMessage(), Toast.LENGTH_LONG).show();
//              } catch (Exception e) {
////                  FirebaseCrashlytics.getInstance().recordException(e);
//                  e.printStackTrace();
//                  Log.e("Inicio de Sesion", e.getMessage(), e);
//                  Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//              }
//
//              } else {
////                  FirebaseUser user = mAuth.getCurrentUser();
//                  Toast.makeText(LoginActivity.this, " mAuth.getCurrentUser();.", Toast.LENGTH_LONG).show();
//
//
//                  assert user != null;
//
////                  askForTypeOfUser(user);
//              }





//              String email = user.getEmail();
//              String uid = user.getUid();
////              String name = account.getDisplayName();
////              String image = account.getPhotoUrl().toString();
//
//              HashMap<Object,String> hashMap = new HashMap<>();
//              hashMap.put("email", email);
//              hashMap.put("uid", uid);
////              hashMap.put("name",name);
////              hashMap.put("phone","");
////              hashMap.put("image",image);

              FirebaseDatabase db = FirebaseDatabase.getInstance();

              DatabaseReference reference = db.getReference("Users");
//              updateUI(user);

//              reference.child(uid).setValue(hashMap);


//              Toast.makeText(LoginActivity.this, ""+user.getEmail(), Toast.LENGTH_SHORT).show();


          }else {


              Toast.makeText(LoginActivity.this,"Inicio con credenciales fallido "+task.getException(),Toast.LENGTH_SHORT).show();
          }

        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(LoginActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

        }
    });


}//firebaseAuthWithGoogle


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        reload();

//        updateUI(currentUser);
    }



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
}//LoginActivity