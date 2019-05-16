package com.company;

import com.company.logic.Run;
import com.company.logic.Store;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Store store = new Store();
        Run run = new Run();
        run.store = store;

        while (true) {
            store.run(run);
            run.run();
        }
    }
}
