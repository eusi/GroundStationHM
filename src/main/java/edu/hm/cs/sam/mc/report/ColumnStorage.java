package edu.hm.cs.sam.mc.report;

import java.util.ArrayList;
import java.util.List;

class ColumnStorage {

    private final List<String> storage;
    private int longestElementLength;

    public ColumnStorage() {
        storage = new ArrayList<String>();
        longestElementLength = 0;
    }

    void add(final String element) {
        if (element.length() > longestElementLength) {
            longestElementLength = element.length();
        }
        storage.add(element);
    }

    String get(final int index) {
        return storage.get(index);
    }

    int getLongestElementLength() {
        return longestElementLength;
    }
}
