package easygame20161215.eightdight;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by L on 2017/2/13.
 */
public class ImageDetail {
    static BufferedImage image = read(0.8);

    public static BufferedImage read(double k) {
        try {
            BufferedImage image = ImageIO.read(new File("2.jpg"));
            int imageWidth = ((int) (image.getWidth() * k));
            int imageHeight = ((int) (image.getHeight() * k));
            return new BufferedImage(imageWidth, imageHeight, image.getType()) {
                {
                    getGraphics().drawImage(image, 0, 0, imageWidth, imageHeight, null);
                }
            };
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    static Random random = new Random();
    public static BufferedImage read(int imageWidth, int imageHeight) {
        File[] files = new File("./").listFiles();
        int max = 0;
        for (int i = 0; i < files.length; i++) {
            String name = files[i].getName();
            if (name.endsWith(".jpg")) {
                int k = Integer.valueOf(name.substring(0, name.length() - 4));
                if (k > max) {
                    max = k;
                }
            }
        }
        int which = (random.nextInt(max + 1));
        try {
            BufferedImage image = ImageIO.read(new File(which + ".jpg"));
            return new BufferedImage(imageWidth, imageHeight, image.getType()) {
                {
                    getGraphics().drawImage(image, 0, 0, imageWidth, imageHeight, null);
                }
            };
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("which: " + which);
            System.exit(0);
            return null;
        }
    }
    public BufferedImage[] imageArray;
    int size;

    int row;
    int columns;
    public int eachWidth;
    public int eachHeight;

    public ImageDetail(int row, int columns) {
        eachWidth = image.getWidth() / columns;
        eachHeight = image.getHeight() / row;
        cut(row, columns);
    }

    public ImageDetail(int row, int columns, int eachWidth, int eachHeight) {
//        int L = Math.min(eachWidth * columns, eachHeight * row);
        image = read(eachWidth * columns, eachHeight * row);
        this.eachWidth = eachWidth;
        this.eachHeight = eachHeight;
        cut(row, columns);
    }




    private void cut(int row, int columns) {
        this.row = row;
        this.columns = columns;
        size = row * columns;
        imageArray = new BufferedImage[size];
        int k = 0;
        int sx = 0;
        int sy = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < columns; j++) {
                int finalSx = sx;
                int finalSy = sy;
                imageArray[k++] = new BufferedImage(eachWidth, eachHeight, image.getType()) {
                    {
                        getGraphics().drawImage(image, 0, 0, eachWidth, eachHeight, finalSx, finalSy, eachWidth + finalSx, eachHeight + finalSy, null);
                    }
                };
                sx += eachWidth;
            }
            sy += eachHeight;
            sx = 0;
        }
    }

    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(() -> new JFrame(){
            {
                new JFrame(){
                    {
                        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                        setSize(image.getWidth(), image.getHeight());
                        setLocationRelativeTo(null);
                        add(new JComponent() {
                            @Override
                            protected void paintComponent(Graphics g) {
                                Graphics2D d = (Graphics2D) g;
                                d.drawImage(image, 0, 0, null);
                            }
                        });
                    }
                }.setVisible(true);
                ImageDetail imageDetail = new ImageDetail(6, 6);
                setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                setSize(image.getWidth() + 20, image.getHeight() + 30);
                setLocationRelativeTo(null);
                add(new JComponent() {
                    Font one = new Font("Dialog", Font.PLAIN, 20);
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D d = (Graphics2D) g;
                        BufferedImage[] images = imageDetail.imageArray;
                        d.setColor(Color.WHITE);
//                        d.setStroke(new BasicStroke(2.0f));
                        int sx = 0;
                        int sy = 0;
                        int k = 0;

                        for (int i = 0; i < imageDetail.row; i++) {
                            for (int j = 0; j < imageDetail.columns; j++) {
                                d.drawImage(images[k++], sx, sy, null);
                                d.drawRect(sx, sy, imageDetail.eachWidth, imageDetail.eachHeight);
                                sx += imageDetail.eachWidth;
                            }
                            sy += imageDetail.eachHeight;
                            sx = 0;
                        }
                    }
                });
            }
        }.setVisible(true));
    }


    public static void f() {
        EventQueue.invokeLater(() -> new JFrame() {
            {
                setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                setSize(853, 853);
                setLocationRelativeTo(null);
                add(new JComponent() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
                    }
                });


            }
        }.setVisible(true));
    }

    public static void g() {
         /*BufferedImage img = new BufferedImage(853, 853, image.getType());
        img.getGraphics().drawImage(image, 0, 0, 853, 853, null);
        ImageIO.write(img, "jpg", new File("3.jpg"));*///重新定义图片尺寸

        int each_width = image.getWidth() / 6;
        int each_height = image.getHeight() / 6;

        BufferedImage each = new BufferedImage(each_width, each_height, image.getType());
        each.getGraphics().drawImage(image, 0, 0, each_width, each_height, 0, 0, each_width, each_height, null);


        SwingUtilities.invokeLater(() -> new JFrame() {
            {
                setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                setSize(853, 853);
                setLocationRelativeTo(null);
                add(new JComponent() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D d = (Graphics2D) g;
                        d.drawImage(each, 0, 0, null);
                    }
                });
            }
        }.setVisible(true));
    }
}
