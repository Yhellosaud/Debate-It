package dicomp.debateit;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;

import SharedModels.*;

public class BattleMenuActivity extends AppCompatActivity implements DataReceivable {

    private static final int STAGE_SIDE_SELECTION = 0;
    private static final int STAGE_INITIAL_ARGUMENTS = 1;
    private static final int STAGE_COUNTER_ARGUMENTS = 2;
    private static final int STAGE_ANSWERS = 3;
    private static final int STAGE_CONCLUSION = 4;
    private static final int STAGE_VOTING = 5;

    // *********************
    // ***** VARIABLES *****
    // *********************
    ServerBridge sb;
    User user;

    EditText inargument;
    int stage;

    TextView player1Label, player2Label, player3Label, player4Label, ideaName, categoryView, remainingTime;
    TextView title1, title2, title3, title4;
    ImageView avatar1, avatar2, avatar3, avatar4;
    TextView arg1, arg2, counter1, counter2, answer1, answer2, conclusion1, conclusion2;
    TextView stage1Label, stage2Label, stage3Label, stage4Label;
    Button sendArgumentButton, joinButton, yesButton, noButton;
    EditText argumentEdit;

    Debate curDebate;

    // **********************************
    // ***** CONSTRUCTOR - ONCREATE *****
    // **********************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle_menu);


        player1Label = (TextView) findViewById(R.id.player1View);
        player2Label = (TextView) findViewById(R.id.player2View);
        player3Label = (TextView) findViewById(R.id.player3View);
        player4Label = (TextView) findViewById(R.id.player4View);

        title1 = (TextView) findViewById(R.id.title1);
        title2 = (TextView) findViewById(R.id.title2);
        title3 = (TextView) findViewById(R.id.title3);
        title4 = (TextView) findViewById(R.id.title4);

        avatar1 = (ImageView) findViewById(R.id.player1Avatar);
        avatar2 = (ImageView) findViewById(R.id.player2Avatar);
        avatar3 = (ImageView) findViewById(R.id.player3Avatar);
        avatar4 = (ImageView) findViewById(R.id.player4Avatar);

        ideaName = (TextView) findViewById(R.id.ideaNameView);

        categoryView = (TextView) findViewById(R.id.categoryView);

        arg1 = (TextView) findViewById(R.id.arguement1Text);
        arg2 = (TextView) findViewById(R.id.arguement2Text);
        counter1 = (TextView) findViewById(R.id.counter1Text);
        counter2 = (TextView) findViewById(R.id.counter2Text);
        answer1 = (TextView) findViewById(R.id.answer1Text);
        answer2 = (TextView) findViewById(R.id.answer2Text);
        conclusion1 = (TextView) findViewById(R.id.conclusion1Text);
        conclusion2 = (TextView) findViewById(R.id.conclusion2Text);
        stage1Label = (TextView) findViewById(R.id.stage1View);
        stage2Label = (TextView) findViewById(R.id.stage2View);
        stage3Label = (TextView) findViewById(R.id.stage3View);
        stage4Label = (TextView) findViewById(R.id.stage4View);

        remainingTime = (TextView) findViewById(R.id.remainingTime);
        sendArgumentButton = (Button) findViewById(R.id.sendArgument);
        joinButton = (Button) findViewById(R.id.joinButton);
        yesButton = (Button) findViewById(R.id.yesSideButton);
        noButton = (Button) findViewById(R.id.noSideButton);
        argumentEdit = (EditText) findViewById(R.id.argumentEdit);


        user = (User) getIntent().getSerializableExtra("user");
        setStage(-2);

