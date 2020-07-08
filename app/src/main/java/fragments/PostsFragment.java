package fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.parstagram.EndlessRecyclerViewScrollListener;
import com.example.parstagram.Post;
import com.example.parstagram.PostsAdapter;
import com.example.parstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class PostsFragment extends Fragment {

    private static final String TAG = "PostsFragment" ;
    protected RecyclerView rvposts;
    protected PostsAdapter adapter;
    protected List<Post> allposts;
    private EndlessRecyclerViewScrollListener scrollListener;


    public static PostsFragment newInstance(String param1, String param2) {
        PostsFragment fragment = new PostsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public PostsFragment() {
        // Required empty public constructor
    }

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        allposts = new ArrayList<>();

        rvposts = view.findViewById(R.id.rvPosts);
        adapter = new PostsAdapter(getContext(), allposts);

        rvposts.setAdapter(adapter);

        LinearLayoutManager linearLayoutman = new LinearLayoutManager(getContext());

        rvposts.setLayoutManager(linearLayoutman);


        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutman) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                queryPosts();
            }
        };
        // Adds the scroll listener to RecyclerView
        rvposts.addOnScrollListener(scrollListener);


        queryPosts();

    }



    protected void queryPosts()
    {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATEDAT);
        query.findInBackground(new FindCallback<Post>()
        {
            @Override
            public void done(List<Post> posts, ParseException e)
            {
                if(e != null)
                {
                    Log.e(TAG, "Issues getting posts", e );
                }
                for (Post post: posts)
                {
                    Log.i(TAG, "post: " + post.getDescription() + " " + post.getUser().getUsername());
                }
                allposts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }
}