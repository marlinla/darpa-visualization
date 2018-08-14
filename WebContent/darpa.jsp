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
		<script src="http://vizjs.org/viz.v1.3.0.min.js"></script>
		<script src="slider/nouislider.js"></script>
		<link rel="stylesheet" href="style.css">
		<link rel="stylesheet" href="slider/nouislider.css">
		<title>DARPA Attack</title>
	</head>

	<body>
		<div id="visualization">
			<h1 style="text-align: center">DARPA Attack Visualization</h1>
			<div id="visualization-controls">
				<div id="visualization-controls-playback">
						<p>
								<strong>Playback Controls:</strong>
						</p>
					<p>
						<button id="increment">Increment</button>

						<button id="start">Start</button>

						<button id="stop">Stop</button>

						<button id="restart">Restart</button>
					</p>
				</div>
				<div id="visualization-controls-speed">
					<p>
						<strong>Speed Multiplier:</strong>
					</p>
					<p>
						<input id="speed" type="number" value="1" step="0.25" placeho er="Speed">

						<button id="reset">Reset Speed Multiplier</button>
					</p>
				</div>
			</div>


			<div id="sliderContainer">

			</div>
			<div id="visualization-graph">
				<svg width="1280" height="1000">
					<g transform="translate(275,50), scale(1)"></g>
				</svg>
			</div>
		</div>



		<script src="app.js"></script>
	</body>

	</html>