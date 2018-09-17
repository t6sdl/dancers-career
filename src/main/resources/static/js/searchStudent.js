$(function () {
	const contextPath = $('#contextPath').val();
	const $prefecture = $('#prefecture'),
	$university = $('#university'),
	$faculty = $('#faculty'),
	$department = $('#department'),
	$hiddenPref = $('#hiddenPref'),
	$hiddenUniv = $('#hiddenUniv'),
	$hiddenFac = $('#hiddenFac'),
	$hiddenDep = $('#hiddenDep');
	let json;
	const prepJSON = function () {
		$.ajaxSetup({async: false});
		$.getJSON(contextPath + 'js/university.json', function (data) {
			json = data;
		});
		$.ajaxSetup({async: true});
	}
	const setUniversity = function () {
		if ($prefecture.val() === 'default') {
			return;
		}
		$university.children('[value!="default"]').remove();
		$faculty.children('[value!="default"]').remove();
		$department.children('[value!="default"]').remove();
		for (let univ in json[$prefecture.val()]) {
			let options = '<option value="' + univ + '">' + univ + '</option>';
			$university.append(options);
		}
	}
	const setFaculty = function () {
		if ($university.val() === 'default') {
			return;
		}
		$faculty.children('[value!="default"]').remove();
		$department.children('[value!="default"]').remove();
		for (let fac in json[$prefecture.val()][$university.val()]) {
			let options = '<option value="' + fac + '">' + fac + '</option>';
			$faculty.append(options);
		}
	}
	const setDepartment = function () {
		if ($faculty.val() === 'default') {
			return;
		}
		$department.children('[value!="default"]').remove();
		for (let i = 0; i < json[$prefecture.val()][$university.val()][$faculty.val()].length; i++) {
			let dep = json[$prefecture.val()][$university.val()][$faculty.val()][i];
			let options = '<option value="' + dep + '">' + dep + '</option>';
			$department.append(options);
		}
	}
	const initPrefecture = function () {
		$prefecture.children('[value="' + $hiddenPref.val() + '"]').prop('selected', true);
	}
	const initUniversity = function () {
		setUniversity();
		$university.children('[value="' + $hiddenUniv.val() + '"]').prop('selected', true);
	}
	const initFaculty = function () {
		setFaculty();
		$faculty.children('[value="' + $hiddenFac.val() + '"]').prop('selected', true);
	}
	const initDepartment = function () {
		setDepartment();
		$department.children('[value="' + $hiddenDep.val() + '"]').prop('selected', true);
	}
	prepJSON();
	initPrefecture();
	initUniversity();
	initFaculty();
	initDepartment();
	$prefecture.on('input', function (event) {
		event.preventDefault();
		setUniversity();
	});
	$university.on('input', function (event) {
		event.preventDefault();
		setFaculty();
	});
	$faculty.on('input', function (event) {
		event.preventDefault();
		setDepartment();
	});
});