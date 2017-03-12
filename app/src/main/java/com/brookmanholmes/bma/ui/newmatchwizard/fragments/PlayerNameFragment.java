/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.brookmanholmes.bma.ui.newmatchwizard.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.data.UserModel;
import com.brookmanholmes.bma.ui.newmatchwizard.model.PlayerNamePage;
import com.brookmanholmes.bma.ui.profile.PlayerProfileActivity;
import com.brookmanholmes.bma.ui.view.BaseViewHolder;
import com.brookmanholmes.bma.wizard.model.Page;
import com.brookmanholmes.bma.wizard.ui.BasePageFragment;
import com.github.pavlospt.roundedletterview.RoundedLetterView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.brookmanholmes.bma.wizard.model.Page.SIMPLE_DATA_KEY;

public class PlayerNameFragment extends BasePageFragment<PlayerNamePage> {
    private static final String TAG = "PlayerNameFragment";
    private static final String ARG_KEY = "key";
    private static final String ARG_PLAYER_NAME = PlayerProfileActivity.ARG_ACCOUNT_ID;

    @Bind(R.id.playerName)
    TextView playerName;
    @Bind(R.id.opponentName)
    MaterialAutoCompleteTextView opponentName;
    @Bind(R.id.scrollView)
    RecyclerView recyclerView;

    UserRecyclerAdapter adapter;
    List<UserModel> users = new ArrayList<>();
    List<UserModel> recentUsers = new ArrayList<>();

    public PlayerNameFragment() {
    }

    public static PlayerNameFragment create(String key, @Nullable String playerName) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        if (playerName != null) {
            args.putString(ARG_PLAYER_NAME, playerName);
        }

        PlayerNameFragment fragment = new PlayerNameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new UserRecyclerAdapter(new UserModel(getString(R.string.single_player)));
        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("matches")
                .orderByKey()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("users")
                                    .child(snapshot.getValue(String.class))
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            UserModel model = dataSnapshot.getValue(UserModel.class);
                                            Log.i(TAG, "onDataChange: " + model);
                                            if (model.id != null && !recentUsers.contains(model)) {
                                                recentUsers.add(model);
                                                setRecentUsers();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .orderByKey()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            UserModel model = snapshot.getValue(UserModel.class);
                            if (model.id != null)
                                users.add(model);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        page = (PlayerNamePage) callbacks.onGetPage(key);
        adapter.setPage(page);
        adapter.selectedPosition = savedInstanceState == null ? -1 : savedInstanceState.getInt("selected_item", -1);
        View rootView = inflater.inflate(R.layout.fragment_player_names, container, false);
        ButterKnife.bind(this, rootView);

        ((TextView) rootView.findViewById(android.R.id.title)).setText(page.getTitle());

        opponentName.setText(page.getData().getString(PlayerNamePage.OPPONENT_NAME_KEY, ""));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        if (adapter.users.isEmpty())
            adapter.setRecentUsers(recentUsers);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        opponentName.addTextChangedListener(textWatcher(PlayerNamePage.OPPONENT_ID_KEY));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selected_item", adapter.selectedPosition);
    }

