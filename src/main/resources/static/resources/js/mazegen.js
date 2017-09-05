/**
 * 
 */

$("button#btnGenerate").on("click", function() {
	generate();
});

$("button#btnSolve").on("click", function() {
	solve();
});

var stompUrl = 'http://' + window.location.host + '/mazegen'; // _not_ /app/minesweeper
var stompSock = new SockJS(stompUrl);
var stomp = Stomp.over(stompSock);

function generate() {
	var payload = JSON.stringify({});
	stomp.send('/stomp/mazegen/generate', {}, payload); // /stomp/mazegen required
	window.setTimeout(function() {
		window.location = "mazegen";
	}, 100);
};

function solve() {
	var payload = JSON.stringify({});
	stomp.send('/stomp/mazegen/solve', {}, payload); // /stomp/mazegen required
	window.setTimeout(function() {
		window.location = "mazegen";
	}, 100);
};
