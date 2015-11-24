package example.com.pruebaandroid;

import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import example.com.pruebaandroid.DataObjects.Entry;
import example.com.pruebaandroid.utils.ImageTransroundedcorner;

public class Detailsapps extends ActionBarActivity {

    Aplicationapp app;
    Entry entry;
    TextView summary,name,artis,lblpriceamountdetail,lblcurencidetail,lblrelasedetail,lblcategory;
    ImageView ivdetails;
    private Toolbar mToolbar;
    public AlertDialog	dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailsapps);
        app =(Aplicationapp)getApplicationContext();
        entry = app.getEntryselect();
        if(app.isTablet(getApplicationContext()))
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        initToolbar();
        summary=(TextView)findViewById(R.id.lblsummary);
        name =(TextView)findViewById(R.id.lblnamedetails);
        artis  =(TextView)findViewById(R.id.lblartisdetails);
        lblpriceamountdetail = (TextView)findViewById(R.id.lblpriceamountdetail);
        lblcurencidetail = (TextView)findViewById(R.id.lblcurencidetail);
        lblrelasedetail = (TextView)findViewById(R.id.lblrelasedetail);
        lblcategory = (TextView)findViewById(R.id.lblcategory);
        summary.setText(getString(R.string.lblsumary)+entry.getSummary());
        name.setText(entry.getName());
        artis.setText(entry.getArtist());
        lblpriceamountdetail.setText("$ "+entry.getPriceamount());
        lblcurencidetail.setText(entry.getPricecurrency());
        lblrelasedetail.setText(getString(R.string.lblrelasedate)+entry.getReleaseDate());
        lblcategory.setText(getString(R.string.lblcategory)+entry.getCategory());
        ivdetails = (ImageView)findViewById(R.id.ivdetail);
        Picasso.with(getApplicationContext())
                .load(entry.getUrlimagen())
                .resize(150, 150)
                .transform(new ImageTransroundedcorner())
                .into(ivdetails);

        Animation anim;
        anim = AnimationUtils.loadAnimation(MainActivity.CONTEXTO, R.anim.zoom);
        anim.reset();
        ivdetails.startAnimation(anim);
    }
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle(entry.getName());
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_adetailsapps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id ==16908332)
        {

            onBackPressed();
            overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Permite abirir el poup de detalles
     */
    public void ConfigurarPopupdetallespago() {

    }
}
