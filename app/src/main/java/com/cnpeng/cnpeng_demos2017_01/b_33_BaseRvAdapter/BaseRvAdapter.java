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
import com.cnpeng.cnpeng_demos2017_01.utils.LogUtils;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * 作者：CnPeng
 * 时间：2018/6/14
 * 功用：RecyclerView 的 Adapter 基类。
 * 其他：
 * 1、附带了上拉加载的功能处理，下拉加载通过SwipeRefreshLayout实现.
 * 2、暂时只支持 LinearLayoutManager
 * // TODO: CnPeng 2018/6/15 上午9:07 考虑下拉时头布局的封装
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

    private final int    ITEM_TYPE_HEADER = 0;
    private final int    ITEM_TYPE_FOOTER = 1;
    private final String TAG              = "BaseRvAdapter";
    private       String mLoadingStatus   = "";

    private OnLoadingMoreListener mLoadingMoreListener;
    private Context               mContext;
    private RecyclerView          mRv;
    /**
     * 是否正在执行加载更多的操作，可以避免上次请求未结束时重复发送请求的情况。内部处理之后，上拉监听中的onLoadingMore()中就不需要再做判断
     */
    private boolean               mIsLoadingMore;
    private boolean               mIsAtBottom;
    private int                   mLastY;
    private int                   mDownY;
    private int                   mDownX;
    private int                   mTouchSlop;
    private boolean               mFooterEnable;


    /**
     * 基类的构造方法，子类的构造中必须传递这两个参数给该基类
     *
     * @param context      用来激活默认脚布局、获取触摸时的最小反馈量
     * @param recyclerView 用来监听和处理触摸和滚动事件，从而触发监听器中的上拉加载事件
     */
    public BaseRvAdapter(Context context, RecyclerView recyclerView) {
        // ATTENTION CnPeng 2018/6/15 上午8:55  子类的构造方法必须在此基础上扩展,context 和 recyclerView是BaseRvAdapter必须的
        mContext = context;
        mRv = recyclerView;

        //触摸时最小的响应距离
        mTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();


        // TODO: CnPeng 2018/6/14 下午9:48 如何确保最后一条完全可见的时候才上拉加载呢？
        initRvScrollListener();
        initRvTouchListener();
    }

    /**
     * 只有在开启了脚布局之后才去触发这个状态更新的操作
     */
    public void setLoadingStatus(String loadingStatus) {
        // TODO: CnPeng 2018/6/15 上午9:22 如果允许外部配置脚布局view，还需要考虑该view是否为空
        if (mFooterEnable) {
            mLoadingStatus = loadingStatus;
            switch (mLoadingStatus) {
                case STATUS_LOADING:
                    mIsLoadingMore = true;
                    break;
                case STATUS_NO_MORE:
                case STATUS_OVER:
                case STATUS_RELEASE_TO_LOAD:
                default:
                    mIsLoadingMore = false;
                    break;
            }
            notifyItemChanged(getItemCount() - 1);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        switch (viewType) {
            case ITEM_TYPE_FOOTER:

                // TODO: CnPeng 2018/6/15 上午9:35 考虑如何由外部动态的配置脚布局view
                FooterRvBinding footerBinding = DataBindingUtil.inflate(inflater, R.layout.footer_rv, parent, false);
                View footerView = footerBinding.getRoot();
                FooterHolder footerHolder = new FooterHolder(footerView);
                footerHolder.setBinding(footerBinding);
                return footerHolder;
            default:
                return onCreateContentHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof FooterHolder) {
            updateFooterView((FooterHolder) holder);
        } else {
            onBindContentHolder(holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mFooterEnable) {
            if (position == getItemCount() - 1) {
                return ITEM_TYPE_FOOTER;
            } else {
                //此处做判断，防止子类定义的条目类型与该基类中定义的头布局和脚布局冲突
                int contentType = getContentItemType(position);
                if (ITEM_TYPE_HEADER == contentType || ITEM_TYPE_FOOTER == contentType) {
                    new Exception("BaseRvAdapter:该条目类型已经在基类中定义为头布局/脚布局，不能子类重复定义").printStackTrace();
                }
                return getContentItemType(position);
            }
        } else {
            return getContentItemType(position);
        }
    }

    @Override
    public int getItemCount() {
        //如果开启了脚布局，数量+1;否则，返回实际数量
        return mFooterEnable ? getContentCount() + 1 : getContentCount();
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
                        if (!mIsLoadingMore || null != mLoadingMoreListener) {
                            mIsLoadingMore = true;
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
                        LogUtils.i(TAG, "移动的绝对值：" + absX + "/" + absY);

                        if (absX > absY) {
                            //左右划的时候不拦截，直接传递给子View
                            return false;
                        } else {
                            if (curY - mDownY < 0 && absY > mTouchSlop && mIsAtBottom) {
                                if (!mIsLoadingMore && null != mLoadingMoreListener) {
                                    //                                    mLoadingMoreListener.releaseToLoadMore();
                                    setLoadingStatus(STATUS_RELEASE_TO_LOAD);
                                }
                            } else {
                                if (!mIsLoadingMore && null != mLoadingMoreListener) {
                                    //                                    mLoadingMoreListener.clearUpLoadHint();
                                }
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        mLastY = (int) ev.getY();
                        if (mLastY - mDownY < 0 && Math.abs(mLastY - mDownY) > mTouchSlop && mIsAtBottom) {
                            if (!mIsLoadingMore && null != mLoadingMoreListener) {
                                mIsLoadingMore = true;
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

    /**
     * 是否允许展示脚布局。
     *
     * @param enable true 展示脚布局，false 不允许展示脚布局
     */
    public void enableFooterView(boolean enable) {
        mFooterEnable = enable;
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
