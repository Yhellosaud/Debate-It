package dicomp.debateit;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.Serializable;
import java.util.ArrayList;

import SharedModels.*;

public class LoginActivity extends AppCompatActivity implements DataReceivable {
    EditText inusername, inpassword;
    Button login;
    int dataRetrieceAttempts;
    private ServerBridge sb;
    Button goToRegister;
    String username, password;
    User user;
    ArrayList<Serializable> coming;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.LoginTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        sb = new ServerBridge(this);
        sb.startListeningToServer();
        login = (Button)findViewById(R.id.login);
        inusername   = (EditText)findViewById(R.id.username);
        inpassword   = (EditText)findViewById(R.id.password);

        login.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        //login.setClickable(false);
                        username = inusername.getText().toString();
                        password = inpassword.getText().toString();
                        /*
                        Avatar avatar = new Avatar(101);
                        User user = new User(username, password, avatar);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        /**/
                        System.out.println(username);
                        System.out.println(password);
                        sb.requestSignIn(username, password);

                    }
                }
        );
    }
    public void goToRegister(View view){
        /*
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        */
        sb.startListeningToServer();
    }
    public boolean receiveAndUpdateUI(int responseId,ArrayList<Serializable> responseData) {
        System.out.println("Receive tamam");
        if(responseId == ServerBridge.RESPONSE_USER_OBJECT){
            System.out.println("response id tamam");
            if(responseData.get(0) == null)
                return false;
            user = (User)responseData.get(0);
            System.out.println("user tamam");
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            sb.disconnectFromServer();
            login.setClickable(true);
            intent.putExtra("user", user);
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    public void updateRetrieveProgress(int progress) {

    }
}
