package com.example.enjoy.enjoycamera;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by admin on 2018/04/15   .
 */

public class ModeAdapter extends RecyclerView.Adapter<ModeAdapter.ViewHolder>{
    private List<ModeItem> mModeList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView modeImage;
        TextView modeName;

        public ViewHolder(View itemView) {
            super(itemView);
            modeImage = itemView.findViewById(R.id.ivModeImage);
            modeName = itemView.findViewById(R.id.tvModeName);
        }
    }

    public ModeAdapter(List<ModeItem> modeList) {
        this.mModeList = modeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mode_item,
                parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ModeItem modeItem = mModeList.get(position);
        holder.modeImage.setImageResource(modeItem.getImageId());
        holder.modeName.setText(modeItem.getName());
    }

    @Override
    public int getItemCount() {
        return mModeList.size();
    }

}
