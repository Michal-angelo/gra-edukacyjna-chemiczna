package pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Common;

import android.content.res.Resources;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Constants {
    public static int counter = 0;

    public static final int SCREEN_WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels;
    public static final int SCREEN_HEIGHT = Resources.getSystem().getDisplayMetrics().heightPixels;
    public static final List<Enums.Element> ELEMENT_VALUES = Collections.unmodifiableList(Arrays.asList(Enums.Element.values()));
}
