package fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.parstagram.EndlessRecyclerViewScrollListener;
import com.example.parstagram.Post;
import com.example.parstagram.PostsAdapter;
import com.example.parstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class PostsFragment extends Fragment {

    private static final String TAG = "PostsFragment";
    public static final int POSTS_PER_PAGE = 10;
    public static final int TOP_OF_PAGE = 0;

    protected RecyclerView rvposts;
    protected PostsAdapter adapter;
    protected List<Post> allposts;
    protected EndlessRecyclerViewScrollListener scrollListener;
    protected SwipeRefreshLayout swipeContainer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        allposts = new ArrayList<>();

        rvposts = view.findViewById(R.id.rvPosts);
        adapter = new PostsAdapter(getContext(), allposts);

        rvposts.setAdapter(adapter);

        //Linear to keep posts tightly wrapped together.
        LinearLayoutManager linearLayoutman = new LinearLayoutManager(getContext());

        rvposts.setLayoutManager(linearLayoutman);


        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutman) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                //starting from page
                queryPosts(page);
            }
        };
        rvposts.addOnScrollListener(scrollListener);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                queryPosts(TOP_OF_PAGE);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        queryPosts(TOP_OF_PAGE);
        //so on original loading of the post fragment
        //home page can be populated
    }


    protected void queryPosts(int page) {


        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setSkip(page * POSTS_PER_PAGE);
        query.setLimit(POSTS_PER_PAGE);
        query.addDescendingOrder(Post.KEY_CREATEDAT);

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issues getting posts", e);
                } else if (posts != null) {


                    allposts.addAll(posts);
                    adapter.notifyDataSetChanged();
                }

            }
        });


    }
}