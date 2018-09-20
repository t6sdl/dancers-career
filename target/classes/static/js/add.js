$(function () {
	const $addClub = $('#add-club'),
	$addOffer = $('#add-offer'),
	$addEs = $('#add-es'),
	$addInterview = $('#add-interview');
	$addClub.on('click', function () {
		const $clubItem = $('.club-item');
		const clubInput = '\n<input class="club-item" type="text" id="club' + $clubItem.length + '" name="club[' + $clubItem.length + ']" placeholder="空欄可" value>';
		$clubItem.filter(':last').after(clubInput);
	});
	$addOffer.on('click', function () {
		const $offerItem = $('.offer-item');
		const offerInput = '\n<input class="offer-item" type="text" id="offer' + $offerItem.length + '" name="offer[' + $offerItem.length + ']" placeholder="空欄可" value>';
		$offerItem.filter(':last').after(offerInput);
	});
	$addEs.on('click', function () {
		const $esItem = $('.es-item');
		const corpInput = '\n<div>\n<label for="es' + $esItem.length + '.corp">提出先企業</label>\n<input type="text" id="es' + $esItem.length + '.corp" name="es[' + $esItem.length + '].corp" placeholder="空欄可" value></div>',
		resultInput = '\n<div>\n<label for="es' + $esItem.length + '.result">選考結果</label>\n<input type="text" id="es' + $esItem.length + '.result" name="es[' + $esItem.length + '].result" placeholder="空欄可" value></div>',
		questionInput = '\n<div>\n<label for="es' + $esItem.length + '.question">設問</label>\n<input type="text" id="es' + $esItem.length + '.question" name="es[' + $esItem.length + '].question" placeholder="空欄可" value></div>',
		answerInput = '\n<div>\n<label for="es' + $esItem.length + '.answer">回答</label>\n<br><textarea id="es' + $esItem.length + '.answer" name="es[' + $esItem.length + '].answer" rows="10" cols="100" placeholder="空欄可"></textarea>',
		adviceInput = '\n<div>\n<label for="es' + $esItem.length + '.advice">赤入れ</label>\n<br><textarea id="es' + $esItem.length + '.advice" name="es[' + $esItem.length + '].advice" rows="10" cols="100" placeholder="空欄可"></textarea>';
		const esInput = '\n<div class="es-item">' + corpInput + resultInput + questionInput + answerInput + adviceInput + '\n<hr>\n</div>';
		$esItem.filter(':last').after(esInput);
	});
	$addInterview.on('click', function () {
		const $interviewItem = $('.interview-item');
		const questionInput = '\n<div>\n<label for="interview' + $interviewItem.length + '.question">質問</label>\n<input type="text" id="interview' + $interviewItem.length + '.question" name="interview[' + $interviewItem.length + '].question" placeholder="空欄可" value></div>',
		answerInput = '\n<div>\n<label for="interview' + $interviewItem.length + '.answer">回答</label>\n<br><textarea id="interview' + $interviewItem.length + '.answer" name="interview[' + $interviewItem.length + '].answer" rows="10" cols="100" placeholder="空欄可"></textarea>';
		const interviewInput = '\n<div class="interview-item">' + questionInput + answerInput + '\n<hr>\n</div>';
		$interviewItem.filter(':last').after(interviewInput);
	});
});