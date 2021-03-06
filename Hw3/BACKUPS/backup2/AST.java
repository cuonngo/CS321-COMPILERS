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
