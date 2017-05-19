package com.cnpeng.cnpeng_demos2017_01.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnpeng.cnpeng_demos2017_01.R;

import java.util.Date;
import java.util.List;

/**
 * 作者：CnPeng
 * <p>
 * 时间：2017/5/18:下午9:47
 * <p>
 * 说明：自定义日历控件中，用来展示当月中日期的适配器
 */

class CusCalendarRvAdapter extends RecyclerView.Adapter<CusCalendarRvAdapter.DateHolder> {
    Context    context;
    List<Date> datesToShow;

    /**
     * 自定义更新数据的方法，切换月份时需要使用
     */
    public void updateDatesToShow(List<Date> list) {
        this.datesToShow = list;
        notifyDataSetChanged(); //刷新全部数据
    }

    public CusCalendarRvAdapter(Context context, List<Date> datesToShow) {
        this.context = context;
        this.datesToShow = datesToShow;
    }

    @Override
    public DateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_customcalendar, parent, false);
        DateHolder dataHolder = new DateHolder(itemView);
        return dataHolder;
    }

    @Override
    public void onBindViewHolder(DateHolder holder, int position) {
        Date date = datesToShow.get(position);
        int dayOfMonth = date.getDate();
        holder.tv_dateItem.setText(String.valueOf(dayOfMonth));
    }

    @Override
    public int getItemCount() {
        return datesToShow.size();
    }

    public class DateHolder extends RecyclerView.ViewHolder {
        TextView tv_dateItem;

        public DateHolder(View itemView) {
            super(itemView);
            tv_dateItem = (TextView) itemView.findViewById(R.id.tv_cusCalendarCell);
        }
    }
}
