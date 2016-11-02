package midian.baselib.widget.pulltorefresh;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.ListView;

import com.midian.baselib.R;

import midian.baselib.widget.astickyheader.PinnedSectionListView;


/**
 * 这个类实现了ListView下拉刷新，上加载更多和滑到底部自动加载
 * 
 * @author Li Hong
 * @since 2013-8-15
 */
public class PullToRefreshSectionListView extends PullToRefreshBase<ListView> {

	/** ListView */
	private PinnedSectionListView mListView;
	/** 滚动的监听器 */
	private OnScrollListener mScrollListener;

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            context
	 */
	public PullToRefreshSectionListView(Context context) {
		this(context, null);
	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            context
	 * @param attrs
	 *            attrs
	 */
	public PullToRefreshSectionListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            context
	 * @param attrs
	 *            attrs
	 * @param defStyle
	 *            defStyle
	 */
	public PullToRefreshSectionListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		setPullLoadEnabled(false);
	}

	@Override
	protected PinnedSectionListView createRefreshableView(Context context, AttributeSet attrs) {
		PinnedSectionListView listView = new PinnedSectionListView(context, attrs);
		mListView = listView;
		mListView.setScrollBarStyle(SCROLLBARS_OUTSIDE_OVERLAY);
		mListView.setSelector(android.R.color.transparent);
		mListView.setDivider(getResources().getDrawable(android.R.color.transparent));
		mListView.setDividerHeight(0);
		mListView.setHeaderDividersEnabled(false);
		mListView.setFooterDividersEnabled(false);
		mListView.setShadowVisible(false);
		return listView;
	}

	/**
	 * 设置滑动的监听器
	 * 
	 * @param l
	 *            监听器
	 */
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}

	@Override
	protected boolean isReadyForPullUp() {
		return isLastItemVisible();
	}

	@Override
	protected boolean isReadyForPullDown() {
		return isFirstItemVisible();
	}

	/**
	 * 判断第一个child是否完全显示出来
	 * 
	 * @return true完全显示出来，否则false
	 */
	private boolean isFirstItemVisible() {
		final Adapter adapter = mListView.getAdapter();

		if (null == adapter || adapter.isEmpty()) {
			return true;
		}

		int mostTop = (mListView.getChildCount() > 0) ? mListView.getChildAt(0).getTop() : 0;
		if (mostTop >= 0) {
			return true;
		}

		return false;
	}

	/**
	 * 判断最后一个child是否完全显示出来
	 * 
	 * @return true完全显示出来，否则false
	 */
	private boolean isLastItemVisible() {
		final Adapter adapter = mListView.getAdapter();

		if (null == adapter || adapter.isEmpty()) {
			return true;
		}

		final int lastItemPosition = adapter.getCount() - 1;
		final int lastVisiblePosition = mListView.getLastVisiblePosition();

		/**
		 * This check should really just be: lastVisiblePosition ==
		 * lastItemPosition, but ListView internally uses a FooterView which
		 * messes the positions up. For me we'll just subtract one to account
		 * for it and rely on the inner condition which checks getBottom().
		 */
		if (lastVisiblePosition >= lastItemPosition - 1) {
			final int childIndex = lastVisiblePosition - mListView.getFirstVisiblePosition();
			final int childCount = mListView.getChildCount();
			final int index = Math.min(childIndex, childCount - 1);
			final View lastVisibleChild = mListView.getChildAt(index);
			if (lastVisibleChild != null) {
				return lastVisibleChild.getBottom() <= mListView.getBottom();
			}
		}

		return false;
	}

	public void setMessage(Message msg) {
		mListView.setTag(R.id.listview_msg, msg);
	}
}
