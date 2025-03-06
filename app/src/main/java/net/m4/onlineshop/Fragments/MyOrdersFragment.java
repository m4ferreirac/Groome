package net.m4.onlineshop.Fragments;

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

import net.m4.onlineshop.Adapter.OrderAdapter;
import net.m4.onlineshop.Adapter.WishListAdapter;
import net.m4.onlineshop.Domain.ItemDomain;
import net.m4.onlineshop.Models.Order;
import net.m4.onlineshop.R;
import net.m4.onlineshop.databinding.FragmentMyOrderBinding;
import net.m4.onlineshop.databinding.FragmentWishListBinding;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyOrdersFragment extends Fragment {
    private FragmentMyOrderBinding binding;
    private OrderAdapter orderAdapter;
    private ArrayList<Order> ordersList = new ArrayList<>();
    private ArrayList<Order> filteredList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyOrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyOrdersFragment newInstance(String param1, String param2) {
        MyOrdersFragment fragment = new MyOrdersFragment();
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
        binding = FragmentMyOrderBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.ordersView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        orderAdapter = new OrderAdapter(filteredList, getContext());
        binding.ordersView.setAdapter(orderAdapter);

        fetchOrders();

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

    private void fetchOrders() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Orders");

        ordersRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ordersList.clear();

                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    String orderId = orderSnapshot.getKey();
                    String status = orderSnapshot.child("status").getValue(String.class);
                    Order order = new Order(orderId, status, userId);
                    ordersList.add(order);
                }

                filteredList.clear();
                filteredList.addAll(ordersList);
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void filterItems(String query) {
        filteredList.clear();

        if (query.isEmpty()) {
            filteredList.addAll(ordersList);
        } else {
            for (Order order : ordersList) {
                if (order.getOrderId().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(order);
                }
            }
        }

        orderAdapter.notifyDataSetChanged();
    }
}