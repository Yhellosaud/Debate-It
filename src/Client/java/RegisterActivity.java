package dicomp.debateit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
    }
    public void goToLogin(View view){
        finish();
    }
    public boolean receiveAndUpdateUI(Object[] objects) {
        return false;
    }
}
