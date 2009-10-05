package jk;
import java.awt.Frame;
import com.cycling74.max.*;

public class p5 extends link {

	P5Launcher launcher;
	Frame frame;

	p5(Atom args[]) {
		super(args);
		load(args);
		//focus();
	}

	void load(Atom[] a) {
		if (a.length == 0) {
			post("couldn't load p5 sketch");
		} else {
			if (frame != null) frame.dispose();
			launcher = new P5Launcher();
			launcher.load(a[0].getString());
			frame = launcher.getFrame();
		}
	}

	public void focus() {
		frame.toFront();
	}

	/*
	public void fullscreen(Atom[] a) {
		if (a[0].toInt() == 1) {
			launcher.setFullscreen(true);
		} else {
			launcher.setFullscreen(false);
		}
	}
	*/

	public void processInput(Atom a[]) {
		super.processInput(a);
	}

	protected void notifyDeleted() {
		launcher.stop();
		frame.dispose();
		super.notifyDeleted();
	}
}