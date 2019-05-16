package com.company.logic;

import com.company.entities.Transition;
import com.company.utils.RunUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicReference;

import static com.company.Constants.*;
import static com.company.utils.RunUtils.canRunTransition;

public class Run {

    public Store store;
    public boolean shouldRun = false;

    public void run() throws IOException {
        if (!shouldRun) {
            return;
        }
        java.io.BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String cmd = "";

        while (shouldRun) {
            if (cmd.equals("")) {
                System.out.println("please provide a valid transition");
                printValidTransitions();
                cmd = in.readLine();
                continue;
            }
            if (cmd.equals(EXIT)) {
                return;
            }
            if (cmd.equals(HELP)) {
                printValidTransitions();
                cmd = in.readLine();
                continue;
            }
            if (cmd.equals(PRINT)) {
                store.print();
                cmd = in.readLine();
                continue;
            }
            Transition transition = store.transitions.get(cmd);
            if (transition == null && !canRunTransition(transition)) {
                System.out.println("please provide a valid transition");
                printValidTransitions();
                cmd = in.readLine();
                continue;
            }

            transition.runTransition();
            cmd = in.readLine();

            printValidTransitions();
        }

    }

    public void printValidTransitions() {
        AtomicReference<String> x = new AtomicReference<>("");
        System.out.print(" valid transitions:");
        store.transitions.forEach((s, transition) -> {
            if (RunUtils.canRunTransition(transition)) {
                System.out.print(s + " ,");
                return;
            }
            x.set("NONE");
        });
        System.out.println(x.get());
    }
}
