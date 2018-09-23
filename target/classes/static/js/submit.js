$(function () {
	const $submit = $('input[type="submit"]'),
	$form = $('form');
	$submit.prop('disabled', false);
	$form.submit(function (event) {
		setTimeout(function () {
			$submit.prop('disabled', true);
		}, 0);
	});
});