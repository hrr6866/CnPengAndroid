package com.cnpeng.cnpeng_demos2017_01.b_33_BaseRvAdapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cnpeng.cnpeng_demos2017_01.R;
import com.cnpeng.cnpeng_demos2017_01.databinding.ItemRvBinding;

import java.util.List;

/**
 * 作者：CnPeng
 * 时间：2018/6/15
 * 功用：
 * 其他：
 */
public class MyRvAdapter extends BaseRvAdapter {
    private Context      mContext;
    private List<String> mList;

    public MyRvAdapter(Context context, RecyclerView recyclerView) {
        super(context, recyclerView);
        mContext = context;
    }

    public void setData(List<String> list) {
        mList = list;
    }

    @Override
    int getContentCount() {
        return mList.size();
    }

    @Override
    int getContentItemType(int position) {
        return 0;
    }

    @Override
    void onBindContentHolder(RecyclerView.ViewHolder holder, int position) {
        // TODO: CnPeng 2018/6/14 下午12:06 这个三个position 和 position有什么区别呢？
        //        int realPosition = holder.getAdapterPosition();
        //        int realPosition2 = holder.getLayoutPosition();
        //        int oldPosition = holder.getOldPosition();

        if (holder instanceof ContentHolder) {
            String str = mList.get(position);
            ((ContentHolder) holder).mBinding.tv.setText(str);
        }
    }

    @Override
    RecyclerView.ViewHolder onCreateContentHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        ItemRvBinding itemBinding = DataBindingUtil.inflate(inflater, R.layout.item_rv, parent, false);
        View itemView = itemBinding.getRoot();

        ContentHolder contentHolder = new ContentHolder(itemView);
        contentHolder.setBinding(itemBinding);

        return contentHolder;
    }

    public class ContentHolder extends RecyclerView.ViewHolder {
        private ItemRvBinding mBinding;

        public ContentHolder(View itemView) {
            super(itemView);
        }

        public void setBinding(ItemRvBinding binding) {
            mBinding = binding;
        }
    }
}
