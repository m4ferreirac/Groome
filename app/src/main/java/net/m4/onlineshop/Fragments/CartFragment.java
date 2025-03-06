package net.m4.onlineshop.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.m4.onlineshop.Adapter.CartAdapter;
import net.m4.onlineshop.Enums.OrderStatus;
import net.m4.onlineshop.Models.ItemCart;
import net.m4.onlineshop.Models.Order;
import net.m4.onlineshop.databinding.FragmentCartBinding;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment implements CartAdapter.CartUpdateListener{
    private FragmentCartBinding binding;
    private CartAdapter cartAdapter;
    private ArrayList<ItemCart> cartList;
    private double totalSubtotal = 0.0;
    private double totalDeliveryFee = 0.0;
    private double totalPrice = 0.0;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.cartView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("cart", getContext().MODE_PRIVATE);
        String json = sharedPreferences.getString("cart_items", "");

        if (json != null && !json.isEmpty()) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<ItemCart>>() {}.getType();
            cartList = gson.fromJson(json, type);
        } else {
            cartList = new ArrayList<>();
        }

        calculateCartTotals();

        binding.subTotalTxt.setText(String.format("%.2f €", totalSubtotal));
        binding.deliveryFeeTxt.setText(String.format("%.2f €", totalDeliveryFee));
        binding.totalTxt.setText(String.format("%.2f €", totalPrice));

        cartAdapter = new CartAdapter(cartList, this);
        binding.cartView.setAdapter(cartAdapter);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("cart", getContext().MODE_PRIVATE);
        String json = sharedPreferences.getString("cart_items", "");

        if (json != null && !json.isEmpty()) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<ItemCart>>() {}.getType();
            cartList = gson.fromJson(json, type);
        } else {
            cartList = new ArrayList<>();
        }

        calculateCartTotals();

        binding.subTotalTxt.setText(String.format("%.2f €", totalSubtotal));
        binding.deliveryFeeTxt.setText(String.format("%.2f €", totalDeliveryFee));
        binding.totalTxt.setText(String.format("%.2f €", totalPrice));

        cartAdapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.checkoutBtn.setOnClickListener(v -> createOrder());
    }

    private void createOrder() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("cart", getContext().MODE_PRIVATE);
        String cartJson = sharedPreferences.getString("cart_items", "[]");

        if (cartJson == null || cartJson.equals("[]")) {
            Toast.makeText(getContext(), "Cart is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ItemCart>>() {}.getType();
        ArrayList<ItemCart> cartItems = gson.fromJson(cartJson, type);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Orders");
        String orderId = ordersRef.push().getKey();

        Order order = new Order(
                orderId,
                userId,
                cartItems,
                totalPrice,
                getCurrentDate(),
                OrderStatus.PENDING
        );

        if (orderId != null) {
            ordersRef.child(orderId).setValue(order).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show();
                    cartAdapter.clearCart();
                    binding.subTotalTxt.setText("0.00 €");
                    binding.deliveryFeeTxt.setText("0.00 €");
                    binding.totalTxt.setText("0.00 €");
                } else {
                    Toast.makeText(getContext(), "Error placing the order.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    private void calculateCartTotals() {
        totalSubtotal = 0.0;
        totalDeliveryFee = 0.0;
        totalPrice = 0.0;

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("cart", getContext().MODE_PRIVATE);
        String cartJson = sharedPreferences.getString("cart_items", "[]");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ItemCart>>() {}.getType();
        ArrayList<ItemCart> cartItems = gson.fromJson(cartJson, type);

        for (ItemCart item : cartItems) {
            double productPrice = item.getPrice();
            int quantityKg = (int) Math.round(item.getQuantityKg());
            double subtotal = quantityKg * productPrice;
            double deliveryFee = quantityKg * 0.10;
            double totalItem = subtotal + deliveryFee;

            totalSubtotal += subtotal;
            totalDeliveryFee += deliveryFee;
            totalPrice += totalItem;
        }

        binding.subTotalTxt.setText(String.format("%.2f €", totalSubtotal));
        binding.deliveryFeeTxt.setText(String.format("%.2f €", totalDeliveryFee));
        binding.totalTxt.setText(String.format("%.2f €", totalPrice));
    }

    @Override
    public void onCartUpdated() {
        calculateCartTotals();
        binding.subTotalTxt.setText(String.format("%.2f €", totalSubtotal));
        binding.deliveryFeeTxt.setText(String.format("%.2f €", totalDeliveryFee));
        binding.totalTxt.setText(String.format("%.2f €", totalPrice));
    }
}