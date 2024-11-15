<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="comp4321.SearchEngine"%>
<% 
    SearchEngine searchEngine = new SearchEngine();
    searchEngine.setup(true);
%>
<html>
    <head>
        <title>Search Engine - Keywords</title>
        <link rel="stylesheet" href="style.css"/>
    </head>
    <body>
        <div id="content">
            <form method='post' id="search_form" action="index.jsp">
                <input type="hidden" name="expand_result" value="true">
                <input type="hidden" name="search_word" id="hidden_search_bar">
                <input type="hidden" name="previous_searches" id="previous_searches" value="<% 
                if (request.getParameter("search_word") != null) {
                    out.print(request.getParameter("search_word"));
                    out.print(";");
                }
                if (request.getParameter("previous_searches") != null) { 
                    out.print(request.getParameter("previous_searches")); 
                } 
                %>">
                <div id="words_content">
                    <div id="words_checkboxes">
                        <div style="height: 30px"></div>
                        <% 
                            out.print(searchEngine.getKeywordCheckboxes());
                        %>
                    </div>
                    <div id="bottom_bar">
                        <span id="word_selected">Words selected: </span>
                        <input id="search" type="submit" value="Search" style="margin-right: 0px"/>
                    </div>
                </div>
            </form>
        </div>
    </body>
    <script>
        var word_list = [];
        window.onload = function() {
            var checkboxes = document.querySelectorAll(".stem_checkbox");
            for (let i=0;i<checkboxes.length;i++) {
                checkboxes[i].addEventListener("change", (event) => {
                    if (event.target.checked == true) {
                        word_list.push(event.target.value);
                    } else {
                        var index = word_list.indexOf(event.target.value);
                        if (index > -1) {
                            word_list.splice(index, 1);
                        }
                    }
                    var words = word_list.join(" ");
                    document.querySelector("#hidden_search_bar").value = words;
                    document.querySelector("#word_selected").innerHTML = "Word Selected: " + words;
                })
            }
        }
    </script>
</html>
