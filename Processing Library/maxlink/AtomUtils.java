package maxlink;
import java.util.Stack;
import com.cycling74.max.*;

public class AtomUtils {
	
	public static Atom condense(Atom firstAtom, Atom[] a) {
		if (firstAtom.isString()) {
			// concatenate list of strings 
			String s = firstAtom.getString();
			for (int i=0; i<a.length; i++) {
				s += " " + a[i].toString();
			}
			Atom atom = Atom.newAtom(s);
			return atom;
		} else {
			// if it's not a string, just return the first one
			return firstAtom;
		}
	}

	public static Stack makeStack(Atom[] a) {
		// utility for creating a stack from an array
		Stack s = new Stack();
		for (int i=a.length-1; i>=0; i--) s.push(a[i]);
		return s;
	}
	
	public static Atom[] makeAtomList(Stack s) {
		Atom[] a = new Atom[s.size()];
		for (int i=0; i<a.length; i++) a[i] = (Atom)s.pop();
		return a;
	}
	
	public static Atom[] makeAtomList(String[] args) {
		Atom atoms[] = new Atom[args.length];
		for (int i=0; i<atoms.length; i++) 
			atoms[i] = Atom.newAtom(args[i]);
		return atoms;
	}
	
	public static Atom[] makeAtomList(String s) {
		String[] args = s.split(" ");
		return makeAtomList(args);
	}
	
	public static String AtomListToString(Atom[] a) {
		String s = "";
		for (int i=0; i<a.length; i++) s += a[i] + " ";
		return s;
	}
}
