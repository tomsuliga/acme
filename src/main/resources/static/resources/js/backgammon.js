
var divBoard;
var sessionId;
var points;
var openPips;
var openPipsBear;
var bar1;
var bar2;
var bear1;
var bear2;
var currentSide;
var currentState;
var currentSelectedPoint;
var startOfGameFirstRoll;
var turnOver;
var gameOver;
var replay;

$(document).ready(function() {
	init();
});

$(document).on('click', '#btnNewGame', function() {
	let payload = JSON.stringify({ 'sessionId':sessionId});
	stomp.send('/stomp/backgammon/newGame', {}, payload);
	window.location.reload();
});

$(document).on('click', '#btnSaveGame', function() {
	console.log("Save Game");
	let payload = JSON.stringify({ 'sessionId':sessionId});
	stomp.send('/stomp/backgammon/saveGame', {}, payload);
});

$(document).on('click', '#btnLoadGame', function() {
	console.log("Load Game");
	$("#myModal").modal('show');
});

$(document).on('click', '.loadGameId', function() {
	let gameId = $(this).attr('data-gameId');
	console.log('Id = ' + gameId);
	let payload = JSON.stringify({ 'sessionId':sessionId, 'gameId':gameId, 'replay':true });
	stomp.send('/stomp/backgammon/loadGame', {}, payload);
});

function gameLoaded(incoming) {
	let ob = JSON.parse(incoming.body);
	window.location.reload();
}

function init() {
	divBoard = $("div#board");
	sessionId = divBoard.attr("data-sessionId"); // from Controller and HTML 
	replay = divBoard.attr("data-replay");
	points = [];
	openPips = [];
	openPipsBear = [];
	bar1 = [];
	bar2 = [];
	bear1 = [];
	bear2 = [];
	currentSide = 1; // 1 or 2
	currentState = "open";
	currentSelectedPoint = -1;
	startOfGameFirstRoll = true;
	turnOver = false;
	gameOver = false;
	
	for (let i=0;i<24;i++) {
		points[i] = new Array();
	}
	
	// create and move 15 pips each side
	// pip-index: 0-14, point-index: 0-23
	let pips1 = [ [0,0], [1,0], [2,11], [3,11], [4,11], [5,11], [6,11], [7,16], [8,16], [9,16], [10,18], [11,18], [12,18], [13,18], [14,18] ];
	let pips2 = [ [0,5], [1,5], [2,5], [3,5], [4,5], [5,7], [6,7], [7,7], [8,12], [9,12], [10,12], [11,12], [12,12], [13,23], [14,23] ];

	for (let i=0;i<15;i++) {
		createAndMovePip(pips1[i][0], pips1[i][1], 1);
		createAndMovePip(pips2[i][0], pips2[i][1], 2);
	}
	
	// create 24 dummy points
	for (let i=0;i<24;i++) {
		let p1 = document.createElement("div");
		$(p1).addClass("openPip"); $(p1).attr("id", "openPip_" + i); $(p1).attr("data-point", i); $(p1).attr("hidden", "hidden");
		divBoard.append(p1);
		let id = $(p1).attr("id");
		openPips[i] = id;
	}

	for (let i=0;i<2;i++) {
		let p1 = document.createElement("div");
		$(p1).addClass("openPip"); $(p1).attr("id", "openPip_" + (88+i)); $(p1).attr("data-point", (88+i)); $(p1).attr("hidden", "hidden");
		divBoard.append(p1);
		let id = $(p1).attr("id");
		openPipsBear[i] = id;
	}
	
	// create roll button
	let b1 = document.createElement("button");
	$(b1).addClass("btnRoll"); 
	$(b1).attr("id", "btnRoll"); 
		
	if (replay == "true") {
		$(b1).text("Replay Game");
		console.log("<> replay 1 = " + replay);
	} else {
		$(b1).text("Roll Dice");
		console.log("<> replay 2 = " + replay);
	}
	
	divBoard.append(b1);
	$(b1).animate({
	    'top': 65 + 12 + 6 * 56,
	    'left': 122 + 10 + 5 * 56}, 0
	);
	
	// playerSide, position
	createDice(1,1);
	createDice(2,9);
};

