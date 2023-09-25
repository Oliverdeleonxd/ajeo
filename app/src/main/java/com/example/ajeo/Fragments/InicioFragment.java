package com.example.ajeo.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ajeo.Adapter.BusquedaAdapter;
import com.example.ajeo.Adapter.PostAdapter;
import com.example.ajeo.Models.ModelPost;
import com.example.ajeo.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class InicioFragment extends Fragment {

    FirebaseAuth mAuth;
    List<ModelPost> postList;

    BusquedaAdapter busquedaAdapter;
    PostAdapter postAdapter;

    RecyclerView homeRecyclerView;

    GridLayoutManager layoutManager;

    SearchView Search;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root= inflater.inflate(R.layout.fragment_inicio, container, false);

        mAuth = FirebaseAuth.getInstance();

        postList=new ArrayList<>();

        homeRecyclerView = root.findViewById(R.id.RecyclerViewInicio);

        Search = root.findViewById(R.id.toolbar_search);
//        layoutManager = new GridLayoutManager(getContext(),2);

//        layoutManager.setStackFromEnd(true);
//        layoutManager.setReverseLayout(true);

//        homeRecyclerView.setLayoutManager(layoutManager);
//        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

//        sear.setOnQueryTextListener(new SearchView.OnQueryTextListener){
//
//        }

       // postList = new ArrayList<>();
        
        loadPosts();

        Search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.isEmpty()){
                    loadPosts();
                }else {
                    searchPosts(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.isEmpty()){
                    loadPosts();
                    postAdapter.startListening();
                }else {
                   searchPosts(s);
                }

                return false;
            }
        });


//        ServerValue time = new ServerValue();
//        Map<String, String> timesS = ServerValue.TIMESTAMP;
//
////        String tim = ServerValue.TIMESTAMP;
//
//        Toast.makeText(getContext(), "time "+ timesS.toString(), Toast.LENGTH_SHORT).show();


//        postAdapter = new PostAdapter(getActivity(), postList);
//        homeRecyclerView.setAdapter(postAdapter);

//        postAdapter = new PostAdapter(postList);
//        homeRecyclerView.setAdapter(postAdapter);

//        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("Posts");
//        mReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                //  postList.clear();
//                if (snapshot.exists()) {
//
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                        ModelPost modelPost = dataSnapshot.getValue(ModelPost.class);
//
//                        postList.add(modelPost);
//
////                        postAdapter = new PostAdapter(getActivity(), postList);
////                        homeRecyclerView.setAdapter(postAdapter);
//
//                    }
//                    postAdapter.notifyDataSetChanged();
//                }//end if
//
//            }
//            @Override public void onCancelled(@NonNull DatabaseError error) {
//
//                Toast.makeText(getContext(), "Error: "+error, Toast.LENGTH_SHORT).show();
//
//            }
//        });


//        homeRecyclerView.setLayoutManager(layoutManager);
//        homeRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


//        busquedaAdapter = new BusquedaAdapter(getActivity(),postList);

        return root;
    }//onCreateView



//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//
//        inflater.inflate(R.menu.searchbar, menu);
//
//        MenuItem item = menu.findItem(R.id.search_bar);
//
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                if (!TextUtils.isEmpty(query)){
//                    searchPosts(query);
//                }else {
//                     loadPosts();
//                }
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String query) {
//                if (!TextUtils.isEmpty(query)){
//                    searchPosts(query);
//                }else {
//                    loadPosts();
//                }
//                return false;
//            }
//        });
//        super.onCreateOptionsMenu(menu,inflater);
//

//        MenuInflater inflate = requireActivity().getMenuInflater();
//        inflate.inflate(R.menu.searchbar, menu);
//
//        MenuItem.OnActionExpandListener OnActionExpandListener = new MenuItem.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//                return false;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//                return false;
//            }
//        };

//        return true;

//        super.onCreateOptionsMenu(menu, inflater);
//    }

    private void searchPosts(String queryi) {

        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("Posts");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                if (snapshot.exists()) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ModelPost modelPost = dataSnapshot.getValue(ModelPost.class);

                        assert modelPost != null;
                        if (modelPost.getTitulo().toLowerCase().contains(queryi.toLowerCase())||
                                modelPost.getDescripcion().toLowerCase().contains(queryi.toLowerCase())||modelPost.getRaza().toLowerCase().contains(queryi.toLowerCase())){
                            postList.add(modelPost);

                        }


                        busquedaAdapter = new BusquedaAdapter(getActivity(),postList);
                        homeRecyclerView.setAdapter(busquedaAdapter);
                        busquedaAdapter.notifyDataSetChanged();

                    }
                    busquedaAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getContext(), "Error: "+error, Toast.LENGTH_SHORT).show();

            }
        });



    }//SearchPost



    private void loadPosts() {
//        homeRecyclerView.setLayoutManager(layoutManager);
        homeRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<ModelPost> options =
                new FirebaseRecyclerOptions.Builder<ModelPost>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Posts"), ModelPost.class)
                        .build();

//        postList.add(modelPost);
        postAdapter = new PostAdapter(options,getActivity());
        postAdapter.notifyDataSetChanged();
        homeRecyclerView.setAdapter(postAdapter);


    }//loadPosts

    @Override
    public void onStart() {
        super.onStart();
        postAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        postAdapter.stopListening();
    }

class WrapContentLinearLayoutManager extends GridLayoutManager {
        public WrapContentLinearLayoutManager(Context context) {
            super(context,2);
        }

        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation);
        }

        public WrapContentLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("TAG", "meet a IOOBE in RecyclerView");
            }
        }
    }

}