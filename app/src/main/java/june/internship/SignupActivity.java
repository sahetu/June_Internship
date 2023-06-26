package june.internship;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SignupActivity extends AppCompatActivity {

    EditText name,email,contact,password,confirmPassword,dob;
    Button signup;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    Calendar calendar;

    //RadioButton male,female;
    RadioGroup gender;

    Spinner city;

    String[] cityArray = {"Select City","Ahmedabad","Vadodara","Surat","Rajkot","Gandhinagar","Mehsana"};

    String sCity,sGender;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        db = openOrCreateDatabase("JuneInternship",MODE_PRIVATE,null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT INT(10),PASSWORD VARCHAR(20),DOB VARCHAR(10),GENDER VARCHAR(10),CITY VARCHAR(50))";
        db.execSQL(tableQuery);

        name = findViewById(R.id.signup_name);
        email = findViewById(R.id.signup_email);
        contact = findViewById(R.id.signup_contact);
        password = findViewById(R.id.signup_password);
        confirmPassword = findViewById(R.id.signup_confirm_password);

        dob = findViewById(R.id.signup_dob);

        gender = findViewById(R.id.signup_gender);

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(i);
                sGender = radioButton.getText().toString();
                Toast.makeText(SignupActivity.this, sGender, Toast.LENGTH_SHORT).show();
            }
        });

        /*male = findViewById(R.id.signup_male);
        female = findViewById(R.id.signup_female);

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignupActivity.this, male.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignupActivity.this, female.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });*/

        city = findViewById(R.id.signup_city);
        //city.setPrompt("Select City");
        ArrayAdapter adapter = new ArrayAdapter(SignupActivity.this, android.R.layout.simple_list_item_1,cityArray);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        city.setAdapter(adapter);

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    sCity = "";
                }
                else {
                    sCity = cityArray[i];
                    Toast.makeText(SignupActivity.this, sCity, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateClick = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR,i);
                calendar.set(Calendar.MONTH,i1);
                calendar.set(Calendar.DAY_OF_MONTH,i2);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                dob.setText(sdf.format(calendar.getTime()));

            }
        };

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(SignupActivity.this,dateClick,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

                //Select Past Date
                //dialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                //Select Future Date
                dialog.getDatePicker().setMinDate(System.currentTimeMillis());

                dialog.show();
            }
        });

        signup = findViewById(R.id.signup_button);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().trim().equals("")){
                    name.setError("Name Required");
                }
                else if(email.getText().toString().trim().equals("")){
                    email.setError("Email Id Required");
                }
                else if(!email.getText().toString().trim().matches(emailPattern)){
                    email.setError("Valid Email Id Required");
                }
                else if(contact.getText().toString().trim().equals("")){
                    contact.setError("Contact No. Required");
                }
                else if(contact.getText().toString().trim().length()<10){
                    contact.setError("Valid Contact No. Required");
                }
                else if(password.getText().toString().trim().equals("")){
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
                else if(!confirmPassword.getText().toString().trim().matches(password.getText().toString())){
                    confirmPassword.setError("Confirm Password Does Not Match");
                }
                else if(dob.getText().toString().trim().equals("")){
                    Toast.makeText(SignupActivity.this, "Please Select Date Of Birth", Toast.LENGTH_SHORT).show();
                }
                else if(gender.getCheckedRadioButtonId() == -1){
                    Toast.makeText(SignupActivity.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
                }
                else if(sCity.equals("")){
                    Toast.makeText(SignupActivity.this, "Please Select City", Toast.LENGTH_SHORT).show();
                }
                else{
                    String selectQuery = "SELECT * FROM USERS WHERE EMAIL='"+email.getText().toString()+"' OR CONTACT='"+contact.getText().toString()+"'";
                    Cursor cursor = db.rawQuery(selectQuery,null);
                    if(cursor.getCount()>0){
                        Toast.makeText(SignupActivity.this, "Email/Contact Already Registered", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String insertQuery = "INSERT INTO USERS VALUES (NULL,'" + name.getText().toString() + "','" + email.getText().toString() + "','" + contact.getText().toString() + "','" + password.getText().toString() + "','" + dob.getText().toString() + "','" + sGender + "','" + sCity + "')";
                        db.execSQL(insertQuery);
                        Toast.makeText(SignupActivity.this, "Signup Successfully", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }
            }
        });

    }
}