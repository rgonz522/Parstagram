package fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.parstagram.EndlessRecyclerViewScrollListener;
import com.example.parstagram.adapters.GridPostsAdapter;
import com.example.parstagram.LogInActivity;

import com.example.parstagram.models.Post;
import com.example.parstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    public static final String KEY_PROFILE_IMG = "profile_pic";



    protected RecyclerView rvposts;
    protected GridPostsAdapter adapter;
    protected List<Post> allposts;
    protected EndlessRecyclerViewScrollListener scrollListener;
    protected SwipeRefreshLayout swipeContainer;



    private ParseUser profileUser;
    private ImageView ivProfilePic;
    private TextView tvUserName;
    private Button btnLogOut;
    private boolean currentUser;



    public ProfileFragment()
    {

    }

    public ProfileFragment(ParseUser profileUser, boolean currentUser) {
        this.profileUser = profileUser;
        this.currentUser = currentUser;
    }

    public static ProfileFragment newInstance(ParseUser profileUser, boolean currentUser) {
        ProfileFragment fragment = new ProfileFragment(profileUser, currentUser);
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public ProfileFragment(boolean currentUser) {

        profileUser = ParseUser.getCurrentUser();
        this.currentUser = currentUser;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);


    }

    protected void queryPosts(int page) {



        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, profileUser);
        query.setLimit(PostsFragment.POSTS_PER_PAGE);
        query.setSkip(page * PostsFragment.POSTS_PER_PAGE);
        query.addDescendingOrder(Post.KEY_CREATEDAT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issues getting posts", e);
                    return;
                }
                allposts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        allposts = new ArrayList<>();

        rvposts = view.findViewById(R.id.rvPosts);
        adapter = new GridPostsAdapter(getContext(), allposts);

        rvposts.setAdapter(adapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);

        rvposts.setLayoutManager(layoutManager);



        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
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
                allposts.clear();
                queryPosts(PostsFragment.TOP_OF_PAGE);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        queryPosts(PostsFragment.TOP_OF_PAGE);
        //so on original loading of the post fragment
        //home page can be populated


        tvUserName = view.findViewById(R.id.tvUsername);
        ivProfilePic = view.findViewById(R.id.ivProfilePic);
        btnLogOut = view.findViewById(R.id.btnLogOut);


        tvUserName.setText(profileUser.getUsername());

        ParseFile profile_pic = profileUser.getParseFile(KEY_PROFILE_IMG);


        if (profile_pic != null) {
            String img_url = profile_pic.getUrl();

            if (img_url != null) {

                Glide.with(getContext()).load(img_url).circleCrop().into(ivProfilePic);
            }

        }
        if (currentUser) {
            btnLogOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ParseUser.logOut();
                    getActivity().finish();
                    Intent login_intent = new Intent(getContext(), LogInActivity.class);
                    startActivity(login_intent);
                }
            });

            ivProfilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startUserProfileFragment();
                }
            });
        } else
            {
              btnLogOut.setVisibility(View.INVISIBLE);

        }



    }



    public void startUserProfileFragment(){

        ChangeUserPicFragment changeUserPicFragment = new ChangeUserPicFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContainer,changeUserPicFragment);
        fragmentTransaction.commit();

    }
}
