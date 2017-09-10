/**
 * 
 */
var globalLastSelectedId = null;
var globalDirection = 'across';
var globalShowErrors = false;
var globalLastClueLineId = null;

$('.numberAndLetter').on('click', function() {
	numberAndLetterClicked(this);
});

function numberAndLetterClicked(elem) {
	let e = $(elem);

	let id = e.attr('id');
	let col = parseInt(e.attr('data-col'));
	let row = parseInt(e.attr('data-row'));
	
	if ($('#cellLetter_' + col + '_' + row).hasClass('cellLetterBlock')) {
		return;
	}
	
	removePrvious();
	
	togglePrimary(col,row);
	
	switch (globalDirection) {
	case 'across': 
		//console.log('across');
		// if same as last one, toggle opposite
		if ((id == globalLastSelectedId && isDownWord(col,row)) || !isAcrossWord(col,row)) {
			toggleDown(col,row);
			globalDirection = 'down';
		} else {
			toggleAcross(col,row);			
		}
		break;
	case 'down': 
		//console.log('down');
		// if same as last one, toggle opposite
		if ((id == globalLastSelectedId && isAcrossWord(col,row)) || !isDownWord(col,row)) {
			toggleAcross(col,row);
			globalDirection = 'across';
		} else {
			toggleDown(col,row);
		}
		break;
	}
		
	globalLastSelectedId = id;
	//console.log(id + ' ' + col + ' ' + row);
	
	selectClueLine(col, row);
}

function selectClueLine(col, row) {
	// find head cell for clue line
	if (globalDirection == 'across') {
		while (true) {
			if (col == 0) {
				break;
			}
			col--;
			let e = $('#cellLetter_' + col + '_' + row);
			if (e.hasClass('cellLetterBlock')) {
				col++;
				break;
			}
		}
	} else {
		while (true) {
			if (row == 0) {
				break;
			}
			row--;
			let e = $('#cellLetter_' + col + '_' + row);
			if (e.hasClass('cellLetterBlock')) {
				row++;
				break;
			}
		}
		
	}
	let clueLineId = '#' + globalDirection + '_clue_' + col + '_' + row;
	if (clueLineId == globalLastClueLineId) {
		// same ell, but cannot toggle direction
		return;
	}
	let clueLine = $(clueLineId);
	clueLine.addClass('selectedClueLine');
	if (globalLastClueLineId != null) {
		$(globalLastClueLineId).removeClass('selectedClueLine');
	}
	globalLastClueLineId = clueLineId;
	
	// scroll
	var temp = document.getElementById(globalDirection + '_clue_' + col + '_' + row);
	if (temp != null) {
		temp.scrollIntoView(false);
	}
}

function isAcrossWord(col,row) {
	let found = false;
	
	// Right
	for (let i=col+1;i<15;i++) {
		if (i==15) {
			break;
		}
		let cellLetter = $('#cellLetter_' + i + '_' + row);
		if (!cellLetter.hasClass('cellLetterBlock'))  {
			found = true;
			break;
		} else {
			break;
		}
	}
	
	// Left
	for (let i=col-1;i>=0;i--) {
		if (i==-1) {
			break;
		}
		let cellLetter = $('#cellLetter_' + i + '_' + row);
		if (!cellLetter.hasClass('cellLetterBlock')) {
			found = true;
			break;
		} else {
			break;
		}
	}
	return found;
}

function isDownWord(col,row) {
	let found = false;
	
	// Down
	for (let i=row+1;i<15;i++) {
		if (i==15)  {
			break;
		}
		let cellLetter = $('#cellLetter_' + col + '_' + i);
		if (!cellLetter.hasClass('cellLetterBlock')) {
			found = true;
			break;
		} else {
			break;
		}
	}
	
	// Up
	for (let i=row-1;i>=0;i--) {
		if (i==-1) {
			break;
		}
		let cellLetter = $('#cellLetter_' + col + '_' + i);
		if (!cellLetter.hasClass('cellLetterBlock')) {
			found = true;
			break;
		} else {
			break;
		}
	}
	return found;
}

function removePrvious() {
	if (globalLastSelectedId == null) {
		return;
	}
	
	let e = $('#' + globalLastSelectedId);
	let id = e.attr('id');
	let col = parseInt(e.attr('data-col'));
	let row = parseInt(e.attr('data-row'));
	togglePrimary(col,row);
	if (globalDirection == 'across') {
		toggleAcross(col,row);
	} else {
		toggleDown(col,row);
	}
}

function togglePrimary(col,row) {
	let cellNumber = $('#cellNumber_' + col + '_' + row);
	let cellLetter = $('#cellLetter_' + col + '_' + row);
	cellNumber.toggleClass('selectedPrimary');
	cellLetter.toggleClass('selectedPrimary');
}

