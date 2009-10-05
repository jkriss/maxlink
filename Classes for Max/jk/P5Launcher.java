package jk;
import java.applet.Applet;
import java.awt.*;
//import java.awt.event.*;

//public class P5Launcher extends Applet implements KeyListener{
public class P5Launcher extends Applet {

	// DisplayMode oldDisplayMode;

	String appletName;
	Applet applet;
	// GraphicsDevice gd;
	Frame frame;

	// can add a key listener for this so it toggles fullscreen
	// on escape key
	
	public void load(String appletName) {
		this.appletName = appletName;
		try {
			frame = new Frame();
			Class c = Class.forName(appletName);
			// System.out.println("found class \"" + c.getName() + "\"");
			applet = (Applet) c.newInstance();
			// System.out.println("created applet instance");
			
			try {
				applet.init();
				applet.start();
			} catch (Exception e){
				e.printStackTrace();
			}
			// System.out.println("started applet");

			//Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

			//frame.setUndecorated(false);

			frame.setLayout(new BorderLayout());
			frame.add(applet, BorderLayout.CENTER);
			//frame.addKeyListener(this);
			frame.pack();

			frame.show();
			applet.requestFocus(); // get keydowns right away

			/*
			frame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					// disable window closing -- have to delete the max object
				}
			});
			*/
			
			//gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		} catch (Exception e) {
			System.out.println("jk.p5: couldn't find p5 sketch '" + appletName + "'");
			//e.printStackTrace();
		}
	}

	public void stop() {
		if (applet != null) applet.stop();
	}
	
	public Applet getApplet() {
		return applet;
	}

	public Frame getFrame() {
		return frame;
	}

	/*
	public void toggleFullscreen() {
		if (oldDisplayMode == gd.getDisplayMode()) {
			setFullscreen(true);
		} else {
			setFullscreen(false);
		}
	}
	
	public void setFullscreen(boolean goFullscreen) {
		
		frame.dispose();
		
		//	 get default screendevice and configuration
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		DisplayMode newDisplayMode = new DisplayMode(applet.g.width,
				applet.g.height, 32, DisplayMode.REFRESH_RATE_UNKNOWN);

		if (goFullscreen) {

			oldDisplayMode = gd.getDisplayMode();

			try {
				gd.setFullScreenWindow(frame);
				gd.setDisplayMode(newDisplayMode);

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			gd.setDisplayMode(oldDisplayMode);
			gd.setFullScreenWindow(null);
		}

		load(appletName);
	
	}
	*/
	/*
	public void keyPressed(KeyEvent e) {
		//if (e.getKeyCode() == KeyEvent.VK_ESCAPE) toggleFullscreen();
	}
	*/
	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	/*
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	*/
	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	/*
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	*/
}
