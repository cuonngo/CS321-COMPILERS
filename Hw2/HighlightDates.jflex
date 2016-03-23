// hw2 Highlight Dates

/* user code */

%%

/* operations and declarations */

%standalone
%class HighlightDates


%init{
  System.out.println("<html>");
  System.out.println("<head>");
  System.out.println("<title>My Syntax Colored Web Page</title>");
  System.out.println("<style type=\"text/css\">");
  System.out.println("  body     {white-space:pre;");
  System.out.println("            background-color:#ffffcc;");
  System.out.println("            color:black;");
  System.out.println("            font-family:\"Lucida Console\",\"Courier New\",Monotype}");
  System.out.println("  .date {background-color:white; color:green}");
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

Month29 = "February"  | "february"  |  "feb"  |  "Feb" | [0][2] | [2]
Date29 = [0-2][0-9] | [1-9]

Month31 = "January"   | "january"   |  "jan"  |  "Jan"  | [0][1] | [1] |
	  "March"     | "march"     |  "mar"  |  "Mar"  | [0][3] | [3] |
	  "May"       | "may"       | [0][5]  | [5] |
	  "July"      | "july"      |  "jul"  |  "Jul"  | [0][7] | [7] |
	  "August"    | "august"    |  "aug"  |  "Aug"  | [0][8] | [8] |
	  "October"   | "october"   |  "oct"  |  "Oct"  | [1][0] |
	  "December"  | "december"  |  "dec"  |  "Dec"  | [1][2]
Date31 = {Date30} | [3][1]

Month30 = "April"     | "april"     |  "Apr"  |  "apr"  | [0][4] | [4] |
	  "June"      | "june"      |  "Jun"  |  "jun"  | [0][6] | [6] |
	  "September" | "september" |  "Sep"  |  "sep"  |  "Sept"  |  "sept"  | [0][9] | [9] |
	  "November"  | "november"  |  "Nov"  |  "nov"  | [1][1]
Date30 = {Date29} | [3][0]

Year   = [1][5-9][0-9][0-9] | 		//1500 - 1999
	 [2][0-4][0-9][0-9] |		//2000 - 2499
	 [2][5][0][0]			//2500

Dates = {Month29} " " {Date29} "," " " {Year} |  {Month30} " " {Date30} "," " " {Year} | {Month31} " " {Date31} "," " " {Year} |
	{Date29} " " {Month29} "," " " {Year} |  {Date30} " " {Month30} "," " " {Year} | {Date31} " " {Month31} "," " " {Year} |
	{Date29} " " {Month29} " " {Year} | {Date30} " " {Month30} " " {Year} | {Date31} " " {Month31} " " {Year} |
	{Date29} " " {Month29} | {Date30} " " {Month30} | {Date31} " " {Month31} |
	{Month29} " " {Date29} | {Month30} " " {Date30} | {Month31} " " {Date31} |
	{Date31} [ \n]+ {Month31} |
	{Month29} "/" {Date29} "/" ([0-9][0-9] | {Year}) | {Month30} "/" {Date30} "/" ([0-9][0-9] | {Year}) | {Month31} "/" {Date31} "/" ([0-9][0-9] | {Year}) |
	{Month29} "/" {Date29} | {Month30} "/" {Date30} | {Month31} "/" {Date31}
%%

/* lexical rules */



","  | "["  | "]"  | "("  | ")"  | "{"  | "}" | ";" |
"="  | "==" | ">"  | ">=" | "<"  | "<=" | "!" | "~" |
"!=" | "&"  | "&&" | "|"  | "||" | "^"  | "*" | "+" |
"-" | "/"       { echo(); }

{Dates} [/]	{ System.out.print(yytext()); }
[\n] {Dates}	{ date(); }
{Dates}	{ date(); }
[^ "\""] {Dates} 	{ System.out.print(yytext()); }
[^ "\""] {Dates} [0-9]	{ System.out.print(yytext()); } 
{Dates} [0-9]	{System.out.print(yytext()); }








