$(function () {
	const $addClub = $('#addClub'),
	$addOffer = $('#addOffer'),
	$addEs = $('#addEs'),
	$addInterview = $('#addInterview');
	const experienceId = $('#experienceId').val();
	$addClub.on('click', function () {
		const $clubItem = $('.clubItem');
		const clubInput = '\n<input class="clubItem" type="text" id="club' + $clubItem.length + '" name="club[' + $clubItem.length + ']" placeholder="空欄可" value>';
		$clubItem.filter(':last').after(clubInput);
	});
	$addOffer.on('click', function () {
		const $offerItem = $('.offerItem');
		const offerInput = '\n<input class="offerItem" type="text" id="offer' + $offerItem.length + '" name="offer[' + $offerItem.length + ']" placeholder="空欄可" value>';
		$offerItem.filter(':last').after(offerInput);
	});
	$addEs.on('click', function () {
		const $esItem = $('.esItem');
		const corpInput = '\n<div>\n<label for="es' + $esItem.length + '.corp">提出先企業</label>\n<input type="text" id="es' + $esItem.length + '.corp" name="es[' + $esItem.length + '].corp" placeholder="空欄可" value></div>',
		resultInput = '\n<div>\n<label for="es' + $esItem.length + '.result">選考結果</label>\n<input type="text" id="es' + $esItem.length + '.result" name="es[' + $esItem.length + '].result" placeholder="空欄可" value></div>',
		questionInput = '\n<div>\n<label for="es' + $esItem.length + '.question">設問</label>\n<br><textarea id="es' + $esItem.length + '.question" name="es[' + $esItem.length + '].question" rows="5" cols="100" placeholder="空欄可"></textarea></div>',
		answerInput = '\n<div>\n<label for="es' + $esItem.length + '.answer">回答</label>\n<br><textarea id="es' + $esItem.length + '.answer" name="es[' + $esItem.length + '].answer" rows="10" cols="100" placeholder="空欄可"></textarea></div>',
		adviceInput = '\n<div>\n<label for="es' + $esItem.length + '.advice">赤入れ</label>\n<br><textarea id="es' + $esItem.length + '.advice" name="es[' + $esItem.length + '].advice" rows="10" cols="100" placeholder="空欄可"></textarea></div>';
		const esInput = '\n<div class="esItem">' + corpInput + resultInput + questionInput + answerInput + adviceInput + '\n<hr>\n</div>';
		$esItem.filter(':last').after(esInput);
	});
	$addInterview.on('click', function () {
		const $interviewItem = $('.interviewItem');
		const questionInput = '\n<div>\n<label for="interview' + $interviewItem.length + '.question">質問</label>\n<br><textarea id="interview' + $interviewItem.length + '.question" name="interview[' + $interviewItem.length + '].question" rows="5" cols="100" placeholder="空欄可"></textarea></div>',
		answerInput = '\n<div>\n<label for="interview' + $interviewItem.length + '.answer">回答</label>\n<br><textarea id="interview' + $interviewItem.length + '.answer" name="interview[' + $interviewItem.length + '].answer" rows="10" cols="100" placeholder="空欄可"></textarea></div>';
		const interviewInput = '\n<div class="interviewItem">' + questionInput + answerInput + '\n<hr>\n</div>';
		$interviewItem.filter(':last').after(interviewInput);
	});
});