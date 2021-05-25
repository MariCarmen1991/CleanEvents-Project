package com.example.cleanevents;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class EventosAdapter extends RecyclerView.Adapter<EventosAdapter.ViewHolder> implements View.OnClickListener {
    ArrayList<Evento> listEventos;
    private LayoutInflater mInflater;
    private Context context;
    private View.OnClickListener listener;
    ImageView iconoActividad;

    public EventosAdapter(ArrayList<Evento> itemEventos, Context context) {
        this.listEventos = itemEventos;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }
    public void setOnClickListener(View.OnClickListener listener){

        this.listener=listener;
    }

    @Override
    public EventosAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v=mInflater.inflate(R.layout.card_evento, null);
        v.setOnClickListener(this);
        Log.d("MARICARMEN", "RECYCLER!!");

        return new EventosAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final EventosAdapter.ViewHolder holder, int position) {
        holder.bindData(listEventos.get(position));
        Picasso.get()
                .load(listEventos.get(position).getImagen()) // internet path
                .placeholder(R.mipmap.ic_launcher)  // preload
                .error(R.mipmap.ic_launcher_round) // load error
                .into(holder.imagenActividad);

    }

    @Override
    public int getItemCount() {
        return listEventos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView  imagenActividad;
        TextView nombreActividad, fechaActividad, organizadorActividad, lugarActividad, numeroParticipantes;
        Usuario usuario = new Usuario();

        ViewHolder(View itemView) {
            super(itemView);
            iconoActividad=itemView.findViewById(R.id.icon_tipo_actividad);
            imagenActividad=itemView.findViewById(R.id.imagen_id);
            nombreActividad=itemView.findViewById(R.id.nombre_actividad_id);
            fechaActividad=itemView.findViewById(R.id.fecha_actividad_id);
            organizadorActividad=itemView.findViewById(R.id.organizador_actividad_id);
            lugarActividad=itemView.findViewById(R.id.lugar_actividad_id);
            numeroParticipantes=itemView.findViewById(R.id.participantes_actividad_id);
        }

        public void bindData(final Evento item) {
            nombreActividad.setText(item.getNombre());
            //fechaActividad.setText(item.getFecha().toString());//modificar si es necesario pueden haber problemas con el tipo
            organizadorActividad.setText(item.getNombreOrganizador());
            lugarActividad.setText(item.getPoblacion());
            numeroParticipantes.setText(""+item.getNumParticipantes());

            switch (item.getTipoActividad()){

                case "Playa":
                    iconoActividad.setImageResource(R.drawable.outline_waves_black_18);
                    break;
                case "Fondo Marino":
                    iconoActividad.setImageResource(R.drawable.sea3);

                    break;

                case "Bosque":
                    iconoActividad.setImageResource(R.drawable.round_nature_black_18);
                    break;
                case "Río":
                    iconoActividad.setImageResource(R.drawable.twotone_legend_toggle_black_18);

                    break;

                case "Ciudad":
                    iconoActividad.setImageResource(R.drawable.twotone_location_city_black_18);

                    break;

            }


            //falta hacer el set de las imágenes

        }
    }


}
