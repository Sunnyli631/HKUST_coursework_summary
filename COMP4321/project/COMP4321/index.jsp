<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="comp4321.SearchEngine"%>
<% 
    SearchEngine searchEngine = new SearchEngine();
    searchEngine.setup(true);
%>
<html>
    <head>
        <title>Search Engine</title>
        <link rel="stylesheet" href="style.css"/>
    </head>
    <body>
        <div id="content">
            <form method='post' id="search_form">
                <input type="hidden" name="previous_searches" id="previous_searches" value='<%
                if (request.getParameter("search_word") != null) {
                    out.print(request.getParameter("search_word"));
                    out.print(";");
                }
                if (request.getParameter("previous_searches") != null) {
                    out.print(request.getParameter("previous_searches"));
                }
                %>'>
                <div id="search_bar_div">
                    <div id="search_border_div">
                        <input type="text" id="search_bar" name="search_word" value="<% if (request.getParameter("search_word") != null) {
                            out.print(request.getParameter("search_word"));
                        } else {
                            out.print("");
                        }%>">
                        <div id="search_div">
                            <input id="search" type="submit" value="Search">
                        </div>
                    </div>
                </div>
                <div id="previous_query_bar"><span>Previous queries: </span> <%
                    if (request.getParameter("search_word") != null) {
                        out.print(searchEngine.getPrevious(request.getParameter("search_word")));
                    }
                    if (request.getParameter("previous_searches") != null) {
                        out.print(searchEngine.getPrevious(request.getParameter("previous_searches")));
                    }
                %></div>
            </form>
            <div id="search:hover_div">
              <input id="search" type="submit" type="button" onclick="redirectToPage2()" value="View all stemmed keywords">
            </div>

            <div id="search_results" <% 
                if (request.getParameter("expand_result") != null) {
                    out.print("style=\"animation: search_result_out 1s; animation-fill-mode: forward;\"");
                } %>> <table>
                <%
                    if (request.getParameter("search_word") != null) {
                        String output = searchEngine.request(request.getParameter("search_word"));
                        String[] lines = output.split("\n");
                        for (String line : lines) {
                            out.println("<span>" + line + "</span>");
                        }
                    }
                %> </table>
            </div>

            <script>
                function redirectToPage2() {
                    window.location.href = "words.jsp";
                }
                function search_previous(word) {
                    document.querySelector("#search_bar").value = word;
                    document.querySelector("#search_form").submit();
                }
            </script>
        </div>
    </body>
</html>
