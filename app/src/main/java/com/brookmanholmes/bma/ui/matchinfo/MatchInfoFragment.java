package com.brookmanholmes.bma.ui.matchinfo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.data.DatabaseAdapter;
import com.brookmanholmes.bma.databinding.FragmentMatchInfoBinding;
import com.brookmanholmes.bma.ui.BaseActivity;
import com.brookmanholmes.bma.ui.BaseFragment;

/**
 * Created by Brookman Holmes on 9/21/2016.
 */
public class MatchInfoFragment extends BaseFragment implements MatchInfoActivity.UpdateMatchInfo {
    private static final String TAG = "MatchInfoFragment";

    MatchOverviewBinder overview;
    ShootingBinder shooting;
    SafetiesBinder safeties;
    BreaksBinder breaks;
    RunsBinder runs;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MatchInfoFragment() {
    }


    public static MatchInfoFragment create(long matchId) {
        MatchInfoFragment fragment = new MatchInfoFragment();

        Bundle args = new Bundle();
        args.putLong(BaseActivity.ARG_MATCH_ID, matchId);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MatchInfoActivity) getActivity()).registerFragment(this);
    }

    @Override
    public void onDetach() {
        ((MatchInfoActivity) getActivity()).removeFragment(this);
        super.onDetach();
    }

    public void update(Match match) {
        overview.update(match.getPlayer(), match.getOpponent());
        shooting.update(match.getPlayer(), match.getOpponent());
        safeties.update(match.getPlayer(), match.getOpponent());
        breaks.update(match.getPlayer(), match.getOpponent());
        runs.update(match.getPlayer(), match.getOpponent());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long matchId;
        if (getArguments().getLong(BaseActivity.ARG_MATCH_ID, -1L) != -1L) {
            matchId = getArguments().getLong(BaseActivity.ARG_MATCH_ID);
        } else {
            throw new IllegalArgumentException("This fragment must be created with a match ID passed into it");
        }

        DatabaseAdapter db = new DatabaseAdapter(getContext());
        Match match = db.getMatch(matchId);
        overview = new MatchOverviewBinder(match.getPlayer(), match.getOpponent(), getString(R.string.title_match_overview));
        shooting = new ShootingBinder(match.getPlayer(), match.getOpponent(), getString(R.string.title_shooting));
        safeties = new SafetiesBinder(match.getPlayer(), match.getOpponent(), getString(R.string.title_safeties));
        breaks = new BreaksBinder(match.getPlayer(), match.getOpponent(), getString(R.string.title_breaks));
        runs = new RunsBinder(match.getPlayer(), match.getOpponent(), getString(R.string.title_run_outs));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match_info, null);


        FragmentMatchInfoBinding binder = FragmentMatchInfoBinding.bind(view);
        binder.setOverview(overview);
        binder.setShooting(shooting);
        binder.setSafeties(safeties);
        binder.setBreaks(breaks);
        binder.setRuns(runs);
        return view;
    }
}
