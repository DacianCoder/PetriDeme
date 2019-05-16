package com.company.utils;

import com.company.entities.Point;
import com.company.entities.Transition;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RunUtils {


    public static boolean canRunTransition(Transition transition) {
        return transition != null && !transition.enterArcs.isEmpty() &&
                transition.enterArcs.stream().allMatch(arc -> ((Point) arc.from).value >= arc.value);
    }

    public static List<String> getCommands(String x) {
        return Arrays.stream(x.split(" ")).collect(Collectors.toList());
    }
}
