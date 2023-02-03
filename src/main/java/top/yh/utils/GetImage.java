package top.yh.utils;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * @author yuhao
 * @date 2023/2/2
 **/
public class GetImage {
    private GetImage(){

    }
    public static Image getImage(String path) {
        return new ImageIcon(Objects.requireNonNull(GetImage.class.getResource("../image/" + path))).getImage();
    }

    public static ImageIcon getImageIcon(String path) {
        return new ImageIcon(Objects.requireNonNull(GetImage.class.getResource("../image/" + path)));
    }
}
