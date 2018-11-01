$(function () {
	const contextPath = $('#contextPath').val();
	const $univPref = $('#univPref'),
	$univName = $('#univName'),
	$faculty = $('#faculty'),
	$department = $('#department'),
	$hiddenUnivPref = $('#hiddenUnivPref'),
	$hiddenUnivName = $('#hiddenUnivName'),
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
		$univName.children('[value!=""]').remove();
		$faculty.children('[value!=""]').remove();
		$department.children('[value!=""]').remove();
		if ($univPref.val() === '') {
			return;
		}
		for (let univ in json[$univPref.val()]) {
			let options = '<option value="' + univ + '">' + univ + '</option>';
			$univName.append(options);
		}
	}
	const setFaculty = function () {
		$faculty.children('[value!=""]').remove();
		$department.children('[value!=""]').remove();
		if ($univName.val() === '') {
			return;
		}
		for (let fac in json[$univPref.val()][$univName.val()]) {
			let options = '<option value="' + fac + '">' + fac + '</option>';
			$faculty.append(options);
		}
	}
	const setDepartment = function () {
		$department.children('[value!=""]').remove();
		if ($faculty.val() === '') {
			return;
		}
		for (let i = 0; i < json[$univPref.val()][$univName.val()][$faculty.val()].length; i++) {
			let dep = json[$univPref.val()][$univName.val()][$faculty.val()][i];
			let options = '<option value="' + dep + '">' + dep + '</option>';
			$department.append(options);
		}
	}
	const initUnivPref = function () {
		$univPref.children('[value="' + $hiddenUnivPref.val() + '"]').prop('selected', true);
	}
	const initUnivName = function () {
		setUniversity();
		$univName.children('[value="' + $hiddenUnivName.val() + '"]').prop('selected', true);
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
	initUnivPref();
	initUnivName();
	initFaculty();
	initDepartment();
	$univPref.on('input', function (event) {
		event.preventDefault();
		setUnivName();
	});
	$univName.on('input', function (event) {
		event.preventDefault();
		setFaculty();
	});
	$faculty.on('input', function (event) {
		event.preventDefault();
		setDepartment();
	});
});