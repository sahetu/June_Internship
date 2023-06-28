package june.internship;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sp;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sp = getSharedPreferences(ConstantData.PREF,MODE_PRIVATE);

        imageView = findViewById(R.id.splash_iv);

        //Picasso.get().load("https://i.pinimg.com/originals/67/d0/13/67d0137c6517d5ed0fcc461f2582dcfd.gif").placeholder(R.mipmap.ic_launcher).into(imageView);
        Glide.with(SplashActivity.this).asGif().load("https://i.pinimg.com/originals/67/d0/13/67d0137c6517d5ed0fcc461f2582dcfd.gif").placeholder(R.mipmap.ic_launcher).into(imageView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sp.getString(ConstantData.USERID,"").equalsIgnoreCase("")) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },2000);

    }
}