function toggleAcross(col, row) {
	// Right
	for (let i=col+1;i<15;i++) {
		let cellNumber = $('#cellNumber_' + i + '_' + row);
		let cellLetter = $('#cellLetter_' + i + '_' + row);
		if (cellLetter.hasClass('cellLetterBlock')) break;
		cellNumber.toggleClass('selectedSecondary');
		cellLetter.toggleClass('selectedSecondary');
	}
	
	// Left
	for (let i=col-1;i>=0;i--) {
		let cellNumber = $('#cellNumber_' + i + '_' + row);
		let cellLetter = $('#cellLetter_' + i + '_' + row);
		if (cellLetter.hasClass('cellLetterBlock')) break;
		cellNumber.toggleClass('selectedSecondary');
		cellLetter.toggleClass('selectedSecondary');
	}
}

function toggleDown(col,row) {
	// Down
	for (let i=row+1;i<15;i++) {
		let cellLetter = $('#cellLetter_' + col + '_' + i);
		if (cellLetter.hasClass('cellLetterBlock')) break;
		let cellNumber = $('#cellNumber_' + col + '_' + i);
		cellNumber.toggleClass('selectedSecondary');
		cellLetter.toggleClass('selectedSecondary');
	}
	
	// Up
	for (let i=row-1;i>=0;i--) {
		let cellLetter = $('#cellLetter_' + col + '_' + i);
		if (cellLetter.hasClass('cellLetterBlock')) break;
		let cellNumber = $('#cellNumber_' + col + '_' + i);
		cellNumber.toggleClass('selectedSecondary');
		cellLetter.toggleClass('selectedSecondary');
	}
}

$('button#btnSolveLetter').on('click', function() {
	if (globalLastSelectedId == null) {
		return;
	}
	
	console.log("solve letter");
	let parent = $('#' + globalLastSelectedId);
	let col = parseInt(parent.attr('data-col'));
	let row = parseInt(parent.attr('data-row'));
	let cell = $('#cellLetter_' + col + '_' + row);
	let realLetter = cell.attr('data-realLetter');
	
	if (cell.text() != realLetter) {
		cell.text(realLetter);
	}
	
	nextLetter(col, row);
});

$('button#btnShowErrors').on('click', function() {
	console.log("show errors");
	let e = $(this);
	if (globalShowErrors) {
		e.text('Show Any Errors');
		globalShowErrors = false;
	} else {
		e.text('Hide All Errors');
		globalShowErrors = true;
	}
});

window.onkeydown = function(e) {
	let key = e.keyCode ? e.keyCode : e.which;
	console.log(key);
	
	const DELETE_KEY = 46;
	
	if (key == DELETE_KEY) {
		let parent = $('#' + globalLastSelectedId);
		let col = parseInt(parent.attr('data-col'));
		let row = parseInt(parent.attr('data-row'));
		let cell = $('#cellLetter_' + col + '_' + row);
		cell.text('');
	}
}

window.onkeypress = function(e) {
	let key = e.keyCode ? e.keyCode : e.which;
	console.log(key);
	
	const LETTER_A = 65;
	const LETTER_z = 122;
	// BACKSPACE = 8;
	// SPACE = 32;

	if (key < LETTER_A || (key>=91 && key<=96) || key > LETTER_z || globalLastSelectedId == null) {
		return;
	}
	
	let parent = $('#' + globalLastSelectedId);
	let col = parseInt(parent.attr('data-col'));
	let row = parseInt(parent.attr('data-row'));
	let cell = $('#cellLetter_' + col + '_' + row);
	cell.text(String.fromCharCode(key).toUpperCase());
	nextLetter(col, row);
}

function nextLetter(col, row) {
	if (globalDirection == 'across') {
		while (true) {
			col++;
			if (col > 14) {
				col = 0;
				row++;
				if (row > 14) {
					row = 0;
					globalDirection = 'down';
					break;
				}
			}
			if ($('#cellLetter_' + col + '_' + row).hasClass('cellLetterBlock')) {
				continue;
			}
			if (!isAcrossWord(col,row)) {
				continue;
			}
			break;
		}
		numberAndLetterClicked($('#numberAndLetter_' + col + '_' + row));
		return;
	}
	
	if (globalDirection == 'down') {
		while (true) {
			row++;
			if (row > 14) {
				row = 0;
				col++;
				if (col > 14) {
					col = 0;
					globalDirection = 'across';
					break;
				}
			}
			if ($('#cellLetter_' + col + '_' + row).hasClass('cellLetterBlock')) {
				continue;
			}
			if (!isDownWord(col,row)) {
				continue;
			}
			break;
		}
		numberAndLetterClicked($('#numberAndLetter_' + col + '_' + row));
		return;
	}
}



