package de.hsd.hacking;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.DataLoader;
import de.hsd.hacking.Data.SaveGameManager;
import de.hsd.hacking.Screens.ScreenManager;

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
        SaveGameManager.LoadGame();
    }

    public void dispose () {
    }
}
