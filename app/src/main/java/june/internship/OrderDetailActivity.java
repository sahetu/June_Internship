package june.internship;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class OrderDetailActivity extends AppCompatActivity {

    TextView orderId,name,contact,address,amount;
    
    SQLiteDatabase db;
    SharedPreferences sp;

    RecyclerView recyclerView;
    ArrayList<ProductList> arrayList;

    String[] prodId = {"1", "2", "3", "4", "5", "6", "7"};
    String[] prodName = {"Butter", "Cloth", "Bread", "Makup Kit", "Cloth", "Bread", "Makup Kit"};
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

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
        
        orderId = findViewById(R.id.order_detail_id);
        name = findViewById(R.id.order_detail_name);
        contact = findViewById(R.id.order_detail_contact);
        address = findViewById(R.id.order_detail_address);
        amount = findViewById(R.id.order_detail_amount);
        
        String orderQuery = "SELECT * FROM SHIPPING_ORDER WHERE ORDERID='"+sp.getString(ConstantData.ORDER_ID,"")+"'";
        Cursor cursor = db.rawQuery(orderQuery,null);
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                orderId.setText("Order Id : "+cursor.getString(0));
                name.setText(cursor.getString(2));
                contact.setText(cursor.getString(3));
                address.setText(cursor.getString(4)+","+cursor.getString(5));
                if(cursor.getString(7).equalsIgnoreCase("Online")) {
                    amount.setText(ConstantData.PRICE_SYMBOL + cursor.getString(6)+" ("+cursor.getString(8)+")");
                }
                else{
                    amount.setText(ConstantData.PRICE_SYMBOL + cursor.getString(6));
                }
            }
        }

        recyclerView = findViewById(R.id.order_detail_product);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        String cartQuery = "SELECT * FROM CART WHERE ORDERID='"+sp.getString(ConstantData.ORDER_ID,"")+"'";
        Cursor cartCursor = db.rawQuery(cartQuery,null);
        if(cartCursor.getCount()>0){
            arrayList = new ArrayList<>();
            while (cartCursor.moveToNext()){
                ProductList list = new ProductList();
                list.setProductId(cartCursor.getString(3));
                for (int i = 0; i < prodId.length; i++) {
                    if (cartCursor.getString(3).equals(prodId[i])) {
                        list.setName(prodName[i]);
                        break;
                    }
                }
                list.setPrice(cartCursor.getString(4));
                list.setUnit(cartCursor.getString(5));
                list.setDescription(cartCursor.getString(6));
                list.setImage(Integer.parseInt(cartCursor.getString(7)));
                list.setQty(cartCursor.getString(8));
                list.setTotalPrice(cartCursor.getString(9));
                arrayList.add(list);
            }
            OrderDetailAdapter adapter = new OrderDetailAdapter(OrderDetailActivity.this,arrayList);
            recyclerView.setAdapter(adapter);
        }

    }
}