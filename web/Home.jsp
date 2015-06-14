<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Kwery</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">

<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap-theme.min.css">
        
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script>

$(document).ready(function() {
	
	$("#btnSearch").click(function(){
		//var apiKey = "AIzaSyC0DF507U3EojxOLqzDVRcVVY0g3lPG8PQ";
		var query = $("#queryBar").val();
		//var cxi = "017099887525841532988:n_wpdgqoerw";
		var cxi = "013006379665930061845:rji_fp6jccu";
		var apiKey = "AIzaSyDyYwdS0VyihCIVfO2rgV1KdZpa6GNIUZk";
		$.ajax({url: "https://www.googleapis.com/customsearch/v1?key="+apiKey+"&cx="+cxi+"&q="+query+"&alt=json",
							success: function(result){
									//alert("In success: "+query);
									
									var response = result;
									//obj = JSON.parse(response);
									var x = response.items;
									var s = "";
									var links = "";
									for(var i = 0; i < x.length; i++ )
									{
										s += "<p>";
										s += "<h5><a href = \"" + x[i].link + "\">"+ x[i].htmlTitle +"</a></h5>";
										s += x[i].displayLink + "<br>"; 
										s += x[i].htmlSnippet ;
										s += "</p> <br>";
										
										if( i == 0)
											links = x[i].link;
										else
											links = links+"^"+x[i].link;
									}
									$("#search-results").html(s);
									$.ajax({url: "findAnswer.action?jsonString="+links+"&query="+query,
									dataType: 'text',
									success: function(res1){
										//alert("Result: "+res1);
										//alert("Back again: "+$("#summary").val());
										$("#summaryPane").html(res1);
										
									}}); 
									
									/* $.ajax({url: "findAnswer.action?firstLink="+firstLink,
											dataType: 'text',
											success: function(res1){
												//alert("Result: "+res1);
												//alert("Back again: "+$("#summary").val());
												$("#summaryPane").html(res1);
												
											}}); */
	        						//alert(result);
	    			}});
	});
    
});
</script>
</head>
    <body>
        <div class = "container">

            <div class =" row">
                <div class =" col-lg-10 col-lg-offset-1">
                    <div class="header clearfix">
                        <a href="/SMERCApp/getHome.action"><img src="kwery_logo.jpg" height=70px width=100px /></a>
                    </div>

                    <div class="row">
                        <div class="jumbotron">
                            
                                    <div id="search-bar">
                                        <div class ="row" >
                                            <div class ="col-lg-10">
                                                <input type="text" class="form-control" id= "queryBar" name="query" label="Query"/>
                                            </div>
                                            <div class="col-lg-2">
                                                <button id="btnSearch" type="submit" class="btn btn-default">Search</button>
                                            </div>
                                        </div>
                                    </div>
                                
                        </div>
                    </div>

                    <div id="summaryPane">
                    </div>
<hr>
                    <div id="search-results">
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>