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

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * 作者：CnPeng
 * 时间：2018/6/14
 * 功用：
 * 其他：
 */
public abstract class BaseRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /**
     * 正在加载
     */
    public final String STATUS_LOADING         = "loadingMore";
    /**
     * 加载完毕——没有更多
     */
    public final String STATUS_NO_MORE         = "noMore";
    /**
     * 加载完毕——有新的数据
     */
    public final String STATUS_OVER            = "loadingOver";
    /**
     * 释放加载更多，当最后一条完整可见条目为脚布局，并且用户还在下拉的时候触发
     */
    public final String STATUS_RELEASE_TO_LOAD = "releaseToLoad";


    private final int ITEM_TYPE_HEADER = 0;
    private final int ITEM_TYPE_FOOTER = 1;
    //    private final int ITEM_TYPE_CONTENT = 2;

    private RecyclerView          mRv;
    private boolean               isLoadingMore;
    //    private List<String>          mList;
    private Context               mContext;
    private boolean               mIsAtBottom;
    private int                   mLastY;
    private int                   mDownY;
    private int                   mDownX;
    private OnLoadingMoreListener mLoadingMoreListener;
    private int                   mTouchSlop;
    private String mLoadingStatus = "";


    public BaseRvAdapter(Context context, RecyclerView recyclerView) {
        // ATTENTION CnPeng 2018/6/15 上午8:55  子类的构造方法必须在此基础上扩展,context 和 recyclerView是BaseRvAdapter必须的
        //    public BaseRvAdapter(Context context, List<String> list, RecyclerView recyclerView) {
        mContext = context;
        //        mList = list;
        mRv = recyclerView;

        //触摸时最小的响应距离
        mTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();


        // TODO: CnPeng 2018/6/14 下午9:48 如何确保最后一条完全可见的时候才上拉加载呢？
        initRvScrollListener();
        initRvTouchListener();
    }

    public void setLoadingStatus(String loadingStatus) {
        mLoadingStatus = loadingStatus;
        notifyItemChanged(getItemCount() - 1);
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
            //            case ITEM_TYPE_CONTENT:
            default:
                //    ItemRvBinding itemBinding = DataBindingUtil.inflate(inflater, R.layout.item_rv, parent, false);
                //    View itemView = itemBinding.getRoot();
                //
                //    ContentHolder contentHolder = new ContentHolder(itemView);
                //    contentHolder.setBinding(itemBinding);

                return onCreateContentHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        // TODO: CnPeng 2018/6/14 下午12:06 这个三个position 和 position有什么区别呢？
        //        int realPosition = holder.getAdapterPosition();
        //        int realPosition2 = holder.getLayoutPosition();
        //        int oldPosition = holder.getOldPosition();
        //
        //        if (holder instanceof ContentHolder) {
        //            String str = mList.get(position);
        //            ((ContentHolder) holder).mBinding.tv.setText(str);
        //        } else {
        //            updateFooterView((FooterHolder) holder);
        //        }

        if (holder instanceof FooterHolder) {
            updateFooterView((FooterHolder) holder);
        } else {
            onBindContentHolder(holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return ITEM_TYPE_FOOTER;
        } else {
            //            return ITEM_TYPE_CONTENT;
            return getContentItemType(position);
        }
        //        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        //+1 是脚布局
        //        return mList.size() + 1;
        return getContentCount() + 1;
    }

    /**
     * 获取内容的数量
     */
    abstract int getContentCount();

    /**
     * 获取条目类型：0 和 1 已经被头布局和脚布局占用，不用重复定义
     */
    abstract int getContentItemType(int position);

    /**
     * 绑定内容视图
     */
    abstract void onBindContentHolder(RecyclerView.ViewHolder holder, int position);

    /**
     * 创建内容hodler
     */
    abstract RecyclerView.ViewHolder onCreateContentHolder(ViewGroup parent, int viewType);

    private void updateFooterView(FooterHolder holder) {
        switch (mLoadingStatus) {
            case STATUS_LOADING:
                //正在加载——展示进度和文本
                holder.mBinding.progressBar.setVisibility(View.VISIBLE);
                holder.mBinding.tvLoadingHint.setVisibility(View.VISIBLE);
                holder.mBinding.tvLoadingHint.setText("正在加载。。。");
                break;
            case STATUS_NO_MORE:
                //加载完毕——没有更多
                holder.mBinding.progressBar.setVisibility(View.GONE);
                holder.mBinding.tvLoadingHint.setVisibility(View.VISIBLE);
                holder.mBinding.tvLoadingHint.setText("没有更多数据了。。。");
                break;
            case STATUS_RELEASE_TO_LOAD:
                //加载完毕——还有更多数据
                holder.mBinding.progressBar.setVisibility(View.GONE);
                holder.mBinding.tvLoadingHint.setVisibility(View.VISIBLE);
                holder.mBinding.tvLoadingHint.setText("释放后开始加载。。。");
                break;
            case STATUS_OVER:
            default:
                //加载完毕——还有更多数据
                holder.mBinding.progressBar.setVisibility(View.GONE);
                holder.mBinding.tvLoadingHint.setVisibility(View.VISIBLE);
                holder.mBinding.tvLoadingHint.setText("上拉加载更多。。。");
                break;
        }
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
                    //                    lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
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
                                    setLoadingStatus(STATUS_RELEASE_TO_LOAD);
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
