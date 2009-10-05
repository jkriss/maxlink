package jk;

import maxlink.AtomUtils;
import maxlink.LinkConstants;

import com.cycling74.max.*;
import com.cycling74.net.MultiReceiver;
import com.cycling74.net.MultiSender;

public class spy extends MaxObject{

	MultiSender ms = null;
	MultiReceiver mr = null;
	
	spy() {
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
	}

	public void processInput(Atom[] a) {
		this.outlet(0, AtomUtils.AtomListToString(a));
	}
	
	protected void notifyDeleted() {
		// called when the object is deleted
		if (mr != null) mr.close();
	}
	
}
