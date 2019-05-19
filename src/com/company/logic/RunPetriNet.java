package com.company.logic;

import com.company.Constants;
import com.company.entities.Transition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.company.Constants.*;
import static com.company.utils.RunUtils.canRunTransition;

public class RunPetriNet {

    public Data data;

    public void run() throws IOException {
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        String input;

        printValidTransitions();

        while (true) {
            input = consoleReader.readLine();
            switch (input) {
                case EMPTY:
                case HELP:
                    printValidTransitions();
                    break;
                case PRINT:
                    data.print();
                    break;
                default:

            }
            if (input.equals(EXIT)) {
                return;
            }

            Transition transition = data.transitions.get(input);
            if (!canRunTransition(transition)) {
                printValidTransitions();
                continue;
            }

            transition.runTransition();
            printValidTransitions();
        }

    }

    public void printValidTransitions() {
        System.out.println("Please provide a valid transition");
        System.out.print(" Valid transitions:");
        data.transitions.forEach((s, transition) -> {
            if (canRunTransition(transition)) {
                System.out.print(s + " ,");
            }
        });
        if (data.transitions.entrySet().stream().noneMatch(transitionEntry -> canRunTransition(transitionEntry.getValue()))) {
            System.out.println("NONE : please  exit");
        }
        System.out.println("\n\n");
    }
}
