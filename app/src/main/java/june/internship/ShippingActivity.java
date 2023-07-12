package june.internship;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ShippingActivity extends AppCompatActivity implements PaymentResultListener {

    EditText name, contact, address;
    Button payNow;

    //RadioButton male,female;
    RadioGroup paymentMethod;

    Spinner city;

    String[] cityArray = {"Select City", "Ahmedabad", "Vadodara", "Surat", "Rajkot", "Gandhinagar", "Mehsana"};

    String sCity, sPaymentMethod, sTransactionId = "";
    SQLiteDatabase db;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping);

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

        name = findViewById(R.id.shipping_name);
        contact = findViewById(R.id.shipping_contact);
        address = findViewById(R.id.shipping_address);

        paymentMethod = findViewById(R.id.shipping_payment_method);

        paymentMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(i);
                sPaymentMethod = radioButton.getText().toString();
            }
        });

        city = findViewById(R.id.shipping_city);
        //city.setPrompt("Select City");
        ArrayAdapter adapter = new ArrayAdapter(ShippingActivity.this, android.R.layout.simple_list_item_1, cityArray);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        city.setAdapter(adapter);

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    sCity = "";
                } else {
                    sCity = cityArray[i];
                    Toast.makeText(ShippingActivity.this, sCity, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        name.setText(sp.getString(ConstantData.NAME, ""));
        contact.setText(sp.getString(ConstantData.CONTACT, ""));
        sCity = sp.getString(ConstantData.CITY, "");
        int iCityPosition = 0;
        for (int i = 0; i < cityArray.length; i++) {
            if (sCity.equalsIgnoreCase(cityArray[i])) {
                iCityPosition = i;
                break;
            }
        }
        city.setSelection(iCityPosition);

        payNow = findViewById(R.id.shipping_button);
        payNow.setText("Pay " + ConstantData.PRICE_SYMBOL + sp.getString(ConstantData.CART_TOTAL, ""));
        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().trim().equals("")) {
                    name.setError("Name Required");
                } else if (contact.getText().toString().trim().equals("")) {
                    contact.setError("Contact No. Required");
                } else if (contact.getText().toString().trim().length() < 10) {
                    contact.setError("Valid Contact No. Required");
                } else if (address.getText().toString().trim().equals("")) {
                    address.setError("Address Required");
                } else if (sCity.equals("")) {
                    Toast.makeText(ShippingActivity.this, "Please Select City", Toast.LENGTH_SHORT).show();
                } else if (paymentMethod.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(ShippingActivity.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
                } else {
                    if (sPaymentMethod.equals("Cash On Delivery")) {
                        sTransactionId = "";
                        addOrder(sTransactionId);
                    } else if (sPaymentMethod.equals("Online")) {
                        startPayment();
                    } else {

                    }
                }
            }
        });

    }

    private void startPayment() {
        Activity activity = this;
        Checkout co = new Checkout();

        try {
            JSONObject object = new JSONObject();
            object.put("name", getResources().getString(R.string.app_name));
            object.put("description", "Online Purchase Product");
            object.put("send_sms_hash", true);
            object.put("allow_rotation", true);
            object.put("image", R.mipmap.ic_launcher);
            object.put("currency", "INR");
            object.put("amount", Integer.parseInt(sp.getString(ConstantData.CART_TOTAL, "")) * 100);

            JSONObject prefill = new JSONObject();
            prefill.put("email", sp.getString(ConstantData.EMAIL, ""));
            prefill.put("contact", sp.getString(ConstantData.CONTACT, ""));

            object.put("prefill", prefill);

            co.open(activity, object);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }

    private void addOrder(String sTransactionId) {
        String shipping_insert = "INSERT INTO SHIPPING_ORDER VALUES(NULL,'" + sp.getString(ConstantData.USERID, "") + "','" + name.getText().toString() + "','" + contact.getText().toString() + "','" + address.getText().toString() + "','" + sCity + "','" + sp.getString(ConstantData.CART_TOTAL, "") + "','" + sPaymentMethod + "','" + sTransactionId + "')";
        db.execSQL(shipping_insert);

        String selectQuery = "SELECT MAX(ORDERID) FROM SHIPPING_ORDER";
        Cursor cursor = db.rawQuery(selectQuery, null);
        String sOrderId = "";
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                sOrderId = cursor.getString(0);
            }
        }

        String cartUpdate = "UPDATE CART SET ORDERID='" + sOrderId + "' WHERE ORDERID='0' AND USERID='" + sp.getString(ConstantData.USERID, "") + "'";
        db.execSQL(cartUpdate);

        Toast.makeText(ShippingActivity.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ShippingActivity.this, DashboardActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPaymentSuccess(String s) {
        sTransactionId = s;
        addOrder(sTransactionId);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}