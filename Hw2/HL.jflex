// hw2 Highlight Dates

/* user code */

%%

/* operations and declarations */

%standalone
%class HL

%init{
  System.out.println("<html>");
  System.out.println("<head>");
  System.out.println("<title>My Syntax Colored Web Page</title>");
  System.out.println("<style type=\"text/css\">");
  System.out.println("  body     {white-space:pre;");
  System.out.println("            background-color:#ffffcc;");
  System.out.println("            color:black;");
  System.out.println("            font-family:\"Lucida Console\",\"Courier New\",Monotype}");
  System.out.println("  .date {background-color: #FFFF00}");
  System.out.println("</style>");
  System.out.println("</head>");
  System.out.println("<body>");
%init}


%eof{
  System.out.println("</body>");
  System.out.println("</html>");
%eof}


%{
  void echo() {
    int len = yylength();       // Find length of current lexeme
    for (int i=0; i<len; i++) { // Run through each character in turn
      char c = yycharat(i);
      switch (c) {              // and translate as appropriate ...
        case '<' : System.out.print("&lt;");  break;
        case '>' : System.out.print("&gt;");  break;
        case '&' : System.out.print("&amp;"); break;
        default  : System.out.print(c);       break;
      }
    }
  }
%}

%{
  void tag(String cl) {
    System.out.print("<span class=\"" + cl + "\">");
    echo();
    System.out.print("</span>");
  }
%}

%{
  void date() { tag("date"); }
%}









