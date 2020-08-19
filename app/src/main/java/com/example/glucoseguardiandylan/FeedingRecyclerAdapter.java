package com.example.glucoseguardiandylan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//Puts the data into the recycler view to show a list of feedings in the main activity
public class FeedingRecyclerAdapter extends RecyclerView.Adapter<FeedingRecyclerAdapter.FeedingHolder> { // Holds views of the feeding data items on screen
    private List<Feeding> feedings = new ArrayList<>(); //list of feedings
    private OnItemClickListener listener;

    @NonNull
    @Override
    public FeedingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feeding_item, parent, false);
        return new FeedingHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedingHolder holder, int position) { //gets the data from java obj and puts in into the screen view
        Feeding currentFeeding = feedings.get(position); //reference to feeding data at this position in the array
        String formattedDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(currentFeeding.getDate());
        holder.textViewBloodSugar.setText(String.valueOf(currentFeeding.getBloodSugar()));
        holder.textViewDescription.setText(currentFeeding.getFoodInfo());
        holder.textViewInsulin.setText(String.valueOf(currentFeeding.getInsulin()));
        holder.textViewCarbs.setText(String.valueOf(currentFeeding.getCarbs()));
        holder.textViewMealInfo.setText(String.valueOf(currentFeeding.getMealInfo()));
        holder.textViewDate.setText(formattedDate);
    }

    @Override
    public int getItemCount() { //how many items you want to display in the recycler view
        return feedings.size();
    }

    public void setFeedings(List<Feeding> feedings) { //pass list of feedings to the recycler view so it can observe the live data
        this.feedings = feedings;
        notifyDataSetChanged(); //replace with more efficient method later / tells the adapter to redraw the layout
    }

    public Feeding getFeedingAt(int position) {
        return feedings.get(position);
    }

    class FeedingHolder extends RecyclerView.ViewHolder {
        private TextView textViewBloodSugar;
        private TextView textViewInsulin;
        private TextView textViewDescription;
        private TextView textViewCarbs;
        private TextView textViewMealInfo;
        private TextView textViewDate;

        public FeedingHolder(View itemView) {
            super(itemView);
            textViewBloodSugar = itemView.findViewById(R.id.text_view_blood_sugar);
            textViewInsulin = itemView.findViewById(R.id.text_view_insulin);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewCarbs = itemView.findViewById(R.id.text_view_carbs);
            textViewMealInfo = itemView.findViewById(R.id.text_view_meal_info);
            textViewDate = itemView.findViewById(R.id.text_view_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    //Check if listener was ever called and that the position is valid
                    // No Position is a constant that is equal to -1, an invalid position
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(feedings.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Feeding feeding);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}