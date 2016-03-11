package com.brookmanholmes.billiardmatchanalyzer.ui.stats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.utils.MultiSelectionSpinner;
import com.brookmanholmes.billiards.game.util.PlayerTurn;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 3/11/2016.
 */
public class AdvStatsDialog extends DialogFragment {

    private PlayerTurn turn = PlayerTurn.PLAYER;

    public AdvStatsDialog() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: 3/11/2016 set transition to slide in from the left if it's PLAYER's turn and slide in from right if it's OPPONENT's turn
        getDialog().getWindow().setWindowAnimations(R.style.CustomDialogTransitionTheme);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adv_stats, container, false);
        ButterKnife.bind(this, view);


        return view;
    }


    private static class AdvStatViewHolder extends RecyclerView.ViewHolder implements MultiSelectionSpinner.OnMultipleItemsSelectedListener {
        TextView title;

        public AdvStatViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.card_title);
        }

        @Override
        public void selectedIndices(List<Integer> indices) {

        }

        @Override
        public void selectedStrings(List<String> strings) {

        }
    }

    private static class ShootingViewHolder extends AdvStatViewHolder {
        String[] shotTypes = new String[]{"Cut", "Long straight shot", "Bank", "Kick", "Combo", "Carom", "Jump"};
        String[] angleList = new String[]{"0 degrees", "15 degrees", "30 degrees", "45 degrees", "60 degrees", "75 degrees", "90 degrees"};
        String[] howList = new String[]{"Too thin", "Too thick", "Left of aim point", "Right of aim point"};
        String[] whyList = new String[]{"Bad position", "Jacked up", "Lack of focus", "Over spin", "Unintentional english", "Too slow", "Too fast", "CB curved", "On the rail", "Forcing position"};

        public ShootingViewHolder(View view) {
            super(view);
            title.setText("Shooting Errors");
            MultiSelectionSpinner spinner1 = (MultiSelectionSpinner) view.findViewById(R.id.spinner);
            MultiSelectionSpinner spinner2 = (MultiSelectionSpinner) view.findViewById(R.id.spinner2);
            MultiSelectionSpinner spinner3 = (MultiSelectionSpinner) view.findViewById(R.id.spinner3);
            MultiSelectionSpinner spinner4 = (MultiSelectionSpinner) view.findViewById(R.id.spinner4);

            spinner1.setItems(shotTypes);
            spinner2.setItems(angleList);
            spinner3.setItems(howList);
            spinner4.setItems(whyList);
            spinner1.setListener(this);
            spinner2.setListener(this);
            spinner3.setListener(this);
            spinner4.setListener(this);
        }


    }

    private static class SafetyViewHolder extends AdvStatViewHolder {
        public SafetyViewHolder(View view) {
            super(view);

            title.setText("Safeties");

            MultiSelectionSpinner spinner1 = (MultiSelectionSpinner) view.findViewById(R.id.spinner);
            spinner1.setListener(this);
            spinner1.setItems(new String[]{"Full hook", "Partial hook", "Long T", "Short T", "Open"});
        }
    }

    private static class BreaksViewHolder extends AdvStatViewHolder {
        public BreaksViewHolder(View itemView) {
            super(itemView);
            title.setText("Breaks");
        }
    }
}
