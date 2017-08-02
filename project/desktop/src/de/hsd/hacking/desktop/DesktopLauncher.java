package de.hsd.hacking.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.hsd.hacking.HackingGame;
import de.hsd.hacking.Utils.Constants;

/**
 * Entry point for desktop application.
 *
 * @author Florian
 */
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Constants.APP_WIDTH;
		config.height = Constants.APP_HEIGHT;
		new LwjglApplication(new HackingGame(), config);
	}
}