function createDice(ps, pos) {
	for (let i=0;i<4;i++) {
		let d = document.createElement("div");
		$(d).addClass("p" + ps + "Die"); 
		$(d).attr("id", "p" + ps + "Die" + (i+1)); 
		$(d).attr("hidden", "hidden");
		divBoard.append(d);
		$(d).animate({
		    'top': 65 + 6 * 56,
		    'left': 122 + 15 + (pos+i) * 56}, 0
		);		
	}
}

function createAndMovePip(index, point, side) {
	let p1 = document.createElement("div");
	$(p1).addClass("pip pip_" + side); 
	$(p1).attr("id", "pip_" + side + "_" + index);
	$(p1).attr("data-point", point); 
	$(p1).attr("data-side", side);
	divBoard.append(p1);
	movePipToSpot(p1, point, 0);
	let id = $(p1).attr("id");
	points[point].push(id);
};

function movePipToSpot(p1, point, delay) {
	let marginTop = 65;
	let marginLeft = 122;
	let startCol = 0;
	let startRow = 0;
	let pointCount = 0;
	let adjustmentNeeded = false;
	
	if (point == 88) {
		startCol = 13 * 56 + 28 - 1;
		if (currentSide == 1) {
			startRow = 18 * bear1.length;
		} else if (currentSide == 2) {
			startRow = (13 * 56) - 18 - (bear2.length * 18);
			if ($(p1).hasClass('openPip')) {
				startRow -= (56-18);
			} else {
				startRow -= (56-18);
				adjustmentNeeded = true;
			}
		}
	} else if (point <= 23) {
		pointCount = points[point].length;
	}
	
	if (point >= 0 && point <= 5) {
		startCol = (12-point) * 56;
		startRow = 12 * 56 - (pointCount * 56);
	} else if (point >= 6 && point <= 11) {
		startCol = (11-point) * 56;
		startRow = 12 * 56 - (pointCount * 56);
	} else if (point >= 12 && point <= 17) {
		startCol = (point-12) * 56;
		startRow = pointCount * 56;
	} else if (point >= 18 && point <= 23) {
		startCol = (point-11) * 56;
		startRow = pointCount * 56;
	}
	
	// stack pips
	let zoffset = 0;
	switch (pointCount) {
	default: break;
	case 6:
		if (point <= 11) {
			startRow = 12 * 56 - 28;
		} else {
			startRow = 0 * 56 + 28;
		}
		zoffset = 1;
		break;
	case 7:
		if (point <= 11) {
			startRow = 11 * 56 - 28;
		} else {
			startRow = 1 * 56 + 28;
		}
		zoffset = 1;
		break;
	case 8:
		if (point <= 11) {
			startRow = 10 * 56 - 28;
		} else {
			startRow = 2 * 56 + 28;
		}
		zoffset = 1;
		break;
	case 9:
		if (point <= 11) {
			startRow = 9 * 56 - 28;
		} else {
			startRow = 3 * 56 + 28;
		}
		zoffset = 1;
		break;
	case 10:
		if (point <= 11) {
			startRow = 8 * 56 - 28;
		} else {
			startRow = 4 * 56 + 28;
		}
		zoffset = 1;
		break;
	case 11:
		if (point <= 11) {
			startRow = 11 * 56;
		} else {
			startRow = 1 * 56;
		}
		zoffset = 2;
		break;
	case 12:
		if (point <= 11) {
			startRow = 10 * 56;
		} else {
			startRow = 2 * 56;
		}
		zoffset = 2;
		break;
	case 13:
		if (point <= 11) {
			startRow = 9 * 56;
		} else {
			startRow = 3 * 56;
		}
		zoffset = 2;
		break;
	case 14:
		if (point <= 11) {
			startRow = 8 * 56;
		} else {
			startRow = 4 * 56;
		}
		zoffset = 2;
		break;
	}
	
	$(p1).attr("data-point", point);
	$(p1).css("z-index", 11);
	$(p1).animate({
	    'top': marginTop + startRow,
	    'left': marginLeft + startCol}, delay, function() {
	    	$(p1).css("z-index", 1 + zoffset);
	    	if (point == 88 && !($(p1).hasClass('openPip'))) {
	    		//if (currentSide == 2) {
	    		//	$(p1).animate({
	    		//		'top': marginTop + startRow + 56,
	    		//		'left': marginLeft + startCol});
	    		//}
	    		$(p1).removeClass('pip');
	    		$(p1).addClass('pipBear');
	    		if (adjustmentNeeded) {
		    		$(p1).animate(
		    			{
		    		    'top': marginTop + startRow + (56-18),
		    		    'left': marginLeft + startCol
		    		    }, 0, function() {	    		
		    		    }
		    		);
	    		}
	    	}	    	
	    }
	);	
};

