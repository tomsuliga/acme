
var divBoard;
var sessionId;
var points = new Array();
var openPips = new Array();
var currentSide = 1;
var currentState = "open";
var currentSelectedPoint;
var currentSelectedId;
var possibleSelectMessage;

$(document).ready(function() {
	divBoard = $("div#board");
	sessionId = divBoard.attr("data-sessionId");
	initBoard();
});

function initBoard() {
	for (let i=0;i<24;i++) {
		points[i] = new Array();
	}
	
	// create and move 15 pips each side
	// id-index, point, side
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
	
	// create roll button
	let b1 = document.createElement("button");
	$(b1).addClass("btnRoll"); $(b1).attr("id", "btnRoll"); $(b1).text("Roll");
	divBoard.append(b1);
	$(b1).animate({
	    'top': 65 + 12 + 6 * 56,
	    'left': 122 + 38 + 5 * 56}, 0
	);
	
	// create dice
	for (let i=0;i<4;i++) {
		let d = document.createElement("div");
		$(d).addClass("divDie"); $(d).attr("id", "divDie" + (i+1)); $(d).attr("hidden", "hidden");
		divBoard.append(d);
		$(d).animate({
		    'top': 65 + 6 * 56,
		    'left': 122 + 15 + (8+i) * 56}, 0
		);		
	}
};

function createAndMovePip(index, point, side) {
	let p1 = document.createElement("div");
	$(p1).addClass("pip pip_" + side); $(p1).attr("id", "pip_" + side + "_" + index);
	$(p1).attr("data-point", point); $(p1).attr("data-side", side);
	divBoard.append(p1);
	movePipToSpot(p1, point, 0, false);
	let id = $(p1).attr("id");
	points[point].push(id);
};

function movePipToSpot(p1, point, delay, notifyServer) {
	let marginTop = 65;
	let marginLeft = 122;
	let startCol = 0;
	let startRow = 0;
	
	if (point >= 0 && point <= 5) {
		startCol = (12-point) * 56;
		startRow = 12 * 56 - (points[point].length * 56);
	} else if (point >= 6 && point <= 11) {
		startCol = (11-point) * 56;
		startRow = 12 * 56 - (points[point].length * 56);
	} else if (point >= 12 && point <= 17) {
		startCol = (point-12) * 56;
		startRow = points[point].length * 56;
	} else if (point >= 18 && point <= 23) {
		startCol = (point-11) * 56;
		startRow = points[point].length * 56;
	}
	
	$(p1).attr("data-point", point);
	$(p1).css("z-index", 11);
	$(p1).animate({
	    'top': marginTop + startRow,
	    'left': marginLeft + startCol}, delay, function() {
	    	$(p1).css("z-index", 1);
	    	//if (currentState == "moving") {
	    	//	if (notifyServer) {
	    	//		var payload = JSON.stringify({ 'sessionId':sessionId});
	    	//		stomp.send('/stomp/backgammon/continueTurn', {}, payload); // /stomp/minesweeper required
	    	//	}
	    	//}
	    }
	);		
};

var stompUrl = 'http://' + window.location.host + '/backgammon'; // _not_ /app/minesweeper
var stompSock = new SockJS(stompUrl);
var stomp = Stomp.over(stompSock);

stomp.connect({}, function(frame) {
	stomp.subscribe('/topic/backgammon/clickResult', function(incoming) { // /topic/result required
		console.log('incoming received msg: ' + incoming);
	});
	
	stomp.subscribe('/topic/backgammon/showPipsAllowedToMove', function(incoming) {
		showPipsAllowedToMove(incoming);
	});
	
	stomp.subscribe('/topic/backgammon/selectDestinationPoint', function(incoming) { // /topic/result required
		selectDestinationPoint(incoming);
	});
	
	stomp.subscribe('/topic/backgammon/movingPip', function(incoming) { // /topic/result required
		movingPip(incoming);
	});
});

/////////////////////////////////////////////////////////////////////////////

function showPipsAllowedToMove(incoming) {
	console.log('incoming received msg: ' + incoming);
	let ob = JSON.parse(incoming.body);
	
	hideOpenPips();

	if (currentState != "possibleMove") { // why needed? Showing dice
		$('div#divDie1').text(ob.diceRolled[0]);
		$('div#divDie2').text(ob.diceRolled[1]);
		if (ob.diceRolled[0] == ob.diceRolled[1]) {
			$('div#divDie3').text(ob.diceRolled[0]);
			$('div#divDie4').text(ob.diceRolled[1]);
			$('div#divDie1').show(300, "linear", function() {
				$('div#divDie2').show(300, "linear", function() {
					$('div#divDie3').show(300, "linear", function() {
						$('div#divDie4').show(300, "linear", function() {
							highlightLegalPips(ob);
						});
					});
				});
			});
		} else {
			$('div#divDie1').show(400, "linear", function() {
				$('div#divDie2').show(400, "linear", function() {
					highlightLegalPips(ob);
				});
			});
		}
	} else { // why here? not showing dice
		highlightLegalPips(ob);
	}
	currentState = "possibleSelect";
}

