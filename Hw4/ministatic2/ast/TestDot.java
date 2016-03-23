package ast;
import compiler.Position;
import compiler.Failure;
import compiler.Handler;
import compiler.JavaSource;
import compiler.SimpleHandler;
import compiler.Source;
import java.io.FileReader;
import syntax.*;

public class TestDot {

    public static void main(String[] args) {
        Handler handler = new SimpleHandler();
    
        if (args.length!=1) {
          handler.report(new Failure("This program requires exactly one argument"));
        } else {
    
          try {
            String     input  = args[0] + ".mini";
            FileReader reader = new FileReader(input);
            Source     source = new JavaSource(handler, input, reader);
            MiniLexer  lexer  = new MiniLexer(handler, source);
            MiniParser parser = new MiniParser(handler, lexer);
            Stmt       prog   = parser.getProgram();
            if (handler.hasFailures()) {
                throw new Failure(
                    "Aborting: errors detected during syntax analysis");
            }
    
            new ScopeAnalysis(handler).analyze(prog);
    
            String     output = args[0] + ".dot";
            new DotOutput(output).toDot(prog);
            System.out.println("Written abstract syntax graph to " + output);
    
          } catch (Failure f) {
            handler.report(f);
          } catch (Exception e) {
            handler.report(new Failure(e.toString()));
          }
    
        }
      }
}
