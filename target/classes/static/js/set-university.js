$(function () {
	const contextPath = $('#contextPath').val();
	const $univLoc = $('#univLoc'),
	$univType = $('#univType'),
	$univName = $('#univName'),
	$univFac = $('#univFac'),
	$univDep = $('#univDep'),
	$gradLoc = $('#gradLoc'),
	$gradType = $('#gradType'),
	$gradName = $('#gradName'),
	$gradSchool = $('#gradSchool'),
	$gradDiv = $('#gradDiv'),
	$hiddenUnivLoc = $('#hiddenUnivLoc'),
	$hiddenUnivName = $('#hiddenUnivName'),
	$hiddenUnivFac = $('#hiddenUnivFac'),
	$hiddenUnivDep = $('#hiddenUnivDep')
	$hiddenGradLoc = $('#hiddenGradLoc'),
	$hiddenGradName = $('#hiddenGradName'),
	$hiddenGradSchool = $('#hiddenGradSchool'),
	$hiddenGradDiv = $('#hiddenGradDiv');
	$hiddenSorts = $('input[name="sort"]');
	$sort = $('select[name="sort"]');
	let univJson;
	let gradJson;
	const prepJSON = function () {
		$.ajaxSetup({async: false});
		$.getJSON(contextPath + 'js/university.json', function (data) {
			univJson = data;
		});
		$.getJSON(contextPath + 'js/grad_school.json', function (data) {
			gradJson = data;
		});
		$.ajaxSetup({async: true});
	}
	const setUnivType = function () {
		$univType.children('[value=""]').prop('selected', true);
		$univName.children('[value!=""]').remove();
		$univFac.children('[value!=""]').remove();
		$univDep.children('[value!=""]').remove();
	}
	const setUnivName = function () {
		$univName.children('[value!=""]').remove();
		$univFac.children('[value!=""]').remove();
		$univDep.children('[value!=""]').remove();
		if ($univType.val() === '') {
			return;
		}
		for (let un in univJson[$univLoc.val()][$univType.val()]) {
			let option = '<option value="' + un + '">' + un + '</option>';
			$univName.append(option);
		}
	}
	const setUnivFac = function () {
		$univFac.children('[value!=""]').remove();
		$univDep.children('[value!=""]').remove();
		if ($univName.val() === '') {
			return;
		}
		for (let uf in univJson[$univLoc.val()][$univType.val()][$univName.val()]) {
			let option = '<option value="' + uf + '">' + uf + '</option>';
			$univFac.append(option);
		}
	}
	const setUnivDep = function () {
		$univDep.children('[value!=""]').remove();
		if ($univFac.val() === '') {
			return;
		}
		for (let i = 0; i < univJson[$univLoc.val()][$univType.val()][$univName.val()][$univFac.val()].length; i++) {
			let ud = univJson[$univLoc.val()][$univType.val()][$univName.val()][$univFac.val()][i];
			let option = '<option value="' + ud + '">' + ud + '</option>';
			$univDep.append(option);
		}
	}
	const initUnivLoc = function () {
		$univLoc.children('[value="' + $hiddenUnivLoc.val() + '"]').prop('selected', true);
	}
	const initUnivName = function () {
		if ($hiddenUnivName.val() !== '') {
			let found = false;
			for (let ut in univJson[$univLoc.val()]) {
				for (let un in univJson[$univLoc.val()][ut]) {
					if (un === $hiddenUnivName.val()) {
						$univType.val(ut);
						$univType.children('[value="' + ut + '"]').prop('selected', true);
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
	
	const setGradType = function () {
		$gradType.children('[value=""]').prop('selected', true);
		$gradName.children('[value!=""]').remove();
		$gradSchool.children('[value!=""]').remove();
		$gradDiv.children('[value!=""]').remove();
		if ($gradLoc.val() === '') {
			return;
		}
	}
	const setGradName = function () {
		$gradName.children('[value!=""]').remove();
		$gradSchool.children('[value!=""]').remove();
		$gradDiv.children('[value!=""]').remove();
		if ($gradType.val() === '') {
			return;
		}
		for (let gn in gradJson[$gradLoc.val()][$gradType.val()]) {
			let option = '<option value="' + gn + '">' + gn + '</option>';
			$gradName.append(option);
		}
	}
	const setGradSchool = function () {
		$gradSchool.children('[value!=""]').remove();
		$gradDiv.children('[value!=""]').remove();
		if ($gradName.val() === '') {
			return;
		}
		for (let gs in gradJson[$gradLoc.val()][$gradType.val()][$gradName.val()]) {
			let option = '<option value="' + gs + '">' + gs + '</option>';
			$gradSchool.append(option);
		}
	}
	const setGradDiv = function () {
		$gradDiv.children('[value!=""]').remove();
		if ($gradSchool.val() === '') {
			return;
		}
		for (let i = 0; i < gradJson[$gradLoc.val()][$gradType.val()][$gradName.val()][$gradSchool.val()].length; i++) {
			let gd = gradJson[$gradLoc.val()][$gradType.val()][$gradName.val()][$gradSchool.val()][i];
			let option = '<option value="' + gd + '">' + gd + '</option>';
			$gradDiv.append(option);
		}
	}
	const initGradLoc = function () {
		$gradLoc.children('[value="' + $hiddenGradLoc.val() + '"]').prop('selected', true);
	}
	const initGradName = function () {
		if ($hiddenGradName.val() !== '') {
			let found = false;
			for (let gt in gradJson[$gradLoc.val()]) {
				for (let gn in gradJson[$gradLoc.val()][gt]) {
					if (gn === $hiddenGradName.val()) {
						$gradType.val(gt);
						$gradType.children('[value="' + gt + '"]').prop('selected', true);
						found = true;
						break;
					}
				}
				if (found) {
					break;
				}
			}			
		}
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

	prepJSON();
	initUnivLoc();
	initUnivName();
	initUnivFac();
	initUnivDep();
	initGradLoc();
	initGradName();
	initGradSchool();
	initGradDiv();
	$univLoc.on('input', function (event) {
		event.preventDefault();
		setUnivType();
	});
	$univType.on('input', function (event) {
		event.preventDefault();
		setUnivName();
	});
	$univName.on('input', function (event) {
		event.preventDefault();
		setUnivFac();
	});
	$univFac.on('input', function (event) {
		event.preventDefault();
		setUnivDep();
	});
	$gradLoc.on('input', function (event) {
		event.preventDefault();
		setGradType();
	});
	$gradType.on('input', function (event) {
		event.preventDefault();
		setGradName();
	});
	$gradName.on('input', function (event) {
		event.preventDefault();
		setGradSchool();
	});
	$gradSchool.on('input', function (event) {
		event.preventDefault();
		setGradDiv();
	});
	$sort.on('input', function (event) {
		$hiddenSorts.val($sort.val());
	});
});