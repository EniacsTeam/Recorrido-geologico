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
 * Created by kenca on 14/05/2017.
 */

public class adapterMenuMultimedia extends RecyclerView.Adapter<adapterMenuMultimedia.ViewHolder> {


    private List<listItemMenuMultimedia> listItems;
    private Context context;

    public adapterMenuMultimedia(List<listItemMenuMultimedia> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
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
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override {@link #onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
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
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
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

        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            Toast.makeText(context, listItems.get(getAdapterPosition()).getTitulo(), Toast.LENGTH_SHORT).show();
           /* Intent intent = new Intent(context, Galeria.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("Id", String.valueOf(listItems.get(getAdapterPosition()).getId()));
            context.startActivity(intent);*/
        }
    }
}
