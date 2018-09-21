$(function () {
	const contextPath = $('#contextPath').val();
	const $birthYear = $('#birth_year'),
	$birthMonth = $('#birth_month'),
	$birthDay = $('#birth_day'),
	$prefecture = $('#prefecture'),
	$university = $('#university'),
	$faculty = $('#faculty'),
	$department = $('#department'),
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
		$university.children('[value!="default"]').remove();
		$faculty.children('[value!="default"]').remove();
		$department.children('[value!="default"]').remove();
		if ($prefecture.val() === 'default') {
			$university.prop('disabled', true);
			$faculty.prop('disabled', true);
			$department.prop('disabled', true);
			return;
		}
		$university.prop('disabled', false);
		$faculty.prop('disabled', true);
		$department.prop('disabled', true);
		for (let univ in json[$prefecture.val()]) {
			let options = '<option value="' + univ + '">' + univ + '</option>';
			$university.append(options);
		}
	}
	const setFaculty = function () {
		$faculty.children('[value!="default"]').remove();
		$department.children('[value!="default"]').remove();
		if ($university.val() === 'default') {
			$faculty.prop('disabled', true);
			$department.prop('disabled', true);
			return;
		}
		$faculty.prop('disabled', false);
		$department.prop('disabled', true);
		for (let fac in json[$prefecture.val()][$university.val()]) {
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
		for (let i = 0; i < json[$prefecture.val()][$university.val()][$faculty.val()].length; i++) {
			let dep = json[$prefecture.val()][$university.val()][$faculty.val()][i];
			let options = '<option value="' + dep + '">' + dep + '</option>';
			$department.append(options);
		}
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
	initUniversity();
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