package org.firstinspires.ftc.teamcode.opmodes.auto.task;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.commandBase.Scheduler;
import org.firstinspires.ftc.teamcode.commandBase.Task;
import org.firstinspires.ftc.teamcode.subsystem.DriveTrain;
import org.firstinspires.ftc.teamcode.util.Vector;

import java.util.concurrent.TimeUnit;

public class General {
    public Task currentTask;
    private ElapsedTime taskTimer;
    public DriveTrain driveTrain;
    public double time = 0;
    private boolean newTask = true;
    public static Vector startingPosition = new Vector(0,0,0);
    public static Vector go1= new Vector(1,2,3);
    public static Vector go2 = new Vector(3,4,5);
    public static Vector go3 = new Vector(5,6,7);
    public void init(){
        driveTrain=new DriveTrain();
        taskTimer = new ElapsedTime();
        driveTrain.setState(DriveTrain.State.AUTO);
        Scheduler.reset();
        Task emptyTask = Task.named("emptyTask")
                .waitUntil(() -> false);
        Task Back3 =Task.named("intoarcetelastart")
                .onStart(() -> {
                    driveTrain.setTargetPosition(startingPosition);
                    taskTimer.reset();
                })
                .waitUntil(() ->time>750)
                .then(emptyTask,()->{});
        Task Pos3 =Task.named("dutela3")
                .onStart(() -> {
                    driveTrain.setTargetPosition(go3);
                    taskTimer.reset();
                })
                .waitUntil(() ->time>600)
                .then(Back3,()->{});
        Task Back2 =Task.named("intoarcetelastart")
                .onStart(() -> {
                    driveTrain.setTargetPosition(startingPosition);
                    taskTimer.reset();
                })
                .waitUntil(() ->time>500)
                .then(Pos3,()->{});
        Task Pos2 =Task.named("dutela2")
                .onStart(() -> {
                    driveTrain.setTargetPosition(go2);
                    taskTimer.reset();
                })
                .waitUntil(() ->time>1000)
                .then(Back2,()->{});
        Task Back1 =Task.named("intoarcetelastart")
                .onStart(() -> {
                    driveTrain.setTargetPosition(startingPosition);
                    taskTimer.reset();
                })
                .waitUntil(() ->time>800)
                .then(Pos2,()->{});
        Task Pos1 =Task.named("mergilapos1")
                .onStart(() -> {
                    driveTrain.setTargetPosition(go1);
                    taskTimer.reset();
                })
                .waitUntil(() ->driveTrain.hasReachedTarget(3,3,10))
                .then(Back1,()->{});
        currentTask = Pos1;
    }
    public void run(){
        driveTrain.periodic();
        Scheduler.run();
        time = taskTimer.time(TimeUnit.MILLISECONDS);
        currentTask.run();
        if (currentTask.readyToAdvance()) {
            taskTimer.reset();
            currentTask.runLeaveActionOnce();
            currentTask = currentTask.getNextTask();
            currentTask.reset();
            newTask = true;
        }
    }
}