function highlightLegalPips(ob) {
	// highlight legal pips
	for (let i=0;i<ob.moveablePoints.length;i++) {
		let point = ob.moveablePoints[i];
		let index = points[point].length;
		let id = points[point][index-1];
		$('#' + id).removeClass('pipSelectedToMove');
		$('#' + id).addClass('pipAllowedToMove');
	}	
}

function hideOpenPips() {
	for (let i=0;i<24;i++) {
		let id = '#' + openPips[i];
		$(id).hide();
	}
}

//dynamic - works after class being added
$(document).on('click', "div.pip", function() {	
	let point = $(this).attr("data-point");

	if (currentState == "possibleSelect") {
		// is allowed side?
		let side = $(this).attr("data-side");
		if (currentSide != side) {
			return;
		}
				
		// is currently possible?
		let pointLen = points[point].length;
		let id = points[point][pointLen-1];	
		if (!$('#' + id).hasClass("pipAllowedToMove")) {
			return;
		}
	}
	
	if (currentState == "possibleMove") {
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
	// turn on all move to open points
	for (let i=0;i<ob.moveablePoints.length;i++) {
		let point = ob.moveablePoints[i];
		console.log("point=" + point);
		let id = '#' + openPips[point];
		movePipToSpot(id, point, 0);
		$(id).show();
	}

	currentState = "possibleMove";
}

async function movingPip(incoming) {
	console.log("movingPip")
	const ob = JSON.parse(incoming.body);
	console.log("from=" + ob.fromPoint + ", to=" + ob.toPoint);
	//let len = points[ob.fromPoint].length;
	let pip = points[ob.fromPoint].pop();
	currentState = "moving";
	movePipToSpot("#" + pip, ob.toPoint, 1000, true);
	await sleep(1000);
	points[ob.toPoint].push(pip);
	console.log("isDone=" + ob.done);
	$("#" + pip).removeClass('pipSelectedToMove');
	
	if (!ob.done) {
		currentState = "possibleSelect";
	}
	
	for (let i=0;i<ob.diceUsed.length;i++) {
		if (ob.diceUsed[i] == true) {
			let id = "#divDie" + (i+1);
			$(id).addClass("dieUsed");
		}
	}
	
	if (ob.turnOver) {
		hideOpenPips();
		for (let i=1;i<5;i++) {
			$('#divDie' + i).hide();
			$('#divDie' + i).removeClass('dieUsed');
		}
		$('#btnRoll').show();
	} else {
		let payload = JSON.stringify({ 'sessionId':sessionId });
		stomp.send('/stomp/backgammon/continueTurn', {}, payload);
	}
}

//dynamic - works after class being added
$(document).on('click', "button#btnRoll", function() {
	$('button#btnRoll').hide();
	var payload = JSON.stringify({ 'sessionId':sessionId });
	stomp.send('/stomp/backgammon/comeOutRoll', {}, payload); // /stomp/minesweeper required
});

function pipSelectedToMove(point) {
	console.log('point clicked: ' + point);
	var payload = JSON.stringify({ 'sessionId':sessionId, 'selectedPoint':point});
	stomp.send('/stomp/backgammon/pipSelectedToMove', {}, payload);
};

function pipDeselected(point) {
	var payload = JSON.stringify({ 'sessionId':sessionId});
	stomp.send('/stomp/backgammon/pipDeselected', {}, payload);
}

$(document).on('click', "div.openPip", function() {
	let pointTo = $(this).attr("data-point");
	var payload = JSON.stringify({ 'sessionId':sessionId, 'fromPoint':currentSelectedPoint, 'toPoint':pointTo });
	stomp.send('/stomp/backgammon/movePip', {}, payload);
});

$(document).on('click', '#btnNewGame', function() {
	var payload = JSON.stringify({ 'sessionId':sessionId});
	stomp.send('/stomp/backgammon/newGame', {}, payload);
});

function sleep(ms) {
	  return new Promise(resolve => setTimeout(resolve, ms));
}
