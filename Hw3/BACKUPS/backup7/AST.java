class Class {
	private boolean isAbstract;
	private String name;
	private Args args;
	private Class[] subclasses;

	Class(boolean isAbstract, String name, Args args, Class[] subclasses) {
		this.isAbstract = isAbstract;
		this.name = name;
		this.args = args;
		this.subclasses = subclasses;
	}


	/**Printing IndentOutput **/
	void indent(IndentOutput out, int n) {
 		out.indent(n, name);
 		out.indent(n+1, "isAbstract="+isAbstract);
		out.indent(n+1, "Args");
 		Args.indent(out, n+2, args, null);
		out.indent(n+1, "Classes");
		indent(out, n+2, subclasses);
	}

	public static void indent(IndentOutput out, int n, Class[] classes) {
		for (int i=0; i<classes.length; i++) {
			classes[i].indent(out, n);
		}
	}

	/**Printing in Java form */
	void java(JavaOutput out, int n, String parent, Args parentArgs) {
		out.printName(n, name, parent);
		Args.displayFields(out, n+2, args, parentArgs);
		Args.displayCons(out, n+2, args, parentArgs, name);
		out.print(n, "}\n");
		java(out, n, subclasses, name, args);
	}
	public static void java(JavaOutput out, int n, Class[] classes, String parent, Args parentArgs) {
		for (int i=0; i<classes.length; i++){
			classes[i].java(out, n, parent, parentArgs);
		}
	}


}


class Args {
	Args before;
	Arg last;

	Args(Args before, Arg last) {
		this.before = before;
		this.last = last;
	}

	// Output an indented description of a list of arguments.
   
	public static void indent(IndentOutput out, int n, Args args, Args end) {
		if (args!=end) {                    // Add to the Args class
 			indent(out, n, args.before, end);
			args.last.indent(out, n);
		}
	}


	//This function with return a new modified Args.
	private static Args argsList(Args args, Args parentArgs) {
		if(args != parentArgs){
			return new Args(argsList(args.before, parentArgs), args.last);
		} else {
			return null;
		}

	}

	/****************************Print out Fields*******************************/
	//Recursive printing
	private static void displayFields(JavaOutput out, int n, Args args) {
		if(args != null) {
			displayFields(out, n, args.before);
			args.last.displayFields(out, n);
		}
	}

	//Wrapper function
	public static void displayFields(JavaOutput out, int n, Args args, Args parentArgs) {
		out.print(n, "// declare the fields for this class\n");
		Args list = argsList(args, parentArgs);
		displayFields(out, n, list);
		out.print(0,"\n");
	}


	/****************************Print out Constructors*******************************/

	public static void displayCons(JavaOutput out, int n, Args args, Args parentArgs, String name) {
		out.print(n, "// declare the default constructor for this class\n");
		displayCons(out, n, args, name);
		if(args != null)
			out.print(0, ") {\n");
		displaySuper(out, n+2, parentArgs);
		displayThis(out, n+2, args, parentArgs);
		if(args != null)
			out.print(n, "}\n");
	}

	private static void displayCons(JavaOutput out, int n, Args args, String name) {
		if(args != null) {
			displayCons(out, n, args.before, name);
			if(args.before == null)
				out.print(n, name + "(");
			else if(args.before != null && args.last != null)
				out.print(0, ", ");
			args.last.displayConsArgs(out, 0);
		}
	}

	private static void displaySuper(JavaOutput out, int n, Args args) {
		if(args != null){
			displaySuper(out, n, args.before);
			args.last.displayConsSuper(out, n);
		}
	}

	private static void displayThis(JavaOutput out, int n, Args args, Args parentArgs) {
		Args list = argsList(args, parentArgs);
		displayThis(out, n, list);
	}

	private static void displayThis(JavaOutput out, int n, Args args) {
		if(args != null) {
			displayThis(out, n, args.before);
			args.last.displayConsThis(out, n);
		}
	}
}

class Arg {
	private String visibility;
	private Type type;
	private String id;

	Arg(String visibility, Type type, String id) {
		this.visibility = visibility;
		this.type = type;
		this.id = id;
	}

	void Print()
	{
		System.out.println(visibility);
		System.out.println(type);
		System.out.println(id);
	}


	/** Output an indented description of this argument.
	*/
	public void indent(IndentOutput out, int n) {
		StringBuffer buf = new StringBuffer();
			if (visibility!=null) {
 				buf.append(visibility);
				buf.append(" ");
			}
		buf.append(type.toString());
		buf.append(" ");
		buf.append(id);
		out.indent(n, buf.toString());
	}

        /****************************Display Fields*******************************/
	public void displayFields(JavaOutput out, int n) {
		StringBuffer buf = new StringBuffer();
			if(visibility != null) {
				buf.append(visibility);
				buf.append(" ");
			}
		buf.append(type.toString());
		buf.append(" ");
		buf.append(id);
		out.print(n, buf.toString() + ";\n");
	}

	/****************************Display Constructors*******************************/
	public void displayConsArgs(JavaOutput out, int n) {
		StringBuffer buf = new StringBuffer();
		buf.append(type.toString());
		buf.append(" ");
		buf.append(id);
		out.print(n, buf.toString());

	}

	public void displayConsSuper(JavaOutput out, int n){
		StringBuffer buf = new StringBuffer();
		buf.append("super(");
		buf.append(id);
		buf.append(");\n");
		out.print(n, buf.toString());
	}

	public void displayConsThis(JavaOutput out, int n) {
		StringBuffer buf = new StringBuffer();
		buf.append("this.");
		buf.append(id);
		buf.append(" = ");
		buf.append(id);
		buf.append(";");
		out.print(n, buf.toString() + "\n");

	}
}




//an abstract base class (Type), and two concrete classes (NameType and ArrayType)
abstract class Type {
	Type() {

	}
}

class NameType extends Type {
	private String[] ids;

	NameType(String[] ids) {
		this.ids = ids;
	}

	public String toString() {
		if (ids.length == 1) {
			return ids[0];
		} else {
			StringBuffer buf = new StringBuffer(ids[0]);
			for (int i=1; i<ids.length; i++) {
				buf.append(".");
				buf.append(ids[i]);
			}
			return buf.toString();
		}
	}
}

class ArrayType extends Type {
	private Type elem;

	ArrayType(Type elem) {
		this.elem = elem;
	}

	public String toString() {
		return elem.toString() + "[]";
	}
}

/**Represents an output phase for producing java descriptions
* of .java files.
*/

class JavaOutput {

	private java.io.PrintStream out;

	/**Default constructor.
	*/
	public JavaOutput(java.io.PrintStream out) {
		this.out = out;
	}

	/** Print a given String message to java form
	*/

	public void print(int n, String msg) {
		for(int i = 0; i<n; i++)
			out.print(" ");
		if(msg != null)
			out.print(msg);
		return;
	}

	public void printName(int n, String name, String parent) {
		for (int i=0; i<n; i++)
			out.print(" ");
		out.print("class " + name + " "); 
		if(parent != null)
			out.print("extends " + parent + " ");
		out.println("{");
		//print(2, "// declare the fields for this class");
	}

	

}




