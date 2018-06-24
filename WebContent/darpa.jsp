<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	  	<meta name="viewport" content="width=device-width, initial-scale=1">
	  	<script src="https://d3js.org/d3.v4.min.js"></script>
	  	<script src="https://d3js.org/d3-fetch.v1.min.js"></script>
	  	<script src="https://cdnjs.cloudflare.com/ajax/libs/PapaParse/4.4.0/papaparse.js"></script>
<!-- 	  	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script> -->	  	
		<script src="http://vizjs.org/viz.v1.3.0.min.js"></script>
		<script src="slider/nouislider.js"></script>
		<link rel="stylesheet" href="style.css">
		<link rel="stylesheet" href="slider/nouislider.css">
		<title>DARPA Attack</title>
	</head>
	<body>
	<h1 style="text-align: center">DARPA Attack Visualization</h1>
	    <p>Open DARPA attack files</p>
	    <p>
	        <input type="file" id="files" name="files"/>
	        <output id="list"></output>
	    </p>
	    <p>
	        <button id="increment">Click to increment</button>
	    </p>
	    <div class="form">
			<form name="form" method="post" action="darpa" id="form">
				<ul class="ul">
					<li>Input two numbers:</li>
					<li><input id="n1" type="text" name="n1"></li>
					<li><input id="n2" type="text" name="n2"></li>
					<li><input type="submit" value="Sum" name="button" /></li>
					<li><textarea name="sum" id="sum">${sum}</textarea></li>
				</ul>
			</form>
		</div>
	    <div id="sliderContainer">
	        
	    </div>
	    <div>
	        <svg width="1280" height="1000">
	            <g transform="translate(275,50), scale(1)"></g>
	            <p>Files:</p>
	            <p><a href="data/darpa-newTime-sorted-001.txt">darpa-newTime-sorted-001.txt</a></p>
	            <p><a href="data/darpa-newTime-sorted-002.txt">darpa-newTime-sorted-002.txt</a></p>
	            <p><a href="data/darpa-newTime-sorted-003.txt">darpa-newTime-sorted-003.txt</a></p>
	            <p><a href="data/darpa-newTime-sorted-004.txt">darpa-newTime-sorted-004.txt</a></p>
	            <p><a href="data/darpa-newTime-sorted-005.txt">darpa-newTime-sorted-005.txt</a></p>
	            <p><a href="data/darpa-newTime-sorted-006.txt">darpa-newTime-sorted-006.txt</a></p>
	            <p><a href="data/darpa-newTime-sorted-007.txt">darpa-newTime-sorted-007.txt</a></p>
	            <p><a href="data/darpa-newTime-sorted-008.txt">darpa-newTime-sorted-008.txt</a></p>
	            <p><a href="data/darpa-newTime-sorted-009.txt">darpa-newTime-sorted-009.txt</a></p>
	            <p><a href="data/darpa-newTime-sorted-010.txt">darpa-newTime-sorted-010.txt</a></p>
	            <p><a href="data/darpa-newTime-sorted-080.txt">darpa-newTime-sorted-010.txt</a></p>
	            <script src="app.js"></script>
	        </svg>
	    </div>
	</body>
</html>