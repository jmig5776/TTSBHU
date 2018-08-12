package app.strangers.talk.ttsbhu;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {


    private EditText userName;
    private Button startConnecting;
    private DatabaseReference onlineUsers;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userName=(EditText)findViewById(R.id.username);
        mAuth = FirebaseAuth.getInstance();
        startConnecting=(Button)findViewById(R.id.start_connect);

        startConnecting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nickName=userName.getText().toString();

                RegisterAccount(nickName);
            }
        });
    }

    private void RegisterAccount(final String nickName)
    {
        if(TextUtils.isEmpty(nickName))
        {
            Toast.makeText(MainActivity.this, "Please write your nickname", Toast.LENGTH_LONG).show();
        }
        else
        {


            mAuth.signInAnonymously()

                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                String currentUserId=mAuth.getCurrentUser().getUid();
                                onlineUsers= FirebaseDatabase.getInstance().getReference().child("onlineUsers").child(currentUserId);

                                onlineUsers.child("userName").setValue(nickName)
                                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Intent intent=new Intent(MainActivity.this,ChatActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                {
                                        }

                            } else {
                                // If sign in fails, display a message to the user.

                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });
        }
    }


}
