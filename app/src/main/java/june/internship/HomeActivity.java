package june.internship;

import androidx.appcompat.app.AppCompatActivity;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    EditText name,email,contact,dob;
    Button editProfile,submit,changePassword,logout;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    Calendar calendar;

    RadioButton male,female;
    RadioGroup gender;

    Spinner city;

    String[] cityArray = {"Select City","Ahmedabad","Vadodara","Surat","Rajkot","Gandhinagar","Mehsana"};

    String sCity,sGender;
    SQLiteDatabase db;
    SharedPreferences sp;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sp = getSharedPreferences(ConstantData.PREF,MODE_PRIVATE);

        db = openOrCreateDatabase("JuneInternship",MODE_PRIVATE,null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT INT(10),PASSWORD VARCHAR(20),DOB VARCHAR(10),GENDER VARCHAR(10),CITY VARCHAR(50))";
        db.execSQL(tableQuery);

        name = findViewById(R.id.home_name);
        email = findViewById(R.id.home_email);
        contact = findViewById(R.id.home_contact);

        dob = findViewById(R.id.home_dob);

        gender = findViewById(R.id.home_gender);

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(i);
                sGender = radioButton.getText().toString();
                Toast.makeText(HomeActivity.this, sGender, Toast.LENGTH_SHORT).show();
            }
        });

        male = findViewById(R.id.home_male);
        female = findViewById(R.id.home_female);

        city = findViewById(R.id.home_city);
        //city.setPrompt("Select City");
        ArrayAdapter adapter = new ArrayAdapter(HomeActivity.this, android.R.layout.simple_list_item_1,cityArray);
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
                    Toast.makeText(HomeActivity.this, sCity, Toast.LENGTH_SHORT).show();
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
                DatePickerDialog dialog = new DatePickerDialog(HomeActivity.this,dateClick,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

                //Select Past Date
                //dialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                //Select Future Date
                dialog.getDatePicker().setMinDate(System.currentTimeMillis());

                dialog.show();
            }
        });

        editProfile = findViewById(R.id.home_edit_profile);

        changePassword = findViewById(R.id.home_change_password);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        logout = findViewById(R.id.home_logout);

        submit = findViewById(R.id.home_submit_profile);
        submit.setOnClickListener(new View.OnClickListener() {
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
                else if(dob.getText().toString().trim().equals("")){
                    Toast.makeText(HomeActivity.this, "Please Select Date Of Birth", Toast.LENGTH_SHORT).show();
                }
                else if(gender.getCheckedRadioButtonId() == -1){
                    Toast.makeText(HomeActivity.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
                }
                else if(sCity.equals("")){
                    Toast.makeText(HomeActivity.this, "Please Select City", Toast.LENGTH_SHORT).show();
                }
                else{
                    String selectQuery = "SELECT * FROM USERS WHERE USERID='"+sp.getString(ConstantData.USERID,"")+"'";
                    Cursor cursor = db.rawQuery(selectQuery,null);
                    if(cursor.getCount()>0){
                        String updateQuery = "UPDATE USERS SET NAME='"+name.getText().toString()+"',EMAIL='"+email.getText().toString()+"',CONTACT='"+contact.getText().toString()+"',DOB='"+dob.getText().toString()+"',GENDER='"+sGender+"',CITY='"+sCity+"' WHERE USERID='"+sp.getString(ConstantData.USERID,"")+"'";
                        db.execSQL(updateQuery);
                        Toast.makeText(HomeActivity.this, "Profile Update Successfully", Toast.LENGTH_SHORT).show();

                        sp.edit().putString(ConstantData.NAME,name.getText().toString()).commit();
                        sp.edit().putString(ConstantData.EMAIL,email.getText().toString()).commit();
                        sp.edit().putString(ConstantData.CONTACT,contact.getText().toString()).commit();
                        sp.edit().putString(ConstantData.DOB,dob.getText().toString()).commit();
                        sp.edit().putString(ConstantData.GENDER,sGender).commit();
                        sp.edit().putString(ConstantData.CITY,sCity).commit();

                        setData(false);
                    }
                    else {
                        Toast.makeText(HomeActivity.this, "Invalid User Id", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        setData(false);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData(true);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sp.edit().remove(ConstantData.USERID).commit();
                sp.edit().clear().commit();
                Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setData(boolean b) {
        name.setText(sp.getString(ConstantData.NAME,""));
        email.setText(sp.getString(ConstantData.EMAIL,""));
        contact.setText(sp.getString(ConstantData.CONTACT,""));
        dob.setText(sp.getString(ConstantData.DOB,""));

        sGender = sp.getString(ConstantData.GENDER,"");
        if(sGender.equalsIgnoreCase("Male")){
            male.setChecked(true);
            female.setChecked(false);
        }
        else if(sGender.equalsIgnoreCase("Female")){
            male.setChecked(false);
            female.setChecked(true);
        }
        else{

        }

        sCity = sp.getString(ConstantData.CITY,"");
        int iCityPosition = 0;
        for(int i=0;i<cityArray.length;i++){
            if(sCity.equalsIgnoreCase(cityArray[i])){
                iCityPosition = i;
                break;
            }
        }
        city.setSelection(iCityPosition);

        name.setEnabled(b);
        email.setEnabled(b);
        contact.setEnabled(b);
        dob.setEnabled(b);

        male.setEnabled(b);
        female.setEnabled(b);

        city.setEnabled(b);

        if(b){
            editProfile.setVisibility(View.GONE);
            submit.setVisibility(View.VISIBLE);
        }
        else{
            editProfile.setVisibility(View.VISIBLE);
            submit.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finishAffinity();
    }
}