        sb = new ServerBridge(this);
        sb.startListeningToServer();


    }

    public void onClick(View v) {

        if (v.getId() == R.id.sendArgument) {
            String argument = argumentEdit.getText().toString();
            sb.requestSendArgument(user, argument, 0);

        } else if (v.getId() == R.id.joinButton) {
            sb.requestJoinBattle(user);
            setStage(-1);
        } else if (v.getId() == R.id.yesSideButton) {
            sb.requestSendSideSelection(user, Player.SIDE_POSITIVE);

        } else if (v.getId() == R.id.noSideButton) {
            sb.requestSendSideSelection(user, Player.SIDE_NEGATIVE);

        }
    }


    private void setStage(int stageNo) // !!! DipNot: Sırasıyla, 0-1-2-3-4-5 olarak kullanılmazsa, hatalı çalışır.!
    {
        stage = stageNo;
        if (stageNo == -2) { //Pre join stage
            player1Label.setVisibility(View.INVISIBLE);
            player2Label.setVisibility(View.INVISIBLE);
            player3Label.setVisibility(View.INVISIBLE);
            player4Label.setVisibility(View.INVISIBLE);
            ideaName.setVisibility(View.INVISIBLE);
            categoryView.setVisibility(View.INVISIBLE);
            remainingTime.setVisibility(View.INVISIBLE);
            title1.setVisibility(View.INVISIBLE);
            title2.setVisibility(View.INVISIBLE);
            title3.setVisibility(View.INVISIBLE);
            title4.setVisibility(View.INVISIBLE);
            avatar1.setVisibility(View.INVISIBLE);
            avatar2.setVisibility(View.INVISIBLE);
            avatar3.setVisibility(View.INVISIBLE);
            avatar4.setVisibility(View.INVISIBLE);
            arg1.setVisibility(View.INVISIBLE);
            arg2.setVisibility(View.INVISIBLE);
            counter1.setVisibility(View.INVISIBLE);
            counter2.setVisibility(View.INVISIBLE);
            answer1.setVisibility(View.INVISIBLE);
            answer2.setVisibility(View.INVISIBLE);
            conclusion1.setVisibility(View.INVISIBLE);
            conclusion2.setVisibility(View.INVISIBLE);
            stage1Label.setVisibility(View.INVISIBLE);
            stage2Label.setVisibility(View.INVISIBLE);
            stage3Label.setVisibility(View.INVISIBLE);
            stage4Label.setVisibility(View.INVISIBLE);
            sendArgumentButton.setVisibility(View.INVISIBLE);
            joinButton.setVisibility(View.VISIBLE);
            yesButton.setVisibility(View.INVISIBLE);
            noButton.setVisibility(View.INVISIBLE);
            argumentEdit.setVisibility(View.INVISIBLE);

        } else if (stageNo == -1) // waiting in lobby
        {
            player1Label.setVisibility(View.VISIBLE);
            player2Label.setVisibility(View.VISIBLE);
            player3Label.setVisibility(View.VISIBLE);
            player4Label.setVisibility(View.VISIBLE);
            ideaName.setVisibility(View.VISIBLE);
            remainingTime.setVisibility(View.VISIBLE);
            title1.setVisibility(View.VISIBLE);
            title2.setVisibility(View.VISIBLE);
            title3.setVisibility(View.VISIBLE);
            title4.setVisibility(View.VISIBLE);
            avatar1.setVisibility(View.VISIBLE);
            avatar2.setVisibility(View.VISIBLE);
            avatar3.setVisibility(View.VISIBLE);
            avatar4.setVisibility(View.VISIBLE);

            //yesButton.setVisibility(View.VISIBLE);
            /*arg1.setVisibility(View.INVISIBLE);
            arg2.setVisibility(View.INVISIBLE);
            counter1.setVisibility(View.INVISIBLE);
            counter2.setVisibility(View.INVISIBLE);
            answer1.setVisibility(View.INVISIBLE);
            answer2.setVisibility(View.INVISIBLE);
            conclusion1.setVisibility(View.INVISIBLE);
            conclusion2.setVisibility(View.INVISIBLE);
            stage1Label.setVisibility(View.INVISIBLE);
            stage2Label.setVisibility(View.INVISIBLE);
            stage3Label.setVisibility(View.INVISIBLE);
            stage4Label.setVisibility(View.INVISIBLE);*/
        } else if (stageNo == 0) { // Side selection
            setStage(-2);
            setStage(-1);
            joinButton.setVisibility(View.INVISIBLE);
            yesButton.setVisibility(View.VISIBLE);
            noButton.setVisibility(View.VISIBLE);
            categoryView.setVisibility(View.VISIBLE);

        } else if (stageNo == 1) // initial arguments
        {
            argumentEdit.setVisibility(View.VISIBLE);
            sendArgumentButton.setVisibility(View.VISIBLE);
            yesButton.setVisibility(View.INVISIBLE);
            noButton.setVisibility(View.INVISIBLE);
            arg1.setVisibility(View.VISIBLE);
            arg1.setText("Argument");
            arg2.setVisibility(View.VISIBLE);
            arg2.setText("Argument");
            stage1Label.setVisibility(View.VISIBLE);
            arg1.setEnabled(true);
            arg2.setEnabled(true);
        } else if (stageNo == 2) // counter arguments
        {
            counter1.setVisibility(View.VISIBLE);
            counter1.setText("Counter argument");
            counter2.setText("Counter argument");
            counter2.setVisibility(View.VISIBLE);
            stage2Label.setVisibility(View.VISIBLE);
            arg1.setEnabled(false);
            arg2.setEnabled(false);
            counter1.setEnabled(true);
            counter2.setEnabled(true);
        } else if (stageNo == 3) // answers
        {

            answer1.setVisibility(View.VISIBLE);
            answer1.setText("Answer");
            answer2.setText("Answer");
            answer2.setVisibility(View.VISIBLE);
            stage3Label.setVisibility(View.VISIBLE);
            counter1.setEnabled(false);
            counter2.setEnabled(false);
            answer1.setEnabled(true);
            answer2.setEnabled(true);
        } else if (stageNo == 4) // conclusion
        {
            conclusion1.setVisibility(View.VISIBLE);
            conclusion1.setText("Conclusion");
            conclusion2.setText("Conclusion");
            conclusion2.setVisibility(View.VISIBLE);
            stage4Label.setVisibility(View.VISIBLE);
            answer1.setEnabled(false);
            answer2.setEnabled(false);
            conclusion1.setEnabled(true);
            conclusion2.setEnabled(true);
        } else if (stageNo == 5) // vote
        {
            yesButton.setVisibility(View.VISIBLE);
            noButton.setVisibility(View.VISIBLE);
        }
    }


    private void Set_Remaining_Time(int seconds) {
        remainingTime.setText("Remaining Time: " + seconds);
    }


    @Override
    public boolean receiveAndUpdateUI(int responseId, ArrayList<Serializable> responseData) {

        System.out.println("Battle Menu receiveAndUpdateUI, responseID: " + responseId);

        if (responseId == ServerBridge.RESPONSE_BATTLE_TIME) {

            int time = 0;
            try {
                time = (int) responseData.get(0);
            } catch (Exception e) {
                System.out.println("error in casting time");
                e.printStackTrace();
            }
            remainingTime.setText("" + time);
        } else if (responseId == ServerBridge.RESPONSE_NEW_STAGE) {

            int stage = 0;
            try {
                stage = (int) responseData.get(0);
            } catch (Exception e) {
                System.out.println("error in casting stage");
                e.printStackTrace();
            }
            setStage(stage);
        } else if (responseId == ServerBridge.RESPONSE_UPDATED_DEBATE) {
            Debate updatedDebate = (Debate) responseData.get(0);
            drawDebate(updatedDebate);
        }
        return false;
    }


    private void drawDebate(Debate updatedDebate) {

        curDebate = updatedDebate;
        String ideaText = curDebate.getIdea().getStatement();
        ideaName.setText(ideaText);
        int category = curDebate.getIdea().getCategory();
        String categoryString = "Idea";


        switch (category) {

            case (Idea.CATEGORY_ECONOMY):
                categoryString = "ECONOMY";
                break;
            case (Idea.CATEGORY_EDUCATION):
                categoryString = "EDUCATION";
                break;
            case (Idea.CATEGORY_HEALTH):
                categoryString = "HEALTH";
                break;
            case (Idea.CATEGORY_HISTORY):
                categoryString = "HISTORY";
                break;
            case (Idea.CATEGORY_PHILOSOPHY):
                categoryString = "PHILOSOPHY";
                break;
        }

        categoryView.setText(categoryString);


        ArrayList<Player> players = curDebate.getPlayers();
        int spectator = 0;

        for (int i = 0; i < players.size(); i++) {


            Player curPlayer = players.get(i);
            if (curPlayer.getSide() == Player.SIDE_POSITIVE) {
                player3Label.setText(curPlayer.getUsername());
                title3.setText(curPlayer.getSelectedTitle().getTitleName());

                int avatarID = curPlayer.getSelectedAvatar().getItemID();
                String avatarFile = "a" + avatarID+"s";
                int id = getResources().getIdentifier(avatarFile, "drawable", getPackageName());
                avatar3.setImageResource(id);

                ArrayList<Argument> positiveArguments = curPlayer.getArguments();
                int posArgsSize = positiveArguments.size();
                if (posArgsSize > 0) {
                    arg1.setText(positiveArguments.get(0).getArgument());
                }
                if (posArgsSize > 1) {
                    counter1.setText(positiveArguments.get(1).getArgument());
                }
                if (posArgsSize > 2) {
                    answer1.setText(positiveArguments.get(2).getArgument());
                }
                if (posArgsSize > 3) {
                    conclusion1.setText(positiveArguments.get(3).getArgument());
                }


                //Set avatar here
            } else if (curPlayer.getSide() == Player.SIDE_NEGATIVE) {
                player4Label.setText(curPlayer.getUsername());
                title4.setText(curPlayer.getSelectedTitle().getTitleName());

                int avatarID = curPlayer.getSelectedAvatar().getItemID();
                String avatarFile = "a" + avatarID+"s";
                int id = getResources().getIdentifier(avatarFile, "drawable", getPackageName());
                avatar4.setImageResource(id);

                ArrayList<Argument> negativeArguments = curPlayer.getArguments();
                int negArgsSize = negativeArguments.size();
                if (negArgsSize > 0) {
                    arg2.setText(negativeArguments.get(0).getArgument());
                }
                if (negArgsSize > 1) {
                    counter2.setText(negativeArguments.get(1).getArgument());
                }
                if (negArgsSize > 2) {
                    answer2.setText(negativeArguments.get(2).getArgument());
                }
                if (negArgsSize > 3) {
                    conclusion2.setText(negativeArguments.get(3).getArgument());
                }

            } else {
                if(curPlayer.getPlayerID() == user.getUserID()){

                }
                if (spectator == 0) {
                    player1Label.setText(curPlayer.getUsername());
                    title1.setText(curPlayer.getSelectedTitle().getTitleName());

                    int avatarID = curPlayer.getSelectedAvatar().getItemID();
                    String avatarFile = "a" + avatarID+"s";
                    int id = getResources().getIdentifier(avatarFile, "drawable", getPackageName());
                    avatar1.setImageResource(id);
                    spectator++;

                } else if (spectator == 1) {
                    player2Label.setText(curPlayer.getUsername());
                    title2.setText(curPlayer.getSelectedTitle().getTitleName());

                    int avatarID = curPlayer.getSelectedAvatar().getItemID();
                    String avatarFile = "a" + avatarID+"s";
                    int id = getResources().getIdentifier(avatarFile, "drawable", getPackageName());
                    avatar2.setImageResource(id);
                    spectator++;

                } else if (spectator == 2) {
                    player3Label.setText(curPlayer.getUsername());
                    title3.setText(curPlayer.getSelectedTitle().getTitleName());

                    int avatarID = curPlayer.getSelectedAvatar().getItemID();
                    String avatarFile = "a" + avatarID+"s";
                    int id = getResources().getIdentifier(avatarFile, "drawable", getPackageName());
                    avatar3.setImageResource(id);
                    spectator++;

                } else if (spectator == 3) {
                    player4Label.setText(curPlayer.getUsername());
                    title4.setText(curPlayer.getSelectedTitle().getTitleName());

                    int avatarID = curPlayer.getSelectedAvatar().getItemID();
                    String avatarFile = "a" + avatarID+"s";
                    int id = getResources().getIdentifier(avatarFile, "drawable", getPackageName());
                    avatar4.setImageResource(id);
                    spectator++;

                }
            }
        }
    }


    @Override
    public void updateRetrieveProgress(int progress) {

    }
}