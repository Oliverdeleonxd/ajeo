package com.example.ajeo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.ajeo.Fragments.FavoritosFragment;
import com.example.ajeo.Fragments.Ingresar;
import com.example.ajeo.Fragments.InicioFragment;
import com.example.ajeo.Fragments.PerfilFragment;
import com.example.ajeo.Fragments.PublicarFragment;
import com.example.ajeo.Fragments.SubastaFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
//import meow.bottomnavigation.MeowBottomNavigation;


public class Inicio extends AppCompatActivity {

    long preBackPressTime;
    /* Number of times to press the return button*/
    long pressTimes;

    MeowBottomNavigation meowBottomNavigation;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);


//        meowBottomNavigation = (MeowBottomNavigation) findViewById(R.id.bottom_nav);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        meowBottomNavigation = findViewById(R.id.meowBottomNavigation);
//        meowBottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.ic_house));
//        meowBottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.ic_house));

//        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)
//                meowBottomNavigation.getLayoutParams();
//        layoutParams.setBehavior(new BottomNavigationViewBehavior());



        //add menu items to bottom nav
        meowBottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_house));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_bag));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_round_add_24));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_favorite));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(5, R.drawable.ic_person));

       // meowBottomNavigation.show(1,true);

        replace(new InicioFragment());
        //set bottom nav on show listener
        meowBottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId()){
                    case 1:
                        replace(new InicioFragment());
                        break;

                    case 2:
                        replace(new SubastaFragment());
                        break;

                    case 3:
                        if (currentUser != null) {
                            replace(new PublicarFragment());
                        } else {
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.frameLayout, new Ingresar());
                            transaction.addToBackStack(null);
                            // Commit a la transacción
                            transaction.commit();
                        }

                        break;

                    case 4:
                        replace(new FavoritosFragment());
                        break;
                    case 5:
                        if (currentUser != null) {
                            replace(new PerfilFragment());
                        } else {
                            replace(new Ingresar());
                        }
                        break;
                    default:
                        replace(new InicioFragment());
                        break;

                }
                return null;
            }
        });

        //set the initial fragment to show
        meowBottomNavigation.show(1, true);




        //set menu item on click listener
//        meowBottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
//            @Override
//            public void onClickItem(MeowBottomNavigation.Model item) {
//                //display a toast
//                Toast.makeText(getApplicationContext()," You clicked "+ item.getId(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        //set on reselect listener
//        meowBottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
//            @Override
//            public void onReselectItem(MeowBottomNavigation.Model item) {
//                //display a toast
//                Toast.makeText(getApplicationContext()," You reselected "+ item.getId(), Toast.LENGTH_SHORT).show();
//            }
//        });

        //set count to dashboard item
       // meowBottomNavigation.setCount(3, "10");

    }//onCreate

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        View decoView = getWindow().getDecorView();
        if (hasFocus){
            decoView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE|
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                            |View.SYSTEM_UI_FLAG_FULLSCREEN
//                            |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

            );

        }

//            |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN


    }//onWindowFocusChangued

    private void replace(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout,fragment);
        transaction.commit();
    }

    //define a load method to feed the screen
//    private void loadFragment(Fragment fragment) {
//        //replace the fragment
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.nav_host_fragment_container,fragment, null)
//                .commit();
//    }


    @Override
    public void onBackPressed() {

//        finish();
//        System.exit(0);

        long cBackPressTime = SystemClock.uptimeMillis();
        if (cBackPressTime - preBackPressTime < 3000) {
            pressTimes++;
            if (pressTimes >= 2) {
                super.onBackPressed();
                finish();
            }
        } else {
            pressTimes = 1;
        }
        if (pressTimes == 1) {
            Toast. makeText (this,"Presion salir de nuevo para salir",Toast.LENGTH_SHORT).show ();
        }
        preBackPressTime = cBackPressTime;

    }

}//Inicio