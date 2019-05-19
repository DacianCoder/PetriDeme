package com.company.entities;

import java.util.ArrayList;
import java.util.List;

public class Transition extends Unit {

    public List<Arc> enterArcs = new ArrayList<>();
    public List<Arc> leavingArcs = new ArrayList<>();


    public void runTransition() {
        enterArcs.forEach(arc -> ((Point) arc.from).value = ((Point) arc.from).value - arc.value);
        leavingArcs.forEach(arc -> ((Point) arc.to).value = ((Point) arc.to).value + arc.value);
    }

}
