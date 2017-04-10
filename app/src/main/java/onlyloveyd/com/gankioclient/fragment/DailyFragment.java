package onlyloveyd.com.gankioclient.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import onlyloveyd.com.gankioclient.R;
import onlyloveyd.com.gankioclient.adapter.MultiRecyclerAdapter;
import onlyloveyd.com.gankioclient.decorate.Visitable;
import onlyloveyd.com.gankioclient.gsonbean.DailyBean;
import onlyloveyd.com.gankioclient.http.HttpMethods;
import rx.Subscriber;
import rx.exceptions.OnErrorFailedException;

/**
 * Copyright 2017 yidong
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class DailyFragment extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate {
    @BindView(R.id.rv_content)
    RecyclerView rvContent;
    @BindView(R.id.rl_gank_refresh)
    BGARefreshLayout bgaRefreshLayout;

    MultiRecyclerAdapter mMultiRecyclerAdapter;
    List<Visitable> mVisitableList = new ArrayList<>();

    LinearLayoutManager llm;

    public static DailyFragment newInstance() {
        Bundle args = new Bundle();
        DailyFragment fragment = new DailyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gank, container, false);
        ButterKnife.bind(this, view);
        initBGALayout();
        initRvContent();
        return view;
    }

    private void initBGALayout() {
        // 为BGARefreshLayout 设置代理
        bgaRefreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能

        BGANormalRefreshViewHolder refreshViewHolder =
                new BGANormalRefreshViewHolder(getContext(), true);
        refreshViewHolder.setLoadingMoreText("加载更多");
        refreshViewHolder.setLoadMoreBackgroundColorRes(R.color.white);
        refreshViewHolder.setRefreshViewBackgroundColorRes(R.color.white);
        bgaRefreshLayout.setRefreshViewHolder(refreshViewHolder);
    }

    private void initRvContent() {
        llm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mMultiRecyclerAdapter = new MultiRecyclerAdapter(null);
        rvContent.setLayoutManager(llm);
        rvContent.setAdapter(mMultiRecyclerAdapter);
        bgaRefreshLayout.beginRefreshing();
        Date date = new Date(System.currentTimeMillis());
        getDaily(date.getYear() + 1900, date.getMonth() + 1, date.getDate());
    }

    private void getDaily(int year, int month, int day) {
        Subscriber subscriber = new Subscriber<DailyBean>() {
            @Override
            public void onCompleted() {
                if (bgaRefreshLayout.isLoadingMore()) {
                    bgaRefreshLayout.endLoadingMore();
                } else {
                    bgaRefreshLayout.endRefreshing();
                }
            }

            @Override
            public void onError(Throwable e) {
                try {
                    Snackbar.make(rvContent, "网络请求错误", Snackbar.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (OnErrorFailedException errorFailedException) {
                    e.printStackTrace();
                }
                if (bgaRefreshLayout.isLoadingMore()) {
                    bgaRefreshLayout.endLoadingMore();
                } else {
                    bgaRefreshLayout.endRefreshing();
                }
            }

            @Override
            public void onNext(DailyBean dailyBean) {
                if (bgaRefreshLayout.isLoadingMore()) {
                } else {
                    mVisitableList.clear();
                }

                if (dailyBean.getResults().getAndroid() != null) {
                    mVisitableList.addAll(dailyBean.getResults().getAndroid());
                }
                if (dailyBean.getResults().getApp() != null) {
                    mVisitableList.addAll(dailyBean.getResults().getApp());
                }
                if (dailyBean.getResults().getBonus() != null) {
                    mVisitableList.addAll(dailyBean.getResults().getBonus());
                }
                if (dailyBean.getResults().getIOS() != null) {
                    mVisitableList.addAll(dailyBean.getResults().getIOS());
                }
                if (dailyBean.getResults().getJs() != null) {
                    mVisitableList.addAll(dailyBean.getResults().getJs());
                }
                if (dailyBean.getResults().getRec() != null) {
                    mVisitableList.addAll(dailyBean.getResults().getRec());
                }
                if (dailyBean.getResults().getRes() != null) {
                    mVisitableList.addAll(dailyBean.getResults().getRes());
                }
                if (dailyBean.getResults().getVideo() != null) {
                    mVisitableList.addAll(dailyBean.getResults().getVideo());
                }
                mMultiRecyclerAdapter.setData(mVisitableList);
            }
        };
        HttpMethods.getInstance().getDailyData(subscriber, year, month, day);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        Date date = new Date(System.currentTimeMillis());
        getDaily(date.getYear() + 1900, date.getMonth() + 1, date.getDate());
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
