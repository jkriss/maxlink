package maxlink;

import java.util.Stack;


import com.cycling74.max.*;
import com.cycling74.net.*;

public class MaxLink {

	private Object parent;
	private MultiSender ms = null;
	private MultiReceiver mr = null;
	private String name;
	private String P5LinkName;

	private static final int MAX_FUNCTIONS = 100;
	private MaxFunction functions[] = new MaxFunction[MAX_FUNCTIONS];
	private int nFunctions = 0;

	private static final int MAX_FIELDS = 100;
	private MaxField fields[] = new MaxField[MAX_FIELDS];
	private int nFields = 0;

	private MaxField inlets[] = new MaxField[LinkConstants.MAX_INLETS];
	private int nInlets = 0;
	private int nOutlets = 0;
	
	public static final byte LOCAL = (byte)0;
	public static final byte SUBNET = (byte)1;

	public MaxLink(Object parent) {
		// name = the class name
		// (if run within the p5 ide, it'll use the temporary class name)
		this(parent, parent.getClass().getName());
	}

	public MaxLink(Object parent, String name) {

		this.parent = parent;
		this.name = name;
		this.P5LinkName = name + LinkConstants.MAX_SUFFIX;

		//System.out.println("creating link for " + name);

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
		
		announceIOInfo();
		System.out.println("initialized maxlink");
	}

	public void stop() {
		//System.out.println("thread stopped");
		mr.close();
		mr = null;
	}
	
	public void setScope(int ttl) {
		ms.setTimeToLive(ttl);
	}

	public void declareMaxFunction(String functionName) {
		// declare a function (with no parameters) to be accessible via max
		//System.out.println("declared new function: " + functionName);
		functions[nFunctions++] = new MaxFunction(parent, functionName);
	}

	public void declareMaxFunction(String functionName, Class types[]) {
		// declares a function (with arguments) to be accessible via max
		functions[nFunctions++] = new MaxFunction(parent, functionName, types);
	}

	public void declareMaxField(String fieldName) {
		// declares a field to be accessible via max
		fields[nFields++] = new MaxField(parent, fieldName);
	}

	public void declareMaxField(String fieldName, String functionName) {
		// declares a field to be visible to max, but calls the parent object's
		// getterFunctionName to get the value
		fields[nFields++] = new MaxField(parent, fieldName, functionName);
	}

	public void declareInlet(String varName) {
		declareInlet(varName, null);
	}

	public void declareInlet(String varName, String functionName) {
		// map a variable to an inlet
		MaxField newField = new MaxField(parent, varName, functionName);
		// System.out.println("adding field");
		inlets[nInlets++] = newField;
		//System.out.println("announcing inlet " + (nInlets-1) + ", " + newField.toString());
		announceNewInlet(nInlets - 1, newField);
	}

	/*
	public void declareOutlet(Class type) {
		nOutlets++;
		announceNewOutlet(nOutlets - 1, type);
	}
	*/

	public void output(String s) {
		// send to default outlet if outletNum is unspecified
		output(0, s);
	}

	public void output(int i) {
		output(0, i);
	}

	public void output(float f) {
		output(0, f);
	}

	public void output(int outletNum, String s) {
		//System.out.println("sending string: '" + s + "'");
		sendToMax(new Atom[] { Atom.newAtom(outletNum), Atom.newAtom(s) });
	}

	public void output(int outletNum, int i) {
		sendToMax(new Atom[] { Atom.newAtom(outletNum), Atom.newAtom(i) });
	}

	public void output(int outletNum, float f) {
		sendToMax(new Atom[] { Atom.newAtom(outletNum), Atom.newAtom(f) });
	}

