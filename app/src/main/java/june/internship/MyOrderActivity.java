package june.internship;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class MyOrderActivity extends AppCompatActivity {

    RelativeLayout dataLayout,defaultLayout;

    RecyclerView recyclerView;
    ArrayList<OrderList> arrayList;

    SQLiteDatabase db;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        sp = getSharedPreferences(ConstantData.PREF, MODE_PRIVATE);

        db = openOrCreateDatabase("JuneInternship", MODE_PRIVATE, null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT INT(10),PASSWORD VARCHAR(20),DOB VARCHAR(10),GENDER VARCHAR(10),CITY VARCHAR(50))";
        db.execSQL(tableQuery);

        String wishlistTableQuery = "CREATE TABLE IF NOT EXISTS WISHLIST(WISHLISTID INTEGER PRIMARY KEY AUTOINCREMENT,USERID INT(10),PRODUCTID INT(10),PRICE VARCHAR(10),UNIT VARCHAR(10),DESCRIPTION TEXT,IMAGE VARCHAR(100))";
        db.execSQL(wishlistTableQuery);

        String cartTableQuery = "CREATE TABLE IF NOT EXISTS CART(CARTID INTEGER PRIMARY KEY AUTOINCREMENT,ORDERID INT(10),USERID INT(10),PRODUCTID INT(10),PRICE VARCHAR(10),UNIT VARCHAR(10),DESCRIPTION TEXT,IMAGE VARCHAR(100),QTY INT(10),TOTALPRICE VARCHAR(50))";
        db.execSQL(cartTableQuery);

        String orderTableQuery = "CREATE TABLE IF NOT EXISTS SHIPPING_ORDER(ORDERID INTEGER PRIMARY KEY AUTOINCREMENT,USERID INT(10),NAME VARCHAR(10),CONTACT VARCHAR(10),ADDRESS TEXT,CITY VARCHAR(100),TOTAL_AMOUNT INT(10),PAYMENT_METHOD VARCHAR(50),TRANSACTION_ID VARCHAR(50))";
        db.execSQL(orderTableQuery);

        defaultLayout = findViewById(R.id.my_order_default_layout);
        dataLayout = findViewById(R.id.my_order_data_layout);

        recyclerView = findViewById(R.id.my_order_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyOrderActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        String selectQuery = "SELECT * FROM SHIPPING_ORDER WHERE USERID='"+sp.getString(ConstantData.USERID,"")+"' ORDER BY ORDERID DESC";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.getCount()>0){
            defaultLayout.setVisibility(View.GONE);
            dataLayout.setVisibility(View.VISIBLE);

            arrayList = new ArrayList<>();
            while (cursor.moveToNext()){
                OrderList list = new OrderList();
                list.setOrderId(cursor.getString(0));
                list.setName(cursor.getString(2));
                list.setContact(cursor.getString(3));
                list.setAddress(cursor.getString(4));
                list.setCity(cursor.getString(5));
                list.setTotalAmount(cursor.getString(6));
                list.setPaymentMethod(cursor.getString(7));
                list.setTransactionId(cursor.getString(8));
                arrayList.add(list);
            }
            OrderAdapter adapter = new OrderAdapter(MyOrderActivity.this,arrayList);
            recyclerView.setAdapter(adapter);
        }
        else{
            defaultLayout.setVisibility(View.VISIBLE);
            dataLayout.setVisibility(View.GONE);
        }

    }
}