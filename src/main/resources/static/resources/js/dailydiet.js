/**
 * 
 */

var lastNutrients = null;

$('.selectable').on('click', function() {
	const e = $(this);
	if (e.hasClass('numServings')) {
		let id = $(this).attr('id');
		let len = id.length;
		let pos = id.charAt(len-1);
		let partialId = id.substr(0, len-1);
		for (let i=1;i<5;i++) {
			if (i != pos) {
				$('#' + partialId + i).removeClass('selected');
			}
		}
		e.toggleClass('selected');
		// notify server to update totals
		var payload = JSON.stringify({ 'numServingsId':e.attr('id'), 
									   'selected':e.hasClass('selected'), 
									   'dailyDietName':$('#dailyDietName').attr('data-dailyDietName')});
		nutrientAggregates = stomp.send('/stomp/dailydiet/numServingsChanged', {}, payload);
				
		let eMenuItemPrev = $('.menuItem.selected');
		if (eMenuItemPrev != null) {
			eMenuItemPrev.removeClass('selected');
		}
		let eMenuItemNow = ($('#' + id.substr(0, len-8)))
		eMenuItemNow.addClass('selected');
		selectNutrientsOneServing(eMenuItemNow);
	} else if (e.hasClass('menuItem')) {
		let eMenuItemPrev = $('.menuItem.selected');
		if (eMenuItemPrev != null && eMenuItemPrev.attr('id') != e.attr('id')) {
			eMenuItemPrev.removeClass('selected');
		}
		e.toggleClass('selected');
		selectNutrientsOneServing(e);
	}
	else if (e.hasClass('ingredient')) {
		let ePrev = $('.ingredient.selected');
		if (ePrev != null && ePrev.attr('id') != e.attr('id')) {
			ePrev.removeClass('selected');
		}
		e.toggleClass('selected');		
	}
});

function selectNutrientsOneServing(eNow) {
	let selected = eNow.hasClass('selected');
	console.log('sn1s ' + selected);
	clearNutrientServing();
	if (selected) {
		var payload = JSON.stringify({ 'foodItemId':eNow.attr('id')});
		nutrientAggregates = stomp.send('/stomp/dailydiet/getOneServingNutrients', {}, payload);
		console.log("length of nutrientAggregates = " + nutrientAggregates);
	}
}

var stompUrl = 'http://' + window.location.host + '/dailydiet';
var stompSock = new SockJS(stompUrl);
var stomp = Stomp.over(stompSock);

stomp.connect({}, function(frame) {
    stomp.subscribe('/topic/result/getOneServingNutrients', function (message) {
        let nutrients = JSON.parse(message.body);
        //console.log("nutrients.length = " + nutrients.length);
        clearNutrientServing();
        for (let i=0;i<nutrients.length;i++) {
        	let nutrient = nutrients[i];
        	//console.log("nutrient = " + nutrient.nutrientItem.name + ", " + nutrient.amount);
        	let nutrientServingId = "#nutrientServing-" + nutrient.nutrientItem.id;
        	$(nutrientServingId).text(Math.round((nutrient.amount/nutrient.nutrientItem.rda*100)) + "%");
       }
       lastNutrients = nutrients;
    });
    stomp.subscribe('/topic/result/nutrientDisplaySummary', function (message) {
        let nutrientDisplaySummaryList = JSON.parse(message.body);
        console.log("nutrientDisplaySummaryList.length = " + nutrientDisplaySummaryList.length);
        for (let i=0;i<nutrientDisplaySummaryList.length;i++) {
        	let totalId = '#' + nutrientDisplaySummaryList[i].totalId;
        	let totalAmount = nutrientDisplaySummaryList[i].totalAmount;
        	let percentId = '#' + nutrientDisplaySummaryList[i].percentId;
        	let percentAmount = nutrientDisplaySummaryList[i].percentAmount;
        	$(totalId).text(totalAmount);
        	if (percentAmount == 0) {
        		$(percentId).text('');
        	} else {
        		$(percentId).text(percentAmount + '%');
        	}
        }
    });
});

function clearNutrientServing() {
	if (lastNutrients == null) {
		return;
	}
	
    for (let i=0;i<lastNutrients.length;i++) {
    	let nutrient = lastNutrients[i];
    	let nutrientServingId = "#nutrientServing-" + nutrient.nutrientItem.id;
    	$(nutrientServingId).text("");
     }
}









