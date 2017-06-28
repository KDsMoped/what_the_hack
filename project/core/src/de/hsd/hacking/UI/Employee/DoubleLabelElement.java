package de.hsd.hacking.UI.Employee;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import de.hsd.hacking.Utils.Constants;

public class DoubleLabelElement extends Table {

    Label valueLabel;
    StringProvider value;

    private boolean modifiable;

    public DoubleLabelElement(String category, StringProvider value) {

        modifiable = true;
        Init();
        SetNameLabel(category);

        valueLabel = new Label(value.get(), Constants.LabelStyle());
        this.value = value;

        add(valueLabel).right().padLeft(10);
    }

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

    public interface StringProvider {
        String get();
    }
}
