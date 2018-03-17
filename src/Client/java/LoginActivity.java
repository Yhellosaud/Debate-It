package dicomp.debateit;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import SharedModels.User;

public class LoginActivity extends AppCompatActivity implements DataReceivable {
    EditText inusername, inpassword;
    Button login;
    int dataRetrieceAttempts;
    private ServerBridge sb = new ServerBridge(this);
    Button goToRegister;
    String username, password;
    User user;
    ArrayList<Object> coming;
    LoginActivity la = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        login = (Button)findViewById(R.id.login);
        inusername   = (EditText)findViewById(R.id.username);
        inpassword   = (EditText)findViewById(R.id.password);

        login.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        login.setClickable(false);
                        username = inusername.getText().toString();
                        password = inpassword.getText().toString();
                        Object[] req = new Object[]{username, password};
                        sb.request(ServerBridge.REQUEST_REGISTER,req);
                        Helper helper = new Helper();
                        helper.execute();
                    }
                }
        );
    }
    public void goToRegister(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
    public boolean receiveAndUpdateUI(Object[] objects) {
        boolean done = false;
        return done;
    }

    private class Helper extends AsyncTask<Void, Void, User> {
        protected User doInBackground(Void... arg0) {
            coming = sb.getLeastRecentlyReceivedData();
            if(coming == null)
                login.setClickable(true);
            else{
                user = (User)coming.get(0);
                System.out.println(user.getUsername());
                Intent intent = new Intent(la, MainActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
            return null;
        }
    }
}
