package june.internship;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView trandingProductsRecycler,productsRecycler;

    String[] trendingProdName = {"Bread","Butter","Cloth","Makup Kit"};
    String[] trendProdPrice = {"50","100","1500","1000"};
    String[] trendProdUnit = {"Packet","200 GM","Pair","Box"};
    int[] trendProdImage = {R.drawable.bread,R.drawable.butter,R.drawable.cloth,R.drawable.makup_kit};

    ArrayList<ProductList> trendProdArray;


    String[] prodName = {"Butter","Cloth","Bread","Makup Kit","Cloth","Bread","Makup Kit"};
    String[] prodPrice = {"100","1500","50","1000","1500","50","1000"};
    String[] prodUnit = {"200 GM","Pair","Packet","Box","Pair","Packet","Box"};
    int[] prodImage = {R.drawable.butter,R.drawable.cloth,R.drawable.bread,R.drawable.makup_kit,R.drawable.cloth,R.drawable.bread,R.drawable.makup_kit};

    ArrayList<ProductList> prodArray;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        trandingProductsRecycler = view.findViewById(R.id.home_tranding_products);
        trandingProductsRecycler.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));
        trandingProductsRecycler.setItemAnimator(new DefaultItemAnimator());
        trandingProductsRecycler.setNestedScrollingEnabled(false);

        trendProdArray = new ArrayList<>();
        for(int i=0;i<trendingProdName.length;i++){
            ProductList list = new ProductList();
            list.setName(trendingProdName[i]);
            list.setPrice(trendProdPrice[i]);
            list.setUnit(trendProdUnit[i]);
            list.setImage(trendProdImage[i]);
            trendProdArray.add(list);
        }
        TrendProductAdapter adapter = new TrendProductAdapter(getActivity(),trendProdArray);
        trandingProductsRecycler.setAdapter(adapter);


        productsRecycler = view.findViewById(R.id.home_products);
        productsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        productsRecycler.setItemAnimator(new DefaultItemAnimator());
        productsRecycler.setNestedScrollingEnabled(false);

        prodArray = new ArrayList<>();
        for(int i=0;i<prodName.length;i++){
            ProductList list = new ProductList();
            list.setName(prodName[i]);
            list.setPrice(prodPrice[i]);
            list.setUnit(prodUnit[i]);
            list.setImage(prodImage[i]);
            prodArray.add(list);
        }
        ProductAdapter prodAdapter = new ProductAdapter(getActivity(),prodArray);
        productsRecycler.setAdapter(prodAdapter);

        return view;
    }
}