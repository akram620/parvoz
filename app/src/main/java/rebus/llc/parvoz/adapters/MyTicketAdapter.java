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
import rebus.llc.parvoz.models.MyTicketModel;
import rebus.llc.parvoz.others.Settings;

import static rebus.llc.parvoz.others.Utilities.convertFromatYMDtoDMY;

public class MyTicketAdapter  extends RecyclerView.Adapter<MyTicketAdapter.ViewHolder>  {

    private Context context;
    private ArrayList<MyTicketModel> listItems;
    Settings settings;
    private MyTicketAdapter.ClickListener clickListener;

    public MyTicketAdapter(ArrayList<MyTicketModel> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public MyTicketAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mytickets_list_item, parent, false);
        return new MyTicketAdapter.ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(MyTicketAdapter.ViewHolder holder, int position) {

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        MyTicketModel listItem  = listItems.get(position);

        settings = new Settings(context);

        holder.txtViewFrom.setText(listItem.getGorod_vyleta_name());
        holder.txtViewTo.setText(listItem.getGorod_prileta_name());
        holder.textViewFlightDate.setText(convertFromatYMDtoDMY(listItem.getData_vyleta()));
        holder.txtViewToAirport.setText(listItem.getAeroport_prileta_name());
        holder.txtViewFromAirport.setText(listItem.getAeroport_vyleta_name());

        if(listItem.getValjuta()!=null) {
            holder.txtViewCost.setText("" + listItem.getStoimost() + " " + listItem.getValjuta());
        }
        holder.textViewFlightTime.setText(listItem.getVremja_vyleta());

        if(listItem.getStatus_id().equals("novyj")){
            holder.mailLayout.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.textViewFlightStatus.setText("Новый заказ");
            holder.textViewFlightStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }else if(listItem.getStatus_id().equals("obrabatyvaetsja")){
            holder.mailLayout.setBackgroundColor(context.getResources().getColor(R.color.colorParvozOrange));
            holder.textViewFlightStatus.setText("Обрабатывается");
            holder.textViewFlightStatus.setTextColor(context.getResources().getColor(R.color.colorParvozOrange));
        }else if(listItem.getStatus_id().equals("ozhidaet-oplatu")){
            holder.mailLayout.setBackgroundColor(context.getResources().getColor(R.color.colorParvozOrange));
            holder.textViewFlightStatus.setText("Ожидает оплату");
            holder.textViewFlightStatus.setTextColor(context.getResources().getColor(R.color.colorParvozOrange));
        }else if(listItem.getStatus_id().equals("oplachen")){
            holder.mailLayout.setBackgroundColor(context.getResources().getColor(R.color.colorParvozGreen));
            holder.textViewFlightStatus.setText("Оплачен");
            holder.textViewFlightStatus.setTextColor(context.getResources().getColor(R.color.colorParvozGreen));
        }else if(listItem.getStatus_id().equals("otmenjon")){
            holder.mailLayout.setBackgroundColor(context.getResources().getColor(R.color.colorParvozGrey));
            holder.textViewFlightStatus.setText("Отменен");
            holder.textViewFlightStatus.setTextColor(context.getResources().getColor(R.color.colorParvozGrey));
        }

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
        public TextView textViewFlightStatus;
        public TextView textViewFlightTime;
        public TextView txtViewCost;

        public LinearLayout mailLayout;

        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            mailLayout = (LinearLayout) itemView.findViewById(R.id.mailLayout);
            txtViewFrom = (TextView)  itemView.findViewById(R.id.txtViewFrom);
            txtViewFromAirport = (TextView)  itemView.findViewById(R.id.txtViewFromAirport);
            txtViewTo = (TextView)  itemView.findViewById(R.id.txtViewTo);
            txtViewToAirport = (TextView)  itemView.findViewById(R.id.txtViewToAirport);
            textViewFlightDate = (TextView)  itemView.findViewById(R.id.textViewFlightDate);
            textViewFlightStatus = (TextView)  itemView.findViewById(R.id.textViewFlightStatus);
            textViewFlightTime = (TextView)  itemView.findViewById(R.id.textViewFlightTime);
            txtViewCost = (TextView)  itemView.findViewById(R.id.txtViewCost);

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

                clickListener.itemMyTicketClicked(listItems.get(getPosition()), getPosition());

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

    public void setFilter(ArrayList<MyTicketModel> arrayList){
        listItems =  new ArrayList<>();
        listItems.addAll(arrayList);
        notifyDataSetChanged();
    }

    public MyTicketModel getItem(int pos){

        return listItems.get(pos);
    }

    public void setClickListener(MyTicketAdapter.ClickListener clickListener){
        this.clickListener = clickListener;
    }

    public interface ClickListener{
        void itemMyTicketClicked(MyTicketModel model, int position);
    }
}
