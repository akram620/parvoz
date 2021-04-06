package rebus.llc.parvoz.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import rebus.llc.parvoz.R;
import rebus.llc.parvoz.models.ObjavlenijaModel;
import rebus.llc.parvoz.others.Settings;

public class NotifAdapter extends RecyclerView.Adapter<NotifAdapter.ViewHolder>  {

    private Context context;
    private ArrayList<ObjavlenijaModel> listItems;
    Settings settings;
    private NotifAdapter.ClickListener clickListener;

    public NotifAdapter(ArrayList<ObjavlenijaModel> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public NotifAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notif_item, parent, false);
        return new NotifAdapter.ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(NotifAdapter.ViewHolder holder, int position) {

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        ObjavlenijaModel listItem  = listItems.get(position);

        settings = new Settings(context);

        holder.txtViewTitle.setText(listItem.getZagolovok());
        holder.txtViewDescription.setText(listItem.getOpisanie());
        holder.textViewDateTime.setText(listItem.getCreated_at());



    }



    @Override
    public int getItemCount() {
        return listItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        public TextView txtViewTitle;
        public TextView txtViewDescription;
        public TextView textViewDateTime;

        public LinearLayout mailLayout;

        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            mailLayout = (LinearLayout) itemView.findViewById(R.id.mailLayout);
            txtViewTitle = (TextView)  itemView.findViewById(R.id.txtViewTitle);
            txtViewDescription = (TextView)  itemView.findViewById(R.id.txtViewDescription);
            textViewDateTime = (TextView)  itemView.findViewById(R.id.textViewDateTime);


            cardView = (CardView) itemView.findViewById(R.id.cardView);
            cardView.setOnClickListener(this);
            cardView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View view) {

            if (clickListener != null) {//Обработчик нажатия на адрес
//                if(!listItems.get(getPosition()).isState()){
//                    listItems.get(getPosition()).setState(true);
//                }else{
//                    listItems.get(getAdapterPosition()).setState(false);
//                }

                clickListener.itemNotifClicked(listItems.get(getPosition()), getPosition());

                Animation animation = new AlphaAnimation(1.0f, 0.0f);
                animation.setDuration(1000);
                view.startAnimation(animation);

            }
        }

        @Override
        public boolean onLongClick(View view) {

            return false;
        }
    }

    public void setFilter(ArrayList<ObjavlenijaModel> arrayList){
        listItems =  new ArrayList<>();
        listItems.addAll(arrayList);
        notifyDataSetChanged();
    }

    public ObjavlenijaModel getItem(int pos){

        return listItems.get(pos);
    }

    public void setClickListener(NotifAdapter.ClickListener clickListener){
        this.clickListener = clickListener;
    }

    public interface ClickListener{
        void itemNotifClicked(ObjavlenijaModel model, int position);
    }
}
