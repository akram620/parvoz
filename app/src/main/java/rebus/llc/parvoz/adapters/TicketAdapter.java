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
import rebus.llc.parvoz.models.TicketModel;
import rebus.llc.parvoz.others.Settings;

import static rebus.llc.parvoz.others.Utilities.convertFromatYMDtoDMY;

public class TicketAdapter   extends RecyclerView.Adapter<TicketAdapter.ViewHolder>  {

    private Context context;
    private ArrayList<TicketModel> listItems;
    Settings settings;
    private TicketAdapter.ClickListener clickListener;

    public TicketAdapter(ArrayList<TicketModel> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public TicketAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ticket_list_item, parent, false);
        return new TicketAdapter.ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(TicketAdapter.ViewHolder holder, int position) {

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        TicketModel listItem  = listItems.get(position);

        settings = new Settings(context);

        holder.txtViewFrom.setText(listItem.getGorod_vyleta_name());
        holder.txtViewTo.setText(listItem.getGorod_prileta_name());
        holder.textViewFlightDate.setText(convertFromatYMDtoDMY(listItem.getData_vyleta()));
        holder.textViewFlightTime.setText(listItem.getVremja_vyleta());

        holder.txtViewFromAirport.setText(listItem.getAeroport_vyleta_name());
        holder.txtViewToAirport.setText(listItem.getAeroport_prileta_name());
        holder.txtViewCost.setText(""+listItem.getStoimost()+" "+listItem.getValjuta());

    }



    @Override
    public int getItemCount() {
        return listItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        public TextView txtViewFrom;
        public TextView txtViewFromAirport;
        public TextView txtViewTo;
        public TextView txtViewToAirport;
        public TextView textViewFlightDate;
        public TextView txtViewCost;
        public TextView textViewFlightTime;

        public LinearLayout mailLayout;

        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            mailLayout = (LinearLayout) itemView.findViewById(R.id.mailLayout);
            txtViewTo = (TextView)  itemView.findViewById(R.id.txtViewTo);
            txtViewFrom = (TextView)  itemView.findViewById(R.id.txtViewFrom);
            txtViewFromAirport = (TextView)  itemView.findViewById(R.id.txtViewFromAirport);
            txtViewToAirport = (TextView)  itemView.findViewById(R.id.txtViewToAirport);
            textViewFlightDate = (TextView)  itemView.findViewById(R.id.textViewFlightDate);
            txtViewCost = (TextView)  itemView.findViewById(R.id.txtViewCost);
            textViewFlightTime = (TextView)  itemView.findViewById(R.id.textViewFlightTime);

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

                clickListener.itemNewFlightClicked(listItems.get(getPosition()), getPosition());

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

    public void setFilter(ArrayList<TicketModel> arrayList){
        listItems =  new ArrayList<>();
        listItems.addAll(arrayList);
        notifyDataSetChanged();
    }

    public TicketModel getItem(int pos){

        return listItems.get(pos);
    }

    public void setClickListener(TicketAdapter.ClickListener clickListener){
        this.clickListener = clickListener;
    }

    public interface ClickListener{
        void itemNewFlightClicked(TicketModel model, int position);
    }
}
