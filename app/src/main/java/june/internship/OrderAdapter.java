package june.internship;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyHolder> {

    Context context;
    ArrayList<OrderList> arrayList;
    SharedPreferences sp;

    public OrderAdapter(Context context, ArrayList<OrderList> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        sp = context.getSharedPreferences(ConstantData.PREF,Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_order,parent,false);
        return new MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView orderId,name,contact,address,amount;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.custom_order_id);
            name = itemView.findViewById(R.id.custom_order_name);
            contact = itemView.findViewById(R.id.custom_order_contact);
            address = itemView.findViewById(R.id.custom_order_address);
            amount = itemView.findViewById(R.id.custom_order_amount);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.orderId.setText("Order Id : "+arrayList.get(position).getOrderId());
        holder.name.setText(arrayList.get(position).getName());
        holder.contact.setText(arrayList.get(position).getContact());
        holder.address.setText(arrayList.get(position).getAddress()+","+arrayList.get(position).getCity());
        if(arrayList.get(position).getPaymentMethod().equalsIgnoreCase("Online")) {
            holder.amount.setText(ConstantData.PRICE_SYMBOL + arrayList.get(position).getTotalAmount()+" ("+arrayList.get(position).getTransactionId()+")");
        }
        else{
            holder.amount.setText(ConstantData.PRICE_SYMBOL + arrayList.get(position).getTotalAmount());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString(ConstantData.ORDER_ID,arrayList.get(position).getOrderId()).commit();
                Intent intent = new Intent(context, OrderDetailActivity.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
