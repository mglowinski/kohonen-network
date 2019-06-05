import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

public class Images {

    public static int[][] loadImage(String path) throws IOException {
        File file = new File(path);
        BufferedImage bufferedImage = ImageIO.read(file);

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        int[][] imageArray = new int[width][height];
        Raster raster = bufferedImage.getData();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                imageArray[i][j] = raster.getSample(i, j,0);
            }
        }

        return imageArray;
    }

    public static void saveImage(int[][] imageArray, String path) throws IOException {
        BufferedImage bufferedImage =
                new BufferedImage(imageArray.length, imageArray[0].length, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster writableRaster = bufferedImage.getRaster();

        for (int i = 0; i < bufferedImage.getWidth(); i++) {
            for (int j = 0; i < bufferedImage.getHeight(); j++) {
                writableRaster.setSample(i, j, 0, imageArray[i][j]);
            }
        }

        File file = new File(path);
        ImageIO.write(bufferedImage, "PNG", file);
    }

}
