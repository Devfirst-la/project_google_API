package com.example.project_google_api;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;


public class Detail extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private static final String TAG = "Detail";

    private ImageView imageView;
    private TextView app_titile,app_subtitile,date,time,title;
    private boolean isHidetoobar=false;
    private FrameLayout date_behavior;
    private LinearLayout titleAppbar;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private String mUrl,mImage,mDate,mSource,mAuthor,mtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

//        toolbar.findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout toolbarLayout = findViewById(R.id.collapsing_toolbar);
        toolbarLayout.setTitle("");

        appBarLayout = findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(this);
        date_behavior = findViewById(R.id.date_behavior);
        titleAppbar=findViewById(R.id.title_appbar);
        imageView = findViewById(R.id.backdrop);
        app_titile = findViewById(R.id.title_on_appbar);
        app_subtitile = findViewById(R.id.subtitle_on_appbar);
        date = findViewById(R.id.n_date);
        time = findViewById(R.id.n_time);
        title = findViewById(R.id.title);


        loadData();
    }

    private  void  loadData(){
        Intent intent = getIntent();
        mUrl = intent.getStringExtra("url");
        mImage = intent.getStringExtra("img");
        mtitle= intent.getStringExtra("title");
        mDate = intent.getStringExtra("date");
        mSource = intent.getStringExtra("source");
        mAuthor = intent.getStringExtra("author");

        RequestOptions options = new RequestOptions();
        options.error(Utils.getRandomDrawbleColor());

        Glide.with(this)
                .asBitmap()
                .apply(options)
                .load(mImage)
                .into(imageView);

        app_titile.setText(mSource);
        app_subtitile.setText(mUrl);
        date.setText(Utils.DateFormat(mDate));
        title.setText(mtitle);

        String author =null;
        if (mAuthor != null || mAuthor != "") {
            mAuthor = " \u2022 " + mAuthor;
        }
        else{
            author ="";

        }
        time.setText(mSource + author +" \u2022 "+Utils.DateToTimeFormat(mDate));
        initview(mUrl);


    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }


    private  void  initview(String url){
        WebView webView = findViewById(R.id.webView);
        webView .getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);;
        webView.loadUrl(url);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        int maxscroll = appBarLayout.getTotalScrollRange();
        float percentage=(float) Math.abs(i)/(float) maxscroll;

        if (percentage == 1f && isHidetoobar){
            date_behavior.setVisibility(View.GONE);
            titleAppbar.setVisibility(View.GONE);
            isHidetoobar = !isHidetoobar;
        }
        else if (percentage < 1f && !isHidetoobar){
            date_behavior.setVisibility(View.VISIBLE);
            titleAppbar.setVisibility(View.GONE);
            isHidetoobar = !isHidetoobar;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int  id = item.getItemId();

        if(id == R.id.view_web){
            Intent intent= new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(mUrl));
            startActivity(intent);
            return true;

        }
        else if(id == R.id.share){
            try {
                Intent intent= new Intent(Intent.ACTION_SEND);
                intent.setType("text/plan");
                intent.putExtra(Intent.EXTRA_SUBJECT,mSource);
                String str= mtitle + "\n" +mUrl +"\n"+ "share from the News"+"\n";
                intent.putExtra(Intent.EXTRA_TEXT,str);
                startActivity(intent.createChooser(intent,"share of :"));

            }catch (Exception e){
                Toast.makeText(this,"Sorry ",Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
