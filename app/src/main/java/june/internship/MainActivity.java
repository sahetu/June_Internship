package june.internship;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                    System.out.println("Login Successfully");
                    Log.d("SILVEROAK","Login Successfully");
                    Log.e("SILVEROAK","Login Successfully");
                    Toast.makeText(MainActivity.this,"Login Successfully",Toast.LENGTH_LONG).show();
                    Snackbar.make(view,"Login Successfully",Snackbar.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(intent);

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



}