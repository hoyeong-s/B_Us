package ssafy.third.bus;

import static ssafy.third.bus.Home.android_id;
import static ssafy.third.bus.Home.arsId;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ssafy.third.bus.function.TTS;

public class Alarm_Adapter extends RecyclerView.Adapter {
    List<String> myModelList;
    OnBtnClickListener mOnBtnClickListener;
    public Alarm_Adapter(List<String> myModelList,OnBtnClickListener mOnBtnClickListener) {
        this.myModelList = myModelList;
        this.mOnBtnClickListener = mOnBtnClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.buslist_layout,parent,false);
        return new myViewHolder(view,mOnBtnClickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String line = myModelList.get(position);
        String [] arr = line.split("\":\"|\",\"");
        ((myViewHolder)holder).btn.setText(arr[1]+"   "+arr[3]);
    }


    @Override
    public int getItemCount() {
        return myModelList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button btn;
        TTS tts = new TTS();
        OnBtnClickListener mOnBtnClickListener;
        public myViewHolder(@NonNull View itemView , final OnBtnClickListener mOnBtnClickListener) {
            super(itemView);
            btn = itemView.findViewById(R.id.button);
            this.mOnBtnClickListener = mOnBtnClickListener;

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnBtnClickListener != null){
                        int position = getAdapterPosition();

                        String line = myModelList.get(position);
                        String [] arr = line.split("\":\"|\",\"");

                        try{
                            URLConnector_delete connector = new URLConnector_delete();
                            connector.execute(android_id,arr[1]).get();
                        }catch (Exception e){
                        }

                        tts.speakOut(arr[1]+" 버스를 삭제했습니다 ");
                        if (position != RecyclerView.NO_POSITION){
                            mOnBtnClickListener.onDeleteBtnClick(position);
                        }
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }

    public void updateList(List<String> myModelList) {
        this.myModelList = myModelList;
        notifyDataSetChanged();
    }

    public interface OnBtnClickListener{
        void onDeleteBtnClick(int position);
    }
}