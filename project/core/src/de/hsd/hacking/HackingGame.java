package de.hsd.hacking;

import com.badlogic.gdx.Game;
import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.SaveGameManager;
import de.hsd.hacking.Screens.ScreenManager;
import de.hsd.hacking.Utils.Constants;

public class HackingGame extends Game {

	private Assets assets;
	
	@Override
	public void create () {
		assets = new Assets();
		assets.load();

        SaveGameManager.LoadGame();

		ScreenManager.initialize(this);
		ScreenManager.setMenuScreen();
	}

	public Assets getAssets(){
		return assets;
	}

    public void pause () {
        SaveGameManager.SaveGame();
    }

    public void resume () {
        
    }

    public void dispose () {
		assets.dispose();
    }
}
