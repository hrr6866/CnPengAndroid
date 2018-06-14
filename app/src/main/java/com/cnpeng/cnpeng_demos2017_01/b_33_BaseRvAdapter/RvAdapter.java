package com.cnpeng.cnpeng_demos2017_01.b_33_BaseRvAdapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.cnpeng.cnpeng_demos2017_01.R;
import com.cnpeng.cnpeng_demos2017_01.databinding.FooterRvBinding;
import com.cnpeng.cnpeng_demos2017_01.databinding.ItemRvBinding;

import java.util.List;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * 作者：CnPeng
 * 时间：2018/6/14
 * 功用：
 * 其他：
 */
public class RvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_TYPE_HEADER  = 0;
    private final int ITEM_TYPE_FOOTER  = 1;
    private final int ITEM_TYPE_CONTENT = 2;
    private final RecyclerView          mRv;
    private       boolean               isLoadingMore;
    private       List<String>          mList;
    private       Context               mContext;
    private       boolean               mIsAtBottom;
    private       int                   mLastY;
    private       int                   mDownY;
    private       int                   mDownX;
    private       OnLoadingMoreListener mLoadingMoreListener;
    private       int                   mTouchSlop;


    public RvAdapter(Context context, List<String> list, RecyclerView recyclerView) {
        mContext = context;
        mList = list;
        mRv = recyclerView;

        //触摸时最小的响应距离
        mTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();

        initRvScrollListener();
        initRvTouchListener();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        switch (viewType) {
            case ITEM_TYPE_FOOTER:
                FooterRvBinding footerBinding = DataBindingUtil.inflate(inflater, R.layout.footer_rv, parent, false);
                View footerView = footerBinding.getRoot();
                FooterHolder footerHolder = new FooterHolder(footerView);
                footerHolder.setBinding(footerBinding);
                return footerHolder;
            case ITEM_TYPE_CONTENT:
            default:
                ItemRvBinding itemBinding = DataBindingUtil.inflate(inflater, R.layout.item_rv, parent, false);
                View itemView = itemBinding.getRoot();

                ContentHolder contentHolder = new ContentHolder(itemView);
                contentHolder.setBinding(itemBinding);

                return contentHolder;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        // TODO: CnPeng 2018/6/14 下午12:06 这个三个position 和 position有什么区别呢？
        int realPosition = holder.getAdapterPosition();
        int realPosition2 = holder.getLayoutPosition();
        int oldPosition = holder.getOldPosition();

        if (holder instanceof ContentHolder) {
            String str = mList.get(position);
            ((ContentHolder) holder).mBinding.tv.setText(str);
        } else {
            ((FooterHolder) holder).mBinding.tvLoadingHint.setText("正在加载");
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return ITEM_TYPE_FOOTER;
        } else {
            return ITEM_TYPE_CONTENT;
        }
        //        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        //+1 是脚布局
        return mList.size() + 1;
    }

    private void initRvScrollListener() {
        mRv.setOnScrollListener(new RecyclerView.OnScrollListener() {

            int firstVisibleItem, visibleItemCount, totalItemCount, lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (SCROLL_STATE_IDLE == newState) {
                    if (mIsAtBottom && mLastY - mDownY < 0 && Math.abs(mLastY - mDownY) > mTouchSlop) {
                        if (!isLoadingMore || null != mLoadingMoreListener) {
                            isLoadingMore = true;
                            mLoadingMoreListener.onLoadingMore();
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    visibleItemCount = recyclerView.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    firstVisibleItem = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                    lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    mIsAtBottom = (totalItemCount - visibleItemCount) <= firstVisibleItem;
                }
            }
        });
    }

    private void initRvTouchListener() {

        mRv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                int action = ev.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mDownY = (int) ev.getY();
                        mDownX = (int) ev.getX();

                        if (null != mRv) {
                            //解决RV的0索引条目未展示出来时会触发下拉的情况。
                            RecyclerView.LayoutManager layoutManager = mRv.getLayoutManager();
                            if (layoutManager instanceof LinearLayoutManager) {
                                int firstCompletelyVisibleItem = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
                                if (firstCompletelyVisibleItem > 0) {
                                    return false;
                                }
                            }
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int curX = (int) ev.getX();
                        int curY = (int) ev.getY();
                        int absX = Math.abs(curX - mDownX);
                        int absY = Math.abs(curY - mDownY);
                        //LogUtils.e("移动的绝对值", absX + "/" + absY);

                        if (absX > absY) {
                            //左右划的时候不拦截，直接传递给子View
                            return false;
                        } else {
                            if (curY - mDownY < 0 && absY > mTouchSlop && mIsAtBottom) {
                                if (!isLoadingMore && null != mLoadingMoreListener) {
                                    //                                    mLoadingMoreListener.releaseToLoadMore();
                                }
                            } else {
                                if (!isLoadingMore && null != mLoadingMoreListener) {
                                    //                                    mLoadingMoreListener.clearUpLoadHint();
                                }
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        mLastY = (int) ev.getY();
                        if (mLastY - mDownY < 0 && Math.abs(mLastY - mDownY) > mTouchSlop && mIsAtBottom) {
                            if (!isLoadingMore && null != mLoadingMoreListener) {
                                isLoadingMore = true;
                                mLoadingMoreListener.onLoadingMore();
                            }
                        }

                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    public OnLoadingMoreListener getLoadingMoreListener() {
        return mLoadingMoreListener;
    }

    public void setLoadingMoreListener(OnLoadingMoreListener loadingMoreListener) {
        mLoadingMoreListener = loadingMoreListener;
    }

    public void setData(List<String> list) {
        mList = list;
        notifyDataSetChanged();
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

    private class FooterHolder extends RecyclerView.ViewHolder {
        private FooterRvBinding mBinding;

        public FooterHolder(View itemView) {
            super(itemView);
        }

        public void setBinding(FooterRvBinding binding) {
            mBinding = binding;
        }
    }

    public interface OnLoadingMoreListener {
        void onLoadingMore();
    }
}
