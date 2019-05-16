package com.company.logic;

import com.company.entities.Arc;
import com.company.entities.Point;
import com.company.entities.Transition;

import java.io.*;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.company.Constants.*;
import static com.company.utils.RunUtils.getCommands;

public class Store {
    private String P = "P";
    private String T = "T";
    private String A = "A";

    public boolean shouldRun = true;


    public Map<String, Point> points = new HashMap<>();
    public Map<String, Transition> transitions = new HashMap<>();
    public Map<String, Arc> arcs = new HashMap<>();

    int currentPoint = 0;
    int currentTransition = 0;
    int currentArc = 0;


    public void addPoint(String name, int val) {
        Point point = new Point();
        point.value = val;
        point.name = name == null ? P + currentPoint : name;

        points.put(point.name, point);
        currentPoint++;
    }

    public void addTransition(String name) {
        Transition transition = new Transition();
        transition.name = name == null ? T + currentTransition : name;

        transitions.put(name, transition);
        currentTransition++;
    }

    public void addArc(String fromName, String toName, int val, String name, boolean toTranstion) throws Exception {
        Arc arc = new Arc();
        arc.value = val;
        arc.name = name == null ? A + currentArc : name;
        arc.from = toTranstion ? points.get(fromName) : transitions.get(fromName);
        arc.to = toTranstion ? transitions.get(toName) : points.get(toName);
        if (arc.from == null || arc.to == null) {
            throw new Exception();
        }

        arcs.put(arc.name, arc);
        if (toTranstion) {
            ((Transition) arc.to).enterArcs.add(arc);
        } else {
            ((Transition) arc.from).leavingArcs.add(arc);
        }
        currentArc++;
    }

    public void run(Run run) throws IOException {
        help();

        if (!shouldRun) {
            return;
        }
        String cmd = "";
        java.io.BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        while (shouldRun) {
            if (cmd.equals(EMPTY)) {
                cmd = in.readLine();
                continue;
            }
            if (cmd.equalsIgnoreCase(RUN)) {
                shouldRun = false;
                run.shouldRun = true;
                return;
            }

            if (cmd.equals(HELP)) {
                help();
                cmd = in.readLine();
                continue;
            }
            if (cmd.equals(PRINT)) {
                print();
                cmd = in.readLine();
                continue;
            }

            if (cmd.equals(EXIT)) {
                return;
            }

            List<String> commands = getCommands(cmd);
            if (commands.isEmpty()) {
                System.out.println("please try again");
                cmd = in.readLine();
                continue;
            }

            try {
                addObject(commands);
            } catch (Exception e) {
                System.out.println("The command was not accepted, please try again");
            }


            cmd = in.readLine();

        }

    }

    public void print() {
        points.forEach((s, point) -> {
            System.out.println("Point with name " + point.name + " and value " + point.value);
        });
        transitions.forEach((s, transition) -> {
            System.out.println("Transition with name " + transition.name);
        });
        arcs.forEach((s, arc) -> {
            System.out.println("Arc between " + arc.from.name + " and " + arc.to.name + " with value " + arc.value);
        });

        if (arcs.isEmpty() && transitions.isEmpty() && points.isEmpty()) {
            System.out.println("No entities added yet!");
        }
    }

    private void addObject(List<String> commands) throws Exception {
        if (P.equals(commands.get(0))) {
            String name = commands.size() >= 3 ? commands.get(2) : null;
            int val = commands.size() >= 2 ? Integer.valueOf(commands.get(1)) : 1;
            addPoint(name, val);
        } else if (T.equals(commands.get(0))) {
            String name1 = commands.size() == 2 ? commands.get(1) : T + currentTransition;
            addTransition(name1);
        } else if (A.equals(commands.get(0))) {
            if (commands.size() < 3) {
                throw new Exception();
            }
            int val1 = commands.size() >= 4 ? Integer.valueOf(commands.get(3)) : 1;
            String name2 = commands.size() >= 5 ? commands.get(4) : null;
            addArc(commands.get(1), commands.get(2), val1, name2, transitions.containsKey(commands.get(2)));
        }
    }

    public void help() {
        System.out.println("Spaces are required!");
        System.out.println("P val name . val and name are optional");
        System.out.println("T name . name is optional");
        System.out.println("A from to val name . val and name are optional");
        System.out.println("Other valid commands : help , print ,run");
    }

}
