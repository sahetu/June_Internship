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

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyHolder> {

    Context context;
    ArrayList<ProductList> arrayList;

    SharedPreferences sp;

    public OrderDetailAdapter(Context context, ArrayList<ProductList> arrayList) {
        this.context  = context;
        this.arrayList = arrayList;
        sp = context.getSharedPreferences(ConstantData.PREF,Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_order_detail,parent,false);
        return new MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name,price,qty;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.custom_order_detail_image);
            name = itemView.findViewById(R.id.custom_order_detail_name);
            price = itemView.findViewById(R.id.custom_order_detail_price);
            qty = itemView.findViewById(R.id.custom_order_detail_qty);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.name.setText(arrayList.get(position).getName());
        holder.price.setText(ConstantData.PRICE_SYMBOL+arrayList.get(position).getPrice()+"/"+arrayList.get(position).getUnit());
        holder.imageView.setImageResource(arrayList.get(position).getImage());

        holder.qty.setText("Qty : "+arrayList.get(position).getQty());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString(ConstantData.PRODUCT_ID,arrayList.get(position).getProductId()).commit();
                sp.edit().putString(ConstantData.PRODUCT_NAME,arrayList.get(position).getName()).commit();
                sp.edit().putString(ConstantData.PRODUCT_PRICE,arrayList.get(position).getPrice()).commit();
                sp.edit().putString(ConstantData.PRODUCT_UNIT,arrayList.get(position).getUnit()).commit();
                sp.edit().putString(ConstantData.PRODUCT_DESCRIPTION,arrayList.get(position).getDescription()).commit();
                sp.edit().putString(ConstantData.PRODUCT_IMAGE, String.valueOf(arrayList.get(position).getImage())).commit();
                Intent intent = new Intent(context, ProductDetailActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