function movePipToBar(pip, side, delay) {
	let rowPos = 65 + 6 * 56; // middle
	const colPos = 122 + 6 * 56;
	let bar = [];
	
	if (side == 2) {
		rowPos += 56 * (2 + bar1.length) * 1;
	} else {
		rowPos -= 56 * (2 + bar2.length) * 1;
	}
	
	$(pip).css("z-index", 11);
	$(pip).animate({
	    'top': rowPos,
	    'left': colPos}, delay, function() {
	    	$(pip).css("z-index", 1);
	    }
	);		
};

var stompUrl = 'http://' + window.location.host + '/backgammon';
var stompSock = new SockJS(stompUrl);
var stomp = Stomp.over(stompSock);

stomp.connect({}, function(frame) {
	stomp.subscribe('/topic/backgammon/showPipsAllowedToMove', function(incoming) {
		showPipsAllowedToMove(incoming);
	});
	
	stomp.subscribe('/topic/backgammon/selectDestinationPoint', function(incoming) {
		selectDestinationPoint(incoming);
	});
	
	stomp.subscribe('/topic/backgammon/movingPip', function(incoming) {
		movingPip(incoming);
	});

	stomp.subscribe('/topic/backgammon/doComputerSide', function(incoming) {
		doComputerSide(incoming);
	});
	
	stomp.subscribe('/topic/backgammon/gameLoaded', function(incoming) {
		gameLoaded(incoming);
	});
});

/////////////////////////////////////////////////////////////////////////////

async function showPipsAllowedToMove(incoming) {
	console.log('incoming received msg: ' + incoming);
	let ob = JSON.parse(incoming.body);
	
	if (checkWin()) {
		return;
	}
	
	if (ob.turnOver) {
		if (ob.startTurn) {
			revealDice(ob, ob.side);
			await sleep(1000);
		}
		$('#btnRoll').text("No Move");
		$('#btnRoll').show();
		turnOver = true;
		return;
	} else {
		turnOver = false;
		$('#btnRoll').text("Roll Dice");
		console.log("<> replay 3 = " + replay);
	}
	
	hideOpenPips();

	if (ob.startTurn) { // here - need to show dice
		revealDice(ob, ob.side);
		await sleep(1000);
		highlightLegalPips(ob, true);

	} else { // here - do not need to show dice
		highlightLegalPips(ob, true);
	}
	currentState = "possibleSelect";
}

function revealDice(ob, ps) { // ps is playerSide
	if (ob.firstMove) {
		$('div#p1Die1').text(ob.diceRolled[0]);
		$('div#p2Die2').text(ob.diceRolled[1]);
		$('div#p1Die1').show(500, "linear", function() {
			$('div#p2Die2').show(500, "linear", function() {
			});
		});
		return;
	}
	
	$('div#p' + ps + 'Die1').text(ob.diceRolled[0]);
	$('div#p' + ps + 'Die2').text(ob.diceRolled[1]);
	if (ob.diceRolled[0] == ob.diceRolled[1]) {
		$('div#p' + ps + 'Die3').text(ob.diceRolled[0]);
		$('div#p' + ps + 'Die4').text(ob.diceRolled[1]);
		$('div#p' + ps + 'Die1').show(250, "linear", function() {
			$('div#p' + ps + 'Die2').show(250, "linear", function() {
				$('div#p' + ps + 'Die3').show(250, "linear", function() {
					$('div#p' + ps + 'Die4').show(250, "linear", function() {
					});
				});
			});
		});
	} else {
		$('div#p' + ps + 'Die1').show(500, "linear", function() {
			$('div#p' + ps + 'Die2').show(500, "linear", function() {
			});
		});
	}
}

