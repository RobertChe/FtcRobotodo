package org.firstinspires.ftc.teamcode.subsystem;

import static org.firstinspires.ftc.teamcode.robot.Robot.leftFront;
import static org.firstinspires.ftc.teamcode.robot.Robot.leftRear;
import static org.firstinspires.ftc.teamcode.robot.Robot.pinpointDriver;
import static org.firstinspires.ftc.teamcode.robot.Robot.rightFront;
import static org.firstinspires.ftc.teamcode.robot.Robot.rightRear;
import static org.firstinspires.ftc.teamcode.util.Variabile.xCurrent;
import static org.firstinspires.ftc.teamcode.util.Variabile.xD;
import static org.firstinspires.ftc.teamcode.util.Variabile.xI;
import static org.firstinspires.ftc.teamcode.util.Variabile.xP;
import static org.firstinspires.ftc.teamcode.util.Variabile.xTarget;
import static org.firstinspires.ftc.teamcode.util.Variabile.yCurrent;
import static org.firstinspires.ftc.teamcode.util.Variabile.yD;
import static org.firstinspires.ftc.teamcode.util.Variabile.yI;
import static org.firstinspires.ftc.teamcode.util.Variabile.yP;
import static org.firstinspires.ftc.teamcode.util.Variabile.yTarget;
import static org.firstinspires.ftc.teamcode.util.Variabile.zCurrent;
import static org.firstinspires.ftc.teamcode.util.Variabile.zD;
import static org.firstinspires.ftc.teamcode.util.Variabile.zI;
import static org.firstinspires.ftc.teamcode.util.Variabile.zP;
import static org.firstinspires.ftc.teamcode.util.Variabile.zTarget;

import com.arcrobotics.ftclib.controller.PIDController;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import org.firstinspires.ftc.teamcode.util.Subsystem;
import org.firstinspires.ftc.teamcode.util.Vector;

public class DriveTrain implements Subsystem {
    public static PIDController xPID= new PIDController(xP, xI, xD), yPID = new PIDController(yP, yI, yD), zPID = new PIDController(zP, zI, zD);
    public enum State{
        OFF, DRIVE, AUTO
    }
    State state = State.OFF;

    public void setState(State state) {
        if(this.state!= state){
            this.state = state;
        }

    }

    public State getState() {
        return state;
    }

    public double[] powerArray = new double[4];
    public void setPowerVector(Vector powerVector){
        double d = Math.max(Math.abs(powerVector.getY())+Math.abs(powerVector.getX())+Math.abs(powerVector.getZ()),1);
        powerArray[0]=(powerVector.getY()+ powerVector.getX()+ powerVector.getZ())/d;
        powerArray[1]=(powerVector.getY()- powerVector.getX()+ powerVector.getZ())/d;
        powerArray[2]=(powerVector.getY()-powerVector.getX()-powerVector.getZ())/d;
        powerArray[3]=(powerVector.getY()+powerVector.getX()-powerVector.getZ())/d;
    }

    @Override
    public void periodic() {
        switch (state){
            case OFF:
                break;
            case DRIVE:
                leftFront.setPower(powerArray[0]);
                leftRear.setPower(powerArray[1]);
                rightFront.setPower(powerArray[2]);
                rightRear.setPower(powerArray[3]);
                break;
            case AUTO:
                xPID.setPID(xP,xI,xD);
                yPID.setPID(yP,yI,yD);
                zPID.setPID(zP,zI,zD);
                xCurrent = pinpointDriver.getPosX(DistanceUnit.INCH);
                yCurrent=pinpointDriver.getPosY(DistanceUnit.INCH);
                zCurrent=pinpointDriver.getHeading(AngleUnit.RADIANS);
                double x = xPID.calculate(xCurrent, xTarget);
                double y = yPID.calculate(yCurrent,yTarget);
                double angle = zCurrent;
                double error = Math.toRadians(-zTarget)-angle;
                if(Math.abs(error)>Math.PI)
                    error=-Math.signum(error)*(2*Math.PI-Math.abs(error));
                double t = zPID.calculate(error,0);
                double x_rotation = x*Math.sin(angle) + y*Math.cos(angle);
                double y_rotation = x*Math.cos(angle) - y*Math.sin(angle);
                double denominator = Math.max(Math.abs(x_rotation) + Math.abs(y_rotation) + Math.abs(t), 1.0);
                leftFront.setPower(((y_rotation + x_rotation + t) / denominator));
                rightFront.setPower(((y_rotation - x_rotation - t) / denominator));
                leftRear.setPower(((y_rotation - x_rotation + t) / denominator));
                rightRear.setPower(((y_rotation+ x_rotation- t) / denominator));
                break;
        }
    }
}
