package maxlink;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MaxField {
	private String fieldName;
	private Field field;
	private Class fieldClass;
	private Object parent;
	private Class parentClass;
	private Method setterFunction = null;
	boolean hasFunction = false;
	
	public MaxField(Object parent, String fieldName) {
		this(parent, fieldName, null);
	}
	
	public MaxField(Object parent, String fieldName, String functionName) {
		
		this.parent = parent;
		this.parentClass = parent.getClass();
		
		this.fieldName = fieldName;
		
		try {
			// get the field object
			this.field = parentClass.getDeclaredField(fieldName);
		} catch (SecurityException e2) {
			System.err.println("MaxLink :: can't access field '" + fieldName + "'.  Is it public?\n");
			e2.printStackTrace();
		} catch (NoSuchFieldException e2) {
			System.err.println("MaxLink :: couldn't find field '" + fieldName + "'\n");
			e2.printStackTrace();
		}
		
		this.hasFunction = (functionName != null);
		// if (!hasFunction) System.out.println("creating field, no setter function");
		try {
			this.fieldClass = field.getType();
			
		// catch all nasty exceptions
		} catch (SecurityException e) {
			System.err.println("MaxLink :: can't access field '" + fieldName + "'.  Is it public?\n");
			e.printStackTrace();
		}
		
		// if it has a setter function, grab that too
		if (this.hasFunction) {
			
			try {
				// find the setter method -- it's parameter type should be the
				// same as the field type
				this.setterFunction = parentClass.getDeclaredMethod(functionName, new Class[] {fieldClass});
			
			// catch all nasty exceptions
			} catch (SecurityException e1) {
				System.err.println("MaxLink :: couldn't access function '" + functionName + "'. Is it public?");
				e1.printStackTrace();
			} catch (NoSuchMethodException e1) {
				System.err.println("MaxLink :: couldn't find function '" + functionName + "'");
				e1.printStackTrace();
			}
		}
	}

	public void setField(Object newValue) {
	
		//System.out.println("setting field " + fieldName + " to " + newValue);
		
		if (this.hasFunction) {
			// if it has a setter function, call that
			
			try {
				setterFunction.invoke(parent, new Object[] {newValue});
				
			// catch nasty exceptions
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			
		} else {
			// no function, so just set the value
			try {
				// might need to type check this
				field.set(parent, newValue);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

		}
		//System.out.println("finished setting value");
	}

	public String getFieldName() {
		return fieldName;
	}
	
	public Class getFieldClass() {
		return fieldClass;
	}
}
