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

public class CategoryFragment extends Fragment {

    RecyclerView recyclerView;
    String[] catNameArray = {"Bakery","Beauty","Fashion","Health","Travel"};
    int[] catImageArray = {R.drawable.bakery,R.drawable.beauty,R.drawable.fashion,R.drawable.health,R.drawable.travel};

    ArrayList<CategoryList> arrayList;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        recyclerView = view.findViewById(R.id.category_recyclerview);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        arrayList = new ArrayList<>();
        for(int i=0;i<catNameArray.length;i++){
            CategoryList list = new CategoryList();
            list.setName(catNameArray[i]);
            list.setImage(catImageArray[i]);
            arrayList.add(list);
        }
        CategoryAdapter adapter = new CategoryAdapter(getActivity(),arrayList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}