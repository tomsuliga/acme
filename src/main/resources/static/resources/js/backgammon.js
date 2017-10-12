
var sessionId;

$(document).ready(function() {
	let board = $("div#board");
	sessionId = board.attr("data-sessionId");
	initBoard();
});

function initBoard() {
	setPoint(1,2,1);
	setPoint(6,5,2);
	setPoint(8,3,2);
	setPoint(12,5,1);
	
	setPoint(13,5,2);
	setPoint(17,3,1);
	setPoint(19,5,1);
	setPoint(24,2,2);
	
	// bear
	let row = 0;
	let col = 13;
	let col2 = 6;
	for (let i=0;i<14;i++) {
		$('div#' + (row+i) + '_' + col).addClass('bear');
		if (i != 6) {
			$('div#' + (row+i) + '_' + col2).addClass('bear');
		}
 	}
	for (let i=0;i<13;i++) {
		$('div#' + 6 + '_' + i).addClass('bear');
	}
};

function setPoint(index, count, side) {
	if (index <= 12) {
		let row = 12;
		let col = 13-index;
		if (col < 7) {
			col -= 1;
		}
		for (let i=0;i<count;i++) {
			$('div#' + (row-i) + '_' + col).addClass('pip' + side);
		}
		// temp
		//$('div#' + (row-count+1) + '_' + col).addClass('pip' + 'High');
	} else {
		let row = 0;
		let col = index-13;
		if (col > 5) {
			col += 1;
		}
		for (let i=0;i<count;i++) {
			$('div#' + (row+i) + '_' + col).addClass('pip' + side);
		}
		// temp
		//$('div#' + (row+count-1) + '_' + col).addClass('pip' + 'High');
	}
};

// dynamic - works after class being added
$(document).on('click', "div.pip1", function() {
	console.log('pip1 clicked');
	$(this).addClass('pipHigh');
});

$(document).on('click', "div.pip2", function() {
	console.log('pip2 clicked');
});

$('button#btnRollDice').on('click', function() {
	initGame();
//	alert("roll dice");
});

$('button#btnMovePip').on('click', function() {
//	alert('move pip');
});

var stompUrl = 'http://' + window.location.host + '/backgammon'; // _not_ /app/minesweeper
var stompSock = new SockJS(stompUrl);
var stomp = Stomp.over(stompSock);

stomp.connect({}, function(frame) {
	stomp.subscribe('/topic/backgammonResult', function(incoming) { // /topic/result required
		console.log('incoming received msg');
		//const primeNumberResult = JSON.parse(incoming.body);
		//console.log('myCalc result = ' + primeNumberResult.result);
	});
});

function initGame() {
	var payload = JSON.stringify({ 'sessionId':sessionId, 'command':'initGame'});
	stomp.send('/stomp/backgammon/initGame', {}, payload); // /stomp/minesweeper required
};

