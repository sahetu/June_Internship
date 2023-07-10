package june.internship;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyHolder> {

    Context context;
    ArrayList<ProductList> trendProdArray;

    SharedPreferences sp;
    SQLiteDatabase db;

    public CartAdapter(Context context, ArrayList<ProductList> trendProdArray) {
        this.context = context;
        this.trendProdArray = trendProdArray;
        sp = context.getSharedPreferences(ConstantData.PREF,Context.MODE_PRIVATE);
        db = context.openOrCreateDatabase("JuneInternship", MODE_PRIVATE, null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT INT(10),PASSWORD VARCHAR(20),DOB VARCHAR(10),GENDER VARCHAR(10),CITY VARCHAR(50))";
        db.execSQL(tableQuery);

        String wishlistTableQuery = "CREATE TABLE IF NOT EXISTS WISHLIST(WISHLISTID INTEGER PRIMARY KEY AUTOINCREMENT,USERID INT(10),PRODUCTID INT(10),PRICE VARCHAR(10),UNIT VARCHAR(10),DESCRIPTION TEXT,IMAGE VARCHAR(100))";
        db.execSQL(wishlistTableQuery);

        String cartTableQuery = "CREATE TABLE IF NOT EXISTS CART(CARTID INTEGER PRIMARY KEY AUTOINCREMENT,ORDERID INT(10),USERID INT(10),PRODUCTID INT(10),PRICE VARCHAR(10),UNIT VARCHAR(10),DESCRIPTION TEXT,IMAGE VARCHAR(100),QTY INT(10),TOTALPRICE VARCHAR(50))";
        db.execSQL(cartTableQuery);
    }

    @NonNull
    @Override
    public CartAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cart,parent,false);
        return new CartAdapter.MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView imageView,removeCart,wishlistBlank,wishlistFill;
        TextView name,price,qty;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.custom_cart_image);
            name = itemView.findViewById(R.id.custom_cart_name);
            price = itemView.findViewById(R.id.custom_cart_price);
            qty = itemView.findViewById(R.id.custom_cart_qty);
            removeCart = itemView.findViewById(R.id.custom_cart_remove_cart);
            wishlistBlank = itemView.findViewById(R.id.custom_cart_add_wishlist);
            wishlistFill = itemView.findViewById(R.id.custom_cart_remove_wishlist);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.MyHolder holder, int position) {
        holder.name.setText(trendProdArray.get(position).getName());
        holder.price.setText(ConstantData.PRICE_SYMBOL+trendProdArray.get(position).getPrice()+"/"+trendProdArray.get(position).getUnit());
        holder.imageView.setImageResource(trendProdArray.get(position).getImage());

        holder.qty.setText("Qty : "+trendProdArray.get(position).getQty());

        if(trendProdArray.get(position).isAddedWishlist()){
            holder.wishlistBlank.setVisibility(View.GONE);
            holder.wishlistFill.setVisibility(View.VISIBLE);
        }
        else{
            holder.wishlistBlank.setVisibility(View.VISIBLE);
            holder.wishlistFill.setVisibility(View.GONE);
        }

        holder.removeCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CartFragment.iTotal = CartFragment.iTotal-Integer.parseInt(trendProdArray.get(position).getTotalPrice());

                if(CartFragment.iTotal==0){
                    CartFragment.checkout.setVisibility(View.GONE);
                }
                else{
                    CartFragment.checkout.setVisibility(View.VISIBLE);
                    CartFragment.checkout.setText("Checkout With "+ConstantData.PRICE_SYMBOL+CartFragment.iTotal);
                }

                String cartRemoveQuery = "DELETE FROM CART WHERE PRODUCTID='"+trendProdArray.get(position).getProductId()+"' AND USERID='"+sp.getString(ConstantData.USERID,"")+"' AND ORDERID='0'";
                db.execSQL(cartRemoveQuery);
                trendProdArray.remove(position);
                notifyDataSetChanged();



            }
        });

        holder.wishlistBlank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String wishlistAddQuery = "INSERT INTO WISHLIST VALUES(NULL,'" + sp.getString(ConstantData.USERID, "") + "','" + trendProdArray.get(position).getProductId() + "','" + trendProdArray.get(position).getPrice() + "','" + trendProdArray.get(position).getUnit() + "','" + trendProdArray.get(position).getDescription() + "','" + trendProdArray.get(position).getImage() + "')";
                db.execSQL(wishlistAddQuery);
                holder.wishlistBlank.setVisibility(View.GONE);
                holder.wishlistFill.setVisibility(View.VISIBLE);
            }
        });

        holder.wishlistFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String wishlistRemoveQuery = "DELETE FROM WISHLIST WHERE PRODUCTID='"+trendProdArray.get(position).getProductId()+"' AND USERID='"+sp.getString(ConstantData.USERID,"")+"'";
                db.execSQL(wishlistRemoveQuery);
                holder.wishlistBlank.setVisibility(View.VISIBLE);
                holder.wishlistFill.setVisibility(View.GONE);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString(ConstantData.PRODUCT_ID,trendProdArray.get(position).getProductId()).commit();
                sp.edit().putString(ConstantData.PRODUCT_NAME,trendProdArray.get(position).getName()).commit();
                sp.edit().putString(ConstantData.PRODUCT_PRICE,trendProdArray.get(position).getPrice()).commit();
                sp.edit().putString(ConstantData.PRODUCT_UNIT,trendProdArray.get(position).getUnit()).commit();
                sp.edit().putString(ConstantData.PRODUCT_DESCRIPTION,trendProdArray.get(position).getDescription()).commit();
                sp.edit().putString(ConstantData.PRODUCT_IMAGE, String.valueOf(trendProdArray.get(position).getImage())).commit();
                Intent intent = new Intent(context, ProductDetailActivity.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return trendProdArray.size();
    }
}

