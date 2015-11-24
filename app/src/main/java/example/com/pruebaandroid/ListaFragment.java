package example.com.pruebaandroid;

/**
 * Created by Ravi on 29/07/15.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;

import example.com.pruebaandroid.adapter.AdapterMenu;
import example.com.pruebaandroid.ClientWS.IRecepcionMensaje;
import example.com.pruebaandroid.ClientWS.InvocacionRest;
import example.com.pruebaandroid.ClientWS.NotificadorInvocacion;
import example.com.pruebaandroid.ClientWS.TipoOperacionRest;
import example.com.pruebaandroid.DataObjects.Entry;
import example.com.pruebaandroid.utils.DetectordeInternet;


public class ListaFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, IRecepcionMensaje {

    Aplicationapp app;
    ArrayList<Entry> listentryObjet;
    GridView lvresul;
    private Toolbar mToolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageButton mFabButton;
    int mLastFirstVisibleItem=0;


    public ListaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public static View ROOT;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_lista, container, false);
        ROOT = rootView;
        app = (Aplicationapp)getActivity().getApplicationContext();
        app.acMenuPrincipal = (MainActivity) MainActivity.CONTEXTO;
        initToolbar();
        listentryObjet = new ArrayList<>();
        mFabButton = (ImageButton) rootView.findViewById(R.id.fabButton);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        lvresul = (GridView )rootView.findViewById(R.id.lvResult);

        if(app.isTablet(getActivity().getApplicationContext()))
        {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            lvresul.setNumColumns(3);
        }
        else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            lvresul.setNumColumns(1);
        }
        lvresul.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                app.setEntryselect(listentryObjet.get(position));
                Intent intent = new Intent(MainActivity.CONTEXTO, Detailsapps.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });
        lvresul.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //TODO Auto-generated method stub

                final int currentFirstVisibleItem = view.getFirstVisiblePosition();
                if (currentFirstVisibleItem > mLastFirstVisibleItem) {
                    hideViews();
                    Log.i("a", "scrolling down...");
                } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
                    showViews();
                    Log.i("a", "scrolling up...");
                }
                mLastFirstVisibleItem = currentFirstVisibleItem;
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        listentryObjet = new Entry().GetData(getActivity().getApplicationContext());
        if(listentryObjet.size()==0)
        {
            Obtenerdatos();
        }
        else
        {
            if(Actualizar()&&new DetectordeInternet(getActivity().getApplicationContext()).estasConectado()) {
                Obtenerdatos();
            }
            else
                cargardatosenlista();
        }




        // Inflate the layout for this fragment
        return rootView;
    }
    @Override
    public void onStart() {
        super.onStart();
    }
    /**
     * Permite inicar animacion para escoder el toolbar
     */
    private void hideViews()
    {
        mToolbar.animate().translationY(-mToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFabButton.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        mFabButton.animate().translationY(mFabButton.getHeight() + fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
        FrameLayout.LayoutParams ps = (FrameLayout.LayoutParams) swipeRefreshLayout.getLayoutParams();
        ps.topMargin = 0;
        swipeRefreshLayout.setLayoutParams(ps);
        swipeRefreshLayout.requestLayout();
    }
    /**
     * Permite inicar animacion para mostar el toolbar
     */
    private void showViews() {
        mToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        mFabButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
        swipeRefreshLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        FrameLayout.LayoutParams ps = (FrameLayout.LayoutParams) swipeRefreshLayout.getLayoutParams();
        ps.topMargin = mToolbar.getHeight()+10;
        swipeRefreshLayout.setLayoutParams(ps);
        swipeRefreshLayout.requestLayout();
    }

    /**
     * Pemrite validar si la lista no se ha actulizado reciantemente
     *
     * el tiempo de actulizacion es de 1 hora
     * @return
     */
    private  Boolean Actualizar(){
        Boolean rta = false;
        Calendar Datenow = Calendar.getInstance();
        Calendar Lasupdate = app.getLastupdate();
        long milis1 = Lasupdate.getTimeInMillis();
        long milis2 = Datenow.getTimeInMillis();
        long diff = milis2 - milis1;
        long diffHours = diff / (60 * 60 * 1000);
        if(diffHours>1||diffHours<0) {
            rta=true;
        }
        return rta;
    }
    /**
     * Permite realizar el consumo del webservices para obtener la data a mostar
     */
    private  void Obtenerdatos()
    {
        if(new DetectordeInternet(getActivity().getApplicationContext()).estasConectado()) {
            swipeRefreshLayout.setRefreshing(true);
            String url = "https://itunes.apple.com/us/rss/topfreeapplications/limit=20/json";
            new Thread(new InvocacionRest(url, null, TipoOperacionRest.GET,
                    (new NotificadorInvocacion(ListaFragment.this))
                    , null
            )).start();
        }
        else
        {
            ShowpoupValidacion(getString(R.string.lblerrordeconeccion));
        }
    }
    /**
     * Permite mostrar mensaje de validacion
     * @param Mensaje
     */
    public void ShowpoupValidacion(String Mensaje )
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.CONTEXTO);;
        ProgressDialog dialogProgress = new ProgressDialog(MainActivity.CONTEXTO);
        dialogProgress.setCancelable(false);
        dialogProgress.dismiss();
        builder.setPositiveButton(getString(R.string.lblbtnaceptar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (!new DetectordeInternet(getActivity().getApplicationContext()).estasConectado())
                    ShowpoupValidacion(getString(R.string.lblerrordeconeccion));
                else
                    Obtenerdatos();
            }
        });
        AlertDialog	dialog = builder.create();
        TextView myMsg = new TextView(MainActivity.CONTEXTO);
        myMsg.setText(Mensaje);
        myMsg.setTextSize(20);
        myMsg.setPadding(10, 10, 10, 10);
        myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
        dialog.setView(myMsg);
        dialog.show();
    }
    /**
     * inicializa la configuracion del toolbar
     */
    private void initToolbar() {
        mToolbar = (Toolbar) ROOT.findViewById(R.id.toolbar);
        //getActivity().setSupportActionBar(mToolbar);
        getActivity().setTitle(getString(R.string.app_name));
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }




    /**
     * Permite serializar el texto convertirlo en un json y luego almacenarlo como un archivo plano
     * @param texto
     */
    public  void getTexto(String texto)
    {

    }

    /**
     * Permite cargar los datos en la lista
     */
    public void cargardatosenlista()
    {
        swipeRefreshLayout.setRefreshing(false);
        lvresul.setAdapter(new AdapterMenu(getActivity().getApplicationContext(), listentryObjet));
    }

    /**
     * Permite mostar el error al consumir un webservice
     */
    public  void getTextoError(String textoError)
    {
        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(getActivity().getApplicationContext(),textoError,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        Obtenerdatos();
    }

    @Override
    public void recibirTexto(String texto) {
        try {
            JSONObject raiz = (new JSONObject(texto)).getJSONObject("feed");
            JSONArray entry = raiz.getJSONArray("entry");
            // JSONObject updated = raiz.getJSONObject("updated");
            app.setLastupdate();
            for (int i = 0; i <entry.length() ; i++)
            {
                Entry entryObjet = new Entry();
                JSONObject entrys = entry.getJSONObject(i);
                JSONObject name = entrys.getJSONObject("im:name");
                entryObjet.setName(name.get("label").toString());
                JSONObject artist = entrys.getJSONObject("im:artist");
                entryObjet.setArtist(artist.get("label").toString());
                JSONObject summary = entrys.getJSONObject("summary");
                entryObjet.setSummary(summary.get("label").toString());
                JSONArray image = entrys.getJSONArray("im:image");
                entryObjet.setUrlimagen(image.getJSONObject(2).get("label").toString());
                JSONObject category = entrys.getJSONObject("category");
                JSONObject attributes = category.getJSONObject("attributes");
                entryObjet.setCategory(attributes.get("label").toString());
                JSONObject price = entrys.getJSONObject("im:price");
                JSONObject attributesprice = price.getJSONObject("attributes");
                entryObjet.setPriceamount(attributesprice.get("amount").toString());
                entryObjet.setPricecurrency(attributesprice.get("currency").toString());
                JSONObject releaseDate = entrys.getJSONObject("im:releaseDate");
                JSONObject attributesreleaseDate = releaseDate.getJSONObject("attributes");
                entryObjet.setReleaseDate(attributesreleaseDate.get("label").toString());
                listentryObjet.add(entryObjet);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        OutputStreamWriter archivo = null;
        try {
            archivo = new OutputStreamWriter(getActivity().openFileOutput("data.json", Activity.MODE_PRIVATE));

            try {
                archivo.write(new Gson().toJson(listentryObjet));
                archivo.flush();
                archivo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        cargardatosenlista();
    }

    @Override
    public void recibirError(String textoError) {

    }
}
