package org.firstinspires.ftc.teamcode.commandBase;

public interface Command {
    void start();
    void execute();
    void end();
    boolean isFinished();
}
