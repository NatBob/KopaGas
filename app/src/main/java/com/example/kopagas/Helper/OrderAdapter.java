package com.example.kopagas.Helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kopagas.R;
import com.example.kopagas.model.Message;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<Message> messages;
    private Context mCtx;

    public OrderAdapter(List<Message> messages, Context mCtx) {
        this.messages = messages;
        this.mCtx = mCtx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sent_msg_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(OrderAdapter.ViewHolder holder, int position) {
        final Message message = messages.get(position);
        holder.textViewName.setText(message.getFrom());
        holder.textViewTitle.setText(message.getTitle());
        holder.textViewMessage.setText(message.getMessage());
        holder.textViewTime.setText(message.getSent());
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewName;
        public TextView textViewTitle;
        public TextView textViewMessage;
        public TextView textViewTime;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            textViewMessage = (TextView) itemView.findViewById(R.id.textViewMessage);
            textViewTime = (TextView) itemView.findViewById(R.id.textViewTime);
        }
    }
}
