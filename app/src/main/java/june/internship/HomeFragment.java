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

public class HomeFragment extends Fragment {

    RecyclerView trandingProductsRecycler,productsRecycler;

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

        productsRecycler = view.findViewById(R.id.home_products);
        productsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        productsRecycler.setItemAnimator(new DefaultItemAnimator());

        return view;
    }
}