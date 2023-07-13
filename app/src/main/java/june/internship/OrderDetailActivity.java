package june.internship;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

public class OrderDetailActivity extends AppCompatActivity {

    TextView orderId,name,contact,address,amount;
    
    SQLiteDatabase db;
    SharedPreferences sp;
    
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
        
    }
}