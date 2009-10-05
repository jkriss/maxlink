package maxlink;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MaxFunction {
	private String functionName;
	private Method function;
	private Object parent;
	private Class parentClass;
	
	public MaxFunction(Object parent, String functionName) {
		this(parent, functionName, null);
	}
	
	public MaxFunction(Object parent, String functionName, Class args[]) {
		this.parent = parent;
		this.parentClass = parent.getClass();
		
		this.functionName = functionName;
			
		try {
			// find the setter method -- it's parameter type should be the
			// same as the field type
			this.function = parentClass.getDeclaredMethod(functionName, args);
		
		// catch all nasty exceptions
		} catch (SecurityException e1) {
			System.err.println("MaxLink :: couldn't access function '" + functionName + "'. Is it public?");
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			System.err.println("MaxLink :: couldn't find function '" + functionName + "'");
			e1.printStackTrace();
		}
	}

	public void call(Object args[]) {
		//System.out.println("calling function " + this.functionName);
		try {
			function.invoke(parent, args);
			
		// catch nasty exceptions
		} catch (IllegalArgumentException e) {
			System.err.println("MaxLink :: unexpected arguments for function '" + functionName + "'");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.err.println("MaxLink :: couldn't access function '" + functionName + "'. Is it public?");
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public String getFunctionName() {
		return functionName;
	}
}
