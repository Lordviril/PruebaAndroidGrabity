package example.com.pruebaandroid.DataObjects;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by adserranov on 23/09/2015.
 */
public class Entry {

    private String summary;
    private String urlimagen;
    private String name;
    private String artist;
    private String category;
    private String releaseDate;
    private String priceamount;
    private String pricecurrency;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUrlimagen() {
        return urlimagen;
    }

    public void setUrlimagen(String urlimagen) {
        this.urlimagen = urlimagen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    /***
     * Permite obtener la informacion almacenada en el archivo json
     * @param context
     * @return List Entry
     */
    public ArrayList<Entry> GetData(Context context) {
        ArrayList<Entry> Lista=new ArrayList<Entry>();
        InputStreamReader archivo;
        try {
            archivo = new InputStreamReader(context.openFileInput("data.json"));
            BufferedReader br = new BufferedReader(archivo);
            String linea = br.readLine();
            ArrayList<Entry> ListaEntry =new Gson().fromJson(linea,new TypeToken<ArrayList<Entry>>() {}.getType());
            for (Entry entry : ListaEntry)
            {
                    Lista.add(entry);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Lista;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPriceamount() {
        return priceamount;
    }

    public void setPriceamount(String priceamount) {
        this.priceamount = priceamount;
    }

    public String getPricecurrency() {
        return pricecurrency;
    }

    public void setPricecurrency(String pricecurrency) {
        this.pricecurrency = pricecurrency;
    }
}
