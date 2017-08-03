package de.hsd.hacking;

import com.badlogic.gdx.Game;
import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.SaveGameManager;
import de.hsd.hacking.Screens.ScreenManager;

public class HackingGame extends Game {

	private Assets assets;
	
	@Override
	public void create () {
		assets = new Assets();
		assets.load();
		GameManager.instance();

		ScreenManager.initialize(this);
		ScreenManager.setMenuScreen();
	}

	public Assets getAssets(){
		return assets;
	}

    public void pause () {
		if (ScreenManager.isGameRunning())
        	SaveGameManager.SaveGame();
    }

    public void resume () {
        
    }

    public void dispose () {
		assets.dispose();
    }
}
