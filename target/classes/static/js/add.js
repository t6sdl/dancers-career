$(function () {
	const $addClub = $('#addClub'),
	$addOffer = $('#addOffer'),
	$addEs = $('#addEs'),
	$addInterview = $('#addInterview');
	const experienceId = $('#experienceId').val();
	$addClub.on('click', function () {
		const $clubItem = $('.clubItem');
		const clubInput = '\n<div class="clubItem col-sm-6 my-2">\n<input class="form-control" type="text" id="club' + $clubItem.length + '" name="club[' + $clubItem.length + ']" placeholder="大学 サークル（非公開は空欄）" value>\n</div>';
		$clubItem.filter(':last').after(clubInput);
	});
	$addOffer.on('click', function () {
		const $offerItem = $('.offerItem');
		const offerInput = '\n<div class="offerItem col-sm-6 my-2">\n<input class="form-control" type="text" id="offer' + $offerItem.length + '" name="offer[' + $offerItem.length + ']" placeholder="企業（非公開は空欄）" value></div>';
		$offerItem.filter(':last').after(offerInput);
	});
	$addEs.on('click', function () {
		const $esItem = $('.esItem');
		const corpInput = '\n<div class="form-group col-sm-6">\n<label for="es' + $esItem.length + '.corp">提出先企業</label>\n<input type="text" id="es' + $esItem.length + '.corp" name="es[' + $esItem.length + '].corp" class="form-control" placeholder="企業" value></div>',
		resultInput = '\n<div class="form-group col-sm-6">\n<label for="es' + $esItem.length + '.result">選考結果</label>\n<input type="text" id="es' + $esItem.length + '.result" name="es[' + $esItem.length + '].result" class="form-control" placeholder="合否等" value></div>',
		questionInput = '\n<div class="form-group">\n<label for="es' + $esItem.length + '.question">設問</label>\n<br><textarea id="es' + $esItem.length + '.question" name="es[' + $esItem.length + '].question" class="form-control" placeholder="「挫折した経験は？」など"></textarea></div>',
		answerInput = '\n<div class="form-group">\n<label for="es' + $esItem.length + '.answer">回答</label>\n<br><textarea id="es' + $esItem.length + '.answer" name="es[' + $esItem.length + '].answer" class="form-control" placeholder="設問に対する回答"></textarea></div>',
		adviceInput = '\n<div class="form-group">\n<label for="es' + $esItem.length + '.advice">添削</label>\n<br><textarea id="es' + $esItem.length + '.advice" name="es[' + $esItem.length + '].advice" class="form-control" placeholder="回答に対する添削"></textarea></div>';
		const esInput = '\n<div class="esItem">' + '\n<div class="form-row">' + corpInput + resultInput + '\n</div>' + questionInput + answerInput + adviceInput + '\n<hr>\n</div>';
		$esItem.filter(':last').after(esInput);
	});
	$addInterview.on('click', function () {
		const $interviewItem = $('.interviewItem');
		const questionInput = '\n<div class="form-group">\n<label for="interview' + $interviewItem.length + '.question">質問</label>\n<br><textarea id="interview' + $interviewItem.length + '.question" name="interview[' + $interviewItem.length + '].question" class="form-control" placeholder="ESを書く上で意識したことは？など"></textarea></div>',
		answerInput = '\n<div class="form-group">\n<label for="interview' + $interviewItem.length + '.answer">回答</label>\n<br><textarea id="interview' + $interviewItem.length + '.answer" name="interview[' + $interviewItem.length + '].answer" class="form-control" placeholder="質問への回答"></textarea></div>';
		const interviewInput = '\n<div class="interviewItem">' + questionInput + answerInput + '\n<hr>\n</div>';
		$interviewItem.filter(':last').after(interviewInput);
	});
});