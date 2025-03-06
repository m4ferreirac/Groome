package net.m4.onlineshop.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.m4.onlineshop.Adapter.CartAdapter;
import net.m4.onlineshop.Adapter.WishListAdapter;
import net.m4.onlineshop.Domain.ItemDomain;
import net.m4.onlineshop.Models.ItemCart;
import net.m4.onlineshop.R;
import net.m4.onlineshop.databinding.FragmentCartBinding;
import net.m4.onlineshop.databinding.FragmentWishListBinding;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WishListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WishListFragment extends Fragment {
    private FragmentWishListBinding binding;
    private WishListAdapter wishListAdapter;
    private ArrayList<ItemDomain> wishListItems = new ArrayList<>();
    private ArrayList<ItemDomain> filteredList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WishListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WishListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WishListFragment newInstance(String param1, String param2) {
        WishListFragment fragment = new WishListFragment();
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
        binding = FragmentWishListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.wishlistView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        wishListAdapter = new WishListAdapter(filteredList, getContext());
        binding.wishlistView.setAdapter(wishListAdapter);

        fetchWishListItems();

        binding.searchbarTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filterItems(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchWishListItems();
    }

    private void fetchWishListItems() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference wishlistRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(userId)
                .child("wishlist");

        wishlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                wishListItems.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ItemDomain item = snapshot.getValue(ItemDomain.class);
                    if (item != null) {
                        wishListItems.add(item);
                    }
                }

                filteredList.clear();
                filteredList.addAll(wishListItems);

                wishListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void filterItems(String query) {
        filteredList.clear();

        if (query.isEmpty()) {
            filteredList.addAll(wishListItems);
        } else {
            for (ItemDomain item : wishListItems) {
                if (item.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }

        wishListAdapter.notifyDataSetChanged();
    }

}