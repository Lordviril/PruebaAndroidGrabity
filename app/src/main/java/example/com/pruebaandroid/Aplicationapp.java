package example.com.pruebaandroid;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import example.com.pruebaandroid.DataObjects.Entry;


/**
 * Created by adserranov on 21/04/2015.
    */
    public class Aplicationapp extends Application {

    public ListaFragment mainActivityFragment;
    public MainActivity acMenuPrincipal;
    public Entry entryselect;


    public MainActivity getAcMenuPrincipal() {
        return acMenuPrincipal;
    }

    public void setAcMenuPrincipal(MainActivity acMenuPrincipal) {
        this.acMenuPrincipal = acMenuPrincipal;
    }

    public Entry getEntryselect() {
        return entryselect;
    }

    public void setEntryselect(Entry entryselect) {
        this.entryselect = entryselect;
    }
    public void setLastupdate()
    {

        Calendar Lastupdate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String  strdate = sdf.format(Lastupdate.getTime());
        SharedPreferences settings = getApplicationContext().getSharedPreferences("DataConfigapp", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("Lastupdate",strdate);
        editor.commit();

    }
    public Calendar getLastupdate()
    {

        SharedPreferences settings = getApplicationContext().getSharedPreferences("DataConfigapp", MODE_PRIVATE);
        String data = settings.getString("Lastupdate", "").replace("T"," ");
        Calendar cal = Calendar.getInstance();
        Calendar Lastupdate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Lastupdate.setTime(sdf.parse(data));// all done

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return  Lastupdate;
    }
    /**
     * permite identificar si el dispositovo es tabet o smarphone
     * @param context
     * @return
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

}
