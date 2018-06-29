var files = null;
var parseLength;
var lineCursor = 0;
var timeCursor = 0;
var lineIncrement;
var parseResults;
var minuteIncrement = 10;
var spamThreshold = 600;
var updateRate = 100;
var drawLoop = null;
var sliderMin = 0;
var sliderMax = 100;
var slider;
var sliderContainer;
var sliderValue;
var bP;
var dataSet1 = [];
var dataSet2 = [];

main();

function main() {
    document.getElementById('increment').addEventListener('click', clickIncrement, false);
    document.getElementById('start').addEventListener('click', clickStart, false);
    document.getElementById('stop').addEventListener('click', clickStop, false);
    document.getElementById('reset').addEventListener('click', clickReset, false);
    document.getElementById('update').addEventListener('click', clickUpdate, false);

    document.getElementById('speed').value = updateRate.toString();
    //document.getElementById('speed').addEventListener('click', updateSpeed, false);
    document.getElementById('interval').value = minuteIncrement.toString();
    //document.getElementById('interval').addEventListener('click', updateInterval, false);




    data = [
        ['A', 'X', 2]
        , ['A', 'Y', 3]
        , ['B', 'X', 5]
        , ['B', 'Y', 8]
        , ['C', 'X', 2]
        , ['C', 'Y', 9]
        , ['A', 'Y', 3]
        , ['A', 'Y', 3]
    ];
    bP = viz.biPartite()
        .data(data)
    d3.select("g").call(bP);

    //ajax request a string
    fetch(window.location.href.split('.')[0])
        .then(function (response) {
            return response.text()
        }).then(function (myText) {
            parseFile([myText], "");
        });

}

function clickIncrement() {
    if (parseResults !== null) {
        lineIncrement = 1;
        // itterating through time until the last time is less than or equal to minuteIncrement
        for (; lineCursor + lineIncrement < parseLength && (parseResults.data[lineCursor + lineIncrement][4] - timeCursor <= minuteIncrement); ++lineIncrement) {
        }
        processData();
        lineCursor += lineIncrement;
        // check if next itteration will overflow, if so reset to start
        if (!(lineCursor < parseLength)) {
            lineCursor = 0;
            dataSet1 = [];
        }
        timeCursor = parseResults.data[lineCursor][4];
        slider.noUiSlider.set(timeCursor);
    }
}

function clickStart() {
    clearInterval(drawLoop);
    drawLoop = window.setInterval(clickIncrement, updateRate);
}
function clickStop() {
    clearInterval(drawLoop);
}
function clickReset() {
    lineCursor = 0;
    timeCursor = parseResults.data[lineCursor][4];
    slider.noUiSlider.set(timeCursor);
    clickIncrement();
}
function clickUpdate() {
    //document.getElementById('interval');
    //document.getElementById('speed');
    console.log("clickUpdate");
}
function updateInterval() {
    minuteIncrement = this.value;
}
function updateSpeed() {
    updateRate = this.value;
}

function processData() {
    var returnArray = [];
    var colorGuide = {};
    //loop through parsed data within increment interval to process further
    for (var i = 0 + lineCursor; i < parseLength && i < lineCursor + lineIncrement; i++) {
        var attack = parseResults.data[i][3];
        // if attack: color sender ip
        if (attack != '-') {
            console.log(attack);
            colorGuide[parseResults.data[i][0]] = '#FF0000';
        }
        //if high levels of connections, aggregate sender IPs to later be sorted and sliced.
        if (lineIncrement >= spamThreshold) {

            filterSpam(i, returnArray);
        }
        else {
            returnArray.push(parseResults.data[i].slice(0, 2).concat(1));
        }
    }
    //operations for machine learning

    //viz framework optimizations
    if (returnArray.length > 300) {
        returnArray.sort(function (a, b) { return a[2] < b[2] })
        returnArray = returnArray.slice(0, Math.min(returnArray.length, 300)); //viz has a hard limit of 300

    }

    d3.select("g").call(bP.data(returnArray).fill(fill(colorGuide)));
}

function filterSpam(i, returnArray) {
    if (returnArray.length == 0) {
        returnArray.push(parseResults.data[i].slice(0, 2).concat(1));
    }
    else {
        var found = false;
        for (element of returnArray) {
            if (element[0] == parseResults.data[i][0]) {
                element[2]++;
                found = true;
                break
            }
        }
        if (!found) {
            returnArray.push(parseResults.data[i].slice(0, 2).concat(1));
        }
    }
}


function fill(colorGuide) {
    return function (d) { return colorGuide[d.primary] }
}

function parseFile(files, func) {
    clearInterval(drawLoop);
    Papa.parse(files[0], {
        delimiter: ",",
        newline: "\n",
        header: false,
        skipEmptyLines: true,
        complete: function (results) {
            parseResults = results;
            parseLength = parseResults.data.length;
            timeCursor = parseResults.data[0][4];
            sliderMin = timeCursor;
            sliderMax = parseResults.data[parseResults.data.length - 1][4];
            slider = document.getElementById('sliderContainer');
            sliderFactory();
            slider.setAttribute('disabled', true);
            console.log('results:');
            console.log(parseResults);
        }
    });
}

function sliderFactory() {
    if (slider.noUiSlider == null) {
        noUiSlider.create(slider, {
            start: [0],
            step: minuteIncrement,
            range: {
                'min': [parseInt(sliderMin)],
                'max': [parseInt(sliderMax)]
            },
            connect: [true, false],
            disable: true
        });
    }
    else {
        slider.noUiSlider.updateOptions({
            range: {
                'min': [parseInt(sliderMin)],
                'max': [parseInt(sliderMax)]
            }
        });
    }
}