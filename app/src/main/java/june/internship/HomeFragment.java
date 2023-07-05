package june.internship;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    RecyclerView trandingProductsRecycler, productsRecycler;

    String[] trendingProdId = {"3","1","2","4"};
    String[] trendingProdName = {"Bread", "Butter", "Cloth", "Makup Kit"};
    String[] trendProdPrice = {"50", "100", "1500", "1000"};
    String[] trendProdUnit = {"Packet", "200 GM", "Pair", "Box"};

    String[] trendProdDesc = {
            "White bread typically refers to breads made from wheat flour from which the bran and the germ layers have been removed from the whole wheatberry as part of the flour grinding or milling process, producing a light-colored flour.",
            "Butter, a yellow-to-white solid emulsion of fat globules, water, and inorganic salts produced by churning the cream from cows milk. Butter has long been used as a spread and as a cooking fat. It is an important edible fat in northern Europe, North America, and other places where cattle are the primary dairy animals.",
            "The dictionary defines cloth as “a pliable material made usually by weaving, felting, or knitting natural or synthetic fibers and filaments.” Essentially, fabric is a material made of fibers that is used to make items such as clothing, shoes, bags, and homewares like bedsheets, cushions, and towels.",
            "What does a makeup kit consist of? Ideally, a complete makeup kit consists of a moisturizer, skin tint (like foundation or tinted primer), concealer, lip product, bronzer, blush, and mascara."
    };

    int[] trendProdImage = {R.drawable.bread, R.drawable.butter, R.drawable.cloth, R.drawable.makup_kit};

    ArrayList<ProductList> trendProdArray;

    String[] prodId = {"1","2","3","4","5","6","7"};
    String[] prodName = {"Butter", "Cloth", "Bread", "Makup Kit", "Cloth", "Bread", "Makup Kit"};
    String[] prodPrice = {"100", "1500", "50", "1000", "1500", "50", "1000"};
    String[] prodUnit = {"200 GM", "Pair", "Packet", "Box", "Pair", "Packet", "Box"};
    String[] prodDescription = {
            "Butter, a yellow-to-white solid emulsion of fat globules, water, and inorganic salts produced by churning the cream from cows milk. Butter has long been used as a spread and as a cooking fat. It is an important edible fat in northern Europe, North America, and other places where cattle are the primary dairy animals.",
            "The dictionary defines cloth as “a pliable material made usually by weaving, felting, or knitting natural or synthetic fibers and filaments.” Essentially, fabric is a material made of fibers that is used to make items such as clothing, shoes, bags, and homewares like bedsheets, cushions, and towels.",
            "White bread typically refers to breads made from wheat flour from which the bran and the germ layers have been removed from the whole wheatberry as part of the flour grinding or milling process, producing a light-colored flour.",
            "What does a makeup kit consist of? Ideally, a complete makeup kit consists of a moisturizer, skin tint (like foundation or tinted primer), concealer, lip product, bronzer, blush, and mascara.",
            "The dictionary defines cloth as “a pliable material made usually by weaving, felting, or knitting natural or synthetic fibers and filaments.” Essentially, fabric is a material made of fibers that is used to make items such as clothing, shoes, bags, and homewares like bedsheets, cushions, and towels.",
            "White bread typically refers to breads made from wheat flour from which the bran and the germ layers have been removed from the whole wheatberry as part of the flour grinding or milling process, producing a light-colored flour.",
            "What does a makeup kit consist of? Ideally, a complete makeup kit consists of a moisturizer, skin tint (like foundation or tinted primer), concealer, lip product, bronzer, blush, and mascara."
    };
    int[] prodImage = {R.drawable.butter, R.drawable.cloth, R.drawable.bread, R.drawable.makup_kit, R.drawable.cloth, R.drawable.bread, R.drawable.makup_kit};

    ArrayList<ProductList> prodArray;

    SharedPreferences sp;
    SQLiteDatabase db;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        db = getActivity().openOrCreateDatabase("JuneInternship", Context.MODE_PRIVATE, null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT INT(10),PASSWORD VARCHAR(20),DOB VARCHAR(10),GENDER VARCHAR(10),CITY VARCHAR(50))";
        db.execSQL(tableQuery);

        String wishlistTableQuery = "CREATE TABLE IF NOT EXISTS WISHLIST(WISHLISTID INTEGER PRIMARY KEY AUTOINCREMENT,USERID INT(10),PRODUCTID INT(10),PRICE VARCHAR(10),UNIT VARCHAR(10),DESCRIPTION TEXT,IMAGE VARCHAR(100))";
        db.execSQL(wishlistTableQuery);

        String cartTableQuery = "CREATE TABLE IF NOT EXISTS CART(CARTID INTEGER PRIMARY KEY AUTOINCREMENT,ORDERID INT(10),USERID INT(10),PRODUCTID INT(10),PRICE VARCHAR(10),UNIT VARCHAR(10),DESCRIPTION TEXT,IMAGE VARCHAR(100),QTY INT(10),TOTALPRICE VARCHAR(50))";
        db.execSQL(cartTableQuery);

        sp = getActivity().getSharedPreferences(ConstantData.PREF, Context.MODE_PRIVATE);

        trandingProductsRecycler = view.findViewById(R.id.home_tranding_products);

        productsRecycler = view.findViewById(R.id.home_products);

        return view;
    }

    private void setData() {
        trandingProductsRecycler.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        trandingProductsRecycler.setItemAnimator(new DefaultItemAnimator());
        trandingProductsRecycler.setNestedScrollingEnabled(false);

        trendProdArray = new ArrayList<>();
        for (int i = 0; i < trendingProdName.length; i++) {
            ProductList list = new ProductList();
            list.setProductId(trendingProdId[i]);
            list.setName(trendingProdName[i]);
            list.setPrice(trendProdPrice[i]);
            list.setUnit(trendProdUnit[i]);
            list.setDescription(trendProdDesc[i]);
            list.setImage(trendProdImage[i]);

            String checkCartQuery = "SELECT * FROM CART WHERE PRODUCTID='"+trendingProdId[i]+"' AND USERID='"+sp.getString(ConstantData.USERID,"")+"' AND ORDERID='0'";
            Cursor cursorCart = db.rawQuery(checkCartQuery,null);
            if(cursorCart.getCount()>0){
                list.setAddedCart(true);
            }
            else{
                list.setAddedCart(false);
            }

            String checkWishlistQuery = "SELECT * FROM WISHLIST WHERE PRODUCTID='"+trendingProdId[i]+"' AND USERID='"+sp.getString(ConstantData.USERID,"")+"'";
            Cursor cursor = db.rawQuery(checkWishlistQuery,null);
            if(cursor.getCount()>0){
                list.setAddedWishlist(true);
            }
            else{
                list.setAddedWishlist(false);
            }

            trendProdArray.add(list);
        }
        TrendProductAdapter adapter = new TrendProductAdapter(getActivity(), trendProdArray);
        trandingProductsRecycler.setAdapter(adapter);

        productsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        productsRecycler.setItemAnimator(new DefaultItemAnimator());
        productsRecycler.setNestedScrollingEnabled(false);
        prodArray = new ArrayList<>();
        for (int i = 0; i < prodName.length; i++) {
            ProductList list = new ProductList();
            list.setProductId(prodId[i]);
            list.setName(prodName[i]);
            list.setPrice(prodPrice[i]);
            list.setUnit(prodUnit[i]);
            list.setDescription(prodDescription[i]);
            list.setImage(prodImage[i]);

            String checkCartQuery = "SELECT * FROM CART WHERE PRODUCTID='"+prodId[i]+"' AND USERID='"+sp.getString(ConstantData.USERID,"")+"' AND ORDERID='0'";
            Cursor cursorCart = db.rawQuery(checkCartQuery,null);
            if(cursorCart.getCount()>0){
                list.setAddedCart(true);
            }
            else{
                list.setAddedCart(false);
            }

            String checkWishlistQuery = "SELECT * FROM WISHLIST WHERE PRODUCTID='"+prodId[i]+"' AND USERID='"+sp.getString(ConstantData.USERID,"")+"'";
            Cursor cursor = db.rawQuery(checkWishlistQuery,null);
            if(cursor.getCount()>0){
                list.setAddedWishlist(true);
            }
            else{
                list.setAddedWishlist(false);
            }

            prodArray.add(list);
        }
        ProductAdapter prodAdapter = new ProductAdapter(getActivity(), prodArray);
        productsRecycler.setAdapter(prodAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }
}