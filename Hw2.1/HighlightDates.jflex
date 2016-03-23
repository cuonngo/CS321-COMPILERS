// CS321-hw2 Cuong Ngo
// Highlight Dates from text

/** This is an example of dates highlighting program for any
 *  text file language that has been written using the jflex **/
%%

%standalone

//This %class declaration will creates a HighlightDates javac file
//Instead of yytext javac file

%class HighlightDates

//HTML file begins with this block

%init{
  System.out.println("<html>");
  System.out.println("<head>");
  System.out.println("<title>My Highlight Dates Page</title>");
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

//HTML file ends with this block

%eof{
  System.out.println("</body>");
  System.out.println("</html>");
%eof}

// One small technical challenge in this is that
// we need to make sure that the three characters <, >, and &, which
// have special uses in HTML, are replaced by the appropriate escape
// sequences &lt;, &gt:, and &amp;, respectively in the output.

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

// For lexemes that we want to highlight, we use the
// tag() method which adds span tag

%{
  void tag(String cl) {
    System.out.print("<span class=\"" + cl + "\">");
    echo();
    System.out.print("</span>");
  }
%}

// For lexemes that we want to highlight we need to
// call the tag function with its keyword

%{
  void date() { tag("date"); }
%}


WhiteSpace         = [ \r\n]+

//Different months have different numbers of days in them
//February has maximum of 29 days. The variable Month29 represents month with 29 days

Month29 = "February"  | "february"  |  "feb"  |  "Feb" | [0][2] | [2]
Date29 = [0-2][0-9] | [1-9]

//The variable Month31 represents the month with 31 days

Month31 = "January"   | "january"   |  "jan"  |  "Jan"  | [0][1] | [1] |
          "March"     | "march"     |  "mar"  |  "Mar"  | [0][3] | [3] |
          "May"       | "may"       | [0][5]  | [5] |
          "July"      | "july"      |  "jul"  |  "Jul"  | [0][7] | [7] |
          "August"    | "august"    |  "aug"  |  "Aug"  | [0][8] | [8] |
          "October"   | "october"   |  "oct"  |  "Oct"  | [1][0] |
          "December"  | "december"  |  "dec"  |  "Dec"  | [1][2]
Date31 = {Date30} | [3][1]

//The variable Month30 represents the motnh with 30 days

Month30 = "April"     | "april"     |  "Apr"  |  "apr"  | [0][4] | [4] |
          "June"      | "june"      |  "Jun"  |  "jun"  | [0][6] | [6] |
          "September" | "september" |  "Sep"  |  "sep"  |  "Sept"  |  "sept"  | [0][9] | [9] |
          "November"  | "november"  |  "Nov"  |  "nov"  | [1][1]
Date30 = {Date29} | [3][0]

//This will only keep track of the year between 1500 to 2500
Year   = [1][5-9][0-9][0-9] |           //1500 - 1999
         [2][0-4][0-9][0-9] |           //2000 - 2499
         [2][5][0][0]				//2500

//List of the valid dates format
Dates = {Month29} {WhiteSpace} {Date29} "," {WhiteSpace} {Year} | 	//Month Day, Year format 
	{Month30} {WhiteSpace} {Date30} "," {WhiteSpace} {Year} | 	
	{Month31} {WhiteSpace} {Date31} "," {WhiteSpace} {Year} |	
        {Date29} {WhiteSpace} {Month29} "," {WhiteSpace} {Year} |	//Day Month, Year format
	{Date30} {WhiteSpace} {Month30} "," {WhiteSpace} {Year} | 
	{Date31} {WhiteSpace} {Month31} "," {WhiteSpace} {Year} |
        {Date29} {WhiteSpace} {Month29} {WhiteSpace} {Year} |		//Day Month Year format 
	{Date30} {WhiteSpace} {Month30} {WhiteSpace} {Year} | 
	{Date31} {WhiteSpace} {Month31} {WhiteSpace} {Year} |
        {Date29} {WhiteSpace} {Month29} | 				//Day Month format
	{Date30} {WhiteSpace} {Month30} | 
	{Date31} {WhiteSpace} {Month31} |
        {Month29} {WhiteSpace} {Date29} | 				//Month Day
	{Month30} {WhiteSpace} {Date30} | 
	{Month31} {WhiteSpace} {Date31} |
        {Month29} "/" {Date29} "/" ([0-9][0-9] | {Year}) | 		//Month/Day/Year
	{Month30} "/" {Date30} "/" ([0-9][0-9] | {Year}) | 
	{Month31} "/" {Date31} "/" ([0-9][0-9] | {Year}) |
        {Month29} "/" {Date29} | 					//Month/Day
	{Month30} "/" {Date30} |
	{Month31} "/" {Date31}


%%
// RULES:
// If two rules match the same input, choose the earliest rule
// Longest lexeme wins

// Invalid if there's an extra '/' after the dates
{Dates} [/]		{ echo(); }

// Valid Dates
"\n" {Dates}		{ date(); }
{Dates}			{ date(); }

// More invalid dates.
// If there's an digits after dates
[^ "\""] {Dates}        { echo(); }
[^ "\""] {Dates} [0-9]  { echo(); }
{Dates} [0-9]		{ echo(); }

