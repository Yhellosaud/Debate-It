package dicomp.debateit;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import SharedModels.*;

import dicomp.debateit.R;

public class RegisterActivity extends AppCompatActivity implements DataReceivable {
    EditText inusername, inpassword, inconfirmPassword;
    ServerBridge sb;
    ArrayList<Serializable> coming;
    TextView warning;
    String username, password, confirmPassword;
    Button register;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.LoginTheme);
        setContentView(R.layout.register_activity);
        sb = new ServerBridge(this);
        sb.startListeningToServer();
        warning = (TextView)findViewById(R.id.warning);
        warning.setVisibility(View.GONE);
        register = (Button)findViewById(R.id.register);
        inusername   = (EditText)findViewById(R.id.username);
        inpassword   = (EditText)findViewById(R.id.password);
        inconfirmPassword   = (EditText)findViewById(R.id.confirmPassword);
    }
    public void register(View view){
        register.setClickable(false);
        username = inusername.getText().toString();
        password = inpassword.getText().toString();
        confirmPassword = inconfirmPassword.getText().toString();
        System.out.println("asd1");
        if(!password.equals(confirmPassword)){
            warning.setText("Registeration Failed!");
            warning.setVisibility(View.VISIBLE);
            System.out.println("asd2");
            register.setClickable(true);
            System.out.println("asd3");
        }
        else{
            warning.setVisibility(View.VISIBLE);
            warning.setText("Maybe Successful, Maybe Not.");
            sb.requestRegister(username, password);
        }
    }

    public void goToLogin(View view){
        finish();
    }

    public boolean receiveAndUpdateUI(int responseId,ArrayList<Serializable> responseData) {
        return false;
    }

    @Override
    public void updateRetrieveProgress(int progress) {

    }
}
