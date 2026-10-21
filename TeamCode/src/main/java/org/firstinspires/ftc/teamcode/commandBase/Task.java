package org.firstinspires.ftc.teamcode.commandBase;

import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.function.Supplier;

public class Task {
    public String taskName;
    private Runnable action, leaveAction;
    private Supplier<Boolean> condition;
    private Task next;
    public int tick = -1;
    private boolean hasRunLeaveAction = false;

    // NEW: Runnable for actions that run once when the task starts
    private Runnable onStartAction;

    // NEW: Flag to ensure onStartAction runs only once per task activation
    private boolean hasRunOnStart = false;

    // Internal timer for sleep tasks
    private ElapsedTime internalTimer = new ElapsedTime();
    private boolean timerStarted = false;

    public static Task named(String taskName) {
        Task s = new Task();
        s.taskName = taskName;
        return s;
    }
    public static Task sleep(long millis) {
        return Task.named("Sleep " + millis)
                .doAction(() -> {}) // Do nothing
                .setSleepCondition(millis);
    }
    private Task setSleepCondition(long millis) {
        this.condition = () -> {
            if (!timerStarted) {
                internalTimer.reset();
                timerStarted = true;
            }
            return internalTimer.milliseconds() > millis;
        };
        return this;
    }

    public static Task sequence(Task... tasks) {
        if (tasks.length == 0) return Task.named("Empty");

        for (int i = 0; i < tasks.length - 1; i++) {
            tasks[i].then(tasks[i+1], () -> {});
        }
        return tasks[0];
    }
    public Task onStart(Runnable onStartAction) {
        this.onStartAction = onStartAction;
        return this;
    }

    public Task doAction(Runnable action) {
        this.leaveAction = () -> {};
        this.action = action;
        return this;
    }

    public Task waitUntil(Supplier<Boolean> condition) {
        this.condition = condition;
        return this;
    }

    public Task then(Task next, Runnable leaveAction) {
        this.next = next;
        this.leaveAction = leaveAction;
        return this;
    }

    public void run() {
        if (!hasRunOnStart && onStartAction != null) {
            onStartAction.run();
            hasRunOnStart = true;
        }

        if (action != null) action.run();
    }

    public boolean readyToAdvance() {
        return condition != null && condition.get();
    }

    public void runLeaveActionOnce() {
        if (!hasRunLeaveAction && leaveAction != null) {
            leaveAction.run();
            hasRunLeaveAction = true;
        }
    }

    public Task getNextTask() {
        return next;
    }

    public void reset() {
        hasRunLeaveAction = false;
        timerStarted = false;
        hasRunOnStart = false;
    }
}