package june.internship;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyHolder> {

    Context context;
    ArrayList<ProductList> trendProdArray;
    SharedPreferences sp;
    public ProductAdapter(Context context, ArrayList<ProductList> trendProdArray) {
        this.context = context;
        this.trendProdArray = trendProdArray;
        sp = context.getSharedPreferences(ConstantData.PREF,Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_product,parent,false);
        return new MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name,price;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.custom_product_image);
            name = itemView.findViewById(R.id.custom_product_name);
            price = itemView.findViewById(R.id.custom_product_price);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.name.setText(trendProdArray.get(position).getName());
        holder.price.setText(ConstantData.PRICE_SYMBOL+trendProdArray.get(position).getPrice()+"/"+trendProdArray.get(position).getUnit());
        holder.imageView.setImageResource(trendProdArray.get(position).getImage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