    private TextWatcher textWatcher(final String key) {
        return new TextWatcher() {
            Handler handler = new Handler(Looper.getMainLooper());
            Runnable workRunnable;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(final Editable editable) {
                // delay this until .5 seconds after the user finishes typing to cut down on updates
                handler.removeCallbacks(workRunnable);
                workRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if (TextUtils.isEmpty(editable.toString()))
                            setRecentUsers();
                        else
                            adapter.setUsers(filterUsers(editable.toString()));

                        adapter.updatePage(adapter.selectedPosition);
                    }
                };

                handler.postDelayed(workRunnable, 500);
            }
        };
    }

    private void setRecentUsers() {
        Collections.sort(recentUsers);
        List<UserModel> temp = new ArrayList<>(recentUsers);
        adapter.setRecentUsers(temp);
    }

    public List<UserModel> filterUsers(String name) {
        Collections.sort(this.users); // sort the collection first

        List<UserModel> users = new ArrayList<>();
        // add the second option if the user has started typing a name
        if (!TextUtils.isEmpty(name))
            users.add(0, new UserModel(name));

        // add the rest of the users if they match
        for (UserModel user : this.users) {
            if (user.name.contains(name)
                    && !FirebaseAuth.getInstance().getCurrentUser().getUid().equals(user.id))
                users.add(user);
        }

        return users;
    }

    private static class UserRecyclerAdapter extends RecyclerView.Adapter<UserViewHolder> {
        final int[] colors = new int[]{Color.parseColor("#f44336"), Color.parseColor("#9C27B0"),
                Color.parseColor("#3F51B5"), Color.parseColor("#2196F3"), Color.parseColor("#00BCD4"),
                Color.parseColor("#4CAF50"), Color.parseColor("#CDDC39"), Color.parseColor("#FF9800"),
                Color.parseColor("#FF5722"), Color.parseColor("#795548"), Color.parseColor("#9E9E9E"),
                Color.parseColor("#607D8B")};
        List<UserModel> users = new ArrayList<>();
        private UserModel ghost;
        private Page page;
        private boolean emptyList = true;
        private int selectedPosition = -1;

        public UserRecyclerAdapter(UserModel ghost) {
            this.ghost = ghost;
            users.add(ghost);
        }

        public void setPage(Page page) {
            this.page = page;
        }

        @Override
        public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user, parent, false);

            return new UserViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final UserViewHolder holder, int position) {
            holder.bind(users.get(position));
            holder.setSelected(selectedPosition == position);
            holder.icon.setBackgroundColor(colors[position % colors.length]);

            if (position == 0) {
                holder.setTitle(R.string.single_player);
            } else if (position == 1) {
                if (emptyList) {
                    holder.setTitle(R.string.recent_players);
                } else {
                    holder.setTitle(R.string.add_guest_user);
                    holder.icon.setTitleText("+");
                }
            } else if (position == 2 && !emptyList)
                holder.setTitle(R.string.add_bma_user);

            holder.itemView
                    .findViewById(R.id.container)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            notifyItemChanged(selectedPosition);
                            holder.setSelected(true);
                            selectedPosition = holder.getAdapterPosition();
                            notifyItemChanged(holder.getAdapterPosition());
                            updatePage(holder.getAdapterPosition());
                        }
                    });
        }

        public void updatePage(int position) {
            if (page != null) {
                if (position != -1) {
                    String id, name;
                    if (position == 0) {
                        id = ghost.name;
                        name = ghost.name;
                    } else {
                        UserModel user = users.get(position);
                        id = user.id == null ? user.name : user.id;
                        name = user.name;
                    }

                    page.getData().putString(PlayerNamePage.OPPONENT_ID_KEY, id);
                    page.getData().putString(PlayerNamePage.OPPONENT_NAME_KEY, name);
                    page.getData().putString(SIMPLE_DATA_KEY, Boolean.toString(position == 0));
                    page.notifyDataChanged();
                }
            }
        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        public void setUsers(List<UserModel> users) {
            DiffUtil.DiffResult diffResult =
                    DiffUtil.calculateDiff(new UserDiffUtil(users, this.users, ghost, emptyList, false));
            this.users.clear();
            this.users.addAll(users);
            emptyList = false;
            resetSelection(this.users.size() + 1 < selectedPosition);
            diffResult.dispatchUpdatesTo(this);
        }

        public void setRecentUsers(List<UserModel> users) {
            DiffUtil.DiffResult diffResult =
                    DiffUtil.calculateDiff(new UserDiffUtil(users, this.users, ghost, emptyList, true));
            this.users.clear();
            this.users.addAll(users);
            emptyList = true;
            resetSelection(this.users.size() + 1 < selectedPosition);
            diffResult.dispatchUpdatesTo(this);
        }

        private void resetSelection(boolean reset) {
            if (reset)
                selectedPosition = 0;
        }
    }

    static class UserViewHolder extends BaseViewHolder {
        @Bind(R.id.playerIndicator)
        RoundedLetterView icon;
        @Bind(R.id.playerImage)
        RoundedImageView playerImage;
        @Bind(R.id.playerName)
        TextView name;
        @Bind(R.id.title)
        TextView title;

        UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(UserModel user) {
            title.setVisibility(View.GONE);
            icon.setTitleText(user.name.substring(0, 1));
            name.setText(user.name);
            if (user.imageUrl != null)
                Picasso.with(itemView.getContext())
                        .load(user.imageUrl)
                        .into(playerImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                icon.setVisibility(View.GONE);
                                playerImage.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onError() {
                                icon.setVisibility(View.VISIBLE);
                                playerImage.setVisibility(View.GONE);
                            }
                        });
            else {
                icon.setVisibility(View.VISIBLE);
                playerImage.setVisibility(View.GONE);
            }
        }

        public void setTitle(@StringRes int title) {
            this.title.setVisibility(View.VISIBLE);
            this.title.setText(title);
        }

        public void setSelected(boolean selected) {
            if (selected)
                itemView.findViewById(R.id.container).setBackgroundColor(getColor(R.color.colorPrimarySuperTransparent));
            else
                itemView.findViewById(R.id.container).setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private static class UserDiffUtil extends DiffUtil.Callback {
        List<UserModel> newList;
        List<UserModel> oldList;
        boolean oldEmptyList, newEmptyList;

        public UserDiffUtil(List<UserModel> newList, List<UserModel> oldList, UserModel ghost,
                            boolean oldEmptyList, boolean newEmptyList) {
            this.newList = newList;
            this.newList.add(0, ghost);
            this.oldList = oldList;
            this.oldEmptyList = oldEmptyList;
            this.newEmptyList = newEmptyList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            UserModel oldUser = oldList.get(oldItemPosition);
            UserModel newUser = newList.get(newItemPosition);

            return oldUser.id.equals(newUser.id) ||
                    (oldUser.name.equals(oldUser.id) && newUser.name.equals(newUser.id)
                            && oldUser.name.substring(0, 1).equals(newUser.name.substring(0, 1)));
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return false;
        }
    }
}
