package org.firstinspires.ftc.teamcode.commandBase;

import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;

public class Action implements Command{
    public String actionName = "NULL";
    private Runnable onStart = () ->{};
    private Runnable onExecute = () ->{};
    private Runnable onEnd = ()->{};
    private BooleanSupplier isFinished = ()->false;
    private ElapsedTime timer = new ElapsedTime();

    public static Action named(String taskName) {
        Action newAction = new Action();
        newAction.actionName = taskName;
        return newAction;
    }
    public Action onStart(Runnable task){
        this.onStart = task;
        return this;
    }
    public Action loop(Runnable task){
        this.onExecute = task;
        return this;
    }
    public Action stopWhen(BooleanSupplier cond){
        this.isFinished = cond;
        return this;
    }
    public Action onEnd(Runnable task){
        this.onEnd = task;
        return this;
    }
    public Action stopAfter(double t, TimeUnit timeUnit) {
        BooleanSupplier existingCondition = this.isFinished;
        this.isFinished = () -> existingCondition.getAsBoolean() || timer.time(timeUnit) > t;
        return this;
    }
    public Action then(Action next) {
        Action chainedAction = new Action();
        chainedAction.actionName = this.actionName + " -> " + next.actionName;
        AtomicBoolean firstHasFinished = new AtomicBoolean(false);

        chainedAction.onStart(() -> {
            firstHasFinished.set(false);
            this.start();
        });

        chainedAction.loop(() -> {
            if (!firstHasFinished.get()) {
                this.execute();
                if (this.isFinished()) {
                    this.end();
                    firstHasFinished.set(true);
                    next.start();
                }
            } else {
                next.execute();
            }
        });
        chainedAction.stopWhen(() -> firstHasFinished.get() && next.isFinished());

        chainedAction.onEnd(() -> {
            if (firstHasFinished.get()) {
                next.end();
            } else {
                this.end();
            }
        });

        return chainedAction;
    }
    @Override
    public void start() {
        timer.reset();
        onStart.run();
    }

    @Override
    public void execute() {
        onExecute.run();
    }

    @Override
    public void end() {
        onEnd.run();

    }
    @Override
    public String toString(){
        return actionName;
    }
    @Override
    public boolean isFinished() {
        return isFinished.getAsBoolean();
    }
}