function highlightLegalPips(ob, add) {
	// highlight legal pips
	if (ob.barOff) {
		let id = null;
		if (currentSide == 1) {
			id = bar1[bar1.length - 1];
		} else {
			id = bar2[bar2.length - 1];		
		}
		$('#' + id).removeClass('pipSelectedToMove');
		$('#' + id).addClass('pipAllowedToMove');
	} else {
		for (let i=0;i<ob.moveablePoints.length;i++) {
			let point = ob.moveablePoints[i];
			let index = points[point].length;
			let id = points[point][index-1];
			if (add) {
				$('#' + id).removeClass('pipSelectedToMove');
				$('#' + id).addClass('pipAllowedToMove');
			} else {
				$('#' + id).removeClass('pipAllowedToMove');
			}
		}	
	}
}

function hideOpenPips() {
	for (let i=0;i<24;i++) {
		let id = '#' + openPips[i];
		$(id).hide();
	}
	for (let i=0;i<2;i++) {
		let id = '#' + openPipsBear[i];
		$(id).hide();
	}
}

// temp - inefficient
function hideAllowedToMovePips() {
	for (let i=0;i<24;i++) {
		if (points[i].length > 0) {
			let id = '#' + points[i][points[i].length-1];
			$(id).removeClass("pipAllowedToMove");
		}
	}	
}

function hideDice() {
	for (let i=1;i<5;i++) {
		$('#p1Die' + i).hide();
		$('#p1Die' + i).removeClass('dieUsed');
		$('#p2Die' + i).hide();
		$('#p2Die' + i).removeClass('dieUsed');
	}
}

//dynamic - works after class being added
$(document).on('click', "div.pip", function() {	
	// only allow clicks when it is player's turn
	if (currentSide != 1) {
		return;
	}
	
	let point = $(this).attr("data-point");

	if (currentState == "possibleSelect") {
		// is allowed side?
		let side = $(this).attr("data-side");
		if (currentSide != side) {
			return;
		}
				
		if (point !== "bar1") {
			// is currently possible?
			let pointLen = points[point].length;
			let id = points[point][pointLen-1];	
			if (!$('#' + id).hasClass("pipAllowedToMove")) {
				return;
			}
		}
	}
	
	if (currentState == "possibleMove") {
		if (point === "bar1") {
			return;
		}
		// if is currently selected, de-select
		let pointLen = points[point].length;
		let id = '#' + points[point][pointLen-1];	
		if ($(id).hasClass("pipSelectedToMove")) {
			pipDeselected(point);
			return;
		}
		// if not action available on this dummy point, return
		let dummyId = '#' + openPips[point];
		if (!$(dummyId).hasClass("pipAllowedToMove")) {
			return;
		}
	}
	
	console.log("class div.pip clicked for point: " + point);
	pipSelectedToMove(point);
});

function selectDestinationPoint(incoming) {
	console.log("selectDestinationPoint")
	const ob = JSON.parse(incoming.body);

	// turn off other pip points and select single pip from point
	for (let i=0;i<points.length;i++) {
		let stack = points[i];
		if (stack.length > 0) {
			let id = stack[stack.length-1];
			$("#" + id).removeClass("pipAllowedToMove");
			$("#" + id).removeClass("pipSelectedToMove");
			// select from point
			if (i == ob.selectedPoint) {
				$("#" + id).addClass("pipSelectedToMove");
				currentSelectedPoint = i;
			}
		}
		hideOpenPips();
	}
	// select bar point
	if (ob.barOff) {
		let pip = null;
		if (currentSide == 1) {
			pip = bar1[bar1.length-1];
		} else {
			pip = bar2[bar2.length-1];
		}
		$('#' + pip).addClass("pipSelectedToMove");
		currentSelectedPoint = -99;
	}
	
	// turn on all move to open points
	for (let i=0;i<ob.moveablePoints.length;i++) {
		let point = ob.moveablePoints[i];
		console.log("point=" + point);
		if (point == 88 || point == 89) {
			let bearId = '#' + openPipsBear[point-88];
			movePipToSpot(bearId, point, 0);
			$(bearId).show();			
		} else {
			let openId = '#' + openPips[point];
			movePipToSpot(openId, point, 0);
			$(openId).show();
		}
	}

	currentState = "possibleMove";
}

