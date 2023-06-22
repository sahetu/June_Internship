package june.internship;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText email,password,confirmPassword;
    Button continueButton,submitButton;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

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
                    email.setEnabled(false);
                    password.setVisibility(View.VISIBLE);
                    confirmPassword.setVisibility(View.VISIBLE);

                    continueButton.setVisibility(View.GONE);
                    submitButton.setVisibility(View.VISIBLE);
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
                    Toast.makeText(ForgotPasswordActivity.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }
        });

    }
}