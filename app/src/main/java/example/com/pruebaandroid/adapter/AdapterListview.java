package example.com.pruebaandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import example.com.pruebaandroid.Aplicationapp;
import example.com.pruebaandroid.DataObjects.Data;
import example.com.pruebaandroid.R;


/**
 * Created by andresdavid on 08/10/2015.
 */
public class AdapterListview extends BaseAdapter {
    private static ArrayList<Data> itemDetailsrrayList;

    private LayoutInflater l_Inflater;
Aplicationapp app;

    public AdapterListview(Context context, ArrayList<Data> results) {
        app = (Aplicationapp)context.getApplicationContext();
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

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = l_Inflater.inflate(R.layout.item_list, null);
            holder = new ViewHolder();
            holder.txt_itemNombre = (TextView) convertView.findViewById(R.id.lbl_name);
            holder.btnurlImage = (ImageButton)convertView.findViewById(R.id.btnloadimage);
            holder.btnGaleryImage = (ImageButton)convertView.findViewById(R.id.btngaleryimage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txt_itemNombre.setText(itemDetailsrrayList.get(position).getName());
        holder.btnurlImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //app.mainActivityFragment.showpopup(position,null);
            }
        });
        holder.btnGaleryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //app.mainActivityFragment.AbrirGaleria();
            }
        });
        return convertView;
    }
    static class ViewHolder
    {
        TextView txt_itemNombre;
        ImageButton btnurlImage;
        ImageButton btnGaleryImage;
    }
}

