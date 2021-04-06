package rebus.llc.parvoz.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import rebus.llc.parvoz.R;
import rebus.llc.parvoz.models.HorizontalListModel;
import rebus.llc.parvoz.others.Settings;

public class HurizonatlListAdapter  extends RecyclerView.Adapter<HurizonatlListAdapter.ViewHolder>  {

    private Context context;
    private ArrayList<HorizontalListModel> listItems;
    Settings settings;
    private  ClickListener clickListener;

    public HurizonatlListAdapter(ArrayList<HorizontalListModel> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizontal_list_item, parent, false);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        HorizontalListModel listItem  = listItems.get(position);

        settings = new Settings(context);
        holder.txtView.setText(listItem.getCountryName());

        if(listItem.isState()){
            holder.imgView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_airplane_white_menu));
            holder.txtView.setTextColor(context.getResources().getColor(R.color.colorParvozWhite));
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorParvozOrange2));
            holder.linearOut.setBackgroundColor(context.getResources().getColor(R.color.colorParvozOrange2));
        }else{
            holder.imgView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_airplane_orange_menu));
            holder.txtView.setTextColor(context.getResources().getColor(R.color.colorParvozOrange2));
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorParvozWhite));
            holder.linearOut.setBackgroundColor(context.getResources().getColor(R.color.colorParvozOrange2));
        }

    }



    @Override
    public int getItemCount() {
        return listItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        public TextView txtView;
        public ImageView imgView;
        public LinearLayout linearLayout;
        public LinearLayout linearOut;

        //public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            linearOut = (LinearLayout) itemView.findViewById(R.id.linearOut);
            txtView = (TextView)  itemView.findViewById(R.id.txtView);
            imgView  = (ImageView) itemView.findViewById(R.id.imgView);

            //cardView = (CardView) itemView.findViewById(R.id.cardView);
            linearLayout.setOnClickListener(this);
            linearLayout.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View view) {

            if (clickListener != null) {//Обработчик нажатия на адрес
//                if(!listItems.get(getPosition()).isState()){
//                    listItems.get(getPosition()).setState(true);
//                }else{
//                    listItems.get(getAdapterPosition()).setState(false);
//                }

                clickListener.itemHorizontalClicked(listItems.get(getPosition()), getPosition());

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

    public void setFilter(ArrayList<HorizontalListModel> arrayList){
        listItems =  new ArrayList<>();
        listItems.addAll(arrayList);
        notifyDataSetChanged();
    }

    public HorizontalListModel getItem(int pos){

        return listItems.get(pos);
    }

    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }

    public interface ClickListener{
        void itemHorizontalClicked(HorizontalListModel model, int position);
    }
}
