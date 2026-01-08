package util;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class AssetLoader {

    public static Image load(String path) {
        return new ImageIcon(
                Objects.requireNonNull(
                        AssetLoader.class.getResource(path)
                )
        ).getImage();
    }
}