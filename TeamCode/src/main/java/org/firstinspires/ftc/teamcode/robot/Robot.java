package org.firstinspires.ftc.teamcode.robot;

import static org.firstinspires.ftc.teamcode.util.Variabile.xOffset;
import static org.firstinspires.ftc.teamcode.util.Variabile.yOffset;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Robot {
    public static DcMotorEx leftFront, leftRear, rightFront, rightRear;

    public static GoBildaPinpointDriver pinpointDriver;

    public  static void initHardware(HardwareMap hwMap){
        leftFront = hwMap.get(DcMotorEx.class, "leftFront");
        leftRear = hwMap.get(DcMotorEx.class, "leftRear");
        rightFront = hwMap.get(DcMotorEx.class, "rightFront");
        rightRear = hwMap.get(DcMotorEx.class, "rightRear");
        pinpointDriver = hwMap.get(GoBildaPinpointDriver.class, "pinpointDriver");
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftRear.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        pinpointDriver.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        pinpointDriver.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);
        pinpointDriver.setOffsets(DistanceUnit.INCH.fromInches(xOffset), DistanceUnit.INCH.fromInches(yOffset), DistanceUnit.INCH);
        pinpointDriver.resetPosAndIMU();
    }
}
