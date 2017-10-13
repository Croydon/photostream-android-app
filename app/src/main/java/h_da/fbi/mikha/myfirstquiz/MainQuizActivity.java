package h_da.fbi.mikha.myfirstquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import hochschuledarmstadt.quizapp.QuizActivity;

public class MainQuizActivity extends QuizActivity {

    /*
     * TODO: Hier weitere Variablen für die UI Komponenten anlegen
     */
    private RadioGroup radioGroup;
    private Button button;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        textView = (TextView) findViewById(R.id.Frage);
        button = (Button) findViewById(R.id.submitButton);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                getQuiz().setCheckedRadioButtonId(checkedId);

            }
        });



        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                getQuiz().submitAnswer();
            }
        });


    }

    /*
	 * Methode definert eine mögliche Antwort an den Radio Button
	 * an Position index
	 */
    @Override
    public void renderPossibleAnswer(int index, String possibleAnswerText) {

        RadioButton selectedButton = (RadioButton) radioGroup.getChildAt(index);
        selectedButton.setText(possibleAnswerText);
    }

    /*
	 * Methode löscht den gewählten Radio Button
	 */
    @Override
    public void clearCheckedRadioButton() {
        radioGroup.clearCheck();
    }

    /*
	 * Methode definiert die aktulle Frage in der TextView
	 */
    @Override
    public void renderQuestion(String questionText) {
        textView.setText(questionText);

    }

    /*
	 *  Methode definiert einen Untertitel, der den Fortschritt
	 * des Spiels angibt.
	 */
    @Override
    public void renderActionBarSubtitle(String subtitleText) {

        setTitle(subtitleText);
    }


    /*
	 *  Methode aktiviert den Button zur Übermittlung von Antworten
	 */
    @Override
    public void setAnswerButtonEnabled() {
        button.setEnabled(true);
    }

    /*
	 *  Methode deaktiviert den Button zur Übermittlung von Antworten
	 */
    @Override
    public void setAnswerButtonDisabled() {
        button.setEnabled(false);
    }


    /*
	 * TODO: Methode wird aufgerufen, nachdem die letzte Frage beantwortet
	 *       wurde.
	 */
    @Override
    public void onQuizEnd(final int correctAnswers, final int wrongAnswers) {


    }

    @Override
    public int getIndexOfRadioButtonInRadioGroupFor(final int radioButtonId) {
        View radioButton = radioGroup.findViewById(radioButtonId);
        int positionOfRadioButtonInRadioGroup = radioGroup.indexOfChild(radioButton);
        return positionOfRadioButtonInRadioGroup;
    }
}