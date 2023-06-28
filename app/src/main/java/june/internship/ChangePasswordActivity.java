package june.internship;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText oldPassword,newPassword,confirmPassword;
    Button submit;

    SQLiteDatabase db;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        sp = getSharedPreferences(ConstantData.PREF,MODE_PRIVATE);

        db = openOrCreateDatabase("JuneInternship",MODE_PRIVATE,null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT INT(10),PASSWORD VARCHAR(20),DOB VARCHAR(10),GENDER VARCHAR(10),CITY VARCHAR(50))";
        db.execSQL(tableQuery);

        oldPassword = findViewById(R.id.change_old_password);
        newPassword = findViewById(R.id.change_new_password);
        confirmPassword = findViewById(R.id.change_confirm_password);

        submit = findViewById(R.id.change_password_button);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(oldPassword.getText().toString().trim().equals("")){
                    oldPassword.setError("Old Password Required");
                }
                else if(oldPassword.getText().toString().trim().length()<6){
                    oldPassword.setError("Min. 6 Char Password Required");
                }
                else if(newPassword.getText().toString().trim().equals("")){
                    newPassword.setError("New Password Required");
                }
                else if(newPassword.getText().toString().trim().length()<6){
                    newPassword.setError("Min. 6 Char Password Required");
                }
                else if(confirmPassword.getText().toString().trim().equals("")){
                    confirmPassword.setError("Confirm Password Required");
                }
                else if(confirmPassword.getText().toString().trim().length()<6){
                    confirmPassword.setError("Min. 6 Char Password Required");
                }
                else if(!confirmPassword.getText().toString().trim().matches(newPassword.getText().toString())){
                    confirmPassword.setError("Confirm Password Does Not Match");
                }
                else{
                    String selectQuery = "SELECT * FROM USERS WHERE USERID='"+sp.getString(ConstantData.USERID,"")+"' AND PASSWORD='"+oldPassword.getText().toString()+"'";
                    Cursor cursor = db.rawQuery(selectQuery,null);
                    if(cursor.getCount()>0){
                        String updateQuery = "UPDATE USERS SET PASSWORD='"+newPassword.getText().toString()+"' WHERE USERID='"+sp.getString(ConstantData.USERID,"")+"'";
                        db.execSQL(updateQuery);
                        Toast.makeText(ChangePasswordActivity.this, "Password Change Successfully", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                    else{
                        Toast.makeText(ChangePasswordActivity.this, "Old Password Does Not Match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}