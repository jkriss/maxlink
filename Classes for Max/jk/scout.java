
package jk;
import java.util.Stack;

import maxlink.AtomUtils;
import maxlink.LinkConstants;


import com.cycling74.max.*;
import com.cycling74.net.*;

public class scout extends MaxObject{
	
	private MultiSender ms = null;
	private MultiReceiver mr = null;
	
	private static final int MAX_OBJECTS = 300;
	private String[] objects = new String[MAX_OBJECTS];
	int nObjects = 0;
	
	private static int spacer = 5;
	private static int defaultWidth = 150;
	private static int initialSpacer = 15;
	
	int firstX;
	int currentX;
	int currentY;

	scout() {
		ms = new MultiSender();
		ms.setGroup(LinkConstants.MAXGROUP);
		ms.setPort(LinkConstants.MAXPORT); 
		ms.setDebugString(LinkConstants.OBJECTNAME);
		ms.setTimeToLive(LinkConstants.TIMETOLIVE);
		mr = new MultiReceiver();
		mr.setPort(LinkConstants.MAXPORT);
		mr.join(LinkConstants.MAXGROUP);
		mr.setDebugString(LinkConstants.OBJECTNAME);
		mr.setCallback(this, "processInput");
		
		setPositionVars();
	}

	private void setPositionVars() {
		// position new objects based on this object's location
		int[] rect = this.getMaxBox().getRect();
		firstX = rect[2] + initialSpacer;
		currentX = firstX;
		currentY = rect[1];
	}
	
	public void bang() {
		//post("this scout is bangin'!");
	}
	
	public void refresh() {
		//post("looking for objects");
		broadcast(LinkConstants.WHO_QUERY_MESSAGE);
	}
	
	public void clear() {
		setPositionVars();
		for (int i=0; i<nObjects; i++) {
			outlet(0,"script",new Atom[] {
					Atom.newAtom("delete"),
					Atom.newAtom(objects[i] + LinkConstants.OBJECT_SUFFIX)
			});
		}
		nObjects = 0;
		currentX = firstX;
	}
	
	private void broadcast(String s) {
		ms.send(new Atom[]{Atom.newAtom(s)});
	}
	
	private void createP5Link(String appletName, int nInlets, int nOutlets) {
		//post("creating new p5 link (" + appletName + ", " + nInlets + ", " + nOutlets + ")");
		objects[nObjects++] = appletName;
		
//		MaxPatcher p = this.getParentPatcher();
//		int[] rect = this.getMaxBox().getRect();
//		int thisWidth = rect[2] - rect[0];

		String objectName = appletName + LinkConstants.OBJECT_SUFFIX;
		// script new myobj newex 102 242 58 9109513 mxj P5Link max_com_test 4 1
		
		Atom[] message = new Atom[] {
				Atom.newAtom("new"),
				Atom.newAtom(objectName),
				Atom.newAtom("newex"),
				Atom.newAtom(currentX),
				Atom.newAtom(currentY),
				//Atom.newAtom(58),
				Atom.newAtom(defaultWidth),
				Atom.newAtom(9109513),
				Atom.newAtom("mxj"),
				Atom.newAtom("jk.link"),
				Atom.newAtom(appletName),
				Atom.newAtom(nInlets),
				Atom.newAtom(nOutlets)
		};

		currentX += defaultWidth+spacer+spacer;
		//String messageString = AtomUtils.AtomListToString(message);
		outlet(0,"script",message);
		
		/*
		MaxBox newBox = p.newDefault(rect[0]+thisWidth+spacer, rect[1], "newex", 
				new Atom[] {
				Atom.newAtom("mxj"),
				Atom.newAtom("P5Link"),
				Atom.newAtom(appletName),
				Atom.newAtom(nInlets),
				Atom.newAtom(nOutlets)});
		*/
	}
	
	private boolean isNewObject(String name) {
		// check the patcher and see if the object exists
		MaxBox[] objects = this.getMaxBox().getPatcher().getAllBoxes();
		//post("scout sees " + objects.length + " objects in its patcher");
		for (int i=0; i<objects.length; i++) {
			if (objects[i].getName() != null) {
				//post("obj: " + objects[i].getName());
				if (objects[i].getName().equals(name + LinkConstants.OBJECT_SUFFIX)) return false;
			}
		}
		
		//post("new object: " + name);
		return true;
	}
	
	public void processInput(Atom atoms[]) {
		
		Stack args = AtomUtils.makeStack(atoms);
		Atom atom = (Atom)args.pop();
		
		if (atom.toString().equals(LinkConstants.ANNOUNCE_APPLET_MESSAGE)) {
			
			// check this against the list of previously
			// spotted elements
			
			// create new P5Link to talk to it
			String appletName = args.pop().toString();
			int nInlets = ((Atom)args.pop()).getInt();
			int nOutlets = ((Atom)args.pop()).getInt();
			
			//post("scout sees " + appletName + " with " + nInlets + " inlets " + 
			//		" and " + nOutlets + " outlets");
			
			if (isNewObject(appletName)) createP5Link(appletName, nInlets, nOutlets);
		} 
	}
	
	protected void notifyDeleted() {
		// called when the object is deleted
		if (mr != null) {
			mr.close();
			mr = null;
		}
	}
}
