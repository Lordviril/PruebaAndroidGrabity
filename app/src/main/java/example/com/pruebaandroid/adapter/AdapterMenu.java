package example.com.pruebaandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import example.com.pruebaandroid.Aplicationapp;
import example.com.pruebaandroid.DataObjects.Entry;
import example.com.pruebaandroid.MainActivity;
import example.com.pruebaandroid.R;
import example.com.pruebaandroid.utils.ImageTransroundedcorner;


/**
 * Created by adserranov on 02/09/2015.
 */
public class AdapterMenu extends BaseAdapter {
    private static ArrayList<Entry> itemDetailsrrayList;

    private LayoutInflater l_Inflater;
    Aplicationapp app;

    public AdapterMenu(Context context, ArrayList<Entry> results) {
        app = (Aplicationapp) context.getApplicationContext();
        itemDetailsrrayList = results;
        l_Inflater = LayoutInflater.from(context);

    }

    public int getCount() {
        return itemDetailsrrayList.size();
    }

    public Object getItem(int position) {
        return itemDetailsrrayList.get(position);
    }

    public long getItemId(int position) {


        return position;
    }

    ArrayList<ImageView> images = new ArrayList<ImageView>();
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = l_Inflater.inflate(R.layout.item_menu, null);
            holder = new ViewHolder();
            holder.txt_itemNombre = (TextView) convertView.findViewById(R.id.lbl_nombre);
            holder.txt_itemcompany = (TextView) convertView.findViewById(R.id.lbl_company);
            holder.img_itemfoto = (ImageView)convertView.findViewById(R.id.IVMenu);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txt_itemNombre.setText(itemDetailsrrayList.get(position).getName());
        holder.txt_itemcompany.setText(itemDetailsrrayList.get(position).getArtist());

        Picasso.with(convertView.getContext())
                .load(itemDetailsrrayList.get(position).getUrlimagen())
                        //.placeholder(R.drawable.ic_placeholder)   // optional
                //.error(R.drawable.ic_error_fallback)      // optional
                .transform(new ImageTransroundedcorner())
                .into(holder.img_itemfoto);

        images.add(holder.img_itemfoto);

        Animation anim;
        anim = AnimationUtils.loadAnimation(MainActivity.CONTEXTO, R.anim.scale);
        anim.reset();
        holder.img_itemfoto.startAnimation(anim);
        return convertView;
    }
    static class ViewHolder {
        LinearLayout lycontenedortarjetas;
        TextView txt_itemNombre;
        TextView txt_itemcompany;
        ImageView img_itemfoto;

    }
}

