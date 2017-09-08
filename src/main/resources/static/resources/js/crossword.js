/**
 * 
 */

var globalLastSelected;

$('.numberAndLetter').on('click', function() {
	//console.log("numberAndLetter");
	let e = $(this);
	let id = e.attr('id');
	let col = parseInt(e.attr('data-col'));
	let row = parseInt(e.attr('data-row'));
	let cellNumber = $('#cellNumber_' + col + '_' + row);
	let cellLetter = $('#cellLetter_' + col + '_' + row);
	cellNumber.toggleClass('selectedPrimary');
	cellLetter.toggleClass('selectedPrimary');
	
	// temp right
	for (let i=col+1;i<16;i++) {
		if (i==16) break;
		let cellNumber = $('#cellNumber_' + i + '_' + row);
		let cellLetter = $('#cellLetter_' + i + '_' + row);
		if (cellLetter.hasClass('cellLetterBlock')) break;
		cellNumber.toggleClass('selectedSecondary');
		cellLetter.toggleClass('selectedSecondary');
	}
	
	// temp left
	for (let i=col-1;i>=0;i--) {
		if (i==-1) break;
		let cellNumber = $('#cellNumber_' + i + '_' + row);
		let cellLetter = $('#cellLetter_' + i + '_' + row);
		if (cellLetter.hasClass('cellLetterBlock')) break;
		cellNumber.toggleClass('selectedSecondary');
		cellLetter.toggleClass('selectedSecondary');
	}
	
	// temp down
	for (let i=row+1;i<16;i++) {
		if (i==16) break;
		let cellNumber = $('#cellNumber_' + col + '_' + i);
		let cellLetter = $('#cellLetter_' + col + '_' + i);
		if (cellLetter.hasClass('cellLetterBlock')) break;
		cellNumber.toggleClass('selectedSecondary');
		cellLetter.toggleClass('selectedSecondary');
	}
	
	// temp up
	for (let i=row-1;i>=0;i--) {
		if (i==-1) break;
		let cellNumber = $('#cellNumber_' + col + '_' + i);
		let cellLetter = $('#cellLetter_' + col + '_' + i);
		if (cellLetter.hasClass('cellLetterBlock')) break;
		cellNumber.toggleClass('selectedSecondary');
		cellLetter.toggleClass('selectedSecondary');
	}
	
	console.log(id + ' ' + col + ' ' + row);
});

