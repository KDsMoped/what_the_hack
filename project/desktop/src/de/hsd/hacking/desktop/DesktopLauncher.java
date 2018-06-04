package de.hsd.hacking.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import de.hsd.hacking.HackingGame;
import de.hsd.hacking.Utils.Constants;

/**
 * Entry point for desktop application.
 *
 * @author Florian
 */
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowSizeLimits(Constants.APP_WIDTH, Constants.APP_HEIGHT, Constants.APP_WIDTH, Constants.APP_HEIGHT);
		new Lwjgl3Application(new HackingGame(), config);
	}
}