async function movingPip(incoming) {
	console.log("movingPip")
	const ob = JSON.parse(incoming.body);
	console.log("from=" + ob.fromPoint + ", to=" + ob.toPoint);
	//let len = points[ob.fromPoint].length;
	
	let pip = null;
	if (ob.barOff) {
		if (currentSide == 1) {
			pip = bar1.pop();
		} else {
			pip = bar2.pop();
		}
	} else {
		pip = points[ob.fromPoint].pop();
	}
	currentState = "moving";
	movePipToSpot("#" + pip, ob.toPoint, 1000);
	await sleep(1000);
	
	if (ob.barHop) {
		let otherSidePip = points[ob.toPoint].pop();
		movePipToBar("#" + otherSidePip, ob.side, 1000);
		$('#' + otherSidePip).attr("data-point", "bar" + (currentSide == 1 ? "2" : "1"));
		if (currentSide == 1) {
			bar2.push(otherSidePip);
		} else {
			bar1.push(otherSidePip);
		}
		await sleep(1100);
		movePipToSpot("#" + pip, ob.toPoint, 500);
		await sleep(600);
	}
	
	if (ob.toPoint <= 23) {
		points[ob.toPoint].push(pip);
	} else if (currentSide == 1) {
		bear1.push(pip);
	} else if (currentSide == 2) {
		bear2.push(pip);
	}
	hideDiceUsed(ob);
	hideOpenPips();
	hideAllowedToMovePips();
	$("#" + pip).removeClass('pipSelectedToMove');

	if (checkWin()) {
		return;
	}
	
	if (!ob.done) {
		currentState = "possibleSelect";
	}
	
	if (ob.turnOver) {
		$('#btnRoll').text("Roll Dice");
		console.log("<> replay 4 = " + replay);
		handleTurnOverPlayer();
	} else {
		let payload = JSON.stringify({ 'sessionId':sessionId });
		stomp.send('/stomp/backgammon/continuePlayerTurn', {}, payload);
	}
}

function hideDiceUsed(ob) {
	for (let i=0;i<ob.diceUsed.length;i++) {
		if (ob.diceUsed[i] == true) {
			if (ob.firstMove) {
				if (i==0) {
					let id = "#p1Die" + (i+1);
					$(id).addClass("dieUsed");
				} else {
					let id = "#p2Die" + (i+1);
					$(id).addClass("dieUsed");					
				}
			} else {
				let id = "#p" + currentSide + "Die" + (i+1);
				$(id).addClass("dieUsed");
			}
		}
	}
}

function handleTurnOverPlayer() {
	hideOpenPips();
	hideDice();
	
	turnOver = false;
	var payload = JSON.stringify({ 'sessionId':sessionId });
	stomp.send('/stomp/backgammon/switchToComputerSide', {}, payload);
}

function handleTurnOverComputer() {
	hideOpenPips();
	hideDice();
	
	currentSide = 1;
	$('#btnRoll').show();
}

async function doComputerSide(incoming) {
	const ob = JSON.parse(incoming.body);
	currentSide = ob.side;

	if (ob.startTurn) {
		revealDice(ob, 2);	
		await sleep(1000);	
	}
	
	if (ob.noMove) {
		$('#btnRoll').text("No Computer Move");
		$('#btnRoll').show();
		await sleep(1000);
		$('#btnRoll').hide();
		$('#btnRoll').text("Roll Dice");
		console.log("<> replay 5 = " + replay);
		handleTurnOverComputer();
		return;
	}
	
	// Highlight pip to move
	let pip = null;
	if (ob.barOff) {
		if (currentSide == 1) {
			pip = bar1.pop();
		} else {
			pip = bar2.pop();
		}
	} else {
		pip = points[ob.fromPoint].pop();
	}
	
	$("#" + pip).addClass("pipSelectedToMove"); // yellow
	await sleep(1000);
	
	// Highlight destination point - open pip
	let toPoint = ob.toPoint;
	console.log("to point=" + toPoint);
	let openId = '#' + openPips[toPoint];
	if (toPoint == 88) {
		openId = '#openPip_89';
	}
	movePipToSpot(openId, toPoint, 0);
	$(openId).show();
	await sleep(1000);
	
	currentState = "moving";
	movePipToSpot("#" + pip, ob.toPoint, 1000);
	await sleep(1000);
	
	if (ob.barHop) {
		let otherSidePip = points[ob.toPoint].pop();
		movePipToBar("#" + otherSidePip, ob.side, 1000);
		$('#' + otherSidePip).attr("data-point", "bar" + (currentSide == 1 ? "2" : "1"));
		if (currentSide == 1) {
			bar2.push(otherSidePip);
		} else {
			bar1.push(otherSidePip);
		}
		await sleep(1100);
		movePipToSpot("#" + pip, ob.toPoint, 500);
		await sleep(600);
	}
	
	if (ob.toPoint <= 23) {
		points[ob.toPoint].push(pip);
	} else if (currentSide == 1) {
		bear1.push(pip);
	} else if (currentSide == 2) {
		bear2.push(pip);
	}

	hideDiceUsed(ob);
	hideOpenPips();
	$("#" + pip).removeClass('pipSelectedToMove');
	
	if (checkWin()) {
		return;
	}
	
	// end turn?
	if (ob.turnOver) {
		handleTurnOverComputer();
	} else {
		var payload = JSON.stringify({ 'sessionId':sessionId });
		stomp.send('/stomp/backgammon/continueComputerTurn', {}, payload);
	}
}