	public void processInput(Atom[] atoms) {

		//System.out.println("received input: " + AtomUtils.AtomListToString(atoms));
		//System.out.println(name + " received input");

		Stack args = AtomUtils.makeStack(atoms);

		Atom firstArg = (Atom) args.pop();

		//System.out.println("first atom: " + firstArg.toString());
		
		if (firstArg.toString().equals(name)) {

			Atom atom = (Atom) args.pop();
			String atomString = atom.toString();

			//System.out.println("MaxLink input: " + atomString);
	
			if (atom.isInt()) {
				int inletNum = atom.getInt();
				atom = (Atom) args.pop();
				atomString = atom.toString();
				if (isFunction(atomString) && inletNum == 0) {
					// if it's a function (in the first inlet?), call it.
					MaxFunction function = getFunction(atomString);
					Atom[] otherArgs = new Atom[args.size()];
					args.copyInto(otherArgs);
					function.call(otherArgs);
				} else if (inletNum < nInlets){
					// if it's a field, set the new value
					// MaxField field = getField(secondArg);
					// field.setField(args.pop());
					// if it's an input through an inlet, set that field
					atom = AtomUtils.condense(atom, AtomUtils.makeAtomList(args));
					inlets[inletNum].setField(atom.toObject());
				} else {
					System.out.println("error: bad input");
				}
			} else if (atomString.equals(LinkConstants.IO_QUERY_MESSAGE)) {
				announceIOInfo();
			} else {
				System.out.println("unknown input: " + atom.getString());
			}

		} else if (firstArg.toString().equals(LinkConstants.WHO_QUERY_MESSAGE)) {
			// respond to "who" query by sending name, nInlets, nOutlets
			announceApplet();
			announceIOInfo();
		}
		// should try multicasting osc...need Atom->OSC conversion
	}

	private boolean isFunction(String functionName) {
		// see if function is declared
		if (functionName == null)
			return false;
		for (int i = 0; i < nFunctions; i++) {
			if (functions[i].getFunctionName().equals(functionName))
				return true;
		}
		return false;
	}

	private MaxFunction getFunction(String functionName) {
		for (int i = 0; i < nFunctions; i++) {
			if (functions[i].getFunctionName().equals(functionName))
				return functions[i];
		}
		return null;
	}

	protected void announceApplet() {
		announce(LinkConstants.ANNOUNCE_APPLET_MESSAGE, new Atom[] {
				Atom.newAtom(name), Atom.newAtom(nInlets),
				Atom.newAtom(nOutlets) });
	}

	private void announceNewInlet(int inletNum, MaxField field) {
		// sends "<P5LinkName> <new inlet message> <inlet num> <field name>
		// <field type> <field type name>"
		sendToMax(new Atom[] { Atom.newAtom(LinkConstants.NEW_INLET_MESSAGE),
				Atom.newAtom(inletNum), Atom.newAtom(field.getFieldName()),
				Atom.newAtom(getDataType(field.getFieldClass().getName())),
				Atom.newAtom(field.getFieldClass().getName())});
	}

//	private void announceNewOutlet(int outletNum, Class type) {
//		// sends "<P5LinkName> <new outlet message> <outlet num> <outlet type>"
//		sendToMax(new Atom[] { Atom.newAtom(LinkConstants.NEW_OUTLET_MESSAGE),
//				Atom.newAtom(outletNum),
//				Atom.newAtom(getDataType(type.getName())) });
//	}

	private int getDataType(String typeName) {
		int datatype;
		if (typeName.indexOf("integer") != -1) {
			datatype = DataTypes.INT;
		} else if (typeName.indexOf("float") != -1) {
			datatype = DataTypes.FLOAT;
		} else {
			datatype = DataTypes.MESSAGE;
		}
		return datatype;
	}

	private void announceIOInfo() {
		for (int i = 0; i < this.nInlets; i++)
			announceNewInlet(i, inlets[i]);
	}

	private void sendToMax(Atom[] a) {
		// send to corresponding P5Link object in max
		ms.send(P5LinkName, a);
	}

	private void announce(String announcementType, Atom[] a) {
		// broadcast message
		ms.send(announcementType, a);
	}

}