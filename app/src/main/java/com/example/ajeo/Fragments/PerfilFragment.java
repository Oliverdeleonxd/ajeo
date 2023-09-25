package com.example.ajeo.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import com.example.ajeo.EditProfileActivity;
import com.example.ajeo.Home;
import com.example.ajeo.R;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;


public class PerfilFragment extends Fragment {

    Toolbar toolbar;

    FirebaseUser user;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;

    TextView tvNombre, tvTelefono, tvCorreo;

    ImageButton btImgUser,btnEdit;

    private MaterialTextView tvAbout, tvMoreState;
    private boolean moreState = false;

    View root;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
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
//        init();
         root= inflater.inflate(R.layout.fragment_perfil, container, false);
//
//          toolbar= root.findViewById(R.id.toolbarr);
//          AppCompatActivity activity = (AppCompatActivity) getActivity();
//         activity.setSupportActionBar(toolbar);
//          activity.getSupportActionBar().setTitle("HOLA");
//          activity.getSupportActionBar().setLogo(R.drawable.ic_round_edit_24);
//        activity.getSupportActionBar().set


        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbarr);
//        toolbar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "Hola", Toast.LENGTH_SHORT).show();
//            }
//        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    finalize();
//                    getActivity().finish();
                    Intent intent = new Intent(getContext(),Home.class);
                    startActivity(intent);

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }


            }
        });

         mAuth = FirebaseAuth.getInstance();
         user = mAuth.getCurrentUser();

         mDatabase = FirebaseDatabase.getInstance();
         mReference = mDatabase.getReference("Users");

         tvNombre = (TextView)root.findViewById(R.id.tvNameUser);
         tvTelefono = (TextView)root.findViewById(R.id.tvTelefono);
         tvCorreo = (TextView)root.findViewById(R.id.tvCorreo);
         btImgUser =root.findViewById(R.id.imageUser);
         btnEdit = root.findViewById(R.id.btnEdit);

         btnEdit.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(getContext(), EditProfileActivity.class);
                 startActivity(intent);

             }
         });

        Query query = mReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()){
                    String name = "" +ds.child("name").getValue();
                    String email = "" +ds.child("email").getValue();
                    String Telefono = "" +ds.child("phone").getValue();
                    String image = "" +ds.child("image").getValue();

                    tvCorreo.setText(email);
                    tvNombre.setText(name);
                    String text  = PhoneNumberUtils.formatNumber(Telefono,"PA");
                    tvTelefono.setText(text);

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


        return root;
    }


}