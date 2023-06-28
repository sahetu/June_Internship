package june.internship;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText email,password,confirmPassword;
    Button continueButton,submitButton;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        db = openOrCreateDatabase("JuneInternship",MODE_PRIVATE,null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT INT(10),PASSWORD VARCHAR(20),DOB VARCHAR(10),GENDER VARCHAR(10),CITY VARCHAR(50))";
        db.execSQL(tableQuery);

        email = findViewById(R.id.forgot_password_email);
        password = findViewById(R.id.forgot_password);
        confirmPassword = findViewById(R.id.forgot_confirm_password);

        continueButton = findViewById(R.id.forgot_password_continue);
        submitButton = findViewById(R.id.forgot_password_submit);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().trim().equals("")){
                    email.setError("Mail Id Required");
                }
                else if(!email.getText().toString().matches(emailPattern)){
                    email.setError("Valid Mail Id Required");
                }
                else{
                    String selectQuery = "SELECT * FROM USERS WHERE EMAIL='"+email.getText().toString()+"'";
                    Cursor cursor = db.rawQuery(selectQuery,null);
                    if(cursor.getCount()>0){
                        email.setEnabled(false);
                        password.setVisibility(View.VISIBLE);
                        confirmPassword.setVisibility(View.VISIBLE);

                        continueButton.setVisibility(View.GONE);
                        submitButton.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(ForgotPasswordActivity.this, "Invalid Email Id", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password.getText().toString().trim().equals("")){
                    password.setError("Password Required");
                }
                else if(password.getText().toString().trim().length()<6){
                    password.setError("Min. 6 Char Password Required");
                }
                else if(confirmPassword.getText().toString().trim().equals("")){
                    confirmPassword.setError("Confirm Password Required");
                }
                else if(confirmPassword.getText().toString().trim().length()<6){
                    confirmPassword.setError("Min. 6 Char Confirm Password Required");
                }
                else if(!confirmPassword.getText().toString().matches(password.getText().toString())){
                    confirmPassword.setError("Confirm Password Does Not Match");
                }
                else{
                    String updateQuery = "UPDATE USERS SET PASSWORD='"+password.getText().toString()+"' WHERE EMAIL='"+email.getText().toString()+"'";
                    db.execSQL(updateQuery);
                    Toast.makeText(ForgotPasswordActivity.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }
        });

    }
}