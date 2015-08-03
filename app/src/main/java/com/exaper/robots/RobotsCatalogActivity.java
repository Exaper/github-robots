package com.exaper.robots;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.exaper.robots.app.RobotsAdapter;
import com.exaper.robots.data.LoginToRobotImageConverter;
import com.exaper.robots.data.api.RobotsDataService;
import com.exaper.robots.data.model.Robot;
import com.exaper.robots.data.model.RobotsResponse;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import javax.inject.Inject;

public class RobotsCatalogActivity extends AppCompatActivity implements Callback<RobotsResponse>,
        RobotsAdapter.OnItemClickListener {
    private RobotsAdapter mRobotsAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mPullToRefreshText;
    @Inject
    protected RobotsDataService mDataService;
    @Inject
    protected LoginToRobotImageConverter mLoginToRobotImageConverter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RobotsApplication.get(this).getComponent().inject(this);
        setContentView(R.layout.activity_robots_catalog);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        mPullToRefreshText = (TextView) findViewById(R.id.pullToRefreshText);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mDataService.getRobots(RobotsCatalogActivity.this);
                mPullToRefreshText.setVisibility(View.INVISIBLE);
            }
        });
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // Responsive UI: Landscape vs portrait vs tablet.
        int columns = getResources().getInteger(R.integer.catalogGridColumnsCount);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(columns, StaggeredGridLayoutManager.VERTICAL) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return true;
            }
        });
        mRobotsAdapter = new RobotsAdapter(this, mLoginToRobotImageConverter);
        mRobotsAdapter.setItemClickListener(this);
        recyclerView.setAdapter(mRobotsAdapter);
        // Swipe refresh needs to measure itself first.
        mSwipeRefreshLayout.post(new Runnable() {
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        mPullToRefreshText.setVisibility(View.INVISIBLE);
        mDataService.getRobots(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.robots_catalog, menu);
        final MenuItem searchMenuItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setSubmitButtonEnabled(false);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mRobotsAdapter.filter(null);
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mRobotsAdapter.filter(newText);
                return true;
            }
        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean queryTextFocused) {
                if (!queryTextFocused) {
                    searchMenuItem.collapseActionView();
                    searchView.setQuery("", false);
                }
            }
        });
        return true;
    }

    @Override
    public void onItemClick(View container, Robot robot) {
        Intent viewRobotDetailsIntent = new Intent(this, RobotDetailsActivity.class);
        viewRobotDetailsIntent.putExtra(RobotDetailsActivity.EXTRA_ROBOT, robot);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, container.findViewById(R.id.robotImage), "hero_robot");
        ActivityCompat.startActivity(this, viewRobotDetailsIntent, options.toBundle());
    }

    @Override
    public void success(RobotsResponse robots, Response response) {
        mRobotsAdapter.setItems(robots.items);
        mSwipeRefreshLayout.setRefreshing(false);
        mPullToRefreshText.setVisibility(robots.items.isEmpty() ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void failure(RetrofitError error) {
        Snackbar.make(mSwipeRefreshLayout, error.getLocalizedMessage(), Snackbar.LENGTH_LONG)
                .setAction(R.string.try_again, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDataService.getRobots(RobotsCatalogActivity.this);
                        mSwipeRefreshLayout.setRefreshing(true);
                    }
                })
                .show();
        mSwipeRefreshLayout.setRefreshing(false);
        mPullToRefreshText.setVisibility(mRobotsAdapter.getItemCount() == 0 ? View.VISIBLE : View.INVISIBLE);
    }
}
