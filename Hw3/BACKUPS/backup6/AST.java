
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

	void printJava(JavaOutput out, String parent, boolean isDerived, Args ParentArgs){
		out.printNAME(name, parent);
		Args.printJava(out, args, ParentArgs, null);
		out.print("\n\t// declare default constructor for this class\n\t"+name+"(");
		Args.PrintConstructure(out, args, ParentArgs, null);
		out.print(") {\n");
		out.print("\tsuper(");
		Args.Constructure(out, args, ParentArgs, null);
		out.print(");\n");
		printJava(out, subclasses, name, true, args);
	}

	public static void printJava(JavaOutput out, Class[] classes, String parent, boolean isDerived, Args args){
		for (int i=0; i<classes.length; i++) {
			classes[i].printJava(out, parent, isDerived, args);
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

	public static void printJava(JavaOutput out, Args args, Args ParentArgs, Args end ){
		if(args != end) {
			if(args == ParentArgs)
				return;
			printJava(out, args.before, ParentArgs, end);
			args.last.printJava(out);
		}
	}

	public static void PrintConstructure(JavaOutput out, Args args, Args ParentArgs, Args end){
		if(args != end){
			PrintConstructure(out, args.before, ParentArgs, end);
			if(args.before != end)
				out.print(", ");
			args.last.printArg(out);
		}

	}

	public static void Constructure(JavaOutput out, Args args, Args parentArgs, Args end){
		if(args != end){
			args.Constructure(out, args.before, parentArgs, end);
			if(args == parentArgs)
				args.last.printId(out);
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

	public void printJava(JavaOutput out) {
		StringBuffer buf = new StringBuffer();
		if(visibility != null) {
			buf.append(visibility);
			buf.append(" ");
		}
		buf.append(type.toString());
		buf.append(" ");
		buf.append(id);
		out.printFIELDS(buf.toString());
	}

	public void printArg(JavaOutput out) {
		out.print(type.toString() + " " + id);

	}

	public void printId(JavaOutput out) {
		out.print(id);
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



class JavaOutput {
	private java.io.PrintStream out;
	
	public JavaOutput(java.io.PrintStream out){
		this.out = out;
	}

	public void print(String m)
	{
		out.print(m);
	}
	public void printNAME(String name, String parent){
		out.print("class ");
		out.print(name + " ");
		if(parent != null) {
			out.print("extends ");
			out.print(parent + " ");
		}
		out.println("{");
		out.println("\t//declare the fields for this class");
	}

	public void printFIELDS(String fields)
	{
		out.print("\t" + fields);
		out.println(";");
	}
	

}