//dynamic - works after class being added
$(document).on('click', "button#btnRoll", function() {
	if (gameOver) {
		return;
	}
	
	$('button#btnRoll').hide();
	$('button#btnRoll').text('Roll Dice');
	console.log("<> replay 7 = " + replay);
	let payload = JSON.stringify({ 'sessionId':sessionId });
	if (startOfGameFirstRoll) {
		startOfGameFirstRoll = false;
		$('button#btnRoll').text("Roll Dice"); // hidden, for next reveal
		console.log("<> replay 8 = " + replay);
		$('button#btnRoll').animate({
		    'top': 65 + 12 + 6 * 56,
		    'left': 122 + 10 + 5 * 56}, 0
		);
		stomp.send('/stomp/backgammon/startOfGameFirstRoll', {}, payload);
	} else if (turnOver) {
		handleTurnOverPlayer();
	}
	else {
		stomp.send('/stomp/backgammon/startPlayerTurn', {}, payload);
	}
});

function pipSelectedToMove(point) {
	console.log('point clicked: ' + point);
	let payload = null;
	if (point == 'bar1') {
		payload = JSON.stringify({ 'sessionId':sessionId, 'selectedPoint':-99, 'barOff':true});
	} else {
		payload = JSON.stringify({ 'sessionId':sessionId, 'selectedPoint':point});
	}
	stomp.send('/stomp/backgammon/pipSelectedToMove', {}, payload);
};

function pipDeselected(point) {
	let payload = JSON.stringify({ 'sessionId':sessionId});
	stomp.send('/stomp/backgammon/pipDeselected', {}, payload);
}

$(document).on('click', "div.openPip", function() {
	let pointTo = $(this).attr("data-point");
	let payload = JSON.stringify({ 'sessionId':sessionId, 'fromPoint':currentSelectedPoint, 'toPoint':pointTo });
	stomp.send('/stomp/backgammon/movePip', {}, payload);
});

function sleep(ms) {
	  return new Promise(resolve => setTimeout(resolve, ms));
}

$(document).on('click', '#btnDebugPoints', function() {
	for (let i=0;i<24;i++) {
		if (points[i].length > 0) {
			let pip = points[i][0];
			console.log(i + ' ' + points[i].length + '  p' + $('#' + pip).attr("data-side"));
		} else {
			console.log(i + ' ' + points[i].length);			
		}
	}
	console.log('bar1 ' + bar1.length);
	console.log('bar2 ' + bar2.length);

	console.log('bear1 ' + bear1.length);
	console.log('bear2 ' + bear2.length);
	
	console.log('replay = ' + replay);

	let payload = JSON.stringify({ 'sessionId':sessionId});
	stomp.send('/stomp/backgammon/debugPoints', {}, payload);
});

function checkWin() {
	if (bear1.length == 15) {
		gameOver = true;
		$('button#btnRoll').text("You Win!");
		$('button#btnRoll').show();
		return true;
	} else if (bear2.length == 15) {
		gameOver = true;
		$('button#btnRoll').text("You Lose!");
		$('button#btnRoll').show();
		return true;
	}
	return false;
}





