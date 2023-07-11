package june.internship;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

public class CartFragment extends Fragment {

    SQLiteDatabase db;
    SharedPreferences sp;

    RecyclerView recyclerView;
    ArrayList<ProductList> arrayList;

    String[] prodId = {"1", "2", "3", "4", "5", "6", "7"};
    String[] prodName = {"Butter", "Cloth", "Bread", "Makup Kit", "Cloth", "Bread", "Makup Kit"};

    public static Button checkout;

    public static int iTotal = 0;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        db = getActivity().openOrCreateDatabase("JuneInternship", Context.MODE_PRIVATE, null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT INT(10),PASSWORD VARCHAR(20),DOB VARCHAR(10),GENDER VARCHAR(10),CITY VARCHAR(50))";
        db.execSQL(tableQuery);

        String wishlistTableQuery = "CREATE TABLE IF NOT EXISTS WISHLIST(WISHLISTID INTEGER PRIMARY KEY AUTOINCREMENT,USERID INT(10),PRODUCTID INT(10),PRICE VARCHAR(10),UNIT VARCHAR(10),DESCRIPTION TEXT,IMAGE VARCHAR(100))";
        db.execSQL(wishlistTableQuery);

        String cartTableQuery = "CREATE TABLE IF NOT EXISTS CART(CARTID INTEGER PRIMARY KEY AUTOINCREMENT,ORDERID INT(10),USERID INT(10),PRODUCTID INT(10),PRICE VARCHAR(10),UNIT VARCHAR(10),DESCRIPTION TEXT,IMAGE VARCHAR(100),QTY INT(10),TOTALPRICE VARCHAR(50))";
        db.execSQL(cartTableQuery);

        sp = getActivity().getSharedPreferences(ConstantData.PREF, Context.MODE_PRIVATE);

        checkout = view.findViewById(R.id.cart_checkout);

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString(ConstantData.CART_TOTAL, String.valueOf(iTotal)).commit();
                Intent intent = new Intent(getActivity(), ShippingActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = view.findViewById(R.id.cart_recyclerview);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        String cartQuery = "SELECT * FROM CART WHERE USERID='" + sp.getString(ConstantData.USERID, "") + "' AND ORDERID='0'";
        Cursor cursor = db.rawQuery(cartQuery, null);
        if (cursor.getCount() > 0) {
            iTotal = 0;
            checkout.setVisibility(View.VISIBLE);
            arrayList = new ArrayList<>();
            while (cursor.moveToNext()) {
                ProductList list = new ProductList();
                list.setProductId(cursor.getString(3));
                for (int i = 0; i < prodId.length; i++) {
                    if (cursor.getString(3).equals(prodId[i])) {
                        list.setName(prodName[i]);
                        break;
                    }
                }
                list.setPrice(cursor.getString(4));
                list.setUnit(cursor.getString(5));
                list.setDescription(cursor.getString(6));
                list.setImage(Integer.parseInt(cursor.getString(7)));

                list.setAddedCart(true);

                String checkWishlistQuery = "SELECT * FROM WISHLIST WHERE PRODUCTID='" + cursor.getString(3) + "' AND USERID='" + sp.getString(ConstantData.USERID, "") + "'";
                Cursor cursorWishlist = db.rawQuery(checkWishlistQuery, null);
                if (cursorWishlist.getCount() > 0) {
                    list.setAddedWishlist(true);
                } else {
                    list.setAddedWishlist(false);
                }
                list.setQty(cursor.getString(8));
                list.setTotalPrice(cursor.getString(9));
                iTotal += Integer.parseInt(cursor.getString(9));
                //iTotal =  iTotal + cursor.getString(9);

                arrayList.add(list);
            }
            CartAdapter adapter = new CartAdapter(getActivity(), arrayList);
            recyclerView.setAdapter(adapter);

            checkout.setText("Checkout With " + ConstantData.PRICE_SYMBOL + iTotal);

        } else {
            checkout.setVisibility(View.GONE);
        }
        return view;
    }
}