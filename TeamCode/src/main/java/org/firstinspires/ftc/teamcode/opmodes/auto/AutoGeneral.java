package org.firstinspires.ftc.teamcode.opmodes.auto;

import static org.firstinspires.ftc.teamcode.robot.Robot.initHardware;
import static org.firstinspires.ftc.teamcode.util.Variabile.xCurrent;
import static org.firstinspires.ftc.teamcode.util.Variabile.yCurrent;
import static org.firstinspires.ftc.teamcode.util.Variabile.zCurrent;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.commandBase.Scheduler;
import org.firstinspires.ftc.teamcode.opmodes.auto.task.General;
import org.firstinspires.ftc.teamcode.util.Drawing;

@Autonomous(name="AutoRed")
public class AutoGeneral extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        initHardware(hardwareMap);
        Drawing drawing = new Drawing();
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        TelemetryPacket packet;
        General general=new General();
        general.init();
        waitForStart();
        while(opModeIsActive()){
            packet = new TelemetryPacket();
            drawing.drawImage(packet.fieldOverlay(),new Pose2d(xCurrent,-yCurrent,zCurrent));
            FtcDashboard.getInstance().sendTelemetryPacket(packet);
            general.run();
        }
    }
}
