package com.brookmanholmes.bma.data;

import com.brookmanholmes.billiards.game.BallStatus;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.Player;
import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TurnEnd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Brookman Holmes on 2/20/2017.
 */

public class FireDataSource implements DataSource {
    private static final String TAG = "FireDataSource";
    private static final String MATCHES = "matches";
    private static final String USERS = "users";
    private static final String TURNS = "turns";

    private static final TurnEnd[] turnEnds = TurnEnd.values();
    private static final BallStatus[] ballStatus = BallStatus.values();
    private static final PlayerTurn[] playerTurns = PlayerTurn.values();
    private static final GameType[] gameTypes = GameType.values();
    private static final AdvStats.HowType[] howTypes = AdvStats.HowType.values();
    private static final AdvStats.WhyType[] whyTypes = AdvStats.WhyType.values();
    private static final AdvStats.Angle[] angles = AdvStats.Angle.values();

    private DatabaseReference database;

    public FireDataSource() {
        database = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public List<Player> getPlayers() {
        return new ArrayList<>();
    }

    @Override
    public List<Player> getPlayer(String userId, GameType gameType, String id) {
        return new ArrayList<>();
    }

    @Override
    public void undoTurn(final Match match) {
        if (match.isUndoTurn()) {
            database.child("turns")
                    .child(match.getMatchId())
                    .orderByKey()
                    .limitToLast(1)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String key = snapshot.getKey();
                                Map<String, Object> map = new HashMap<>();
                                map.put("/users/" + match.getPlayer().getId() + "/turns/" + key, null);
                                map.put("/users/" + match.getOpponent().getId() + "/turns/" + key, null);
                                map.put("/turns/" + match.getMatchId() + "/" + key, null);
                                database.updateChildren(map);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    @Override
    public String insertMatch(Match match) {
        String matchId = database.child("matches").push().getKey();
        MatchModel model = new MatchModel(match, matchId);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/matches/" + matchId, model);

        childUpdates.put("/users/" + match.getPlayer().getId() + "/matches/" + matchId, match.getOpponent().getId());
        childUpdates.put("/users/" + match.getOpponent().getId() + "/matches/" + matchId, match.getPlayer().getId());

        database.updateChildren(childUpdates);
        match.setMatchId(matchId);
        return matchId;
    }

    @Override
    public void updateMatchNotes(String matchNotes, String matchId) {
        database.child("matches")
                .child(matchId)
                .child("notes")
                .setValue(matchNotes);
    }

    @Override
    public void updateMatchLocation(String location, String matchId) {
        database.child("matches")
                .child(matchId)
                .child("location")
                .setValue(location);
    }

    @Override
    public void insertTurn(ITurn turn, String matchId, String playerId) {
        TurnModel model = new TurnModel(turn);
        String turnId = database.child("turns").child(matchId).push().getKey();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/turns/" + matchId + "/" + turnId + "/", model);
        childUpdates.put("/users/" + playerId + "/turns/" + turnId, matchId);

        database.updateChildren(childUpdates);
    }

    @Override
    public void deleteMatch(final Match match, final String deletingUser) {
        Map<String, Object> childUpdates = new HashMap<>();

        // remove matches from player's profile
        childUpdates.put("/users/" + deletingUser + "/matches/" + match.getMatchId(), null);
        database.updateChildren(childUpdates);

        // remove turns for player
        database.child("users")
                .child(deletingUser)
                .child("turns")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put("/users/" + deletingUser + "/turns/" + snapshot.getKey(), null);
                            database.updateChildren(childUpdates);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    /**
     * Updates the currently signed in user in the database
     */
    public void updateUser() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            database.child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String imageUrl = null;
                    for (UserInfo info : user.getProviderData()) {
                        if (info.getProviderId().equals("google.com")) {
                            if (info.getPhotoUrl() != null)
                                imageUrl = info.getPhotoUrl().toString();
                        }
                    }

                    Map<String, Object> childUpdates = new HashMap<>();
                    String path = "/users/" + user.getUid() + "/";
                    childUpdates.put(path + "name", user.getDisplayName());
                    childUpdates.put(path + "email", user.getEmail());
                    childUpdates.put(path + "id", user.getUid());
                    childUpdates.put(path + "imageUrl", imageUrl);

                    database.updateChildren(childUpdates);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
