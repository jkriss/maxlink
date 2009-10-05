package jk;
import java.util.Stack;

import maxlink.AtomUtils;
import maxlink.LinkConstants;

import com.cycling74.max.*;
import com.cycling74.net.MultiReceiver;
import com.cycling74.net.MultiSender;

public class link extends MaxObject {

	protected MultiSender ms = null;
	protected MultiReceiver mr = null;
	
	protected String name;
	protected String targetName;
	
	protected int[] inlets = new int[LinkConstants.MAX_INLETS];
	protected int nInlets;
	
	protected int[] outlets = new int[LinkConstants.MAX_OUTLETS];
	protected int nOutlets;
	
	link(Atom args[]) {
		
		if (args.length == 1) {
			nInlets = 1; 
			nOutlets = 0;
			init(args[0].getString(), this);
		} else if (args.length == 3) {
			nInlets = args[1].getInt();
			nOutlets = args[2].getInt();
			init(args[0].getString(), this);
		} else {
			post("usage: P5Link <applet name> [<nInlets> <nOutlets>]");
		}
	}
	
	public void init(String targetName, Object callbackObj) {
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
		
		this.targetName = targetName;
		this.name = targetName + LinkConstants.MAX_SUFFIX;

		declareIO(nInlets, nOutlets);
		
		// query inlets in case the sketch is already running
		// sendToP5(new Atom[] {Atom.newAtom(LinkConstants.IO_QUERY_MESSAGE)});
	}
	
	public void inlet(float f) {
		// send to the applet
		sendToP5(new Atom[] {
				Atom.newAtom(getInlet()),
				Atom.newAtom(f)
		});
	}
	
	public void inlet(int i) {
		// send to the applet
		sendToP5(new Atom[] {
				Atom.newAtom(getInlet()),
				Atom.newAtom(i)
		});
	}
	
	public void anything(String s, Atom[] args) {
		
		/*
		// just pass it through to the object
		Atom[] newargs = new Atom[args.length+2];
		newargs[0] = Atom.newAtom(getInlet());
		newargs[1] = Atom.newAtom(s);
		System.arraycopy(args,0,newargs,2,args.length);
		//ms.send(targetName, newargs);
		sendToP5(newargs);
		*/
		
		// for now, assume it's a string and concatenate all atoms
		Atom a = AtomUtils.condense(Atom.newAtom(s), args);
		sendToP5(new Atom[] {Atom.newAtom(getInlet()), a});
	}
	
	private void sendToP5(Atom[] a) {
		// send to corresponding MaxLink object
		ms.send(targetName, a);
	}
	
	public void processInput(Atom atoms[]) {
		
		Stack args = AtomUtils.makeStack(atoms);

		Atom firstArg = (Atom)args.pop();
		
		if (firstArg.toString().equals(name)) {
			Atom atom = (Atom)args.pop();
			String secondArg = atom.toString();

			if (atom.isInt()) {
				// it's a request to send something out of an outlet
				int outletNum = atom.getInt();

				// send the remaining arguments to the appropriate outlet
				Atom[] otherArgs = AtomUtils.makeAtomList(args);
				if (outletNum < nOutlets+1) outlet(outletNum, otherArgs);
			} else if (secondArg.equals(LinkConstants.NEW_INLET_MESSAGE)) {
				// <inlet num> <field name> <field type> <field type name>
				int inletNum = ((Atom)args.pop()).getInt();
				String fieldName = args.pop().toString();
				setInletAssist(inletNum, fieldName);
			} else if (secondArg.equals(LinkConstants.NEW_OUTLET_MESSAGE)) {
				// <inlet num> <inlet label>
				int inletNum = ((Atom)args.pop()).getInt();
				String inletLabel = args.pop().toString();
				declareOutlets(new int[] {DataTypes.ANYTHING});
				setOutletAssist(inletNum, inletLabel);
			}
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
