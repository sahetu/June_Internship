package june.internship;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    Button login;
    EditText email,password;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    TextView forgetPassword,createAccount;
    SQLiteDatabase db;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences(ConstantData.PREF,MODE_PRIVATE);

        db = openOrCreateDatabase("JuneInternship",MODE_PRIVATE,null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT INT(10),PASSWORD VARCHAR(20),DOB VARCHAR(10),GENDER VARCHAR(10),CITY VARCHAR(50))";
        db.execSQL(tableQuery);

        email = findViewById(R.id.main_email);
        password = findViewById(R.id.main_password);

        login = findViewById(R.id.main_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().trim().equals("")){
                    email.setError("Mail Id Required");
                }
                else if(!email.getText().toString().trim().matches(emailPattern)){
                    email.setError("Valid Email Id Required");
                }
                else if(password.getText().toString().trim().equals("")){
                    password.setError("Password Required");
                }
                else if(password.getText().toString().trim().length()<6){
                    password.setError("Min. 6 Char Password Required");
                }
                else{

                    String selectQuery = "SELECT * FROM USERS WHERE EMAIL='"+email.getText().toString()+"' AND PASSWORD='"+password.getText().toString()+"'";
                    Cursor cursor = db.rawQuery(selectQuery,null);
                    if(cursor.getCount()>0){

                        while (cursor.moveToNext()){
                            String sUserId = cursor.getString(0);
                            String sName = cursor.getString(1);
                            String sEmail = cursor.getString(2);
                            String sContact = cursor.getString(3);
                            String sPassword = cursor.getString(4);
                            String sDOB = cursor.getString(5);
                            String sGender = cursor.getString(6);
                            String sCity = cursor.getString(7);

                            sp.edit().putString(ConstantData.USERID,sUserId).commit();
                            sp.edit().putString(ConstantData.NAME,sName).commit();
                            sp.edit().putString(ConstantData.EMAIL,sEmail).commit();
                            sp.edit().putString(ConstantData.CONTACT,sContact).commit();
                            sp.edit().putString(ConstantData.PASSWORD,sPassword).commit();
                            sp.edit().putString(ConstantData.DOB,sDOB).commit();
                            sp.edit().putString(ConstantData.GENDER,sGender).commit();
                            sp.edit().putString(ConstantData.CITY,sCity).commit();

                            Log.d("LOGIN_DATA",sUserId+"_"+sName+"_"+sEmail+"_"+sContact+"_"+sPassword+"_"+sDOB+"_"+sGender+"_"+sCity);
                        }

                        System.out.println("Login Successfully");
                        Log.d("SILVEROAK","Login Successfully");
                        Log.e("SILVEROAK","Login Successfully");
                        Toast.makeText(MainActivity.this,"Login Successfully",Toast.LENGTH_LONG).show();
                        Snackbar.make(view,"Login Successfully",Snackbar.LENGTH_SHORT).show();

                        Intent intent = new Intent(MainActivity.this,DashboardActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(MainActivity.this,"Login Unsuccessfully",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        forgetPassword = findViewById(R.id.main_forget_password);
        createAccount = findViewById(R.id.main_create_account);

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finishAffinity();
    }

}