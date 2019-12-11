$(function () {
	const contextPath = $('#contextPath').val();
	const $birthYear = $('#birthYear'),
	$birthMonth = $('#birthMonth'),
	$birthDay = $('#birthDay'),
	$univLoc = $('#univLoc'),
	$univType = $('#univType'),
	$univName = $('#univName'),
	$univFac = $('#univFac'),
	$univDep = $('#univDep'),
	$grad = $('#grad'),
	$gradLoc = $('#gradLoc'),
	$gradType = $('#gradType'),
	$gradName = $('#gradName'),
	$gradSchool = $('#gradSchool'),
	$gradDiv = $('#gradDiv'),
	$hiddenUnivType = $('#hiddenUnivType'),
	$hiddenUnivName = $('#hiddenUnivName'),
	$hiddenUnivFac = $('#hiddenUnivFac'),
	$hiddenUnivDep = $('#hiddenUnivDep'),
	$hiddenGradType = $('#hiddenGradType'),
	$hiddenGradName = $('#hiddenGradName'),
	$hiddenGradSchool = $('#hiddenGradSchool'),
	$hiddenGradDiv = $('#hiddenGradDiv');
	let univJson = null;
	let gradJson = null;
	const prepJSON = function (fileName) {
		$.ajaxSetup({async: false});
		$.getJSON(contextPath + fileName, function (data) {
			if (fileName === 'js/university.json') {
				univJson = data;
			} else if (fileName === 'js/grad_school.json') {
				gradJson = data;
			}
		});
		$.ajaxSetup({async: true});
	}
	const setUnivType = function () {
		$univType.children('[value="default"]').prop('selected', true);
		$univName.children('[value!="default"]').remove();
		$univFac.children('[value!="default"]').remove();
		$univDep.children('[value!="default"]').remove();
		if ($univLoc.val() === 'default') {
			$univType.prop('disabled', true);
			$univName.prop('disabled', true);
			$univFac.prop('disabled', true);
			$univDep.prop('disabled', true);
			return;
		}
		$univType.prop('disabled', false);
		$univName.prop('disabled', true);
		$univFac.prop('disabled', true);
		$univDep.prop('disabled', true);
	}
	const setUnivName = function () {
		$univName.children('[value!="default"]').remove();
		$univFac.children('[value!="default"]').remove();
		$univDep.children('[value!="default"]').remove();
		if (!($univType.length)) {
			if ($univLoc.val() === 'default') {
				$univName.prop('disabled', true);
				$univFac.prop('disabled', true);
				$univDep.prop('disabled', true);
				return;
			}
			$univName.prop('disabled', false);
			$univFac.prop('disabled', true);
			$univDep.prop('disabled', true);
			for (let ut in univJson[$univLoc.val()]) {
				let caption = '<option value="' + ut + '" disabled>-- ' + ut + ' --</option>';
				$univName.append(caption);
				for (let un in univJson[$univLoc.val()][ut]) {
					let options = '<option value="' + un + '">' + un + '</option>';
					$univName.append(options);
				}
			}
		} else {
			if ($univType.val() === 'default') {
				$univName.prop('disabled', true);
				$univFac.prop('disabled', true);
				$univDep.prop('disabled', true);
				return;
			}
			$univName.prop('disabled', false);
			$univFac.prop('disabled', true);
			$univDep.prop('disabled', true);
			for (let un in univJson[$univLoc.val()][$univType.val()]) {
				let options = '<option value="' + un + '">' + un + '</option>';
				$univName.append(options);
			}
		}
	}
	const setUnivFac = function () {
		$univFac.children('[value!="default"]').remove();
		$univDep.children('[value!="default"]').remove();
		if ($univName.val() === 'default') {
			$univFac.prop('disabled', true);
			$univDep.prop('disabled', true);
			return;
		}
		$univFac.prop('disabled', false);
		$univDep.prop('disabled', true);
		for (let uf in univJson[$univLoc.val()][$hiddenUnivType.val()][$univName.val()]) {
			let options = '<option value="' + uf + '">' + uf + '</option>';
			$univFac.append(options);
		}
	}
	const setUnivDep = function () {
		$univDep.children('[value!="default"]').remove();
		if ($univFac.val() === 'default') {
			$univDep.prop('disabled', true);
			return;
		}
		$univDep.prop('disabled', false);
		for (let i = 0; i < univJson[$univLoc.val()][$hiddenUnivType.val()][$univName.val()][$univFac.val()].length; i++) {
			let ud = univJson[$univLoc.val()][$hiddenUnivType.val()][$univName.val()][$univFac.val()][i];
			let options = '<option value="' + ud + '">' + ud + '</option>';
			$univDep.append(options);
		}
	}
	const initUnivName = function () {
		if (!($univType.length)) {
			let found = false;
			for (let ut in univJson[$univLoc.val()]) {
				for (let un in univJson[$univLoc.val()][ut]) {
					if (un === $hiddenUnivName.val()) {
						$hiddenUnivType.val(ut);
						found = true;
						break;
					}
				}
				if (found) {
					break;
				}
			}
		}
		setUnivName();
		$univName.children('[value="' + $hiddenUnivName.val() + '"]').prop('selected', true);
	}
	const initUnivFac = function () {
		setUnivFac();
		$univFac.children('[value="' + $hiddenUnivFac.val() + '"]').prop('selected', true);
	}
	const initUnivDep = function () {
		setUnivDep();
		$univDep.children('[value="' + $hiddenUnivDep.val() + '"]').prop('selected', true);
	}
	
	const initGrad = function () {
		if ($('input[name="degree"]:checked').val() === '修士卒' || $('input[name="degree"]:checked').val() === '博士卒') {
			if (gradJson === null) {
				prepJSON('js/grad_school.json');
			}
			if ($gradType.length) {
				setGradType();
			} else if (!($gradType.length)) {
				let found = false;
				for (let gt in gradJson[$gradLoc.val()]) {
					for (let gn in gradJson[$gradLoc.val()][gt]) {
						if (gn === $hiddenGradName.val()) {
							$hiddenGradType.val(gt);
							found = true;
							break;
						}
					}
					if (found) {
						break;
					}
				}
			}
			$grad.css({
				display: "block",
			});
			initGradName();
			initGradSchool();
			initGradDiv();
		} else {
			$grad.css({
				display: "none",
			});
			if ($gradType.length) {
				$gradType.prop('disabled', false);
			}
			$gradName.prop('disabled', false);
			$gradSchool.prop('disabled', false);
			$gradDiv.prop('disabled', false);
		}
	}
	const setGradType = function () {
		$gradType.children('[value=""]').prop('selected', true);
		$gradName.children('[value!=""]').remove();
		$gradSchool.children('[value!=""]').remove();
		$gradDiv.children('[value!=""]').remove();
		if ($gradLoc.val() === '') {
			$gradType.prop('disabled', true);
			$gradName.prop('disabled', true);
			$gradSchool.prop('disabled', true);
			$gradDiv.prop('disabled', true);
			return;
		}
		$gradType.prop('disabled', false);
		$gradName.prop('disabled', true);
		$gradSchool.prop('disabled', true);
		$gradDiv.prop('disabled', true);
	}
	const setGradName = function () {
		$gradName.children('[value!=""]').remove();
		$gradSchool.children('[value!=""]').remove();
		$gradDiv.children('[value!=""]').remove();
		if (!($gradType.length)) {
			if ($gradLoc.val() === '') {
				$gradName.prop('disabled', true);
				$gradSchool.prop('disabled', true);
				$gradDiv.prop('disabled', true);
				return;
			}
			$gradName.prop('disabled', false);
			$gradSchool.prop('disabled', true);
			$gradDiv.prop('disabled', true);
			for (let gt in gradJson[$gradLoc.val()]) {
				let caption = '<option value="' + gt + '" disabled>-- ' + gt + ' --</option>';
				$gradName.append(caption);
				for (let gn in gradJson[$gradLoc.val()][gt]) {
					let options = '<option value="' + gn + '">' + gn + '</option>';
					$gradName.append(options);
				}
			}
		} else {
			if ($gradType.val() === '') {
				$gradName.prop('disabled', true);
				$gradSchool.prop('disabled', true);
				$gradDiv.prop('disabled', true);
				return;
			}
			$gradName.prop('disabled', false);
			$gradSchool.prop('disabled', true);
			$gradDiv.prop('disabled', true);
			for (let gn in gradJson[$gradLoc.val()][$gradType.val()]) {
				let options = '<option value="' + gn + '">' + gn + '</option>';
				$gradName.append(options);
			}
		}
	}
	const setGradSchool = function () {
		$gradSchool.children('[value!=""]').remove();
		$gradDiv.children('[value!=""]').remove();
		if ($gradName.val() === '') {
			$gradSchool.prop('disabled', true);
			$gradDiv.prop('disabled', true);
			return;
		}
		$gradSchool.prop('disabled', false);
		$gradDiv.prop('disabled', true);
		for (let gs in gradJson[$gradLoc.val()][$hiddenGradType.val()][$gradName.val()]) {
			let options = '<option value="' + gs + '">' + gs + '</option>';
			$gradSchool.append(options);
		}
	}
	const setGradDiv = function () {
		$gradDiv.children('[value!=""]').remove();
		if ($gradSchool.val() === '') {
			$gradDiv.prop('disabled', true);
			return;
		}
		$gradDiv.prop('disabled', false);
		for (let i = 0; i < gradJson[$gradLoc.val()][$hiddenGradType.val()][$gradName.val()][$gradSchool.val()].length; i++) {
			let gd = gradJson[$gradLoc.val()][$hiddenGradType.val()][$gradName.val()][$gradSchool.val()][i];
			let options = '<option value="' + gd + '">' + gd + '</option>';
			$gradDiv.append(options);
		}
	}
	const initGradName = function () {
		setGradName();
		$gradName.children('[value="' + $hiddenGradName.val() + '"]').prop('selected', true);
	}
	const initGradSchool = function () {
		setGradSchool();
		$gradSchool.children('[value="' + $hiddenGradSchool.val() + '"]').prop('selected', true);
	}
	const initGradDiv = function () {
		setGradDiv();
		$gradDiv.children('[value="' + $hiddenGradDiv.val() + '"]').prop('selected', true);
	}
	
	const autoSetDate = function (max, $object) {
		for (let i = 1; i <= 31; i++) {
			if (!($object.children('[value="' + i + '"]').length)) {
				let options = '<option value="' + i + '">' + i + '</option>';
				$object.append(options);
			}
		}
		if (max < 31) {
			for (let i = 31; i > max; i--) {
				$object.children('[value="' + i + '"]').remove();
			}
		}
	}
	const setBirthMonth = function () {
		if ($birthYear.val() === 'default') {
			$birthMonth.prop('disabled', true);
			$birthDay.prop('disabled', true);
			return;
		}
		$birthMonth.prop('disabled', false);
		$birthDay.prop('disabled', true);
	}
	const setBirthDay = function() {
		if ($birthMonth.val() === 'default') {
			$birthDay.prop('disabled', true);
			return;
		}
		$birthDay.prop('disabled', false);
		if (($birthYear.val() === '1992' || $birthYear.val() === '1996') && $birthMonth.val() === '2') {
			autoSetDate(29, $birthDay);
		} else if ($birthMonth.val() === '2') {
			autoSetDate(28, $birthDay);
		} else if ($birthMonth.val() === '4' || $birthMonth.val() === '6' || $birthMonth.val() === '9' || $birthMonth.val() === '11') {
			autoSetDate(30, $birthDay);
		} else {
			autoSetDate(31, $birthDay);
		}
	}
	
	setBirthMonth();
	setBirthDay();
	prepJSON('js/university.json');
	if ($univType.length) {
		setUnivType();
	}
	initUnivName();
	initUnivFac();
	initUnivDep();
	initGrad();
	$birthYear.on('input', function (event) {
		event.preventDefault();
		setBirthMonth();
	});
	$birthMonth.on('input', function (event) {
		event.preventDefault();
		setBirthDay();
	});
	$univLoc.on('input', function (event) {
		event.preventDefault();
		if (!($univType.length)) {
			setUnivName();
		} else {
			setUnivType();
		}
	});
	$univType.on('input', function (event) {
		event.preventDefault();
		$hiddenUnivType.val($univType.val());
		setUnivName();
	})
	$univName.on('input', function (event) {
		event.preventDefault();
		let found = false;
		if (!($univType.length)) {
			for (let ut in univJson[$univLoc.val()]) {
				for (let un in univJson[$univLoc.val()][ut]) {
					if (un === $univName.val()) {
						$hiddenUnivType.val(ut);
						found = true;
						break;
					}
				}
				if (found) {
					break;
				}
			}
		}
		setUnivFac();
	});
	$univFac.on('input', function (event) {
		event.preventDefault();
		setUnivDep();
	});
	$gradLoc.on('input', function (event) {
		event.preventDefault();
		if (!($gradType.length)) {
			setGradName();
		} else {
			setGradType();
		}
	});
	$gradType.on('input', function (event) {
		event.preventDefault();
		$hiddenGradType.val($gradType.val());
		setGradName();
	});
	$gradName.on('input', function (event) {
		event.preventDefault();
		let found = false;
		if (!($gradType.length)) {
			for (let gt in gradJson[$gradLoc.val()]) {
				for (let gn in gradJson[$gradLoc.val()][gt]) {
					if (gn === $gradName.val()) {
						$hiddenGradType.val(gt);
						found = true;
						break;
					}
				}
				if (found) {
					break;
				}
			}
		}
		setGradSchool();
	});
	$gradSchool.on('input', function (event) {
		event.preventDefault();
		setGradDiv();
	});
	$('input[name="degree"]').change(function () {
		initGrad();
	})
});