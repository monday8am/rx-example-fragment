package fidesmo.com.rxexample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import rx.functions.Action1;
import rx.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity implements EmailFragment.OnFragmentInteractionListener {

    private PublishSubject<String> fromFragment = PublishSubject.create();
    private String fragmentTag = "FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = (TextView)findViewById(R.id.centeredTextView);

        fromFragment.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                textView.setText("from fragment: " + s);
            }
        });
    }

    // Bound with UI
    public void onButtonClicked(View button) {
        Fragment newFragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);

        if (newFragment == null) {
            newFragment = EmailFragment.newInstance("hello", "fragment");
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, newFragment, fragmentTag)
                    .commit();
        }
    }

    @Override
    public void onFragmentInteraction(String email) {
        fromFragment.onNext(email);

        Fragment existing = getSupportFragmentManager().findFragmentByTag(fragmentTag);
        getSupportFragmentManager()
                .beginTransaction()
                .remove(existing)
                .commit();
    }
}
