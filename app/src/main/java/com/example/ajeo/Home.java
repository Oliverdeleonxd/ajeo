package com.example.ajeo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity {

    TextView name,mail;
    Button logout;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();

        name=(TextView)findViewById(R.id.nombre);
        mail=(TextView)findViewById(R.id.mail);
        logout=(Button) findViewById(R.id.logout);

        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc= GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount account =GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            String nameA =account.getDisplayName();
            String mailL =account.getEmail();

            name.setText(nameA);
            mail.setText(mailL);
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignOut();

                firebaseAuth.signOut();
                checkUserStatus();

            }
        });


    }

    private void SignOut() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>(){
            @Override
            public void onComplete(@NonNull Task<Void> task){
                finish();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));


            }
        });

    }


    private void checkUserStatus() {

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){
            name.setText(user.getEmail());

        }else{
            startActivity(new Intent(Home.this,MainActivity.class));
            finish();
        }

    }// checkUserStatus


    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }
}//home