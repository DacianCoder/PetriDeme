package com.company;

import com.company.logic.RunPetriNet;
import com.company.logic.Data;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Data data = new Data();
        RunPetriNet runPetriNet = new RunPetriNet();
        runPetriNet.data = data;

        while (true) {
            data.run();
            runPetriNet.run();
        }
    }
}
