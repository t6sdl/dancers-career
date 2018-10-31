$(function () {
	const contextPath = $('#contextPath').val();
	const $birthYear = $('#birthYear'),
	$birthMonth = $('#birthMonth'),
	$birthDay = $('#birthDay'),
	$univPref = $('#univPref'),
	$univName = $('#univName'),
	$faculty = $('#faculty'),
	$department = $('#department'),
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
	const setUnivName = function () {
		$univName.children('[value!="default"]').remove();
		$faculty.children('[value!="default"]').remove();
		$department.children('[value!="default"]').remove();
		if ($univPref.val() === 'default') {
			$univName.prop('disabled', true);
			$faculty.prop('disabled', true);
			$department.prop('disabled', true);
			return;
		}
		$univName.prop('disabled', false);
		$faculty.prop('disabled', true);
		$department.prop('disabled', true);
		for (let univ in json[$univPref.val()]) {
			let options = '<option value="' + univ + '">' + univ + '</option>';
			$univName.append(options);
		}
	}
	const setFaculty = function () {
		$faculty.children('[value!="default"]').remove();
		$department.children('[value!="default"]').remove();
		if ($univName.val() === 'default') {
			$faculty.prop('disabled', true);
			$department.prop('disabled', true);
			return;
		}
		$faculty.prop('disabled', false);
		$department.prop('disabled', true);
		for (let fac in json[$univPref.val()][$univName.val()]) {
			let options = '<option value="' + fac + '">' + fac + '</option>';
			$faculty.append(options);
		}
	}
	const setDepartment = function () {
		$department.children('[value!="default"]').remove();
		if ($faculty.val() === 'default') {
			$department.prop('disabled', true);
			return;
		}
		$department.prop('disabled', false);
		for (let i = 0; i < json[$univPref.val()][$univName.val()][$faculty.val()].length; i++) {
			let dep = json[$univPref.val()][$univName.val()][$faculty.val()][i];
			let options = '<option value="' + dep + '">' + dep + '</option>';
			$department.append(options);
		}
	}
	const initUnivName = function () {
		setUnivName();
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
	prepJSON();
	initUnivName();
	initFaculty();
	initDepartment();
	$birthYear.on('input', function (event) {
		event.preventDefault();
		setBirthMonth();
	});
	$birthMonth.on('input', function (event) {
		event.preventDefault();
		setBirthDay();
	});
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