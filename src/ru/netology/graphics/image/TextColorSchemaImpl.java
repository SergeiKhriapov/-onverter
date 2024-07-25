package ru.netology.graphics.image;

public class TextColorSchemaImpl implements TextColorSchema {
    private final static char[] GRADIENT_BRIGHTNESS = {'#', '$', '@', '%', '*', '+', '-'};

    @Override
    public char convert(int color) {
        double scaledValue = (color / 255.0) * (GRADIENT_BRIGHTNESS.length - 1);
        int index = (int) Math.round(scaledValue);
        return GRADIENT_BRIGHTNESS[index];
    }
}