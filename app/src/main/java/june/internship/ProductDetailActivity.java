package june.internship;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductDetailActivity extends AppCompatActivity {

    ImageView imageView;
    TextView name, price, description;

    SharedPreferences sp;

    ImageView addCart, removeCart, wishlistBlank, wishlistFill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

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

        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCart.setVisibility(View.GONE);
                removeCart.setVisibility(View.VISIBLE);
            }
        });

        removeCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCart.setVisibility(View.VISIBLE);
                removeCart.setVisibility(View.GONE);
            }
        });

        wishlistBlank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wishlistBlank.setVisibility(View.GONE);
                wishlistFill.setVisibility(View.VISIBLE);
            }
        });

        wishlistFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wishlistBlank.setVisibility(View.VISIBLE);
                wishlistFill.setVisibility(View.GONE);
            }
        });

    }
}