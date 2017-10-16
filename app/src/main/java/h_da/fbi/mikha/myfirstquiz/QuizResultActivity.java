package h_da.fbi.mikha.myfirstquiz;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class QuizResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);
        int correctAnswers = getIntent().getExtras().getInt("correctAnswers");
        int wrongAnswers = getIntent().getExtras().getInt("wrongAnswers");

        TextView correctAnswersTextView = (TextView) findViewById(R.id.correctAnswerText);
        TextView wrongAnswersTextView = (TextView) findViewById(R.id.wrongAnswerText);

        correctAnswersTextView.setText(correctAnswers + " richtige Antworten");
        wrongAnswersTextView.setText(wrongAnswers + " falsche Antworten");

    }
}
