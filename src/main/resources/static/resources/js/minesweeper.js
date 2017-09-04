// Javascript and jQuery for Minesweeper
var numCols = 0;
var numRows = 0;
var numMines = 0;
var possibleMines = 0;
var recursiveCount = 0;
var sessionId;
var gameOver = false;

// turn off all logging:
console.log = function() {};

$(document).ready(function() {
	let gameGrid = $("div#gameGrid");
	numCols = parseInt(gameGrid.attr("data-numcols"));
	numRows = parseInt(gameGrid.attr("data-numrows"));
	sessionId = gameGrid.attr("data-sessionId");
	const w = numCols * 32 + 2;
	const h = numRows * 32 + 2;
	countMinesLeft();
	checkWin();
});

$("div.cellUnknown").on("click", function() {
	if ($(this).attr("data-visible") === "true") {
		return;
	}
	if (gameOver) {
		return;
	}
	//console.log("Clicked id = >" + $(this).attr("id") + "<");
	loadColsAndRows();
	recursiveCount = 0;
	discover($(this));
	discoverMore($(this));
	//console.log("recursiveCount=" + recursiveCount);
	checkWin();
});

function loadColsAndRows() {
	let gameGrid = $("div#gameGrid");
	if (numMines === 0) {
		numMines = parseInt(gameGrid.attr("data-nummines"));
		possibleMines = 0;
	}
}

$("div.cellKnown").on("contextmenu", function() { 
	return false;
});

$("div.cellUnknown").on("contextmenu", function() { 
	if (gameOver) {
		return false;
	}
	// note: contextmenu captures right mouse click event
	loadColsAndRows();
	const cell = $(this);
	if (cell.attr("data-visible") === "true") {
		return false;
	}
	if (cell.hasClass('possibleMine')) {
		possibleMines--;
	} else {
		possibleMines++;
	}
	$('#minesLeft').text(numMines - possibleMines);
	cell.toggleClass('possibleMine');
	var payload = JSON.stringify({ 'sessionId':sessionId, 
								   'col':cell.attr("data-col"), 
								   'row':cell.attr("data-row"), 
								   'possibleMine':cell.hasClass('possibleMine')});
	//console.log('payload=' + payload);
	stomp.send('/stomp/minesweeper/possibleMine', {}, payload); // /stomp/minesweeper required
	checkWin();
	// false disables right mouse click context menu
	return false;
});

function discoverMore(e) {
	recursiveCount++;
	if (e.attr("data-known") === " ") {
		const col = parseInt(e.attr("data-col"));
		const row = parseInt(e.attr("data-row"));
		for (let colOffset=-1;colOffset<2;colOffset++) {
			for (let rowOffset=-1;rowOffset<2;rowOffset++) {
				if ((colOffset == 0 && rowOffset == 0)
				  || (colOffset + col < 0)
				  || (colOffset + col >= numCols)
				  || (rowOffset + row < 0)
				  || (rowOffset + row >= numRows))  {
					continue;
				}
				let cellToCheck = $("div#" + (col+colOffset) + "_" + (row+rowOffset));
				const tempValue = cellToCheck.attr("data-known");
				const tempVisible = cellToCheck.attr("data-visible");
				if (tempValue !== "*" && tempVisible === "false") {
					discover(cellToCheck);
					discoverMore(cellToCheck); // recursive 
				}
			}
		}
	}
}

function discover(cell) {
	cell.removeClass("cellUnknown");
	cell.addClass("cellKnown");
	const dataKnown = cell.attr("data-known");
	cell.text(dataKnown);
	cell.attr("data-visible", "true");
	if (dataKnown === '*') {
		cell.removeClass('possibleMine');
		cell.addClass('visibleMine');
		youLose();
	} else {
		cell.removeClass('possibleMine');
	}
	var payload = JSON.stringify({ 'sessionId':sessionId, 'col':cell.attr("data-col"), 'row':cell.attr("data-row"), 'possibleMine':false});
	stomp.send('/stomp/minesweeper/cellVisible', {}, payload); // /stomp/minesweeper required
}

function youLose() {
	//console.log("Game Over");
	const primaryMsg = $('div#primaryMsg');
	primaryMsg.text('Game Over');
	primaryMsg.removeClass('playing');
	primaryMsg.addClass('lose');
	gameOver = true;
}

function countMinesLeft() {
	loadColsAndRows();
	for (let col=0;col<numCols;col++) {
		for (let row=0;row<numRows;row++) {
			let e = $('div#' + col + '_' + row);
			if (e.hasClass("possibleMine")) {
				possibleMines++;
			}
		}
	}
	$('#minesLeft').text(numMines - possibleMines);
}

function checkWin() {
	let won = true;
	for (let col=0;col<numCols;col++) {
		for (let row=0;row<numRows;row++) {
			let e = $('div#' + col + '_' + row);
			if (e.attr("data-visible") === "false" && e.attr("data-known") === "*" && e.hasClass("possibleMine") === false) {
				won = false;
				//console.log("false 1");
				//break;
			}
			if (e.attr("data-visible") === "false" && e.attr("data-known") !== "*") {
				//console.log("col=" + col + ", row=" + row);
				won = false;
				//console.log("false 2");
				//break;
			}
			if (e.attr("data-visible") === "true" && e.attr("data-known") === "*" && e.hasClass("possibleMine") === false) {
				youLose();
			}
		}
		//if (!won) {
		//	break;
		//}
	}
	if (won) {
		const primaryMsg = $('div#primaryMsg');
		primaryMsg.text('You Win!!!');
		primaryMsg.removeClass('playing');
		primaryMsg.addClass('win');
		gameOver = true;
	}
}

var stompUrl = 'http://' + window.location.host + '/minesweeper'; // _not_ /app/minesweeper
var stompSock = new SockJS(stompUrl);
var stomp = Stomp.over(stompSock);

$("button#btnNewGameSmall").on("click", function() {
	newGame(12,10);
});

$("button#btnNewGameMedium").on("click", function() {
	newGame(15,15);
});

$("button#btnNewGameLarge").on("click", function() {
	newGame(22,16);
});

function newGame(cols, rows) {
	gameOver = false;
	var payload = JSON.stringify({ 'sessionId':sessionId, 'numCols':cols, 'numRows':rows});
	//console.log("payload=" + payload);
	stomp.send('/stomp/minesweeper/newGame', {}, payload); // /stomp/minesweeper required
	window.setTimeout(function() {
		window.location = "minesweeper";
	}, 50);
};





