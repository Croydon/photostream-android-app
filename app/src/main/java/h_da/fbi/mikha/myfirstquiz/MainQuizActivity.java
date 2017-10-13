package h_da.fbi.mikha.myfirstquiz;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;

import hochschuledarmstadt.quizapp.QuizActivity;

public class MainActivity extends QuizActivity {

    /*
     * TODO: Hier weitere Variablen für die UI Komponenten anlegen
     */
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // TODO: Hier die erzeugten Views über "findViewById(...)" referenzieren
        // und gegebenenfalls Listener setzen
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                getQuiz().setCheckedRadioButtonId(checkedId);
            }
        });
    }

    /*
	 * TODO: Methode definert eine mögliche Antwort an den Radio Button
	 * an Position index
	 */
    @Override
    public void renderPossibleAnswer(int index, String possibleAnswerText) {

    }

    /*
	 * TODO: Methode löscht den gewählten Radio Button
	 */
    @Override
    public void clearCheckedRadioButton() {

    }

    /*
	 * TODO: Methode definiert die aktulle Frage in der TextView
	 */
    @Override
    public void renderQuestion(String questionText) {

    }

    /*
	 * TODO: Methode definiert einen Untertitel, der den Fortschritt
	 * des Spiels angibt.
	 */
    @Override
    public void renderActionBarSubtitle(String subtitleText) {

    }


    /*
	 * TODO: Methode aktiviert den Button zur Übermittlung von Antworten
	 */
    @Override
    public void setAnswerButtonEnabled() {

    }

    /*
	 * TODO: Methode deaktiviert den Button zur Übermittlung von Antworten
	 */
    @Override
    public void setAnswerButtonDisabled() {

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