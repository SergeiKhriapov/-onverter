package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class TextGraphicsConverterImpl implements TextGraphicsConverter {
    private int maxHeight;
    private int maxWidth;
    private double maxRatio;
    private TextColorSchema schema = new TextColorSchemaImpl();


    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage image;
        try {
            image = ImageIO.read(new URL(url));
            System.out.println("Изображение успешно загружено!");
        } catch (IOException e) {
            System.err.println("Не удалось загрузить изображение!");
            throw new IOException("Failed to download image");
        }
        int width = image.getWidth();
        int height = image.getHeight();

        double ratioWidthByHeight = (double) width / height;
        double ratioHeightByWidth = (double) height / width;
        if (maxRatio < ratioWidthByHeight) {
            throw new BadImageSizeException(ratioWidthByHeight, maxRatio);
        }
        if (maxRatio < ratioHeightByWidth) {
            throw new BadImageSizeException(ratioHeightByWidth, maxRatio);
        }

        double reductionIndex = Math.max((double) height / maxHeight, (double) width / maxWidth);
        width = (int) (width / reductionIndex);
        height = (int) (height / reductionIndex);

        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();
        WritableRaster bwRaster = resizedImage.getRaster();
        StringBuilder pictureOfSymbols = new StringBuilder();
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                int color = bwRaster.getPixel(w, h, new int[1])[0];
                pictureOfSymbols.append(schema.convert(color));
            }
            pictureOfSymbols.append('\n');
        }
        return pictureOfSymbols.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;

    }

}
