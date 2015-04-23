package utils;

import sample.intercls.ICalculator;

/**
 * Created by duongnapham on 3/5/15.
 */
public class Calculator implements ICalculator{

    @Override
    public int sum(int a, int b) {
        return a + b;
    }

    @Override
    public int subtraction(int a, int b) {
        return a - b;
    }

    @Override
    public int multiplication(int a, int b) {
        return a * b;
    }

    @Override
    public int divison(int a, int b) throws Exception {
        if (b == 0) {
            throw new Exception("Divider can't be zero");
        }

        return a / b;
    }

    @Override
    public boolean equalIntegers(int a, int b) {
        boolean result = false;

        if (a == b) {
            result = true;
        }

        return result;
    }

}
