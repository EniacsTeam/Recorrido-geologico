package com.eniacs_team.rutamurcielago;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * Adaptador que controla los elementos del recyclerView.
 */

public class adapterMenuMultimedia extends RecyclerView.Adapter<adapterMenuMultimedia.ViewHolder> {


    private List<listItemMenuMultimedia> listItems;
    private Context context;

    public adapterMenuMultimedia(List<listItemMenuMultimedia> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    /**
     * Método que crea el layout para un item del view holder.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_multimedia, parent,false);
        return new ViewHolder(v);
    }

    /**
     *
     *
     * @param holder   El viewHolder que es modificado segun información deseada.
     * @param position posición en el viewHolder.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        listItemMenuMultimedia listItem = listItems.get(position);

        if(listItem.getTitulo().equals("Audio")){
            holder.cardView.setBackground(context.getDrawable(R.drawable.audios));
        }else if(listItem.getTitulo().equals("Imagen")){
            holder.cardView.setBackground(context.getDrawable(R.drawable.imagenes));
        }else if(listItem.getTitulo().equals("Video")){
            holder.cardView.setBackground(context.getDrawable(R.drawable.videos));
        }else if(listItem.getTitulo().equals("Animacion")){
            holder.cardView.setBackground(context.getDrawable(R.drawable.animaciones));
        }



    }

    /**
     * Devuelve el numero de items en el data set.
     *
     * @return Tnumero de items en el adapter.
     */
    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //public TextView titulo;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            //titulo = (TextView) itemView.findViewById(R.id.titulo_multimedia);
            cardView = (CardView) itemView.findViewById(R.id.cv);
            cardView.setOnClickListener(this);

            cardView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {


                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {


                    ViewGroup.LayoutParams Lp =  v.getLayoutParams();
                    Bitmap backgroundBitmap = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.animaciones);

                    float imageRatio = (float) backgroundBitmap.getWidth() / (float) backgroundBitmap.getHeight();

                    int imageRealHeight = (int) (right / imageRatio);


                    Lp.height = imageRealHeight;
                    v.setLayoutParams(Lp);




                }

            });



        }

        /**
         * Llamado cuando un cardView es seleccionado, entra a la galeria de dicho elemento..
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {

            if(listItems.get(getAdapterPosition()).getTitulo().equals("Audio")){
                Intent intent = new Intent(context, reproductor_audio.class);
                intent.putExtra("id", listItems.get(getAdapterPosition()).getId());
                context.startActivity(intent);
            }else if(listItems.get(getAdapterPosition()).getTitulo().equals("Imagen")){
                Intent intent = new Intent(context, Gallery.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", String.valueOf(listItems.get(getAdapterPosition()).getId()));
                context.startActivity(intent);
            }else{
                Toast.makeText(context, listItems.get(getAdapterPosition()).getTitulo(), Toast.LENGTH_SHORT).show();
            }
           /* */
        }
    }
}
