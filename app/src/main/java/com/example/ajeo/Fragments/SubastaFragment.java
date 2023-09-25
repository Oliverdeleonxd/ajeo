package com.example.ajeo.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ajeo.Adapter.BusquedaAdapter;
import com.example.ajeo.Adapter.PostAdapter;
import com.example.ajeo.Adapter.SubastaApdapter;
import com.example.ajeo.Models.ModelPost;
import com.example.ajeo.PublicarSubastaActivity;
import com.example.ajeo.R;
import com.example.ajeo.SubastaActivity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SubastaFragment extends Fragment {



    RecyclerView RecyclerViewSubasta;

    FirebaseAuth mAuth;
    List<ModelPost> postList;
    SubastaApdapter subastaAdapter;
//    PostAdapter postAdapter;

//    RecyclerView RecyclerViewSubasta;

    GridLayoutManager layoutManager;

    SearchView toolbarSearch;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_subasta, container, false);





        mAuth = FirebaseAuth.getInstance();

        postList=new ArrayList<>();

        final FloatingActionButton btnSubasta;

        RecyclerViewSubasta = root.findViewById(R.id.RecyclerViewSubasta);
        btnSubasta = root.findViewById(R.id.btnSubastar);


        btnSubasta.bringToFront();



        btnSubasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Subastar", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), PublicarSubastaActivity.class);
                startActivity(intent);


            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        RecyclerViewSubasta.setLayoutManager(layoutManager);



        CargarSubastas();



        return root;
    }//Oncreate




    private void CargarSubastas() {

        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("Subastas");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                if (snapshot.exists()) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ModelPost modelPost = dataSnapshot.getValue(ModelPost.class);

                        assert modelPost != null;
                            postList.add(modelPost);


                        subastaAdapter = new SubastaApdapter(getActivity(),postList);
                        RecyclerViewSubasta.setAdapter(subastaAdapter);
                        subastaAdapter.notifyDataSetChanged();

                    }
                    subastaAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error: "+error, Toast.LENGTH_SHORT).show();

            }
        });

    }//Productos




}//Subasta Fragment