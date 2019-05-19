package com.company.logic;

import com.company.entities.Arc;
import com.company.entities.Point;
import com.company.entities.Transition;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.company.Constants.*;
import static com.company.utils.RunUtils.getCommands;

public class Data {
    private final String P = "P";
    private final String T = "T";
    private final String A = "A";

    private Map<String, Point> points = new HashMap<>();
    Map<String, Transition> transitions = new HashMap<>();
    private Map<String, Arc> arcs = new HashMap<>();

    private int currentPoint = 0;
    private int currentTransition = 0;
    private int currentArc = 0;


    private void addPoint(String name, int val) {
        Point point = new Point();
        point.value = val;
        point.name = name == null ? P + currentPoint : name;

        points.put(point.name, point);
        currentPoint++;
    }

    private void addTransition(String name) {
        Transition transition = new Transition();
        transition.name = name == null ? T + currentTransition : name;

        transitions.put(name, transition);
        currentTransition++;
    }

    private void addArc(String fromName, String toName, int val, String name, boolean toTranstion) throws Exception {
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

    public int run() throws IOException {
        help();

        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        String input;

        while (true) {
            input = consoleReader.readLine();
            switch (input) {
                case EMPTY:
                case HELP:
                    help();
                    continue;
                case PRINT:
                    print();
                    continue;
                default:

            }
            if (input.equalsIgnoreCase(RUN)) {
                return 1;
            }

            if (input.equals(EXIT)) {
                return 0;
            }

            List<String> commands = getCommands(input);
            if (commands.isEmpty()) {
                System.out.println("please try again");
                continue;
            }

            try {
                addUnit(commands);
            } catch (Exception e) {
                System.out.println("The command was not accepted, please try again");
                help();
            }

        }

    }

    void print() {
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

    private void addUnit(List<String> commands) throws Exception {
        switch (commands.get(0)) {
            case P:
                String name = commands.size() >= 3 ? commands.get(2) : null;
                int val = commands.size() >= 2 ? Integer.valueOf(commands.get(1)) : 1;
                addPoint(name, val);
                break;
            case T:
                String name1 = commands.size() == 2 ? commands.get(1) : T + currentTransition;
                addTransition(name1);
                break;
            case A:
                if (commands.size() < 3) {
                    throw new Exception();
                }
                int val1 = commands.size() >= 4 ? Integer.valueOf(commands.get(3)) : 1;
                String name2 = commands.size() >= 5 ? commands.get(4) : null;
                addArc(commands.get(1), commands.get(2), val1, name2, transitions.containsKey(commands.get(2)));
                break;
            default:
                throw new Exception();
        }
    }

    public void help() {
        System.out.println("\n-------------------------------------------------------------------------------------------\n");
        System.out.println("Spaces are required! Names are case sensitive");
        System.out.println(P + " value name------------------------- value and name are optional (using internal counter and value =1)");
        System.out.println(T + " name      ------------------------ name is optional(using internal counter)");
        System.out.println(A + " from to value name  --------------- value and name are optional (using internal counter and value =1)");
        System.out.println(String.format("Other valid commands : %s,%s, %s %s", PRINT, HELP, RUN, EXIT));
        System.out.println("\n-------------------------------------------------------------------------------------------\n");
    }

}
