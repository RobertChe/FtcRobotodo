package org.firstinspires.ftc.teamcode.opmodes.teleop;

import static org.firstinspires.ftc.teamcode.robot.Robot.initHardware;
import static org.firstinspires.ftc.teamcode.util.Variabile.xCurrent;
import static org.firstinspires.ftc.teamcode.util.Variabile.yCurrent;
import static org.firstinspires.ftc.teamcode.util.Variabile.zCurrent;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystem.DriveTrain;
import org.firstinspires.ftc.teamcode.util.Drawing;
import org.firstinspires.ftc.teamcode.util.Vector;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp ( name= "TeleOp")
public class TeleOp extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        initHardware(hardwareMap);
        Drawing drawing = new Drawing();
        DriveTrain driveTrain = new DriveTrain();
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        TelemetryPacket packet;
        waitForStart();
        while (opModeIsActive()) {
            packet = new TelemetryPacket();
            Vector controller = new Vector(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad2.right_stick_x);
            driveTrain.setPowerVector(controller);
            driveTrain.periodic();
            drawing.drawImage(packet.fieldOverlay(),new Pose2d(xCurrent,-yCurrent,zCurrent));
            FtcDashboard.getInstance().sendTelemetryPacket(packet);
        }
    }
}
