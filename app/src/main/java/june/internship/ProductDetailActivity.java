package june.internship;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductDetailActivity extends AppCompatActivity {

    ImageView imageView;
    TextView name, price, description;

    SharedPreferences sp;

    ImageView addCart, removeCart, wishlistBlank, wishlistFill;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        db = openOrCreateDatabase("JuneInternship", MODE_PRIVATE, null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT INT(10),PASSWORD VARCHAR(20),DOB VARCHAR(10),GENDER VARCHAR(10),CITY VARCHAR(50))";
        db.execSQL(tableQuery);

        String wishlistTableQuery = "CREATE TABLE IF NOT EXISTS WISHLIST(WISHLISTID INTEGER PRIMARY KEY AUTOINCREMENT,USERID INT(10),PRODUCTID INT(10),PRICE VARCHAR(10),UNIT VARCHAR(10),DESCRIPTION TEXT,IMAGE VARCHAR(100))";
        db.execSQL(wishlistTableQuery);

        String cartTableQuery = "CREATE TABLE IF NOT EXISTS CART(CARTID INTEGER PRIMARY KEY AUTOINCREMENT,ORDERID INT(10),USERID INT(10),PRODUCTID INT(10),PRICE VARCHAR(10),UNIT VARCHAR(10),DESCRIPTION TEXT,IMAGE VARCHAR(100),QTY INT(10),TOTALPRICE VARCHAR(50))";
        db.execSQL(cartTableQuery);

        sp = getSharedPreferences(ConstantData.PREF, MODE_PRIVATE);

        imageView = findViewById(R.id.product_detail_image);
        name = findViewById(R.id.product_detail_name);
        price = findViewById(R.id.product_detail_price);
        description = findViewById(R.id.product_detail_description);

        name.setText(sp.getString(ConstantData.PRODUCT_NAME, ""));
        price.setText(ConstantData.PRICE_SYMBOL + sp.getString(ConstantData.PRODUCT_PRICE, "") + "/" + sp.getString(ConstantData.PRODUCT_UNIT, ""));
        description.setText(sp.getString(ConstantData.PRODUCT_DESCRIPTION, ""));
        imageView.setImageResource(Integer.parseInt(sp.getString(ConstantData.PRODUCT_IMAGE, "")));

        addCart = findViewById(R.id.product_detail_add_cart);
        removeCart = findViewById(R.id.product_detail_remove_cart);
        wishlistBlank = findViewById(R.id.product_detail_wishlist_blank);
        wishlistFill = findViewById(R.id.product_detail_wishlist_fill);

        String checkCartQuery = "SELECT * FROM CART WHERE PRODUCTID='"+sp.getString(ConstantData.PRODUCT_ID,"")+"' AND USERID='"+sp.getString(ConstantData.USERID,"")+"' AND ORDERID='0'";
        Cursor cursorCart = db.rawQuery(checkCartQuery,null);
        if(cursorCart.getCount()>0){
            addCart.setVisibility(View.GONE);
            removeCart.setVisibility(View.VISIBLE);
        }
        else{
            addCart.setVisibility(View.VISIBLE);
            removeCart.setVisibility(View.GONE);
        }

        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int iQty = 2;
                int iPrice = Integer.parseInt(sp.getString(ConstantData.PRODUCT_PRICE,""));
                int iTotalPrice = iQty * iPrice;
                String cartAddQuery = "INSERT INTO CART VALUES(NULL,'0','" + sp.getString(ConstantData.USERID, "") + "','" + sp.getString(ConstantData.PRODUCT_ID, "") + "','" + sp.getString(ConstantData.PRODUCT_PRICE, "") + "','" + sp.getString(ConstantData.PRODUCT_UNIT, "") + "','" + sp.getString(ConstantData.PRODUCT_DESCRIPTION, "") + "','" + sp.getString(ConstantData.PRODUCT_IMAGE, "") + "','"+iQty+"','"+iTotalPrice+"')";
                db.execSQL(cartAddQuery);

                addCart.setVisibility(View.GONE);
                removeCart.setVisibility(View.VISIBLE);
            }
        });

        removeCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cartRemoveQuery = "DELETE FROM CART WHERE PRODUCTID='"+sp.getString(ConstantData.PRODUCT_ID,"")+"' AND USERID='"+sp.getString(ConstantData.USERID,"")+"' AND ORDERID='0'";
                db.execSQL(cartRemoveQuery);
                addCart.setVisibility(View.VISIBLE);
                removeCart.setVisibility(View.GONE);
            }
        });

        String checkWishlistQuery = "SELECT * FROM WISHLIST WHERE PRODUCTID='"+sp.getString(ConstantData.PRODUCT_ID,"")+"' AND USERID='"+sp.getString(ConstantData.USERID,"")+"'";
        Cursor cursor = db.rawQuery(checkWishlistQuery,null);
        if(cursor.getCount()>0){
            wishlistBlank.setVisibility(View.GONE);
            wishlistFill.setVisibility(View.VISIBLE);
        }
        else{
            wishlistBlank.setVisibility(View.VISIBLE);
            wishlistFill.setVisibility(View.GONE);
        }

        wishlistBlank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String wishlistAddQuery = "INSERT INTO WISHLIST VALUES(NULL,'" + sp.getString(ConstantData.USERID, "") + "','" + sp.getString(ConstantData.PRODUCT_ID, "") + "','" + sp.getString(ConstantData.PRODUCT_PRICE, "") + "','" + sp.getString(ConstantData.PRODUCT_UNIT, "") + "','" + sp.getString(ConstantData.PRODUCT_DESCRIPTION, "") + "','" + sp.getString(ConstantData.PRODUCT_IMAGE, "") + "')";
                db.execSQL(wishlistAddQuery);
                wishlistBlank.setVisibility(View.GONE);
                wishlistFill.setVisibility(View.VISIBLE);
            }
        });

        wishlistFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String wishlistRemoveQuery = "DELETE FROM WISHLIST WHERE PRODUCTID='"+sp.getString(ConstantData.PRODUCT_ID,"")+"' AND USERID='"+sp.getString(ConstantData.USERID,"")+"'";
                db.execSQL(wishlistRemoveQuery);
                wishlistBlank.setVisibility(View.VISIBLE);
                wishlistFill.setVisibility(View.GONE);
            }
        });

    }
}