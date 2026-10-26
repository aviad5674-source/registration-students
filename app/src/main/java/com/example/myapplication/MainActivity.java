package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText firstName, lastName, email;
    RadioGroup classGroup;
    Spinner majorSpinner;
    CheckBox notifications, terms;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstName = findViewById(R.id.editTextFirstName);
        lastName = findViewById(R.id.editTextLastName);
        email = findViewById(R.id.editTextEmail);
        classGroup = findViewById(R.id.radioGroupClass);
        majorSpinner = findViewById(R.id.spinnerMajor);
        notifications = findViewById(R.id.checkBoxNotifications);
        terms = findViewById(R.id.checkBoxTerms);
        result = findViewById(R.id.textViewResult);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.majors, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        majorSpinner.setAdapter(adapter);

        // טעינת הנתונים שנשמרו בפעם הקודמת
        SharedPreferences prefs = getSharedPreferences("student", MODE_PRIVATE);
        if (prefs.contains("firstName")) {
            Student savedStudent = new Student(
                    prefs.getString("firstName", ""),
                    prefs.getString("lastName", ""),
                    prefs.getString("email", ""),
                    prefs.getString("studentClass", ""),
                    prefs.getString("major", ""),
                    prefs.getBoolean("notifications", false)
            );
            showStudent(savedStudent);
        }

        Button registerButton = findViewById(R.id.buttonRegister);
        registerButton.setOnClickListener(v -> {
            String name = firstName.getText().toString().trim();
            String surname = lastName.getText().toString().trim();
            String mail = email.getText().toString().trim();

            if (name.isEmpty()) {
                firstName.setError(getString(R.string.error_first_name));
                return;
            }

            if (surname.isEmpty()) {
                lastName.setError(getString(R.string.error_last_name));
                return;
            }

            if (mail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                email.setError(getString(R.string.error_email));
                return;
            }

            if (classGroup.getCheckedRadioButtonId() == -1) {
                TextView classLabel = findViewById(R.id.textViewClassLabel);
                classLabel.setError(getString(R.string.error_class));
                return;
            }

            if (majorSpinner.getSelectedItemPosition() == 0) {
                TextView majorLabel = findViewById(R.id.textViewMajorLabel);
                majorLabel.setError(getString(R.string.error_major));
                return;
            }

            if (!terms.isChecked()) {
                terms.setError(getString(R.string.error_terms));
                return;
            }

            TextView selectedClassView = findViewById(classGroup.getCheckedRadioButtonId());

            Student student = new Student(
                    name,
                    surname,
                    mail,
                    selectedClassView.getText().toString(),
                    majorSpinner.getSelectedItem().toString(),
                    notifications.isChecked()
            );

            // שמירה, כך שהפרטים החדשים יחליפו את הקודמים
            prefs.edit()
                    .putString("firstName", student.firstName)
                    .putString("lastName", student.lastName)
                    .putString("email", student.email)
                    .putString("studentClass", student.studentClass)
                    .putString("major", student.major)
                    .putBoolean("notifications", student.notifications)
                    .apply();

            showStudent(student);
        });
    }

    private void showStudent(Student student) {
        String notificationsAnswer = student.notifications
                ? getString(R.string.notifications_yes)
                : getString(R.string.notifications_no);

        result.setText(getString(
                R.string.result_format,
                student.firstName,
                student.lastName,
                student.email,
                student.studentClass,
                student.major,
                notificationsAnswer
        ));

        result.setVisibility(View.VISIBLE);
    }
}