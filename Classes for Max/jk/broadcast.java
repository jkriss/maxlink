package jk;

import java.util.Stack;

import maxlink.AtomUtils;
import maxlink.LinkConstants;

import com.cycling74.max.*;
import com.cycling74.net.*;

public class broadcast extends MaxObject {

	protected MultiSender ms = null;

	protected MultiReceiver mr = null;

	private static int MAX_INLETS = 50;

	int inlets[] = new int[MAX_INLETS];

	int nInlets = 0;

	int nOutlets = 0;

	String name;

	String targetName;

	broadcast(Atom args[]) {
		if (args.length > 0) {
			init(args);
		} else {
			post("jk.broadcast: need a name to broadcast under");
		}
	}

	private void init(Atom a[]) {

		ms = new MultiSender();
		ms.setGroup(LinkConstants.MAXGROUP);
		ms.setPort(LinkConstants.MAXPORT);
		ms.setDebugString(LinkConstants.OBJECTNAME);
		ms.setTimeToLive(LinkConstants.TIMETOLIVE);

		mr = new MultiReceiver(LinkConstants.MAXGROUP, LinkConstants.MAXPORT,
				this, "processInput");
		//"processNothing");

		Stack args = AtomUtils.makeStack(a);
		Atom atom = (Atom) args.pop();

		name = atom.toString();
		targetName = name + LinkConstants.MAX_SUFFIX;
		// first arg is the send name, rest are inlet names,
		// so a.length - 1 is the number of inlets

		// yeah. this looks backwards. that's 'cause inlets are outlets
		// and vice versa on the receiving end
		nOutlets = args.size();
		declareIO(nOutlets, 0);

		// declare label for each one
		for (int i = 0; i < nOutlets; i++) {
			//maxLink.declareInlet(args.pop().toString());
		}
	}

	public void inlet(float f) {
		// send to the applet
		sendToLink(new Atom[] { Atom.newAtom(getInlet()), Atom.newAtom(f) });
	}

	public void inlet(int i) {
		// send to the applet
		sendToLink(new Atom[] { Atom.newAtom(getInlet()), Atom.newAtom(i) });
	}

	public void bang() {
		sendToLink(new Atom[] { Atom.newAtom(getInlet()), Atom.newAtom("bang") });
	}

	public void anything(String s, Atom[] args) {
		// just send it off to the object
		/*
		 * Atom[] newargs = new Atom[args.length+1]; newargs[0] =
		 * Atom.newAtom(s); System.arraycopy(args,0,newargs,1,newargs.length-1);
		 * ms.send(targetName, newargs);
		 */
		Atom a = AtomUtils.condense(Atom.newAtom(s), args);
		sendToLink(new Atom[] { Atom.newAtom(getInlet()), a });
	}

	private void sendToLink(Atom[] a) {
		// send to corresponding P5Link object in max
		ms.send(targetName, a);
	}

	public void processInput(Atom[] atoms) {

		Stack args = AtomUtils.makeStack(atoms);

		Atom firstArg = (Atom) args.pop();

		if (firstArg.toString().equals(name)) {
			Atom atom = (Atom) args.pop();
			String secondArg = atom.toString();

			if (secondArg.equals(LinkConstants.IO_QUERY_MESSAGE)) {
				announceIOInfo();
			} else {
				System.out.println("unknown input: " + atom.toString());
			}

		} else if (firstArg.toString().equals(LinkConstants.WHO_QUERY_MESSAGE)) {
			// respond to "who" query by sending name, nInlets, nOutlets
			announceApplet();
			//announceIOInfo();
		} 
	}

	protected void announceApplet() {
		announce(LinkConstants.ANNOUNCE_APPLET_MESSAGE, new Atom[] {
				Atom.newAtom(name), Atom.newAtom(nInlets),
				Atom.newAtom(nOutlets) });
	}

	private void announceNewOutlet(int outletNum) {
		announce(LinkConstants.NEW_OUTLET_MESSAGE, new Atom[] {
				Atom.newAtom(outletNum), Atom.newAtom(DataTypes.ANYTHING) });
	}

	private void announceIOInfo() {
		for (int i = 0; i < nOutlets; i++) {
			announceNewOutlet(i);
		}
	}

	private void announce(String announcementType, Atom[] a) {
		// broadcast message
		ms.send(announcementType, a);
	}

	protected void notifyDeleted() {
		// called when the object is deleted
		if (mr != null) {
			//post("deleting broadcast multireceiver");
			mr.setCallback(this, "processNothing");
			mr.leaveAllGroups();
			mr.close();
			mr = null;
		}
		//super.notifyDeleted();
	}

	public void processNothing(Atom a[]) {
		//post("broadcast received input and duly ignored it.");
	}
}