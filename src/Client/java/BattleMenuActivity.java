package dicomp.debateit;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;

import SharedModels.*;

public class BattleMenuActivity extends AppCompatActivity implements DataReceivable {

    // *********************
    // ***** VARIABLES *****
    // *********************
    ServerBridge sb;
    User user;
    Button sendArgument;
    EditText inargument;
    int stage;

    TextView player1Label, player2Label, player3Label, player4Label, ideaName, remainingTime, topic,remainingTimeEdit;
    EditText arg1, arg2, counter1, counter2, answer1, answer2, conclusion1, conclusion2;
    TextView stage1Label, stage2Label, stage3Label, stage4Label;
    Button exitButton;

    // **********************************
    // ***** CONSTRUCTOR - ONCREATE *****
    // **********************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle_menu);
/*
        sb = new ServerBridge(this);
        user = (User)getIntent().getSerializableExtra("user");
        sendArgument = (Button)findViewById(R.id.send_argument);
        sb.startListeningToServer();
        sb.requestJoinBattle(user);
*/
        player1Label = (TextView) findViewById(R.id.player1View);
        player2Label = (TextView) findViewById(R.id.player2View);
        player3Label = (TextView) findViewById(R.id.player3View);
        player4Label = (TextView) findViewById(R.id.player4View);
        ideaName = (TextView) findViewById(R.id.ideaNameView);
        remainingTime = (TextView) findViewById(R.id.remainingTimeView);
        topic = (TextView) findViewById(R.id.topicView);

        arg1 = (EditText) findViewById(R.id.arguement1Text);
        arg2 = (EditText) findViewById(R.id.arguement2Text);
        counter1 = (EditText) findViewById(R.id.counter1Text);
        counter2 = (EditText) findViewById(R.id.counter2Text);
        answer1 = (EditText) findViewById(R.id.answer1Text);
        answer2 = (EditText) findViewById(R.id.answer2Text);
        conclusion1 = (EditText) findViewById(R.id.conclusion1Text);
        conclusion2 = (EditText) findViewById(R.id.conclusion2Text);
        stage1Label = (TextView) findViewById(R.id.stage1View);
        stage2Label = (TextView) findViewById(R.id.stage2View);
        stage3Label = (TextView) findViewById(R.id.stage3View);
        stage4Label = (TextView) findViewById(R.id.stage4View);

        remainingTimeEdit = (TextView)findViewById(R.id.remainingTimeEdit);

        user = (User) getIntent().getSerializableExtra("user");


        /*setStage(0);

        // TEST İÇİN AÇ!
        setStage(1);
        setStage(2);
        setStage(3);
        setStage(4);
        setStage(5);*/
    }

    // *******************
    // ***** METHODS *****
    // *******************

    public void setStage(int stageNo) // !!! DipNot: Sırasıyla, 0-1-2-3-4-5 olarak kullanılmazsa, hatalı çalışır.!
    {
        stage = stageNo;
        if(stageNo == 0) // At Start
        {
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
        }
        else if(stageNo == 1) // Stage 1
        {
            arg1.setVisibility(View.VISIBLE);
            arg2.setVisibility(View.VISIBLE);
            stage1Label.setVisibility(View.VISIBLE);
            arg1.setEnabled(true);
            arg2.setEnabled(true);
        }
        else if(stageNo == 2) // Stage 2
        {
            counter1.setVisibility(View.VISIBLE);
            counter2.setVisibility(View.VISIBLE);
            stage2Label.setVisibility(View.VISIBLE);
            arg1.setEnabled(false);
            arg2.setEnabled(false);
            counter1.setEnabled(true);
            counter2.setEnabled(true);
        }
        else if(stageNo == 3) // Stage 3
        {

            answer1.setVisibility(View.VISIBLE);
            answer2.setVisibility(View.VISIBLE);
            stage3Label.setVisibility(View.VISIBLE);
            counter1.setEnabled(false);
            counter2.setEnabled(false);
            answer1.setEnabled(true);
            answer2.setEnabled(true);
        }
        else if(stageNo == 4) // Stage 4
        {
            conclusion1.setVisibility(View.VISIBLE);
            conclusion2.setVisibility(View.VISIBLE);
            stage4Label.setVisibility(View.VISIBLE);
            answer1.setEnabled(false);
            answer2.setEnabled(false);
            conclusion1.setEnabled(true);
            conclusion2.setEnabled(true);
        }
        else if(stageNo == 5) // When Finished
        {
            conclusion1.setEnabled(false);
            conclusion2.setEnabled(false);
        }
    }

    // *******************************
    // ***** SETTERS - GAME INFO *****
    // *******************************

    public void Set_Player1_Name(String name)
    {
        player1Label.setText(name);
    }
    public void Set_Player2_Name(String name)
    {
        player2Label.setText(name);
    }
    public void Set_Player3_Name(String name)
    {
        player3Label.setText(name);
    }
    public void Set_Player4_Name(String name)
    {
        player4Label.setText(name);
    }
    public void Set_Idea_Name(String text)
    {
        ideaName.setText(text);
    }
    public void Set_Topic(String text)
    {
        topic.setText(text);
    }
    public void Set_Remaining_Time(int seconds)
    {
        remainingTime.setText("Remaining Time: " + seconds);
    }

    // *******************************
    // ***** SETTERS - GAME AREA *****
    // *******************************

    public void Set_Argument1(String text)
    {
        arg1.setText(text);
    }
    public void Set_Argument2(String text)
    {
        arg2.setText(text);
    }
    public void Set_Counter1(String text)
    {
        counter1.setText(text);
    }
    public void Set_Counter2(String text)
    {
        counter1.setText(text);
    }
    public void Set_Answer1(String text)
    {
        answer1.setText(text);
    }
    public void Set_Answer2(String text)
    {
        answer2.setText(text);
    }
    public void Set_Conclusion1(String text)
    {
        conclusion1.setText(text);
    }
    public void Set_Conclusion2(String text)
    {
        conclusion2.setText(text);
    }

    // *******************************
    // ***** GETTERS - GAME AREA *****
    // *******************************

    public String Get_Argument1()
    {
        return arg1.getText().toString();
    }
    public String Get_Argument2()
    {
        return arg2.getText().toString();
    }
    public String Get_Counter1()
    {
        return counter1.getText().toString();
    }
    public String Get_Counter2() { return counter1.getText().toString(); }
    public String Get_Answer1()
    {
        return  answer1.getText().toString();
    }
    public String Get_Answer2()
    {
        return answer2.getText().toString();
    }
    public String Get_Conclusion1()
    {
        return conclusion1.getText().toString();
    }
    public String Get_Conclusion2()
    {
        return conclusion2.getText().toString();
    }

    @Override
    public boolean receiveAndUpdateUI(int responseId,ArrayList<Serializable> responseData) {

        if(responseId == ServerBridge.RESPONSE_BATTLE_TIME){

            int time =0;
            try{
                time = (int) responseData.get(0);
            }catch(Exception e){
                System.out.println("error in casting time");
                e.printStackTrace();
            }
            remainingTimeEdit.setText(time);
        }else if(responseId==ServerBridge.RESPONSE_NEW_STAGE){

            int stage =0;
            try{
                stage = (int)responseData.get(0);
            }catch(Exception e){
                System.out.println("error in casting stage");
                e.printStackTrace();
            }
            setStage(stage);
        }
        return false;
    }
    private void drawDebate(Debate debate){

    }

    private void sendArgument(View view){
        String argument = inargument.getText().toString();
        sb.requestSendArgument(user, argument,stage);
    }

    @Override
    public void updateRetrieveProgress(int progress) {

    }
}