package org.firstinspires.ftc.teamcode.commandBase;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Scheduler {
    private static final List<Command> commands = new ArrayList<>();
    public static void add(Command command){
        command.start();
        commands.add(command);
    }
    public static void run(){
        Iterator<Command> i = commands.iterator();
        while(i.hasNext()){
            Command command = i.next();
            command.execute();
            if(command.isFinished()){
                command.end();
                i.remove();
            }
        }
    }
    public static void reset(){
        commands.clear();
    }
}
