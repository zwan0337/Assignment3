package com.example.assignment3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;

import com.example.assignment3.databinding.ActivityPlanBinding;
import com.example.assignment3.entity.WorkoutPlan;

import java.util.List;
import java.util.concurrent.CompletableFuture;


public class PlanActivity extends AppCompatActivity {

    private ActivityPlanBinding binding;
    private com.example.assignment3.viewmodel.PlanViewModel PlanViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlanBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        //binding.idTextField.setPlaceholderText("This is only used for Edit");

        PlanViewModel =
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(com.example.assignment3.viewmodel.PlanViewModel.class);
        PlanViewModel.getAllWorkoutPlan().observe(this, new Observer<List<WorkoutPlan>>() {
            @Override
            public void onChanged(@Nullable final List<WorkoutPlan> workoutPlans) {
                String allWorkoutPlan = "";
                for (WorkoutPlan temp : workoutPlans) {
                    String workoutplanDetails = (temp.pid + " " + temp.planName + " " + temp.planLength + " " + temp.time + " " + temp.category + " " + temp.planRoutine);
                    allWorkoutPlan = allWorkoutPlan +
                            System.getProperty("line.separator") + workoutplanDetails;
                }
                binding.textViewRead.setText("All data: " + allWorkoutPlan);
            }
        });

        binding.addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String name = binding.nameTextField.getEditText().getText().toString();
                String length = binding.nameTextField.getEditText().getText().toString();
                String time = binding.nameTextField.getEditText().getText().toString();
                String category = binding.categoryTextField.getEditText().getText().toString();
                String planRoutine = binding.planRoutineTextField.getEditText().getText().toString();

                /*
                if ((!name.isEmpty() && name!= null) && (!surname.isEmpty() && strSalary!=null) && (!strSalary.isEmpty() && surname!=null)) {
                    double salary = Double.parseDouble(strSalary);
                    WorkoutPlan workoutplan = new WorkoutPlan(name, surname, salary);
                    PlanViewModel.insert(workoutplan);
                    binding.textViewAdd.setText("Added Record: " + name + " " +
                            surname + " " + salary);
                }*/

            }});
        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PlanViewModel.deleteAll();
                binding.textViewDelete.setText("All data was deleted");
            }
        });
        binding.clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                binding.nameTextField.getEditText().setText("");
                binding.planLengthTextField.getEditText().setText("");
                binding.timeTextField.getEditText().setText("");
                binding.categoryTextField.getEditText().setText("");
                binding.planRoutineTextField.getEditText().setText("");
            }
        });
        binding.updateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String name= binding.nameTextField.getEditText().getText().toString();
                String length = binding.planLengthTextField.getEditText().getText().toString();
                String time = binding.timeTextField.getEditText().getText().toString();
                String category = binding.categoryTextField.getEditText().getText().toString();
                String planRoutine = binding.planRoutineTextField.getEditText().getText().toString();

                WorkoutPlan workoutplan = new WorkoutPlan(name, length, time, category, planRoutine);
                PlanViewModel.insert(workoutplan);

                if ( (!category.isEmpty() && category!=null) && (!planRoutine.isEmpty() && planRoutine!=null)) {
                    //this deals with versioning issues
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        CompletableFuture<WorkoutPlan> workoutPlanCompletableFuture = PlanViewModel.findByIDFuture(workoutplan.pid);
                        workoutPlanCompletableFuture.thenApply(workoutPlan -> {
                            if (workoutPlan != null) {
                                workoutPlan.planName = name;
                                workoutPlan.planLength = length;
                                workoutPlan.time = time;
                                workoutPlan.category = category;
                                workoutPlan.planRoutine = planRoutine;

                                PlanViewModel.update(workoutPlan);
                                binding.textViewUpdate.setText("Update was successful for ID: " + workoutPlan.pid);
                            } else {
                                binding.textViewUpdate.setText("Id does not exist");
                            }
                            return workoutPlan;
                        });
                    }
                }
            }
        });
    }
}