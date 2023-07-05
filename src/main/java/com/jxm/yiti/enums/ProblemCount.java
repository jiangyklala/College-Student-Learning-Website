package com.jxm.yiti.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ProblemCount {
    FIVE(5), TEN(10), FIFTEEN(15), TWENTY(20), THIRTY(30);

    private int value;

    private ProblemCount(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
