package com.cnpeng.cnpeng_demos2017_01.b_33_BaseRvAdapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cnpeng.cnpeng_demos2017_01.R;
import com.cnpeng.cnpeng_demos2017_01.databinding.ItemRvBinding;

import java.util.List;

/**
 * 作者：CnPeng
 * 时间：2018/6/14
 * 功用：
 * 其他：
 */
public class RvAdapter extends RecyclerView.Adapter<RvAdapter.RvHolder> {

    private List<String> mList;
    private Context      mContext;

    public RvAdapter(Context context, List<String> list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public RvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ItemRvBinding itemBinding = DataBindingUtil.inflate(inflater, R.layout.item_rv, parent, false);
        View itemView = itemBinding.getRoot();

        RvHolder holder = new RvHolder(itemView);
        holder.setBinding(itemBinding);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RvHolder holder, int position) {
        // TODO: CnPeng 2018/6/14 下午12:06 这个三个position 和 position有什么区别呢？
        int realPosition = holder.getAdapterPosition();
        int realPosition2 = holder.getLayoutPosition();
        int oldPosition = holder.getOldPosition();

        String str = mList.get(position);
        holder.mBinding.tv.setText(str);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class RvHolder extends RecyclerView.ViewHolder {
        private ItemRvBinding mBinding;

        public RvHolder(View itemView) {
            super(itemView);
        }

        public void setBinding(ItemRvBinding binding) {
            mBinding = binding;
        }
    }
}
