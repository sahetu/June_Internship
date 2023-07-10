package june.internship;

import android.content.Context;
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

import java.util.ArrayList;

public class WishlistFragment extends Fragment {

    SQLiteDatabase db;
    SharedPreferences sp;

    RecyclerView recyclerView;
    ArrayList<ProductList> arrayList;

    String[] prodId = {"1", "2", "3", "4", "5", "6", "7"};
    String[] prodName = {"Butter", "Cloth", "Bread", "Makup Kit", "Cloth", "Bread", "Makup Kit"};

    public WishlistFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);

        db = getActivity().openOrCreateDatabase("JuneInternship", Context.MODE_PRIVATE, null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT INT(10),PASSWORD VARCHAR(20),DOB VARCHAR(10),GENDER VARCHAR(10),CITY VARCHAR(50))";
        db.execSQL(tableQuery);

        String wishlistTableQuery = "CREATE TABLE IF NOT EXISTS WISHLIST(WISHLISTID INTEGER PRIMARY KEY AUTOINCREMENT,USERID INT(10),PRODUCTID INT(10),PRICE VARCHAR(10),UNIT VARCHAR(10),DESCRIPTION TEXT,IMAGE VARCHAR(100))";
        db.execSQL(wishlistTableQuery);

        String cartTableQuery = "CREATE TABLE IF NOT EXISTS CART(CARTID INTEGER PRIMARY KEY AUTOINCREMENT,ORDERID INT(10),USERID INT(10),PRODUCTID INT(10),PRICE VARCHAR(10),UNIT VARCHAR(10),DESCRIPTION TEXT,IMAGE VARCHAR(100),QTY INT(10),TOTALPRICE VARCHAR(50))";
        db.execSQL(cartTableQuery);

        sp = getActivity().getSharedPreferences(ConstantData.PREF, Context.MODE_PRIVATE);

        recyclerView = view.findViewById(R.id.wishlist_recycler);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        String wishlistQuery = "SELECT * FROM WISHLIST WHERE USERID='" + sp.getString(ConstantData.USERID, "") + "'";
        Cursor cursor = db.rawQuery(wishlistQuery, null);
        if (cursor.getCount() > 0) {
            arrayList = new ArrayList<>();
            while (cursor.moveToNext()) {
                ProductList list = new ProductList();
                list.setProductId(cursor.getString(2));
                for (int i = 0; i < prodId.length; i++) {
                    if (cursor.getString(2).equals(prodId[i])) {
                        list.setName(prodName[i]);
                        break;
                    }
                }
                list.setPrice(cursor.getString(3));
                list.setUnit(cursor.getString(4));
                list.setDescription(cursor.getString(5));
                list.setImage(Integer.parseInt(cursor.getString(6)));

                String checkCartQuery = "SELECT * FROM CART WHERE PRODUCTID='" + cursor.getString(2) + "' AND USERID='" + sp.getString(ConstantData.USERID, "") + "' AND ORDERID='0'";
                Cursor cursorCart = db.rawQuery(checkCartQuery, null);
                if (cursorCart.getCount() > 0) {
                    list.setAddedCart(true);
                } else {
                    list.setAddedCart(false);
                }

                list.setAddedWishlist(true);

                arrayList.add(list);
            }
            WishlistAdapter adapter = new WishlistAdapter(getActivity(), arrayList);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }
}