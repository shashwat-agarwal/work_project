package com.example.basic;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class MyAdapter extends FirestoreRecyclerAdapter<Information,MyAdapter.dataHolder> {

    private OnItemClickListener listener;

    public MyAdapter(@NonNull FirestoreRecyclerOptions<Information> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull dataHolder holder, int position, @NonNull Information model) {

        try{
        holder.name.setText("Name: "+model.getName());
        holder.phone.setText("Phone: "+model.getPhone());
        holder.problem.setText("Purpose: "+model.getProblem());
        holder.address.setText("Address: "+model.getAddress());
        holder.date.setText("Date: "+model.getDate());
        holder.report.setText("Report no: "+model.getReport());


          Uri myUri = Uri.parse(model.getImgUri());
         Log.i("uri", String.valueOf(myUri));
         holder.image.setVisibility(View.VISIBLE);
           Picasso.get()
                   .load(model.getImgUri())
                   .into(holder.image);

       }catch (Exception e){
           Log.i("error",e.toString());

       }
    }

    @NonNull
    @Override
    public dataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,
                parent,false);

        return new dataHolder(v);
    }

    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    public class dataHolder extends RecyclerView.ViewHolder {
        TextView name,address,phone,problem,date,report;
        ImageView image;
        public dataHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.textView_name_card);
            address=itemView.findViewById(R.id.textView_address);
            phone=itemView.findViewById(R.id.textView_phone_card);
            problem=itemView.findViewById(R.id.textView_problem_card);
            image=itemView.findViewById(R.id.imageView_card);
            date=itemView.findViewById(R.id.textView_date_card);
            report=itemView.findViewById(R.id.textView_report_card);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if (position!=RecyclerView.NO_POSITION && listener!=null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }
    }
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){

        this.listener=listener;
    }
}
