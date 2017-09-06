/**
 * 
 */

var globalCount = 0;
var globalTotalMillis = 0;

$('button#btnStart').on('click', function() {
	$('select#selectBits').attr("disabled", true);
	$('select#selectThreads').attr("disabled", true);
	$('button#btnStart').attr("disabled", true);
	$('button#btnStop').attr("disabled", false);
	$('button#btnClear').attr("disabled", true);
	$('label#statusMsgRunning').text('Running...');
	let numBits = $('select#selectBits').val();
	let numThreads = $('select#selectThreads').val();
	primegenStart(numBits, numThreads);
});

$('button#btnStop').on('click', function() {
	$('select#selectBits').attr("disabled", false);
	$('select#selectThreads').attr("disabled", false);
	$('button#btnStart').attr("disabled", false);
	$('button#btnStop').attr("disabled", true);	
	$('button#btnClear').attr("disabled", false);
	$('label#statusMsgRunning').text('Stopped');
	primegenStop();
});

$('button#btnClear').on('click', function() {
	$('label#statusMsgCount').text('0');
	$('label#statusMsgAvgTime').text('0');
	$('textarea#taResult').text('');
	globalCount = 0;
	globalTotalMillis = 0;
});

var stompUrl = 'http://' + window.location.host + '/primegen';
var stompSock = new SockJS(stompUrl);
var stomp = Stomp.over(stompSock);

function primegenStart(numBits, numThreads) {
	var payload = JSON.stringify({ 'numBits':numBits, 'numThreads':numThreads});
	stomp.send('/stomp/primegen/start', {}, payload); // /stomp/primegen required
};

function primegenStop() {
	var payload = JSON.stringify({ '':'' });
	stomp.send('/stomp/primegen/stop', {}, payload); // /stomp/primegen required
};

stomp.connect({}, function(frame) {
	stomp.subscribe('/topic/primeNumberResult', function(incoming) { // /topic/result required
		//console.log('incoming received msg');
		const primeNumberResult = JSON.parse(incoming.body);
		//console.log('myCalc result = ' + primeNumberResult.result);
		let ta = $('textarea#taResult');
		let current = ta.text();
		const threadId = primeNumberResult.threadId;
		const numBits = primeNumberResult.numBits;
		const numTries = primeNumberResult.numTries;
		const millis = primeNumberResult.millis;
		const primeNum = primeNumberResult.primeNumberDisplay;
		ta.text(current + threadId + '\t' + numBits + '\t' + numTries + '\t' + millis + '\t' + primeNum + '\n');
		
		// auto scroll to bottom of new text
		ta.scrollTop(ta[0].scrollHeight);
		
		globalCount++;
		globalTotalMillis += millis;
		$('label#statusMsgCount').text(globalCount);
		$('label#statusMsgAvgTime').text(Math.round(globalTotalMillis / globalCount));
	});
});









