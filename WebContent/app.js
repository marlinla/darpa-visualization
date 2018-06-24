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

function main(){
    // console.log("it begins");
	document.getElementById('files').addEventListener('change', handleFileSelect, false);
    document.getElementById('increment').addEventListener('click', clickIncrement, false);
    
    
    data=[
        ['A','X', 2]
        ,['A','Y', 3]
        ,['B','X', 5]
        ,['B','Y', 8]
        ,['C','X', 2]
        ,['C','Y', 9]
        ,['A','Y', 3]
        ,['A','Y', 3]
    ];
    bP = viz.biPartite()
    .data(data)
    //    .edgeMode("straight")
    d3.select("g").call(bP);

    //ajax request a string
    fetch(window.location.href.split('.')[0])
        .then(function(response){
            return response.text()
        }).then(function(myText){
            parseFile([myText], "");
        });
    
    //console.log('mainEnd');
}

function clickIncrement(){
    //console.log("clickIncrement");
    if (parseResults !== null){
        // console.log('pre', counter, countIncrement, timeLast);
        lineIncrement = 1;
        // itterating through time until the last time is less than or equal to minuteIncrement
        for (; lineCursor + lineIncrement < parseLength  && (parseResults.data[lineCursor + lineIncrement][4] - timeCursor <= minuteIncrement); ++lineIncrement){
            // console.log('condition',parseResults.data[counter + countIncrement][4], timeLast);
        }
        processData();
        lineCursor += lineIncrement;
        // check if next itteration will overflow, if so reset to start
        if (!(lineCursor < parseLength)){
            lineCursor = 0;
            dataSet1 = [];
        }
        timeCursor = parseResults.data[lineCursor][4];
        // console.log('post',counter, countIncrement, parseResults.data[counter][4]);
        slider.noUiSlider.set(timeCursor);
        // console.log(timeCursor);
        // console.log('sliderUpdate', slider, parseInt(slider.noUiSlider.get()) +  minuteIncrement);
    }
}

function processData(){
    var returnArray = [];
    var colorGuide = {};
        //console.log(results.data.length);
        //loop through parsed data within increment interval to process further
    for (var i = 0 + lineCursor; i < parseLength && i <  lineCursor + lineIncrement; i++){
        var attack = parseResults.data[i][3];
        //check if attack, adding to the appropriate sender IP
        //console.log(results.data[i].slice(0,2));
        //console.log(returnArray);
        // if attack: color sender ip
        if (attack != '-'){
            console.log(attack);
            colorGuide[parseResults.data[i][0]] = '#FF0000';
        }
        //console.log(lineIncrement);
        //if high levels of connections, aggregate sender IPs to later be sorted and sliced.
        if (lineIncrement >= spamThreshold){

            filterSpam(i, returnArray);
        }
        else{
            returnArray.push(parseResults.data[i].slice(0,2).concat(1));
        }
    }
    //end of for loop
    //operations for machine learning
    dataSet2 = returnArray;
    //function1(dataSet1);
    //function2(dataSet2);
    dataSet1.push.apply(dataSet1, dataSet2);

    //viz framework optimizations
    if (returnArray.length > 300){
        returnArray.sort(function(a,b){return a[2] < b[2]})
        returnArray = returnArray.slice(0, Math.min(returnArray.length, 300)); //viz has a hard limit of 300
        //console.log(returnArray.length,returnArray);

    }
    //console.log('loop');
                
    d3.select("g").call(bP.data(returnArray).fill(fill(colorGuide)));
    //console.log(returnArray.length);
                
}

function filterSpam(i, returnArray){
    if(returnArray.length == 0){
        returnArray.push(parseResults.data[i].slice(0,2).concat(1));
    }
    else{
        var found = false;
        for (element of returnArray){
            //console.log(element[0], parseResults.data[i][0]);
            if (element[0] == parseResults.data[i][0]){
                element[2]++;
                //console.log('found');
                found = true;
                break
            }
            
        }
        if (!found){
            returnArray.push(parseResults.data[i].slice(0,2).concat(1));

        }

    }
    
}

function filterSpam2(returnArray){
    if(returnArray.length != 0){
        for (var i = 0; i < returnArray.length; i++){
            for(var j = i + 1; j < returnArray.length; j++){

            }
        }
        

    }
    
}

function fill(colorGuide){
    return function(d){ return colorGuide[d.primary] }
}

function parseFile(files, func){
    clearInterval(drawLoop);
    Papa.parse(files[0], {
        delimiter: ",",
        newline: "\n",
        header: false,
        skipEmptyLines: true,
        complete: function(results) {
        //console.log(results.data);
        parseResults = results;
        parseLength = parseResults.data.length;
        timeCursor = parseResults.data[0][4];
        sliderMin = timeCursor;
        sliderMax = parseResults.data[parseResults.data.length - 1][4];
        slider = document.getElementById('sliderContainer');
        sliderFactory();
        slider.setAttribute('disabled', true);
//        console.log('call sliderFactory...');
        console.log('results:');
        console.log(parseResults);
        // console.log(results);
        //clickIncrement();
        drawLoop = window.setInterval(clickIncrement, updateRate);
        //record first relative minute
        //console.log('timeLast:', timeLast);
        }
    });
}

//on Choose File
function handleFileSelect(evt) {
    //reset counter
    lineCursor = 0;
    //setup event
    files = evt.target.files; // FileList object
    // files is a FileList of File objects. List some properties.
    var output = [];
    for (var i = 0, f; f = files[i]; i++) {
        output.push('<li><strong>', escape(f.name), '</strong> (', f.type || 'n/a', ') - ',
                    f.size, ' bytes, last modified: ',
                    f.lastModifiedDate ? f.lastModifiedDate.toLocaleDateString() : 'n/a',
                    '</li>');
    }
    document.getElementById('list').innerHTML = '<ul>' + output.join('') + '</ul>';
    //parse file
    func = "";
    if (files.length == 1){
        parseFile(files, func);

    }
    clearInterval(drawLoop);
    drawLoop = window.setInterval(clickIncrement, updateRate);
}

function sliderFactory(){
    //console.log('sliderFactory', typeof sliderMin, typeof sliderMax);
    //console.log(slider.noUiSlider == null);
	//console.log("sliderFactory begin...")
    if (slider.noUiSlider == null){
    	//console.log("createSlider");
        noUiSlider.create(slider, {
            start: [ 0 ],
            step: minuteIncrement,
            range: {
                'min': [ parseInt(sliderMin) ],
                'max': [ parseInt(sliderMax) ]
            },
            connect: [true,false],
            disable: true
        });
    }
    else {
        slider.noUiSlider.updateOptions({
            range: {
                'min': [ parseInt(sliderMin) ],
                'max': [ parseInt(sliderMax) ]
            }
        });
    }
}




  // adjust the bl.ocks frame dimension. Not part of example.
  //d3.select(self.frameElement).style("height", "700px"); 
