$(function () {
	const contextPath = $('#contextPath').val();
	const $birthYear = $('#birthYear'),
	$birthMonth = $('#birthMonth'),
	$birthDay = $('#birthDay'),
	$univPref = $('#univPref'),
	$univName = $('#univName'),
	$faculty = $('#faculty'),
	$department = $('#department'),
	$gradSchool = $('#gradSchool'),
	$gradSchoolPref = $('#gradSchoolPref'),
	$gradSchoolName = $('#gradSchoolName'),
	$gradSchoolOf = $('#gradSchoolOf'),
	$programIn = $('#programIn'),
	$hiddenUnivName = $('#hiddenUnivName'),
	$hiddenFac = $('#hiddenFac'),
	$hiddenDep = $('#hiddenDep'),
	$hiddenGradSchoolName = $('#hiddenGradSchoolName'),
	$hiddenGradSchoolOf = $('#hiddenGradSchoolOf'),
	$hiddenProgramIn = $('#hiddenProgramIn');
	let univJson;
	let gradJson;
	const prepJSON = function (fileName, json) {
		$.ajaxSetup({async: false});
		$.getJSON(contextPath + fileName, function (data) {
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
		for (let univ in univJson[$univPref.val()]) {
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
		for (let fac in univJson[$univPref.val()][$univName.val()]) {
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
		for (let i = 0; i < univJson[$univPref.val()][$univName.val()][$faculty.val()].length; i++) {
			let dep = univJson[$univPref.val()][$univName.val()][$faculty.val()][i];
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
	
	const initGradSchool = function () {
		if ($('input[name="academicDegree"]:checked').val() === '修士卒' || $('input[name="academicDegree"]:checked').val() === '博士卒') {
			if (gradJson == null) {
				prepJSON('/js/grad_school.json', gradJson);
			}
			$gradSchool.css({
				display: "block",
			});
			initGradSchoolName();
			initGradSchoolOf();
			initProgramIn();
		} else {
			$gradSchool.css({
				display: "none",
			});
		}
	}
	const setGradSchoolName = function () {
		$gradSchoolName.children('[value!=""]').remove();
		$gradSchoolOf.children('[value!=""]').remove();
		$programIn.children('[value!=""]').remove();
		if ($gradSchoolPref.val() === '') {
			$gradSchoolName.prop('disabled', true);
			$gradSchoolOf.prop('disabled', true);
			$programIn.prop('disabled', true);
			return;
		}
		$gradSchoolName.prop('disabled', false);
		$gradSchoolOf.prop('disabled', true);
		$programIn.prop('disabled', true);
		for (let grad in gradJson[$gradSchoolPref.val()]) {
			let options = '<option value="' + grad + '">' + grad + '</option>';
			$gradSchoolName.append(options);
		}
	}
	const setGradSchoolOf = function () {
		$gradSchoolOf.children('[value!=""]').remove();
		$programIn.children('[value!=""]').remove();
		if ($gradSchoolName.val() === '') {
			$gradSchoolOf.prop('disabled', true);
			$programIn.prop('disabled', true);
			return;
		}
		$gradSchoolOf.prop('disabled', false);
		$programIn.prop('disabled', true);
		for (let grad in gradJson[$gradSchoolPref.val()][$gradSchoolName.val()]) {
			let options = '<option value="' + grad + '">' + grad + '</option>';
			$gradSchoolOf.append(options);
		}
	}
	const setProgramIn = function () {
		$programIn.children('[value!=""]').remove();
		if ($gradSchoolOf.val() === '') {
			$programIn.prop('disabled', true);
			return;
		}
		$programIn.prop('disabled', false);
		for (let i = 0; i < gradJson[$gradSchoolPref.val()][$gradSchoolName.val()][$gradSchoolOf.val()].length; i++) {
			let grad = gradJson[$gradSchoolPref.val()][$gradSchoolName.val()][$gradSchoolOf.val()][i];
			let options = '<option value="' + grad + '">' + grad + '</option>';
			$programIn.append(options);
		}
	}
	const initGradSchoolName = function () {
		setGradSchoolName();
		$gradSchoolName.children('[value="' + $hiddenGradSchoolName.val() + '"]').prop('selected', true);
	}
	const initGradSchoolOf = function () {
		setGradSchoolOf();
		$gradSchoolOf.children('[value="' + $hiddenGradSchoolOf.val() + '"]').prop('selected', true);
	}
	const initProgramIn = function () {
		setProgramIn();
		$programIn.children('[value="' + $hiddenProgramIn.val() + '"]').prop('selected', true);
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
	prepJSON('/js/university.json');
	initUnivName();
	initFaculty();
	initDepartment();
	initGradSchool();
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
	$gradSchoolPref.on('input', function (event) {
		event.preventDefault();
		setGradSchoolName();
	});
	$gradSchoolName.on('input', function (event) {
		event.preventDefault();
		setGradSchoolOf();
	});
	$gradSchoolOf.on('input', function (event) {
		event.preventDefault();
		setProgramIn();
	});
	$('input[name="academicDegree"]').change(function () {
		initGradSchool();
	})
});