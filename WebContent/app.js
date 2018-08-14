var files = null;
var predictionCount = 32;
var fileCount = 61;
var fileCursor = 0;
var timeCursor = 0;
var lineIncrement;
var ipResults = [];
var ipCursor = 0;
var predictedResults = [];
var predictedCursor = 32;
var attackResults = "";
var minuteIncrement = 10;
var updateRate = 1000;
var updateMultiplier = 1;
var drawLoop = null;
var sliderMin = 0;
var sliderMax = 61;
var slider;
var sliderContainer;
var bP;
var buttonIncrement;
var buttonStart;
var buttonStop;
var buttonRestart;
var buttonSpeed;
var buttonReset;

const GET_IP = "ip";
const GET_PREDICTED = "predicted";
const GET_ATTACK = "attack";
const DELIM_IP = ",";
const DELIM_PREDICTED = " - ";
const DELIM_ATTACK = ",";
const DEFAULT_COLOR = "#000000";

main();

function main() {
  slider = document.getElementById("sliderContainer");

  document.getElementById("speed").value = updateMultiplier.toString();

  //event listeners
  document
    .getElementById("increment")
    .addEventListener("click", clickIncrement, false);
  document.getElementById("start").addEventListener("click", clickStart, false);
  document.getElementById("stop").addEventListener("click", clickStop, false);
  document
    .getElementById("restart")
    .addEventListener("click", clickRestart, false);
  document
    .getElementById("speed")
    .addEventListener("change", updateSpeed, false);
  document.getElementById("reset").addEventListener("click", clickReset, false);

  data = [
    ["A", "X", 2],
    ["A", "Y", 3],
    ["B", "X", 5],
    ["B", "Y", 8],
    ["C", "X", 2],
    ["C", "Y", 9],
    ["A", "Y", 3],
    ["A", "Y", 3]
  ];
  bP = viz.biPartite().data(data);
  d3.select("g").call(bP);

  updateSlider();
  //ajax get_IP and GET_PREDICTED files from 0 to fileCount
  fetchData(ipCursor, GET_IP);
  //fetchData(predictedCursor, GET_PREDICTED);
  //fetchData(null, GET_ATTACK);
}
function fetchData(id, type) {
  var address = window.location.href.split(".");
  var fetchAddress = "";
  for (let i = 0; i < address.length - 1; i++){
    fetchAddress += address[i];
  }
  console.log(
    window.location.href.split(".")[0] + "?type=" + type + "&id=" + id
  );
  fetch(fetchAddress + "?type=" +  type + "&id=" + id)
    .then(function(response) {
      return response.text();
    })
    .then(function(myText) {
      if (type === GET_IP) {
        parseFile([myText], DELIM_IP, false, parseIP);
      } else if (type === GET_PREDICTED) {
        parseFile([myText], DELIM_PREDICTED, true, parsePredicted);
      } else if (type === GET_ATTACK) {
        parseFile([myText], DELIM_ATTACK, false, parseAttack);
      }
    });
}

function processIP() {
  var colorGuide = {};
  var resultsArray = [];
  for (let index = 0; index < ipResults[fileCursor].data.length; index++) {
    let loopArray = [];
    const element = ipResults[fileCursor].data[index];
    loopArray.push(element[0]);
    loopArray.push(element[1]);
    const weight = element[2];
    loopArray.push(weight);
    resultsArray.push(loopArray);
    if (fileCursor < predictionCount) {
      colorGuide[element[0]] = "#000000";
    } else {
      colorGuide[element[0]] = element[3];
    }
  }

  /* if (attack != '-') {
        console.log(attack);
        colorGuide[ipResults.data[i][0]] = '#FF0000';
    } */

  //redraw graph, with attacks colored
  d3.select("g").call(bP.data(resultsArray).fill(fill(colorGuide)));
}
function processPredicted() {}

function fill(colorGuide) {
  return function(d) {
    return colorGuide[d.primary];
  };
}

function clickIncrement() {
  clickIncrement: {
    if (ipResults !== null) {
      if (!(fileCursor < ipCursor)) {
        clickStop();
        break clickIncrement;
      }
      console.log(fileCursor);
      processIP(fileCursor);
      processPredicted();
      fileCursor += 1;
      slider.noUiSlider.set([fileCursor, ipCursor]);
    }
  }
}

function clickStart() {
  if (!(fileCursor < fileCount)) {
    clickRestart();
  }
  clearInterval(drawLoop);
  //drawLoop = window.setInterval(clickIncrement, updateRate / updateMultiplier);
  drawLoop = setInterval(clickIncrement, updateRate / updateMultiplier);
}
function clickStop() {
  clearInterval(drawLoop);
}
function clickRestart() {
  fileCursor = 0;
  slider.noUiSlider.set([fileCursor, ipCursor]);
  clickIncrement();
}
function clickUpdate() {
  //document.getElementById('interval');
  //document.getElementById('speed');
  console.log("clickUpdate");
}
function clickReset() {
  document.getElementById("speed").value = "1";
  updateSpeed();
}
function updateInterval() {
  minuteIncrement = this.value;
}
function updateSpeed() {
  updateMultiplier = this.value;
  if (drawLoop != null) {
    clickStart();
  }
}

function updateSlider() {
  if (slider.noUiSlider == null) {
    noUiSlider.create(slider, {
      start: [0, 0],
      step: 1,
      range: {
        min: [sliderMin],
        max: [sliderMax]
      },
      connect: [true, true, false],
      disable: true
    });
  }
  slider.setAttribute("disabled", true);
}

function parseFile(files, delim, head, func) {
  Papa.parse(files[0], {
    delimiter: delim,
    newline: "\n",
    header: head,
    skipEmptyLines: true,
    complete: func
  });
}

function parseIP(results) {
  ipResults[ipCursor] = results;
  slider.noUiSlider.set([fileCursor, ipCursor]);
  ipCursor++;
  if (ipCursor <= fileCount) {
    fetchData(ipCursor, GET_IP);
  } else {
    console.log("IP results:", ipResults);
  }
}
function parsePredicted(results) {
  predictedResults[predictedCursor] = results;
  predictedCursor++;

  if (predictedCursor <= fileCount) {
    fetchData(predictedCursor, GET_PREDICTED);
  } else {
    console.log("Predicted results:", predictedResults);
  }
}
function parseAttack(results) {
  attackResults = results;
  console.log(attackResults);
}


