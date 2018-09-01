$(function () {
	const contextPath = $('#contextPath').val();
	const $birthYear = $('#birth_year'),
	$birthMonth = $('#birth_month'),
	$birthDay = $('#birth_day'),
	$prefecture = $('#prefecture'),
	$university = $('#university'),
	$faculty = $('#faculty'),
	$department = $('#department');
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
			$university.prop('disabled', true);
			$faculty.prop('disabled', true);
			$department.prop('disabled', true);
			return;
		}
		$university.children('[value!="default"]').remove();
		$faculty.children('[value!="default"]').remove();
		$department.children('[value!="default"]').remove();
		$university.prop('disabled', false);
		$faculty.prop('disabled', true);
		$department.prop('disabled', true);
		for (let univ in json[$prefecture.val()]) {
			let options = '<option value="' + univ + '">' + univ + '</option>';
			$university.append(options);
		}
	}
	const setFaculty = function () {
		if ($university.val() === 'default') {
			$faculty.prop('disabled', true);
			$department.prop('disabled', true);
			return;
		}
		$faculty.children('[value!="default"]').remove();
		$department.children('[value!="default"]').remove();
		$faculty.prop('disabled', false);
		$department.prop('disabled', true);
		for (let fac in json[$prefecture.val()][$university.val()]) {
			let options = '<option value="' + fac + '">' + fac + '</option>';
			$faculty.append(options);
		}
	}
	const setDepartment = function () {
		if ($faculty.val() === 'default') {
			$department.prop('disabled', true);
			return;
		}
		$department.children('[value!="default"]').remove();
		$department.prop('disabled', false);
		for (let i = 0; i < json[$prefecture.val()][$university.val()][$faculty.val()].length; i++) {
			let dep = json[$prefecture.val()][$university.val()][$faculty.val()][i];
			console.log(i);
			console.log(dep);
			let options = '<option value="' + dep + '">' + dep + '</option>';
			$department.append(options);
		}
	}
	const autoSetDate = function (max, $object) {
		for (let i = 1; i <= max; i++) {
			let j;
			if (i < 10) {
				j = '0' + i;
			} else {
				j = i;
			}
			let options = '<option value="' + j + '">' + i + '</option>';
			$object.append(options);
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
		$birthMonth.children('[value!="default"]').remove();
		$birthDay.children('[value!="default"]').remove();
		autoSetDate(12, $birthMonth);
	}
	const setBirthDay = function() {
		if ($birthMonth.val() === 'default') {
			$birthDay.prop('disabled', true);
			return;
		}
		$birthDay.prop('disabled', false);
		$birthDay.children('[value!="default"]').remove();
		if (($birthYear.val() === '1992' || $birthYear.val() === '1996') && $birthMonth.val() === '02') {
			autoSetDate(29, $birthDay);
		} else if ($birthMonth.val() === '02') {
			autoSetDate(28, $birthDay);
		} else if ($birthMonth.val() === '04' || $birthMonth.val() === '06' || $birthMonth.val() === '09' || $birthMonth.val() === '11') {
			autoSetDate(30, $birthDay);
		} else {
			autoSetDate(31, $birthDay);
		}
	}
	setBirthMonth();
	setBirthDay();
	prepJSON();
	console.log(json);
	setUniversity();
	setFaculty();
	setDepartment();
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