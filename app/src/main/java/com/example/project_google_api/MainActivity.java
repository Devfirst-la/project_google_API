package com.example.project_google_api;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_google_api.api.Apiclient;
import com.example.project_google_api.api.Apiinterface;
import com.example.project_google_api.model.Articles;
import com.example.project_google_api.model.news;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MainActivity";

    public  static final String API_key="2ce10eb30f0e4be494723ef0131e7b9e";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Articles> articles=new ArrayList<>();///
    SwipeRefreshLayout swipeRefreshLayout;
    private  Adapters adapter;
    private RelativeLayout errorlaout;
    private ImageView errorimage;
    private TextView errortile,errormessage;
    private Button btnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout= findViewById(R.id.SW_refesh_layput);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        recyclerView = findViewById(R.id.main_recyclerview);
        layoutManager= new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator( new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

//        LoadJson("");
        onloading("");

        errorlaout = findViewById(R.id.Error);
        errorimage = findViewById(R.id.errorimg);
        errortile = findViewById(R.id.errotiltle);
        errormessage = findViewById(R.id.errormessage);
        btnButton = findViewById(R.id.btn_retry);
    }

    public  void  LoadJson( final String str){

        errorlaout.setVisibility(View.GONE);

        swipeRefreshLayout.setRefreshing(true);
        Apiinterface apiinterface= Apiclient.getApiClient().create(Apiinterface.class);
        String country=Utils.getCountry();
        String category = Utils.getcategory();

         Call<news> call;
        if(str.length() > 0){
            call = apiinterface.getnewsSearch(str,"popularity",API_key);

        }
        else{
            call = apiinterface.getNews(country,category,API_key);
        }
        call.enqueue(new Callback<news>() {
            @Override
            public void onResponse(Call<news> call, Response<news> response) {
                if (response.isSuccessful() && response.body().getArticles() != null) {

                    if (!articles.isEmpty()) {
                        articles.clear();
                    }

                    articles = response.body().getArticles();
                    adapter = new Adapters(articles, MainActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                }else{
                    swipeRefreshLayout.setRefreshing(false);
                    String errors;
                    switch (response.code()){
                        case 404:
                            errors="404 not found";
                            break;
                        case 500:
                            errors ="500 server broket";
                            break;default:
                            errors="Unknow Error";
                            break;

                    }
                    ShoeError(R.drawable.no_result,"No Result","please Try Agian \n "+ errors);
                }
            }

            @Override
            public void onFailure(Call<news> call, Throwable t) {

                swipeRefreshLayout.setRefreshing(false);
                ShoeError(R.drawable.no_result,"ທ່ານບໍ່ສາມາດເຊື່ອມຕໍ່ໄດ້","ກະລຸນາກວດສອບອີນເຕີເນັດ ຂອງທ່ານກ່ອນ ແລ້ວ ລອງອີກຄັ້ງ... \n "+"");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search,menu);
        SearchManager searchManager =(SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView= (SearchView)menu.findItem(R.id.action_search).getActionView();
        MenuItem menuItem= menu.findItem(R.id.action_search);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search Title...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.length() >2){
//                    LoadJson(query);
                    onloading(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                LoadJson(newText);
                onloading(newText);
                return false;

            }
        });
        menuItem.getIcon().setVisible(false,false);
        return true;
    }

    @Override
    public void onRefresh() {
        LoadJson("");
    }
    private  void onloading(final  String keyword){
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        LoadJson(keyword);
                    }
                }
        );
    }

    private  void ShoeError(int imageView,String title,String messagge){

        if(errorlaout.getVisibility()== View.GONE){
            errorlaout.setVisibility(View.VISIBLE);
        }
        errorimage.setImageResource(imageView);
        errortile.setText(title);
        errormessage.setText(messagge);
        btnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onloading("");
            }
        });
    }
}
