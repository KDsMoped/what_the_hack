package de.hsd.hacking.UI;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import de.hsd.hacking.Utils.Constants;
import de.hsd.hacking.Utils.Provider.StringProvider;

/**
 * A DoubleLabel displays two strings where the first on is aligned to the left and the second is aligned to the right.
 */
public class DoubleLabelElement extends Table {

    private Label valueLabel;
    private StringProvider value;

    private boolean modifiable;

    /**
     * Creates a DoubleLabel that displays a fixed string and the dynamic value given by the StringProvider object.
     *
     * @param category
     * @param value
     */
    public DoubleLabelElement(String category, StringProvider value) {

        modifiable = true;
        Init();
        SetNameLabel(category);

        valueLabel = new Label(value.get(), Constants.LabelStyle());
        this.value = value;

        add(valueLabel).right().padLeft(10);
    }

    /**
     * Creates a DoubleLabel that displays two fixed strings.
     *
     * @param category
     * @param value
     */
    public DoubleLabelElement(String category, String value) {

        modifiable = false;
        Init();
        SetNameLabel(category);

        valueLabel = new Label(value, Constants.LabelStyle());

        add(valueLabel).right().padLeft(10);
    }

    private void Init(){
        setTouchable(Touchable.enabled);
        align(Align.top);
    }

    private void SetNameLabel(String text){
        Label name = new Label(text, Constants.LabelStyle());
        add(name).expandX().fillX().left();
    }

    @Override
    public void act(float delta) {
        if(modifiable) valueLabel.setText(value.get());

        super.act(delta);
    }
}
